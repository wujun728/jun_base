<template>
	<view class="content">
		<!-- #ifdef APP-PLUS -->
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<default-nav-bar :is-show-title="false"></default-nav-bar>
		</view>
		<!-- #endif -->

		<view class="assessment plr10">
			<view class="title">请输入新手机号</view>

			<view class="cr-form">
				<uni-forms ref="forms" label-position="top">
					<uni-forms-item>
						<template v-slot:label>
							<view class="uni-forms-item__label">
								<text class="label-item">手机号码</text>
								<text class="iconfont">*</text>
							</view>
						</template>
						<uni-easyinput v-model="formData.phone" :clearable="false" @input="inputPhone" :styles="{borderColor: 'transparent'}" placeholder="请输入手机号码"></uni-easyinput>
					</uni-forms-item>

					<uni-forms-item class="forms-item-text">
						<template v-slot:label>
							<view class="uni-forms-item__label">
								<text class="label-item">确认密码</text>
								<text class="iconfont">*</text>
							</view>
						</template>
						<uni-easyinput v-model="formData.verification_code" :clearable="false" :styles="{borderColor: 'transparent'}" placeholder="请输入确认密码">
						</uni-easyinput>
						<text class="item-text" :class="isSendCode && !disabled ? 'default-color': 'default-text-color-four'" @click="sendVerificationCode">{{text}}</text>
					</uni-forms-item>
				</uni-forms>
				<button type="primary" :disabled="!(isSendCode && formData.verification_code.length >= 4)" :loading="loading" @click="handlePreserve">提交</button>
				<image class="logo" :src="phoneImg" mode=""></image>
			</view>
		</view>
	</view>
</template>

<script setup>
	import defaultNavBar from '@/components/defaultNavBar/index'
	import { ref, reactive } from "vue"
	import message from '@/utils/message'
	import phoneImg from '../static/image/phone.png'

	import { useSendCode, useCmsKeyVerify } from '@/utils/useVerifyCode'
	let { text, disabled } = useSendCode()
	// 发送验证码逻辑
	let { getKeyVerify } = useCmsKeyVerify()

	// 手机号码验证
	import { phoneReg } from '@/utils/helper'

	let isSendCode = ref( false )
	const inputPhone = ( e ) => {
		isSendCode.value = phoneReg.test( e )
	}

	let formData = reactive( {
		phone: '',
		verification_code: '',
		avatar: '',
		email: '',
		real_name: '',
		uid: ''
	} )


	const sendVerificationCode = () => {
		if ( !isSendCode.value ) return false
		getKeyVerify( formData.phone )
	}

	const handlePreserve = () => {
		saveCenterUser( formData )
	}


	import { userUserInfoEditApi } from '@/api/user'
	import { delayedNavigateTo } from '@/utils/helper'
	const loading = ref( false )
	// 保存用户信息
	const saveCenterUser = ( data ) => {
		loading.value = true
		userUserInfoEditApi( data ).then( res => {
			loading.value = false
			message.success( res.message )
			delayedNavigateTo( '/pages/users/center/index' )
		} ).catch( ( error ) => {
			loading.value = false
			message.error( error.message )
		} )
	}
</script>

<style>
	page {
		background-color: #fff;
	}
</style>

<style scoped lang="scss">
	@import '@/static/css/login-form.scss';

	.content {
		width: 100%;

		.assessment {
			// #ifdef APP-PLUS
			padding-top: calc($uni-default-bar-height + var(--status-bar-height));
			// #endif
			position: relative;

			.title {
				margin-top: 52rpx;
				font-size: 44rpx;
				color: $uni-text-color;
				font-weight: 600;
			}

			.logo {
				position: fixed;
				right: 0;
				bottom: 0;
				width: 300rpx;
				height: 400rpx;
			}
		}
	}
</style>
