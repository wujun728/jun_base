<template>
	<view class="content">
		<!-- 非h5页面默认工具栏高度获取 -->
		<view class="cr-position-header">
			<!-- #ifdef APP-PLUS -->
			<view class="" @click="closeMoerBar">
				<view class="status_bar"></view>
				<default-nav-bar></default-nav-bar>
			</view>
			<!-- #endif -->
			<template v-if="config.barList.length>0">
				<navigation-bar ref="navigationBarRef" :isActiveLine="false" :isSidebar="false" :bar-data="config.barList" @handleData="handleData"></navigation-bar>
			</template>
		</view>

		<view class="notice-content">
			<view class="m10" v-if="config.listData.length>0">
				<uni-list :border="false">
					<uni-list-item v-for="(item,index) in config.listData" :key="index">
						<template v-slot:header>
							<view class="">
								<uni-dateformat class="header-time" format="yyyy/MM/dd" :date="item.days"></uni-dateformat>
							</view>
						</template>
						<template v-slot:body>
							<template v-for="items in item.data" :key="'lists'+items.id">
								<view class="item-list-body" @click="noticeListItem(items)">
									<view class="item-list-right" :class="items.cover ? 'pl height' : 'width100'">
										<view>
											<view class="title" :class="items.cover ? 'line2' : 'line1'">{{items.title}}</view>
											<view v-if="items.info" class="content" :class="items.cover ? 'line1' : 'line2'">{{items.info}}</view>
										</view>

										<view class="bottom display-align" :class="items.cover ? '' : 'bottom-t'">
											<uni-dateformat class="time pr-14" format="hh:mm" :date="items.push_time"></uni-dateformat>
											<view class="">{{items.visit}}阅读</view>
											<view class="pl-14" v-if="items.is_read === 0">
												<uni-tag text="未读" type="warning" />
											</view>

										</view>
									</view>
									<view class="item-list-left" v-if="items.cover">
										<image class="image" :src="items.cover" mode="aspectFill"></image>
									</view>
								</view>
							</template>
						</template>
					</uni-list-item>
				</uni-list>
				<uni-load-more v-if="loading" iconType="auto" status="loading" />
			</view>
			<empty v-else :index="1" :title="emptyTitle"></empty>
		</view>
		<global-index></global-index>
	</view>
</template>

<script setup>
	import { ref, onMounted, reactive } from 'vue'
	import navigationBar from '@/components/navigationBar/index.vue'
	import globalIndex from '@/components/globalIndex/index.vue'
	import defaultNavBar from '@/components/defaultNavBar/index.vue'
	import empty from '@/components/empty/index.vue'
	import message from '@/utils/message'
	import { noticeCategoryApi, noticeListApi } from '@/api/user'
	const emptyTitle = ref( '暂无企业动态，快去看看别的吧～' )

	const navigationBarRef = ref( null )
	const closeMoerBar = () => {
		navigationBarRef.value.showMoreClick()
	}
	const currentIndex = ref( 0 )
	const config = reactive( {
		barList: [],
		where: {
			page: 1,
			limit: 10,
			cate_id: '',
			status: 1
		},
		listData: []
	} )


	onMounted( () => {
		getNoticeCategory()
	} )

	// 获取企业动态分类
	const getNoticeCategory = () => {
		noticeCategoryApi( { is_show: 1 } ).then( res => {
			config.barList = res.data || []
			if ( config.barList.length > 0 ) {
				config.where.cate_id = config.barList[ 0 ].id
				getListData()
			}
		} ).catch( ( error ) => {
			message.error( error.message )
		} )
	}

	const loading = ref( false )
	// 获取企业动态列表
	const getListData = () => {
		loading.value = true
		noticeListApi( config.where ).then( res => {
			loading.value = false
			config.listData = res.data.list || []
		} ).catch( ( error ) => {
			loading.value = false
			message.error( error.message )
		} )
	}
	const handleData = ( e ) => {
		config.where.page = 1
		config.where.cate_id = e.id
		config.listData = []
		getListData()
	}
	import { clickNavigateTo } from '@/utils/helper'
	const noticeListItem = ( item ) => {
		clickNavigateTo( `/pages/users/noticeDefault/index?id=+${item.id}` )
	}
</script>

<style scoped lang="scss">
	.cr-position-header {
		position: sticky;
	}

	.notice-content {
		::v-deep .uni-list {
			background-color: $uni-default-bg;

			.uni-list--border {
				right: auto;
				left: auto;
			}

			.uni-list-item {
				border-radius: 12rpx;
				margin-bottom: 20rpx !important;

				.uni-list-item__container {
					display: block;
					padding: 20rpx 24rpx;
				}
			}
		}

		.header-time {
			font-size: 22rpx;
			font-weight: 400;
			color: $nui-text-color-two;
			line-height: 22rpx;

		}

		.item-list-body {
			display: flex;
			padding: 20rpx 0;
			border-bottom: 1px solid $uni-line-style-color-three;

			&:last-of-type {
				padding-bottom: 10rpx;
				border-bottom: none;
			}

			.item-list-left {
				width: 240rpx;
				height: 172rpx;
				margin: 6rpx 0;

				.image {
					width: 100%;
					height: 100%;
					border-radius: 8rpx;
				}
			}

			.item-list-right {
				width: calc(100% - 240rpx);
				display: flex;
				justify-content: space-between;
				flex-direction: column;

				&.height {
					height: 184rpx;
				}

				&.pl {
					padding-right: 30rpx;
				}

				.title {
					font-size: 30rpx;
					line-height: 46rpx;
					color: $uni-text-color;
					font-weight: $uni-default-font-weight;
				}

				.content {
					font-size: 26rpx;
					padding-top: 8rpx;
					color: $nui-text-color-two;
				}

				.bottom {
					&.bottom-t {
						margin-top: 26rpx;
					}

					.time {
						display: inline-block;
						font-size: 22rpx;
					}

					height: 32rpx;
					font-size: 22rpx;
					color: $nui-text-color-four;

					::v-deep .uni-tag {
						padding: 0 6rpx;
						font-size: 22rpx;
						line-height: 22rpx;
						background-color: #FFF3E2;
						border: none;
						color: #FF9900;
					}
				}
			}
		}
	}
</style>