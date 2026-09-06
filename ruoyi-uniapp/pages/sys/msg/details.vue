<template>
	<view class="wrap" style="padding-bottom: 60px;">
		<view class="header">
			<view class="userinfo" @click="navTo('info')">
				<view class="image">
					<!-- <image :src="avatarUrl"></image> -->
					<u-avatar size="80" :src="avatarUrl"></u-avatar>
				</view>
				<view class="info" style="flex:1;display: flex;justify-content: space-between;">
					<view>
						<view class="username">请假</view>
						<view class="usercode">王天翔</view>
					</view>
					<u-tag text="待审核" shape="circle"  type="warning" style="height: 55rpx;" />
				</view>
			</view>
		</view>
		<u-tabs :list="list" :is-scroll="false" :current="current" @change="change"></u-tabs>
		<!-- 当前套餐 -->
		<view v-if="current === 0">
			<u-gap height="20" bg-color="#f5f5f5"></u-gap>
			<u-cell-group class="datails-examine">
				<u-cell-item title="申请编号" label="211110" :arrow="false" :border-bottom="false"></u-cell-item>
				<u-cell-item title="所属部门" label="人事部"  :arrow="false" :border-bottom="false"></u-cell-item>
				<u-cell-item title="请假类型" label="事假"  :arrow="false" :border-bottom="false"></u-cell-item>
				<u-cell-item title="开始时间" label="2021年10月25日09:00" :arrow="false"  :border-bottom="false"></u-cell-item>
				<u-cell-item title="结束时间" label="2021年10月25日18:00"  :arrow="false" :border-bottom="false"></u-cell-item>
				<u-cell-item title="时长" label="8h"  :arrow="false" :border-bottom="false"></u-cell-item>
				<u-cell-item title="请假事由" label="感冒了,请假一天,望领导批准!"  :arrow="false" :border-bottom="false"></u-cell-item>
			</u-cell-group>
			<u-gap height="20" bg-color="#f5f5f5"></u-gap>
			<u-time-line>
				<u-time-line-item nodeTop="2">
						<!-- 此处自定义了左边内容，用一个图标替代 -->
					<template v-slot:node>
						<image class="user-images" src="/static/aidex/images/user06.png"></image>
					</template>
					<template v-slot:content>
						<view style="justify-content: space-between;display: flex;">
							<view class="u-order-title">发起人</view>
							<view class="u-order-time">05-08 12:12</view>
						</view>
					</template>
				</u-time-line-item>
				<u-time-line-item nodeTop="2">
						<!-- 此处自定义了左边内容，用一个图标替代 -->
					<template v-slot:node>
						<image class="user-images" src="/static/aidex/images/user06.png"></image>
					</template>
					<template v-slot:content>
						<view style="justify-content: space-between;display: flex;">
							<view class="u-order-title">部门领导</view>
							<view class="u-order-time">05-08 12:12</view>
						</view>
						<view class="u-order-desc">同意</view>
					</template>
				</u-time-line-item>
				<u-time-line-item nodeTop="2">
						<!-- 此处自定义了左边内容，用一个图标替代 -->
					<template v-slot:node>
						<image class="user-images" src="/static/aidex/images/user06.png"></image>
					</template>
					<template v-slot:content>
						<view style="justify-content: space-between;display: flex;">
							<view class="u-order-title">公司领导</view>
							<view class="u-order-time">05-08 12:12</view>
						</view>
						<view class="u-order-desc">同意</view>
					</template>
				</u-time-line-item>
			</u-time-line>
			<u-row gutter="32" class="bottom-box" justify="center">
				<u-col span="5">
					<view><u-button :plain="true" type="primary" shape="circle" @click="navTo('/pages/sys/home/index')">拒绝</u-button></view>
				</u-col>
				<u-col span="5">
					<view><u-button type="primary" shape="circle" @click="navTo('/pages/sys/home/index')">确定</u-button></view>
				</u-col>
			</u-row>
		</view>
		<view v-if="current === 1">
			<view class="search">
				<u-search v-model="keywords" @custom="search" @search="search"></u-search>
			</view>
			<u-card class="task-list-item" :border="false" padding="20" margin="20rpx">
				<view slot="head" style="display: flex;align-items: center;justify-content: space-between;">
					<view style="display: flex;align-items: center;font-size: 30rpx;"><image class="user-images" src="/static/aidex/images/user06.png"></image>李毅的请假申请</view><view style="color: #999999;font-size: 22rpx;">2021年10月24日</view>
				</view>
				<view class="" slot="body">
					<u-row gutter="16">
						<u-col span="12">
							<view class="apply-text"><span>假期类型：</span>年假</view>
						</u-col>
						<u-col span="12">
							<view class="apply-text"><span>开始时间：</span>2021年10月25日14:30</view>
						</u-col>
						<u-col span="12">
							<view class="apply-text"><span>结束时间：</span>2021年10月27日14:30</view>
						</u-col>
					</u-row>
				</view>
				<view class="apply-list-foot" slot="foot" style="text-align: right;color: #58ca93;">
					审批通过
				</view>
			</u-card>
			<u-card class="task-list-item" :border="false" padding="20" margin="20rpx">
				<view slot="head" style="display: flex;align-items: center;justify-content: space-between;">
					<view style="display: flex;align-items: center;font-size: 30rpx;"><image class="user-images" src="/static/aidex/images/user06.png"></image>李毅的请假申请</view><view style="color: #999999;font-size: 22rpx;">2021年10月24日</view>
				</view>
				<view class="" slot="body">
					<u-row gutter="16">
						<u-col span="12">
							<view class="apply-text"><span>假期类型：</span>年假</view>
						</u-col>
						<u-col span="12">
							<view class="apply-text"><span>开始时间：</span>2021年10月25日14:30</view>
						</u-col>
						<u-col span="12">
							<view class="apply-text"><span>结束时间：</span>2021年10月27日14:30</view>
						</u-col>
					</u-row>
				</view>
				<view class="apply-list-foot" slot="foot" style="text-align: right;color: #58ca93;">
					审批通过
				</view>
			</u-card>
			<u-card class="task-list-item" :border="false" padding="20" margin="20rpx">
				<view slot="head" style="display: flex;align-items: center;justify-content: space-between;">
					<view style="display: flex;align-items: center;font-size: 30rpx;"><image class="user-images" src="/static/aidex/images/user06.png"></image>李毅的请假申请</view><view style="color: #999999;font-size: 22rpx;">2021年10月24日</view>
				</view>
				<view class="" slot="body">
					<u-row gutter="16">
						<u-col span="12">
							<view class="apply-text"><span>假期类型：</span>年假</view>
						</u-col>
						<u-col span="12">
							<view class="apply-text"><span>开始时间：</span>2021年10月25日14:30</view>
						</u-col>
						<u-col span="12">
							<view class="apply-text"><span>结束时间：</span>2021年10月27日14:30</view>
						</u-col>
					</u-row>
				</view>
				<view class="apply-list-foot" slot="foot" style="text-align: right;color: #f28c03;">
					待审核
				</view>
			</u-card>
		</view>
		
	</view>
</template>
<script>
export default {
		data() {
			return {
			show: false,
			list: [{
				name: '申请详情'
			}, {
				name: '查看数据',
			}],
			m2mSimflowList:[],
			m2mOrderFlowList:[],
			current: 0,
			status: 'loadmore',
			iconType: 'circle',
			isDot: false,
			loadText: {
						loadmore: '点击加载更多',
						loading: '正在加载...',
						nomore: '没有更多了'
			},
			}
		},
		created(){
		},
		methods: {
			change(index) {
				this.current = index;
			},
			navTo(url) {
				uni.navigateTo({
					url: url
				});
			}
		}

	}
</script>
<style lang="scss" scoped>
page {
	background-color: #f5f5f5;
}
.wrap .search{
	background: #ffffff;
}
.apply-text{
	font-size: 28rpx;
	color: #333333;
	span{
		color: #999999;
	}
}
.user-images{
	width: 28px;
	height:28px;
	margin-right: 8px;
}
.apply-list-foot{
	font-size: 28rpx;
}
.personnel-list{
	display: flex;
	align-items: center;
	flex-wrap:wrap;
	.personnel-user{
		position: relative;
		margin: 5px 9px 0;
	}
	.user-images{
		width: 32px;
		height:32px;
		margin-right:0;
		
	}
	.iconfont{
		position: absolute;
		top:-12px;
		right:-5px;
		color: #FE0100;
	}
}
.header {
	background-color: #ffffff;
	
	.userinfo {
		display: flex;
		padding: 20rpx 20rpx 10rpx;

		.image {
			flex-shrink: 0;
			width: 80rpx;
			height: 80rpx;
			image {
				border-radius: 100%;
				width: 100%;
				height: 100%;
			}
		}

		.info {
			display: flex;
			flex-flow: wrap;
			padding-left: 30rpx;
			color: #333333;

			.username {
				width: 100%;
				font-size: 32rpx;
			}

			.usercode {
				padding: 0;
				font-size: 24rpx;
				color: #999999;
			}
		}
	}

	.logout {
		flex-shrink: 0;
		position: absolute;
		right: 70rpx;
		top: 65rpx;
		.u-btn {
			font-size: 30rpx;
		}
	}
}
.u-node {
		width: 44rpx;
		height: 44rpx;
		border-radius: 100rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		background: #d0d0d0;
	}
	
	.u-order-title {
		color: #333333;
		font-weight: bold;
		font-size: 30rpx;
	}
	
	.u-order-desc {
		color: rgb(150, 150, 150);
		font-size: 28rpx;
		margin-bottom: 6rpx;
	}
	
	.u-order-time {
		color: rgb(200, 200, 200);
		font-size: 26rpx;
	}
</style>
