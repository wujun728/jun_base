<template>
  <view class="main">
    <!-- #ifdef APP-PLUS -->
    <uni-nav-bar dark :fixed="true" background-color="#308bf8" :border="false" status-bar left-icon="left" title="上下班打卡"
      @clickLeft="back" />
    <!-- #endif -->
    <view class="header mb10" v-if="userInfo">
      <view class="user card">
        <view class="user-msg">
          <image class="avatar" :src="userInfo.avatar" mode=""></image>
          <view>
            <view class="name">{{ userInfo.name }}</view>
            <view class="position">{{ userInfo.job?userInfo.job.name:"--" }}</view>
          </view>
        </view>
        <navigator v-if="shift && !isWhite" class="rule" url="/pages/attendance/rules" open-type="navigate"
          hover-class="navigator-hover">
          考勤规则
        </navigator>
      </view>
    </view>
    <view class="ab-condition card">
      <view class="text">本月待处理异常</view>
      <navigator hover-class="none" class="nav-right" url="/pages/attendance/personalReport"
        animation-type="slide-in-right">
        <text class="days">{{ abnormal }}天</text>

        <text class="iconfont icon-jinru-copy"></text>
      </navigator>
    </view>
    <view class="attendance-card card" v-if="!loading">
      <view v-for="(item, index) in basicData" :key="index">
        <view class="stage mb10">
          <view class="time">{{ item.work_hours }}</view>
          <view class="status">
            <view class="status-time">
              <view class="title">
                <view>上班打卡 {{ item.on ? item.on.clock_time : '' }}</view>
                <view v-if="item.on" class="title">
                  <view v-if="item.on.status == 5 || item.on.status == 7" class="tag lack">缺卡</view>
                  <view v-if="item.on.location_status == 1" class="tag out">外勤</view>
                  <view v-if="item.on.status == 2" class="tag be-late">迟到</view>
                  <view v-if="item.on.location_status == 2" class="tag be-add">地点异常</view>
                </view>
              </view>
              <view v-if="item.on">
                <view v-if="item.on.update_status == 1 && (recordData.length == 2 || recordData.length == 4)"
                  class="renew" @click="signIn(index, 'on')">{{ isWhite ? '' : '更新打卡' }}</view>
                <view
                  v-else-if="(item.on.status > 1 || item.on.is_external == 2) && (index == 0 ? isAfterOffHours1 : isAfterOffHours2)"
                  class="renew" @click="applyModal">异常处理
                </view>
              </view>
            </view>
            <view v-if="item.on && item.on.address" class="address line1">
              <text class="iconfont icon-kaoqin-dingwei"></text>
              <text v-if="attendanceData.group" class="line1">{{
                item.on.location_status > 0 ? item.on.address : attendanceData.group.address
              }}</text>
              <text v-else class="line1">{{ item.on.address }}</text>
            </view>
          </view>
        </view>
        <view class="stage content-box mb10">
          <view class="line"></view>
          <view class="content">
            <view class="adr" v-if="item.on && item.on.remark">
              <text class="iconfont icon-kaoqin-beizhu1"></text>
              {{ item.on.remark }}
            </view>
            <view class="imgs" v-if="item.on && item.on.image.length">
              <ImageGallery v-for="(img, i) in item.on.image" :key="i" :imageUrl="img" />
            </view>
            <view class="clock"
              v-if="item.on && ((index == 0 && recordData.length == 1) || (index == 1 && recordData.length == 3))">
              <signInButton :onWork="'on'" :address="address" :type="clockStatus" :clockUpdateTime="clockUpdateTime"
                :recordLength="recordData.length" :classesStatus="recordData[recordData.length - 1].status"
                :updateStatus="recordData[recordData.length - 1].update_status" :isEffectiveRange="isEffectiveRange"
                :isExternal="!isWhite && attendanceData.group ? attendanceData.group.is_external : 0"
                :isGroup="attendanceData.group !== null ? 1 : 0" @signIn="signIn" @getLocation="getLocation"
                @updateType="updateType" :isWhite="isWhite"></signInButton>
            </view>
          </view>
        </view>
        <view class="stage mb10">
          <view class="time">{{ item.off_hours }}</view>
          <view class="status">
            <view class="status-time">
              <view class="title">下班打卡 {{ item.off ? item.off.clock_time : '' }}
                <view v-if="item.off" class="title">
                  <view v-if="item.off.status == 5 || item.off.status == 7" class="tag lack">缺卡</view>
                  <view v-if="item.off.location_status == 1" class="tag out">外勤</view>
                  <view v-if="item.off.status == 2" class="tag be-late">迟到</view>
                  <view v-if="item.off.status == 4 || item.off.status == 6" class="tag be-late">早退 </view>
                  <view v-if="item.off.location_status == 2" class="tag be-add">地点异常</view>
                </view>
              </view>
              <view v-if="item.off">
                <view v-if="item.off.update_status == 1 && (recordData.length == 1 || recordData.length == 3)"
                  class="renew" @click="signIn(index, 'off')">更新打卡
                </view>
                <view
                  v-else-if="(item.off.status > 1 || item.off.is_external == 2) && (index == 0 ? isAfterOffHours1 : isAfterOffHours2)"
                  class="renew" @click="applyModal">异常处理
                </view>
              </view>
            </view>
            <view class="address mt10" v-if="item.off && item.off.address"><text
                class="iconfont icon-kaoqin-dingwei"></text>
              <text v-if="attendanceData.group" class="line1">{{
                item.off.location_status > 0 ? item.off.address : attendanceData.group.address
              }}</text>
              <text v-else class="line2">{{ item.off.address }}</text>
            </view>
          </view>
        </view>
        <view class="stage content-box mb10">
          <view class="line"></view>
          <view class="content">
            <view class="adr" v-if="item.off && item.off.remark">
              <text class="iconfont icon-kaoqin-beizhu1"></text>
              {{ item.off.remark }}
            </view>
            <view class="imgs" v-if="item.off && item.off.image.length">
              <ImageGallery v-for="(img, i) in item.off.image" :key="i" :imageUrl="img" />
            </view>
            <view class="clock"
              v-if="item.off && ((index == 0 && recordData.length == 2) || (index == 1 && recordData.length == 4))">
              <signInButton :onWork="'off'" :address="address" :type="clockStatus" :clockUpdateTime="clockUpdateTime"
                :recordLength="recordData.length" :isEffectiveRange="isEffectiveRange"
                :classesStatus="recordData[recordData.length - 1].status"
                :updateStatus="recordData[recordData.length - 1].update_status"
                :isExternal="!isWhite && attendanceData.group ? attendanceData.group.is_external : 0" @signIn="signIn"
                @getLocation="getLocation" :isGroup="attendanceData.group !== null ? 1 : 0" @updateType="updateType"
                :isWhite="isWhite"></signInButton>
            </view>
          </view>
        </view>
      </view>
    </view>
    <bottom-navigation :type="4" page-path="/pages/attendance/index"></bottom-navigation>
    <!-- 打卡成功/失败 -->
    <uni-popup ref="refsRemindPopup" type="center" :is-mask-click="false">
      <signInModal :clockTime="clockTime" :type="signInType" @ok="signInOk" :onWork="onWork"></signInModal>
    </uni-popup>
    <!-- 申请单 -->
    <uni-popup ref="refsMenuPopup" type="bottom" background-color="#fff" :is-mask-click="false">
      <applyForMenuModal :dateVal="nowDate" @cancel="applyCancel"></applyForMenuModal>
    </uni-popup>
    <!-- 外勤打卡 -->
    <uni-popup ref="refsExternalPopup" type="center" :is-mask-click="false">
      <externalWorkModal :isEffectiveRange="isEffectiveRange"
        :isText="!isWhite && attendanceData.group ? attendanceData.group.is_external_note : 0"
        :isPic="!isWhite && attendanceData.group ? attendanceData.group.is_external_photo : 0" :address="address"
        @cancel="externalCancel" @ok="externalOk" :onWork="onWork" :recordLength="recordData.length">
      </externalWorkModal>
    </uni-popup>
  </view>
</template>

<script setup>
  import bottomNavigation from '@/components/bottomNavigation/index.vue'
  import signInModal from './components/signInModal.vue'
  import applyForMenuModal from './components/applyForMenuModal.vue'
  import externalWorkModal from './components/externalWorkModal.vue'
  import signInButton from './components/signInButton.vue'
  import ImageGallery from '@/components/ImageGallery/index.vue'
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import { ref, onMounted, onBeforeUnmount, computed } from 'vue'
  import { toLogin } from '@/libs/login'
  import { useStore } from 'vuex'
  import { getDistance } from '@/utils/helper'
  import { attendanceClockIn, attendanceClockRecord, attendanceBasic } from '@/api/attendance'
  const store = useStore()
  const userInfo = ref(store.state.app.userInfo)
  const isLogin = computed(() => store.state.app.isLogin)

  const loading = ref(true)
  const address = ref() //当前地址信息
  const clockStatus = ref(0)
  const clockTime = ref()
  const clockUpdateTime = ref() // 当前状态更新时间
  const attendanceData = ref() // 规则数据
  let recordData = ref([]) // 打卡记录
  let abnormal = ref()
  let basicData = ref([]) // 规则
  let nowXy = ref() // 当前坐标
  let rangeXy = ref() // 考勤规则坐标
  let effectiveRange = ref() // 考勤距离范围 例如 1000 就是1000米范围内
  let isEffectiveRange = ref(false) // 是否在考勤范围内
  let imgs = ref([])
  let remark = ref()
  let onWork = ref('on') //是否上班 on上班 off下班
  let isWhite = ref(false)
  let updateNumber = ref('')
  let shift = ref('')
  let is_external = ref(0)
  let nowDate = ref(`${new Date().getFullYear()}-${new Date().getMonth() + 1}-${new Date().getDate()}`)
  let isAfterOffHours1 = ref(false)
  let isAfterOffHours2 = ref(false)
  onLoad(() => {
    if (!isLogin.value) {
      toLogin()
    } else {
      getBasic()
    }
  })
  const getLocation = () => {
    uni.getLocation({
      type: 'gcj02',
      // geocode: true,
      isHighAccuracy: true,
      success: (res) => {
        nowXy.value = {
          longitude: res.longitude,
          latitude: res.latitude,
        }
        let distance = getDistance(nowXy.value, rangeXy.value)
        if (distance <= effectiveRange.value) {
          isEffectiveRange.value = true
        } else {
          isEffectiveRange.value = false
        }
        turnAdrr(nowXy.value.longitude, nowXy.value.latitude)
      },
      fail: (err) => {
        if (err.errMsg === 'getLocation:fail Geolocation permission denied') {
          console.log('用户拒绝了位置权限请求')
          uni.showToast({
            title: '用户拒绝了位置权限请求',
            icon: 'none',
          })
          // 在这里可以提示用户授予位置权限
        } else {
          console.log('获取位置信息失败：', err.errMsg)
          uni.showToast({
            title: '获取位置信息失败',
            icon: 'none',
          })
        }
      },
    })
  }
  const getClockRecord = () => {
    attendanceClockRecord().then((res) => {
      recordData.value = res.data.list
      abnormal.value = res.data.abnormal
      if (recordData.value.length == 1 || recordData.value.length == 3) {
        onWork.value = 'on'
      } else {
        onWork.value = 'off'
      }
      // for (let i = 0; i < basicData.value.length; i++) {
      //   console.log(i * 2, res.data.list[0]);
      //   basicData.value[i].on = res.data.list[i * 2];
      //   basicData.value[i].off = res.data.list[i * 2 + 1];
      // }
      if (basicData.value.length) {
        for (let i = 0; i < basicData.value.length; i++) {
          basicData.value[i].on = res.data.list[i * 2]
          basicData.value[i].off = res.data.list[i * 2 + 1]
        }
      } else if (!basicData.value.length && recordData.value.length) {
        basicData.value =
          recordData.value.length > 2 ? [{
              work_hours: isWhite.value ? '' : '-',
              off_hours: isWhite.value ? '' : '-',
            },
            {
              work_hours: isWhite.value ? '' : '-',
              off_hours: isWhite.value ? '' : '-',
            },
          ] : [{
            work_hours: isWhite.value ? '' : '-',
            off_hours: isWhite.value ? '' : '-',
          }, ]
        for (let i = 0; i < basicData.value.length; i++) {
          basicData.value[i].on = res.data.list[i * 2]
          basicData.value[i].off = res.data.list[i * 2 + 1]
        }
      } else {
        noTing.value = true
      }
      clockUpdateTime.value = res.data.clock_timestamp
      clockStatus.value = res.data.clock_status
      loading.value = false
    })
  }
  const getBasic = async () => {
    try {
      loading.value = true
      const res = await attendanceBasic()
      const data = res.data
      attendanceData.value = data
      shift.value = data.shift

      // 获取当前时间
      const currentTime = new Date()

      let offHoursString1 = ''
      let offHoursString2 = ''

      if (data && data.shift && data.shift.number && data.shift.rules) {
        // 获取下班时间
        offHoursString1 = data.shift.rules[0].off_hours
        if (data.shift.number == 2) {
          offHoursString2 = data.shift.rules[1].off_hours
        }
      }

      // 将下班时间分割为小时和分钟
      const offHoursArray1 = offHoursString1.split(':')
      const offHoursArray2 = offHoursString2.split(':')

      // 创建下班时间的Date对象
      const offHours1 = new Date()
      const offHours2 = new Date()

      offHours1.setHours(parseInt(offHoursArray1[0], 10))
      offHours1.setMinutes(parseInt(offHoursArray1[1], 10))
      offHours1.setSeconds(0)

      offHours2.setHours(parseInt(offHoursArray2[0], 10))
      offHours2.setMinutes(parseInt(offHoursArray2[1], 10))
      offHours2.setSeconds(0)
      // 判断选中日期是否为当日
      let selectedDate = new Date(nowDate.value).getDate()
      let currentDate = currentTime.getDate()
      if (currentDate > selectedDate) {
        isAfterOffHours1.value = true
      } else {
        // 检查当前时间是否超过下班时间
        isAfterOffHours1.value = currentTime > offHours1
        isAfterOffHours2.value = currentTime > offHours2
      }
      basicData.value = data.shift ? data.shift.rules : ''
      effectiveRange.value = data.group ? data.group.effective_range : ''
      isWhite.value = data.whitelist ? data.whitelist : false

      // 设置坐标范围
      rangeXy.value = {
        latitude: data.group ? data.group.lat : '',
        longitude: data.group ? data.group.lng : '',
      }

      // 获取位置信息
      getLocation()
      // 获取打卡记录
      getClockRecord()
      loading.value = false
    } catch (error) {
      loading.value = false
    }
  }
  // 当前状态时间段结束
  const updateType = () => {
    // getBasic();
  }
  // 转地址
  function turnAdrr(longs, lat) {
    // 高德逆地理变码 https://lbs.amap.com/api/webservice/guide/api/georegeo/
    let _key = 'ed1a4be1ee886c9ae5f13e900f0690c3' //高德API key类型：web服务
    uni.request({
      method: 'GET',
      url: 'https://restapi.amap.com/v3/geocode/regeo',
      data: {
        key: _key,
        location: `${longs},${lat}`,
        output: 'JSON',
        radius: 1000,
        extensions: 'base',
        pois: 0,
      },
      success: (res) => {
        const resData = res.data
        address.value = resData.regeocode.formatted_address
      },
      fail: (r) => {},
    })
  }

  // 打卡成功
  const modal = ref(false)
  const signInType = ref(1) // 打卡状态  0失败 1成功
  const refsRemindPopup = ref(null) // 签到提醒

  // index 为班次  type 为on上班  off下班
  const signIn = (index, type, is_update) => {
    onWork.value = type || onWork.value
    if (index !== undefined) {
      // 根据传入的index和type设置updateNumber.value
      if (index === 0) {
        updateNumber.value = type === 'on' ? 0 : 1
      } else if (index === 1) {
        updateNumber.value = type === 'on' ? 2 : 3
      }
    } else {
      updateNumber.value = ''
    }
    if (
      (clockStatus.value === 1 && !isEffectiveRange.value && attendanceData.value.group && attendanceData.value.group
        .is_external) ||
      (clockStatus.value === 4 && isEffectiveRange.value) ||
      (attendanceData.value.group && (attendanceData.value.group.is_photo || attendanceData.value.group
        .is_external)) ||
      isWhite.value ||
      !attendanceData.value.group
    ) {
      externalModal()
    } else {
      submitData(updateNumber.value)
    }
  }
  const submitData = (isUpdate) => {
    if (!nowXy.value) {
      uni.showToast({
        title: '请先获取您的位置信息',
        icon: 'none',
      })
      return
    }
    if (attendanceData.value.group == null) {
      is_external.value = 0
    }
    const data = {
      lat: nowXy.value.latitude,
      lng: nowXy.value.longitude,
      address: address.value,
      remark: remark.value,
      image: imgs.value,
      is_external: is_external.value,
      update_number: isUpdate,
    }
    attendanceClockIn(data)
      .then((res) => {
        clockTime.value = res.data.clock_time
        signInType.value = recordData.value.length
        refsRemindPopup.value.open()
      })
      .catch((err) => {
        uni.showToast({
          title: err.message,
          icon: 'none',
        })
      })
  }
  const signInOk = () => {
    getBasic()
    refsRemindPopup.value.close()
  }
  // 异常处理 申请
  const refsMenuPopup = ref(null) // 申请

  const applyModal = () => {
    refsMenuPopup.value.open()
  }
  const applyCancel = () => {
    refsMenuPopup.value.close()
  }
  // 补卡/早退
  const refsExternalPopup = ref(null) // 申请
  const externalModal = () => {
    refsExternalPopup.value.open()
  }
  const externalOk = (e, index, type) => {
    is_external.value = e.is_external
    imgs.value = e.imgs
    remark.value = e.text
    if (index == 0) {
      updateNumber.value = type === 'on' ? 0 : 1
    } else if (index == 1) {
      updateNumber.value = type === 'on' ? 2 : 3
    }
    refsExternalPopup.value.close()
    submitData(updateNumber.value)
  }
  const externalCancel = () => {
    refsExternalPopup.value.close()
  }
  const back = () => {
    uni.switchTab({
      url: '/pages/index/index',
    })
  }
</script>

<style lang="scss" scoped>
  .bar {
    position: absolute;
    left: 0;
    width: 100%;
    top: 88rpx;
  }

  .header {
    background: linear-gradient(180deg, #308bf8 0%, #f0f1f5 100%);
    padding-top: 20rpx;

    .user {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin: 0 20rpx;

      .user-msg {
        display: flex;
        align-items: center;

        .avatar {
          width: 80rpx;
          height: 80rpx;
          border-radius: 8rpx;
          margin-right: 22rpx;
        }

        .name {
          font-size: 30rpx;
          font-weight: 500;
          color: #303133;
          line-height: 30rpx;
          margin-bottom: 18rpx;
        }

        .position {
          font-size: 24rpx;
          font-weight: 400;
          color: #909399;
          line-height: 24rpx;
        }
      }

      .rule {
        font-size: 24rpx;
        font-weight: 400;
        color: #308bf8;
        line-height: 24rpx;
      }
    }
  }

  .card.ab-condition {
    margin-bottom: 20rpx;
  }

  .ab-condition {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .text {
      font-size: 28rpx;
      font-weight: 400;
      color: #303133;
    }

    .navigator-wrap {
      display: flex;
      align-items: center;
    }

    .nav-right {
      display: flex;
      align-items: center;

      .days {
        font-size: 24rpx;
        line-height: 24rpx;
        font-weight: 400;
        color: #606266;
        margin-right: 12rpx;
      }

      .icon-jinru-copy {
        color: #c0c4cc;
        font-size: 24rpx;
        line-height: 24rpx;
      }
    }
  }

  .card.attendance-card {
    padding: 40rpx 24rpx 38rpx 24rpx;

    .stage {
      display: flex;
      align-items: center;

      .time {
        font-size: 32rpx;
        font-weight: 500;
        color: #303133;
        line-height: 32rpx;
        margin-right: 24rpx;
        width: 88rpx;
        text-align: center;
      }

      .status {
        flex: 1;

        .status-time {
          display: flex;
          align-items: center;
          justify-content: space-between;

          .title {
            font-size: 30rpx;
            font-weight: 500;
            color: #303133;
            display: flex;
            align-items: center;

            .tag {
              font-size: 20rpx;
              font-weight: 400;
              line-height: 20rpx;
              padding: 6rpx;
              border-radius: 2px;
              opacity: 1;
              margin-left: 10rpx;
              transform: scale(0.9);
            }

            .lack {
              color: #ed4014;
              border: 1rpx solid #ed4014;
            }

            .out {
              color: #19be6b;
              border: 1rpx solid #19be6b;
            }

            .be-late {
              color: #ff9900;
              border: 1rpx solid #ff9900;
            }

            .be-add {
              color: #19be6b;
              border: 1rpx solid #19be6b;
            }
          }

          .renew {
            font-size: 24rpx;
            font-weight: 400;
            color: #308bf8;
            line-height: 24rpx;
          }
        }

        .address {
          font-size: 24rpx;
          font-weight: 400;
          color: #909399;
          line-height: 26rpx;
          margin-top: 16rpx;
          max-width: 550rpx;
          display: flex;
          align-items: center;

          .icon-kaoqin-dingwei {
            font-size: 24rpx;
            margin-right: 2rpx;
          }
        }
      }

      .line {
        width: 4rpx;
        background-color: #eeeeee;
      }

      .none {
        display: none;
      }

      .content {
        flex: 1;
        width: auto;
        min-height: 74rpx;
        padding-left: 62rpx;
      }
    }

    .content-box {
      align-items: stretch;
      padding-left: 48rpx;

      .content {
        .adr {
          font-size: 24rpx;
          font-weight: 400;
          color: #909399;
          line-height: 24rpx;

          .icon-kaoqin-beizhu1 {
            font-size: 20rpx;
          }
        }

        .imgs {
          margin-top: 28rpx;
          width: 100rpx;
          height: 100rpx;
          border-radius: 9rpx;
          margin-right: 12rpx;
          margin-bottom: 12rpx;
        }

        .clock {
          margin-left: -104rpx;
          padding: 82rpx 0 60rpx;
        }
      }
    }

    .clock-end {
      padding: 82rpx 0 60rpx;
    }
  }

  .card {
    background-color: #fff;
    padding: 28rpx 24rpx;
    border-radius: 12rpx;
    margin: 0 20rpx;
  }
</style>