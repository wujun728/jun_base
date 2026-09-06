<template>
	<view>
		<uni-popup ref="popupRef" type="bottom" :mask-click="false">
			<view class="slider">
				<view class="share-header">
					<view class="share-title">{{title}}</view>
					<view @click="cancel" class="iconfont icon-shenpizhongxin-jujue"></view>
				</view>
				<view class="share-content plr10">
					<view class="share-content-item">
						<text class="text">图片</text>
						<image class="image" src="@/static/image/cloudfile/image.png" mode=""></image>
					</view>
					<view class="share-content-item">
						<text class="text">文档</text>
						<image class="image" src="@/static/image/cloudfile/folder.png" mode=""></image>
					</view>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script setup>
	import { ref, reactive, toRefs } from 'vue'
	import message from '@/utils/message'
	const props = defineProps( {
		typeData: {
			type: Object,
			default: () => {
				return []
			}
		},
		title: {
			type: String,
			default: ''
		}
	} )
	const { typeData, title } = toRefs( props )
	const emit = defineEmits( [ 'changeItem' ] )
	const popupRef = ref( null )
	const styleId = ref( 1 )
	const data = reactive( {
		listData: [],
		selectLabelData: [],
		selectLabelName: []
	} )

	// 打开弹出
	const popupOpen = val => {
		popupRef.value.open()
	}

	// 关闭
	const cancel = () => {
		popupRef.value.close()
	}

	// 标签确认
	const confirm = () => {
		if ( data.selectLabelData.length <= 0 ) {
			message.error( '至少选个一个' + title.value )
			return false
		}
		emit( 'changeItem', data.selectLabelData, data.selectLabelName )
		cancel()
	}
	defineExpose( { popupOpen } )
</script>

<style lang="scss" scoped>
	::v-deep .uni-popup {
		z-index: 100;
	}

	.slider {
		position: relative;
		height: 340rpx;
		width: 100%;
		background-color: #fff;
		border-radius: 20rpx 20rpx 0px 0px;

		.share-header {
			width: 100%;
			height: 134rpx;
			position: relative;

			.image {
				width: 100%;
				height: 100%;
			}

			.share-title {
				position: absolute;
				top: 38rpx;
				left: 50%;
				transform: translate(-50%, 0);
				font-size: 30rpx;
				font-weight: 600;
				color: $uni-text-color;
			}

			.iconfont {
				font-size: 40rpx;
				color: #C0C4CC;
				position: absolute;
				top: 30rpx;
				right: 32rpx;
			}
		}

		.share-content {
			width: 100%;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.share-content-item {
				margin-right: 30rpx;
				width: calc((100% - 30rpx)/2);
				height: 146rpx;
				border-radius: 8rpx;
				background-color: #F0F1F5;
				padding: 0 40rpx;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.text {
					color: $nui-text-color-two;
					font-size: 28rpx;
				}

				.image {
					width: 88rpx;
					height: 88rpx;
				}

				&:last-of-type {
					margin-right: 0;
				}
			}
		}
	}
</style>
