<template>
	<view class="wrap form_right">
		<u-form class="form" :model="model" :rules="rules" ref="uForm" label-position="left">
			<u-form-item label="编号" prop="id" label-width="180">
				<u-input placeholder="请输入编号" v-model="model.id" type="text" maxlength="64" style="text-align: right;"></u-input>
			</u-form-item>
			<u-form-item label="单行文本" prop="testInput" label-width="180">
				<u-input placeholder="请输入单行文本" v-model="model.testInput" type="text" maxlength="200" style="text-align: right;"></u-input>
			</u-form-item>
			<u-form-item label="多行文本" prop="testTextarea" label-width="180" label-position="top">
				<u-input type="textarea" placeholder="请输入多行文本" v-model="model.testTextarea" style="text-align: left;" height="100" maxlength="500" />
			</u-form-item>
		</u-form>
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
export default {
	data() {
		return {
			model: {
				id: '',
				testInput: '',
				testTextarea: '',
				testSelect: '',
				testSelectMultiple: '',
				testSelectMultipleLabel: '',
				testRadio: '',
				testCheckbox: '',
				testUser: {
					userCode: '',
					userName: ''
				},
				testOffice: {
					officeCode: '',
					officeName: ''
				}
			},
			rules: {
				testInput: [
					{
						required: true,
						message: '请输入单行文本',
						trigger: ['change','blur'],
					}
				]
			},
			officeSelectList: [],
			userSelectList: [],
		};
	},
	onLoad(params){
		if (params.id){
			this.$u.api.testData.form({id: params.id}).then(res => {
				Object.assign(this.model, res.testData);
			});
		}
	},
	onReady() {
		this.$refs.uForm.setRules(this.rules);
		// 机构数据
		this.$u.api.office.treeData().then(res => {
			this.officeSelectList = res;
		});
		// 人员和机构数据
		this.$u.api.office.treeData({isLoadUser: true}).then(res => {
			this.userSelectList = res;
		});
	},
	methods: {
		submit() {
			console.log(this.model)
			this.$refs.uForm.validate(valid => {
				if (valid) {
					this.$u.api.testData.save(this.model).then(res => {
						uni.showModal({
							title: '提示',
							content: res.message,
							showCancel: false,
							success: function () {
								if (res.result == 'true') {
									uni.navigateBack();
								}
							}
						});
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
<style lang="scss" scoped>
.input-placeholder{
	text-align: right;
}
.u-input{
	text-align: right!important;
}
.u-form-item__message{
	text-align: right!important;
}
.uni-textarea-placeholder{
	text-align: left;
}
.u-form-item{
	font-size:36rpx;
}
</style>
