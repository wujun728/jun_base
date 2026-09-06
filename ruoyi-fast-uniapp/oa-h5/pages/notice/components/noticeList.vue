<template>
	<view>
		<view class="content">
			<uni-list :border="false" v-if="listData.length > 0">
				<uni-list-item v-for="(item,index) in listData" :key="'items'+index">
					<template v-slot:header>
						<view class="notice-list-item-left" @click.native="listItemClick(item)">
							<image class="image" :src="item.pic" mode="" />
						</view>
					</template>
					<template v-slot:body>
						<view class="notice-list-item-right" @click.native="listItemClick(item)">
							<uni-row class="notice-list-item-right-top display-center">
								<uni-col :span="18">{{item.cate_name}}</uni-col>
								<uni-col :span="6" class="right-top-time text-right">
									<text v-if="item.item.created_at">{{formatDate(item.item.created_at, true)}}</text>
								</uni-col>
							</uni-row>
							<uni-row class="notice-list-item-right-bottom display-center">
								<uni-col :span="18" class="line1">{{item.item.message ? item.item.message : '暂无最新消息~' }}</uni-col>
								<uni-col :span="6" class="right-top-time text-right">
									<uni-badge :text="item.count" />
								</uni-col>
							</uni-row>
						</view>
					</template>
				</uni-list-item>
			</uni-list>
			<empty v-else :index="5" :title="emptyTitle"></empty>
		</view>
	</view>
</template>

<script setup>
	import empty from '@/components/empty/index'
	import message from '@/utils/message'
	import { formatDate } from '@/utils/schedule'
	const props = defineProps( {
		listData: {
			type: Array,
			default () {
				return []
			}
		},
		emptyTitle: {
			type: String,
			default: ''
		},
	} )
	const { listData, emptyTitle } = toRefs( props )

	import { clickNavigateTo } from '@/utils/helper'
	const listItemClick = ( item ) => {
		// if (item.count > 0) {
		//   messageCate(item.id, 1)
		// }
		clickNavigateTo( `/pages/notice/info?id=${item.id}&title=${item.cate_name}` )
	}

	import { userMessageHandleCateApi } from '@/api/user'
	const messageCate = ( id, status ) => {
		userMessageHandleCateApi( id, status ).then( res => {
			//message.success( res.message )
		} ).catch( ( error ) => {
			message.error( error.message )
		} )
	}
</script>

<style scoped lang="scss">
	.content {
		width: 100%;

		::v-deep .uni-list {
			.uni-list-item {
				margin-bottom: 30rpx;
				padding-bottom: 30rpx;
				position: relative;

				&::after {
					position: absolute;
					bottom: 0;
					right: -30rpx;
					content: '';
					width: calc(100% - 114rpx + 30rpx);
					border-bottom: 1px solid $uni-line-style-color-three;
				}


				&:last-of-type {
					margin-bottom: 0;
				}

				.uni-list-item__container {
					padding: 0;
					align-items: center;
				}
			}

			.uni-list--border {
				left: auto;
				top: auto;
			}
		}

		.notice-list-item-left {
			width: 90rpx;
			height: 90rpx;

			.image {
				width: 100%;
				height: 100%;
			}
		}

		.notice-list-item-right {
			width: calc(100% - 90rpx);
			padding-left: 24rpx;

			.notice-list-item-right-top {
				font-size: $uni-font-size-default;
				font-weight: 400;
				color: $uni-text-color;
				line-height: 1.5;

				.right-top-time {
					font-weight: normal;
					font-size: 20rpx;
					color: $nui-text-color-four;
				}
			}

			.notice-list-item-right-bottom {
				margin-top: 6rpx;
				font-size: 24rpx;
				word-wrap: break-word;
				color: $nui-text-color-four;
				line-height: 1.5;
			}
		}
	}
</style>