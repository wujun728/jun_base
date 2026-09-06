<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right="true" :defaultTitle="selectTime" :right-data="data.rightIcon"
        :backgroundColor="`#1890FF`" :color="`#fff`" @handleNarItem="clickSetsChedule"></default-nav-bar>
    </view>
    <!-- 周内容 -->
    <view class="swiper-box" @touchstart="start" @touchend="end" ref="scrollWrapperRef" @scroll="handleScroll">
      <!-- <uni-load-more status="loading"></uni-load-more> -->
      <view class="swiper-item" :style="{height:data.windowHeight+'px' }" v-for="(item,index) in data.timeData"
        :key="index" @click="handleAdd(item)">
        <view class='month' v-if="item.days.split('-')[2]=='01'">
          {{data.monthList[item.days.split('-')[1]]}}
        </view>
        <view class="left" :class="item.days==moment(new Date()).format('YYYY-MM-DD')?'active':''">
          <view class="bottom">{{item.days.split('-')[2]}}</view>
          <view class="top">{{item.today}}</view>
        </view>
        <view class="right">
          <view class="right-item" v-for="(val,index) in item.list" :key="index+'val'"
            :style="{ borderTopColor: val.color,background:getColor( val.color, '0.1') }"
            @click.native.stop="handleDetail(val)">
            <view class="title over-text3">{{val.title}}</view>
            <view class="time">
              <view>{{val.all_day==1?'全天': moment(val.start_time).format('HH:mm')}}
              </view>
              <view v-if="userInfo.userId!==val.master.id" @click.stop="scheduleRecord(val)">
                <template v-if="val.finish != 2">
                  <text class="icon-no-check" v-if="val.finish === 0 || val.finish === 1 || val.finish === -1"></text>
                  <text class="iconfont icon-denglu-tongyi" v-if="val.finish === 3"></text>
                </template>
                <template v-else>
                  <image class="image-item" src="@/static/image/schedule03.png" mode=""></image>
                </template>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 新增 -->
    <view class="add">
      <text class="iconfont icon-xuanfuanniu-jia" @click.stop="handleAdd()"></text>
    </view>
  </view>
</template>
<script setup>
  import { ref, reactive, computed, onMounted } from "vue"
  import { onReachBottom, onPullDownRefresh, onPageScroll } from '@dcloudio/uni-app'
  import { dayCycleArray, getColor, clickNavigateTo } from '@/utils/helper'
  import { scheduleListApi } from '@/api/user'
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import { useStore } from 'vuex'
  const store = useStore()
  const userInfo = computed(() => store.state.app.userInfo)

  import moment from 'moment'
  import message from '@/utils/message'
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
  const scrollWrapperRef = ref(null)
  const { checkedTypes, typeData } = toRefs(props)
  const selectTime = ref(moment(new Date()).format('YYYY年MM月'))
  const time = ref(moment(new Date()).format('YYYY-MM-DD')) // 当前时间
  const data = reactive({
    rightIcon: [{ type: 1, icon: 'icon-richengshezhi' }],
    timeData: [],
    startData: {
      clientX: '',
      clientY: '',
    },
    moveY: 0,
    touch: {},
    monthList: {
      '01': '一月',
      '02': '二月',
      '03': '三月',
      '04': '四月',
      '05': '五月',
      '06': '六月',
      '07': '七月',
      '08': '八月',
      '09': '九月',
      '10': '十月',
      '11': '十一月',
      '12': '十二月',
    },
    windowHeight: 0,
  })
  onMounted(() => {
    // 获取屏幕高度，均分七份
    uni.getSystemInfo({
      success: function(res) {
        data.windowHeight = (res.windowHeight - 44) / 7
      },
    })
    getTimeDataAll(time.value)
  })

  // 上拉加载
  // onReachBottom(() => {
  //   const nDay = moment(data.timeData[data.timeData.length - 1].days).add(1, 'day').format('yyyy-MM-DD')
  //   let d = getInfoTime(nDay)
  //   selectTime.value = moment(nDay).format('YYYY年MM月')
  //   data.timeData.push(...d)
  //   getScheduleList(nDay, moment(nDay).add(7, 'day').format('yyyy-MM-DD'))
  // })

  // 页面滚动事件
  onPageScroll((e) => {
    let index = Math.round(e.scrollTop / data.windowHeight)
    const nDay = moment(data.timeData[index].days).format('yyyy-MM-DD')
    selectTime.value = moment(nDay).format('YYYY年MM月')
    if (e.scrollTop == 0) {
      const nDay = moment(data.timeData[0].days).add(-7, 'day').format('yyyy-MM-DD')
      let d = getInfoTime(nDay)
      selectTime.value = moment(nDay).format('YYYY年MM月')
      data.timeData.unshift(...d)
      getScheduleList(nDay, data.timeData[0].days)
    }
  })


  // 手指开始触摸
  const start = (e) => { //@touchstart 触摸开始 
    data.startData.clientY = e.changedTouches[0].clientY; //手指按下时的Y坐标
  }
  // 手指结束触摸
  const end = (e) => { //@touchstart 触摸开始
    let index = 0
    if (e.changedTouches[0].clientY - data.startData.clientY > 40) {
      const nDay = moment(data.timeData[0].days).add(-7, 'day').format('yyyy-MM-DD')
      let d = getInfoTime(nDay)
      selectTime.value = moment(nDay).format('YYYY年MM月')
      data.timeData.unshift(...d)
      getScheduleList(nDay, data.timeData[0].days)
    } else if (data.startData.clientY > e.changedTouches[0].clientY) {
      let nDay = moment(data.timeData[data.timeData.length - 1].days).add(1, 'day').format('yyyy-MM-DD')
      let d = getInfoTime(nDay)
      selectTime.value = moment(nDay).format('YYYY年MM月')
      data.timeData.push(...d)
      getScheduleList(nDay, data.timeData[0].days)
    }
  }

  // const move = (event) => { //@touchmove触摸移动
  //   let touch = event.touches[0]; //滑动过程中，手指滑动的坐标信息 返回的是Objcet对象
  //   data.touch = touch;
  //   let val = touch.clientY - data.startData.clientY;
  //   data.moveY = val
  // }

  // 下拉加载
  // onPullDownRefresh(() => {
  //   const nDay = moment(data.timeData[0].days).add(-7, 'day').format('yyyy-MM-DD')
  //   let d = getInfoTime(nDay)
  //   selectTime.value = moment(nDay).format('YYYY年MM月')
  //   data.timeData.unshift(...d)
  //   getScheduleList(nDay, data.timeData[0].days)
  //   uni.stopPullDownRefresh()
  // })

  const getTimeDataAll = (time) => {
    let arr = []
    const nDay = moment(time).add(7, 'day').format('yyyy-MM-DD')
    setTimeout(() => {
      getScheduleList(time, nDay)
    }, 500)
    let d = getInfoTime(time)
    data.timeData = d
  }
  const emit = defineEmits(["clickSetsCheduleFn", "scheduleRecord"]);
  const clickSetsChedule = () => {
    emit('clickSetsCheduleFn')
  }
  // 修改日程状态
  const scheduleRecord = (item) => {
    emit('scheduleRecord', item)
  }

  // 获取日程列表
  const getScheduleList = (start, end) => {
    uni.showLoading({
      title: '加载中'
    })
    const data_s = {
      cid: checkedTypes.value,
      start_time: `${start} 00:00:00`,
      end_time: `${end} 23:59:59`,
      period: 3
    }
    scheduleListApi(data_s).then(res => {
      data.timeData.forEach(item => {
        res.data.forEach(val => {
          if (item.days >= moment(val.start_time).format('yyyy-MM-DD') && item.days <= moment(val
              .end_time).format('yyyy-MM-DD')) {
            item.list.push(val)
          }
        })
      })
      uni.hideLoading()
    }).catch((error) => {
      message.error(error.message)
      uni.hideLoading()
    })
  }

  // 新建日程
  const handleAdd = (val) => {
    console.log(val)
    let time = ''
    let obj = {}
    if (val) {

      obj = {
        start_time: val.days + ' ' + moment().format('HH:mm:ss'),
        end_time: val.days + ' ' + moment().add(1, 'hours').format('HH:mm:ss')

      }
    } else {
      time = moment(new Date()).format('YYYY-MM-DD')
      obj = {
        start_time: time + ' ' + moment().format('HH:mm:ss'),
        end_time: time + ' ' + moment().add(1, 'hours').format('HH:mm:ss')
      }
    }
    clickNavigateTo(`/pages/users/schedule/create?time=${ JSON.stringify(obj)}`)
  }

  // 查看日程详情
  const handleDetail = (e) => {
    clickNavigateTo(`/pages/users/schedule/detail?id=${e.id}&start=${e.start_time}&end=${e.end_time}`)
  }


  const getInfoTime = (day) => {
    let dayArray = []
    let days = {}
    for (let i = 0; i < 7; i++) {
      dayArray.push({
        today: dayCycleArray[moment(day).add(i, 'day').day()],
        days: moment(day).add(i, 'day').format('yyyy-MM-DD'),
        list: []
      });
    }
    return dayArray
  }
</script>
<style scoped lang="scss">
  .content {
    height: 100%;
    width: 100%;
    position: relative;

    .cr-position-header {
      background-color: #fff;
      position: sticky;
    }

    .swiper-box {
      min-height: 100vh;
      overflow: scoll;
      padding: 0 30rpx;
      display: flex;
      flex-direction: column;

      .swiper-item {
        width: 100%;
        display: flex;
        border-bottom: 1px solid #EBEEF5;
        position: relative;

        .left {
          flex-shrink: 0;
          width: 74rpx;
          height: 100%;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          font-family: PingFang SC, PingFang SC;

          &.active {

            .top,
            .bottom {
              color: $uni-color-primary;
            }
          }

          .bottom {
            font-weight: 500;
            font-size: 36rpx;
            color: #303133;
          }

          .top {
            font-weight: 400;
            font-size: 22rpx;
          }
        }

        .right {
          overflow-x: scroll;
          display: flex;
          flex-wrap: nowrap;
          scrollbar-width: none;
          /* firefox */
          -ms-overflow-style: none;

          /* IE 10+ */
          .right-item {
            box-sizing: border-box;
            flex-shrink: 0;
            width: 76px;
            height: 100%;
            padding: 14rpx 16rpx 10rpx 16rpx;
            margin-left: 1px;
            background: rgba(48, 139, 248, 0.2);
            font-family: PingFang SC, PingFang SC;
            font-weight: 400;
            font-size: 22rpx;
            color: #303133;
            border-top: 4rpx solid #1890FF;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            border-radius: 8rpx;


            .time {
              display: flex;
              justify-content: space-between;
              align-items: center;
              font-weight: 400;
              font-size: 10px;
              color: #909399;

              .iconfont {
                font-size: 34rpx;
                color: $uni-color-primary;
              }

              .icon-no-check {
                display: inline-block;
                width: 32rpx;
                height: 32rpx;
                border-radius: 50%;
                background: #FFFFFF;
                border: 1px solid #EEEEEE;
              }
            }
          }
        }
      }

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

  .month {
    position: absolute;
    top: -9px;
    max-width: 37px;
    height: 17px;
    background: #fff;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 12px;
    color: #1890FF;
  }
</style>