<template>
  <view class="com-content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <uni-nav-bar :border="true">
        <view class="nar-bar-title">通讯录</view>
        <template v-slot:left>
          <view class="iconfont icon-fanhui bar-return" @click="cancel"></view>
        </template>
        <template v-slot:right>
          <view v-if="data.isAdd" class="iconfont bar-return icon-shezhi" @click="addUsers"></view>
          <!-- <view class="iconfont bar-return icon-sousuo"></view> -->
        </template>
      </uni-nav-bar>
    </view>
    <view class="content">
      <list :tree-data="tree" :show-select="false" @handleDep="handleDep"></list>
    </view>
    <add-members ref="addMembersRef"></add-members>
    <global-index />
  </view>
</template>
<script lang="ts" setup>
  import list from '@/pages/users/organization/components/list'
  import globalIndex from '@/components/globalIndex/index.vue'
  import addMembers from './components/addMembers'
  import { useBarHeight } from '@/utils/useVerifyCode'
  import { useStore } from 'vuex'
  import { clickSwitchTab } from '@/utils/helper'
  import type { Detail } from '@/utils/typeHelper'
  import { navigateToDepartment } from '@/utils/autoload'

  const { height, getBarHeight } = useBarHeight()
  const instance = getCurrentInstance() // 获取组件实例
  onMounted(() => {
    getBarHeight('.cr-position-header', instance)
  })
  const data = reactive({
    tree: [],
    isAdd: false,
  })

  const store = useStore()
  const isLogin = computed(() => store.state.app.isLogin)
  const tree = computed(() => store.state.app.frameTree)

  onShow(() => {
    if (!isLogin.value) {
      toLogin()
    }
  })

  const cancel = () : void => {
    let pages : any = getCurrentPages()
    let url = '/pages/workbench/index'
    if (pages.length > 1) {
      url = pages[0].$page.path
    }
    clickSwitchTab(url)
  }

  const addMembersRef = ref(null)
  // 添加成员
  const addUsers = () : void => {
    addMembersRef.value.popupOpen()
  }

  const handleDep = (data : Detail) : void => {
    if (data.isUser === 2) {
      const query = `isShow=false&id=${data.id}`
      navigateToDepartment(query)
    }
  }
</script>
<style>
  page {
    background-color: #fff;
  }
</style>
<style lang="scss" scoped>
  .cr-position-header {
    background-color: #fff;
    padding-bottom: 30rpx;

    .nar-bar-title {
      font-size: 16px;
      font-weight: 500;
    }

    .bar-return {
      font-size: 34rpx;
      font-weight: 400;
    }

    ::v-deep .uni-navbar--border {
      border-bottom-color: #ebeef5 !important;
    }

    ::v-deep .uni-navbar__header-btns-right {
      justify-content: v-bind('data.isAdd ? "space-between" : "flex-end"');
    }

    ::v-deep .uni-navbar__header-container {
      justify-content: center;
      align-items: center;
    }
  }

  .content {
    margin-top: v-bind(height);
    height: calc(100vh - 2rpx - v-bind(height));

    ::v-deep .scroll-per {
      height: 100% !important;
    }
  }
</style>