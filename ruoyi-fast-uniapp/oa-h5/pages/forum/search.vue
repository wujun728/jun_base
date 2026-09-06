<template>
  <view>
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <view class="default-search">
        <uni-search-bar @confirm="search" @focus="search" :focus="true" bgColor="#F0F1F5" v-model="data.where.name"
          @clear="clearSearch" @cancel="cancelSearch">
        </uni-search-bar>
      </view>
      <view class="forum-line-style" v-if="data.typeData.includes(data.where.sort)">
        <navigation-bar ref="navigationBarRef" :props-data="{name: 'title' }" :isActiveLine="false" :isSidebar="true"
          :bar-data="data.barList" @handleData="handleData">
        </navigation-bar>
      </view>
    </view>

    <view class="content">
      <forum-list :list-data="data.listData" :hot-data="data.hotData" empty-title="暂无搜索结果~"></forum-list>
    </view>
  </view>
</template>

<script setup lang="ts">
  import navigationBar from '@/components/navigationBar/index'
  import forumList from './components/forumList'
  import message from '@/utils/message'
  import { articleLabelApi, articleListApi } from '@/api/forum'
  import { onLoad } from '@dcloudio/uni-app'
  import { useBarHeight } from '@/utils/useVerifyCode'
  import type { Res, Detail } from '@/utils/typeHelper'
  const { height, getBarHeight } = useBarHeight()
  const instance = getCurrentInstance() // 获取组件实例

  onMounted(() => {
    getBarHeight('.cr-position-header', instance)
    getArticleLabel()
  })

  const data = reactive({
    barList: [],
    listData: [],
    where: {
      label_id: <string | number>'',
      limit: 10,
      name: '',
      page: 1,
      sort: 'recom'
    },
    newIsShowMax: 3,
    hotData: [],
    typeData: ['recom', 'new', 'hot']
  })

  onLoad((e) => {
    if (e.type) {
      data.where.sort = e.type
    }
  })

  const clearSearch = () : void => {
    data.where.name = ''
    data.listData = []
  }

  const cancelSearch = () : void => {
    uni.navigateBack()
  }

  const search = () => {
    if (data.where.name) {
      getArticleList(true)
    } else {
      clearSearch()
    }
  }

  // 获取文章分类
  const getArticleLabel = () : void => {
    articleLabelApi(1).then((res : Res) => {
      data.barList = res.data.list || []
      if (data.barList.length > 0) {
        data.barList.unshift({ id: '', title: '全部' })
      }
    }).catch((error : Res) => {
      message.error(error.message)
    })
  }

  //获取文章列表
  const listLoading : Ref<boolean> = ref(false)
  const getArticleList = (tab : boolean = false) : void => {
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

  const handleData = (e : Detail) : void => {
    if (e) {
      data.where.label_id = e.id
      data.where.page = 1
      if (data.where.name) {
        getArticleList(true)
      }
    } else {
      getArticleLabel()
    }
  }
</script>

<style scoped lang="scss">
  .cr-position-header {
    background-color: #fff;

    ::v-deep .uni-searchbar {
      padding-bottom: 26rpx;

    }
  }

  .forum-line-style {
    border-top: 1px solid $uni-line-style-color-three;
  }

  .content {
    width: 100%;
    padding-top: v-bind(height);
  }
</style>