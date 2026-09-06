<template>
	<view class="content">
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<default-nav-bar :default-title="data.defaultTitle"></default-nav-bar>
		</view>

		<view class="examine-content m10">
			<uni-list :border="false" v-if="data.listData.length > 0">
				<uni-list-item v-for="(item,index) in data.listData" :key="'list'+item.id">
					<template v-slot:body>
						<view class="item-list">
							<view class="item-list-top" @tap="examineList(item)" v-if="item.card">{{item.card.name}}的{{item.approve.name}}</view>
							<view class="" @tap="examineList(item)">
								<examine-list-item :content="item.content"></examine-list-item>
								<uni-row class="item-list-content">
									<uni-col :span="6" class="left">提交时间</uni-col>
									<uni-col :span="18">
										<uni-dateformat format="yyyy/MM/dd hh:mm" :date="item.created_at"></uni-dateformat>
									</uni-col>
								</uni-row>
							</view>

							<image v-if="item.status === 0" class="item-list-status" src="/static/image/examine.png" mode=""></image>
							<image v-if="item.status === 1" class="item-list-status" src="/static/image/passed.png" mode=""></image>
							<image v-if="item.status === 2" class="item-list-status" src="/static/image/refuse.png" mode=""></image>
							<image v-if="item.status === -1" class="item-list-status" src="/static/image/revoke.png" mode=""></image>

						</view>
					</template>
				</uni-list-item>
			</uni-list>
			<empty v-else :index="4" title="暂无提交内容～"></empty>
		</view>
		<global-index></global-index>
	</view>
</template>

<script setup>
	import defaultNavBar from '@/components/defaultNavBar/index.vue'
	import empty from '@/components/empty/index.vue'
	import bottomNavigation from "@/components/bottomNavigation/index.vue"
	import globalIndex from '@/components/globalIndex/index.vue'
	import examineListItem from "./components/examineListItem.vue"
	import { ref, reactive, onMounted } from "vue"
	import message from "@/utils/message"
	const data = reactive( {
		defaultTitle: '',
		customStyle: { border: 'none', lineHeight: '20px', background: '#ED4014' },
		listData: [],
		where: {
			limit: 10,
			page: 1,
			types: 0,
			approve_id: ''
		}
	} )
	import { approveApplyApi, approveApplyRevokeApi } from '@/api/business'
	import { onLoad } from '@dcloudio/uni-app'
	onLoad( ( options ) => {

		if ( options.id ) {
			data.id = options.id
			data.where.approve_id = options.id
			data.defaultTitle = options.name ? options.name : '申请审批'
			getConfigList()
		}

	} )

	// 回到顶部
	const toTop = () => {
		uni.pageScrollTo( {
			scrollTop: 0,
			duration: 100,
		} )
	}

	import { clickNavigateTo } from '@/utils/helper'
	const examineList = ( item ) => {
		clickNavigateTo( `/pages/users/examine/defaults?id=${item.id}` )
	}

	//申请审批撤销
	const getApplyRevoke = ( item ) => {
		approveApplyRevokeApi( item.id ).then( ( res ) => {
			message.success( res.message );
			item.status = -1
		} ).catch( ( error ) => {
			message.error( error.message );
		} )
	}
	const listLoading = ref( false )
	// 列表加载
	const getConfigList = () => {
		approveApplyApi( data.where ).then( ( res ) => {
			data.listData.push( ...res.data.list )
			const allPage = Math.ceil( res.data.count / data.where.limit )
			if ( data.listData.length <= 0 || data.where.page >= allPage ) {
				listLoading.value = false
			} else {
				listLoading.value = true
			}
			uni.stopPullDownRefresh() //停止刷新
		} ).catch( ( error ) => {
			message.error( error.message )
		} );
	}

	import { onReachBottom } from '@dcloudio/uni-app'
	// 下拉加载
	onReachBottom( () => {
		if ( listLoading.value ) {
			data.where.page++
			getConfigList()
		}
	} )
</script>

<style scoped lang="scss">
	.content {
		width: 100%;
		position: relative;


		.cr-position-header {
			background-color: #fff;
		}

		.examine-content {
			padding-top: calc($uni-default-bar-height + var(--status-bar-height));

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
					padding-bottom: 24rpx;
					font-size: 32rpx;
					font-weight: 600;
					color: $uni-text-color;
				}

				.item-list-content {
					font-size: 28rpx;
					color: $uni-text-color;
					font-weight: 400;

					.left {
						color: $nui-text-color-four;
					}

					uni-button {
						width: 100%;
						height: 74rpx;
						line-height: 74rpx;
						font-size: 30rpx;
						background-color: #F0F1F5;
						margin-right: 32rpx;

						&:last-of-type {
							margin-right: 0;
						}

						&::after {
							border-radius: 8rpx;
							border: none;
						}
					}
				}

				.item-list-button {
					display: flex;
					justify-content: space-between;
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
