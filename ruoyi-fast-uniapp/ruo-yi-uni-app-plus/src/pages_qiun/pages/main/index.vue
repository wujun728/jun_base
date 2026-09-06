<template>
	<view class="window">
		<!-- #ifndef H5 -->
		<view class="topLine" :style="{ height: topBar + 'px' }"></view>
		<!-- #endif -->
		<view class="nav row_align_center" id="nav">
			<li class="li_5" :class="['iconfont icon-zuojiantou back']" @click="gotoBack()"></li>
			<text class="title li_5">{{ title ? title : '' }}</text>
			<li class="iconfont icon-zuojiantou back hidden"></li>
		</view>
		<view class="row_align_center head" id="head">
			<!-- 日期下拉列表 -->
			<drop-down ref="caleDrop" @tap="changDrop(1)" @changeItem="changeTime" :list="timeArray" :contentTop="top"
				selectWidth="260rpx" contentLeft="0"></drop-down>
			<!-- 日历选择日期 -->
			<view v-if="showCalendar"
				:class="['dropdown-item__selected', listWidth != '150rpx' ? 'dropdown-item__right' : 'dropdown-item__left']"
				@click="openCalendar" class="calendar_drag">
				<view class="selected__name">{{ nowDate }}</view>
				<li class="iconfont icon-calendar" style="margin-left: 10rpx;"></li>
			</view>
			<!-- 公司区域下拉列表 -->
			<drop-down ref="companyDrop" @tap="changDrop(2)" @changeItem="changeLocation" :list="locationArray"
				:contentTop="top" contentRight="10" :selectWidth="showCalendar ? '200rpx' : '300rpx'" listWidth="75%">
			</drop-down>
		</view>
		<uni-calendar ref="calendar" :insert="false" :start-date="startDate" :end-date="endDate" :clearDate="false"
			@confirm="confirm">
		</uni-calendar>
		<!--滑动列表头-->
		<wuc-tab id="wuctab" :tab-list="tabList" :tabCur.sync="tabCur" tab-class="text-center text-white bg-blue"
			select-class="text-white"></wuc-tab>
		<!--主体内容-->
		<view class="data_body" :style="{ height: scrollHeight }">
			<view v-if="tabCur == 0">
				<wechat :scrollHeight="scrollHeight" />
			</view>
			<view v-else-if="tabCur == 1">
				<user-operate :scrollHeight="scrollHeight" />
			</view>
			<view v-else-if="tabCur == 2">
				<user-healthy :scrollHeight="scrollHeight" />
			</view>
			<view v-else-if="tabCur == 3">
				<user-server :scrollHeight="scrollHeight"></user-server>
			</view>
		</view>
		<!--水印-->
		<view class="water-mark-mask row_wrap" :style="{ height: scrollHeight }">
			<text class="container" v-for="(count, index) in 10" :key="index">{{ info.name }}</text>
		</view>
	</view>
</template>
<script>
import Wechat from "../../components/data-center/wechat.vue"
import UserOperate from "../../components/data-center/user-operate.vue"
import UserHealthy from "../../components/data-center/user-healthy.vue"
import UserServer from "../../components/data-center/user-server.vue"
import WucTab from '../../components/wuc-tab/wuc-tab.vue'
import DropDown from '../../components/drop-down/drop-down.vue'
import UniCalendar from '../../components/uni-calendar/uni-calendar.vue'

import Config from '../../static/js/config'
import Common from "../../static/js/common"

export default {
	components: {
		WucTab,
		DropDown,
		UniCalendar,
		Wechat,
		UserOperate,
		UserHealthy,
		UserServer,
	},
	data() {
		return {
			tabList: Config.TABLIST, //标签头
			timeArray: Config.TIMEARRAY, //时间数组
			info: '大便超人', //用户数据
			title: "数据报表中心", //标题
			showDataTime: "today", //选中的时间
			tabCur: 0, //标签头下标
			topBar: 17, //导航高
			top: '180', //下拉栏位置
			scrollHeight: "100%", //数据展示体高度
			nowDate: Common.getNowDate(), //现在日期
			endDate: Common.getNowDate(), //日历可选日期范围的结束时间
			startDate: Common.getPreMonth(Common.getNowDate()), //日历可选日期范围的开始时间,
			showCalendar: false,
		};
	},
	computed: {
		locationArray() {
			return [{
				value: "GDBJ-ENTRY-1",
				text: "天猫"
			}, {
				value: "GDBJ-ENTRY-201",
				text: "京东"
			}];
		}
	},
	methods: {
		gotoBack() {
			Common.navigateBack("/index/index");
		},
		changDrop(index) {
			if (index == 1 && this.$refs.companyDrop.showList) {
				this.$refs.companyDrop.closePopup()
			} else if (index == 2 && this.$refs.caleDrop.showList) {
				this.$refs.caleDrop.closePopup()
			}
		},
		// 打开日历
		openCalendar() {
			this.$refs.calendar.open();
		},
		// 日历选择日期
		confirm(e) {
			if (this.nowDate != e.fulldate || !this.showCalendar) {
				this.showCalendar = true;
				this.$refs.caleDrop.selectAuto();
				this.nowDate = e.fulldate;
				this.showDataTime = e.fulldate.replace(/-/g, "");
				Common.tipMsg("当前时间:" + this.showDataTime)
			}
		},
		//下拉选择时间
		changeTime(e) {
			if (e.value == "auto") {
				this.openCalendar();
			} else if (this.showDataTime != e.value) {
				this.showDataTime = e.value;
				this.showCalendar = false;
				Common.tipMsg("当前时间:" + this.showDataTime)
			}
		},
		changeLocation(e) {
			Common.tipMsg("当前选中平台:" + e.text)
		},
		//获取设备信息
		async getTelephoneInfo() {
			var telephoneInfo = await Common.getTelephoneInfo();
			let hasHeight = 0;
			if (telephoneInfo.top >= 40) {
				this.top = telephoneInfo.statusBarHeight * 2 + 150;
				this.topBar = telephoneInfo.statusBarHeight;
			}

			// 设置滚动高度
			const query = wx.createSelectorQuery();
			query.select('#nav').boundingClientRect();
			query.select('#head').boundingClientRect();
			query.select('#wuctab').boundingClientRect();
			query.exec(res => {
				res.map((item, index) => {
					hasHeight += item.height;
				})
				this.scrollHeight = (telephoneInfo.screenHeight - hasHeight - this.topBar) + 'px';
			})
		},
		getH5Info() {
			uni.getSystemInfo({
				success: e => {
					let hasHeight = 0;
					let element = wx.createSelectorQuery().in(this);
					element.select('#nav').boundingClientRect();
					element.select('#head').boundingClientRect();
					element.select('#wuctab').boundingClientRect();
					element.exec(res => {
						res.map((item, index) => {
							hasHeight += item.height;
						})
						this.scrollHeight = (e.screenHeight - hasHeight) + 'px';
					})
				},
				fail: (err) => {
					reject(err);
				}
			})
		}
	},
	onReady() {
		//#ifndef H5
		uni.showShareMenu();
		this.getTelephoneInfo();
		//#endif
		//#ifdef H5
		this.getH5Info();
		//#endif
	}
};
</script>
<style lang="scss">
page,
body {
	margin: 0;
	padding: 0;
	width: 100%;
	height: 100%;
}

.li_5 {
	list-style-type: none;
}

.window {
	height: 100vh;
	overflow: hidden;

	.topLine {
		background-color: #40A2ED;
		width: 100%;
	}

	scroll-view {
		box-sizing: border-box;
	}

	.swiper {
		box-sizing: border-box;
	}

	.nav {
		background-image: url(http://photo.gdbjyy.cn/image/BCAI/top_bg.jpg);
		background-size: 100% 100%;

		.back {
			font-size: 54rpx;
			padding: 20rpx 14rpx 15rpx 14rpx;
			color: #fff;
		}

		.title {
			color: #fff;
			font-size: 30rpx;
			flex: 1;
			text-align: center;
		}

		.hidden {
			visibility: hidden;
		}
	}

	.head {
		padding: 0 16rpx 14rpx 16rpx;
		color: #fff;
		background-color: #40A2ED;
		justify-content: space-between;
		font-size: 26rpx !important;

		.calendar_drag {
			width: 340rpx;
			display: flex;
			justify-content: center;
			align-items: center;

			.calendar_name {
				margin-right: 10rpx;
			}

			.icon-calendar {
				font-size: 26rpx;
				margin-top: 4rpx;
			}
		}
	}

	.data_body {
		overflow: auto;
		text-align: center;
		color: #333333;
		background-repeat: repeat;
		background-color: #ffffff;
		position: relative;

		.item {
			padding: 0 20rpx;
			margin-bottom: 20rpx;

			.name {
				font-weight: 600;
				font-size: 36rpx;
			}

			.operate {
				view {
					padding: 5rpx 12rpx;
					color: #fff;
				}

				.bg-blue {
					background-color: #40A2ED;
				}

				.bg-yellow {
					background-color: #FFC300;
				}
			}

			.tip {
				margin-bottom: 30rpx;

				.update {
					color: #C4100A;
					margin-left: auto;
					font-size: 30rpx;
				}
			}
		}

		.cry {
			font-size: 96rpx;
			margin-bottom: 10rpx;
		}
	}
}

.water-mark-mask {
	width: 100%;
	position: fixed;
	left: 0;
	bottom: 0;
	z-index: 1000;
	justify-content: space-between;
	pointer-events: none; //无视鼠标事件，相当于鼠标事件透传下去一样
	flex: 1;
	overflow: hidden;

	text {
		width: 50%;
		color: #909399;
		opacity: 0.25;
		transform: rotate(-15deg);
	}
}

.histogram {
	height: 100%;
	width: 100%;

	canvas {
		margin-top: 40rpx;
	}
}
</style>
