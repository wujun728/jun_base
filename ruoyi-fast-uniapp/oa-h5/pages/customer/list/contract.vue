<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right="true" :jump-url="data.jumpUrl" :is-jump-bar="false" :right-data="rightIcon"
        @handleNarItem="handleNarItem">
      </default-nav-bar>
    </view>
    <view class="examine-content m10">
      <contract-list :list-data="data.listData" :type-index="data.typeIndex"
        :form-type="{type: 'list', eid: data.where.eid}" :empty-title="data.emptyTitle">
      </contract-list>
    </view>
    <global-index />
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import bottomNavigation from '@/components/bottomNavigation/index.vue'
  import contractList from '@/pages/customer/contract/components/contractList.vue'
  import globalIndex from '@/components/globalIndex/index.vue'
  import { clientContractListApi } from '@/api/customer'
  import { ref, reactive, onMounted } from 'vue'
  import message from '@/utils/message'
  const data = reactive({
    typeIndex: 0,
    tabIndex: 0,
    tabId: 1,
    name: '',
    emptyTitle: '暂无合同，快去添加吧～',
    listData: [],
    types: 0,
    where: {
      limit: 10,
      page: 1,
      types: '',
      eid: '',
    },
    jumpUrl: ''
  })

  import { onLoad } from '@dcloudio/uni-app'
  onLoad(e => {
    data.where.eid = e.eid
    data.jumpUrl = `/pages/customer/list/details?id=${ data.where.eid}`
    if (e.name) {
      data.name = e.name
    }
    if (e.types == 3) {
      rightIcon = rightIcon.splice(1, 1)
    }

    getConfigList()
  })

  const rightIcon = reactive([
    { type: 1, icon: 'icon-sousuo' },
    { type: 2, icon: 'icon-a-gengduo2' },
  ])

  import { clickNavigateTo, backToTop } from '@/utils/helper'
  import { useStore } from 'vuex'
  const store = useStore()
  const handleNarItem = e => {
    if (e.type === 1) {
      clickNavigateTo(`/pages/customer/contract/search?eid=${data.where.eid}`)
    }
    if (e.type === 2) {
      store.commit('setCustomerFormType', { type: 'list-contract' })
      clickNavigateTo(`/pages/customer/contract/addContract?eid=${data.where.eid}&name=${data.name}`)
    }
  }

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