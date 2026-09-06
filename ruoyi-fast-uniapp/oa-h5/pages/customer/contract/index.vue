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
      <contract-list :list-data="data.listData" :type="1" :follow="data.followIndex" :form-type="{ list: 'contract' }"
        :empty-title="data.emptyTitle">
      </contract-list>
    </view>
    <global-index />
    <tabbar :currentIndex="1" />
  </view>
</template>

<script setup>
  import tabbar from '@/components/tabbar/index.vue'
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import bottomNavigation from '@/components/bottomNavigation/index.vue'
  import contractList from './components/contractList.vue'
  import customerTab from '@/pages/customer/list/components/customerTab.vue'
  import globalIndex from '@/components/globalIndex/index.vue'
  import formBox from './components/formBox.vue'
  import { ref, reactive } from 'vue'
  import message from '@/utils/message'
  import { contractTabData } from '@/utils/assessment'
  import { useStore } from 'vuex'
  const store = useStore()
  const currentIndex = ref(store.state.app.currentIndex)
  const data = reactive({
    typeIndex: 0,
    tabIndex: 0,
    tabId: 1,
    followIndex: 0,
    emptyTitle: '当前暂无合同～',
    customStyle: { border: 'none', lineHeight: '20px', background: '#ED4014' },
    examineTabData: contractTabData,
    listData: [],
    where: {
      page: 1,
      limit: 10,
      types: 6,
      scope_frame: 'self',
      pay_status: '',
      name: '',
      renew: '',
      follows: '',
      sort: '',
      abnormal: '',
      time: '',
      salesman_id: '',
    },
  })

  import { clientContractListApi } from '@/api/customer'
  import { onLoad } from '@dcloudio/uni-app'
  onLoad((options) => {
    getTabList()
  })

  const rightIcon = reactive([{ type: 1, icon: 'icon-sousuo' }])

  import { clickNavigateTo, backToTop } from '@/utils/helper'
  const handleNarItem = (e) => {
    if (e.type === 1) {
      clickNavigateTo(`/pages/customer/contract/search?index=${data.tabIndex}&type=${data.where.types}`)
    } else {
      clickNavigateTo('/pages/customer/contract/addContract')
    }
  }

  const changeTab = (e) => {
    if (data.where.page > 1) {
      backToTop()
    }
    data.where.types = e.id
    if (e.id == 5) {
      data.where.scope_frame = 'all'
    } else {
      data.where.scope_frame = 'self'
    }

    data.tabIndex = e.index
    data.followIndex = e.index
    data.where.page = 1
    getTabList(true)
  }

  const formBoxChange = (e) => {
    data.where.page = 1
    data.where.abnormal = e.status
    data.where.signing_status = e.label
    data.where.time = e.time
    data.where.salesman_id = e.salesmaId
    getTabList(true)
  }

  // 条件判断
  const getTabList = (tab = false) => {
    data.where.page = 1
    // data.where.types = data.tabId === 2 ? 0 : 1
    getConfigList(tab)
  }

  const listLoading = ref(false)
  // 列表加载
  const getConfigList = (tab = false) => {
    clientContractListApi(data.where)
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