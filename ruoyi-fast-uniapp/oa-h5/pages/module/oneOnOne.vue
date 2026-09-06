<template>
  <view class="content">
    <view class="cr-position-header">
      <default-nav-bar :defaultTitle="`一对一引用`" :backgroundColor="data.backgroundColor" :color="`#fff`"></default-nav-bar>
    </view>
    <view class="examine-content">
      <view class="search">
        <uni-easyinput
          prefixIcon="search"
          v-model="data.where.keyword"
          placeholder="关键字搜索"
          @confirm="getTableList(1)"
          @focus="getTableList(1)"
          @clear="clearSearch"
        ></uni-easyinput>
      </view>

      <item :keyName="data.keyName" :info="data.header" :table-data="data.tableData" :type="`oneOnOne`"></item>
    </view>
  </view>
</template>

<script setup>
import { dataModulerListApi, dataModulerFieldApi } from '@/api/crud'
import defaultNavBar from '@/components/defaultNavBar/index.vue'
import item from './components/item.vue'

const data = reactive({
  backgroundColor: 'rgba(0,0,0,0)',
  tableData: [],
  id: 0,
  jumpUrl: '',

  where: {
    limit: 10,
    page: 1,
    keyword: '',
  },
  keyName: '',
  header: [],
})

onLoad((options) => {
  data.id = options.id
  data.keyName = options.keyName
  data.jumpUrl = `/pages/module/addForm?key=${data.keyName}`
  getHeader()
  getTableList()
})

const getHeader = () => {
  dataModulerFieldApi(data.id).then((res) => {
    data.header = res.data
  })
}

import { clickNavigateTo } from '@/utils/helper'
const clearSearch = () => {
  data.where.keyword = ''
  data.where.page = 1
  getTableList()
}
// 获取实体列表
const getTableList = (val) => {
  if (val) {
    data.where.page = val
  }
  dataModulerListApi(data.id, data.where).then((res) => {
    if (data.where.page == 1) {
      data.tableData = []
    }
    data.tableData.push(...res.data.list)
    const allPage = Math.ceil(res.data.count / data.where.limit)
    if (data.tableData.length <= 0 || data.where.page >= allPage) {
      listLoading.value = false
    } else {
      listLoading.value = true
    }
    uni.stopPullDownRefresh() //停止刷新
  })
}

import { onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'
const listLoading = ref(false)
// 下拉加载
onReachBottom(() => {
  if (listLoading.value) {
    data.where.page++
    getTableList()
  }
})
//上拉加载
onPullDownRefresh(() => {
  data.where.page = 1

  getTableList()
})
</script>

<style scoped lang="scss">
.cr-position-header {
  position: fixed;
  padding-top: var(--status-bar-height);
  height: calc($uni-default-bar-height + var(--status-bar-height));
  background: linear-gradient(90deg, #459fff 0%, #388aef 100%, #3384e7 100%);
}

.examine-content {
  padding-top: calc($uni-default-bar-height + var(--status-bar-height));
  padding-bottom: 126rpx;

  .search {
    width: 100%;
    height: 88rpx;
    background-color: #ffffff;
    padding: 18rpx 30rpx 0 30rpx;
  }
  ::v-deep .is-input-border {
    border: none;
  }
  ::v-deep .uni-easyinput__content {
    background-color: #f5f5f5 !important;
  }

  ::v-deep .uni-easyinput__content-input {
    height: 32px !important;
    font-size: 13px !important;
    .uni-easyinput__placeholder-class {
      font-size: 13px !important;
    }
  }
  ::v-deep .content-clear-icon {
    font-size: 16px !important;
  }

  .view-search {
    width: 100%;
    height: 72rpx;
    padding: 0 30rpx;
    background-color: #ffffff;
  }
}

::v-deep .uni-easyinput__content {
  background-color: #f5f5f5 !important;
}
</style>
