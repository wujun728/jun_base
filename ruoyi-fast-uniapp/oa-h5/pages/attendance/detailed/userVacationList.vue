<template>
  <view class="nav">
    <uni-nav-bar :fixed="true" left-icon="left" status-bar backgroundColor="rgba(255,255,255,1)" color="#303133"
      :border="false" @clickLeft="cancel">
      <view class="nav-content">
        <view class="title">
          <view class="head-title">{{ pageTitle }}</view>
          <text>的假勤明细</text>
        </view>
      </view>
      <template v-slot:right>
        <view class="bar-right">
          <picker ref="picker" mode="date" fields="month" :value="monthTime" start="2021-01" :end="lastTime"
            @change="bindPickerChange">
            <text class="uni-nav-bar-text">
              <text class="month">{{ monthTime }}</text>
              <uni-icons type="arrowdown" color="#606266" size="12" />
            </text>
          </picker>
        </view>
      </template>
    </uni-nav-bar>
    <!-- 		<view class="header">
			<picker ref="picker" :value="index" range-key="label" :range="typeList" @change="bindTypeChange">
				<text class="title">{{ status ? statusText : typeList[index].label }}</text>
				<uni-icons type="arrowdown" size="12" />
			</picker>
		</view> -->
    <view v-if="leaveList.length > 0" class="card">
      <view class="table-list">
        <view class="item" v-for="(item, index) in leaveList" :key="index">
          <view class="num">{{ item.date }}</view>
          <view class="msg">
            <view class="msg-item" v-for="(e, i) in item.details" :key="i">
              {{ e.work_type == 0 ? "上班" : e.work_type == 1 ? "下班" : ""
              }}{{ e.status == -1 ? '补卡' : mapStatus(e.status) }}{{ e.work_hours
              }}{{e.status == -1 ? '次' :
                e.time_type == "day"
                  ? "天"
                  : e.time_type == "hour"
                  ? "小时"
                  : ""
              }}
              {{
                i != item.details.length - 1 && i < item.details.length
                  ? ","
                  : ""
              }}
            </view>
          </view>
        </view>
      </view>
    </view>
    <view v-else class="default">
      <image src="../../../static/image/empty.png" mode=""></image>
      <view class="text">暂无假勤明细～</view>
    </view>
  </view>
</template>

<script setup>
  import {
    onReachBottom
  } from "@dcloudio/uni-app";
  import moment from 'moment'
  import {
    leaveStatistics,
    monthStatistics
  } from "@/api/attendance";

  let lastTime = ref(`${new Date().getFullYear()}-${new Date().getMonth() + 1} `); //当前月：7
  let monthTime = ref(
    `${new Date().getFullYear()}-${new Date().getMonth() + 1} `
  ); //当前月：7
  let currentMonth = ref(new Date().getMonth() + 1); //当前月：7
  let index = ref(0);
  let page = ref(1);
  let limit = ref(20);
  const andMore = ref(true);

  const typeList = ref([{
    label: "全部",
    value: 0,
  }, ]);
  const leaveList = ref([]);
  let userId = ref(0);
  let status = ref(0)
  let statusText = ref('')
  let pageTitle = ref()
  onLoad((options) => {
    if (options.user_id) {
      userId.value = options.user_id;
      pageTitle.value = options.user_name;
      uni.setNavigationBarTitle({
        title: pageTitle.value,
      });
    }
    status.value = options.status
    switch (options.status) {
      case '-1':
        statusText.value = '补卡'
        index.value = 1
        break;
      case '-2':
        statusText.value = '出差'
        index.value = 2
        break;
      case '-3':
        statusText.value = '外出'
        index.value = 3
        break;
      case '1':
        statusText.value = '事假'
        index.value = 4
        break;
      case '2':
        statusText.value = '年假'
        index.value = 5
        break;
      case '3':
        statusText.value = '调休假'
        index.value = 6
        break;
      case '4':
        statusText.value = '产假'
        index.value = 7
        break;
      case '5':
        statusText.value = '陪产假'
        index.value = 8
        break;
      case '6':
        statusText.value = '病假'
        index.value = 9
        break;
      case '7':
        statusText.value = '丧假'
        index.value = 10
        break;
      case '8':
        statusText.value = '婚假'
        index.value = 11
        break;
      default:
    }
    getMonthStatistics();
    getLeaveStatistics();
  });
  const bindPickerChange = (e) => {
    monthTime.value = e.detail.value
    getLeaveStatistics();
  };
  const bindTypeChange = (e) => {
    status.value = ''
    index.value = e.detail.value;
    getLeaveStatistics();
  };
  const getLeaveStatistics = () => {
    monthTime.value = moment(monthTime.value).format('YYYY-MM')
    let d = {
      date: monthTime.value,
      status: index.value,
      user_id: userId.value,
      page: page.value,
      limit: limit.value,
    };

    leaveStatistics(d).then((res) => {
      leaveList.value = res.data;
      if (res.data.length < limit.value) andMore.value = false;
    });
  };
  const getMonthStatistics = () => {
    monthTime.value = moment(monthTime.value).format('YYYY-MM')
    let d = {
      date: monthTime.value,
      type: 1,
      user_id: userId.value,
    };
    monthStatistics(d).then((res) => {
      let data = res.data;
      let arr;
      arr = data.leave_statistics.map((e) => {
        return {
          label: e.name,
          value: e.status
        };
      });
      typeList.value = [...typeList.value, ...arr];
    });
  };
  const mapStatus = (status) => {
    let name;
    typeList.value.map((e) => {
      if (e.value == status) {
        name = e.label;
      }
    });
    return name;
  };
  const cancel = () => {
    uni.navigateBack();
  };
  // 下拉加载
  onReachBottom(() => {
    if (andMore.value) {
      page.value++;
      getLeaveStatistics();
    }
  });
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

    .head-title {
      max-width: 160rpx;
      display: inline-block;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }

    .title {
      display: flex;
      align-items: center;
      font-size: 34rpx;
      font-weight: 500;
      color: #303133;
      line-height: 34rpx;
    }
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
    width: 160rpx !important;
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

  .header {
    display: flex;
    justify-content: space-between;
    background-color: #f0f1f5;
    color: #606266;
    padding: 24rpx 30rpx;

    .title {
      font-size: 26rpx;
      font-weight: 400;
      color: #606266;
      line-height: 26rpx;
    }
  }

  .card {
    background-color: #fff;
    border-radius: 12rpx;

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
        padding: 42rpx 24rpx 40rpx 0;

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
          width: 13em;
          display: flex;
          justify-content: right;

          .msg-item {
            width: max-content;
            white-space: nowrap;
          }
        }
      }
    }
  }

  ::v-deep.uni-navbar__header-container {
    padding: 0;
  }

  .default {
    width: 100%;
    height: calc(100vh - 172rpx);
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