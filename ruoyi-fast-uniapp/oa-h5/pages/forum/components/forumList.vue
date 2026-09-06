<template>
	<view>
		<view class="content">
			<uni-list :border="false" v-if="listData.length > 0">
				<uni-list-item v-for="(item,index) in listData" :key="'items'+index">
					<template v-slot:body>
						<template v-if="item.cover.length <= 0">
							<view class="item-list">
								<view class="item-list-content" @click="toDefault(item)">
									<view class="item-list-content-right width100">
										<view class="title line2" v-html="item.title"></view>
										<view class="image-none line2" v-html="item.info"></view>
										<view class="caption line1">
											<view class="item line1" v-html="item.author"></view>
											<view class="item line1">{{item.support}}点赞</view>
											<view class="item line1">{{item.collect}}收藏</view>
											<view class="item line1">{{item.visit}}浏览</view>
										</view>
									</view>
								</view>
							</view>
						</template>
						<template v-if="item.cover.length > 0 && item.cover.length< 3">
							<view class="item-list">
								<view class="item-list-content" @click="toDefault(item)">
									<uni-row>
										<uni-col :span="14" class="item-list-content-right image-one">
											<view class="title line2" v-html="item.title"></view>

											<view class="caption line1">
												<view class="item line1" v-html="item.author"></view>
												<view class="item line1">{{item.support}}点赞</view>
												<view class="item line1">{{item.collect}}收藏</view>
												<view class="item line1">{{item.visit}}浏览</view>
											</view>
										</uni-col>
										<uni-col :span="10" class="item-list-content-left">
											<image class="image" :src="item.cover[0].url" mode="aspectFill"></image>
										</uni-col>
									</uni-row>
								</view>
							</view>
						</template>
						<template v-if="item.cover.length >= 3">
							<view class="item-list">
								<view class="item-list-content" @click="toDefault(item)">
									<view class="item-list-content-right width100">
										<view class="title line2" v-html="item.title"></view>
										<view class="image-three">
											<image v-for="items in item.cover.slice(0,3)" :key="items.id" class="image-three-img" :src="items.url" mode="aspectFill"></image>
										</view>
										<view class="caption line1">
											<view class="item line1" v-html="item.author"></view>
											<view class="item line1">{{item.support}}点赞</view>
											<view class="item line1">{{item.collect}}收藏</view>
											<view class="item line1">{{item.visit}}浏览</view>
										</view>
									</view>
								</view>
							</view>
						</template>
					</template>
				</uni-list-item>
			</uni-list>
			<empty v-else :index="1" :title="emptyTitle"></empty>
		</view>
		<share ref="shareRef"></share>
	</view>
</template>

<script setup lang="ts">
	import empty from '@/components/empty/index'
	import share from '@/components/share/index'
	import { clickNavigateTo } from '@/utils/helper'
	import type { Detail } from '@/utils/typeHelper'
	const shareRef = ref(null)
	// const clickShare = (item) => {
	// 	shareRef.value.popupOpen()
	// }
	const toDefault = (item : Detail) : void => {
		clickNavigateTo(`/pages/forum/default?id=${item.id}`)
	}
	const props = withDefaults(
		defineProps<{
			listData : Array<any>,
			emptyTitle ?: string
		}>(), {
		emptyTitle: '暂无数据',
		listData: <any>[]
	})
	const { listData, emptyTitle } = toRefs(props)
</script>

<style scoped lang="scss">
	.content {
		width: 100%;
		padding-bottom: 10rpx;

		::v-deep .uni-list {
			background-color: $uni-default-bg;

			.uni-list-item {
				margin-bottom: 10rpx;
				border-radius: 8rpx;

				&:last-of-type {
					margin-bottom: 0;
				}

				.uni-list-item__container {
					padding: 30rpx;
				}
			}

			.uni-list--border {
				left: auto;
				top: auto;
			}
		}

		.item-list {
			width: 100%;

			.item-list-content {
				.item-list-content-left {
					width: 222rpx;
					height: 140rpx;
					border-radius: 4rpx;

					.image {
						width: 100%;
						height: 100%;
						border-radius: 8rpx;
					}
				}

				.item-list-content-right {
					width: calc(100% - 222rpx);

					.title {
						font-size: 30rpx;
						font-weight: $uni-default-font-weight;
						line-height: 1.5;
						color: $uni-text-color;
					}

					.caption {
						display: flex;
						align-items: center;
						font-size: 24rpx;
						color: $nui-text-color-four;

						.item {
							margin-right: 20rpx;
							max-width: 20%;

							&:first-of-type {
								max-width: 40%;
							}

							&:last-of-type {
								margin-right: 0;
							}
						}
					}

					.image-none {
						font-size: 26rpx;
						color: $nui-text-color-two;
						line-height: 1.5;
						margin-bottom: 24rpx;
						margin-top: 16rpx;
					}

					.image-three {
						margin-bottom: 24rpx;
						margin-top: 16rpx;
						display: flex;
						justify-content: space-between;

						.image-three-img {
							width: 222rpx;
							height: 140rpx;
							border-radius: 8rpx;
						}
					}
				}

				.image-one {
					height: 140rpx;
					padding-right: 30rpx !important;
					display: flex;
					justify-content: space-between;
					flex-direction: column;

					.caption {
						.item {
							&:first-of-type {
								max-width: 25%;
							}
						}
					}
				}
			}

			.item-list-bottom {
				margin-top: 24rpx;

				.item-list-bottom-left {
					.iconfont {
						font-size: 30rpx;
						padding-right: 16rpx;

						&:last-of-type {
							padding-right: 0;
						}
					}
				}

				.item-list-bottom-right {
					font-size: 26rpx;
					color: $nui-text-color-four;
					font-weight: 400;
					justify-content: flex-end;

					.item-icon {
						margin-right: 56rpx;

						&:last-of-type {
							margin-right: 0;
						}

						.iconfont {
							padding-right: 10rpx;
						}
					}
				}
			}
		}
	}
</style>