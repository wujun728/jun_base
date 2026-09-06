<template>
	<view class="content">
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<default-nav-bar :index="1"></default-nav-bar>
		</view>
		<view class="assessment m10">
			<uni-list :border="false" v-if="data.listData.length > 0">
				<uni-list-item v-for="item in data.listData" :key="'list'+item.id">
					<template v-slot:body>
						<view class="item-list">
							<uni-row class="item-list-top display-align">
								<uni-col :span="6" class="item-list-top-left">
									<image class="logo" src="/static/image/logo.png" mode=""></image>
								</uni-col>
								<uni-col :span="18" class="item-list-top-right">小北爱吃肉</uni-col>
							</uni-row>
							<uni-row class="item-list-content">
								<uni-col :span="6" class="left">手机号码</uni-col>
								<uni-col :span="18">{{item.phone}}</uni-col>
							</uni-row>
							<uni-row class="item-list-content">
								<uni-col :span="6" class="left">用户状态</uni-col>
								<uni-col :span="18">已同意</uni-col>
							</uni-row>
							<uni-row class="item-list-content">
								<uni-col :span="6" class="left">注册时间</uni-col>
								<uni-col :span="18">
									<uni-dateformat format="yyyy/MM/dd hh:mm" :date="item.created_at"></uni-dateformat>
								</uni-col>
							</uni-row>
							<uni-row class="item-list-content">
								<uni-col :span="12" style="padding-right: 16rpx;">
									<button class="default-color">编辑</button>
								</uni-col>
								<uni-col :span="12" style="padding-left: 16rpx;">
									<button class="default-error">删除</button>
								</uni-col>
							</uni-row>
							<image v-if="item.verify === 0" class="item-list-status" src="/static/image/ent-examine.png" mode=""></image>
							<image v-if="item.verify === 1" class="item-list-status" src="/static/image/passed.png" mode=""></image>
							<image v-if="item.verify === -1" class="item-list-status" src="/static/image/refuse.png" mode=""></image>
						</view>
					</template>
				</uni-list-item>
			</uni-list>
			<empty v-else :index="2" title="暂无邀请记录～"></empty>
		</view>
	</view>
</template>

<script setup lang="ts">
	import defaultNavBar from '@/components/defaultNavBar/index'
	import empty from '@/components/empty/index'
	import message from '@/utils/message'
	import { enterpriseListApi } from '@/api/user'
	import { useBarHeight } from '@/utils/useVerifyCode'
	import type { Res } from '@/utils/typeHelper'

	const { height, getBarHeight } = useBarHeight()
	const instance = getCurrentInstance() // 获取组件实例
	onMounted(() => {
		getBarHeight('.cr-position-header', instance)
		getAssessMine()
	})

	const data = reactive({
		listData: <any>[],
		where: {
			page: 1,
			limit: 0
		}
	})

	const listLoading : Ref<boolean> = ref(false)
	const getAssessMine = () : void => {
		enterpriseListApi(data.where).then((res : Res) => {
			data.listData = res.data
			uni.stopPullDownRefresh() //停止刷新
		}).catch((error : Res) => {
			uni.hideLoading()
			message.error(error.message)
		})
	}
	// 上拉加载
	onPullDownRefresh(() => {
		data.where.page = 1
		listLoading.value = false
		data.listData = []
		getAssessMine()
	})
</script>

<style scoped lang="scss">
	.content {
		width: 100%;

		.assessment {
			padding-top: v-bind(height);

			::v-deep .uni-list {
				background-color: $uni-default-bg;

				.uni-list-item {
					margin-bottom: 20rpx;
					border-radius: 8rpx;

					.uni-list-item__container {
						padding: 32rpx 24rpx 36rpx 24rpx;
					}
				}
			}

			.item-list {
				width: 100%;
				position: relative;

				.item-list-top {
					width: 100%;
					font-size: 32rpx;
					font-weight: 600;
					color: $uni-text-color;

					.item-list-top-left {
						width: 80rpx;
						height: 80rpx;

						.logo {
							width: 100%;
							height: 100%;
							border-radius: 8rpx;
						}
					}

					.item-list-top-right {
						width: calc(100% - 80rpx);
						padding-left: 20rpx !important;
					}
				}

				.item-list-content {
					padding-top: 24rpx;
					font-size: 28rpx;
					color: $uni-text-color;
					font-weight: 400;

					.left {
						color: $nui-text-color-four;
					}

					uni-button {
						height: 74rpx;
						line-height: 74rpx;
						font-size: 30rpx;
						background-color: #F0F1F5;

						&::after {
							border-radius: 8rpx;
							border: none;
						}
					}
				}

				.item-list-status {
					position: absolute;
					top: -32rpx;
					right: -24rpx;
					width: 160rpx;
					height: 188rpx;
				}
			}
		}
	}
</style>