<template>
  <view class="nav" v-if="!loading">
    <uni-nav-bar :fixed="true" left-icon="left" status-bar backgroundColor="rgba(119,176,247,1)" color="#fff"
      :border="false" @clickLeft="cancel">
      <view class="nav-content">
        <view class="title">{{ pageTitle }}</view>
      </view>
      <template v-slot:right>
        <view class="bar-right">
          <picker ref="picker" mode="date" fields="month" :value="monthTime" start="2021-01" :end="lastTime"
            @change="bindPickerChange">
            <view class="dis-flex">
              <text class="uni-nav-bar-text">
                <text class="month">{{ monthTime.split('-')[1] }}月</text>
              </text>
              <uni-icons type="arrowdown" color="#fff" size="12" />
            </view>
          </picker>
        </view>
      </template>
    </uni-nav-bar>
    <view class="header mb10">
      <view class="user card" v-if="userInfo.avatar">
        <view class="user-msg" v-if="userInfo">
          <img class="avatar" :src="userInfo.avatar" mode="" />
          <view class="user-msg-right">

            <view class="name">{{ userInfo.real_name }}</view>

            <view class="position">{{ userInfo.frames.length > 0 ? userInfo.frames[0].frame.name : '' }}
              ({{ userInfo.job.name }} )</view>
          </view>
        </view>
      </view>
    </view>
    <view class="card">
      <view class="header" @click="jump(0)">
        <view class="title">上下班打卡统计</view>
        <view class="jump">
          查看明细
          <text class="iconfont icon-jinru-copy"></text>
        </view>
      </view>
      <view class="schedule">
        <view class="sta">
          <view class="sta-all">
            <view class="err">{{ clockStatistics.normal }}</view>
            <view class="line"> / </view>
            <view class="normal">{{ clockStatistics.abnormal }}</view>
          </view>
          <view class="tip">正常天数/异常天数</view>
        </view>

        <view class="sta-data">
          <view class="sta-data-item">
            <view class="num">{{ clockStatistics.work_hours }}</view>
            <view class="text">平均工时(h)</view>
          </view>
          <view class="sta-data-item" @click="goDetails(2, '迟到')">
            <view class="num c1">{{ clockStatistics.late }}</view>
            <view class="text">迟到(次)</view>
          </view>
          <view class="sta-data-item" @click="goDetails(4, '早退')">
            <view class="num c3">{{ clockStatistics.leave_early }}</view>
            <view class="text">早退(次)</view>
          </view>
        </view>
        <view class="sta-data">
          <view class="sta-data-item" @click="goDetails(1, '地点异常')">
            <view class="num c4">{{ clockStatistics.location_abnormal }}</view>
            <view class="text">地点异常(次)</view>
          </view>
          <view class="sta-data-item" @click="goDetails(5, '缺卡')">
            <view class="num c3">{{ clockStatistics.lack_card }}</view>
            <view class="text">缺卡(次)</view>
          </view>
          <view class="sta-data-item" @click="goDetails(6, '旷工')">
            <view class="num c3">{{ clockStatistics.absenteeism }}</view>
            <view class="text">旷工(天)</view>
          </view>
        </view>
      </view>
    </view>
    <view class="card">
      <view class="header" @click="jump(1)">
        <view class="title"> 加班统计 </view>
        <view class="jump">
          <text class="iconfont icon-jinru-copy"></text>
        </view>
      </view>
      <view class="table-list">
        <view class="item" @click="overTimeFn(0, '工作日加班')">
          <view class="num">工作日加班</view>
          <view class="msg">{{ overtimeStatistics.work }}小时</view>
        </view>
        <view class="item" @click="overTimeFn(2, '休息日加班')">
          <view class="num">休息日加班</view>
          <view class="msg">{{ overtimeStatistics.rest }}小时</view>
        </view>
        <view class="item" @click="overTimeFn(1, '节假日加班')">
          <view class="num">节假日加班</view>
          <view class="msg">{{ overtimeStatistics.holiday }}小时</view>
        </view>
      </view>
    </view>
    <view class="card">
      <view class="header" @click="jump(2)">
        <view class="title"> 假勤统计 </view>
        <view class="jump">
          <text class="iconfont icon-jinru-copy"></text>
        </view>
      </view>
      <view class="table-list">
        <view class="item" v-for="(item, index) in leaveStatistics" :key="index"
          @click="jumpFake(item.status, item.name)">
          <view class="num">{{ item.name }}</view>
          <view class="msg">{{ item.num }}次</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index'
  import progressBox from './components/progress.vue'
  import { monthStatistics, attendanceUserMsg } from '@/api/attendance'
  import { useStore } from 'vuex'

  const store = useStore()
  const userInfo = ref(store.state.app.userInfo)
  let monthTime = ref(`${new Date().getFullYear()}-${new Date().getMonth() + 1}`) //当年月
  let lastTime = ref(`${new Date().getFullYear()}-${new Date().getMonth() + 1}`)
  let picker = ref(null) //当前月：7
  let index = ref() //当前月：7
  let userId = ref(0)
  let loading = ref(true)
  const pageTitle = ref('个人月报')
  onLoad(async (options) => {
    if (options.monthValue) {
      monthTime.value = options.yearValue + '-' + options.monthValue
    }
    if (options.user_id) {
      userId.value = options.user_id
      await getAttendanceUserMsg()
    }
    await getMonthStatistics()
  })
  const overtimeList = reactive([{
      text: '工作日加班',
      num: 8,
    },
    {
      text: '休息日加班',
      num: 8,
    },
    {
      text: '节假日加班',
      num: 8,
    },
  ])
  const dataTree = reactive([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12])
  const clockStatistics = ref()
  const overtimeStatistics = ref()
  const leaveStatistics = ref([])
  const getMonthStatistics = () => {
    let d = {
      date: monthTime.value,
      type: 1,
      user_id: userId.value,
    }
    monthStatistics(d).then((res) => {
      let data = res.data
      clockStatistics.value = data.clock_statistics
      overtimeStatistics.value = data.overtime_statistics
      leaveStatistics.value = data.leave_statistics
      loading.value = false
    })
  }
  const getAttendanceUserMsg = () => {
    attendanceUserMsg(userId.value).then((res) => {
      userInfo.value = res.data
      pageTitle.value = `${res.data.real_name}的月报`
      uni.setNavigationBarTitle({
        title: `${pageTitle.value}的月报`,
      })
    })
  }
  // 月份选择
  const bindPickerChange = (e) => {
    monthTime.value = e.detail.value
    getMonthStatistics()
  }
  const goDetails = (val, text) => {
    uni.navigateTo({
      url: `/pages/attendance/detailed/userCheckList?user_id=${userId.value}&user_name=${userInfo.value.name||userInfo.value.real_name}&date=${monthTime.value}&status=${val}&text=${text}`,
    })
  }

  const overTimeFn = (val, text) => {
    uni.navigateTo({
      url: `/pages/attendance/detailed/userOvertimeList?user_id=${userId.value}&user_name=${userInfo.value.name||userInfo.value.real_name}&date=${monthTime.value}&status=${val}&text=${text}`,
    })
  }
  const jumpFake = (val, text) => {
    uni.navigateTo({
      url: `/pages/attendance/detailed/userVacationList?user_id=${userId.value}&user_name=${userInfo.value.name||userInfo.value.real_name}&date=${monthTime.value}&status=${val}&text=${text}`,
    })
  }

  const jump = (type, status) => {
    let url
    if (type === 0) {
      url = '/pages/attendance/detailed/userCheckList'
    } else if (type === 1) {
      url = '/pages/attendance/detailed/userOvertimeList'
    } else if (type === 2) {
      url = `/pages/attendance/detailed/userVacationList`
    }

    let queryParams = ''
    if (userId.value !== null) {
      queryParams =
        `?user_id=${userId.value}&user_name=${userInfo.value.real_name||userInfo.value.name}&date=${monthTime.value}`
    }


    uni.navigateTo({
      url: `${url}${queryParams}`,
    })
  }
  const cancel = () => {
    uni.navigateBack()
  }
</script>

<style lang="scss" scoped>
  .nav {
    background-size: 100% 240rpx;
    padding-bottom: 40rpx;
  }

  .nav-content {
    display: flex;
    justify-content: space-around;
    align-items: center;
    width: 100%;

    .title {
      font-size: 34rpx;
      font-weight: 500;
      color: rgba(255, 255, 255);
      line-height: 34rpx;
    }
  }

  .bar-right {
    /* #ifndef APP-PLUS-NVUE */
    display: flex;
    /* #endif */
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    margin-left: 4px;

    .dis-flex {
      display: flex;
      align-items: center;
    }
  }

  .header {
    padding-top: 20rpx;

    .user {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin: 0 20rpx;
      padding: 40rpx 28rpx;

      .user-msg {
        display: flex;
        align-items: center;

        .user-msg-right {
          display: flex;
          flex-direction: column;
          justify-content: space-around;
          height: 108rpx;
        }

        .avatar {
          width: 108rpx;
          height: 108rpx;
          border-radius: 8rpx;
          margin-right: 20rpx;
        }

        .name {
          font-size: 32rpx;
          font-weight: 500;
          color: #303133;
          line-height: 32rpx;
        }

        .position {
          font-size: 24rpx;
          font-weight: 400;
          color: #909399;
          line-height: 24rpx;
        }
      }

      .rule {
        font-size: 12rpx;
        font-weight: 400;
        color: #308bf8;
        line-height: 12rpx;
      }
    }
  }

  .card {
    margin: 20rpx 20rpx 0rpx;
    background-color: #fff;
    border-radius: 12rpx;

    .header {
      display: flex;
      justify-content: space-between;
      padding: 30rpx 24rpx;
      border-bottom: 1rpx solid #ebeef5;

      .title {
        font-size: 30rpx;
        font-weight: 500;
        color: #303133;
        line-height: 30rpx;

        .gard {
          font-size: 26rpx;
          font-weight: 500;
          color: #909399;
          line-height: 30rpx;
        }
      }

      .jump {
        display: flex;
        align-items: center;
        font-size: 24rpx;
        font-family: PingFang SC-Regular, PingFang SC;
        font-weight: 400;
        color: #308bf8;
        line-height: 24rpx;

        .icon-jinru-copy {
          font-size: 20rpx;
          color: #c0c4cc;
          margin-left: 12rpx;
        }
      }
    }

    .table-list {
      .item:last-child {
        border-bottom: none;
      }

      .item {
        margin-left: 24rpx;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1rpx solid #f0f1f5;
        padding: 40rpx 24rpx 40rpx 0;

        .num {
          font-size: 30rpx;
          font-weight: 400;
          color: #303133;
          line-height: 30rpx;
        }

        .msg {
          font-size: 28rpx;
          font-weight: 400;
          color: #606266;
          line-height: 28rpx;
        }
      }
    }

    .schedule {
      padding: 30rpx 0;

      .sta {
        margin: 38rpx 0 48rpx;
        text-align: center;

        .sta-all {
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 44rpx;
          font-weight: 500;
          line-height: 44rpx;
          color: #eeeeee;

          .err {
            color: #303133;
          }

          .line {
            margin: 0 8rpx;
          }

          .normal {
            color: #ed4014;
          }
        }

        .tip {
          margin-top: 28rpx;
          font-size: 28rpx;
          font-weight: 400;
          color: #606266;
          line-height: 28rpx;
        }
      }

      .sta-data {
        display: flex;
        justify-content: space-between;
        padding: 36rpx 36rpx 0;
        margin-bottom: 36rpx;

        .sta-data-item {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          text-align: center;

          .num {
            margin-bottom: 16rpx;
            font-size: 32rpx;
            font-weight: 500;
            color: #303133;
            line-height: 32rpx;
          }

          .c1 {
            color: #ff9900;
          }

          .c2 {
            color: #ff9900;
          }

          .c3 {
            color: #ed4014;
          }

          .c4 {
            color: #19be6b;
          }

          .text {
            font-size: 28rpx;
            font-weight: 400;
            color: #909399;
            line-height: 28rpx;
            min-width: 4rem;
          }
        }
      }

      .stop-time {
        margin-top: 64rpx;
        font-size: 24rpx;
        font-weight: 400;
        color: #909399;
        line-height: 24rpx;
        text-align: center;
      }
    }
  }
</style>