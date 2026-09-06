<template>
  <view>
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :default-title="data.defaultTitle" :is-right="true" :right-data="rightIcon"
        @handleNarItem="handleNarItem">
      </default-nav-bar>
    </view>

    <view class="content">
      <forum-list :list-data="data.listData" :hot-data="data.hotData" empty-title="暂无文章~"></forum-list>
    </view>
    <loginPop ref="loginRef" @loginOk='loginOk'></loginPop>
  </view>
</template>

<script setup lang="ts">
  import loginPop from './components/loginPop'
  import defaultNavBar from '@/components/defaultNavBar/index'
  import forumList from './components/forumList'
  import message from '@/utils/message'
  import { articleListApi } from '@/api/forum'
  import type { Res } from '@/utils/typeHelper'
  import { clickNavigateTo } from '@/utils/helper'
  import { useStore } from 'vuex'

  const store = useStore()
  const loginRef = ref(null)
  const rightIcon = reactive([
    { type: 1, icon: 'icon-sousuo', types: 'icon' }
  ])

  const data = reactive({
    barList: [],
    listData: [],
    where: {
      limit: 10,
      name: '',
      page: 1,
      sort: 'recom'
    },
    newIsShowMax: 3,
    hotData: [],
    defaultTitle: '',
    type: '',
    moreButton: false
  })
  onMounted(() => {
    if (!store.state.app.forumToken) {
      loginRef.value.inputDialogToggle()
    } else {
      getArticleList()
    }
  })

  onLoad((e) => {
    if (e.type) {
      data.where.sort = e.type
      data.type = e.type
      data.defaultTitle = e.type === 'history' ? '阅读记录' : '我的收藏'
    }

  })

  //获取文章列表
  const listLoading : Ref<boolean> = ref(false)
  const loginOk = () => {
    getArticleList()
  }
  const getArticleList = (tab : boolean = false) => {
    articleListApi(data.where).then((res : Res) => {
      data.hotData = res.data.hot
      // 切换时数据清空
      if (tab) data.listData = []
      data.listData.push(...res.data.list)
      const allPage : number = Math.ceil(res.data.count / data.where.limit)
      listLoading.value = !(data.listData.length <= 0 || data.where.page >= allPage)
      uni.stopPullDownRefresh() //停止刷新
    }).catch((error : Res) => {
      message.error(error.message)
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

  // 点击头部切换
  const handleNarItem = () : void => {
    clickNavigateTo(`/pages/forum/search?type=${data.type}`)
  }
</script>

<style scoped lang="scss">
  .cr-position-header {
    background-color: #fff;
  }

  .forum-line-style {
    border-top: 1px solid $uni-line-style-color-three;
  }

  .content {
    width: 100%;
    padding-top: calc($uni-default-bar-height + var(--status-bar-height));
  }
</style>