<template>
	<view class="body window">
		<view class="topLine" :style="{ height: topBar + 'px' }"></view>
		<view class="nav row_align_center" id="nav">
			<li class="li_6" :class="['iconfont icon-zuojiantou back']" @click="gotoBack()"></li>
			<text class="title">{{ title ? title : '' }}</text>
			<li class="iconfont icon-zuojiantou back hidden li_6"></li>
		</view>
		<view class="data_body">
			<scroll-view class="scroll_list" scroll-y :style="{ height: scrollHeight }">
				<!-- 教学科研情况 -->
				<view class="view_block">
					<view class="title">教学科研情况</view>
					<progress-bar :content="RankData" @updateRanking="updateRanking"></progress-bar>
				</view>
				<!-- 学历分布状况 -->
				<view class="view_block">
					<view class="title">学历分布状况
						<text class="font-small" style="color: #ccc;">(教职工)</text>
					</view>
					<view class="charts-box" style="height: 300px;">
						<qiun-data-charts type="rose" :chartData="ProductRateData" canvasId="school_a"
							:canvas2d="isCanvas2d" :resshow="delayload" />
					</view>
				</view>
				<!-- 学业成绩 -->
				<view class="view_block">
					<view class="title">学业成绩分布图
						<text class="font-small" style="color: #ccc;">(班级)</text>
					</view>
					<view class="charts-box" style="height: 300px;">
						<qiun-data-charts type="radar" :chartData="RadarModel" background="none" canvasId="school_b"
							:animation="false" :canvas2d="isCanvas2d" :resshow="delayload" />
					</view>
				</view>
				<!-- 图书借阅情况 -->
				<view class="view_block">
					<view class="title">图书借阅情况</view>
					<view class="charts-box" style="height: 300px;">
						<qiun-data-charts type="line" canvasId="school_c" :canvas2d="isCanvas2d" :resshow="delayload"
							:ontouch="true"
							:opts="{ enableScroll: true, xAxis: { scrollShow: true, itemCount: 4, disableGrid: true }, series: { style: 'curve' } }"
							:chartData="friendTrand" />
					</view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<script>
import ProgressBar from "../../components/progress-bar/progress-bar.vue"
import RankData from '../../static/json/school/1.json';
import ProductRateData from '../../static/json/school/2.json';
import RadarModel from '../../static/json/school/3.json';
import friendTrand from '../../static/json/school/4.json';
import Config from '../../static/js/config'
import Common from '../../static/js/common'

export default {
	components: {
		ProgressBar
	},
	data() {
		return {
			info: "大便超人", //用户数据
			title: "智慧教育报表中心", //标题
			showDataTime: "today", //选中的时间
			tabCur: 0, //标签头下标
			topBar: 17, //导航高
			top: '180', //下拉栏位置
			scrollHeight: "1400rpx", //数据展示体高度
			friendTrand,
			RankData,
			ProductRateData,
			RadarModel,
			isCanvas2d: Config.ISCANVAS2D,
			delayload: false, //延时加载图表，否则会出现图表加载完后定位错乱
		};
	},
	computed: {
		locationArray() {
			return [{ value: "GDBJ-ENTRY-1", text: "天猫" }, { value: "GDBJ-ENTRY-201", text: "京东" }];
		}
	},
	methods: {
		async getData() {
			uni.showLoading();
			await setTimeout(() => {
				this.delayload = true;
				uni.hideLoading();
			}, 1000)
		},
		gotoBack() {
			Common.navigateBack("/index/index");
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
			query.exec(res => {
				res.map((item, index) => {
					hasHeight += item.height;
				})
				this.scrollHeight = (telephoneInfo.screenHeight - hasHeight - this.topBar) + 'px';
			})
		},
		updateRanking(nVal) {
			this.RankData = nVal;
		},
	},
	onLoad() {
		//#ifndef H5
		uni.showShareMenu();
		//#endif
		this.getData()
		this.getTelephoneInfo();
	}
}
</script>

<style scoped lang="scss">
.body {
	height: 100vh;
	margin: 0;
	padding: 0 20rpx;
	font-family: "montserrat";
	background-image: linear-gradient(125deg, #CB9FFE, #5894F7, #ABCDFA, #74A3F6, #CB9FFE);
	background-size: 400%;
	animation: bganimation 15s infinite;
}

.li_6 {
	list-style-type: none;
}

page,
body {
	margin: 0;
	padding: 0;
	width: 100%;
	height: 100%;
}

.window {
	height: 100vh;
	overflow: hidden;

	.topLine {
		width: 100%;
	}

	scroll-view {
		box-sizing: border-box;
	}

	.swiper {
		box-sizing: border-box;
	}

	.nav {
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
		height: 100%;

		.scroll_list {
			height: 100%;

			.view_block {
				background-color: #fff;
				padding: 16rpx 20rpx 10rpx 20rpx;
				border-radius: 20rpx;
				margin-bottom: 40rpx;

				.title {
					text-align: left;
					margin-bottom: 30rpx;
					font-size: 30rpx;
				}

				.trend_title {
					text-align: right;
					font-size: 22rpx;
					color: #ff9900;
					margin-top: 50rpx;
				}
			}
		}
	}
}

@keyframes bganimation {
	0% {
		background-position: 0% 50%;
	}

	50% {
		background-position: 100% 50%;
	}

	100% {
		background-position: 0% 50%;
	}
}
</style>
