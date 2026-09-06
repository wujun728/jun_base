<template>
	<view class="content">
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<view class="default-search">
				<uni-search-bar @confirm="search" @focus="search" :placeholder="data.placeholder" :focus="true" bgColor="#F0F1F5" v-model="data.where.name" @clear="clearSearch"
					@cancel="cancelSearch">
				</uni-search-bar>
			</view>
			<customer-tab :examine-tab-data="data.examineTabData" :index="data.tabIndex" @change="changeTab"></customer-tab>
		</view>

		<view class="examine-content m10">
			<invoice-list :list-data="data.listData" :empty-title="data.emptyTitle" :is-finance="true" @change="invoiceChange"></invoice-list>
		</view>

		<global-index />
		<invoice-examine ref="invoiceExamineRef" :config-data="data.config" @change="changeExamine" />
	</view>
</template>

<script setup lang="ts">
	import invoiceList from '@/pages/customer/invoice/components/invoiceList.vue'
	import customerTab from '@/pages/customer/list/components/customerTab.vue'
	import globalIndex from '@/components/globalIndex/index.vue'
	import message from '@/utils/message'
	import invoiceExamine from './components/invoiceExamine.vue'
	import { pendingTabData } from '@/utils/assessment'
	import { financeInvoiceListlApi } from '@/api/finance'
	import { backToTop } from '@/utils/helper'
	import type { Res, Tab, Detail } from '@/utils/typeHelper'

	// --data  --start--
	const data = reactive({
		typeIndex: 0,
		tabIndex: 0,
		tabId: 1,
		placeholder: '请输入关键字搜索',
		emptyTitle: '暂无搜索结果～',
		customStyle: { border: 'none', lineHeight: '20px', background: '#ED4014' },
		examineTabData: pendingTabData,
		listData: [],
		config: {},
		where: {
			way: '',
			page: 1,
			limit: 10,
			status: <number | string | Array<number>>[0, 3],
			time: '',
			name: '',
			invoiced: '',
			from: 1,
			date: '',
			salesman_name: '',
			types: <number | string>''
		},
	})
	// --data --enf--{

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

	const invoiceExamineRef = ref(null)
	interface C {
		row : Detail
		type : number
	}
	const invoiceChange = (e : C) : void => {
		data.config = e
		invoiceExamineRef.value.popupOpen()
	}

	const changeExamine = () : void => {
		getTabList(true)
	}

	const changeTab = (e : Tab) : boolean => {
		if (data.tabId === e.id || data.where.name === '') return false
		if (data.where.page > 1) {
			backToTop()
		}
		data.tabId = e.id
		data.tabIndex = e.index
		getTabList(true)
	}

	// 条件判断
	const getTabList = (tab = false) => {
		data.where.page = 1
		data.where.status = data.tabId === 1 ? [0, 3] : ''
		getConfigList(tab)
	}

	const listLoading : Ref<boolean> = ref(false)
	// 列表加载
	const getConfigList = (tab : boolean = false) : void => {
		financeInvoiceListlApi(data.where).then((res : Res) => {
			// 切换时数据清空
			if (tab) data.listData = []
			data.listData.push(...res.data.list)
			const allPage : number = Math.ceil(res.data.count / data.where.limit)
			listLoading.value = (data.listData.length <= 0 || data.where.page >= allPage) ? false : true
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}

	// 下拉加载
	onReachBottom(() => {
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