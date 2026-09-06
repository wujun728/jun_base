<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar jump-url="/pages/workbench/index" :is-right="true" :right-data="rightIcon"
        @handleNarItem="handleNarItem"></default-nav-bar>
      <customer-tab :examine-tab-data="data.examineTabData" :index="data.tabIndex" @change="changeTab"></customer-tab>
      <form-box @change="formBoxChange" :status="data.where.types" type="approve"></form-box>
    </view>
    <view class="examine-content m10">
      <customer-list-default :list-data="data.listData" :type-index="data.typeIndex" :types="data.where.types"
        :empty-title="data.emptyTitle"></customer-list-default>
    </view>

    <global-index />
    <tabbar :currentIndex="currentIndex" />
  </view>
</template>

<script setup>
  import tabbar from '@/components/tabbar/index.vue'
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import empty from '@/components/empty/index'
  import bottomNavigation from '@/components/bottomNavigation/index.vue'
  import customerListDefault from './components/customerListDefault.vue'
  import customerTab from './components/customerTab.vue'
  import globalIndex from '@/components/globalIndex/index.vue'
  import formBox from './components/formBox.vue'
  import { ref, reactive } from 'vue'
  import message from '@/utils/message'
  import { customerTabData } from '@/utils/assessment'
  import { customerListApi } from '@/api/customer'
  import { useStore } from 'vuex'
  const store = useStore()
  const data = reactive({
    typeIndex: 0,
    tabIndex: 0,
    tabId: 2,
    emptyTitle: '当前暂无客户～',
    customStyle: { border: 'none', lineHeight: '20px', background: '#ED4014' },
    examineTabData: customerTabData,
    listData: [],
    where: {
      limit: 10,
      page: 1,
      types: 1,
      scope_frame: 'self',
      customer_status: '',
      time: '',
      customer_label: [],
      salesman_id: '',
    },
  })

  import { onLoad } from '@dcloudio/uni-app'
  onLoad((options) => {
    if (options.tab) {
      data.tabIndex = Number(options.tab)
      data.tabId = data.examineTabData[data.tabIndex].id
      data.where.types = data.tabId === 2 ? 0 : 1
      getTabList()
    } else {
      getTabList()
    }
  })

  const rightIcon = reactive([
    { type: 1, icon: 'icon-sousuo' },
    { type: 2, icon: 'icon-a-gengduo2' },
  ])
  const currentIndex = ref(store.state.app.currentIndex)

  import { clickNavigateTo, backToTop } from '@/utils/helper'
  // 添加客户
  const handleNarItem = (e) => {
    if (e.type === 1) {
      clickNavigateTo(`/pages/customer/list/search?index=${data.tabIndex}&type=${data.where.types}`)
    } else {
      clickNavigateTo(`/pages/customer/list/addCustomer?types=2`)
    }
  }

  const changeTab = (e) => {
    if (data.where.page > 1) {
      backToTop()
    }

    data.tabId = e.id
    data.tabIndex = e.index
    data.where.page = 1
    data.where.types = e.id
    if (e.id == 2) {
      data.where.scope_frame = 'all'
    } else if (e.id == 2) {
      data.where.scope_frame = 'self'
    } else {
      data.where.scope_frame = 'all'
      data.where.salesman_id = ''
    }

    getTabList(true)
  }

  const formBoxChange = (e) => {
    data.where.page = 1
    data.where.customer_status = e.customer_status
    data.where.time = e.time
    data.where.customer_label = e.customer_label
    data.where.salesman_id = e.salesmanId
    getTabList(true)
  }

  // 条件判断
  const getTabList = (tab = false) => {
    data.defaultTitle = data.examineTabData[data.tabIndex].name
    const id = data.examineTabData[data.tabIndex].id
    data.typeIndex = Number(id)
    getConfigList(tab)
  }

  const listLoading = ref(false)
  // 列表加载
  const getConfigList = (tab = false) => {
    customerListApi(data.where)
      .then((res) => {
        // 切换时数据清空
        if (tab) data.listData = []
        data.listData.push(...res.data.list)
        const allPage = Math.ceil(res.data.count / data.where.limit)
        if (data.listData.length <= 0 || data.where.page >= allPage) {
          listLoading.value = false
        } else {
          listLoading.value = true
        }
        uni.stopPullDownRefresh() //停止刷新
      })
      .catch((error) => {
        message.error(error.message)
      })
  }

  import { onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'
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
    data.value = false
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

    .examine-content {
      margin-bottom: 103rpx;
    }
  }
</style>