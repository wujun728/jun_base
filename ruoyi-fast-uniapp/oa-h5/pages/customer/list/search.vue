<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <view class="default-search">
        <uni-search-bar @confirm="search" @focus="search" :placeholder="data.placeholder" :focus="true"
          bgColor="#F0F1F5" v-model="data.where.customer_name" @clear="clearSearch" @cancel="cancelSearch">
        </uni-search-bar>
      </view>
      <customer-tab :examine-tab-data="data.examineTabData" @change="changeTab" :index="data.tabIndex"></customer-tab>
    </view>

    <view class="examine-content m10">
      <customer-list-default :list-data="data.listData" :type-index="data.typeIndex" :types="data.where.types"
        :empty-title="data.emptyTitle">
      </customer-list-default>
    </view>
  </view>
</template>

<script setup>
  import { ref, reactive } from 'vue'
  import message from '@/utils/message'
  import customerListDefault from './components/customerListDefault.vue'
  import customerTab from './components/customerTab.vue'
  import { customerTabData } from '@/utils/assessment'
  import { backToTop } from '@/utils/helper'
  import { customerListApi } from '@/api/customer'
  import { onLoad } from '@dcloudio/uni-app'
  import { onReachBottom } from '@dcloudio/uni-app'
  const data = reactive({
    typeIndex: 0,
    tabIndex: 0,
    placeholder: '搜索客户名称',
    examineTabData: customerTabData,
    listData: [],
    emptyTitle: '暂无搜索结果～',

    where: {
      limit: 10,
      page: 1,
      types: 1,
      customer_name: ''
    }
  })

  onLoad(options => {
    const len = data.examineTabData.length - 1
    if (options.type) {
      data.where.types = options.type
    }
    if (options.index) {
      if (options.index > len) {
        data.tabIndex = len
      } else {
        data.tabIndex = Number(options.index)
      }
    }
  })

  const clearSearch = () => {
    data.where.customer_name = ''
    data.listData = []
  }

  const cancelSearch = () => {
    uni.navigateBack()
  }

  const search = () => {
    data.where.page = 1
    if (data.where.customer_name) {
      getTabList(true)
    } else {
      clearSearch()
    }
  }

  const changeTab = e => {
    if (data.where.page > 1) {
      backToTop()
    }
    console.log(e)
    data.tabIndex = e.index
    data.where.page = 1
    data.where.types = e.id
    // if (e.name === '我查看的') {
    //   data.where.types = 1
    // } else if (e.name === '我负责的') {
    //   data.where.types = 2
    // } else {
    //   data.where.types = 3

    // }
    if (data.where.customer_name) {
      getTabList(true)
    } else {
      clearSearch()
    }
  }

  // 条件判断
  const getTabList = (tab = false) => {
    const id = data.examineTabData[data.tabIndex].id
    data.typeIndex = Number(id)

    getConfigList(tab)
  }

  const listLoading = ref(false)
  // 列表加载
  const getConfigList = (tab = false) => {
    customerListApi(data.where).then(res => {
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
    }).catch(error => {
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