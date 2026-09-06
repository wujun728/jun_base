<template>
	<view class="content">
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<default-nav-bar></default-nav-bar>
		</view>
		<view class="assessment plr10">
			<view class="content-info">
				<text v-html="data.content"></text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import defaultNavBar from '@/components/defaultNavBar/index'
	import message from '@/utils/message'
	const ident = ref( 'service_agreement' )
	const data = reactive( {
		content: ''
	} )
	onMounted( () => {
		getAgreement()
	} )
	import { commonAgreementApi } from '@/api/public'

	const getAgreement = () => {
		commonAgreementApi( ident.value ).then( ( res ) => {
			data.content = res.data.content
		} ).catch( ( error ) => {
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
	.content {
		width: 100%;

		.cr-position-header {
			position: sticky;
			background-color: #fff;
		}

		.assessment {
			.title {
				padding-top: 20rpx;
				font-size: 36rpx;
				color: #2B2C32;
				font-weight: 500;
				padding-bottom: 40rpx;
			}

			.content-info {
				font-size: $uni-font-size-default;
				line-height: 2;
			}
		}
	}
</style>
