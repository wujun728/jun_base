<template>
	<view>
		<uni-popup ref="popupRef" type="bottom" :mask-click="true" @change="changeShow">
			<view class="slider">
				<view class="share-header">
					<image class="image" src="/static/image/share-bg.png" mode=""></image>
					<view class="share-title">
						添加成员
					</view>
					<view @click="cancel" class="iconfont icon-shenpizhongxin-jujue"></view>
				</view>
				<view class="share-list">
					<view class="share-list-item" v-for="item in data.listData" :key="item.type" @click="shareListItem(item)">
						<image class="logo" :src="item.image" mode=""></image>
						<view class="share-name">{{item.text}}</view>
					</view>
				</view>
				<view class="tips">友情提示：第三方 SDK 可能会收集您的个人信息</view>
			</view>
		</uni-popup>

		<code-invitation ref="codeInvitationRef"></code-invitation>
	</view>
</template>

<script setup>
	import message from '@/utils/message'
	import codeInvitation from './codeInvitation'
	import { clickNavigateTo } from '@/utils/helper'
	import { HTTP_REQUEST_URL } from '@/config/app'

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
	const codeInvitationRef = ref( null )

	const styleId = ref( 1 )

	const data = reactive( {
		listData: [
			{ type: 1, image: '/static/image/share-wechat.png', text: '微信好友', url: '' },
			{ type: 2, image: '/static/image/share-qq.png', text: 'QQ', url: '' },
			{ type: 3, image: '/static/image/share-link.png', text: '复制链接', url: '' },
			{ type: 4, image: '/static/image/share-image.png', text: '二维码分享' },
			{ type: 5, image: '/static/image/share-phone.png', text: '手机号添加', url: '/pages/user/userPhone' },
			{ type: 6, image: '/static/image/share-id.png', text: 'ID添加', url: '/pages/user/userId' },
			{ type: 7, image: '/static/image/record.png', text: '添加记录', url: '/pages/user/userRecord' },
		]
	} )

	// 打开弹出
	const popupOpen = () => {
		popupRef.value.open()
	}

	// 关闭
	const cancel = () => {
		popupRef.value.close()
		isClickImage.value = false
	}

	const isClickImage = ref( false )
	// 关闭
	const changeShow = ( e ) => {
		if ( !e.show && isClickImage.value ) {
			codeInvitationRef.value.popupOpen()
		}
	}

	// 展现形式选择
	const shareListItem = ( item ) => {
		if ( item.type >= 5 ) {
			clickNavigateTo( item.url )
			cancel()
		} else if ( item.type === 3 ) {
			// 复制链接
			uni.setClipboardData( {
				data: HTTP_REQUEST_URL + '/work/pages/workbench/index',
				showToast: false,
				success: () => {
					message.success( '链接已复制成功' )
					cancel()
				}
			} )
		} else if ( item.type === 4 ) {
			// 生成海报
			isClickImage.value = true
			cancel()
		}
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