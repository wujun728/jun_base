<template>
	<view class="wrap">
		<view class="search">
			<u-search v-model="keywords" @custom="search" @search="search"></u-search>
		</view>
		<scroll-view class="scroll-list" scroll-y="true">
			<view class="uni-list">
				<uni-view class="uni-list-cell">
					<view class="uni-media-list" @click="navTo('/pages/sys/msg/list-item')">
						<view class="home-icon icon-color03">
							<i class="iconfont icon-tongzhi1"><u-badge type="error" count="2"></u-badge></i>
						</view>
						<uni-view  class="uni-media-list-body">
							<uni-view class="uni-media-list-text-top"><span>通知公告</span><span style="font-size: 26rpx;color: #999999;">12:35</span></uni-view>
							<uni-view class="uni-media-list-text-bottom">
								<uni-text><span>关于元旦放假的通知</span></uni-text>
							</uni-view>
						</uni-view>
					</view>
				</uni-view>
				<view class="uni-list-cell" @click="navTo('/pages/sys/msg/list-item')">
					<uni-view class="uni-media-list">
						<view class="home-icon icon-color04">
							<i class="iconfont icon-xinwen"><u-badge type="error" count="1"></u-badge></i>
						</view>
						<uni-view  class="uni-media-list-body">
							<uni-view class="uni-media-list-text-top"><span>新闻动态</span><span style="font-size: 26rpx;color: #999999;">09:07</span></uni-view>
							<uni-view class="uni-media-list-text-bottom">
								<uni-text><span>神十三航天员圆满完成出舱任务</span></uni-text>
							</uni-view>
						</uni-view>
					</uni-view>
				</view>
				<view class="uni-list-cell" @click="navTo('/pages/sys/msg/examine-item')">
					<uni-view class="uni-media-list">
						<view class="home-icon icon-color06">
							<i class="iconfont icon-msg-system"></i>
						</view>
						<uni-view  class="uni-media-list-body">
							<uni-view class="uni-media-list-text-top"><span>日常办公</span><span style="font-size: 26rpx;color: #999999;">11月07日</span></uni-view>
							<uni-view class="uni-media-list-text-bottom">
								<uni-text><span>王梓涵提交的“请假申请”待你审批</span></uni-text>
							</uni-view>
						</uni-view>
					</uni-view>
				</view>
				<view class="uni-list-cell" @click="navTo('/pages/sys/msg/list-item')">
					<uni-view class="uni-media-list">
						<view class="home-icon icon-color12">
							<i class="iconfont icon-baoxiao"></i>
						</view>
						<uni-view  class="uni-media-list-body">
							<uni-view class="uni-media-list-text-top"><span>网上报销</span><span style="font-size: 26rpx;color: #999999;">11月06日</span></uni-view>
							<uni-view class="uni-media-list-text-bottom">
								<uni-text><span>吴天祥提交的“差旅费报销”待你审批</span></uni-text>
							</uni-view>
						</uni-view>
					</uni-view>
				</view>
				<view class="uni-list-cell" @click="navTo('/pages/sys/msg/list-item')">
					<uni-view class="uni-media-list">
						<view class="home-icon icon-color04">
							<i class="iconfont icon-tongzhi"></i>
						</view>
						<uni-view  class="uni-media-list-body">
							<uni-view class="uni-media-list-text-top"><span>我的邮件</span><span style="font-size: 26rpx;color: #999999;">11月06日</span></uni-view>
							<uni-view class="uni-media-list-text-bottom">
								<uni-text><span>邮件提醒：邮件提醒：15封新邮件未读</span></uni-text>
							</uni-view>
						</uni-view>
					</uni-view>
				</view>
			</view>
		</scroll-view>
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
			
						
		};
	},
	onLoad() {
		
	},
	methods: {
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
.uni-title{
	font-size: 30rpx;
	color: #333333;
	padding: 10px;
	background: #fff;
	border-top:1px solid #ededed;
	margin-top: 20rpx;
}
.uni-media-list {
    padding: 15px 15px;

}
.uni-media-list-body {
    height: 42px;
	padding-left:20rpx;
}
.uni-media-list-text-top{
	height: 40rpx;
    overflow: hidden;
	width: 100%;
	line-height: 40rpx;
	font-size: 32rpx;
	display: flex;
	justify-content: space-between;
}

.uni-media-list-text-bottom {
    width: 100%;
    line-height: 24rpx;
    font-size: 26rpx;
	color: #666666;
	span{
		margin-right: 10rpx;
	}
}


</style>
