<template>
	<view class="wrap" style="padding-top:50rpx;">
		<view class="reg-text">欢迎注册</view>
		<view class="list">
			<view class="list-call">
				<view class="iconfont icon-shouji" style="font-size: 22px;color:#5473e8;"></view>
				<u-field
					v-model="phoneNo"
					label="+86"
					placeholder="请输入手机号码"
					style="width: 100%;"
				>
				</u-field>
			</view>
		</view>
		<view class="button" @click="getCode()"><text>获取验证码</text></view>
		<view class="bottom-box">
			<view class="copyright">
				注册即代表您已阅读并同意<u-link href="#">用户协议</u-link> 与 <u-link href="#">隐私政策</u-link>
			</view>
		</view>
	</view>
</template>
<script>
export default {
	data() {
		return {
			phoneNo: '',
		};
	},
	onLoad() {
	},
	methods: {
		getCode(){
			uni.showLoading({
								title: '正在获取验证码',
								mask: true
							})
			this.$u.api.sendCode({
				phoneNo: this.phoneNo,
				validCodeType:'1'
			})
			.then(res => {
				if (res.code == '200') {
					setTimeout(() => {
						uni.navigateTo({
							url: '/pages/sys/login/registerCode?phoneNo='+this.phoneNo
						});
					}, 500);
				}else{
					this.$u.toast(res.msg);
				}
			});
		}
	}
};
</script>
<style lang="scss">
@import 'index.scss';

.agreement {
	display:flex;
	flex-direction:row;
	align-items:center;
	margin-left:90rpx;
	color:#666;
}
.uni-input-placeholder{
	font-size: 32rpx;
}

.uni-input-input{
	font-size: 32rpx;
}
.bottom-box{
	position: fixed;
	bottom:40rpx;
	text-align: center;
	width: 100%;
}
.copyright{
	text-align: center;
	color: #939393;
	width: 100%;
	font-size: 24rpx;
	.u-link{
		margin: 0 10rpx;
		font-size: 24rpx!important;
	}
}
</style>
