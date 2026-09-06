<template>
	<view class="wrap" style="padding-bottom: 60px;">
		<u-cell-group :border="false">
			<u-gap height="20" bg-color="#f5f5f5"></u-gap>
			<u-cell-item title="头像" :arrow="true">
				<u-avatar size="60" bg-color='#000;'  :src="avatarUrl" @click="chooseAvatar" style="position: absolute;right: 35px;top:12px;"></u-avatar>
			</u-cell-item>
			<u-cell-item title="公司" value="科技有限公司" :title-width="180" :arrow="false"></u-cell-item>
			<u-cell-item title="所属部门"  value="人事部" :title-width="180" :arrow="false"></u-cell-item>
			<u-cell-item title="岗位"  value="秘书" :title-width="180" :arrow="false"></u-cell-item>
			<u-cell-item title="手机"  value="15389457063" :title-width="180" @click="navTo('/pages/sys/user/modify')"></u-cell-item>
			<u-cell-item title="邮箱"  :title-width="180"></u-cell-item>
			<u-gap height="20" bg-color="#f5f5f5"></u-gap>
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
			model: {
				sex: '1'
			},
			rules: {

			},
			avatarBase64: ''
		};
	},
	onLoad() {
		this.$u.api.getUserInfo().then(res => {
			if (res.code == '200'){
				this.model = {
					id: res.user.id,
					name: res.user.name,
					sex: res.user.sex,
					email: res.user.email,
					phonenumber: res.user.phonenumber,
					officeTel: res.user.officeTel,
					loginDate: res.user.loginDate,
					loginIp: res.user.loginIp
			  }
			}else if (res.result == 'login'){
				uni.reLaunch({
					url: '/pages/sys/login/index'
				});
			}else{
				this.$u.toast(res.message);
			}
		});
		uni.$on('uAvatarCropper', path => {
			this.avatarBase64 = path;
		})
	},
	computed: {
		avatarUrl() {
			if (this.avatarBase64 != ''){
				return this.avatarBase64;
			}
			let url = this.vuex_config.baseUrl+ this.vuex_user.avatar ||  '/static/aidex/tabbar/my_2.png';
			url = this.replaceAll(url,'\\','/');
			//url = url.replace('/aidex/', this.vuex_config.baseUrl + '/');
			/* alert(url); */
			return url;
		}
	},
	onReady() {
		this.$refs.uForm.setRules(this.rules);
	},
	methods: {
		chooseAvatar() {
			this.$u.route({
				url: '/uview-ui/components/u-avatar-cropper/u-avatar-cropper',
				params: {
					destWidth: 200, // 输出图片宽高
					rectWidth: 200, // 裁剪框的宽高
					fileType: 'jpg', // 输出的图片类型，如果'png'类型发现裁剪的图片太大，改成"jpg"即可
				}
			})
		},
		submit() {
			this.$refs.uForm.validate(valid => {
				if (valid) {
					// #ifdef MP-WEIXIN || MP-TOUTIAO
					this.$u.toast('您填写的信息有误，11。');
					if (this.avatarBase64 != '' && !this.avatarBase64.startsWith('data:')){
						this.avatarBase64 = 'data:image/jpeg;base64,' + uni.getFileSystemManager()
								.readFileSync(this.avatarBase64, "base64")
					}
					// #endif
					this.model.avatarBase64 = this.avatarBase64;
					this.$u.api.user.saveUserInfo(this.model).then(res => {
						if(res.code == '200'){
							this.vuex_user.avatar = res.imgUrl;
							this.$u.vuex('vuex_user', this.vuex_user);
							uni.showModal({
								title: '提示',
								content: res.msg,
								showCancel: false,
								success: function () {
								   uni.navigateBack();
								}
							});
						}else{
							this.$u.toast(res.msg);
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
<style lang="scss" scoped>
	page{
		background: #f5f5f5;
	}
	.u-form{
		background: #fff;
		padding:0 15px;
	}
.u-size-medium{
    height: 60rpx;
    line-height: 60rpx;
    padding: 0 20px;
	font-size:28rpx;
	background-color: rgba(0, 0, 0, 0.1);
	color:#fff;
	border: 1px solid #5186e7;
}
.u-hairline-border:after{
	border: 1px solid #5186e7!important;
}
.input-placeholder{
	text-align: right;
}
.u-input{
	text-align: right !important;
}
.u-form-item{
	font-size:36rpx;
}
</style>
