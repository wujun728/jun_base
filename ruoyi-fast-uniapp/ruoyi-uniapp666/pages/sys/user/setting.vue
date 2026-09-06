<template>
	<view class="wrap">
		<u-gap height="20" bg-color="#f5f5f5"></u-gap>
		<u-cell-group :border="false">
			<u-cell-item title="通用" @click="navTo('/pages/sys/user/currency')" :arrow="true"></u-cell-item>
			<u-gap height="20" bg-color="#f5f5f5"></u-gap>
			<u-cell-item title="版本更新"  @click="navTo('')" :arrow="true"></u-cell-item>
			<u-cell-item title="隐私政策" @click="navTo('')" :arrow="true"></u-cell-item>
			<u-cell-item title="用户协议" @click="navTo('')" :arrow="true"></u-cell-item>
		</u-cell-group>
		<u-gap height="20" bg-color="#f5f5f5"></u-gap>
		<view>
			<u-button class="sign-out"  @click="logout" :hair-line="false">退出登录</u-button>
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
			
		};
	},
	methods: {
		navTo(url) {
			uni.navigateTo({
				url: url
			});
		},
		openSettings() {
			// #ifdef APP-PLUS
			uni.getSystemInfo({  
				success(res) {  
					if(res.platform == 'ios'){
						plus.runtime.openURL("app-settings://");
					} else if (res.platform == 'android'){
						var main = plus.android.runtimeMainActivity();  
						var Intent = plus.android.importClass("android.content.Intent");
						var mIntent = new Intent('android.settings.SOUND_SETTINGS');
						main.startActivity(mIntent);
					}
				}
			});
			// #endif
			// #ifndef APP-PLUS
			this.$u.toast('小程序端或H5端已是最新版，无需检查更新！');
			// #endif
		},
		logout() {
			this.$u.api.logout().then(res => {
				this.$u.toast(res.msg);
				if (res.code == '200') {
					let self = this;
					setTimeout(() => {
						uni.reLaunch({
							url: '/pages/sys/login/index'
						});
					}, 500);
				}
			});
		}
	}
};
</script>
<style lang="scss">


page {
	background-color: #f5f5f5;
}

/deep/ .u-cell-title {
	padding: 25rpx 30rpx;
	font-size: 30rpx;
}
</style>
