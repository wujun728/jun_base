<template>
	<view class="main" v-if="attendanceData">
		<!-- #ifdef APP-PLUS -->
		<uni-nav-bar :fixed="true" background-color="#fff" :border="false" status-bar left-icon="left" title="考勤规则"
			@clickLeft="back" />
		<!-- #endif -->
		<view class="header mb10">
			<view class="user card">
				<view class="user-msg">
					<image class="avatar" :src="userInfo.avatar" mode=""></image>
					<view class="">
						<view class="name">{{ userInfo.real_name }}</view>
						<view class="position">{{ userInfo?.job?.name || "-" }}</view>
					</view>
				</view>
				<view class="rule"> </view>
			</view>
		</view>
		<view class="card">
			<view class="header">
				<view class="title"> 考勤时间 </view>
				<view class="jump"> </view>
			</view>
			<view class="content">
				<view v-for="(item, index) in attendanceTimeList" :key="index">
					<view class="name">
						<view class="line"></view>
						<view class="att-name">{{ item.name }}</view>
					</view>
					<view v-for="(c, index) in item.rules" :key="index">
						<view class="time-range">
							<view class="att-name">{{ c.first_day_after ? "次日" : "当日"
                }}{{ c.work_hours }}</view>一
							<view class="att-name">{{ c.second_day_after ? "次日" : "当日"
                }}{{ c.off_hours }}</view>
						</view>
						<view class="text">晚到超过{{ formatTime(c.late) }}记为迟到；提前{{
                formatTime(c.early_leave)
              }}打卡记为早退；晚到超过{{
                formatTime(c.extreme_late)
              }}记为严重迟到</view>
						<view class="text">晚到超过{{ formatTime(c.early_card) }}，提前{{
                formatTime(c.early_lack_card)
              }}打卡记为半天缺卡</view>
						<view class="text">上班最早{{ formatTime(c.early_card) }} 之后可进行打卡；下班最晚
							{{ c.second_day_after ? "次日" : "当日"
              }}{{ formatTime(c.early_card) }}之前可进行打卡；{{c.free_clock==1? '下班可免打卡' : ''}}
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="card">
			<view class="header">
				<view class="title">考勤范围</view>
				<view class="jump"></view>
			</view>
			<view class="address" v-if="attendanceData.group">
				<view class="range">办公地点（{{ attendanceData.group.effective_range }}米以内）</view>
				<navigator class="adr"
					:url="`/pages/attendance/map?lat=${attendanceData.group.lat}&lng=${attendanceData.group.lng}&radius=${attendanceData.group.effective_range}`"
					open-type="navigate" hover-class="navigator-hover">
					<text class="iconfont icon-kaoqin-dingwei"></text>
					{{ attendanceData.group.location_name }}
				</navigator>
			</view>
		</view>
		<view class="card">
			<view class="header">
				<view class="title"> 考勤规则 </view>
				<view class="jump"> </view>
			</view>
			<view class="content">
				<view v-if="attendanceData.group && attendanceData.group.repair_allowed">
					<view class="time-range">
						<view class="att-name">补卡</view>
					</view>
					<view class="text">允许补卡</view>
					<view class="text">{{ publishedBooksMessage }}允许补卡</view>
					<view class="text" v-if="attendanceData.group.is_limit_time">
						可申请过去{{ attendanceData.group.limit_time }}天内的补卡</view>
					<view class="text" v-if="attendanceData.group.is_limit_number">
						每人每月补卡次数上限{{ attendanceData.group.limit_number }}次</view>
				</view>
				<view v-if="attendanceData.group && attendanceData.group.is_photo">
					<view class="time-range">
						<view class="att-name">拍照打卡</view>
					</view>
					<view class="text">员工上下班打卡均需拍照</view>
				</view>
				<view v-if="attendanceData.group && attendanceData.group.is_external">
					<view class="time-range">
						<view class="att-name">外勤打卡</view>
					</view>
					<view class="text">允许外勤打卡</view>
					<view class="text" v-if="attendanceData.group.is_external_note">外勤打卡备注必须填写</view>
					<view class="text" v-if="attendanceData.group.is_external_photo">外勤打卡必须拍照</view>
				</view>
			</view>
		</view>
		<view class="card" v-if="attendanceData && attendanceData.group && attendanceData.group.admins.length">
			<view class="header">
				<view class="title">考勤管理员</view>
				<view class="jump"></view>
			</view>
			<view class="admin">
				<view class="tip">若对考勤规则有疑问，可咨询你的考勤组管理员</view>
				<view class="user">
					<view class="user-msg" v-for="item in attendanceData.group.admins" :key="item.id">
						<image class="avatar" :src="item.avatar" mode=""></image>
						<view class="">
							<view class="name">{{ item.name }}</view>
							<view class="position">{{ item.job?.name || "暂无职位" }}</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		attendanceBasic,
		attendanceUserMsg
	} from "@/api/attendance";
	import {
		useStore
	} from "vuex";
	import {
		formatTime
	} from "@/utils/helper";
	const store = useStore();
	const userInfo = ref(store.state.app.userInfo);
	let attendanceTimeList = ref([]);
	let attendanceData = ref();
	const userId = ref(0);
	const date = ref();
	onLoad((options) => {
		if (options.user_id) {
			userId.value = options.user_id;
			date.value = options.date || '';
			getAttendanceUserMsg();
		}
		getAttendanceBasic();
	});
	const getAttendanceBasic = () => {
		let d = {
			user_id: userId.value,
		};
		date.value ? d.date = date.value : ''

		attendanceBasic(d).then((res) => {
			attendanceData.value = res.data;
			attendanceTimeList.value.push(res.data.shift);
		});
	};
	const getAttendanceUserMsg = () => {
		attendanceUserMsg(userId.value).then((res) => {
			userInfo.value = res.data;
		});
	};
	const publishedBooksMessage = computed(() => {
		let text = [];
		const repairType = attendanceData.value.group.repair_type;
		if (repairType.length > 0) {
			if (repairType.includes(1)) {
				text.push("缺卡");
			}
			if (repairType.includes(2)) {
				text.push("迟到");
			}
			if (repairType.includes(3)) {
				text.push("早退");
			}
			if (repairType.includes(4)) {
				text.push("正常");
			}
		}
		return text.toString();
	});
	const back = () => {
		uni.navigateBack({
			delta: 1
		})
	}
	onBeforeUnmount(() => {});
</script>

<style lang="scss" scoped>
	.header {
		padding-top: 20rpx;

		.user {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin: 0 20rpx;
			padding: 30rpx 24rpx;

			.user-msg {
				display: flex;
				align-items: center;

				.avatar {
					width: 80rpx;
					height: 80rpx;
					border-radius: 8rpx;
					margin-right: 20rpx;
				}

				.name {
					font-size: 30rpx;
					font-weight: 500;
					color: #303133;
					line-height: 30rpx;
					margin-bottom: 18rpx;
				}

				.position {
					font-size: 24rpx;
					font-weight: 400;
					color: #909399;
					line-height: 24rpx;
				}
			}

			.rule {
				font-size: 12rpx;
				font-weight: 400;
				color: #308bf8;
				line-height: 12rpx;
			}
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

		.address {
			padding: 30rpx 24rpx;

			.range {
				font-size: 28rpx;
				font-weight: 500;
				color: #303133;
				line-height: 28rpx;
				margin-bottom: 20rpx;
			}

			.adr {
				font-size: 26rpx;
				font-weight: 400;
				color: #308bf8;
				line-height: 26rpx;
				padding: 14rpx 18rpx;
				background: #f2f6fc;
				border-radius: 4rpx;
				width: max-content;

				.icon-kaoqin-dingwei {
					font-size: 20rpx;
				}
			}
		}

		.content {
			padding: 30rpx 24rpx;

			.name {
				display: flex;
				align-items: center;
				font-size: 28rpx;
				font-weight: 500;
				color: #303133;
				line-height: 28rpx;

				.line {
					width: 4rpx;
					height: 28rpx;
					background: #308bf8;
					margin-right: 16rpx;
				}
			}

			.time-range {
				display: flex;
				align-items: center;
				margin-top: 28rpx;
				font-size: 28rpx;
				font-weight: 500;
				color: #303133;
				line-height: 28rpx;
			}

			.text {
				margin-top: 30rpx;
				font-size: 28rpx;
				font-weight: 400;
				color: #606266;
			}
		}

		.admin {
			padding: 0rpx 24rpx 40rpx;

			.tip {
				font-size: 24rpx;
				font-weight: 500;
				color: #909399;
				line-height: 24rpx;
				margin-top: 36rpx;
			}

			.user {
				display: flex;
				flex-wrap: wrap;

				.user-msg {
					display: flex;
					align-items: center;
					width: 50%;
					margin-top: 30rpx;

					.avatar {
						width: 80rpx;
						height: 80rpx;
						border-radius: 8rpx;
						margin-right: 20rpx;
					}

					.name {
						font-size: 30rpx;
						font-weight: 500;
						color: #303133;
						line-height: 30rpx;
						margin-bottom: 18rpx;
					}

					.position {
						font-size: 24rpx;
						font-weight: 400;
						color: #909399;
						line-height: 24rpx;
					}
				}
			}
		}
	}
</style>