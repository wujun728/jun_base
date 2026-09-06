<template>
	<view class="content">
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<view class="default-search">
				<uni-search-bar @confirm="search" @focus="search" :focus="true" bgColor="#F0F1F5" v-model="config.name" @clear="clearSearch" @cancel="cancelSearch">
				</uni-search-bar>
			</view>
		</view>

		<view class="report-content m10">
			<newes-list v-if="config.type === 'newes'" :list-data="config.newesData" empty-title="暂无搜索的最新内容～"></newes-list>
			<folder-list v-if="config.type === 'folder'" :list-data="config.folderData" @btn-click="folderChange" empty-title="暂无搜索的内容～"></folder-list>
		</view>

	</view>
</template>

<script setup>
	import newesList from './components/newesList.vue'
	import folderList from './components/folderList.vue'
	import { ref, reactive, onMounted } from "vue"
	import message from '@/utils/message'

	const config = reactive( {
		type: 'newes',
		name: '',
		rightIcon: [
			{ type: 1, icon: 'icon-sousuo', types: 'icon' }
		],
		newesWhere: {
			pid: '',
			name: '',
			page: 1,
			limit: 10
		},
		newesData: [],
		folderWhere: {
			pid: '',
			name: '',
			page: 1,
			limit: 10
		},
		folderData: [],
		isLoadingFolder: false,
		listWhere: {
			pid: '',
			name: '',
			page: 1,
			limit: 10
		},
		folderId: '',
		defaultType: 1,
		defaultTitle: '记事本'
	} )
	const newsFolderRef = ref( null )

	import { clickNavigateTo, delayedNavigateTo } from '@/utils/helper'

	const listLoading = ref( false ) //是否正在加载

	import { onLoad } from '@dcloudio/uni-app'

	onLoad( ( e ) => {
		if ( e.tab ) {
			config.type = e.tab
			if ( e.id ) config.defaultType = 0
			config.defaultTitle = ''
			config.folderId = e.id
		}
	} )

	import { userMemorialGroupApi, userMemorialCateListApi, userMemorialListApi } from '@/api/user'
	// 获取最近列表
	const getMemorialGrou = ( tab = false ) => {
		userMemorialGroupApi( config.newesWhere ).then( res => {
			// 切换时数据清空
			if ( tab ) config.newesData = []
			config.newesData.push( ...res.data.list )
			const allPage = Math.ceil( res.data.count / config.newesWhere.limit )
			if ( config.newesData.length <= 0 || config.newesWhere.page >= allPage ) {
				listLoading.value = false
			} else {
				listLoading.value = true
			}
		} ).catch( ( error ) => {
			uni.hideLoading()
			message.error( error.message )
		} )
	}

	// 获取文件夹中文件夹
	const getMemorialCateList = ( tab = false ) => {
		userMemorialCateListApi( config.folderWhere ).then( res => {
			// 切换时数据清空
			if ( tab ) config.folderData = []
			if ( res.data.list.length > 0 ) {
				res.data.list.map( ( value ) => {
					value.folder_type = 1
				} )
			}
			// 判断父级名称
			if ( res.data.parent ) {
				config.defaultTitle = res.data.parent.name
			}
			config.folderData.push( ...res.data.list )
			const allPage = Math.ceil( res.data.count / config.folderWhere.limit )
			if ( config.folderData.length <= 0 || config.folderWhere.page >= allPage ) {
				listLoading.value = false
				config.isLoadingFolder = true
				getMemorialList()
			} else {
				listLoading.value = true
			}
		} ).catch( ( error ) => {
			uni.hideLoading()
			message.error( error.message )
		} )
	}

	// 获取记事本内容列表
	const getMemorialList = () => {
		userMemorialListApi( config.listWhere ).then( res => {
			// 切换时数据清空
			config.folderData.push( ...res.data.list )
			const allPage = Math.ceil( res.data.count / config.listWhere.limit )
			if ( config.folderData.length <= 0 || config.listWhere.page >= allPage ) {
				listLoading.value = false
			} else {
				listLoading.value = true
			}
		} ).catch( ( error ) => {
			uni.hideLoading()
			message.error( error.message )
		} )
	}

	const clearSearch = () => {
		config.name = ''
		if ( config.type === 'newes' ) {
			config.newesData = []
		} else {
			config.folderData = []
		}
	}

	const cancelSearch = () => {
		uni.navigateBack()
	}

	const search = () => {
		if ( config.name ) {
			if ( config.type === 'newes' ) {
				config.newesWhere.name = config.name
				config.newesWhere.page = 1
				getMemorialGrou( true )
			} else {
				config.folderWhere.name = config.name
				config.listWhere.name = config.name
				config.folderWhere.pid = config.folderId
				config.listWhere.pid = config.folderId
				config.folderWhere.page = 1
				config.listWhere.page = 1
				getMemorialCateList( true )
			}
		} else {
			clearSearch()
		}
	}

	import { onReachBottom } from '@dcloudio/uni-app'
	// 下拉加载
	onReachBottom( () => {
		if ( config.type === 'newes' ) {

		} else {
			if ( !config.isLoadingFolder ) {
				// 加载文件夹
				if ( listLoading.value ) {
					config.folderWhere.page++
					getMemorialCateList()
				}
			} else {
				// 加载记事本
				if ( listLoading.value ) {
					config.listWhere.page++
					getMemorialList()
				}
			}
		}
	} )

	// 文件夹事件监听
	const folderChange = ( e ) => {
		if ( e.type === 1 ) {
			config.folderId = e.id
			clickNavigateTo( `/pages/users/memorandum/index?tab=folder&id=${config.folderId}` )
		} else if ( e.type === 2 ) {
			config.folderId = e.id
			const res = {
				pid: e.id,
				name: e.name
			}
			saveMemorialCate( res )
		}
	}
</script>

<style scoped lang="scss">
	::v-deep .selected-list {
		display: none;
	}

	.content {
		width: 100%;
		position: relative;

		.cr-position-header {
			position: sticky;
			background-color: #fff;

			::v-deep .uni-searchbar {
				padding: 34rpx 30rpx;
			}
		}

		.report-content {
			margin-top: 0;
		}
	}
</style>
