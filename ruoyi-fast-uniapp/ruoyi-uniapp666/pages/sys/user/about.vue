<template>
	<view class="wrap">
		<view class="u-p-t-30 u-p-b-30 u-flex u-flex-col u-text-center">
			<!-- <u-avatar size="250" src="/static/aidex/logo200.png"></u-avatar> -->
			<view class="title">若依移动APP</view>
			<!-- <view class="version">{{vuex_config.productVersion}}</view> -->
		</view>
		<u-cell-group class="form" :border="false">
			<navigator url="" open-type="navigate">
				<u-cell-item :arrow="true" title="公司首页"></u-cell-item>
			</navigator>
			<navigator>
				<u-cell-item :arrow="false" title="技术服务">15389457063</u-cell-item>
			</navigator>
			<navigator url="" open-type="navigate">
				<u-cell-item :arrow="true" title="服务条款"></u-cell-item>
			</navigator>
		</u-cell-group>
		<u-row gutter="16" style="margin-top: 20px;">
			<u-col span="6" text-align="center">
				<view class="cade-box"><image src="/static/aidex/images/code01.jpg"></image></view>
				<view class="cade-text">公众号</view>
			</u-col>
			<u-col span="6" text-align="center">
				<view class="cade-box"><image src="/static/aidex/images/code02.jpg"></image></view>
				<view class="cade-text">扫一扫 加我微信</view>
			</u-col>
		</u-row>
		<view class="copyright">
			<view> 版权所有</view>
			<view>Copyright &copy; 2021 baidu.com</view>
		</view>
	</view>
</template>
<script>
/**
 * Copyright (c) 2013-Now http://aidex.vip All rights reserved.
 */
export default {
	methods: {
		upgrade(){
			// #ifdef APP-PLUS
			this.$u.api.upgradeCheck().then(res => {
				if (res.result == 'true'){
					uni.showModal({
						title: '提示',
						content: res.message + '是否下载更新？',
						showCancel: true,
						success: function (res2) {
							if (res2.confirm) {
								plus.runtime.openURL(res.data.apkUrl);
							}
						}
					});
				}else{
					this.$u.toast(res.message);
				}
			});
			// #endif
			// #ifndef APP-PLUS
			this.$u.toast('小程序端或H5端已是最新版，无需检查更新！');
			// #endif
		}
	}
};
</script>
<style lang="scss" scoped>
page {
	background-color: #f5f5f5;
}
.title {
	display: flex;
	margin: 30rpx 0 10px;
	font-size: 50rpx;
}
.version {
	margin-bottom: 10rpx;
	font-size: 40rpx;
}
.copyright {
	margin-top: 50rpx;
	text-align: center;
	line-height: 60rpx;
	color: #999;
}
.cade-box{
	uni-image{
		width: 120px;
		height:120px;
		border: 1px solid #dcdcdc;
		padding: 5px;
		background: #ffffff;
	}
}
.cade-text{
	color: #999999;
	font-size:12px;
}
</style>
