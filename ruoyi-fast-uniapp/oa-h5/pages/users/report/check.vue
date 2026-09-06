<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar
        jump-url="/pages/workbench/index"
        :index="1"
        :default-type="1"
        :tab-data="tabData"
        :is-right="true"
        :right-data="rightIcon"
        @handleNarItem="handleNarItem"
      ></default-nav-bar>
      <view class="bar-search">
        <form-box :type="config.selectType" @change="changeFormBox"></form-box>
      </view>
    </view>

    <view class="report-content m10">
      <report-list :list-data="config.listData" :empty-title="emptyTitle"></report-list>
    </view>

    <view class="uni-p-b-98"></view>
    <bottom-navigation :type="1" page-path="/pages/users/report/check"></bottom-navigation>
  </view>
</template>

<script setup lang="ts">
import defaultNavBar from '@/components/defaultNavBar/index'
import bottomNavigation from '@/components/bottomNavigation/index'
import reportList from './components/reportList.vue'
import formBox from './components/formBox.vue'
import { ref, reactive, onMounted, type Ref } from 'vue'
import message from '@/utils/message'
import { enterpriseDailyApi } from '@/api/user'
import type { Res, Box } from '@/utils/typeHelper'
import { clickNavigateTo, backToTop } from '@/utils/helper'
import { onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'

const emptyTitle: Ref<string> = ref('暂无提交的汇报～')
const tabData = reactive([
  { name: '我提交的', type: 'mine', types: 'tab' },
  { name: '我收到的', type: 'receive', types: 'tab' },
])

const rightIcon = reactive([{ type: 1, icon: 'icon-sousuo', types: 'icon' }])

const config = reactive({
  selectType: 'mine',
  where: {
    finish: '',
    type: 0,
    types: <string | number>'',
    time: '',
    user_id: <string | number>'',
    page: 1,
    limit: 10,
    scope_frame: 'self',
  },
  listData: [],
})
const listLoading: Ref<boolean> = ref(false) //是否正在加载

onMounted(() => {
  getEnterpriseDaily()
})

// 获取
const getEnterpriseDaily = (tab: boolean = false): void => {
  config.where.type = config.selectType === 'mine' ? 0 : 1
  enterpriseDailyApi(config.where)
    .then((res: Res) => {
      // 切换时数据清空
      if (tab) config.listData = []
      config.listData.push(...res.data.list)
      const allPage: number = Math.ceil(res.data.count / config.where.limit)
      if (config.listData.length <= 0 || config.where.page >= allPage) {
        listLoading.value = false
      } else {
        listLoading.value = true
      }
      uni.stopPullDownRefresh() //停止刷新
    })
    .catch((error: Res) => {
      uni.hideLoading()
      message.error(error.message)
    })
}
// 条件搜索
const changeFormBox = (e: Box) => {
  if (e) {
    config.where.types = e.status || ''
    config.where.time = e.time || ''
    config.where.user_id = e.approveId || ''
    config.where.page = 1
    getEnterpriseDaily(true)
  }
}

interface Tab {
  types: string
  type: string
  name: string
}
const handleNarItem = (data: Tab) => {
  if (data.types === 'tab') {
    config.selectType = data.type

    if (data.type == 'mine') {
      config.where.scope_frame = 'self'
    } else if (data.type == 'receive') {
      config.where.scope_frame = 'all'
    }
    backToTop()
  } else {
    clickNavigateTo(`/pages/users/report/search?type=${config.selectType}`)
  }
}

// 下拉加载
onReachBottom(() => {
  if (listLoading.value) {
    config.where.page++
    getEnterpriseDaily()
  }
})
//上拉加载
onPullDownRefresh(() => {
  config.where.page = 1
  listLoading.value = false
  config.listData = []
  getEnterpriseDaily()
})
</script>

<style scoped lang="scss">
::v-deep .selected-list {
  display: none;
}

.content {
  .cr-position-header {
    position: sticky;
  }

  width: 100%;
  position: relative;

  .bar-search {
    border-top: 1px solid #ebeef5;
    width: 100%;
    background-color: #fff;

    ::v-deep .uni-calendar--fixed {
      z-index: 101;
    }
  }

  .report-content {
    margin-top: 0;
  }
}
</style>
