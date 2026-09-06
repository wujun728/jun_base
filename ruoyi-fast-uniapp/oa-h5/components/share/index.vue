<template>
	<view>
		<uni-popup ref="popupRef" type="bottom" :mask-click="false">
			<view class="slider">
				<view class="share-header">
					<image class="image" src="/static/image/share-bg.png" mode=""></image>
					<view class="share-title">
						分享文章
					</view>
					<view @click="cancel" class="iconfont icon-shenpizhongxin-jujue"></view>
				</view>
				<view class="share-list">
					<view class="share-list-item" v-for="item in data.listData" :key="item.type">
						<image class="logo" :src="item.image" mode=""></image>
						<view class="share-name">{{item.text}}</view>
					</view>
				</view>
				<view class="tips">友情提示：第三方 SDK 可能会收集您的个人信息</view>
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
		}
	} )
	const { checkedTypes, typeData } = toRefs( props )

	const emit = defineEmits( [ "handleItem" ] );

	const popupRef = ref( null )

	const styleId = ref( 1 )

	const data = reactive( {
		listData: [
			{ type: 1, image: '/static/image/share-wechat.png', text: '微信好友' },
			{ type: 2, image: '/static/image/share-wechat-m.png', text: '朋友圈' },
			{ type: 3, image: '/static/image/share-qq.png', text: 'QQ' },
			{ type: 4, image: '/static/image/share-weibo.png', text: '微博分享' },
			{ type: 5, image: '/static/image/share-link.png', text: '复制链接' },
		]
	} )

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
		height: 606rpx;
		width: 100%;
		background-color: #fff;
		border-radius: 16rpx 16rpx 0px 0px;

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

		.share-list {
			width: 100%;
			display: flex;
			align-items: center;
			flex-wrap: wrap;

			.share-list-item {
				width: 25%;
				text-align: center;
				margin-bottom: 48rpx;

				.logo {
					width: 100rpx;
					height: 100rpx;
				}

				.share-name {
					font-size: 26rpx;
					font-weight: 400;
					color: $nui-text-color-two;
				}
			}
		}

		.tips {
			font-size: 24rpx;
			font-weight: 400;
			color: $nui-text-color-four;
			position: absolute;
			bottom: 30rpx;
			width: 100%;
			text-align: center;
		}
	}
</style>
