<template>
	<view class="body">
		<view class="nav row_align_center" id="nav">
			<li class="li_7" style="z-index: 999;font-size: 50rpx;" :class="['iconfont icon-zuojiantou back']" @click="gotoBack()">
			</li>
		</view>
		<view class="head">
			<view class="title">运动报告</view>
			<view class="customer_img">
				<!-- <open-data type="userAvatarUrl" class="img"></open-data> -->
				<image class="img" src="https://s1.ax1x.com/2022/11/16/zZUoK1.jpg" mode="widthFix"></image>
			</view>
		</view>
		<view class="score_view">
			<view class="title">本次评分</view>
			<view class="detail">
				<view class="socre">98</view>
				<li class="li_7" :class="['iconfont icon-up text-green']" @click="gotoBack()"></li>
				<view class="up_socre">0.4</view>
			</view>
		</view>
		<view class="census_view">
			<view class="left">
				<view class="text_gray small_text">消耗(千卡)</view>
				<view class="middle_text text_wide_900">165</view>
			</view>
			<view class="right">
				<view class="text_gray small_text">心率主要集中在</view>
				<view style="font-size: 30rpx;">燃烧脂肪</view>
			</view>
		</view>
		<view class="census_view">
			<view class="left">
				<view class="text_gray small_text">时长(分钟)</view>
				<view class="middle_text text_wide_900">75</view>
			</view>
			<view class="center">
				<view class="text_gray small_text">累计打卡(天)</view>
				<view class="middle_text text_wide_900">24</view>
			</view>
			<view class="right">
				<view class="text_gray small_text">平均心率(次/分钟)</view>
				<view class="middle_text text_wide_900">98</view>
			</view>
		</view>
		<view class="consume_view">
			<view class="wrap"><span class="consume_tip">身体消耗</span></view>
			<li class="li_7" :class="['iconfont icon-niunai consume_icon']"></li>
			<view class="desc">
				<view class="small_text">约消耗</view>
				<view class="text_wide_900"><text class="left">1000</text><text class="right">毫升牛奶</text></view>
			</view>
		</view>
		<view class="heart_rate_view">
			<view class="left">
				<li class="li_7" :class="['iconfont icon-zhexiantu']"></li>
				<text class="title">心率变化曲线</text>
			</view>
			<view class="right text_gray">心率变化</view>
		</view>
		<view v-if="heartRateData.series" class="heart_rate_chart">
			<view class="charts-box">
				<qiun-data-charts type="tline" canvasId="sport_a" :canvas2d="isCanvas2d" :resshow="delayload"
					:opts="{ padding: [0, 20, 10, 0], legend: { position: 'top', lineHeight: 20 }, xAxis: { disableGrid: true, format: 'xAxisDemo3' }, yAxis: { data: [{ min: 0, max: 175 }], gridType: 'solid' }, dataLabel: false, dataPointShape: false }"
					:chartData="heartRateData" />
			</view>
		</view>
		<view class="title_view">
			<view class="left">
				<li class="li_7" :class="['iconfont icon-xinlv']"></li>
				<text class="title">心率区间</text>
			</view>
		</view>
		<view class="heart_rate_range">
			<view class="top">
				<view class="item" v-for="(item, index) in heatRateRange" :key="index">
					<view class="name text_gray">{{ item.name }}</view>
					<view class="data">{{ item.type }}{{ item.data }}<text class="unit text_gray">分钟</text></view>
				</view>
			</view>
			<view v-if="heartRateRangeData" class="charts-box">
				<qiun-data-charts type="ring" canvasId="sport_b" :canvas2d="isCanvas2d" :resshow="delayload"
					:opts="{ legend: { position: 'bottom' }, extra: { ring: { border: false, centerColor: '#312C34' } }, title: { name: '' }, subtitle: { name: '' } }"
					:chartData="heartRateRangeData" />
			</view>
		</view>
		<view class="title_view">
			<view class="left">
				<li class="li_7" :class="['iconfont icon-pie']"></li>
				<text class="title">运动分析</text>
			</view>
		</view>
		<view class="sport_analysis_view">
			<view class="top">
				<li class="iconfont icon-feiji li_7"></li>
				<text class="title font-s-34">跑步机</text>
			</view>
			<view class="middle">
				<view class="left">
					<li class="iconfont icon-clock li_7"></li>
					<view>3.24P.M</view>
				</view>
				<view class="right">
					<li class="iconfont icon-huo li_7"></li>
					<view>热量消耗(千卡)</view>
				</view>
			</view>
			<view class="bottom">
				<view class="left">
					<li class="iconfont icon-kongxinyuan li_7"></li>
					<view>燃烧脂肪</view>
				</view>
				<view class="right">
					<view class="text_wide_600 font-s-40">637</view>
				</view>
			</view>
		</view>
		<view class="box_view speed_rank_view">
			<view class="top">
				<view class="item" v-for="(item, index) in speedRank" :key="index">
					<view class="name text_gray">{{ item.name }}</view>
					<view class="data">{{ item.data }}<text class="unit text_gray">{{ item.unit }}</text></view>
				</view>
			</view>
			<view v-if="speedRankData" class="charts-box">
				<qiun-data-charts type="bar" canvasId="sport_c" :canvas2d="isCanvas2d" :resshow="delayload"
					:chartData="speedRankData" background="none"
					:opts="{ xAxis: { disabled: true, disableGrid: true }, extra: { bar: { barBorderCircle: true, width: 20 } }, legend: { show: false } }" />
			</view>
		</view>
		<view class="box_view">
			<view v-if="speedAndRateData.series" class="charts-box">
				<qiun-data-charts type="tline" canvasId="sport_d" :canvas2d="isCanvas2d" :resshow="delayload"
					:opts="{ padding: [0, 20, 10, 0], legend: { position: 'top', lineHeight: 20 }, xAxis: { disableGrid: true, format: 'xAxisDemo3' }, yAxis: { data: [{ position: 'left', min: 0, max: 25 }, { position: 'right', min: 50, max: 175 }], gridType: 'solid' }, dataLabel: false, dataPointShape: false }"
					:chartData="speedAndRateData" />
			</view>
		</view>
	</view>
</template>

<script>
import heartRateData from "../../static/json/sport/1.json"
import heartRateRangeData from "../../static/json/sport/2.json"
import speedRankData from "../../static/json/sport/3.json"
import speedAndRateData from "../../static/json/sport/4.json"
import Config from '../../static/js/config'
import Common from '../../static/js/common'

export default {
	components: {

	},
	data() {
		return {
			info: '大便超人', //用户数据
			isCanvas2d: Config.ISCANVAS2D,
			heartRateData: {},
			speedRankData: {},
			speedAndRateData: {},
			delayload: null,
			heartRateRangeData: {},
			heatRateRange: [{
				name: "激活放松",
				data: "5",
				type: "≤"
			},
			{
				name: "动态热身",
				data: "13",
				type: ""
			},
			{
				name: "脂肪燃烧",
				data: "24",
				type: ""
			},
			{
				name: "糖分消耗",
				data: "8",
				type: "≤"
			},
			{
				name: "心肺训练",
				data: "7",
				type: ""
			},
			{
				name: "极限锻炼",
				data: "16",
				type: ""
			},
			],
			speedRank: [{
				name: "距离",
				data: "5",
				unit: "公里"
			},
			{
				name: "时长",
				data: "12",
				unit: "分钟"
			},
			{
				name: "平均配速",
				data: "6\'05\"",
				unit: ""
			}
			]
		};
	},
	watch: {

	},
	methods: {
		async getData() {
			uni.showLoading();
			/*将钟点时间随机转成某一天的具体时间戳*/
			if (typeof heartRateData.series[0].data[0][0] == 'string') {
				for (let i = 0; i < heartRateData.series.length; i++) {
					heartRateData.series[i].data.map(x => {
						x[0] = "2018/08/08 " + x[0];
						x[0] = this.tranTimestamp(x[0]);
						return x[0];
					})
				}

			}
			if (typeof speedAndRateData.series[0].data[0][0] == 'string') {
				for (let i = 0; i < speedAndRateData.series.length; i++) {
					speedAndRateData.series[i].data.map(x => {
						x[0] = "2018/08/08 " + x[0];
						x[0] = this.tranTimestamp(x[0]);
						return x[0];
					})
				}
			}

			this.heartRateData = heartRateData;
			this.heartRateRangeData = heartRateRangeData;
			this.speedRankData = speedRankData;
			this.speedAndRateData = speedAndRateData;
			this.delayload = true;
			uni.hideLoading();
		},
		tranTimestamp(date) {
			return new Date(date).getTime()
		},
		gotoBack() {
			Common.navigateBack("/index/index");
		},

	},
	onReady() {
		//#ifndef H5
		uni.showShareMenu();
		//#endif
		this.getData()
	}
}
</script>

<style scoped lang="scss">
.body {
	height: 100%;
	background-color: #1C191F;
	margin: 0;
	color: #fff;
	padding: 80rpx 20rpx 0 20rpx;
	width: 100%;
	box-sizing: border-box;
	padding-bottom: 50rpx;

	.box_view {
		width: 100%;
		padding: 20rpx;
		position: relative;
		background-color: #312C34;
		color: #FFFFFF;
		box-sizing: border-box;
		border-radius: 20rpx;
		overflow: hidden;
		margin-top: 30rpx;
	}

	.speed_rank_view {
		.top {
			width: 100%;

			&:after {
				content: "";
				clear: both;
				display: block;
			}

			.item {
				float: left;
				width: 33%;
				box-sizing: border-box;
				padding: 30rpx 20rpx;
				text-align: left;

				.name {
					font-size: 26rpx;
				}

				.data {
					font-size: 40rpx;
					margin-top: 10rpx;

					.unit {
						font-size: 24rpx;
						margin-left: 14rpx;
					}
				}
			}
		}
	}

	.sport_analysis_view {
		width: 100%;
		padding: 20rpx;
		position: relative;
		background-color: #312C34;
		color: #FFFFFF;
		box-sizing: border-box;
		border-radius: 20rpx;
		overflow: hidden;

		.top {
			width: 100%;
			height: 120rpx;
			display: flex;
			align-items: center;

			.icon-feiji {
				margin-top: 10rpx;
			}

			.title {
				margin-left: 10rpx;
			}
		}

		.middle {
			width: 100%;
			display: flex;
			justify-content: space-between;
			font-size: 28rpx;

			.iconfont {
				font-size: 28rpx;
				margin-right: 10rpx;
				margin-top: 4rpx;
			}

			.left {
				width: 50%;
				height: 60rpx;
				display: flex;
				align-items: center;
				justify-content: flex-start;
			}

			.right {
				width: 50%;
				height: 60rpx;
				display: flex;
				align-items: center;
				justify-content: flex-end;
			}
		}

		.bottom {
			width: 100%;
			display: flex;
			justify-content: space-between;
			font-size: 28rpx;

			.iconfont {
				font-size: 28rpx;
				margin-right: 10rpx;
				margin-top: 4rpx;
			}

			.left {
				width: 50%;
				height: 60rpx;
				display: flex;
				align-items: center;
				justify-content: flex-start;

				.icon-kongxinyuan {
					color: #6FCEF7;
				}
			}

			.right {
				width: 50%;
				height: 60rpx;
				display: flex;
				align-items: center;
				justify-content: flex-end;
			}
		}
	}

	.heart_rate_range {
		width: 100%;
		position: relative;
		background-color: #312C34;
		color: #FFFFFF;
		box-sizing: border-box;
		border-radius: 20rpx;
		overflow: hidden;

		.top {
			width: 100%;

			&:after {
				content: "";
				clear: both;
				display: block;
			}

			.item {
				float: left;
				width: 33%;
				box-sizing: border-box;
				padding: 30rpx 20rpx;
				text-align: center;

				.name {
					font-size: 26rpx;
				}

				.data {
					font-size: 40rpx;
					margin-top: 10rpx;

					.unit {
						font-size: 24rpx;
						margin-left: 14rpx;
					}
				}
			}
		}
	}

	.heart_rate_chart {
		display: flex;
		justify-content: center;
		width: 100%;
		position: relative;
		background-color: #312C34;
		color: #FFFFFF;
		box-sizing: border-box;
		border-radius: 20rpx;
		overflow: hidden;
	}

	.title_view {
		display: flex;
		width: 100%;
		align-items: center;
		height: 150rpx;

		.left {
			display: flex;
			align-items: center;

			.iconfont {
				font-size: 40rpx !important;
			}

			.title {
				font-size: 34rpx;
				margin-left: 20rpx;
			}
		}
	}

	.heart_rate_view {
		display: flex;
		width: 100%;
		justify-content: space-around;
		align-items: center;
		height: 150rpx;

		.left {
			display: flex;
			align-items: center;

			.icon-zhexiantu {
				font-size: 26rpx;
			}

			.title {
				font-size: 34rpx;
				margin-left: 20rpx;
			}
		}

		.right {
			font-size: 22rpx;
			padding: 10rpx 30rpx;
			border-radius: 40rpx;
			background-color: #342E39;
		}
	}

	.consume_view {
		display: flex;
		justify-content: center;
		width: 100%;
		height: 190rpx;
		position: relative;
		margin-top: 100rpx;
		background-color: #312C34;
		color: #FFFFFF;
		box-sizing: border-box;
		border-radius: 10rpx;

		.consume_img {
			width: 240rpx;
			height: auto;
			position: absolute;
			top: -80rpx;
			left: 20rpx;
		}

		.consume_icon {
			font-size: 220rpx;
			position: absolute;
			top: -80rpx;
			left: 20rpx;
		}

		.desc {
			position: absolute;
			right: 80rpx;
			top: 20rpx;

			view {
				padding: 10rpx 0;
				display: flex;
				align-items: center;
			}

			.left {
				font-size: 50rpx;
			}

			.right {
				font-size: 30rpx;
				margin-left: 10rpx;
				font-weight: 400;
			}
		}
	}

	.text-green {
		color: #10A764;
	}

	.text_gray {
		color: #8E8B8B;
	}

	.small_text {
		font-size: 24rpx;
	}

	.font-s-34 {
		font-size: 34rpx;
	}

	.font-s-36 {
		font-size: 36rpx;
	}

	.font-s-38 {
		font-size: 38rpx;
	}

	.font-s-40 {
		font-size: 40rpx;
	}

	.middle_text {
		font-size: 36rpx;
	}

	.text_wide_900 {
		font-weight: 900;
	}

	.text_wide_600 {
		font-weight: 600;
	}

	.census_view {
		width: 100%;
		display: flex;
		justify-content: space-around;

		.left {
			text-align: left;

			view {
				padding: 10rpx 0;
			}
		}

		.center {
			text-align: center;

			view {
				padding: 10rpx 0;
			}
		}

		.right {
			text-align: right;

			view {
				padding: 10rpx 0;
			}
		}
	}

	.score_view {
		width: 100%;

		.title {
			color: #8E8B8B;
			font-size: 24rpx;
		}

		.detail {
			height: 120rpx;
			width: 100%;
			display: flex;
			align-items: flex-end;

			.icon-up {
				margin-left: 40rpx;
				height: 54rpx;
				font-weight: 600;
			}

			.socre {
				font-size: 80rpx;
				font-weight: 900;
			}

			.up_socre {
				color: #10A764;
				height: 50rpx;
				font-size: 24rpx;
				font-weight: 600;
			}
		}
	}

	.head {
		height: 140rpx;
		line-height: 140rpx;
		position: relative;

		.title {
			font-size: 40rpx;
			margin-left: 20rpx;
		}

		.customer_img {
			position: absolute;
			bottom: 0rpx;
			right: 20rpx;
			width: 100rpx;
			height: 100rpx;
			margin: 0;
			padding: 0;
			background-size: 100% 100%;
			border-radius: 100%;
			overflow: hidden;

			.img {
				height: auto;
				width: 100%;
			}
		}
	}

	.li_7 {
		list-style-type: none;
	}

	.nav {
		position: fixed;
		top: 50rpx;
		left: 20rpx;
	}
}

.consume_view:nth-child(even) {
	margin-right: 4%;
}

.consume_tip {
	display: inline-block;
	text-align: center;
	width: 188rpx;
	height: 30rpx;
	line-height: 30rpx;
	position: absolute;
	top: 36rpx;
	right: -44rpx;
	z-index: 2;
	overflow: hidden;
	transform: rotate(45deg);
	-ms-transform: rotate(45deg);
	-moz-transform: rotate(45deg);
	-webkit-transform: rotate(45deg);
	-o-transform: rotate(45deg);
	border: 1px dashed;
	box-shadow: 0 0 0 3px #10A764, 0px 21px 5px -18px rgba(0, 0, 0, 0.6);
	background: #10A764;
	font-size: 16rpx;
}

.wrap {
	width: 100%;
	height: 100%;
	position: absolute;
	top: -12rpx;
	left: 12rpx;
	overflow: hidden;
}

.wrap:before {
	content: "";
	display: block;
	border-radius: 8px 8px 0px 0px;
	width: 80rpx;
	height: 14rpx;
	position: absolute;
	right: 68rpx;
	top: -1px;
	background: #4D6530;
}

.wrap:after {
	content: "";
	display: block;
	border-radius: 0px 8px 8px 0px;
	width: 14rpx;
	height: 80rpx;
	position: absolute;
	right: -1px;
	top: 66rpx;
	background: #4D6530;
}</style>
