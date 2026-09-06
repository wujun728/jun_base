<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right="true" :jump-url="data.jumpUrl" :is-jump-bar="false" :right-data="rightIcon"
        @handleNarItem="handleNarItem"></default-nav-bar>
    </view>
    <view class="examine-content m10">
      <liaison-list :list-data="data.listData" :type-index="data.typeIndex" :empty-title="data.emptyTitle">
      </liaison-list>
    </view>

    <global-index />
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import bottomNavigation from '@/components/bottomNavigation/index.vue'
  import liaisonList from './components/liaisonList.vue'
  import globalIndex from '@/components/globalIndex/index.vue'
  import { ref, reactive } from 'vue'
  import { clientLiaisonApi } from '@/api/customer'
  import message from '@/utils/message'
  const data = reactive({
    typeIndex: 0,
    tabIndex: 0,
    tabId: 1,
    emptyTitle: '暂无联系人，快去添加吧～',
    listData: [],
    where: {
      limit: 10,
      page: 1,
      eid: '', // 客户id
    },
    jumpUrl: ''
  })

  import { onLoad } from '@dcloudio/uni-app'
  onLoad(e => {
    data.where.eid = e.eid
    data.jumpUrl = `/pages/customer/list/details?id=${e.eid}`
    getConfigList()
  })
  const rightIcon = reactive([
    { type: 2, icon: 'icon-a-gengduo2' },
  ])

  import { clickNavigateTo, backToTop } from '@/utils/helper'
  const handleNarItem = e => {
    clickNavigateTo(`/pages/customer/list/addLiaison?eid=${data.where.eid}`)
  }
  const listLoading = ref(false)
  // 列表加载
  const getConfigList = (tab = false) => {
    clientLiaisonApi(data.where).then(res => {
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

    .examine-content {}
  }
</style>