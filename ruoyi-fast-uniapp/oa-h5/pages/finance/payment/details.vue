<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar
        :jump-url="data.jumpUrl"
        :is-jump-bar="false"
        :is-right="true"
        :default-title="data.defaultTitle"
        :is-show-title="data.isShowTitle"
        :right-data="data.rightIcon"
        @handleNarItem="handleNarItem"
      >
      </default-nav-bar>
    </view>
    <!-- 主要内容 -->
    <view class="examine-content m10" v-if="data.info.id" :style="{ paddingBottom: data.info.status === 0 ? '126rpx' : 0 }">
      <pay-detail :info="data.info" />
    </view>

    <!-- 底部 -->
    <view class="cr-examine-button" v-if="data.info.status === 0">
      <button class="button default-error" @click="examineClick(0)">拒绝</button>
      <button class="button agree" @click="examineClick(1)">同意</button>
    </view>
    <!-- 组件 -->
    <drop-down ref="dropDownRef" :list-data="forumMeus" @btn-click="dropDownItem"></drop-down>
    <payment-examine ref="paymentExamineRef" :config-data="data.configData" @change="examineChange" />
  </view>
</template>

<script setup lang="ts">
import defaultNavBar from '@/components/defaultNavBar/index'
import dropDown from '@/pages/forum/components/dropDown.vue'
import payDetail from '@/pages/customer/contract/components/payDetail.vue'
import paymentExamine from './components/paymentExamine.vue'
import {  financeBillDeletelApi, financeBillStatuslApi } from '@/api/finance'
import { clientContractBillCateApi,billDetailApi } from '@/api/customer'
import message from '@/utils/message'
import { showModal, delayedReLaunch, clicKReLaunch } from '@/utils/helper'
import type { Res, Drop, GetType, PropType } from '@/utils/typeHelper'
import { getPayRecordTypes } from '@/utils/assessment'

const dropDownRef = ref(null)
interface Info extends PropType {
  readonly id: number
  status: number
  types: number
}
const data = reactive({
  info: <Info>{},
  id: 0,
  defaultTitle: '合同回款详情',
  isShowTitle: true,
  rightIcon: [{ type: 1, icon: 'icon-gengduo1', types: 'icon' }],
  configData: {},
  forumMenus: [],
  jumpUrl: '/pages/finance/payment/index',
  tab: 0,
})
//首次加载{

onLoad((e: GetType) => {
  data.id = Number(e.id)
  if (e.tab) {
    data.tab = Number(e.tab)
    data.jumpUrl = `/pages/finance/payment/index?tab=${e.tab}`
  }
  getbillDetail(data.id)
})
const forumMeus = computed(() => {
  if (data.info.status !== 0) {
    if (data.info.invoice_id === 0) {
      return [
        { name: '撤回审核', id: 1, icon: 'icon-danchuang-chehuishenhe' },
        { name: '编辑', id: 3, icon: 'icon-danchuang-bianji' },
        { name: '删除', id: 2, icon: 'icon-shanchu1' },
      ]
    } else {
      return [
        { name: '编辑', id: 3, icon: 'icon-danchuang-bianji' },
        { name: '删除', id: 2, icon: 'icon-shanchu1' },
      ]
    }
  } else {
    return []
  }
})
// 获取详情
const getbillDetail = (id: number): void => {
  billDetailApi(id)
    .then((res: Res) => {
      data.info = res.data
      data.defaultTitle = `合同${getPayRecordTypes(data.info.types)}详情`
      if (data.info.status === 0) {
        data.rightIcon = []
      }
    })
    .catch((error: Res) => {
      message.error(error.message)
    })
}
const handleNarItem = (): void => {
  dropDownRef.value.openDropdown()
}
const dropDownItem = (e: Drop): void => {
  if (e.id === 2) {
    showModal('删除后，对应的合同付款以及收支记账也会删除')
      .then((): void => {
        getBilllDelete(data.info.id)
      })
      .catch((): void => {})
  } else if (e.id === 1) {
    showModal('确定要撤回审核状态吗')
      .then((): void => {
        getBilllStatus(data.info.id, { status: -1 })
      })
      .catch((): void => {})
  } else {
    if (data.info.types < 2) {
      examineClick(2)
    } else {
      data.configData = {
        row: data.info,
        type: 2,
      }
      paymentExamineRef.value.popupOpen()
    }
  }
}
// 审核
const paymentExamineRef = ref(null)
const examineClick = (type: number): void => {
  getBillCate(type)
}
const examineChange = (): void => {
  clicKReLaunch(`/pages/finance/payment/details?id=${data.info.id}&tab=${data.tab}`)
}
const getBillCate = (type: number): void => {
  clientContractBillCateApi(data.info.cid)
    .then((res: Res) => {
      data.configData = {
        row: data.info,
        type: type,
        path: res.data.bill_cate_path,
      }
      paymentExamineRef.value.popupOpen()
    })
    .catch((error: Res) => {
      message.error(error.message)
    })
}

// 删除付款记录
const getBilllDelete = (id: number): void => {
  financeBillDeletelApi(id)
    .then((res: Res): void => {
      message.error(res.message)
      delayedReLaunch('/pages/finance/payment/index')
    })
    .catch((error: Res): void => {
      message.error(error.message)
    })
}

// 撤销审核
const getBilllStatus = (id: number, datas: object): void => {
  financeBillStatuslApi(id, datas)
    .then((res: Res): void => {
      message.error(res.message)
      getbillDetail(id)
    })
    .catch((error: Res): void => {
      message.error(error.message)
    })
}
</script>
<style lang="scss" scoped>
::v-deep .drop-down-list {
  min-width: 200rpx !important;
}

.content {
  .cr-position-header {
    background-color: #fff;
  }

  .nar-bar-title {
    font-size: 16px;
    font-weight: 500;
  }

  .bar-return {
    font-size: 34rpx;
    font-weight: 400;
  }

  ::v-deep .uni-navbar__header-container {
    justify-content: center;
    align-items: center;
  }

  .examine-content {
    padding-top: calc($uni-default-bar-height + var(--status-bar-height));
  }

  .examine-button {
  }
}
</style>
