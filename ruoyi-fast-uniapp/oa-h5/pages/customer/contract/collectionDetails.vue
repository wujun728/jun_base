<template>
  <view class="content">
    <!-- 合同回款详情和合同续费详情 合同回款：type==0 合同续费：type==1  -->
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right=" data.isRight" :default-title="data.defaultTitle" :is-show-title="data.isShowTitle"
        :right-data="data.rightIcon" @handleNarItem="handleNarItem">
      </default-nav-bar>
    </view>
    <!-- 主要内容 -->

    <view class="examine-content m10" v-if="data.info.id">
      <pay-detail :info="data.info" />
    </view>
    <!-- 组件 -->
    <invoice-examine ref="invoiceExamineRef" :config-data="data.config" @change="changeExamine" />
    <drop-down ref="dropDownRef" :list-data="forumMeus" @btn-click="dropDownItem"></drop-down>
    <textarea-popup ref="textareaPopupRef" :config-data="data.configData" @change="changePop"></textarea-popup>
  </view>
</template>

<script setup>
  import { approveApplyRevokeApi } from '@/api/business'
  import defaultNavBar from '@/components/defaultNavBar/index'
  import dropDown from '@/pages/forum/components/dropDown.vue'
  import payDetail from './components/payDetail.vue'
  import textareaPopup from '@/components/textareaPopup/index.vue'
  import { ref, reactive, toRefs, computed } from 'vue'
  import { billDetailApi, billWithdrawApi, billMarkApi, billDeleteApi } from '@/api/customer'
  import message from '@/utils/message'
  import { clickNavigateTo, delayedReLaunch, showModal } from '@/utils/helper'
  import { getPayRecordTypes } from '@/utils/assessment'
  import { onLoad } from '@dcloudio/uni-app'
  const textareaPopupRef = ref(null)
  const dropDownRef = ref(null)
  const invoiceExamineRef = ref(null)

  const data = reactive({
    info: {},
    isRight: true,
    id: '', // 合同付款id
    eid: '', // 客户id
    types: 0, // 从客户来的
    defaultTitle: '合同回款详情',
    isShowTitle: true,
    rightIcon: [{ type: 1, icon: 'icon-gengduo1', types: 'icon' }],
  })

  onLoad((e) => {
    data.id = e.id
    data.eid = e.eid
    data.types = e.types
    getbillDetail(e.id)
  })
  const forumMeus = computed(() => {
    if (data.info.status == 2) {
      return [
        { name: '删除', id: 4, icon: 'icon-shanchu1' },
        { name: '重新提交', id: 3, icon: 'icon-danchuang-chehui' },
      ]
    } else if (data.info.status == -1) {
      return [

      ]
      data.isRight = false
    } else if (data.info.status === 1 && !data.info.recall) {
      return [
        { name: '撤回', id: 2, icon: 'icon-danchuang-chehui' },
      ]
    } else {
      data.isRight = false
    }
  })

  const getbillDetail = (id) => {
    billDetailApi(id).then((res) => {
      data.info = res.data
      if (data.info.status == -1) {
        data.isRight = false
      }
      data.defaultTitle = `合同${getPayRecordTypes(data.info.types)}详情`
    })
  }
  const handleNarItem = (e) => {

    dropDownRef.value.openDropdown()
  }
  const dropDownItem = (e) => {

    if (e.id === 3) {
      if (data.info.types <= 1) {
        clickNavigateTo(`/pages/customer/contract/addPayment?eid=${data.eid}&id=${data.id}`)
      } else {
        clickNavigateTo(`/pages/customer/list/addSpend?eid=${data.eid}&cid=${data.types}&id=${data.id}`)
      }
    }
    if (e.id === 2) {
      if (data.info.status === 1) {
        clickNavigateTo(`/pages/users/examine/components/addSignature?id=${data.info.apply_id}&type=3`)
      } else {
        showModal('您确定要撤回此账目记录吗')
          .then((res) => {
            approveApplyRevokeApi(data.info.apply_id)
              .then((res) => {
                message.success(res.message)
              })
              .catch((error) => {
                message.error(error.message)
              })
          })
          .catch(() => {
            console.log('取消')
          })
      }
    }
    if (e.id === 1) {

      data.configData = {
        title: '备注',
        placeholder: '请填写备注信息',
        type: e.id,
      }

      textareaPopupRef.value.popupOpen(data.info.mark)
    }

    if (e.id === 4) {
      if (data.info.status === 1) {
        clickNavigateTo(`/pages/users/examine/components/addSignature?id=${data.info.apply_id}&type=3`)
      } else {
        showModal('您确定要删除此账目记录吗')
          .then((res) => {
            billDelete(data.id)
          })
          .catch((error) => {
            console.log('取消了')
          })
      }

    }
  }
  const changePop = (e) => {
    let markId = data.id
    let infoData = { mark: e.value }
    billMarkApi(markId, infoData)
      .then((res) => {
        message.success(res.message)
        getbillDetail(data.id)
      })
      .catch((err) => {
        message.error(err.message)
      })
  }

  // 删除付款记录
  const billDelete = (id) => {
    billDeleteApi(id)
      .then((res) => {
        message.success(res.message)
        if (data.types > 0) {
          delayedReLaunch(`/pages/customer/contract/details?id=${data.types}&tab=1`)
        } else {
          delayedReLaunch(`/pages/customer/list/details?id=${data.eid}&type=3`)
        }
      })
      .catch((error) => {
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
  }
</style>