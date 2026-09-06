<template>
  <view class="content">
    <!-- 顶部 -->
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar
        :is-jump-bar="false"
        :is-right="true"
        :default-title="data.defaultTitle"
        right-text="提交"
        @handleClickRight="clickSubmit"
      ></default-nav-bar>
    </view>
    <!-- 表单内容 -->
    <view class="examine-content m10">
      <oaForm ref="oaFormRef" :listData="listData" @submitOk="submitOk"></oaForm>
    </view>
    <!-- 底部 -->
    <!-- <view class="examine-button">
      <button class="btn" :loading="loading" @click="handleConfirm">提交</button>
    </view> -->
    <success-popup ref="successPopupRef" :type="1" title="合同" button-title="添加付款" @change="successChange"> </success-popup>
  </view>
</template>

<script setup>
import oaForm from '@/components/oaForm'
import successPopup from '@/pages/customer/list/components/successPopup.vue'
import defaultNavBar from '@/components/defaultNavBar/index'
import { ref, reactive, toRefs, computed } from 'vue'
import { contractSaveApi, contractEditSaveApi, contractCreateFormApi, contractEditInfoApi, configApproveApi } from '@/api/customer'
import message from '@/utils/message'
const placeholderStyle = ref('color: #C0C4CC;font-size: 30rpx')
const styles = reactive({
  color: '#303133',
  disableColor: '#ffffff',
})
const oaFormRef = ref(null)
// 定义表单
const data = reactive({
  defaultTitle: '添加合同',
  cid: '', // 合同id
  eid: 0,
  jumpUrl: '',
  name: '', // 客户名称
  list: [], // 客户列表
  buildData: {},
})

import { useStore } from 'vuex'
const store = useStore()
import { delayedReLaunch, clickNavigateTo } from '@/utils/helper'
import { onLoad } from '@dcloudio/uni-app'
onLoad((e) => {
  // getcustomer()
  if (e.name) {
    data.name = e.name
  }
  data.eid = e.eid
  if (e.cid) {
    data.defaultTitle = '编辑合同'
    data.cid = e.cid
  }
  getConfigApprove()
  // data.jumpUrl = toJumpUrl()
})
onMounted(() => {
  if (data.cid) {
    getcontractInfo()
  } else {
    getcontractCreate()
  }
})
const listData = ref([])

// 获取合同新增表单
const getcontractCreate = () => {
  contractCreateFormApi().then((res) => {
    if (data.eid) {
      res.data.forEach((item) => {
        item.data.forEach((val) => {
          val.text = ''
          if (val.key == 'contract_customer') {
            val.text1 = data.name
            val.options_level = 1
            val.value = data.eid
          }
        })
      })
    }
    listData.value = res.data
  })
}

// 获取编辑合同信息
const getcontractInfo = () => {
  contractEditInfoApi(data.cid).then((res) => {
    if (data.eid) {
      res.data.forEach((item) => {
        item.data.forEach((val) => {
          val.text = ''
          if (val.key == 'contract_customer') {
            val.text1 = data.name
            val.options_level = 1
            val.value = data.eid
          }
        })
      })
    }
    listData.value = res.data
  })
}

// 保存
const clickSubmit = () => {
  oaFormRef.value.submit()
}

// 新增合同
const submitOk = (form) => {
  form.types = data.types
  if (data.cid) {
    let dataId = data.cid
    clientPut(dataId, form)
  } else {
    clientSave(form)
  }
}
// 编辑
const clientPut = (id, data) => {
  contractEditSaveApi(id, data)
    .then((res) => {
      message.success(res.message)
      delayedReLaunch(`/pages/customer/contract/details?id=${id}`)
    })
    .catch((err) => {
      message.error(err.message)
    })
}
// 新增
const clientSave = (data) => {
  contractSaveApi(data)
    .then((res) => {
      message.success(res.message)
      successPopupRef.value.popupOpen(res.data.id)
    })
    .catch((err) => {
      message.error(err.message)
    })
}
const getConfigApprove = () => {
  configApproveApi().then((res) => {
    data.buildData = res.data
  })
}

const toJumpUrl = (e, cid) => {
  // 判断跳转地址
  if (e === 1) {
    delayedReLaunch('/pages/customer/contract/index')
  } else {
  }
  // const formType = store.state.app.customerFormType
  // let url = ''
  // url = (formType.type && formType.type === 'list') ? '/pages/customer/list/index' :
  //   `/pages/customer/list/contract?eid=${formData.eid}&name=${data.name}`
  // return url
}

const loading = ref(false)
const successPopupRef = ref(null)

// 去添加付款
const successChange = (e, cid) => {
  if (e === 1) {
    const url = toJumpUrl()
    delayedReLaunch(url)
  } else {
    if (data.buildData.contract_refund_switch) {
      clickNavigateTo(`/pages/users/examine/default?id=${data.buildData.contract_refund_switch}&eid=${data.eid}&cid=${cid}`)
    }
  }
}
</script>

<style lang="scss" space>
.content {
  width: 100%;
  position: relative;

  ::v-deep .uni-navbar__header-container {
    justify-content: center;
    align-items: center;
  }

  .nar-bar-title {
    font-size: 16px;
    font-weight: 500;
  }

  .examine-content {
    // #ifdef H5
    padding-top: calc($uni-default-bar-height + var(--status-bar-height));
    // #endif
    // #ifdef APP-PLUS
    padding-top: 80rpx;
    // #endif
    padding-bottom: 126rpx;
  }
}
</style>
