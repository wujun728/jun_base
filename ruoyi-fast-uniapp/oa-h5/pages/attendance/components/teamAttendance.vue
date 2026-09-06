<template>
	<view class="nav">
		<view class="date-list">
			<view class="date">
				<view class="time">{{ yearValue ? yearValue : nowYear }}年{{monthValue ? monthValue : nowMonth }}月</view>
				<navigator class="jump"
					:url="`/pages/attendance/teamReport?yearValue=${yearValue || nowYear}&monthValue=${monthValue || nowMonth}`"
					open-type="navigate" hover-class="navigator-hover">
					团队月报
					<text class="iconfont icon-jinru-copy"></text>
				</navigator>
			</view>
			<view class="list">
				<calendar :dateList="dateList" @getMonthReport="getMonthReport" @enter="changeDate"
					@changeMonth="changeMonth"></calendar>
			</view>
		</view>
		<view class="notice">
			<text class="iconfont icon-yemiantishi"></text>
			系统自动汇总请假、补卡、外出、出差相关数据至考勤统计
		</view>
		<view class="card">
			<view class="header">
				<view class="title">上下班打卡统计</view>
				<view class="jump" @click="jump">
					查看明细
					<text class="iconfont icon-jinru-copy"></text>
				</view>
			</view>
			<view class="schedule">
				<progressBox :normal="staData.normal" :abnormal="staData.abnormal" :total="staData.total"></progressBox>
				<view class="sta-data">
					<view class="sta-data-item">
						<view class="num">{{ staData.work_hours || 0 }}</view>
						<view class="text">平均工时(h)</view>
					</view>
					<view class="sta-data-item" @click="goDetails(2,'迟到')">
						<view class="num c1">{{ staData.late || 0 }}</view>
						<view class="text">迟到(次)</view>
					</view>
					<view class="sta-data-item" @click="goDetails(4,'早退')">
						<view class="num c2">{{ staData.leave_early || 0 }}</view>
						<view class="text">早退(次)</view>
					</view>
					<view class="sta-data-item" @click="goDetails(5,'缺卡')">
						<view class="num c3">{{ staData.lack_card || 0 }}</view>
						<view class="text">缺卡(次)</view>
					</view>
				</view>
				<!-- <view class="stop-time">统计截止{{ staData.deadline || "" }}</view> -->
			</view>
		</view>
		<cardList title="外勤卡统计" :list="dataList"></cardList>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		toRefs,
		onMounted
	} from "vue";
	import progressBox from "./progress.vue";
	import cardList from "./cardList.vue";
	import calendar from "./calendar.vue";
	import {
		monthReport,
		teamStatistics,
		externalStatistics,
	} from "@/api/attendance";

	const dateList = ref([]);
	let nowDate = ref(
		`${new Date().getFullYear()}-${
    new Date().getMonth() + 1
  }-${new Date().getDate()}`
	);
	const staData = ref({});
	const dataList = reactive([]);
	onMounted(() => {
		getMonthReport();
		getTeamStatistics();
		getExternalStatistics();
	});
	let monthTime = ref(`${new Date().getFullYear()}-${new Date().getMonth() + 1}`);
	let nowYear = ref(new Date().getFullYear());
	let nowMonth = ref(new Date().getMonth() + 1);
	let nowDay = ref();
	let mxYear = ref(new Date().getFullYear());
	let mxMonth = ref(new Date().getMonth() + 1);
	let mxDate = ref(new Date().getDate());
	let yearValue = ref();
	let monthValue = ref();

	const changeMonth = (year, month) => {
		yearValue.value = year;
		monthValue.value = month;
	}
	const getMonthReport = (val) => {
		let data = {
			date: val ? val : monthTime.value,
			type: 0,
		};
		monthReport(data).then((res) => {
			dateList.value = res.data;
		});
	};
	const getExternalStatistics = () => {
		let data = {
			date: nowDate.value,
		};
		externalStatistics(data).then((res) => {
			dataList.value = res.data;
		});
	};
	const changeDate = (date, d) => {
		nowYear.value = d.fullYear;
		nowMonth.value = d.month;
		nowDay.value = d.day;
		nowDate.value = `${d.fullYear}-${d.month}-${d.day}`;
		getTeamStatistics();
		getExternalStatistics();
	};
	const getTeamStatistics = () => {
		let data = {
			date: nowDate.value,
		};
		teamStatistics(data).then((res) => {
			staData.value = res.data;
		});
	};
	const goDetails = (val, text) => {
		mxYear.value = nowYear.value ? nowYear.value : mxYear.value
		mxMonth.value = nowMonth.value ? nowMonth.value : mxMonth.value
		mxDate.value = nowDay.value ? nowDay.value : mxDate.value
		uni.navigateTo({
			url: `/pages/attendance/detailed/teamCheckList?type=0&mxYear=${mxYear.value}&mxMonth=${mxMonth.value}&mxDate=${mxDate.value}&status=${val}&text=${text}`,
		});

	}
	const jump = () => {
		mxYear.value = nowYear.value ? nowYear.value : mxYear.value
		mxMonth.value = nowMonth.value ? nowMonth.value : mxMonth.value
		mxDate.value = nowDay.value ? nowDay.value : mxDate.value
		uni.navigateTo({
			url: `/pages/attendance/detailed/teamCheckList?type=0&mxYear=${mxYear.value}&mxMonth=${mxMonth.value}&mxDate=${mxDate.value}`,
		});
	};
</script>

<style lang="scss" scoped>
	.date-list {
		padding: 30rpx;
		background-color: #308bf8;

		.date {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 30rpx;

			.time {
				font-size: 28rpx;
				font-weight: 400;
				color: #ffffff;
			}

			.jump {
				font-size: 24rpx;
				font-weight: 400;
				color: #ffffff;
				line-height: 24rpx;
				background: rgba(255, 255, 255, 0.2);
				border-radius: 22rpx 22rpx 22rpx 22rpx;
				padding: 10rpx 18rpx;
				display: flex;
				align-items: center;

				.icon-jinru-copy {
					font-size: 20rpx;
					line-height: 24rpx;
					transform: scale(0.8);
				}
			}
		}
	}

	.notice {
		display: flex;
		align-items: center;
		color: #308bf8;
		font-size: 24rpx;
		font-weight: 400;
		color: #308bf8;
		line-height: 36rpx;
		padding: 30rpx 10rpx 30rpx 42rpx;
		background-color: #f2f6fc;

		.icon-yemiantishi {
			margin-right: 12rpx;
		}
	}

	.card {
		margin: 0rpx 20rpx 0rpx;
		background-color: #fff;
		border-radius: 12rpx;

		.header {
			display: flex;
			justify-content: space-between;
			padding: 30rpx 24rpx;
			border-bottom: 1rpx solid #ebeef5;

			.title {
				font-size: 30rpx;
				font-weight: 500;
				color: #303133;
				line-height: 30rpx;
			}

			.jump {
				display: flex;
				align-items: center;
				font-size: 24rpx;
				font-family: PingFang SC-Regular, PingFang SC;
				font-weight: 400;
				color: #308bf8;
				line-height: 24rpx;

				.icon-jinru-copy {
					font-size: 20rpx;
					color: #c0c4cc;
					margin-left: 12rpx;
				}
			}
		}

		.schedule {
			padding: 30rpx 0;

			.sta-data {
				display: flex;
				justify-content: space-between;
				padding: 36rpx 36rpx 0;

				.sta-data-item {
					text-align: center;

					.num {
						margin-bottom: 16rpx;
						font-size: 32rpx;
						font-weight: 500;
						color: #303133;
						line-height: 32rpx;
					}

					.c1 {
						color: #ff9900;
					}

					.c2 {
						color: #ff9900;
					}

					.c3 {
						color: #ed4014;
					}

					.text {
						font-size: 28rpx;
						font-weight: 400;
						color: #909399;
						line-height: 28rpx;
					}
				}
			}

			.stop-time {
				margin-top: 64rpx;
				font-size: 24rpx;
				font-weight: 400;
				color: #909399;
				line-height: 24rpx;
				text-align: center;
			}
		}
	}
</style>