<template>
  <view class="content">
    <!-- 发票详情 -->
    <view class="cr-position-header">
      <default-nav-bar :is-right="true" :backgroundColor="data.backgroundColor" :color="data.color"
        :default-title="data.defaultTitle" :is-show-title="data.isShowTitle" :right-data="data.rightIcon"
        @handleNarItem="handleNarItem">
      </default-nav-bar>
    </view>
    <!-- 内容 -->
    <invoice-detail :detail="data.detail" />
    <drop-down ref="dropDownRef" :list-data="data.forumMeus" @btn-click="dropDownItem"></drop-down>
    <textarea-popup ref="textareaPopupRef" :config-data="data.configData" @change="changePop"></textarea-popup>
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index'
  import message from '@/utils/message'
  import dropDown from '@/pages/forum/components/dropDown.vue'
  import textareaPopup from '@/components/textareaPopup/index.vue'
  import invoiceDetail from './components/invoiceDetail.vue'
  import { ref, reactive, toRefs } from 'vue'
  const data = reactive({
    defaultTitle: '发票详情',
    isShowTitle: true,
    rightIcon: [
      { type: 1, icon: 'icon-gengduo1', types: 'icon' }
    ],
    operation_time: '',
    created_at: '',
    recordList: [],
    buildData: [],
    current: 0,
    backgroundColor: 'rgba(0,0,0,0)',
    color: '#fff',
    id: 0,
    detail: {},
    forumMeus: [
      { name: '备注', id: 1, icon: 'icon-gongzuohuibao-bianji' },
      { name: '转移', id: 6, icon: 'icon-danchuang-zhuanyi' }
    ],
    configData: {}
  })

  import {
    clientInvoiceDetailsApi,
    clientInvoiceMarkApi,
    clientInvoiceWithdrawApi,
    clientInvoiceInvalidApplyApi,
    configApproveApi
  } from '@/api/customer'
  import { approveApplyRevokeApi } from '@/api/business'
  import { onLoad } from '@dcloudio/uni-app'
  onLoad(options => {
    data.id = Number(options.id)
    getDetails(options.id)
    getConfigApprove()
  })
  const popupRef = ref()
  const dropDownRef = ref(null)
  const handleNarItem = e => {
    dropDownRef.value.openDropdown()
  }
  const subStringFn = val => {
    if (val) {
      return val.substring(0, val.lastIndexOf('：'))
    } else {
      return '--'
    }
  }
  import { showModal } from '@/utils/helper'
  const textareaPopupRef = ref(null)
  // 点击回调
  import { clickNavigateTo } from '@/utils/helper'
  const dropDownItem = e => {

    if (e.id === 6) {
      clickNavigateTo(`/pages/customer/list/shift?type=3&iid=${data.id}`)
    } else if (e.id == 4) {
      clickNavigateTo(`/pages/users/examine/default?id=${data.buildData.void_invoice_switch
      }&invoice_id=${data.detail.id}&types=invoice`)
    } else {
      if (e.id === 1) {
        data.configData = {
          title: '备注',
          placeholder: '请填写备注信息',
          type: e.id,
          text: data.detail.mark
        }
        textareaPopupRef.value.popupOpen()
      } else if (e.id === 2) {
        // 发票撤销
        showModal('您确定要撤销该发票吗').then(res => {
          approveApplyRevokeApi(data.detail.link_id).then(res => {
            message.success(res.message)
            getDetails(data.detail.id)
          })
        }).catch(error => {
          console.log('取消了')
        })

      } else if (e.id == 3) {
        // 撤回作废
        showModal('您确定要撤回作废该发票吗').then(res => {
          approveApplyRevokeApi(data.detail.revoke_id).then(res => {
            message.success(res.message)
            getDetails(data.detail.id)
          })
        }).catch(error => {
          console.log('取消了')
        })
      }
    }
  }

  const getConfigApprove = () => {
    configApproveApi().then(res => {
      data.buildData = res.data
    })
  }

  const changePop = e => {
    if (e.type === 1) {
      getMark(data.id, { mark: e.value })
    }
  }

  const getDetails = id => {
    data.forumMeus = [
      { name: '备注', id: 1, icon: 'icon-gongzuohuibao-bianji' },
      { name: '转移', id: 6, icon: 'icon-danchuang-zhuanyi' }
    ]
    clientInvoiceDetailsApi(id).then(res => {
      data.detail = res.data
      if (data.forumMeus.length <= 2) {
        if (data.detail.status === 0) {
          data.forumMeus.push({ name: '发票撤回', id: 2, icon: 'icon-danchuang-chehui' })
        } else if (data.detail.status === 4) {
          data.forumMeus.push({ name: '撤回作废', id: 3, icon: 'icon-danchuang-chehui' })
        } else if (data.detail.status === 5) {
          data.forumMeus.push({ name: '申请作废', id: 4, icon: 'icon-danchuang-shenqingzuofei' })
        }
      }

    })
  }

  // 发票备注修改
  const getMark = (id, datas) => {
    clientInvoiceMarkApi(id, datas).then(res => {
      message.success(res.message)
      getDetails(id)
    }).catch(error => {
      message.error(error.message)
    })
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
  }

  .uni-picker-header {
    border-bottom: 1px solid #e5e5e5;
    width: 100%;
    height: 90rpx;
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .uni-picker-action {
      max-width: 50%;
      top: 0;
      height: 100%;
      box-sizing: border-box;
      padding: 0 14px;
      font-size: 30rpx;
      line-height: 90rpx;
      cursor: pointer;

      &.uni-picker-action-cancel {
        color: #888;
      }

      &.uni-picker-action-confirm {
        color: #007aff;
      }
    }
  }


  .picker-view {
    width: 750rpx;
    height: 480rpx;
    background-color: #fff;
  }

  .item-value {
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>