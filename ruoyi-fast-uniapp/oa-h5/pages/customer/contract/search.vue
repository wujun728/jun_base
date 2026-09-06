<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <view class="default-search">
        <uni-search-bar @confirm="search" @focus="search" :focus="true" bgColor="#F0F1F5" v-model="data.where.name"
          @clear="clearSearch" @cancel="cancelSearch">
        </uni-search-bar>
      </view>
      <customer-tab v-if="!data.eid" :examine-tab-data="data.examineTabData" @change="changeTab" :index="data.tabIndex">
      </customer-tab>
    </view>
    <view class="examine-content m10">
      <contract-list :list-data="data.listData" :type-index="data.typeIndex" :btnShow="data.btnShow"
        :empty-title="data.emptyTitle">
      </contract-list>
    </view>
  </view>
</template>

<script setup>
  import { ref, reactive } from 'vue'
  import message from '@/utils/message'
  import contractList from './components/contractList.vue'
  import customerTab from '@/pages/customer/list/components/customerTab.vue'
  import { customerTabData } from '@/utils/assessment'
  const data = reactive({
    typeIndex: 0,
    tabIndex: 0,
    eid: '',
    examineTabData: [{ name: '我负责的', id: 6 },
      { name: '我查看的', id: 5 },
    ],
    listData: [],
    emptyTitle: '暂无搜索结果～',
    btnShow: false,
    where: {
      page: 1,
      limit: 10,
      types: 1,
      eid: '',
      pay_status: '',
      name: '',
      renew: '',
      sort: '',
      abnormal: '',
      time: '',
      sign_status: '',
      salesman_id: '',
    }
  })

  import { onLoad } from '@dcloudio/uni-app'
  onLoad(options => {
    if (options.index) {
      data.tabIndex = Number(options.index)
    }
    if (options.type) {
      data.where.types = options.type
    }
    if (options.eid) {
      data.eid = options.eid
      data.where.eid = options.eid
    }
  })

  const clearSearch = () => {
    data.where.name = ''
    data.listData = []
  }

  const cancelSearch = () => {
    uni.navigateBack()
  }

  const search = () => {
    data.where.page = 1
    if (data.where.name) {
      getTabList(true)
    } else {
      clearSearch()
    }
  }

  import { backToTop } from '@/utils/helper'
  const changeTab = e => {
    if (data.where.page > 1) {
      backToTop()
    }
    data.tabIndex = e.index
    data.where.page = 1
    if (data.where.name) {
      getTabList(true)
    } else {
      clearSearch()
    }
  }

  // 条件判断
  const getTabList = (tab = false) => {
    data.where.types = data.tabIndex === 1 ? 0 : 1
    getConfigList(tab)
  }

  import { clientContractListApi } from '@/api/customer'
  const listLoading = ref(false)
  // 列表加载
  const getConfigList = (tab = false) => {
    clientContractListApi(data.where).then(res => {
      // 切换时数据清空
      if (tab) data.listData = []
      data.listData.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / data.where.limit)
      if (data.listData.length <= 0 || data.where.page >= allPage) {
        listLoading.value = false
      } else {
        listLoading.value = true
      }
    }).catch(error => {
      message.error(error.message)
    })
  }

  import { onReachBottom } from '@dcloudio/uni-app'
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