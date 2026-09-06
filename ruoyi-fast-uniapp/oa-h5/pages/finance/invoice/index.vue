<template>
  <view class="content">
    <view class="cr-position-header">

      <view class="status_bar"></view>
      <default-nav-bar jump-url="/pages/workbench/index" :is-right="true" :right-data="rightIcon"
        @handleNarItem="handleNarItem">
      </default-nav-bar>
      <customer-tab :examine-tab-data="data.examineTabData" :index="data.tabIndex" @change="changeTab"></customer-tab>
      <form-box @change="formBoxChange" :status="data.tabId" type="approve"></form-box>
    </view>

    <view class="examine-content m10">
      <invoice-list :list-data="data.listData" :empty-title="data.emptyTitle" :is-finance="true" :tab="data.tabIndex"
        @change="invoiceChange"></invoice-list>
    </view>
    <global-index />
    <invoice-examine ref="invoiceExamineRef" :config-data="data.config" @change="changeExamine" />
  </view>
</template>

<script setup lang="ts">
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import invoiceList from '@/pages/customer/invoice/components/invoiceList.vue'
  import customerTab from '@/pages/customer/list/components/customerTab.vue'
  import globalIndex from '@/components/globalIndex/index.vue'
  import formBox from './components/formBox.vue'
  import { ref, reactive, type Ref } from 'vue'
  import message from '@/utils/message'
  import { financeInvoiceListlApi } from '@/api/finance'
  import { clickNavigateTo, backToTop } from '@/utils/helper'
  import type { Res, Box, Tab, Detail, GetType } from '@/utils/typeHelper'
  import invoiceExamine from './components/invoiceExamine.vue'

  interface C {
    row : Detail
    type : number
  }

  const data = reactive({
    typeIndex: 0,
    tabIndex: 0,
    tabId: 1,
    emptyTitle: '当前暂无发票～',
    customStyle: { border: 'none', lineHeight: '20px', background: '#ED4014' },
    examineTabData: [
      { name: '待开发票', id: 1 },
      { name: '开票记录', id: 2 },
    ],
    listData: [],
    config: {},
    where: {
      way: '',
      page: 1,
      limit: 10,
      status: 1,
      time: '',
      name: '',
      invoiced: '',
      from: 1,
      date: '',
      salesman_name: '',
      types: <number | string>''
    },
  })

  // 加载 {
  onLoad((options : GetType) => {
    if (options.tab) {
      data.tabIndex = Number(options.tab)
      const len = data.examineTabData[data.tabIndex]
      data.tabId = len.id
      data.where.status = data.tabId === 1 ? 1 : ''
      getTabList(true)
    } else {
      getTabList()
    }
  })

  const rightIcon = reactive([
    { type: 1, icon: 'icon-sousuo' }
  ])

  const handleNarItem = () : void => {
    clickNavigateTo(`/pages/finance/invoice/search?index=${data.tabIndex}`)
  }

  const invoiceExamineRef = ref(null)
  const invoiceChange = (e : C) : void => {
    data.config = e
    invoiceExamineRef.value.popupOpen()
  }

  const changeExamine = () : void => {
    getTabList(true)
  }

  const changeTab = (e : Tab) : boolean => {
    if (data.tabId === e.id) return false
    if (data.where.page > 1) {
      backToTop()
    }
    data.tabId = e.id
    data.tabIndex = e.index
    data.where.status = data.tabId === 1 ? 1 : ''
    getTabList(true)
  }

  const formBoxChange = (e : Box) : void => {
    data.where.status = data.tabId === 1 ? 1 : e.status
    data.where.types = e.type
    data.where.time = e.time
    data.where.date = e.date
    getTabList(true)
  }

  // 条件判断
  const getTabList = (tab = false) => {
    data.where.page = 1
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
      uni.stopPullDownRefresh() //停止刷新
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
  //上拉加载
  onPullDownRefresh(() => {
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