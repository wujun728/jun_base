<template>
  <view class="content">
    <view class="cr-position-header">
      <!-- #ifdef APP-PLUS -->
      <view class="status_bar"></view>
      <default-nav-bar jump-url="/pages/workbench/index"></default-nav-bar>
      <!-- #endif -->
      <view class="bar-search">
        <form-census :type="config.where.types" @change="changeFormBox"></form-census>
      </view>
    </view>

    <view class="report-content m10">
      <uni-row class="display-align">
        <uni-col :span="12" class="left-title">工作汇报</uni-col>
        <uni-col :span="12" class="right-title text-right">
          <button class="assess-button" v-if="config.todayShow">
            <text class="iconfont icon-huibaotongji-tongjizhong"></text>
            <text>统计中</text>
          </button>
        </uni-col>
      </uni-row>
      <view class="report-info">{{ config.tipsText }}</view>
      <view class="report-list">
        <view class="report-list-item">
          <view class="num">{{ config.statistics.total }}</view>
          <view class="text">提交结果</view>
        </view>
        <view class="report-list-item" @click="censusItem(0)">
          <view class="num default-color">{{ config.statistics.submit }}</view>
          <view class="text">已提交</view>
        </view>
        <view class="report-list-item" @click="censusItem(1)">
          <view class="num default-error">{{ config.statistics.no_submit }}</view>
          <view class="text">未提交</view>
        </view>
      </view>
    </view>

    <view class="census-content m10">
      <view class="census-header">
        <view
          class="census-header-item"
          v-for="(item, index) in config.tabData"
          :key="'tab' + index"
          :class="index === config.index ? 'active' : ''"
          @click="censusItem(index)"
        >
          {{ item ? item.name : '' }}
        </view>
      </view>
      <uni-list :border="false" v-if="config.listData.length > 0">
        <uni-list-item v-for="(items, indexs) in config.listData" :key="'list' + indexs">
          <template v-slot:header>
            <avatar :src="items.avatar" :radius="8" :auto-size="false" :width="70" :height="70"></avatar>
          </template>
          <template v-slot:body>
            <view class="item-list">
              <uni-row class="item-list-content" @click.native="censusItemList(items)">
                <uni-col :span="10" class="left">
                  <text class="name">{{ items ? items.name : '' }}</text>
                </uni-col>
                <template v-if="items.daily_id">
                  <uni-col :span="14" class="right text-right">
                    <uni-dateformat format="MM/dd hh:mm" :date="items.created_at"></uni-dateformat>
                    <text>提交</text>
                    <view class="iconfont icon-fanhui"></view>
                  </uni-col>
                </template>
              </uni-row>
            </view>
          </template>
        </uni-list-item>
      </uni-list>
      <empty v-else :index="2" :title="emptyTitle"></empty>
    </view>

    <view class="uni-p-b-98"></view>
    <bottom-navigation :type="1" page-path="/pages/users/report/census"></bottom-navigation>
  </view>
</template>

<script setup lang="ts">
import defaultNavBar from '@/components/defaultNavBar/index'
import bottomNavigation from '@/components/bottomNavigation/index'
import avatar from '@/components/avatar/index.vue'
import formCensus from './components/formCensus.vue'
import empty from '@/components/empty/index'
import { ref, type Ref, reactive, onMounted, getCurrentInstance, type ComponentInternalInstance } from 'vue'
import message from '@/utils/message'
import { onLoad } from '@dcloudio/uni-app'
import type { Res, GetType, Box } from '@/utils/typeHelper'
import { entDailyStatisticsApi, entDailySubmitListApi, entDailyNoSubmitListApi } from '@/api/user'
import { clickNavigateTo } from '@/utils/helper'
import { onReachBottom } from '@dcloudio/uni-app'
import moment from 'moment'
moment.suppressDeprecationWarnings = true

const emptyTitle: Ref<string> = ref('暂无提交的汇报～')

const config = reactive({
  index: 0,
  tabData: [
    { name: '已提交', id: 1 },
    { name: '未提交', id: 2 },
  ],
  tipsText: '今日 00:00 ~ 23:59 统计',
  selectType: 'census',
  where: {
    types: 0,
    time: `${moment(new Date()).format('YYYY/MM/DD')}-${moment(new Date()).format('YYYY/MM/DD')}`,
    frame_id: '',
    page: 1,
    limit: 10,
  },
  statistics: <any>{},
  listData: [],
  firstLoading: false,
  todayShow: true,
})

onMounted(() => {
  getStatistics()
  getBarHeight()
})

onLoad((options: GetType) => {
  if (options.tab) {
    censusItem(Number(options.tab))
  } else {
    getSubmitList()
  }
})

const instance: ComponentInternalInstance = getCurrentInstance()
const height: Ref<number | string> = ref(0)
const getBarHeight = () => {
  let query = uni.createSelectorQuery().in(instance)
  query.select('.cr-position-header').fields({ size: true, rect: true }, () => {})
  query.select('.report-content').fields({ size: true, rect: true }, () => {})
  query.exec((data) => {
    height.value = data[0].height + data[1].height + 'px'
  })
}

// 已提交未提交切换
const censusItem = (index: number) => {
  if (config.firstLoading && config.index === index) return false
  config.index = index
  config.where.page = 1
  config.firstLoading = true
  if (index === 0) {
    getSubmitList(true)
  } else {
    getNoSubmitList(true)
  }
}

const listLoading: Ref<boolean> = ref(false) //是否正在加载
// 获取已提交的数据
const getSubmitList = (tab: boolean = false) => {
  entDailySubmitListApi(config.where)
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
    })
    .catch((error: Res) => {
      message.error(error.message)
    })
}
// 获取未提交的数据
const getNoSubmitList = (tab: boolean = false) => {
  entDailyNoSubmitListApi(config.where)
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
    })
    .catch((error: Res) => {
      message.error(error.message)
    })
}

const getTipsText = (time: string) => {
  if (!time) return
  const timeArr = time.split('-')
  if (timeArr[0] === timeArr[1]) {
    if (timeArr[0] === moment(new Date()).format('YYYY/MM/DD')) {
      config.tipsText = `今日 00:00 ~ 23:59 统计`
      config.todayShow = true
    } else {
      config.tipsText = `${timeArr[0]} 00:00 ~ 23:59 统计`
      config.todayShow = false
    }
  } else {
    config.tipsText = `${timeArr[0]} 00:00 ~ ${timeArr[1]} 23:59 统计`
    // 判断当前时间是否在一段时间之内
    config.todayShow = moment().isBetween(`${timeArr[0]} 00:00`, `${timeArr[1]} 23:59`, null, '()')
  }
}

//数量统计页面
const getStatistics = () => {
  entDailyStatisticsApi(config.where)
    .then((res: Res) => {
      // 切换时数据清空
      config.statistics = res.data
    })
    .catch((error: Res) => {
      message.error(error.message)
    })
}

const changeFormBox = (e: Box) => {
  if (e) {
    config.where.types = e.status
  }
  if (e.status === 0) {
    if (!e.time) {
      config.where.time = `${moment(new Date()).format('YYYY/MM/DD')}-${moment(new Date()).format('YYYY/MM/DD')}`
    } else {
      config.where.time = e.time + '-' + e.time
    }
  } else if (e.status === 1) {
    if (!e.time) {
      config.where.time = `${moment().weekday(1).format('YYYY/MM/DD')}-${moment().weekday(7).format('YYYY/MM/DD')}`
    } else {
      config.where.time = e.time
    }
  } else {
    if (!e.time) {
      config.where.time = `${moment().startOf('month').format('YYYY/MM/DD')}-${moment().endOf('month').format('YYYY/MM/DD')}`
    } else {
      config.where.time = e.time
    }
  }
  config.where.page = 1
  getStatistics()
  getTipsText(config.where.time)
  if (config.index === 0) {
    getSubmitList(true)
  } else {
    getNoSubmitList(true)
  }
}

const censusItemList = (item) => {
  if (item && item.daily_id) {
    clickNavigateTo(`/pages/users/report/mine?type=${item.types}&id=${item.daily_id}`)
  }
}

// 下拉加载
onReachBottom(() => {
  if (listLoading.value) {
    config.where.page++
    if (config.index === 0) {
      getSubmitList()
    } else {
      getNoSubmitList()
    }
  }
})
</script>

<style scoped lang="scss">
.cr-position-header {
  position: sticky;
}

.content {
  width: 100%;
  position: relative;

  .bar-search {
    // #ifdef APP-PLUS
    border-top: 1px solid #ebeef5;
    // #endif
    width: 100%;
    background-color: #fff;

    ::v-deep .uni-calendar--fixed {
      z-index: 101;
    }
  }

  .report-content {
    padding: 30rpx 24rpx;
    background: #ffffff;
    border-radius: 16rpx;

    .left-title {
      font-size: 32rpx;
      color: $uni-text-color;
      font-weight: $uni-default-font-weight;
    }

    .right-title {
      .assess-button {
        display: inline-block;
        margin-top: 3rpx;
        width: 134rpx;
        height: 40rpx;
        border-radius: 20rpx;
        line-height: 40rpx;
        background-color: rgba(24, 144, 255, 0.1);
        border-color: rgba(24, 144, 255, 0.1);
        color: rgba(24, 144, 255, 1);
        font-size: 24rpx;
        font-weight: 600;
        padding: 0;

        .iconfont {
          margin-right: 10rpx;
          font-size: 24rpx;
        }

        &::after {
          border: none;
        }
      }
    }

    .report-info {
      font-size: 12px;
      color: $nui-text-color-four;
      line-height: 1.2;
      padding-top: 12rpx;
    }

    .report-list {
      width: 100%;
      padding: 52rpx 0 22rpx 0;
      display: flex;
      align-items: center;
      justify-content: space-around;

      .report-list-item {
        text-align: center;
        color: $uni-text-color;

        .num {
          font-size: 44rpx;
          font-weight: 400;
        }

        .text {
          padding-top: 14rpx;
          font-size: 26rpx;
          color: $nui-text-color-four;
        }
      }
    }
  }

  .census-content {
    padding: 0 30rpx;
    background-color: #fff;
    border-radius: 16rpx;
    min-height: calc(100vh - 54px - v-bind(height) - 110rpx);

    .census-header {
      width: 100%;
      height: 80rpx;
      display: flex;
      align-items: center;
      font-size: $uni-font-size-default;
      border-bottom: 1px solid $uni-line-style-color-three;

      .census-header-item {
        height: 100%;
        line-height: 80rpx;
        color: $nui-text-color-four;
        margin-right: 36rpx;
        border-bottom: 2px solid rgba(24, 144, 255, 0);

        &.active {
          color: $uni-text-color;
          font-weight: $uni-default-font-weight;
          border-bottom: 2px solid $uni-color-primary;
        }

        &:last-of-type {
          margin-right: 0;
        }
      }
    }

    ::v-deep .uni-list {
      .uni-list-item {
        margin-bottom: 0;

        &:last-of-type .item-list {
          border-bottom: none;
        }

        .uni-list-item__container {
          display: flex;
          padding: 0;
          align-items: center;
        }

        .uni-list--border::after {
          background-color: rgba(24, 144, 255, 0);
        }
      }
    }

    .item-list {
      width: calc(100% - 24rpx);
      margin-left: 24rpx;
      font-size: $uni-font-size-default;
      height: 126rpx;
      padding: 32rpx 0 24rpx 0;
      border-bottom: 1px solid $uni-line-style-color-three;

      .item-list-content {
        height: 60rpx;
        display: flex;
        align-items: center;

        .left {
          .name {
            color: #2b2c32;
            line-height: 1.2;
          }
        }

        .right {
          display: flex;
          align-items: center;
          justify-content: flex-end;
          font-size: 24rpx;
          color: $nui-text-color-four;
          line-height: 1.2;

          .iconfont {
            margin-left: 12rpx;
            display: inline-block;
            font-size: 24rpx;
            transform: rotate(180deg);
            color: $nui-text-color-four;
          }
        }
      }
    }
  }
}
</style>
