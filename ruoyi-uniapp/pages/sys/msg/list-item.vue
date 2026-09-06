<template>
	<view class="wrap">
		<view class="search">
			<u-search v-model="keywords" @custom="search" @search="search"></u-search>
		</view>
		<scroll-view class="scroll-list msg-list-item" scroll-y="true">
			<view class="msg-time">11:05</view>
			<u-card title="用户分享内容到社交媒体或好友" sub-title="11月08日" padding="20" margin="0rpx 20rpx" thumb="/static/aidex/images/list-icon.png" @click="navTo('/pages/sys/msg/form')">
				<view class="" slot="body">
					<view class="u-body-item u-flex  u-col-between u-p-t-0">
						<view class="u-body-item-title u-line-2">在常用的社交分享组件中，微信（微信好友和朋友圈）、微博、QQ（QQ好友和QQ空间）是三大主要平台 ...</view>
					</view>
				</view>
				<view class="" slot="foot"><u-icon name="eye" size="34" color="" label="30"></u-icon> <u-icon name="thumb-up" size="34" color="" label="7"></u-icon></view>
			</u-card>
			<view class="msg-time">11月10日 15:32</view>
			<u-card title="用户分享内容到社交媒体或好友" sub-title="11月10日" padding="20" margin="0rpx 20rpx" thumb="/static/aidex/images/list-icon.png" @click="navTo('/pages/sys/msg/form')">
				<view class="" slot="body">
					<view class="u-body-item u-flex u-col-between u-p-t-0">
						<view class="u-body-item-title u-line-2">在常用的社交分享组件中，微信（微信好友和朋友圈）、微博、QQ（QQ好友和QQ空间）是三大主要平台 ...</view>
					</view>
				</view>
				<view class="" slot="foot"><u-icon name="eye" size="34" color="" label="30"></u-icon> <u-icon name="thumb-up" size="34" color="" label="15"></u-icon></view>
			</u-card>
			<view class="msg-time">11月08日 10:05</view>
			<u-card title="用户分享内容到社交媒体或好友" sub-title="11月08日" padding="20" margin="0rpx 20rpx" thumb="/static/aidex/images/list-icon.png" @click="navTo('/pages/sys/msg/form')">
				<view class="" slot="body">
					<view class="u-body-item u-flex u-col-between u-p-t-0">
						<view class="u-body-item-title u-line-2">在常用的社交分享组件中，微信（微信好友和朋友圈）、微博、QQ（QQ好友和QQ空间）是三大主要平台 ...</view>
					</view>
				</view>
				<view class="" slot="foot"><u-icon name="eye" size="34" color="" label="30"></u-icon> <u-icon name="thumb-up" size="34" color="" label="15"></u-icon></view>
			</u-card>
			<view class="msg-time">11月08日 09:32</view>
			<u-card title="用户分享内容到社交媒体或好友" sub-title="11月08日" padding="20" margin="0rpx 20rpx" thumb="/static/aidex/images/list-icon.png" @click="navTo('/pages/sys/msg/form')">
				<view class="" slot="body">
					<view class="u-body-item u-flex u-col-between u-p-t-0">
						<view class="u-body-item-title u-line-2">在常用的社交分享组件中，微信（微信好友和朋友圈）、微博、QQ（QQ好友和QQ空间）是三大主要平台 ...</view>
					</view>
				</view>
				<view class="" slot="foot"><u-icon name="eye" size="34" color="" label="30"></u-icon> <u-icon name="thumb-up" size="34" color="" label="15"></u-icon></view>
			</u-card>
			<u-divider>已经到底了</u-divider>
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
.msg-time{
	font-size: 26rpx;
	padding: 10px 0;
	color: #999999;
	text-align: center;
}
.u-card__foot{
	.u-icon{
		margin-right: 10px;
	}
}


</style>
