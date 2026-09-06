<template>
  <view class="content">
    <!-- 顶部 -->
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar
        :is-right="true"
        :default-title="data.defaultTitle"
        :right-data="[]"
        right-text="提交"
        @handleClickRight="clickSubmit"
      ></default-nav-bar>
    </view>
    <!-- 表单内容 -->
    <view class="examine-content m10">
      <oaForm
        ref="oaFormRef"
        :listData="listData"
        @submitOk="submitOk"
      ></oaForm>
    </view>

    <!-- 组件 -->
    <success-popup
      ref="successPopupRef"
      :type="0"
      title="客户"
      button-title="添加合同"
      @change="successChange"
    ></success-popup>
  </view>
</template>
<script setup>
import oaForm from '@/components/oaForm/index.vue'
import defaultNavBar from '@/components/defaultNavBar/index.vue'
import { ref, reactive } from 'vue'
import message from '@/utils/message'
import successPopup from './components/successPopup.vue'
import { clickNavigateTo, delayedReLaunch } from '@/utils/helper'
import {
  clientSaveApi,
  clientPutApi,
  clientEditInfoApi,
  clientCreateFormApi,
} from '@/api/customer'
const oaFormRef = ref(null)
const listData = ref([])
const styles = reactive({
  color: '#303133',
  disableColor: '#ffffff',
})
// 定义表单
const data = reactive({
  eid: '',
  types: 0,
  is_show: false,
  name: '', // 客户名称
  defaultTitle: '添加客户',
  isShowTitle: false,
})

import { onLoad } from '@dcloudio/uni-app'
onLoad((e) => {
  if (e.types) {
    data.types = e.types
  }
  if (e.eid) {
    data.defaultTitle = '编辑客户'
    data.eid = e.eid
    getclientInfo()
  } else {
    getclientCreate()
  }
})

// 获取客户新增表单
const getclientCreate = () => {
  clientCreateFormApi().then((res) => {
    listData.value = res.data
  })
}
// 获取编辑客户信息
const getclientInfo = () => {
  clientEditInfoApi(data.eid).then((res) => {
    listData.value = res.data
  })
}
import { useStore } from 'vuex'
const store = useStore()
const successPopupRef = ref(null)

// 保存
const clickSubmit = () => {
  oaFormRef.value.submit()
}
// 新增客户
const submitOk = (form) => {
  data.name = form.customer_name
  form.types = data.types
  if (data.eid) {
    let dataId = data.eid
    clientPut(dataId, form)
  } else {
    clientSave(form)
  }
}
// 编辑
const clientPut = (id, data) => {
  clientPutApi(id, data)
    .then((res) => {
      message.success(res.message)
      delayedReLaunch(`/pages/customer/list/details?id=${id}`)
    })
    .catch((err) => {
      message.error(err.message)
    })
}
// 新增
const clientSave = (data) => {
  clientSaveApi(data)
    .then((res) => {
      data.eid = res.data.id
      message.success(res.message)
      successPopupRef.value.popupOpen(res.data.id)
    })
    .catch((err) => {
      message.error(err.message)
    })
}

// 去添加合同
const successChange = (e, eid) => {
  if (e === 1) {
    delayedReLaunch('/pages/customer/list/index')
  } else {
    clickNavigateTo(
      `/pages/customer/contract/addContract?eid=${eid}&name=${data.name}`,
    )
  }
}
</script>
<style lang="scss" scoped>
.content {
  width: 100%;
  position: relative;

  .cr-position-header {
    background-color: #fff;
  }

  .examine-content {
    padding-top: calc($uni-default-bar-height + var(--status-bar-height));
    padding-bottom: 126rpx;
  }
}
</style>
