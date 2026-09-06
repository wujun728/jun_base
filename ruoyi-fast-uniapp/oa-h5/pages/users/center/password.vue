<template>
	<view class="content">
		<!-- #ifdef APP-PLUS -->
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<default-nav-bar :index="0" :is-show-title="false"></default-nav-bar>
		</view>
		<!-- #endif -->

		<view class="assessment plr10">
			<view class="title">请输入新密码</view>
			<view class="cr-form">
				<uni-forms ref="forms" label-position="top">
					<uni-forms-item>
						<template v-slot:label>
							<view class="uni-forms-item__label">
								<text class="label-item">新密码</text>
								<text class="iconfont">*</text>
							</view>
						</template>
						<uni-easyinput v-model="formData.password" type="password" :clearable="false" :styles="{borderColor: 'transparent'}" placeholder="请输入密码"></uni-easyinput>
					</uni-forms-item>

					<uni-forms-item>
						<template v-slot:label>
							<view class="uni-forms-item__label">
								<text class="label-item">确认密码</text>
								<text class="iconfont">*</text>
							</view>
						</template>
						<uni-easyinput v-model="formData.password_confirm" type="password" :clearable="false" :styles="{borderColor: 'transparent'}" placeholder="请输入确认密码">
						</uni-easyinput>
					</uni-forms-item>
				</uni-forms>
				<button type="primary" :disabled="handleDisabled()" :loading="loading" @click="handlePreserve">确认</button>
				<image class="logo" :src="passwordImg" mode=""></image>
			</view>
		</view>
	</view>
</template>

<script setup>
	import defaultNavBar from '@/components/defaultNavBar/index'
	import { ref, reactive } from "vue"
	import passwordImg from '../static/image/password.png'
	import message from '@/utils/message'
	import { delayedNavigateTo } from '@/utils/helper'

	let formData = reactive( {
		password: '',
		password_confirm: '',
		avatar: '',
		email: '',
		phone: '',
		real_name: '',
		uid: ''
	} )

	const handleDisabled = () => {
		return !( formData.password && formData.password_confirm && formData.password === formData.password_confirm && formData.password_confirm.length >= 6 &&
			formData.password.length >= 6 )
	}

	const handlePreserve = () => {
		if ( !formData.password ) {
			message.error( '密码不能为空' )
			return false
		}

		if ( !formData.password_confirm ) {
			message.error( '确认密码不能为空' )
			return false
		}

		if ( formData.password !== formData.password_confirm ) {
			message.error( '输入的密码不一致' )
			return false
		}

		saveCenterUser( formData )
	}

	import { userUserInfoEditApi } from '@/api/user'
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
