<template>
	<view>
		<uni-popup ref="popupRef" type="dialog" :mask-click="true">
			<uni-popup-dialog ref="inputClose" mode="input" :before-close="true" :title="title" @close="cancel" @confirm="dialogInputConfirm">
				<uni-data-checkbox v-model="type" :multiple="false" :localdata="range"></uni-data-checkbox>
			</uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script setup lang="ts">
	import { ref, reactive, toRefs } from 'vue'
	const props = defineProps({
		title: {
			type: String,
			default: ''
		},
	})

	const emit = defineEmits(['changeOk']);

	const { title } = toRefs(props)

	const popupRef = ref(null)

	const type = ref(0)
	const range = reactive([
		{ value: 0, text: "此日程" },
		{ value: 1, text: "此日程及后续日程" },
		{ value: 2, text: "所有日程" }
	])

	const popupOpen = () => {
		popupRef.value.open()
	}

	// 关闭验证码
	const cancel = () => {
		popupRef.value.close()
	}

	const dialogInputConfirm = () => {
		emit('changeOk', type.value)
		cancel()
	}

	defineExpose({ popupOpen })
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

	::v-deep .checklist-box {
		width: 100%;
		margin-right: 0 !important;
		margin-bottom: 10px !important;
	}
</style>