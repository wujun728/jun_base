<template>
	<view class="wrap" style="padding-top:50rpx;">
		<view class="reg-text">忘记密码</view>
		<view class="list">
			<view class="list-call">
				<view class="iconfont icon-shouji" style="font-size: 22px;color:#5473e8;"></view>
				<u-field
					v-model="mobile"
					label="+86"
					placeholder="请填写手机号"
					style="width: 100%;"
				>
				</u-field>
			</view>
		</view>
		<view class="button" @click="code()"><text>获取验证码</text></view>
	</view>
</template>
<script>
/**
 * Copyright (c) 2013-Now http://aidex.vip All rights reserved.
 */
export default {
	data() {
		return {
			loginCode: '',
			password: '',
			validCode: '',
			fpValidCode: '',
			showPassword: false,
			imgValidCodeSrc: null,
			tips: '获取验证码',
			seconds: 60
		};
	},
	onLoad() {
		this.refreshImgValidCode();
	},
	methods: {
		code(){
			uni.reLaunch({
				url: '/pages/sys/login/code'
			});
		},
		showPass() {
			this.showPassword = !this.showPassword;
		},
		refreshImgValidCode(e) {
			if (this.vuex_token == '') {
				this.$u.api.index().then(res => {
					this.imgValidCodeSrc = this.vuex_config.baseUrl + '/validCode?__sid='
						+ res.sessionid + '&t=' + new Date().getTime();
				});
			} else {
				this.imgValidCodeSrc = this.vuex_config.baseUrl + '/validCode?__sid='
						+ this.vuex_token + '&t=' + new Date().getTime();
			}
			this.validCode = '';
		},
		codeChange(text) {
			this.tips = text;
		},
		formValid() {
			if (this.loginCode.length == 0) {
				this.$u.toast('请输入账号');
				return false;
			}
			if (this.password.length == 0) {
				this.$u.toast('请输入新密码');
				return false;
			}
			if (this.validCode.length == 0) {
				this.$u.toast('请输入图片验证码');
				return false;
			}
			return true;
		},
		getValidCode() {
			if (!this.formValid()) {
				return;
			}
			if (this.$refs.uCode.canGetCode) {
				this.$u.api.validCode({
					validCode: this.validCode
				})
				.then(res => {
					if (res !== 'true') {
						this.$u.toast('图片验证码错误');
						return;
					}
					this.$u.api.getFpValidCode({
						loginCode: this.loginCode,
						validCode: this.validCode,
						validType: 'mobile'
					})
					.then(res => {
						this.$u.toast(res.message, 3000);
						if (res.result == 'false') {
							this.refreshImgValidCode();
						}
					});
					this.$refs.uCode.start();
				});
			}
		},
		submit() {
			if (!this.formValid()) {
				return;
			}
			if (this.fpValidCode.length == 0) {
				this.$u.toast('请输入手机验证码');
				return false;
			}
			this.$u.api.savePwdByValidCode({
				loginCode: this.loginCode,
				fpValidCode: this.fpValidCode,
				password: this.password
			})
			.then(res => {
				uni.showModal({
					title: '提示',
					content: res.message,
					showCancel: false,
					success: function () {
						if (res.result == 'true') {
							uni.reLaunch({
								url: '/pages/sys/login/index'
							});
						}
					}
				});
			});
		}
	}
};
</script>
<style lang="scss">
@import 'index.scss';
.uni-input-placeholder{
	font-size: 32rpx;
}

.uni-input-input{
	font-size: 32rpx;
}
.u-border-bottom:after {
    border-bottom-width: 0px!important;
}
</style>
