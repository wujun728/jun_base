<template>
	<view class="nav">
		<uni-nav-bar :fixed="true" left-icon="left" status-bar backgroundColor="rgba(255,255,255,1)" color="#303133"
			:border="false" @clickLeft="cancel">
			<view class="nav-content">
				<view class="title">
					<view class="head-title">{{ pageTitle }}</view>
					<text>的加班明细</text>
				</view>
			</view>
			<template v-slot:right>
				<view class="bar-right">
					<picker ref="picker" mode="date" fields="month" :value="monthTime" start="2021-01" :end="lastTime"
						@change="bindPickerChange">
						<text class="uni-nav-bar-text">
							<text class="month">{{ monthTime }}</text>
							<uni-icons type="arrowdown" color="#606266" size="12" />
						</text>
					</picker>
				</view>
			</template>
		</uni-nav-bar>

		<!--    <view class="header">
      <picker ref="picker" :value="index" range-key="label" :range="typeList" @change="bindTypeChange">
        <text class="title" v-if="index">{{ typeList[index].label }}</text>
        <text class="title" v-else>日期类型</text>
        <uni-icons type="arrowdown" size="12" />
      </picker>
      <picker ref="picker" :value="calculatIndex" range-key="label" :range="calculateList" @change="calculatChange">
        <text class="title" v-if="calculatIndex">{{
          calculateList[calculatIndex].label
        }}</text>
        <text class="title" v-else>核算方式</text>
        <uni-icons type="arrowdown" size="12" />
      </picker>
    </view> -->
		<view class="card">
			<view class="table-title">
				<view class="item align-left">日期</view>
				<view class="item">日期类型</view>
				<view class="item">加班时长</view>
				<view class="item align-right">核算方式</view>
			</view>
			<view v-if="leaveList.length > 0" class="table-list">
				<view class="item" v-for="(item, index) in leaveList" :key="index">
					<view class="msg align-left">{{ item.date }}</view>
					<view class="msg">{{
            item.date_type == 1
              ? "工作日"
              : item.date_type == 2
              ? "休息日"
              : item.date_type == 3
              ? "节假日"
              : ""
          }}</view>
					<view class="msg">{{ item.work_hours
            }}{{
              item.time_type == "day"
                ? "天"
                : item.time_type == "hour"
                ? "小时"
                : ""
            }}</view>
					<view class="msg align-right">{{
            item.calc_type == 1 ? "调休" : "加班费"
          }}</view>
				</view>
			</view>
			<view v-else class="default">
				<image src="../../../static/image/empty.png" mode=""></image>
				<view class="text">暂无加班明细～</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		onReachBottom
	} from "@dcloudio/uni-app";
	import {
		overtimeStatistics
	} from "@/api/attendance";
	let lastTime = `${new Date().getFullYear()}-${new Date().getMonth() + 1}`;
	let monthTime = ref(`${new Date().getFullYear()}-${new Date().getMonth() + 1}`);
	let index = ref(0); // 时间
	let calculatIndex = ref(0); //核算
	let userId = ref(0);
	let page = ref(1)
	let pageTitle = ref();
	const andMore = ref(true);
	onLoad((options) => {
		if (options.status) {
			index.value = options.status
		}
		if (options.user_id) userId.value = options.user_id;
		if (options.user_id) {
			userId.value = options.user_id;
			pageTitle.value = options.user_name;
			uni.setNavigationBarTitle({
				title: pageTitle.value,
			});
		}
		getOvertimeStatistics();
	});
	const dataList = ref([])

	const typeList = reactive([{
			label: "工作日",
			value: 1,
		},
		{
			label: "节假日",
			value: 2,
		},
		{
			label: "休息日",
			value: 2,
		},
	]);
	const leaveList = ref([]);
	const calculateList = reactive([{
			label: "调休",
			value: 1,
		},
		{
			label: "加班费",
			value: 2,
		},
	]);

	const bindPickerChange = (e) => {
		dataList.value = [];
		page.value = 1;
		monthTime.value = e.detail.value;
		getOvertimeStatistics();
	};
	const bindTypeChange = (e) => {
		index.value = e.detail.value;
		dataList.value = [];
		page.value = 1;
		getOvertimeStatistics();
	};
	const calculatChange = (e) => {
		calculatIndex.value = e.detail.value;
		page.value = 1;
		getOvertimeStatistics();
	};
	const getOvertimeStatistics = () => {
		let d = {
			date: monthTime.value,
			date_type: index.value,
			calc_type: calculatIndex.value,
			user_id: userId.value,
		};
		overtimeStatistics(d).then((res) => {
			leaveList.value = res.data;
		});
	};
	const cancel = () => {
		uni.navigateBack();
	};
	// 下拉加载
	onReachBottom(() => {
		if (andMore.value) {
			page.value++;
			getOvertimeStatistics();
		}
	});
</script>

<style lang="scss" scoped>
	.nav {
		background: #fff;
		background-size: 100% 240rpx;
	}

	.nav-content {
		display: flex;
		justify-content: space-around;
		align-items: center;
		width: 100%;

		.head-title {
			max-width: 160rpx;
			display: inline-block;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
		}

		.title {
			display: flex;
			align-items: center;
			font-size: 34rpx;
			font-weight: 500;
			color: #303133;
			line-height: 34rpx;
		}
	}

	::v-deep .uni-nav-bar-text {
		width: max-content;

		span {
			display: flex;
			align-items: center;

			.month {
				white-space: nowrap;
				font-size: 30rpx;
				font-weight: 500;
				color: #303133;
				line-height: 30rpx;
				margin-right: 10rpx;
			}
		}
	}

	::v-deep .uni-navbar__header-btns {
		width: 160rpx !important;
	}

	::v-deep .uni-navbar__header {
		padding: 0 40rpx;
	}

	.bar-right {
		/* #ifndef APP-PLUS-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: center;
		justify-content: right;
		width: 180rpx;
		margin-left: 4px;
	}

	.header {
		display: flex;
		justify-content: space-around;
		background-color: #fff;
		color: #606266;
		padding: 24rpx 30rpx;

		.title {
			font-size: 26rpx;
			font-weight: 400;
			color: #606266;
			line-height: 26rpx;
			width: 4em;
		}
	}

	.card {
		background-color: #fff;
		border-radius: 12rpx;

		.table-title {
			display: flex;
			justify-content: space-between;
			background-color: #f0f1f5;
			font-size: 26rpx;
			font-weight: 400;
			color: #303133;
			line-height: 26rpx;
			padding: 24rpx;

			.item {
				// width: 4em;
				// text-align: center;
			}
		}

		.table-list {
			.item:last-child {
				border-bottom: none;
			}

			.item {
				margin-left: 24rpx;
				display: flex;
				justify-content: space-between;
				align-items: center;
				border-bottom: 1rpx solid #f0f1f5;
				padding: 42rpx 24rpx 40rpx 0;

				.msg.align-left {
					text-align: left;
				}

				.msg.align-right {
					text-align: right;
				}

				.msg {
					text-align: center;
					white-space: nowrap;
					font-size: 30rpx;
					font-weight: 400;
					color: #303133;
					line-height: 30rpx;
					width: 4em;
					max-width: 25%;
				}
			}
		}
	}

	::v-deep.uni-navbar__header-container {
		padding: 0;
	}

	.default {
		width: 100%;
		height: calc(100vh - 248rpx);
		padding-top: 246rpx;

		image {
			width: 400rpx;
			height: 300rpx;
			margin: auto;
			display: block;
		}

		.text {
			font-size: 26rpx;
			font-family: PingFang SC-Regular, PingFang SC;
			font-weight: 400;
			color: #909399;
			width: 100%;
			text-align: center;
			margin-top: 28rpx;
		}
	}
</style>