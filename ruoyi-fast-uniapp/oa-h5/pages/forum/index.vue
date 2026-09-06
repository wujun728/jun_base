<template>
  <view>
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-left="false" :default-type="1" :tab-data="tabData" :is-right="true" :right-data="rightIcon"
        @handleNarItem="handleNarItem"></default-nav-bar>
      <view class="forum-line-style">
        <navigation-bar ref="navigationBarRef" :isActiveLine="false" :isFirstlogin="isFirstlogin"
          :props-data="{name: 'title' }" :isSidebar="true" :bar-data="data.barList" @handleData="handleData">
        </navigation-bar>
      </view>
    </view>
    <view class="content ">
      <forum-list :list-data="data.listData" :hot-data="data.hotData" empty-title="暂无文章~"></forum-list>
    </view>
    <drop-down ref="dropDownRef" :list-data="forumMeus" @btn-click="dropDownItem"></drop-down>
    <global-index />
  </view>
</template>
<script setup lang="ts">
  import globalIndex from '@/components/globalIndex/index.vue'
  import navigationBar from '@/components/navigationBar/index'
  import defaultNavBar from '@/components/defaultNavBar/index'
  import forumList from './components/forumList'
  import dropDown from './components/dropDown'
  import message from '@/utils/message'
  import { getuserLabel, articleListApi } from '@/api/forum'
  import { clickNavigateTo } from '@/utils/helper'
  import type { Res, Detail } from '@/utils/typeHelper'
  import { useStore } from 'vuex'

  const store = useStore()
  const loginRef = ref(null)
  const tabData = reactive([
    { name: '推荐', type: 'recom', types: 'tab' },
    { name: '最新', type: 'new', types: 'tab' },
    { name: '热榜', type: 'hot', types: 'tab' }
  ])
  let formData = reactive({
    phone: '',
    verificationCode: '',
  })
  const userInfo = computed(() => store.state.app.userInfo)

  interface RightIcon {
    type : string | number
    name : string
    types : string
  }

  const rightIcon = reactive([
    { type: 1, icon: 'icon-sousuo', types: 'icon' },
    { type: 2, icon: 'icon-gengduo1', types: 'icon' }
  ])

  onShow(() => {
    formData.phone = userInfo.value.phone
  })
  const isFirstlogin = ref(false)
  onMounted(() => {
    getArticleLabel()
    getArticleList()
  })

  const forumMeus = reactive([
    { name: '阅读记录', id: 'history', icon: 'icon-shequ-yuedujilu' },
    { name: '我的收藏', id: 'collect', icon: 'icon-shequ-shoucang' }
  ])
  const data = reactive({
    barList: <any>[],
    listData: <any>[],
    where: {
      label_id: <string | number>'',
      limit: 15,
      name: '',
      page: 1,
      sort: <string | number>'recom'
    },
    newIsShowMax: 3,
    hotData: [],
    moreButton: false
  })

  // 获取文章分类
  const getArticleLabel = () => {
    getuserLabel({ types: 1 }).then((res : Res) => {
      data.barList = res.data.list || []
      if (data.barList.length > 0) {
        data.barList.unshift({ id: '', title: '全部' })
      }
    }).catch((error : Res) => {
      message.error(error.message)
    })
  }

  const inputDialog = ref(null)
  let isSendCode = ref(true)

  // 登录对话框
  const inputDialogToggle = () => {
    inputDialog.value.open()
  }


  //获取文章列表
  const listLoading : Ref<boolean> = ref(false)
  const getArticleList = (tab : boolean = false) => {
    articleListApi(data.where).then((res : Res) => {
      data.hotData = res.data.hot
      // 切换时数据清空
      if (tab) data.listData = []
      data.listData.push(...res.data.list)
      const allPage : number = Math.ceil(res.data.count / data.where.limit)
      if (data.listData.length <= 0 || data.where.page >= allPage) {
        listLoading.value = false
      } else {
        listLoading.value = true
      }
      uni.stopPullDownRefresh() //停止刷新
    })
  }

  // 下拉加载
  onReachBottom(() => {
    if (listLoading.value) {
      data.where.page++
      getArticleList()
    }
  })
  //上拉加载
  onPullDownRefresh(() => {
    data.where.page = 1
    listLoading.value = false
    data.listData = []
    getArticleList()
  })

  const handleData = (e : Detail) => {
    if (e) {
      data.where.label_id = e.id
      data.where.page = 1
      getArticleList(true)
    } else {
      getArticleLabel()
    }
  }

  const dropDownRef = ref(null)
  // 点击头部切换
  const handleNarItem = (e : RightIcon) => {
    if (e.types === 'tab') {
      data.where.sort = e.type
      data.where.page = 1
      getArticleList(true)
    } else {
      if (e.type === 1) {
        clickNavigateTo(`/pages/forum/search?type=${data.where.sort}`)
      } else {
        dropDownRef.value.openDropdown()
      }
    }
  }

  const dropDownItem = (e : Detail) : void => {
    clickNavigateTo(`/pages/forum/history?type=${e.id}`)
  }
</script>
<style scoped lang="scss">
  .content {
    //#ifdef APP-PLUS
    margin-top: 234rpx;
    //#endif
    //#ifdef H5
    margin-top: 164rpx;
    //#endif
  }
</style>