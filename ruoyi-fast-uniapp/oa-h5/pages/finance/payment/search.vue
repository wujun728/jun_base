<template>
	<view class="content">
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<view class="default-search">
				<uni-search-bar @confirm="search" @focus="search" :placeholder="data.placeholder" :focus="true" bgColor="#F0F1F5" v-model="data.where.name" @clear="clearSearch"
					@cancel="cancelSearch">
				</uni-search-bar>
			</view>
			<customer-tab :examine-tab-data="data.examineTabData" @change="changeTab" :index="data.tabIndex"></customer-tab>
		</view>

		<view class="m10">
			<payment-record :listData="data.listData" :type-index="1" :empty-title="data.emptyTitle"></payment-record>
		</view>
	</view>
</template>

<script setup lang="ts">
	import message from '@/utils/message'
	import paymentRecord from '@/pages/customer/list/components/paymentRecord.vue'
	import customerTab from '@/pages/customer/list/components/customerTab.vue'
	import { pendingTabData } from '@/utils/assessment'
	import { billListApi } from '@/api/customer'
	import { backToTop } from '@/utils/helper'
	import type { Res, Tab } from '@/utils/typeHelper'
	const data = reactive({
		typeIndex: 0,
		tabIndex: 0,
		tabId: 1,
		placeholder: '请输入关键字搜索',
		examineTabData: pendingTabData,
		listData: [],
		emptyTitle: '暂无搜索结果～',
		where: {
			types: '',
			page: 1,
			limit: 10,
			status: <number | string>0,
			time: '',
			name: '',
			date: '',
			no_withdraw: 1
		}
	})
	// data --end--{

	interface Options {
		index : number
	}

	onLoad((options : Options) : void => {
		const len = data.examineTabData.length - 1
		if (options.index) {
			if (options.index > len) {
				data.tabIndex = len
			} else {
				data.tabIndex = Number(options.index)
			}
			data.tabId = data.examineTabData[data.tabIndex].id
			data.where.status = data.tabId === 1 ? 0 : ''
		}
	})

	const clearSearch = () => {
		data.where.name = ''
		data.listData = []
	}

	const cancelSearch = () : void => {
		uni.navigateBack()
	}

	const search = () : void => {
		data.where.page = 1
		if (data.where.name) {
			getTabList(true)
		} else {
			clearSearch()
		}
	}

	const changeTab = (e : Tab) : void => {
		if (data.tabId === e.id || data.where.name === '') return false
		if (data.where.page > 1) {
			backToTop()
		}
		data.tabId = e.id
		data.tabIndex = e.index
		data.where.status = data.tabId === 1 ? 0 : ''
		getTabList(true)
	}

	// 条件判断
	const getTabList = (tab : boolean = false) : void => {
		data.where.page = 1
		getConfigList(tab)
	}

	const listLoading : Ref<boolean> = ref(false)
	// 列表加载
	const getConfigList = (tab : boolean = false) : void => {
		billListApi(data.where).then((res : Res) => {
			// 切换时数据清空
			if (tab) data.listData = []
			data.listData.push(...res.data.list)
			const allPage : number = Math.ceil(res.data.count / data.where.limit)
			listLoading.value = (data.listData.length <= 0 || data.where.page >= allPage) ? false : true

			uni.stopPullDownRefresh() //停止刷新
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}

	// 下拉加载
	onReachBottom(() : void => {
		if (listLoading.value) {
			data.where.page++
			getConfigList()
		}
	})
</script>

<style scoped lang="scss">
	.content {
		width: 100%;
		position: relative;

		.cr-position-header {
			background-color: #fff;
			position: sticky;
		}

		.examine-content {}
	}
</style>