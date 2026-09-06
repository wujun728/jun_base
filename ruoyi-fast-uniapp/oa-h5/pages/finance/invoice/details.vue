<template>
  <view class="content">
    <!-- 发票详情 -->
    <view class="cr-position-header">
      <default-nav-bar :jump-url="data.jumpUrl" :is-jump-bar="false" :is-right="true"
        :backgroundColor="data.backgroundColor" :color="data.color" :default-title="data.defaultTitle">
      </default-nav-bar>
    </view>
    <!-- 内容 -->
    <invoice-detail :detail="data.detail" />

    <!-- 底部 -->
    <view class="cr-examine-button" v-if="data.detail.status === 5||data.detail.status === 1">
      <button class="button default-error" v-if="data.detail.status === 5" @click="toExamine(2)">作废</button>
      <button v-if="data.detail.status === 1" class="button agree" @click="toExamine(1)">录入发票</button>
    </view>
    <invoice-examine ref="invoiceExamineRef" :config-data="data.config" @change="changeInvoice" />
  </view>
</template>

<script setup lang="ts">
  import defaultNavBar from '@/components/defaultNavBar/index'
  import message from '@/utils/message'
  import invoiceDetail from '@/pages/customer/invoice/components/invoiceDetail.vue'
  import invoiceExamine from './components/invoiceExamine.vue'
  import { clientInvoiceDetailsApi } from '@/api/customer'
  import type { Res, Detail, GetType } from '@/utils/typeHelper'

  const showBtn : Ref<boolean> = ref(false)
  const data = reactive({
    defaultTitle: '发票详情',
    backgroundColor: 'rgba(0,0,0,0)',
    color: '#fff',
    id: <number>0,
    detail: <Detail>{},
    showArray: <Array<number>>[2, 4],
    config: {
      row: {},
      type: 0
    },
    jumpUrl: '/pages/finance/invoice/index'
  })
  //data ---end--- {

  onLoad((options : GetType) => {
    data.id = Number(options.id)
    if (options.tab) {
      data.jumpUrl = `/pages/finance/invoice/index?tab=${options.tab}`
    }
    getDetails(data.id)
  })

  const getDetails = (id : number) => {
    clientInvoiceDetailsApi(id).then((res : Res) => {
      data.detail = res.data
      showBtn.value = !data.showArray.includes(data.detail.status)
    }).catch((error : Res) => {
      message.error(error.message)
    })
  }
  const invoiceExamineRef = ref(null)
  const toExamine = (type : number) : void => {
    data.config = {
      row: data.detail,
      type: type
    }
    invoiceExamineRef.value.popupOpen()
  }

  const changeInvoice = () : void => {
    getDetails(data.id)
  }
</script>

<style lang="scss" scoped>
  .content {
    width: 100%;

    .cr-position-header {
      position: fixed;
      padding-top: var(--status-bar-height);
      height: calc($uni-default-bar-height + var(--status-bar-height));
      background: linear-gradient(#459FFF 0%, #388AEF 100%);
    }

    ::v-deep .content-box {
      padding-bottom: v-bind('showBtn ? "146rpx" : "20rpx"');
    }
  }
</style>