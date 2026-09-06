<template>
	<view>
		<uni-popup ref="popupRef" type="dialog" :mask-click="true">
			<uni-popup-dialog ref="inputClose" mode="input" :before-close="true" :title="title" :value="editData.value" @close="cancel" @confirm="dialogInputConfirm">
				<view class="folder-image display-center">
					<image class="image" src="@/static/image/cloudfile/folder.png" mode=""></image>
				</view>
				<uni-easyinput v-model="folderName" :inputBorder="false" :maxlength="16" :placeholder="editData.placeholder" placeholderStyle="color: #C0C4CC;font-size: 30rpx;">
				</uni-easyinput>
			</uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script setup>
	import { ref, toRefs, watch } from 'vue'
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
	const folderName = ref( '' )

	const popupOpen = () => {
		popupRef.value.open()
	}

	// 关闭验证码
	const cancel = () => {
		popupRef.value.close()
	}

	const dialogInputConfirm = () => {
		if ( !folderName.value ) {
			message.error( '输入的内容不能为空' )
			return false
		}
		const data = {
			value: folderName.value,
			type: editData.value.type ? editData.value.type : 0
		}
		emit( 'handleOk', data )
		folderName.value = ''
		cancel()
	}

	watch(
		() => editData,
		( newvalue, oldvalue ) => {
			if ( newvalue.value.type === 1 ) {
				folderName.value = newvalue.value.title
			}
		}, { deep: true }
	)

	defineExpose( { popupOpen } )
</script>

<style lang="scss" scoped>
	.folder-image {
		width: 100%;

		.image {
			width: 100rpx;
			height: 100rpx;
		}
	}

	::v-deep .uni-popup-dialog {
		.uni-dialog-title {
			padding-top: 40rpx;

			.uni-dialog-title-text {
				font-size: $uni-font-size-default;
				font-weight: $uni-default-font-weight;
				color: #2B2C32;
			}
		}

		.uni-dialog-button-text {
			font-size: $uni-font-size-default;
		}
	}

	::v-deep .uni-dialog-content {
		padding: 40rpx 32rpx 50rpx 32rpx;
		flex-wrap: wrap;
	}

	::v-deep .uni-easyinput__content {
		background-color: #F0F1F5 !important;
		border-radius: 12rpx;
		margin-top: 40rpx;

		.uni-input-input {
			font-size: 28rpx;
		}

		.content-clear-icon {
			color: #C0C4CC !important;
		}
	}
</style>
