<template>
	<view class="wrap">
		<view class="search">
			<u-search v-model="keywords" @custom="search" @search="search"></u-search>
		</view>
		<u-tabs :list="list" :is-scroll="false" :current="current" @change="change"></u-tabs>
		<view v-if="current === 0">
		<scroll-view class="scroll-list msg-list-item" scroll-y="true">
			<view class="msg-time">11:05</view>
			<u-card class="task-list-item" :border="false" padding="20" margin="10rpx 20rpx" @click="navTo('/pages/sys/msg/details')">
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
				<view class="apply-list-foot" slot="foot">
					<u-row gutter="16">
						<u-col span="6" text-align="center">
							<view class="agree-text">同意</view>
						</u-col>
						<u-col span="6" text-align="center">
							<view class="refuse-text">拒绝</view>
						</u-col>
					</u-row>
				</view>
			</u-card>
			<view class="msg-time">11月10日 15:32</view>
			<u-card class="task-list-item" :border="false" padding="20" margin="10rpx 20rpx">
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
				<view class="apply-list-foot" slot="foot">
					<u-row gutter="16">
						<u-col span="6" text-align="center">
							<view class="agree-text">同意</view>
						</u-col>
						<u-col span="6" text-align="center">
							<view class="refuse-text">拒绝</view>
						</u-col>
					</u-row>
				</view>
			</u-card>
			<view class="msg-time">11月08日 10:05</view>
			<u-card class="task-list-item" :border="false" padding="20" margin="10rpx 20rpx">
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
				<view class="apply-list-foot" slot="foot">
					<u-row gutter="16">
						<u-col span="6" text-align="center">
							<view class="agree-text">同意</view>
						</u-col>
						<u-col span="6" text-align="center">
							<view class="refuse-text">拒绝</view>
						</u-col>
					</u-row>
				</view>
			</u-card>
			<u-divider>已经到底了</u-divider>
		</scroll-view>
		</view>
		<view v-if="current === 1">
		<scroll-view class="scroll-list msg-list-item" scroll-y="true">
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
				<view class="apply-list-foot" slot="foot" style="text-align: right;color: #58ca93;">
					审批通过
				</view>
			</u-card>
			<u-divider>已经到底了</u-divider>
		</scroll-view>
		</view>
		<view v-if="current === 2">
		<scroll-view class="scroll-list msg-list-item" scroll-y="true">
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
			<u-divider>已经到底了</u-divider>
		</scroll-view>
		</view>
	</view>
</template>
<script>
/**
 * Copyright (c) 2013-Now http://aidex.vip All rights reserved.
 */
export default {
	data() {
		return {
			keywords: '',
			list: [{
				name: '待办'
			}, {
				name: '已办',
			}, {
				name: '已发送',
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
						
		};
	},
	onLoad() {
		
	},
	methods: {
		change(index) {
			this.current = index;
		},
		navTo(url) {
			uni.navigateTo({
				url: url
			});
		},
		search(value) {
			this.$u.toast('搜索内容为：' + value)
		}
		
	}
};

</script>
<style lang="scss">
@import '../../../common/uni.css';
page {
	background-color: #f5f5f5;
}
.wrap .search{
	background: #ffffff;
}
.u-dropdown {
		background: #ffffff;
}
.msg-time{
	font-size: 26rpx;
	padding: 5px 0;
	color: #999999;
	text-align: center;
}
.u-card__foot{
	.u-icon{
		margin-right: 10px;
	}
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
	justify-content: space-around;
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
.agree-text{
	color: #4094ff;
}
.refuse-text{
	color: #ff4400;
}
</style>
