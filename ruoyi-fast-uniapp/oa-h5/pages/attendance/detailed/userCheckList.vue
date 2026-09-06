<template>
  <view class="nav">
    <uni-nav-bar :fixed="true" left-icon="left" status-bar backgroundColor="rgba(255,255,255,1)" color="#303133"
      :border="false" @clickLeft="cancel">
      <view class="nav-content">
        <view class="title">
          <view class="head-title">{{ pageTitle }}</view>
          <text>的考勤明细</text>
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
    <selected-label title="筛选状态" ref="selectedLabelRef" @changeItem="changeItem">
    </selected-label>
    <!-- 		<view class="header" @click="changeLabel">
			<text class="title">{{ data.labelText }}</text>
			<uni-icons type="arrowdown" size="12" />
		</view> -->
    <view v-if="leaveList.length > 0" class="card">
      <view class="table-list">
        <view class="item" v-for="(item, index) in leaveList" :key="index">
          <view class="num">
            <text>{{ item.date }}-</text>
            <text v-if="!item.details.length && item.absenteeism == 0">正常</text>
            <text class="waring" v-else-if="item.absenteeism == 1">旷工</text>
            <!-- <text v-else>{{ getStatusString(item.external.status) }}</text> -->
            <text class="waring" v-else>{{ getStatusString([item.details[0].status]) }}</text>
            <text v-if="item.location_status.length">({{ getLacationString(item.external.location_status) }})</text>
          </view>
          <view v-if="item.details.length" class="msg" @click="applyModal(item.date,item)">处理</view>
        </view>
      </view>
    </view>
    <view v-else class="default">
      <image src="../../../static/image/empty.png" mode=""></image>
      <view class="text">暂无考勤明细～</view>
    </view>
    <!-- 申请单 -->
    <uni-popup ref="refsMenuPopup" type="bottom" background-color="#fff" :is-mask-click="false">
      <applyForMenuModal :dateVal="dateVal" :dateId=data.dateId @cancel="applyCancel" @selectType="selectType">
      </applyForMenuModal>
    </uni-popup>
  </view>
</template>

<script setup>
  import {
    onReachBottom
  } from "@dcloudio/uni-app";
  import {
    personAttendanceStatistics
  } from "@/api/attendance";
  import applyForMenuModal from "../components/applyForMenuModal.vue";
  import {
    getStatusString,
    getLacationString
  } from "@/utils/attendance";
  import selectedLabel from "../components/selectedLabel.vue";
  let lastTime = ref(`${new Date().getFullYear()}-${new Date().getMonth() + 1} `);
  let monthTime = ref(
    `${new Date().getFullYear()}-${new Date().getMonth() + 1} `
  ); //当年月
  let currentMonth = ref(new Date().getMonth() + 1);
  let index = ref(0);
  let userId = ref(0);
  let page = ref(1);
  let limit = ref(20);
  let pageTitle = ref();
  let dateVal = ref('');
  const andMore = ref(true);

  onLoad((options) => {
    if (options.status) {
      data.status = options.status
      data.labelText = options.text
    }
    if (options.user_id) {
      userId.value = options.user_id;
      pageTitle.value = options.user_name;
      uni.setNavigationBarTitle({
        title: pageTitle.value,
      });
    }
    monthTime.value = options.date;
    getPersonAttendanceStatistics();
  });

  const typeList = reactive([{
      label: "全部",
      value: 0,
    },
    {
      label: "请假",
      value: 1,
    },
    {
      label: "迟到",
      value: 2,
    },
  ]);
  // 异常处理 申请
  const refsMenuPopup = ref(null); // 申请
  const applyModal = (date, item) => {
    dateVal = date
    data.dateId = item.id
    refsMenuPopup.value.open()
  };
  const applyCancel = () => {
    refsMenuPopup.value.close();
  }
  const selectType = () => {
    refsMenuPopup.value.close();
  };
  const leaveList = ref([]);
  const formData = reactive({
    label: [],
  });
  const selectedLabelRef = ref(null);
  // 标签选择
  const changeLabel = (e) => {
    selectedLabelRef.value.popupOpen(formData.label);
  };

  const bindPickerChange = (e) => {
    monthTime.value = e.detail.value;
    getPersonAttendanceStatistics();
  };
  const bindTypeChange = (e) => {
    index.value = e.detail.value;
  };

  const data = reactive({
    labelText: "全部",
    status: '',
    dateId: 0,
  });
  // 标签选择回调
  const changeItem = (e, name) => {
    formData.label = e;
    let status = []
    e.forEach(value => {
      status.push(value);
    });
    data.status = status.toString()
    page.value = 1;
    if (e.length > 0) {
      let labelText = []
      name.forEach(value => {
        labelText.push(value);
      });
      data.labelText = labelText.toString();
    } else {
      data.labelText = "全部";
    }
    getPersonAttendanceStatistics();
    // emit("change", formData);
  };
  const getPersonAttendanceStatistics = () => {
    let d = {
      date: monthTime.value,
      status: data.status,
      user_id: userId.value,
      page: page.value,
      limit: limit.value,
    };
    personAttendanceStatistics(d).then((res) => {
      leaveList.value = [...leaveList.value, ...res.data];
    });
  };
  const cancel = () => {
    uni.navigateBack();
  };
  // 下拉加载
  onReachBottom(() => {
    if (andMore.value) {
      page.value++;
      getPersonAttendanceStatistics();
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
    // justify-content: space-between;
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

      .waring {
        color: #ED4014;
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
          font-size: 30rpx;
          font-weight: 400;
          color: #308BF8;
          line-height: 30rpx;
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