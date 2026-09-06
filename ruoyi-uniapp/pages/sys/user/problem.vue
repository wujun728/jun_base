<template>
	<view class="wrap">
		<u-gap height="20" bg-color="#f5f5f5"></u-gap>
		<u-cell-group :border="false">
			<u-cell-item title="如何切换用户？"  @click="navTo('')" :arrow="true"></u-cell-item>
			<u-cell-item title="如何修改密码？" @click="navTo('')" :arrow="true"></u-cell-item>
			<u-cell-item title="如何找回密码？" @click="navTo('')" :arrow="true"></u-cell-item>
			<u-cell-item title="如何更新系统？" @click="navTo('')" :arrow="true"></u-cell-item>
		</u-cell-group>
	</view>
</template>
<script>
/**
 * Copyright (c) 2013-Now http://aidex.vip All rights reserved.
 */
export default {
	data() {
		return {
			message: true,
			messageBar: true,
			upgrade: true
		};
	},
	methods: {
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
		},
		navTo(url) {
			uni.navigateTo({
				url: url
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
