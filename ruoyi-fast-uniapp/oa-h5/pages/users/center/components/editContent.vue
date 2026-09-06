<template>
	<view>
		<uni-popup ref="popupRef" type="dialog" :mask-click="true">
			<uni-popup-dialog ref="inputClose" mode="input" :before-close="true" :title="title" :value="editData.value" :placeholder="editData.placeholder" @close="cancel"
				@confirm="dialogInputConfirm">
			</uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script setup>
	import { ref, reactive, toRefs } from 'vue'
	import message from '@/utils/message'
	import { mailboxReg } from '@/utils/helper'
	const props = defineProps( {
		title: {
			type: String,
			default: ''
		},
		editData: {
			type: Object,
			default: () => {
				return {}
			},
			required: true
		}
	} )

	const emit = defineEmits( [ 'handleOk' ] );

	const { title, editData } = toRefs( props )

	const popupRef = ref( null )

	const popupOpen = () => {
		popupRef.value.open()
	}

	// 关闭验证码
	const cancel = () => {
		popupRef.value.close()
	}


	const dialogInputConfirm = ( e ) => {
		if ( !e ) {
			message.error( '输入的内容不能为空' )
			return false
		}
		if ( editData.value.types && editData.value.types === 'email' && !mailboxReg.test( e ) ) {
			message.error( '输入的邮箱不合法' )
			return false
		}
		const data = {
			value: e,
			type: editData.value.type ? editData.value.type : 0
		}
		emit( 'handleOk', data )
		cancel()
	}


	defineExpose( { popupOpen } )
</script>

<style lang="scss" scoped>
	::v-deep .uni-popup-dialog {
		.uni-dialog-title {
			padding-top: 40rpx;

			.uni-dialog-title-text {
				font-size: 30rpx;
				font-weight: 600;
				color: #2B2C32;
			}

			.uni-dialog-content {
				padding: 40rpx 32rpx;
			}
		}

		.uni-dialog-button-text {
			font-size: 30rpx;
		}
	}

	::v-deep .uni-dialog-input {
		border: none;
		background-color: #F0F1F5;
		height: 66rpx;
	}
</style>
