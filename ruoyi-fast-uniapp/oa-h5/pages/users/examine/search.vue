<template>
	<view class="content">
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<view class="default-search">
				<uni-search-bar @confirm="search" placeholder="请填写人员姓名搜索" @focus="search" :focus="true" bgColor="#F0F1F5" v-model="data.where.name" @clear="clearSearch"
					@cancel="cancelSearch">
				</uni-search-bar>
			</view>
			<examine-tab :examine-tab-data="data.examineTabData" @change="changeTab" :index="data.tabIndex"></examine-tab>
		</view>

		<view class="examine-content m10">
			<examine-list-default :list-data="data.listData" :type-index="data.typeIndex" :empty-title="data.emptyTitle"></examine-list-default>
		</view>
	</view>
</template>

<script setup>
	import { ref, reactive, onMounted, getCurrentInstance } from "vue"
	import message from "@/utils/message"
	import examineListDefault from "./components/examineListDefault.vue"
	import examineTab from "./components/examineTab.vue"
	import { examineTabData, approveTabData } from '@/utils/assessment'
	const data = reactive( {
		typeIndex: 0,
		tabIndex: 0,
		examineTabData: [],
		listData: [],
		emptyTitle: '暂无搜索结果～',
		where: {
			limit: 10,
			page: 1,
			types: 0,
			name: ''
		}
	} )

	import { useBarHeight } from '@/utils/useVerifyCode'
	const { height, getBarHeight } = useBarHeight()
	const instance = getCurrentInstance()

	onMounted( () => {
		getBarHeight( '.cr-position-header', instance )
	} )

	import { approveApplyApi } from '@/api/business'
	import { onLoad } from '@dcloudio/uni-app'
	onLoad( ( options ) => {
		if ( options.type && options.type > 1 ) {
			data.examineTabData = examineTabData
		} else {
			data.examineTabData = approveTabData
		}
		const len = data.examineTabData.length - 1
		if ( options.index ) {
			if ( options.index > len ) {
				data.tabIndex = len
			} else {
				data.tabIndex = Number( options.index )
			}
		}
	} )

	const clearSearch = () => {
		data.where.name = ''
		data.listData = []
	}

	const cancelSearch = () => {
		uni.navigateBack()
	}

	const search = () => {
		data.where.page = 1
		if ( data.where.name ) {
			getTabList( true )
		} else {
			clearSearch()
		}
	}

	import { backToTop } from '@/utils/helper'
	const changeTab = ( e ) => {
		if ( data.where.page > 1 ) {
			backToTop()
		}
		data.tabIndex = e.index
		data.where.page = 1
		if ( data.where.name ) {
			getTabList( true )
		} else {
			clearSearch()
		}
	}

	// 条件判断
	const getTabList = ( tab = false ) => {
		const id = data.examineTabData[ data.tabIndex ].id
		data.typeIndex = Number( id )
		if ( id < 0 ) {
			data.where.types = 0
			data.where.verify_status = 5
		} else {
			data.where.types = 1
			data.where.verify_status = id
		}
		getConfigList( tab )
	}

	const listLoading = ref( false )
	// 列表加载
	const getConfigList = ( tab = false ) => {
		approveApplyApi( data.where ).then( ( res ) => {
			// 切换时数据清空
			if ( tab ) data.listData = []
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
			padding-top: v-bind(height);
		}
	}
</style>