<template>
	<view class="content">
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<default-nav-bar jump-url="/pages/workbench/index" :is-right="true" :right-data="rightIcon" @handleNarItem="handleNarItem">
			</default-nav-bar>
			<customer-tab :examine-tab-data="data.examineTabData" :index="data.tabIndex" @change="changeTab"></customer-tab>
			<form-box @change="formBoxChange" :status="data.tabId" type="approve"></form-box>
		</view>
		<view class=" m10">
			<payment-record :listData="data.listData" :cid="1" :type-index="1" :tab="data.tabIndex" @change="payChange"></payment-record>
		</view>

		<payment-examine ref="paymentExamineRef" :config-data="data.configData" @change="examineChange" />
		<global-index />
	</view>
</template>

<script setup lang="ts">
	import defaultNavBar from '@/components/defaultNavBar/index.vue'
	import customerTab from '@/pages/customer/list/components/customerTab.vue'
	import paymentRecord from '@/pages/customer/list/components/paymentRecord.vue'
	import globalIndex from '@/components/globalIndex/index.vue'
	import formBox from './components/formBox.vue'
	import paymentExamine from './components/paymentExamine.vue'
	import { pendingTabData } from '@/utils/assessment'
	import { clickNavigateTo, backToTop } from '@/utils/helper'
	import { clientContractBillCateApi,billListApi } from '@/api/customer'
	import message from '@/utils/message'
	import type { Res, Box, Tab, Detail, GetType } from '@/utils/typeHelper'
	const paymentExamineRef = ref(null)

	const data = reactive({
		typeIndex: 0,
		tabIndex: 0,
		tabId: 1,
		emptyTitle: '暂无付款记录～',
		customStyle: { border: 'none', lineHeight: '20px', background: '#ED4014' },
		examineTabData: pendingTabData,
		listData: [],
		where: {
			types: <number | string>'',
			page: 1,
			limit: 10,
			status: <number | string>0,
			time: '',
			name: '',
			date: '',
			no_withdraw: 1,
		},
		configData: {
			path: []
		}
	})
	// {

	onLoad((options : GetType) => {
		if (options.tab) {
			data.tabIndex = Number(options.tab)
			const len = data.examineTabData[data.tabIndex]
			data.tabId = len.id
			data.where.status = data.tabId === 1 ? 0 : ''
			getTabList(true)
		} else {
			getConfigList()
		}
	})
	const rightIcon = reactive([
		{ type: 1, icon: 'icon-sousuo' }
	])

	const handleNarItem = () : void => {
		clickNavigateTo(`/pages/finance/payment/search?index=${data.tabIndex}`)
	}

	const changeTab = (e : Tab) : void => {
		if (data.where.page > 1) {
			backToTop()
		}
		data.tabId = e.id
		data.tabIndex = e.index
		data.where.status = data.tabId === 1 ? 0 : ''
		getTabList(true)
	}

	const formBoxChange = (e : Box) : void => {
		data.where.status = data.tabId === 1 ? 0 : e.status
		data.where.types = e.type
		data.where.time = e.time
		data.where.date = e.date
		getTabList(true)
	}

	// 条件判断
	const getTabList = (tab : boolean = false) : void => {
		data.where.page = 1
		getConfigList(tab)
	}

	const examineChange = () : void => {
		getTabList(true)
	}
	interface C {
		row : Detail
		type : number
		path : Array<number>
	}
	const payChange = (e : C) : void => {
		getBillCate(e)
	}

	const getBillCate = (e : C) : void => {
		clientContractBillCateApi(e.row.cid).then((res : Res) => {
			data.configData = e
			data.configData.path = res.data.bill_cate_path
			paymentExamineRef.value.popupOpen()
		}).catch((error : Res) => {
			message.error(error.message)
		})
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
	// //上拉加载
	onPullDownRefresh(() : void => {
		data.where.page = 1
		data.listData = []
		getConfigList()
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