<template>
  <view class="content">
    <view class="cr-position-header">
      <default-nav-bar :is-right="true" :default-title="data.title" :right-data="[]" right-text="提交"
        @handleClickRight="clickSubmit"></default-nav-bar>
      <!-- 表单内容 -->
    </view>
    <view class="examine-content">
      <moduleForm ref="oaFormRef" :listData="data.listData" :info="data.info" :formInfo="data.formInfo"
        :addressData="data.addressData" :keyName="data.keyName" @submitOk="submitOk"></moduleForm>
    </view>
  </view>
</template>
<script setup>
  import moduleForm from '@/components/moduleForm'
  import message from '@/utils/message'
  import { useStore } from 'vuex'
  const store = useStore()
  import defaultNavBar from '@/components/defaultNavBar/index'
  import {
    crudModuleCreateApi,
    crudModuleSaveDataApi,
    crudModuleFindApi,
    crudModuleUpdateApi,
    getDictTreeListApi
  } from '@/api/crud'
  import { reactive } from 'vue'
  const oaFormRef = ref(null)
  const data = reactive({
    title: '新增',
    backgroundColor: 'rgba(0,0,0,0)',
    listData: [],
    formInfo: {},
    keyName: '',
    name: '',
    info: {},
    addressData: [],
    id: 0,
    detailsId: 0,
    crud_id: 0,
    tableName: '',
    crud_value: 0,
  })
  onLoad((options) => {
    if (options.key) {
      data.keyName = options.key
    }
    if (options.id) {
      data.id = options.id
      data.detailsId = options.id
      data.tableName = options.key
      data.title = '编辑'
      getInfo()
    }
    if (options.name) {
      data.name = options.name
    }

    if (options.route) {
      const item = JSON.parse(decodeURIComponent(options.route))
      data.crud_id = item.crud_id
      data.crud_value = item.crud_value
      data.name = item.name
      data.keyName = item.key
      data.tableName = item.keyName
      data.detailsId = item.id
      let obj = {
        name: item.name,
        id: item.id,
      }
      store.commit('setoneOnOneData', obj)
    }
    createdForm()
  })

  const getInfo = () => {
    crudModuleFindApi(data.keyName, data.id).then((res) => {
      data.info = res.data.module_info
    })
  }

  const createdForm = () => {
    let obj = {}
    obj.id = data.id
    crudModuleCreateApi(data.keyName, obj).then((res) => {
      data.listData = res.data.column
      data.listData.map((item) => {
        if (item.form_value == 'cascader_address') {
          getDict(item.options.is_city_show)
        }
        if (item.form_value == 'input_select' && data.crud_id) {
          data.info[item.field_name_en] = {
            name: data.name,
            id: data.detailsId,
          }
        }
      })
      data.formInfo = res.data.form_info
    })
  }
  const getDict = (type) => {
    let obj = {
      type_id: 2,
      isCityShow: type,
    }
    getDictTreeListApi(obj).then((res) => {
      data.addressData = res.data
      // uni.hideLoading()
    })
  }
  import { clickNavigateTo, showModal } from '@/utils/helper'
  // 保存
  const clickSubmit = () => {
    oaFormRef.value.handleConfirm()
  }
  const submitOk = (e) => {
    if (data.id == 0) {
      e.crud_id = data.crud_id
      e.crud_value = data.crud_value
      crudModuleSaveDataApi(data.keyName, e)
        .then((res) => {
          message.success(res.message)
          if (data.crud_id || data.name) {
            clickNavigateTo(`/pages/module/details?id=${data.detailsId}&key=${data.tableName}&&name=${data.name}`)
          } else {
            clickNavigateTo(`/pages/module/list?tablename=${data.keyName}`)
          }
        })
        .catch((err) => {
          message.error(err.message)
        })
    } else {
      e.crud_id = data.crud_id
      e.crud_value = data.crud_value
      crudModuleUpdateApi(data.keyName, data.id, e).then((res) => {
        message.success(res.message)
        if (data.crud_id || data.name) {
          clickNavigateTo(`/pages/module/details?id=${data.detailsId}&key=${data.tableName}&&name=${data.name}`)
        } else {
          clickNavigateTo(`/pages/module/list?tablename=${data.keyName}`)
        }
      })
    }
  }
</script>
<style lang="scss" scoped>
  .content {
    .cr-position-header {
      position: fixed;
      padding-top: var(--status-bar-height);
      height: calc($uni-default-bar-height + var(--status-bar-height));
      background: linear-gradient(90deg, #459fff 0%, #388aef 100%, #3384e7 100%);
    }

    .examine-content {
      padding-top: calc($uni-default-bar-height + var(--status-bar-height));
      padding-bottom: 50rpx;
    }
  }
</style>