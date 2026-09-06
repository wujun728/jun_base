<template>
	<view class="wrap">
		<u-gap height="20" bg-color="#f5f5f5"></u-gap>
		<u-form  class="task-form-field" :model="model" :rules="rules" ref="uForm">
			<u-form-item label="修改姓名" label-width="180">
				<u-input  placeholder="请输入姓名"></u-input>
			</u-form-item>
		</u-form>
		<view class="form-footer">
			<u-button class="btn" type="primary" @click="submit">保存</u-button>
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
.remind-text{
	padding: 20rpx 30rpx;
	color: #666666;
}
</style>
