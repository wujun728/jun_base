<script setup>
import { ref } from 'vue'

const show = ref(false)
const password = ref('')

const onChange = (val) => {
	if (password.value.length < 6) {
		password.value += val
	}

	if (password.value.length >= 6) {
		pay()
	}
}

const onBackspace = () => {
	if (password.value.length > 0) {
		password.value = password.value.substring(0, password.value.length - 1)
	}
}

const pay = () => {
	uni.showLoading({ title: '支付中' })
	setTimeout(() => {
		uni.hideLoading()
		show.value = false
		uni.showToast({
			icon: 'success',
			title: '支付成功'
		})
	}, 2000)
}

const showPop = (flag = true) => {
	password.value = ''
	show.value = flag
	// #ifdef H5 || WEB
	setTimeout(() => {
		const btn = document.querySelector('.u-keyboard__button-wrapper__button.u-keyboard__button-wrapper__button--gray')
		if (btn) {
			console.log(btn)
			btn.onclick = onBackspace
		}
	}, 500)
	// #endif
}

const finish = () => {
	console.log(11111)
}
</script>
<template>
	<view>
		<view class="u-padding-40">
			<u-button type="success" @click="showPop(true)">
				<u-icon name="red-packet"></u-icon>
				<text class="u-padding-left-10">发送1.00元红包</text>
			</u-button>
		</view>
		<u-keyboard default="" ref="uKeyboard" mode="number" :mask="true" :mask-close-able="false" :dot-enabled="false"
			:show="show" :safe-area-inset-bottom="true" :tooltip="false" @change="onChange" @backspace="onBackspace">
			<view>
				<view class="u-text-center u-padding-20 money">
					<text>1.00</text>
					<text class="u-font-20 u-padding-left-10">元</text>
					<view class="u-padding-10 close" data-flag="false" @tap="showPop(false)">
						<u-icon name="close" color="#333333" size="28"></u-icon>
					</view>
				</view>
				<view class="u-flex u-row-center">
					<u-message-input mode="box" :maxlength="6" :dot-fill="true" v-model="password"
						:disabled-keyboard="true" @finish="finish"></u-message-input>
				</view>
				<view class="u-text-center u-padding-top-10 u-padding-bottom-20 tips">支付键盘</view>
			</view>
		</u-keyboard>
	</view>
</template>

<style lang="scss">
.money {
	font-size: 80rpx;
	color: $u-warning;
	position: relative;

	.close {
		position: absolute;
		top: 20rpx;
		right: 20rpx;
		line-height: 28rpx;
		font-size: 28rpx;
	}
}

.tips {
	color: $u-tips-color;
}
</style>