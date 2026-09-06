<template>
	<view class="notice-list">
		<view class="cr-position-header">
			<!-- #ifdef APP-PLUS -->
			<view class="status_bar"></view>
			<default-nav-bar :default-title="data.defaultTitle" :is-right="false" :right-data="rightIcon" @handleNarItem="handleNarItem"></default-nav-bar>
			<!-- #endif -->
			<view class="notice-work plr10">
				<view class="notice-work-con">
					<scroll-view :scroll-x="true" style="white-space: nowrap; display: flex;" :scroll-into-view="data.scrollIntoView" scroll-with-animation :show-scrollbar="false">
						<view class="long-item" v-for="(item,index) in data.barData" :key="index" :class="data.barIndex === index ? 'active' : ''" :id="'id'+index"
							@click="longClick(item,index)">
							{{item.name}}
							<uni-badge v-if="item.id" :text="item.number" absolute="rightTop" :offset="[0,-14]" size="small"></uni-badge>
						</view>
					</scroll-view>
				</view>
			</view>
		</view>
		<view class="notice-content">
			<work-list :list-data="data.listData" empty-title="暂无待处理任务～" />
		</view>
		<global-index />
	</view>
</template>

<script setup>
	import defaultNavBar from '@/components/defaultNavBar/index.vue'
	import workList from './components/workList.vue'
	import globalIndex from '@/components/globalIndex/index.vue'
	import { useStore } from 'vuex'
	import { toLogin } from '@/libs/login'
	import message from '@/utils/message'
	import { userMessageCountApi, messageListApi } from '@/api/user'
	import { clickNavigateTo, backToTop } from '@/utils/helper'

	const store = useStore()
	const isLogin = computed( () => store.state.app.isLogin )
	onShow( () => {
		if ( !isLogin.value ) {
			toLogin()
		} else {
			getMessageCount()
		}
	} )

	onMounted( () => {
		getMessageList()
	} )

	const rightIcon = reactive( [
		{ type: 1, icon: 'icon-sousuo' }
	] )

	const data = reactive( {
		defaultTitle: '工作待办',
		barIndex: 0,
		barData: [
			{ id: '', name: '全部', number: 0 },
			{ id: 62, name: '审批', number: 0 },
			{ id: 61, name: '绩效', number: 0 }
		],
		scrollIntoView: 'id0',
		where: {
			cate_id: '',
			page: 1,
			limit: 10,
			is_read: '',
			is_handle: 0
		},
		listData: []
	} )

	const handleNarItem = () => {
		console.log( 111 )
	}
	const longClick = ( item, index ) => {
		if ( data.where.page > 1 ) {
			backToTop()
		}
		data.barIndex = index
		data.where.cate_id = item.id
		data.where.page = 1
		getMessageList( true )
	}

	const getMessageCount = ( id ) => {
		userMessageCountApi().then( ( res ) => {
			const config = res.data
			data.barData[ 1 ].number = config.approve
			data.barData[ 2 ].number = config.assess
		} ).catch( ( error ) => {
			message.error( error.message )
		} );
	}

	const listLoading = ref( false )
	const getMessageList = ( tab = false ) => {
		if ( tab ) {
			uni.showLoading( {
				title: '加载中'
			} );
		}
		messageListApi( data.where ).then( ( res ) => {
			// 切换时数据清空
			if ( tab ) {
				uni.hideLoading()
				data.listData = []
			}
			data.listData.push( ...res.data.list )
			const allPage = Math.ceil( res.data.messageNum / data.where.limit )
			listLoading.value = !( data.listData.length <= 0 || data.where.page >= allPage )
			uni.stopPullDownRefresh() //停止刷新
		} ).catch( ( error ) => {
			if ( tab ) {
				uni.hideLoading()
			}
			message.error( error.message )
		} )
	}

	// 下拉加载
	onReachBottom( () => {
		if ( listLoading.value ) {
			data.where.page++
			getMessageList()
		}
	} )
	//上拉加载
	onPullDownRefresh( () => {
		data.where.page = 1
		data.value = false
		data.listData = []
		getMessageList()
	} )
</script>

<style scoped lang="scss">
	.cr-position-header {
		position: sticky;
		top: 0;
		background-color: #fff;
	}

	.notice-work {
		height: 40px;
		width: 100%;
		background-color: #fff;
		border-bottom: 1px solid $uni-line-style-color-three;

		.notice-work-con {
			width: 100%;

			::-webkit-scrollbar {
				display: none;
			}

			.long-item {
				height: 40px;
				font-size: 28rpx;
				font-weight: 400;
				color: $nui-text-color-four;
				line-height: 40px;
				display: inline-block;
				margin-right: 50rpx;
				border-bottom: 2px solid rgba(0, 0, 0, 0);
				position: relative;

				&.active {
					color: $uni-text-color;
					font-weight: $uni-default-font-weight;
					border-bottom: 2px solid $uni-color-primary;

					::v-deep .uni-badge {
						font-weight: normal;
					}
				}

				&:last-of-type {
					margin-right: 0;
				}

				::v-deep .uni-badge {
					position: absolute;
				}
			}
		}
	}

	.notice-content {
		margin: 20rpx;
	}
</style>