<script setup>
import { ref, onMounted } from 'vue';

const maxlength = ref(4);
const value = ref('');
const second = ref(3);
const show = ref(false);
const error = ref(false);

onMounted(() => {
	let interval = setInterval(() => {
		second.value--;
		if (second.value <= 0) {
			show.value = true;
			if (value.value.length !== 4) {
				error.value = true;
			}
			clearInterval(interval);
		}
	}, 1000);
});

const noCaptcha = () => {
	uni.showActionSheet({
		itemList: ['重新获取验证码', '接听语音验证码'],
		success: function (res) {
			// 处理成功逻辑
		},
		fail: function (res) {
			// 处理失败逻辑
		}
	});
};

const change = (value) => {
	// console.log('change', value);
};

const finish = (value) => {
	// console.log('finish', value);
};
</script>
<template>
	<view class="wrap">
		<view class="key-input">
			<view class="title">输入验证码</view>
			<view class="tips">验证码已发送至 +150****9320</view>
			<u-message-input :focus="true" :value="value" @change="change" @finish="finish" mode="bottomLine"
				:maxlength="maxlength"></u-message-input>
			<text :class="{ error: error }">验证码错误，请重新输入</text>
			<view class="captcha">
				<text :class="{ noCaptcha: show }" @tap="noCaptcha">收不到验证码点这里</text>
				<text :class="{ regain: !show }">{{ second }}秒后重新获取验证码</text>
			</view>
		</view>
	</view>
</template>

<style lang="scss" scoped>
.wrap {
	padding: 80rpx;
}

.box {
	margin: 30rpx 0;
	font-size: 30rpx;
	color: 555;
}

.key-input {
	padding: 30rpx 0;

	text {
		display: none;
	}

	.error {
		display: block;
		color: red;
		font-size: 30rpx;
		margin: 20rpx 0;
	}
}

.title {
	font-size: 50rpx;
	color: #333;
}

.key-input .tips {
	font-size: 30rpx;
	color: #333;
	margin-top: 20rpx;
	margin-bottom: 60rpx;
}

.captcha {
	color: $u-warning;
	font-size: 30rpx;
	margin-top: 40rpx;

	.noCaptcha {
		display: block;
	}

	.regain {
		display: block;
	}
}
</style>
