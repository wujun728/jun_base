<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="calendar-header box-shadow">
        <view class="flex-header">
          <view @click="cancel" class="iconfont icon-fanhui bar-return back"></view>
          <view>{{selectTime}} </view>
          <view class="set-schedule iconfont icon-richengshezhi" @click="clickSetsChedule"></view>
        </view>
        <scroll-view scroll-x="true">
          <view class="swiper-box ">
            <view class="allday-text" v-if="data.allDayData.length>0">
              全天
            </view>
            <swiper class="swiper " :style="[{ height: cHeight+'px'},{ transition: 'all 0.2s  linear' }]"
              :circular="false" :acceleration="true" :duration="1000" :indicator-dots="false" :autoplay="false"
              easing-function="easeInCubic" :current="currentIndex" :display-multiple-items="3" @change="handleChange">
              <swiper-item v-for="(item,index) in data.timeData" :key="index" class="swiper-item">
                <view class="title" :class="index === currentIndex ? 'active' : ''">
                  <view class="top">
                    {{item.today}}
                  </view>
                  <view class="bottom">{{item.days.split('-')[2]}}</view>
                </view>
                <!-- 全天展示 -->
                <view class="flex all-day " v-if="data.allDayData.length>0 ">
                  <view v-for="(val,indexj) in data.allDayData">
                    <view class="day" :key="val.id" v-if="index==getIndex(val)"
                      :style="{ borderLeftColor: val.color,background:getColor( val.color, '0.1'),width:getWidth(val,item)}"
                      @click="handleDetail(val)">
                      <view class="over-text">{{val.title}} </view>
                      <view v-if="userInfo.userId!==val.master.id" @click.stop="scheduleRecord(val)">
                        <template v-if="val.finish != 2">
                          <text class="iconfont icon-gouxuan-weigouxuan"
                            v-if="val.finish === 0 || val.finish === 1 || val.finish === -1"></text>
                          <text class="iconfont icon-denglu-tongyi" v-if="val.finish === 3"></text>
                        </template>
                        <template v-else>
                          <image class="image-item" src="@/static/image/schedule03.png" mode=""></image>
                        </template>
                      </view>
                    </view>
                  </view>
                </view>
              </swiper-item>
            </swiper>
          </view>
        </scroll-view>
      </view>
    </view>

    <!-- 日程内容 -->
    <view class="schedule-box flex" :style="{'--height':cHeight+'px' }" @touchstart="start" @touchend="end"
      @touchmove="move">
      <nl-schedule :type="3" :list="data.listData" @detail="handleDetail" @add="handleAdd($event,0)"></nl-schedule>
      <nl-schedule :type="3" :list="data.listData2" :scheduleHeaderShow="false" :showCurrentTimeLine="false"
        @add="handleAdd($event,1)"></nl-schedule>
      <nl-schedule :type="3" :list="data.listData3" :scheduleHeaderShow="false" :showCurrentTimeLine="false"
        @add="handleAdd($event,2)"></nl-schedule>
    </view>
    <!-- 新增 -->
    <view class="add">
      <text class="iconfont icon-xuanfuanniu-jia" @click="handleAdd('',0)"></text>
    </view>
  </view>
</template>

<script setup>
  import moment from 'moment'
  import {
    ref,
    reactive,
    computed,
    getCurrentInstance,
    onMounted,
  } from "vue"
  import { useStore } from 'vuex'
  import { scheduleListApi } from '@/api/user'
  import { onPageScroll } from '@dcloudio/uni-app'
  import message from '@/utils/message'
  import { dayCycleArray, getColor } from '@/utils/helper'
  import { clickNavigateTo } from '@/utils/helper'
  const store = useStore()
  const userInfo = computed(() => store.state.app.userInfo)
  const cHeight = ref(53)
  const instance = getCurrentInstance() // 获取组件实例
  const currentIndex = ref(3)
  const props = defineProps({
    // 自定义导航栏列表与defaultType为1时，同时使用
    checkedTypes: {
      type: Object,
      default: () => {
        return []
      }
    },
    typeData: {
      type: Object,
      default: () => {
        return []
      }
    }
  })
  const { checkedTypes, typeData } = toRefs(props)
  const time = ref(moment(new Date()).format('YYYY-MM-DD'))
  const selectTime = ref(moment(new Date()).format('YYYY年MM月'))
  const data = reactive({
    timeData: [],
    height: '',
    listData: [], // 日程列表
    listData2: [],
    listData3: [],
    allDayData: [], // 全天展示列表
    windowWidth: 98,
    startData: {
      clientX: '',
      clientY: '',
    },
    moveX: 0,
    touch: {},
  })
  onMounted(() => {
    // 获取跨天日程渲染的长度
    uni.getSystemInfo({
      success: function(res) {
        data.windowWidth = (res.windowWidth - 63) / 3 - 6
      },
    })
    getTimeDataAll(time.value)
    setTimeout(() => {
      getScheduleList()
    }, 300)

    uni.pageScrollTo({
      scrollTop: 400,
      duration: 0,
    })
  })



  // 动态计算轮播图高度
  const getCurrentSwiperHeight = (element) => {
    let query = uni.createSelectorQuery().in(this);
    query.selectAll(element).boundingClientRect();
    query.exec((res) => {
      if (res[0].length > 0) {
        let currentHeightArr = [res[0][currentIndex.value].height, res[0][currentIndex.value + 1].height, res[0][
          currentIndex.value + 2
        ].height]
        cHeight.value = 65 + Math.max(...currentHeightArr)
        data.height = cHeight.value + 53 + 'px'
      } else {
        cHeight.value = 65
        data.height = cHeight.value + 53 + 'px'
      }
    })
  }

  // 获取日程列表
  const getScheduleList = () => {
    uni.showLoading({
      title: '加载中'
    })
    data.allDayData = []
    let sortedArray = []
    let time = data.timeData[currentIndex.value].days
    let endtime = data.timeData[currentIndex.value + 2].days
    const data_s = {
      cid: checkedTypes.value,
      start_time: `${time} 00:00:00`,
      end_time: `${endtime} 23:59:59`,
      period: 3
    }
    data.listData = []
    data.listData2 = []
    data.listData3 = []
    scheduleListApi(data_s).then(res => {
      res.data.forEach((item, index) => {
        if (item.all_day == 1) {
          sortedArray.push(item)
        } else if (moment(item.start_time).format('YYYY-MM-DD') !== moment(item.end_time).format(
            'YYYY-MM-DD')) {
          sortedArray.push(item)
        } else {
          item.startHour = moment(item.start_time).format('HH:mm')
          item.endHour = moment(item.end_time).format('HH:mm')
        }
      })

      res.data.map(item => {
        if (moment(item.start_time).format('YYYY-MM-DD') == data.timeData[currentIndex.value].days) {
          data.listData.push(item)
        } else if (moment(item.start_time).format('YYYY-MM-DD') == data.timeData[currentIndex.value + 1]
          .days) {
          data.listData2.push(item)
        } else if (moment(item.start_time).format('YYYY-MM-DD') == data.timeData[currentIndex.value + 2]
          .days) {
          data.listData3.push(item)
        }
      })

      // 全天日程排序
      data.allDayData = sortedArray.sort((a, b) => {
        let indexa = getIndex(a)
        let indexb = getIndex(b)
        let widtha = getWidth(a, data.timeData[currentIndex.value])
        let widthb = getWidth(b, data.timeData[currentIndex.value])
        return indexa > indexb && widtha < widthb ? 1 : -1
      })
      setTimeout(() => {
        getCurrentSwiperHeight('.all-day')
        uni.hideLoading()
      }, 500)

    }).catch((error) => {
      uni.hideLoading()
      message.error(error.message)
    })
  }

  // 获取全天数据从那个时间开始展示
  const getIndex = (val) => {
    let index = currentIndex.value
    let time = moment(val.start_time).format('yyyy-MM-DD')
    let firstDay = moment(data.timeData[index].days).format('yyyy-MM-DD')
    let secondDay = moment(data.timeData[index + 1].days).format('yyyy-MM-DD')
    let thirdDay = moment(data.timeData[index + 2].days).format('yyyy-MM-DD')
    if (time <= firstDay) {
      return index
    } else if (time == secondDay) {
      return index + 1
    } else if (time == thirdDay) {
      return index + 2
    }
  }

  // 获取全天数据的展示长度
  const getWidth = (val, item) => {
    let width = 0
    let start = moment(val.start_time).format('yyyy-MM-DD')
    let end = moment(val.end_time).format('yyyy-MM-DD')
    let time = item.days
    if (start == time && end == time) {
      width = data.windowWidth + 'px'
      return width
    } else if (start == time && end > time) {
      width = data.windowWidth * 2 + 'px'
      return width
    } else if (start < time && end > time) {
      width = data.windowWidth * 3 + 'px'
      return width
    }

  }

  // 查看日程详情
  const handleDetail = (e) => {
    clickNavigateTo(`/pages/users/schedule/detail?id=${e.id}&start=${e.start_time}&end=${e.end_time}`)
  }

  // 修改日程状态
  const scheduleRecord = (item) => {
    emit('scheduleRecord', item)
    getScheduleList(0)
  }

  // 新建日程
  const handleAdd = (e, val) => {

    let time = data.timeData[currentIndex.value + val].days
    let obj = {}
    if (e) {
      obj = {
        start_time: time + ' ' + e.startHour + ':' + '00',
        end_time: time + ' ' + e.endHour + ':' + '00'
      }
    } else {
      obj = {
        start_time: time + ' ' + moment().format('HH:mm:ss'),
        end_time: time + ' ' + moment().add(1, 'hours').format('HH:mm:ss')
      }
    }
    clickNavigateTo(`/pages/users/schedule/create?time=${ JSON.stringify(obj)}`)
  }

  const emit = defineEmits(["clickSetsCheduleFn", "scheduleRecord"]);
  const clickSetsChedule = () => {
    emit('clickSetsCheduleFn')
  }


  // 日期滑动触发
  const handleChange = (e) => {
    const current = e.detail.current
    selectTime.value = moment(data.timeData[e.detail.current].days).format('YYYY年MM月')
    if (e.detail.current < currentIndex.value) {
      let time = moment(data.timeData[0].days).add(-3, 'day').format('yyyy-MM-DD')
      let d = getInfoTime(time)
      data.timeData.unshift(...d)
      currentIndex.value = current + 4
      getScheduleList(0)

    }
    if (e.detail.current > currentIndex.value) {
      let time = moment(data.timeData[data.timeData.length - 1].days).add(1, 'day').format('yyyy-MM-DD')
      let d = getInfoTime(time)
      data.timeData.push(...d)
      currentIndex.value = current
      getScheduleList(0)
    }
  }

  // 根据某个日期获取最近三天是日期集合
  const getInfoTime = (day) => {
    let dayArray = []
    let days = {}
    for (let i = 0; i < 3; i++) {
      dayArray.push({
        today: dayCycleArray[moment(day).add(i, 'day').day()],
        days: moment(day).add(i, 'day').format('yyyy-MM-DD'),
        list: []
      });
    }
    return dayArray
  }

  // 初始化日期数据
  const getTimeDataAll = (time) => {
    let arr = []
    const nDay = moment(time).add(3, 'day').format('yyyy-MM-DD')
    let nD = getInfoTime(nDay)
    const pDay = moment(time).add(-3, 'day').format('yyyy-MM-DD')
    let pD = getInfoTime(pDay)
    let d = getInfoTime(time)
    arr = [...pD, ...d, ...nD]
    data.timeData = arr
  }

  // 手指开始触摸
  const start = (e) => { //@touchstart 触摸开始
    data.startData.clientX = e.changedTouches[0].clientX; //手指按下时的X坐标         
    data.startData.clientY = e.changedTouches[0].clientY; //手指按下时的Y坐标
  }
  // 手指结束触摸
  const end = (e) => { //@touchstart 触摸开始
    let index = 0
    if (data.moveX < -40) {
      index = currentIndex.value + 1
      handleChange({ detail: { current: index } })
    } else if (data.moveX > 40) {
      index = currentIndex.value - 2
      handleChange({ detail: { current: index } })
    }
  }

  const move = (event) => { //@touchmove触摸移动
    let touch = event.touches[0]; //滑动过程中，手指滑动的坐标信息 返回的是Objcet对象
    data.touch = touch;
    let val = touch.clientX - data.startData.clientX;
    data.moveX = val
  }
</script>
<style scoped lang="scss">
  .content {
    width: 100%;
    position: relative;

    .cr-position-header {
      position: fixed;
      padding-top: var(--status-bar-height);
      background: linear-gradient(90deg, #459fff 0%, #388aef 100%, #3384e7 100%);


      .back,
      .set-schedule {
        z-index: 2;
        height: 44px;
        display: flex;
        align-items: center;
        color: #FFFFFF;
      }

      .calendar-header {
        .flex-header {
          padding: 0 20px;
          height: 53px;
          width: 100%;
          color: #FFFFFF;
          font-size: 17px;
          background-color: #1890FF;
          display: flex;
          justify-content: space-between;
          align-items: center;

          .icon-richengshezhi {
            font-size: 18px;
          }
        }
      }
    }

    .flex {
      display: flex;
      flex-wrap: wrap;
    }

    .box-shadow {
      box-shadow: 0px 2px 4px 0px rgba(0, 0, 0, 0.06) !important;
    }

    .swiper-box {
      background-color: #FFFFFF;
      position: relative;
      width: 100%;
      padding-left: 53px;
      padding-right: 10px;
      box-shadow: 0px 2px 4px 0px rgba(0, 0, 0, 0.0147);

      .allday-text {
        position: absolute;
        left: 10px;
        top: 56px;
        font-family: PingFang SC, PingFang SC;
        font-weight: 500;
        font-size: 12px;
        color: #9E9E9E;
      }

      .swiper {
        overflow-y: scroll;

        ::v-deep uni-swiper-item {
          margin-top: 10px;
          overflow: visible;
        }
      }

      .mt12 {
        margin-top: 12px;
      }

      .title {
        margin-bottom: 12px;

        &.active {

          .top,
          .bottom {
            color: $uni-color-primary;
          }
        }

        .top {
          font-family: PingFang SC, PingFang SC;
          font-weight: 400;
          font-size: 10px;
          color: #909399;
        }

        .bottom {
          font-family: PingFang SC, PingFang SC;
          font-weight: 500;
          font-size: 18px;
          color: #303133;
        }
      }
    }

    .swiper {
      width: 100%;
      height: 100%;
    }
  }

  .add {
    position: fixed;
    right: 20rpx;
    bottom: 350rpx;
    width: 44px;
    height: 44px;
    background: linear-gradient(135deg, #47B5FF 0%, #0F86F5 100%);
    box-shadow: 0px 4px 4px 0px rgba(28, 146, 248, 0.1145);
    border-radius: 50%;
    text-align: center;
    line-height: 44px;
    color: #fff;

    .icon-xuanfuanniu-jia {
      font-size: 15px;
    }
  }

  .day {
    height: 44rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    margin-bottom: 4rpx;
    padding-left: 14rpx;
    padding-right: 4px;

    line-height: 44rpx;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 12px;
    color: #303133;
    box-sizing: border-box;
    border-left: 2px solid;
    font-size: 13px;
    border-radius: 4px;

    .iconfont {
      font-size: 34rpx;
      color: $uni-color-primary;
    }

    .icon-gouxuan-weigouxuan {
      color: #FFFFFF;
    }
  }

  .schedule-box {
    padding-top: calc($uni-default-bar-height + var(--status-bar-height) + var(--height));
  }

  .schedule-box .schedule-container:nth-child(1) {
    width: 38%;
    margin-right: 6px;
  }

  ::v-deep .schedule-content {
    padding-right: 0;
  }

  .schedule-box .schedule-container:nth-child(2) {
    width: calc((100% - 50px) / 3);
    margin-right: 6px;
  }

  .schedule-box .schedule-container:nth-child(3) {
    width: calc((100% - 50px) / 3);
  }

  ::v-deep .schedule-content .add-wrap {
    z-index: 2;
    width: calc(100% / 3 + 50px);
  }

  ::v-deep .schedule-content .current-time-line {
    width: 75%;
  }

  .putItAway {
    ::v-deep.swiper {
      height: 90px;
      overflow: hidden;
      animation: fadenum 0.7s;
      animation-fill-mode: forwards;
    }
  }

  .fadenum {
    ::v-deep.swiper {
      height: 100%;
      animation: fadenum1 0.7s;
      animation-fill-mode: forwards;
    }

  }

  @keyframes fadenum1 {
    0% {
      height: var(--height);
    }

    100% {
      height: 53px;
    }
  }

  @keyframes fadenum {
    0% {
      height: 53px;
    }

    100% {
      height: var(--height);
    }
  }
</style>