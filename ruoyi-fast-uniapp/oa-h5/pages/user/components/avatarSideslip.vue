<template>
	<view>
		<uni-popup ref="popupRef" type="left" :mask-click="true" @maskClick="maskClick">
			<view class="slider plr10">
				<view class="slider-header">
					<avatar :src="userInfo.avatar" @change="clickNavigate('/pages/users/center/index')" :auto-size="false" :width="100" :height="100" :radius="10"></avatar>
					<view class="capitin" @click="caption">
						<view class="username">
							<text class="">{{userInfo.real_name}}</text>
							<uni-icons type="right"></uni-icons>
						</view>
						<!-- <text class="job">UI设计工程师</text> -->
					</view>
				</view>
				<view class="enterprise">
					<scroll-view class="scroll-per" scroll-y :scroll-top="0">
						<view class="enterprise-list" :class="item.entid === userInfo.entid ? 'active' : ''" v-for="item in config.enterprise" :key="item.entid">
							<uni-row class="display-align">
								<uni-col :span="20" class="header-left">
									<view class="avatar">
										<text class="iconfont icon-zuzhijiagou"></text>
									</view>
									<text class="title line1">{{item.enterprise.enterprise_name}}</text>
								</uni-col>
								<uni-col :span="4" class="text-right">
									<text v-if="item.entid === userInfo.entid" class="iconfont icon-xuanzhong  list-select-icon"></text>
									<uni-badge v-else class="uni-badge-left-margin list-badge" text="123" type="info" />
								</uni-col>
							</uni-row>
						</view>
					</scroll-view>
				</view>

				<view class="slider-bottom">
					<view class="caption" @click="logout"><text class="iconfont icon-touxiangcehua-tuichudenglu"></text>退出登录</view>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script setup lang="ts">
	import { logoutApi } from '@/api/public'
	import avatar from '@/components/avatar/index.vue'
	import message from '@/utils/message'
	import { useStore } from 'vuex'
	import type { Res } from '@/utils/typeHelper'
	import { showModal, clickNavigateTo } from '@/utils/helper'

	const store = useStore()
	const userInfo = computed(() => store.state.app.userInfo)

	let emit = defineEmits(['change'])
	// 打开弹出
	let isFirstOpen = ref(false)
	const popupRef = ref()
	const popupOpen = () => {
		popupRef.value.open()
		if (!isFirstOpen.value) {
			getClientTop()
		}
		emit('change', true)
	}

	// 关闭验证码
	const cancel = () => {
		popupRef.value.close()
		emit('change', false)
	}

	// 关闭验证码
	const maskClick = () => {
		emit('change', false)
	}

	const caption = () : void => {
		setTimeout(() => {
			clickNavigateTo('/pages/users/center/index')
		}, 200)
		emit('change', false)
	}

	const instance = getCurrentInstance() // 获取组件实例
	const entHeight : Ref<number | string> = ref(0)
	// 获取默认页面高度
	const getClientTop = async () : Promise<void> => {
		await nextTick()
		let query = uni.createSelectorQuery().in(instance)
		query.select('.slider-header').fields({ size: true }, () => { })
		query.select('.slider-bottom').fields({ size: true }, () => { })
		query.exec((data) => {
			data.map((value : any) => {
				entHeight.value += value.height
			})
			entHeight.value = Math.floor(Number(entHeight.value)) + 'px'
			isFirstOpen.value = true
		})
	}

	// 获取加入企业列表
	const config = reactive({
		enterprise: <any>[]
	})

	const clickNavigate = (url : string) : void => {
		emit('change', false)
		clickNavigateTo(url)
		cancel()
	}

	// 退出登录
	const logout = () : void => {
		showModal('确认退出登录').then(() => {
			logoutApi().then((res : Res) => {
				if (res.status === 200) {
					store.commit('logout')
					uni.reLaunch({
						url: '/pages/users/login/index'
					})
				}
			}).catch((error : Res) => {
				message.error(error.message)
			})
		}).catch(() => { })
	}

	defineExpose({ popupOpen })
</script>

<style lang="scss" scoped>
	::v-deep .uni-popup {
		z-index: 999;
	}

	::v-deep .uni-popup__wrapper.left {
		padding-top: 0 !important;
	}

	.slider {
		height: 100vh;
		width: calc(100vw - 170rpx);
		background-color: #fff;

		.slider-header {
			display: flex;
			align-items: center;
			// #ifndef APP-PLUS
			padding-top: 40rpx;
			// #endif
			// #ifdef APP-PLUS
			padding-top: 138rpx;
			// #endif

			.avatar {
				width: 100rpx;
				height: 100rpx;
				border-radius: 10rpx;
			}

			.capitin {
				padding-left: 28rpx;

				uni-text {
					display: block;
				}

				.username {
					font-size: 36rpx;
					color: #2B2C32;
					font-weight: 600;
					display: flex;
					align-items: center;

					.uni-icons {
						font-weight: normal;
						font-size: 22rpx !important;
						color: #CCCCCC !important;
						padding-left: 14rpx;
					}
				}

				.job {
					padding-top: 16rpx;
					font-size: 26rpx;
					color: #909399;
				}
			}
		}

		.enterprise {
			padding-top: 44rpx;
			padding-bottom: 44rpx;
			// vue3新特性
			height: calc(100vh - 66rpx - v-bind(entHeight));

			.scroll-per {
				height: 100%;
			}

			.enterprise-list {
				padding: 20rpx 32rpx 20rpx 20rpx;
				margin-bottom: 10rpx;

				.header-left {
					display: flex;
					align-items: center;

					.avatar {
						width: 80rpx;
						height: 80rpx;
						border-radius: 8rpx;
						background: linear-gradient(203deg, rgba(66, 172, 249, 0.15) 0%, rgba(44, 132, 247, 0.15) 100%);
						text-align: center;
						line-height: 80rpx;

						uni-text {
							font-size: 60rpx;
							color: #318CF8;
						}
					}

					.title {
						padding-left: 20rpx;
						font-size: 28rpx;
						width: calc(100% - 100rpx);
						color: #303133;
						font-weight: 600;
					}
				}

				.list-select-icon {
					font-size: 28rpx;
				}

				.list-badge {
					::v-deep .uni-badge {
						background-color: #C0C4CC;
					}
				}

				&.active {
					background-color: #F0F1F5;
					border-radius: 8rpx;

					.list-select-icon {
						color: #1890FF;
					}
				}
			}
		}

		.slider-bottom {
			// #ifndef APP-PLUS
			position: fixed;
			// #endif
			// #ifdef APP-PLUS
			position: absolute;
			// #endif
			left: 30rpx;
			bottom: 66rpx;
			padding-bottom: env(safe-area-inset-bottom);

			.caption {
				font-size: 24rpx;
				color: #909399;
				padding-bottom: 44rpx;
				display: flex;
				align-items: center;

				uni-text {
					padding-right: 14rpx;
				}

				&:last-of-type {
					padding-bottom: 0;
				}
			}
		}
	}
</style>