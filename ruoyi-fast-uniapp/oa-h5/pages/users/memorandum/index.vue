<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right="true" :is-left="!!config.folderId" :jump-url="config.jumpUrl" :is-jump-bar="false" :tab-index="config.tabIndex"
        :default-title="config.defaultTitle" :default-type="config.defaultType" :tab-data="tabData"
        :right-data="config.rightIcon" @handleNarItem="handleNarItem">
      </default-nav-bar>
    </view>

    <view class="report-content m10">
      <newes-list v-if="config.type === 'newes'" :list-data="config.newesData"
        empty-title="暂无笔记文件，点击右上方按钮可新建～"></newes-list>
      <folder-list v-if="config.type === 'folder'" :parent-id="config.folderId" :list-data="config.folderData"
        @btn-click="folderChange" empty-title="暂无笔记文件，点击右上方按钮可新建～">
      </folder-list>
    </view>

    <drag-button :isDock="true" :existTabBar="true" @btn-click="btnClick">
      <text class="iconfont icon-bianji2"></text>
    </drag-button>

    <news-folder ref="newsFolderRef" title="新建文件夹" :edit-data="config.editData" @handleOk="change"></news-folder>
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index'
  import dragButton from "@/components/dragButton/index.vue";
  import newesList from './components/newesList.vue'
  import folderList from './components/folderList.vue'
  import newsFolder from './components/newsFolder.vue'
  import { ref, reactive, onMounted } from "vue"
  import message from '@/utils/message'

  const tabData = reactive([
    { name: '最新', type: 'newes', types: 'tab' },
    { name: '文件夹', type: 'folder', types: 'tab' },
  ])

  const config = reactive({
    type: 'newes',
    tabIndex: 0,
    editData: {
      placeholder: '请输入文件夹名称',
      type: 0
    },
    rightIcon: [
      { type: 1, icon: 'icon-sousuo', types: 'icon' }
    ],
    newesWhere: {
      pid: '',
      name: '',
      page: 1,
      limit: 10
    },
    newesData: [],
    folderWhere: {
      pid: '',
      name: '',
      page: 1,
      limit: 10
    },
    folderData: [],
    isLoadingFolder: false,
    listWhere: {
      pid: '',
      name: '',
      page: 1,
      limit: 10
    },
    folderId: '',
    defaultType: 1,
    defaultTitle: '记事本',
    jumpUrl: '/pages/workbench/index'
  })
  const newsFolderRef = ref(null)

  import { clickNavigateTo, delayedNavigateTo, backToTop, delayedReLaunch } from '@/utils/helper'
  const handleNarItem = (e) => {
    if (e.types === 'tab') {
      if (config.type === e.type) return
      config.type = e.type
      backToTop()
      if (e.type === 'newes') {
        config.rightIcon.splice(1, 1)
        getMemorialGrou(true)
      } else {
        if (config.rightIcon.length < 2) {
          config.rightIcon.push({ type: 2, icon: 'icon-jishiben-xinjianwenjianjia', types: 'icon' })
        }
        // 获取文件夹中文件夹
        config.folderWhere.page = 1
        config.listWhere.page = 1
        config.listWhere.pid = 0
        getMemorialCateList(true)
      }
    } else {
      if (e.type === 1) {
        // 条件搜索
        clickNavigateTo(`/pages/users/memorandum/search?tab=${config.type}&id=${config.folderId}`)
      } else {
        newsFolderRef.value.popupOpen()
      }
    }
  }

  const listLoading = ref(false) //是否正在加载

  import { onLoad } from '@dcloudio/uni-app'

  onLoad((e) => {
    if (e.tab) {
      config.type = e.tab
      if (e.id) {
        config.defaultType = 0
        // config.rightIcon.push( { type: 2, icon: 'icon-jishiben-xinjianwenjianjia', types: 'icon' } )
      }
      config.defaultTitle = ''
      config.folderId = e.id
      config.folderWhere.pid = e.id
      if (e.id) {
        config.listWhere.pid = e.id
      } else {
        config.listWhere.pid = 0
      }

      if (config.type === 'newes') { // 最新
        config.tabIndex = 0
        getMemorialGrou()
      } else { // 文件夹
        config.tabIndex = 1
        getMemorialCateList()
      }
    } else {
      getMemorialGrou()
    }
  })

  import {
    userMemorialGroupApi,
    userMemorialCateListApi,
    userMemorialListApi,
    userMemorialCateSaveApi
  } from '@/api/user'
  // 获取最近列表
  const getMemorialGrou = (tab = false) => {
    userMemorialGroupApi(config.newesWhere).then(res => {
      // 切换时数据清空
      if (tab) config.newesData = []
      config.newesData.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / config.newesWhere.limit)
      if (config.newesData.length <= 0 || config.newesWhere.page >= allPage) {
        listLoading.value = false
      } else {
        listLoading.value = true
      }
      uni.stopPullDownRefresh() //停止刷新
    }).catch((error) => {
      uni.hideLoading()
      message.error(error.message)
    })
  }

  // 获取文件夹中文件夹
  const getMemorialCateList = (tab = false) => {
    userMemorialCateListApi(config.folderWhere).then(res => {
      // 切换时数据清空
      if (tab) config.folderData = []
      if (res.data.list.length > 0) {
        res.data.list.map((value) => {
          value.folder_type = 1
        })
      }
      const parent = res.data.parent
      // 判断父级名称
      if (parent) {
        config.defaultTitle = parent.name
        if (parent.parent) {
          config.jumpUrl = `/pages/users/memorandum/index?tab=${config.type}&id=${parent.parent.id}`
        } else {
          config.jumpUrl = `/pages/users/memorandum/index?tab=${config.type}&id=`
        }
        if (parent.path.length < 2 && config.rightIcon.length < 2) {
          config.rightIcon.push({ type: 2, icon: 'icon-jishiben-xinjianwenjianjia', types: 'icon' })
        }
      } else {
        config.jumpUrl = '/pages/workbench/index'
        if (config.rightIcon.length < 2) {
          config.rightIcon.push({ type: 2, icon: 'icon-jishiben-xinjianwenjianjia', types: 'icon' })
        }
      }
      config.folderData.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / config.folderWhere.limit)
      if (config.folderData.length <= 0 || config.folderWhere.page >= allPage) {
        listLoading.value = false
        config.isLoadingFolder = true
        getMemorialList()
      } else {
        listLoading.value = true
      }
      uni.stopPullDownRefresh() //停止刷新
    }).catch((error) => {
      uni.hideLoading()
      message.error(error.message)
    })
  }

  // 获取记事本内容列表
  const getMemorialList = () => {
    userMemorialListApi(config.listWhere).then(res => {
      // 切换时数据清空
      config.folderData.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / config.listWhere.limit)
      if (config.folderData.length <= 0 || config.listWhere.page >= allPage) {
        listLoading.value = false
      } else {
        listLoading.value = true
      }
      uni.stopPullDownRefresh() //停止刷新
    }).catch((error) => {
      uni.hideLoading()
      message.error(error.message)
    })
  }

  // 保存文件夹
  const saveMemorialCate = (data) => {
    userMemorialCateSaveApi(data).then(res => {
      message.success(res.message)
      delayedNavigateTo(`/pages/users/memorandum/index?tab=folder&id=${config.folderId}`)
    }).catch((error) => {
      message.error(error.message)
    })
  }

  import { onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'
  // 下拉加载
  onReachBottom(() => {
    if (config.type === 'newes') {

    } else {
      if (!config.isLoadingFolder) {
        // 加载文件夹
        if (listLoading.value) {
          config.folderWhere.page++
          getMemorialCateList()
        }
      } else {
        // 加载记事本
        if (listLoading.value) {
          config.listWhere.page++
          getMemorialList()
        }
      }
    }
  })

  // 上拉刷新
  onPullDownRefresh(() => {
    if (config.type === 'newes') {
      config.newesWhere.page = 1
      listLoading.value = false
      config.newesData = []
      getMemorialGrou()
    } else {
      config.folderWhere.page = 1
      listLoading.value = false
      config.folderData = []
      getMemorialCateList()
    }
  })

  //新建文件夹(根目录新建)
  const change = (e) => {
    if (e.value) {
      const data = {
        pid: config.folderId,
        name: e.value
      }
      saveMemorialCate(data)
    }
  }
  //新建记事本
  const btnClick = () => {

    clickNavigateTo(`/pages/users/memorandum/create?tab=${config.type}&pid=${config.folderId}`)
  }
  // 文件夹事件监听
  const folderChange = (e) => {
    if (e.type === 1) {
      config.folderId = e.id
      delayedReLaunch(`/pages/users/memorandum/index?tab=folder&id=${config.folderId}`)
    } else if (e.type === 2) {
      config.folderId = e.id
      const res = {
        pid: e.id,
        name: e.name
      }
      saveMemorialCate(res)
    }
  }
</script>

<style scoped lang="scss">
  ::v-deep .selected-list {
    display: none;
  }

  .content {
    width: 100%;
    position: relative;

    .cr-position-header {
      position: sticky;
    }

    .report-content {
      margin-top: 0;
    }
  }
</style>