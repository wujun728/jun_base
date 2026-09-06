<template>
	<view class="content">
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<uni-nav-bar left-text="取消" @clickLeft="loginCancel">
				<view class="uni-nav-bar-text">{{typeIndex === 0 ? '扫码失败' : '扫码成功'}}</view>
			</uni-nav-bar>
		</view>
		<view class="code-content">
			<view class="code-tips">
				<template v-if="typeIndex === 1">
					<image class="image" src="@/static/image/users/code-success.png" mode=""></image>
					<view class="code-tips-text">派瑞集团登录确认</view>
				</template>
				<template v-else>
					<image class="image" src="@/static/image/users/code-filled.png" mode=""></image>
					<view class="code-tips-text">请认准派瑞集团二维码</view>
					<view class="filled-text">
						派瑞集团管理系统无法处理其他二维码，请扫描网页版登录二维码进行的登录：{{HTTP_REQUEST_URL}}
					</view>
				</template>

			</view>

			<view class="code-button">
				<template v-if="typeIndex === 1">
					<button class="button" type="primary" :loading="loading" @click="ok">确认并登录</button>
					<button class="button cancel" @click="loginCancel">取消登录</button>
				</template>
				<template v-else>
					<button class="button" type="primary" @click="loginCancel">返回</button>
				</template>

			</view>
		</view>
	</view>
</template>

<script setup lang="ts">
	import { ref,type Ref } from 'vue'
	import { useStore } from 'vuex'
	import { HTTP_REQUEST_URL } from '@/config/app'
	import { onLoad } from '@dcloudio/uni-app'
	import { userScanLoginApi } from '@/api/user'
	const loading : Ref<boolean> = ref(false)
	const typeIndex : Ref<number> = ref(0)
	const store = useStore()

	interface O {
		type : number
	}
	onLoad((options : O) : void => {
		typeIndex.value = Number(options.type)
	})

	const cancel = () : any => {
		uni.navigateBack()
	}
	const getScanLogin = (data : object) : void => {
		userScanLoginApi(data).then((res : object) => {
			cancel()
		}).catch((error : object) => {
			console.log(error)
		})
	}

	// 确认登录
	const ok = () : void => {
		const data : object = {
			key: store.state.app.codeSuccessKey,
			status: 1
		}
		getScanLogin(data)
	}

	// 取消
	const loginCancel = () : void => {
		const data : object = {
			key: store.state.app.codeSuccessKey,
			status: 0
		}
		getScanLogin(data)
	}
</script>

<style scoped lang="scss">
	.cr-position-header {
		position: sticky;

		::v-deep .uni-navbar {
			.uni-navbar__header-container {
				display: flex;
				align-items: center;
				justify-content: center;
			}

			.uni-navbar-btn-text {
				uni-text {
					padding-top: 8rpx;
					font-size: 32rpx !important;
					color: $uni-color-primary !important;
				}
			}

			.uni-nav-bar-text {
				font-size: 34rpx;
				font-weight: 500;
			}

			.uni-navbar__header-btns-right {
				justify-content: flex-end;
			}
		}
	}

	.code-content {
		width: 100%;

		.code-tips {
			display: flex;
			width: 80%;
			margin: 120rpx auto 0 auto;
			justify-content: center;
			align-items: center;
			flex-direction: column;

			.success-icon {
				color: $uni-color-primary !important;
			}

			.filled-icon {
				color: $uni-color-error !important;
			}

			.image {
				width: 260rpx;
				height: 260rpx;
			}

			.code-tips-text {
				padding-top: 20rpx;
				color: $uni-text-color;
				font-size: 34rpx;
			}

			.filled-text {
				padding-top: 10rpx;
				color: $nui-text-color-two;
				font-size: $uni-font-size-default;
				line-height: 1.5;
			}
		}



		.code-button {
			width: 100%;
			position: fixed;
			left: 0;
			bottom: 180rpx;

			.button {
				width: 50%;
				margin: 0 auto;
				height: 86rpx;
				line-height: 86rpx;
				font-size: $uni-font-size-default;
				margin-bottom: 30rpx;

				&:last-of-type {
					margin-bottom: 0;
				}

				&.cancel {
					color: $uni-color-primary;
					background-color: rgba(0, 0, 0, 0);

					&::after {
						border: none;
					}
				}

			}


		}


	}
</style>