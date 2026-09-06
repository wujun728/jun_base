<template>
  <view class="notice-list">
    <!-- #ifdef APP-PLUS -->
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-left="false" :is-right="false"></default-nav-bar>
      <!-- 			<view class="notice-header">
				<view class="notice-header-item" @click="noticeHeaderItem(1)">
					<text class="iconfont icon-xiaoxi-wodericheng"></text>
					<text>我的日程</text>
					<uni-badge v-if="data.allFinish" text="3" :is-dot="true" absolute="rightTop" :offset="[-5,-8]" size="small"></uni-badge>
				</view>
				<view class="notice-header-item-line"></view>
				<view class="notice-header-item" @click="noticeHeaderItem(2)">
					<text class="iconfont icon-xiaoxi-gongzuodaiban"></text>
					<text>工作待办</text>
				</view>
			</view> -->
    </view>
    <!-- #endif -->
    <view class="notice-content">
      <notice-list :list-data="data.listData" empty-title="暂无待办消息～"></notice-list>
    </view>
    <global-index />
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import noticeList from './components/noticeList.vue'
  import globalIndex from '@/components/globalIndex/index.vue'
  import { useStore } from 'vuex'
  import { toLogin } from '@/libs/login'
  import message from '@/utils/message'
  import moment from 'moment'
  import { messageCateApi, scheduleTypesApi, scheduleListApi } from '@/api/user'
  import { clickNavigateTo } from '@/utils/helper'
  import { useBarHeight } from '@/utils/useVerifyCode'

  const store = useStore()
  const isLogin = computed(() => store.state.app.isLogin)
  onShow(() => {
    if (!isLogin.value) {
      toLogin()
    } else {
      // getScheduleTypes()

      getMessageCate()

    }
  })

  const rightIcon = reactive([
    { type: 1, icon: 'icon-sousuo' }
  ])

  const data = reactive({
    defaultTitle: '待办(0)',
    listData: [],
    allFinish: false
  })

  const noticeHeaderItem = type => {
    let str = ''
    if (type === 1) {
      str = '/pages/users/schedule/index'
    } else if (type === 2) {
      str = '/pages/notice/work'
    }
    clickNavigateTo(str)
  }
  const getMessageCate = () => {
    messageCateApi().then(res => {
      data.listData = res.data
      let num = 0
      data.listData.map(item => {
        num += Number(item.count);
      })

      if (num == 0) {
        uni.removeTabBarBadge({
          index: 1
        });
      } else {
        uni.setTabBarBadge({
          index: 1,
          text: num > 99 ? '99+' : num,
        });
      }


      scrollToPage()
    }).catch(error => {
      message.error(error.message)
    })
  }

  const { height, getBarHeight } = useBarHeight()
  const instance = getCurrentInstance()
  const scrollToPage = () => {
    nextTick(() => {
      getBarHeight('.notice-content', instance, false)
      uni.pageScrollTo({
        scrollTop: height.value,
        duration: 0
      })
    })
  }

  //获取类型
  const getScheduleTypes = () => {
    let checkedTypes = []
    data.allFinish = false
    scheduleTypesApi().then(res => {
      if (res.data.length > 0) {
        res.data.forEach(value => {
          checkedTypes.push(value.key)
        })
      }
      const data_s = {
        types: checkedTypes,
        time: moment(new Date()).format('YYYY/MM/DD'),
        period: 0
      }
      getScheduleList(data_s)
    }).catch(error => {
      message.error(error.message)
    })
  }

  //获取列表
  const getScheduleList = data_s => {
    scheduleListApi(data_s).then(res => {
      let listData = res.data[0].list
      for (let i = 0; i < listData.length; i++) {
        let value = listData[i]
        if (value.finish === 0) {
          data.allFinish = true
          break
        }
      }
    }).catch(error => {
      message.error(error.message)
    })
  }
</script>
<style>
  page {
    background-color: #fff;
  }
</style>

<style scoped lang="scss">
  .cr-position-header {
    position: sticky;
    top: 0;
    background-color: #fff;
    /* #ifdef APP-PLUS */
    // border-bottom: 1px solid $uni-line-style-color-three;
    /* #endif */
  }

  .notice-header {
    height: 72rpx;
    width: 100%;
    border-bottom: 1px solid $uni-line-style-color-three;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .notice-header-item {
      width: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24rpx;
      line-height: 1;
      color: $nui-text-color-two;

      .iconfont {
        font-size: 40rpx;
        margin-right: 4rpx;
      }
    }

    .notice-header-item-line {
      width: 1px;
      height: 32rpx;
      background-color: $uni-line-style-color-three;
    }
  }


  .notice-content {
    padding: 30rpx;

    ::v-deep .uni-badge {
      z-index: 9;
    }
  }
</style>