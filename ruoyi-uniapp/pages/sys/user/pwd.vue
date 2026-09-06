<template>
	<view class="wrap">
		<view class="remind-text">请设置登录密码<br>
		定期更新密码提高安全性</view>
		<u-form class="form" :model="model" :rules="rules" ref="uForm">
			<u-form-item label="旧密码" prop="oldPassword" label-width="180">
				<u-input type="password" v-model="model.oldPassword" placeholder="请输入旧密码"></u-input>
			</u-form-item>
			<u-form-item label="新密码" prop="newPassword" label-width="180">
				<u-input type="password" v-model="model.newPassword" placeholder="请输入新密码"></u-input>
			</u-form-item>
			<u-form-item label="确认密码" prop="confirmPassword" label-width="180">
				<u-input type="password" v-model="model.confirmPassword" placeholder="请确认新密码"></u-input>
			</u-form-item>
		</u-form>
		<view class="remind-text">
			<u-icon name="question-circle" color="#2767dc" size="28"></u-icon> 
			密码必须是8-16位的数字，字符组合（不能是纯数字）</view>
		<view class="form-footer">
			<u-button class="btn" type="primary" @click="submit">提交</u-button>
			<!-- <u-button class="btn" type="default" @click="cancel">关闭</u-button> -->
		</view>
	</view>
</template>
<script>
/**
 * Copyright (c) 2013-Now http://aidex.vip All rights reserved.
 */
import base64 from '@/common/base64.js';
export default {
	data() {
		return {
			model: {
				oldPassword: '',
				newPassword: '',
				confirmNewPassword: ''
			},
			rules: {
				oldPassword: [
					{
						required: true,
						message: '请输入旧密码',
						trigger: ['change','blur'],
					}
				],
				newPassword: [
					{
						required: true,
						message: '请输入新密码',
						trigger: ['change','blur'],
					},
					{
						pattern: /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]+\S{5,12}$/,
						message: '需同时含有字母和数字，长度在6-12之间',
						trigger: ['change','blur'],
					}
				],
				confirmPassword: [
					{
						required: true,
						message: '请重新输入密码',
						trigger: ['change','blur'],
					},
					{
						validator: (rule, value, callback) => {
							return value === this.model.newPassword;
						},
						message: '两次输入的密码不相等',
						trigger: ['change','blur'],
					}
				],
			}
		};
	},
	onReady() {
		this.$refs.uForm.setRules(this.rules);
	},
	methods: {
		submit() {
			this.$refs.uForm.validate(valid => {
				if (valid) {
					this.$u.api.user.infoSavePwd({
						oldPassword: this.model.oldPassword,
						newPassword: this.model.newPassword
					}).then(res => {
						if(res.code == '200'){
							uni.showModal({
								title: '提示',
								content: res.msg,
								showCancel: false,
								success: function () {
									uni.navigateBack();
								}
							});
						}else{
							uni.showModal({
								title: '提示',
								content: res.msg,
								showCancel: false
							});
						}
					});
				} else {
					this.$u.toast('您填写的信息有误，请根据提示修正。');
				}
			});
		},
		cancel() {
			uni.navigateBack();
		}
	}
};
</script>
<style lang="scss">
page{
	background: #f5f5f5;
}
.u-form{
	background: #fff;
	padding: 0px 15px;
}
.remind-text{
	padding: 20rpx 30rpx;
	color: #666666;
}
</style>
