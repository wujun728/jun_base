<template>
	<view class="nav" v-if="!loading">
		<uni-nav-bar :fixed="true" left-icon="left" status-bar backgroundColor="rgba(119,176,247,1)" color="#fff"
			:border="false" @clickLeft="cancel">
			<view class="nav-content">
				<view class="title">团队月报</view>
			</view>
			<block v-slot:right>
				<view class="bar-right">
					<picker ref="picker" mode="date" fields="month" :value="monthTime" start="2021-01" :end="lastTime"
						@change="bindPickerChange">
						<view class="dis-flex">
							<text class="uni-nav-bar-text">
								<text class="month">{{ monthTime.split("-")[1] }}月</text>
							</text>
							<uni-icons type="arrowdown" color="#fff" size="18" />
						</view>
					</picker>
				</view>
			</block>
		</uni-nav-bar>
		<view class="card">
			<view class="header">
				<view class="title">上下班打卡统计</view>
				<view class="jump" @click="jump(1)">
					查看明细
					<text class="iconfont icon-jinru-copy"></text>
				</view>
			</view>
			<view class="schedule">
				<progressBox :normal="clockStatistics.normal || 0" :abnormal="clockStatistics.abnormal || 0"
					:total="clockStatistics.total || 0"></progressBox>
				<view class="sta-data">
					<view class="sta-data-item">
						<view class="num">{{ clockStatistics.work_hours }}</view>
						<view class="text">平均工时(h)</view>
					</view>
					<view class="sta-data-item" @click="goDetails(2,'迟到')">
						<view class="num c1">{{ clockStatistics.late }}</view>
						<view class="text">迟到(次)</view>
					</view>
					<view class="sta-data-item" @click="goDetails(4,'早退')">
						<view class="num c3">{{ clockStatistics.leave_early }}</view>
						<view class="text">早退(次)</view>
					</view>
				</view>
				<view class="sta-data">
					<view class="sta-data-item" @click="goDetails(1,'地点异常')">
						<view class="num c4">{{ clockStatistics.location_abnormal }}</view>
						<view class="text">地点异常(次)</view>
					</view>
					<view class="sta-data-item" @click="goDetails(5,'缺卡')">
						<view class="num c3">{{ clockStatistics.lack_card }}</view>
						<view class="text">缺卡(次)</view>
					</view>
					<view class="sta-data-item" @click="goDetails(6,'旷工')">
						<view class="num c3">{{ clockStatistics.absenteeism }}</view>
						<view class="text">旷工(天)</view>
					</view>
				</view>
			</view>
		</view>
		<view class="card pb48">
			<view class="header" @click="jump(3)">
				<view class="title">
					加班统计
					<text class="gard">(人)</text>
				</view>
				<view class="jump">
					<text class="iconfont icon-jinru-copy"></text>
				</view>
			</view>
			<view class="statistics schedule">
				<view class="sta-data">
					<view class="sta-data-item" @click="overTimeFn(1,'工作日加班')">
						<view class="num">{{ overtimeStatistics.work }}</view>
						<view class="text">工作日加班</view>
					</view>
					<view class="sta-data-item" @click="overTimeFn(2,'休息日加班')">
						<view class="num">{{ overtimeStatistics.rest }}</view>
						<view class="text">休息日加班</view>
					</view>
					<view class="sta-data-item" @click="overTimeFn(3,'节假日加班')">
						<view class="num">{{ overtimeStatistics.holiday }}</view>
						<view class="text">节假日加班</view>
					</view>
				</view>
			</view>
		</view>
		<view class="card pb48">
			<view class="header" @click="jump(2)">
				<view class="title">
					假勤统计
					<text class="gard">(人)</text>
				</view>
				<view class="jump">
					<text class="iconfont icon-jinru-copy"></text>
				</view>
			</view>
			<view class="statistics schedule">
				<view class="sta-data" v-for="(item, index) in leaveStatistics" :key="index">
					<view class="sta-data-item" v-for="(e, i) in item" :key="i">
						<view @click="jumpFake(e.status, e.name)" class="num">{{ e.num }}</view>
						<view @click="jumpFake(e.status, e.name)" class="text">{{ e.name }}</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import defaultNavBar from "@/components/defaultNavBar/index";
	import progressBox from "./components/progress.vue";
	import {
		monthStatistics
	} from "@/api/attendance";
	let monthTime = ref(`${new Date().getFullYear()}-${new Date().getMonth() + 1}`); //当年月
	let lastTime = ref(`${new Date().getFullYear()}-${new Date().getMonth() + 1}`);
	const loading = ref(true);
	onLoad((options) => {
		monthTime.value = options.yearValue + '-' + options.monthValue
		getMonthStatistics();
	});
	const goDetails = (val, text) => {
		uni.navigateTo({
			url: `/pages/attendance/detailed/teamCheckList?type=1&date=${monthTime.value}&status=${val}&text=${text}`,
		});
	}
	const overTimeFn = (val, text) => {
		uni.navigateTo({
			url: `/pages/attendance/detailed/teamCheckList?type=3&date=${monthTime.value}&status=${val}&text=${text}`,
		});
	}
	const jump = (type) => {
		uni.navigateTo({
			url: `/pages/attendance/detailed/teamCheckList?type=${type}&date=${monthTime.value}`,
		});
	};
	const jumpFake = (val, text) => {
		uni.navigateTo({
			url: `/pages/attendance/detailed/teamCheckList?type=2&date=${monthTime.value}&status=${val}&text=${text}`,
		});
	}
	const clockStatistics = ref();
	const overtimeStatistics = ref();
	const leaveStatistics = ref([]);
	const getMonthStatistics = () => {
		let data = {
			date: monthTime.value,
			type: 0,
		};
		monthStatistics(data).then((res) => {
			let data = res.data;
			clockStatistics.value = data.clock_statistics;
			overtimeStatistics.value = data.overtime_statistics;
			let array = data.leave_statistics;
			leaveStatistics.value = Array.from({
					length: Math.ceil(array.length / 3)
				},
				(_, index) => array.slice(index * 3, (index + 1) * 3)
			);
			loading.value = false;
		});
	};
	// 月份选择
	const bindPickerChange = (e) => {
		monthTime.value = e.detail.value;
		getMonthStatistics();
	};
	const cancel = () => {
		uni.navigateTo({
			url: '/pages/attendance/statistics'
		})
	};
</script>

<style lang="scss" scoped>
	.nav {
		background-size: 100% 240rpx;
		padding-bottom: 40rpx;
	}

	.nav-content {
		display: flex;
		justify-content: space-around;
		align-items: center;
		width: 100%;

		.title {
			font-size: 34rpx;
			font-weight: 500;
			color: rgba(255, 255, 255);
			line-height: 34rpx;
		}
	}

	.bar-right {
		/* #ifndef APP-PLUS-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
		margin-left: 4px;

		.dis-flex {
			display: flex;
			align-items: center;
		}
	}

	.card {
		margin: 20rpx 20rpx 0rpx;
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

				.gard {
					font-size: 26rpx;
					font-weight: 500;
					color: #909399;
					line-height: 30rpx;
				}
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

		.schedule.statistics {
			padding: 0 48rpx 0rpx 48rpx;

			.sta-data {
				margin-top: 48rpx;
				padding: 0;
				margin-bottom: 0;
			}
		}

		.schedule {
			padding: 30rpx 0;

			.sta-data {
				display: flex;
				justify-content: space-between;
				padding: 36rpx 36rpx 0;
				margin-bottom: 36rpx;
				/* 如果最后一行是3个元素 */
				.sta-data-item:last-child:nth-child(3n - 1) {
					margin-right: calc(32.5% + 128rpx / 3);
				}

				/* 如果最后一行是2个元素 */
				.sta-data-item:last-child:nth-child(3n - 2) {
					margin-right: calc(65% + 128rpx / 3);
				}

				.sta-data-item {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: center;
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

					.c4 {
						color: #19BE6B;
					}

					.text {
						font-size: 28rpx;
						font-weight: 400;
						color: #909399;
						line-height: 28rpx;
						min-width: 4rem;
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