<template>
  <view class="nav">
    <uni-nav-bar :fixed="true" left-icon="left" status-bar backgroundColor="rgba(255,255,255,1)" color="#303133"
      :border="false" @clickLeft="cancel">
      <view class="nav-content">
        <view class="title">{{ title }}</view>
      </view>
      <template v-slot:right>
        <view class="bar-right" v-if="type != 0">
          <picker ref="picker" mode="date" fields="month" :value="monthTime" start="2021-01" :end="lastTime"
            @change="bindPickerChange">
            <text class="uni-nav-bar-text">
              <text class="month">{{ monthTime }}</text>
              <!-- <uni-icons type="arrowdown" color="#606266" size="12" /> -->
            </text>
          </picker>
        </view>
        <view class="bar-right" v-else>
          <text class="uni-nav-bar-text">
            <text class="month">{{ showDate }}</text>
            <!-- <uni-icons type="arrowdown" color="#606266" size="12" /> -->
          </text>
        </view>
      </template>
    </uni-nav-bar>

    <selected-label title="选择条件" ref="selectedLabelRef" :type="type" @changeItem="changeItem"> </selected-label>
    <view class="card">
      <!-- 			<view class="header" :class="dataList.length ? 'm-t-74' : 'm-t-88'">
				<view v-if="type == 0 || type == 1 || type == 3" @click="changeLabel">
					<text class="title">{{ data.labelText }}</text>
					<text class="iconfont icon-zhankai1"></text>
				</view>
				<picker v-else class="picker-selector" mode="selector" @change="changeUsers" :value="data.usersIndex"
					:range="data.usersData" range-key="name">
					<text class="title">{{ data.usersText }}</text>
					<text class="iconfont icon-zhankai1"></text>
				</picker>
			</view> -->
      <view class="schedule" v-if="dataList.length">
        <view class="item" v-for="(item, index) in dataList" :key="index" @click="jump(item)">
          <view class="user">
            <image :src="item.card.avatar" mode=""></image>
            <view class="user-msg">
              <view class="name">{{ item.card.name }}</view>
              <view class="title">
                <text>{{ item.card.frame.name }}</text>
                <text v-if="item.card.job"> ({{ item.card.job.name }})</text>
              </view>
            </view>
          </view>
          <view v-if="type == 0" class="msg" :class="{ err: item.external.status }">
            <text class="normal" v-if="!item.external.status.length">正常</text>
            <text class="waring" v-else-if="item.absenteeism == 1">旷工</text>
            <text class="waring" v-else>{{ getStatusString(item.external.status) }}</text>
            <!-- <text class="waring" v-else>{{ getStatusString([item.external.status]) }}</text> -->
            <text v-if="item.external.location_status">{{ getLacationString(item.external.location_status) }}</text>
          </view>
          <view v-else-if="type == 1" class="msg" :class="{ err: item.status != 0 }">
            <text v-if="item.status == 0">正常</text>
            <text v-else>{{ typeText(item.status) }}{{ item.num }}次</text>
          </view>
          <view v-else-if="type == 2 || type == 3" class="msg">
            <text>{{ type == 3 ? '加班' : '假勤' }}{{ item.num }}次</text>
          </view>
        </view>
      </view>
      <view v-else class="default">
        <image src="../../../static/image/empty.png" mode=""></image>
        <view class="text">暂无{{ type == 3 ? '加班' : type == 2 ? '假勤' : '上下班' }}明细～</view>
      </view>
    </view>
  </view>
</template>

<script setup>
  import { onReachBottom } from '@dcloudio/uni-app'
  import selectedLabel from '../components/selectedLabel.vue'
  import {
    teamCommuteDetails,
    teamAttendanceStatistics,
    teamAttendanceLeaveStatistics,
    teamAttendanceOvertimeStatistics
  } from '@/api/attendance'
  import { getStatusString, getLacationString, typeText } from '@/utils/attendance'
  let lastTime = ref(`${new Date().getFullYear()}-${new Date().getMonth() + 1} `)
  let monthTime = ref(`${new Date().getFullYear()}-${new Date().getMonth() + 1} `) //当年月
  let currentMonth = ref(new Date().getMonth() + 1)
  let index = ref(0)
  let title = ref('')
  let showDate = ref('')
  let type = ref(1)
  let status = ref([])
  let page = ref(1)
  let limit = ref(20)
  const andMore = ref(true)

  onLoad((options) => {
    type.value = options.type
    status.value = options.status
    monthTime.value = options.date
    if (options.type == 0) {
      showDate.value = `${options.mxMonth}月${options.mxDate}日`
      data.date = `${options.mxYear}-${options.mxMonth}-${options.mxDate}`
    }
    if (options.status) {
      data.status = options.status
      data.labelText = options.text
      data.usersText = options.text
    }
    switch (options.type) {
      case '0':
        title.value = '上下班明细'
        getTeamCommuteDetails()
        break
      case '1':
        title.value = '考勤明细'
        getTeamAttendanceStatistics()
        break
      case '2':
        title.value = '假勤明细'
        getTeamAttendanceLeaveStatistics()
        break
      case '3':
        title.value = '加班明细'
        getTeamAttendanceOvertimeStatistics()
        break
    }
    uni.setNavigationBarTitle({
      title: title.value,
    })
  })
  const dataTree = reactive([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12])

  const dataList = ref([])
  const formData = reactive({
    label: [],
    approveId: 0,
    usersIndex: 0,
  })
  const cancel = () => {
    uni.navigateBack({
      delta: 1,
    })
  }
  // 上下班明细
  const getTeamCommuteDetails = () => {
    let d = {
      date: data.date,
      status: data.status,
      page: page.value,
      limit: limit.value,
    }
    teamCommuteDetails(d).then((res) => {
      dataList.value = [...dataList.value, ...res.data]
    })
  }
  // 考勤明细
  const getTeamAttendanceStatistics = () => {
    let d = {
      date: monthTime.value,
      status: data.status,
      page: page.value,
      limit: limit.value,
    }
    teamAttendanceStatistics(d).then((res) => {
      dataList.value = [...dataList.value, ...res.data]
    })
  }
  // 假勤
  const getTeamAttendanceLeaveStatistics = () => {
    let d = {
      date: monthTime.value,
      status: data.status,
      page: page.value,
      limit: limit.value,
    }
    teamAttendanceLeaveStatistics(d).then((res) => {
      dataList.value = [...dataList.value, ...res.data]
    })
  }
  // 加班
  const getTeamAttendanceOvertimeStatistics = () => {
    let d = {
      date: monthTime.value,
      status: data.status,
      page: page.value,
      limit: limit.value,
    }
    teamAttendanceOvertimeStatistics(d).then((res) => {
      dataList.value = [...dataList.value, ...res.data]
    })
  }
  const selectedLabelRef = ref(null)
  // 标签选择
  const changeLabel = (e) => {
    selectedLabelRef.value.popupOpen(formData.label)
  }
  // 用户选择
  const selectedUserRef = ref(null)

  const changeUserLabel = (e) => {
    selectedUserRef.value.popupOpen(formData.userlabel)
  }
  // 月份选择
  const bindPickerChange = (e) => {
    dataList.value = []
    page.value = 1
    monthTime.value = e.detail.value
    switch (type.value) {
      case '0':
        getTeamCommuteDetails()
        break
      case '1':
        getTeamAttendanceStatistics()
        break
      case '2':
        getTeamAttendanceLeaveStatistics()
        break
      case '3':
        getTeamAttendanceOvertimeStatistics()
        break
    }
  }
  const bindTypeChange = (e) => {
    index.value = e.detail.value
  }

  const data = reactive({
    usersText: '状态',
    labelText: '状态',
    date: '',
    status: '',
    usersData: [{
        name: '假勤',
        id: 1,
      },
      {
        name: '补卡',
        id: 2,
      },
      {
        name: '出差',
        id: 3,
      },
      {
        name: '外出',
        id: 4,
      },
    ],
  })

  // 标签选择回调
  const changeItem = (e, name) => {
    formData.label = e
    data.status = e.join()
    page.value = 1
    dataList.value = []
    data.labelText = e.length > 0 ? name.join() : '状态'
    switch (type.value) {
      case '0':
        getTeamCommuteDetails()
        break
      case '1':
        getTeamAttendanceStatistics()
        break
      case '2':
        getTeamAttendanceLeaveStatistics()
        break
      case '3':
        getTeamAttendanceOvertimeStatistics()
        break
    }
  }
  const jump = (item) => {
    if (type.value == 0) {
      uni.navigateTo({
        url: `/pages/attendance/statistics?user_id=${item.card.id}&is_user=1&user_name=${item.card.name}`,
      })
    }
    if (type.value == 1) {
      uni.navigateTo({
        url: `/pages/attendance/personalReport?user_id=${item.card.id}&is_user=1&user_name=${item.card.name}`,
      })
    }
  }
  // 类型选择
  const changeUsers = (e) => {
    const len = e.detail.value
    data.usersText = data.usersData[len].name
    formData.approveId = data.usersData[len].id
    data.status = len
    dataList.value = []
    getTeamAttendanceLeaveStatistics()
  }
  onReachBottom(() => {
    if (andMore.value) {
      page.value++
      switch (type.value) {
        case '0':
          getTeamCommuteDetails()
          break
        case '1':
          getTeamAttendanceStatistics()
          break
        case '2':
          getTeamAttendanceLeaveStatistics()
          break
        case '3':
          getTeamAttendanceOvertimeStatistics()
          break
      }
    }
  })
</script>

<style lang="scss" scoped>
  .nav {
    background: #fff;
    background-size: 100% 240rpx;
  }

  .nav-content {
    display: flex;
    justify-content: space-around;
    align-items: center;
    width: 100%;

    .title {
      font-size: 34rpx;
      font-weight: 500;
      color: #303133;
      line-height: 34rpx;
    }
  }

  ::v-deep uni-view.uni-navbar {
    width: 100%;
    height: 88rpx;
    background: #fff;
    position: fixed;
    top: 0;
    z-index: 99;
  }

  ::v-deep .uni-nav-bar-text {
    width: max-content;

    span {
      display: flex;
      align-items: center;

      .month {
        white-space: nowrap;
        font-size: 30rpx;
        font-weight: 500;
        color: #303133;
        line-height: 30rpx;
        margin-right: 10rpx;
      }
    }
  }

  ::v-deep .uni-navbar__header-btns {
    width: 180rpx !important;
  }

  ::v-deep .uni-navbar__header {
    padding: 0 40rpx;
  }

  .bar-right {
    /* #ifndef APP-PLUS-NVUE */
    display: flex;
    /* #endif */
    flex-direction: row;
    align-items: center;
    justify-content: right;
    width: 180rpx;
    margin-left: 4px;
  }

  .m-t-74 {
    margin-top: -74rpx;
  }

  .m-t-88 {
    margin-top: 88rpx;
  }

  .header {
    position: fixed;
    display: flex;
    width: 100%;
    height: 88rpx;
    background-color: #f0f1f5;
    color: #606266;
    padding: 24rpx 30rpx;
    z-index: 99;
    margin-top: -80rpx;

    .title {
      font-size: 26rpx;
      font-weight: 400;
      color: #606266;
      line-height: 26rpx;
    }

    .icon-zhankai1 {
      color: #c0c4cc;
      font-size: 20rpx;
      margin-left: 10rpx;
    }
  }

  .card {
    background-color: #fff;
    border-radius: 12rpx;

    .schedule {
      /* #ifdef APP-PLUS */
      margin-top: 152rpx;
      /* #endif */
      /* #ifndef APP-PLUS */
      margin-top: 72rpx;
      /* #endif */

      .item:last-child {
        border-bottom: none;
      }

      .item {
        margin-left: 24rpx;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1rpx solid #f0f1f5;
        padding: 30rpx 28rpx 30rpx 0;

        .user {
          display: flex;

          image {
            width: 80rpx;
            height: 80rpx;
            border-radius: 8rpx;
            margin-right: 20rpx;
          }

          .user-msg {
            display: flex;
            flex-direction: column;
            justify-content: space-between;

            .name {
              font-size: 30rpx;
              font-weight: 400;
              color: #303133;
              line-height: 30rpx;
              margin-bottom: 10rpx;
            }

            .title {
              font-size: 24rpx;
              font-weight: 400;
              color: #909399;
              line-height: 30rpx;
            }
          }
        }

        .msg {
          font-size: 28rpx;
          font-weight: 400;
          color: #606266;
          line-height: 36rpx;
          text-align: right;
          width: 9em;
          word-break: keep-all;
        }

        .msg.err {
          color: #ed4014;
        }
      }
    }
  }

  .waring {
    color: #ed4014;
  }

  .normal {
    color: #606266;
  }

  .default {
    width: 100%;
    // height: calc(100vh - 172rpx);
    height: 100vh;
    padding-top: 246rpx;

    image {
      width: 400rpx;
      height: 300rpx;
      margin: auto;
      display: block;
    }

    .text {
      font-size: 26rpx;
      font-family: PingFang SC-Regular, PingFang SC;
      font-weight: 400;
      color: #909399;
      width: 100%;
      text-align: center;
      margin-top: 28rpx;
    }
  }
</style>