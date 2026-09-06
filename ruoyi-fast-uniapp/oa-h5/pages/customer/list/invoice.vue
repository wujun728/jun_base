<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right="true" :jump-url="data.jumpUrl" :is-jump-bar="false" :right-data="rightIcon"
        @handleNarItem="handleNarItem">
      </default-nav-bar>
    </view>

    <view class="examine-content m10">
      <invoice-list :list-data="data.listData" :type="data.typeIndex" :name="data.name" :eid=" data.where.eid"
        :empty-title="data.emptyTitle">
      </invoice-list>
    </view>

    <global-index />
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import invoiceList from '@/pages/customer/invoice/components/invoiceList.vue'
  import globalIndex from '@/components/globalIndex/index.vue'
  import { clientInvoiceApi, unInvoicedListApi, configApproveApi } from '@/api/customer'
  import { ref, reactive, onMounted } from 'vue'
  import message from '@/utils/message'
  const data = reactive({
    typeIndex: 0,
    tabIndex: 0,
    tabId: 1,
    emptyTitle: '暂无发票，快去申请吧～',
    listData: [],
    buildData: {},
    name: '', // 客户名称
    where: {
      limit: 10,
      page: 1,
      types: '',
      eid: ''
    },
    jumpUrl: ''
  })

  import { onLoad } from '@dcloudio/uni-app'
  onLoad(e => {
    data.where.eid = e.eid
    data.jumpUrl = `/pages/customer/list/details?id=${e.eid}`
    data.name = e.name
    if (e.types == 3) {
      rightIcon = rightIcon.splice(1, 1)
    }
    getConfigList()
    getConfigApprove()
  })

  const rightIcon = reactive([
    { type: 1, icon: 'icon-sousuo' },
    { type: 2, icon: 'icon-a-gengduo2' },
  ])
  const getConfigApprove = () => {
    configApproveApi().then(res => {
      data.buildData = res.data
    })
  }

  import { clickNavigateTo, backToTop } from '@/utils/helper'
  const handleNarItem = e => {
    if (e.type === 1) {
      clickNavigateTo(`/pages/customer/invoice/search?eid=${data.where.eid}`)
    }
    if (e.type === 2) {

      // invoicing_switch
      let dataInfo = { eid: data.where.eid }
      unInvoicedListApi(dataInfo).then(res => {
        if (res.data.length > 0) {
          clickNavigateTo(
            `/pages/customer/invoice/checkPayment?id=${data.buildData.invoicing_switch}&eid=${data.where.eid}&name=${data.name}`
          )
        } else {
          clickNavigateTo(
            `/pages/users/examine/default?id=${data.buildData.invoicing_switch}&eid=${data.where.eid}&types=invoice`
          )
        }
      })

    }
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