<template>
	<view>
		<uni-popup ref="popupRef" type="center" :mask-click="true">
			<view class="slider">
				<view class="share-header">
					<image class="image" :src="assessTips" mode=""></image>
				</view>
				<view class="share-list">
					<view class="title">评分标准</view>
					<view class="info">{{explain}}</view>
					<view class="explain-button-bottom">
						<button type="primary" class="explain-button" @click="cancel">我知道了</button>
					</view>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script setup>
	import { ref, toRefs } from 'vue'
	import message from '@/utils/message'
	const props = defineProps( {
		explain: {
			type: String,
			default: '暂无填写考核说明'
		}
	} )
	import assessTips from '../../static/image/assess-tips.png'
	const { explain } = toRefs( props )

	const emit = defineEmits( [ "handleItem" ] );

	const popupRef = ref( null )

	// 打开弹出
	const popupOpen = () => {
		popupRef.value.open()
	}

	// 关闭
	const cancel = () => {
		popupRef.value.close()
	}

	// 展现形式选择
	const clickStyleItem = () => {
		message.error( '目前仅支持单日展示，正在制作中......' )
	}


	defineExpose( { popupOpen } )
</script>

<style lang="scss" scoped>
	::v-deep .uni-popup {
		z-index: 100;
	}

	.slider {
		position: relative;
		height: auto;
		width: 560rpx;
		border-radius: 16rpx;

		.share-header {
			width: 100%;
			height: 166rpx;

			.image {
				width: 100%;
				height: 100%;
			}
		}

		.share-list {
			width: 100%;
			background-color: #fff;
			position: relative;
			top: -2px;
			padding: 52rpx 40rpx 50rpx 40rpx;
			border-radius: 0 0 16rpx 16rpx;

			.title {
				font-size: 36rpx;
				line-height: 1.5;
				font-weight: $uni-default-font-weight;
				color: #282828;
				text-align: center;
				position: absolute;
				top: -20rpx;
				left: 50%;
				transform: translate(-50%, 0);
			}

			.info {
				font-size: $uni-font-size-default;
				color: $nui-text-color-two;
				line-height: 1.5;
			}

			.explain-button-bottom {
				width: 100%;
				margin-top: 50rpx;

				.explain-button {
					margin: 0 auto;
					width: 332rpx;
					height: 86rpx;
					line-height: 86rpx;
					padding: 0;
					font-size: $uni-font-size-default;
				}
			}
		}
	}
</style>
