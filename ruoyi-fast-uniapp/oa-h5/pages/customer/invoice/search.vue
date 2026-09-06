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
      <invoice-list :list-data="data.listData" :empty-title="data.emptyTitle"></invoice-list>
    </view>

    <global-index />
  </view>
</template>

<script setup>
  import invoiceList from './components/invoiceList.vue'
  import customerTab from '@/pages/customer/list/components/customerTab.vue'
  import globalIndex from '@/components/globalIndex/index.vue'
  import formBox from './components/formBox.vue'
  import { ref, reactive, onMounted } from 'vue'
  import message from '@/utils/message'
  import { customerTabData } from '@/utils/assessment'
  const data = reactive({
    typeIndex: 0,
    tabIndex: 0,
    tabId: 1,
    eid: '',
    emptyTitle: '暂无搜索结果～',
    customStyle: { border: 'none', lineHeight: '20px', background: '#ED4014' },
    examineTabData: [{ name: '我负责的', id: 2 },
      { name: '我查看的', id: 1 },
    ],
    listData: [],
    where: {
      eid: '',
      page: 1,
      limit: 10,
      name: '',
      time: '',
      status: '',
      types: '',
      way: 1,
      salesman_id: '',
    },
  })

  import { clientInvoiceApi } from '@/api/customer'
  import { onLoad } from '@dcloudio/uni-app'
  onLoad(options => {
    if (options.index) {
      data.tabIndex = Number(options.index)
    }
    if (e.way) {
      data.where.way = e.way
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

  const changeTab = e => {
    console.log(e)
    if (data.where.page > 1) {
      backToTop()
    }
    data.tabId = e.id
    data.tabIndex = e.index
    data.where.page = 1
    // if (data.where.name) {
    getTabList(true)
    // }
  }

  // 条件判断
  const getTabList = (tab = false) => {
    data.where.page = 1
    if (data.tabIndex === 0) {
      data.where.way = 1
    } else {
      data.where.way = 0
    }
    getConfigList(tab)
  }

  const listLoading = ref(false)
  // 列表加载
  const getConfigList = (tab = false) => {
    clientInvoiceApi(data.where).then(res => {
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

  import { onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'
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