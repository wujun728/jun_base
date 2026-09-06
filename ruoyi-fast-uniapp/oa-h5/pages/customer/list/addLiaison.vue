<template>
  <view class="content">
    <!-- 顶部 -->
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right="true" :default-title="data.defaultTitle" right-text="提交"
        @handleClickRight="clickSubmit">
      </default-nav-bar>
    </view>
    <!-- 表单内容 -->
    <view class="examine-content m10">
      <oaForm ref="oaFormRef" :listData="listData" @submitOk="submitOk"></oaForm>
    </view>
  </view>
</template>

<script setup>
  import oaForm from '@/components/oaForm'
  import defaultNavBar from '@/components/defaultNavBar/index'
  import { ref, reactive, toRefs } from 'vue'
  import { liaisonSaveApi, liaisonPutApi, liaisonCreateFormApi, clientLiaisonDetailApi } from '@/api/customer'
  import { delayedReLaunch, getFindIndex, phonelineReg, mailboxReg } from '@/utils/helper'
  import { onLoad } from '@dcloudio/uni-app'
  import message from '@/utils/message'
  const oaFormRef = ref(null)
  const listData = ref([])
  const loading = ref(false)

  const data = reactive({
    defaultTitle: '添加联系人',
    treeData: [],
    rangeTypeText: '',
    type: '',
    eid: '', //客户id
    liaisonId: 0, // 联系人id
  })

  onLoad(e => {
    data.eid = e.eid
    if (e.id) {
      data.defaultTitle = '编辑联系人'
      data.liaisonId = Number(e.id)
      getLiaisonDetail(data.liaisonId)
    } else {
      liaisonCreate()
    }
  })

  // 新增联系人表单
  const liaisonCreate = () => {
    liaisonCreateFormApi().then(res => {
      listData.value = res.data
    })
  }

  // 编辑联系人表单
  const getLiaisonDetail = (id) => {
    clientLiaisonDetailApi(id).then(res => {
      listData.value = res.data
    })
  }
  // 保存
  const clickSubmit = () => {
    oaFormRef.value.submit()
  }

  // 新增.编辑联系人
  const submitOk = (form) => {
    form.eid = data.eid
    if (data.liaisonId) {
      let dataId = data.liaisonId
      liaisonPut(dataId, form)
    } else {
      liaisonSave(form)
    }
  }

  // 添加联系人
  const liaisonSave = datas => {
    liaisonSaveApi(datas).then(res => {
      if (res.status == 200) {
        message.success(res.message)
        delayedReLaunch(`/pages/customer/list/liaison?eid=${data.eid}`)
      }

    }).catch(error => {
      message.error(error.message)
    })
  }

  // 编辑联系人
  const liaisonPut = (id, datas) => {
    liaisonPutApi(id, datas).then(res => {
      if (res.status == 200) {
        message.success(res.message)
        delayedReLaunch(`/pages/customer/list/liaison?eid=${data.eid}`)
      }
    }).catch(error => {
      message.error(error.message)
    })
  }
</script>

<style lang="scss" scoped>
  .content {
    width: 100%;
    position: relative;

    .cr-position-header {
      background-color: #fff;
    }

    .tips {
      font-size: 20rpx;
      font-family: PingFang SC-常规体, PingFang SC;
      font-weight: 400;
      color: #999999;
    }

    .examine-content {
      padding-top: calc($uni-default-bar-height + var(--status-bar-height));
      padding-bottom: 26rpx;
    }
  }
</style>