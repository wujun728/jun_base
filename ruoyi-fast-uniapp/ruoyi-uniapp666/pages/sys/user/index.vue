<template>
	<view class="wrap">
		<view class="header">
			<view class="userinfo">
				<view class="image" @click="navTo('info')">
					<!-- <image :src="avatarUrl"></image> -->
					<u-avatar size="100"  src="/static/aidex/images/user01.png"></u-avatar>
					</view>
				<view class="info" style="display: flex;justify-content: space-between;">
					<view>
						<view class="username">管理员</view>
						<view class="usercode">普通会员</view>
					</view>
					<view class="sign-in-images"><image src="/static/aidex/images/sign-in.png"></image></view>
				</view>
			</view>
			<u-row class="userinfo-topbox" gutter="16" justify="center">
				<u-col span="4" text-align="center">
					<view class="number">9,999<em>元</em></view>
					<view>余额</view>
				</u-col>
				<u-col span="4" text-align="center"  @click="navTo('/pages/sys/application/recharge')">
					<view><u-icon size="28px" color="#ffffff" name="rmb-circle"></u-icon></view>
					<view>充值</view>
				</u-col>
				<u-col span="4" text-align="center" @click="navTo('/pages/sys/application/balance-details')">
					<view>
						<view class="iconfont icon-faan" style="font-size: 24px;color:#ffffff;"></view>
					</view>
					<view>余额明细</view>
				</u-col>
			</u-row>
		</view>
		<view class="list">
			<view>
				<u-cell-group class="personal-list">
					<u-gap height="20" bg-color="#f5f5f5"></u-gap>
					<u-cell-item icon="question-circle" :iconStyle="{ color: '#ff8d06' }" title="常见问题" @click="navTo('problem')"></u-cell-item>
					<u-cell-item icon="kefu-ermai" :iconStyle="{ color: '#5f8dff' }" title="意见反馈" @click="navTo('comment')"></u-cell-item>
					<u-cell-item icon="map" :iconStyle="{ color: '#316ede' }" title="账号安全" @click="navTo('pwd')"></u-cell-item>
					<u-cell-item icon="order" :iconStyle="{ color: '#59bdf9' }" title="清除缓存" @click="navTo('clear-cache')"></u-cell-item>
					<u-cell-item icon="account" :iconStyle="{ color: '#27c0dc' }" title="关于我们" @click="navTo('about')"></u-cell-item>
					<u-cell-item icon="kefu-ermai" :iconStyle="{ color: '#ff8a00' }" title="联系客服" @click="navTo('service')"></u-cell-item>
					<u-gap height="20" bg-color="#f5f5f5"></u-gap>
					<u-cell-item icon="setting" :iconStyle="{ color: '#2767dc' }" title="系统设置" @click="navTo('setting')"></u-cell-item>
				</u-cell-group>
			</view>
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
			iconSize: 38
		};
	},
	computed: {
		avatarUrl() {
			// let url = this.vuex_user.avatarUrl || '/ctxPath/static/images/user1.jpg';
			// url = url.replace('/ctxPath/', this.vuex_config.baseUrl + '/');
			let url = this.vuex_config.baseUrl+ this.vuex_user.avatar ||  '/static/aidex/tabbar/my_2.png';
			url = this.replaceAll(url,'\\','/');
			return url + '?t=' + new Date().getTime();
		}
	},
	methods: {
		navTo(url) {
			uni.navigateTo({
				url: url
			});
		},
		logout() {
			this.$u.api.logout().then(res => {
				this.$u.toast(res.msg);
				if (res.code == '200' || res.code == '401') {
					let self = this;
					setTimeout(() => {
						uni.reLaunch({
							url: '/pages/sys/login/index'
						});
					}, 500);
				}
			});
		},
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
			this.$u.toast('小程序端或H5端无需检查更新')
			// #endif
		}
	}
};
</script>
<style lang="scss">
@import 'index.scss';
page {
	background-color: #f5f5f5;
}
.wrap .u-cell-box .u-cell_title{
	color:#202328;
}
.sign-in-images{
	width: 125rpx;
	height:50rpx;
	position: absolute;
	right: 0;
	top:5px;
	uni-image{
		width: 125rpx;
		height:50rpx;
	}
}
</style>
