<template>
  <view class="pj-calendar-container">
    <view class="main">
      <view class="week-box">
        <view v-for="item in data.weeks" :key="item">{{ item }}
        </view>
      </view>
      <swiper circular @change="onChangeSwiper" :current="data.current" class="swiper-box"
        :style="{height:data.swiperHeight}">
        <swiper-item class="month-box" v-for="sitem in 3" :key="sitem">
          <view v-for="(item,index) in data.monthDays" :key="index" class="day" @click="onClickDay(item,index)">
            <view class="day-text" :class="[getClass(item,index)]" :style="[getStyle(item)]"> {{ item.day }} <text
                class="no_submit" v-if="item.no_submit==1"></text></view>
            <view class="lunar-text" :style="[getStyle(item)]">
              {{getLunarFn(item.year, item.month, item.day)}}
            </view>
          </view>
        </swiper-item>
      </swiper>
    </view>
  </view>
</template>
<script setup>
  import { getLunar } from "chinese-lunar-calendar";
  import Util from "./util"
  import moment from 'moment'
  import {
    ref,
    reactive,
    onMounted,
    computed
  } from "vue"

  const props = defineProps({
    // 自定义导航栏列表与defaultType为1时，同时使用
    countData: {
      type: Array,
      default: () => {
        return []
      }
    }
  })
  const { countData } = toRefs(props)

  const data = reactive({
    util: null,
    weeks: ['日', '一', '二', '三', '四', '五', '六'],
    monthDays: [],
    select_date: '',
    select_color: '#1890ff',
    current: 0,
    shrink: false,
    index: 0,
    swiperHeight: '90rpx',
    // 日期
    date: {
      year: null,
      month: null,
      selectDay: null
    }
  })

  onMounted(() => {
    init()
    data.util.pack_up = data.shrink
  })

  const init = () => {
    const date = new Date(new Date().getTime())
    data.util = new Util()
    data.date = {
      year: date.getFullYear(),
      month: date.getMonth() + 1,
      selectDay: date.getDate()
    }

    data.util.setBaseDate({
      year: data.date.year,
      month: data.date.month,
      select_day: data.date.selectDay
    })
    data.monthDays = data.util.getData()
    data.index = data.monthDays.findIndex(item => {
      return item.year == data.date.year && item.month == data.date.month && item.day == data.date.selectDay
    })

    setTimeout(() => {
      getDaysCountData()
    }, 400)
  }


  const getDaysCountData = () => {
    data.monthDays.forEach(item => {
      countData.value.forEach(el => {
        if (moment(item.date).format('YYYY-MM-DD') === el.time && el.no_submit > -1) {
          item.no_submit = 1
        }
      })
    })
  }

  // 手动修改轮播图type:1下一个 type：0上一个
  const handleSwiper = (val, type) => {
    if (type === 1) {
      next()
      if (data.current === 2) {
        data.current = 0
      } else {
        data.current = data.current + val
      }

    } else {
      pre()
      if (data.current === 0) {
        data.current = 2
      } else {
        data.current = data.current - val
      }
    }
    const length = data.monthDays.length - 1
    const obj = {
      startTime: moment(data.monthDays[0].date).format('YYYY-MM-DD'),
      endTime: moment(data.monthDays[length].date).format('YYYY-MM-DD'),

    }
    emit('getselectTime', data.monthDays[0].year + '年' + data.monthDays[0].month + '月', obj)
    setTimeout(() => {
      getDaysCountData()
    }, 700)

  }
  defineExpose({ handleSwiper })
  // 根据当前日期获取农历数据
  const getLunarFn = (curYear, curMonth, curDay) => {
    const lunarDate = getLunar(curYear, curMonth, curDay);
    return lunarDate.dateStr.substring(2)
  }
  const onChangeSwiper = (e) => {
    if (!e.detail.source) {
      return false
    }
    const current_swiper = e.detail.current
    if (current_swiper - data.current === 1 || current_swiper - data.current === -2) {
      next()
    } else {
      pre()
    }
    data.current = current_swiper
    const length = data.monthDays.length - 1
    const obj = {
      startTime: moment(data.monthDays[0].date).format('YYYY-MM-DD'),
      endTime: moment(data.monthDays[length].date).format('YYYY-MM-DD'),
      handChange: true
    }
    emit('getselectTime', data.monthDays[0].year + '年' + data.monthDays[0].month + '月', obj)
    emit("onClickDay", data.monthDays[data.index], 1)
    setTimeout(() => {
      getDaysCountData()
    }, 700)
  }

  const next = () => {
    data.monthDays = data.util.getData('next', data.monthDays[data.monthDays.length - 1])
  }

  const pre = () => {
    data.monthDays = data.util.getData('pre', data.monthDays[0])
  }
  const emit = defineEmits(['onClickDay', 'getselectTime']);
  const onClickDay = (item, index) => {
    data.index = index
    const { day, month, year } = item
    data.date.selectDay = day
    data.util.setBaseDate({
      year,
      month,
      select_day: day
    })

    if (data.date.year !== year || data.date.month !== month) {
      data.date.month = month
      data.date.year = year
      data.monthDays = data.util.getData()
    }

    emit("onClickDay", item)
  }
  const getClass = (item, index) => {
    const isSelected = index === data.index
    return {
      'is_selected': isSelected
    }
  }

  const getStyle = (item) => {
    const date = new Date(new Date().getTime())
    const isSelected = item.day === date.getDate() && item.month === date.getMonth() + 1
    return {
      color: isSelected ? '#1890ff' : '',
    }

  }
</script>
<style lang="scss" scoped>
  .pj-calendar-container {
    .main {
      .swiper-box {
        transition: height 0.5s ease 0s;
      }

      .week-box {
        margin: 20rpx 0 10rpx 0;
        font-family: PingFang SC, PingFang SC;
        font-weight: 400;
        font-size: 24rpx;
        color: #606266;
      }

      .week-box {
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        justify-items: center;
        align-items: center;
      }

      .month-box {
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        grid-template-rows: repeat(auto-fill, 1fr);
        justify-items: center;
        align-items: center;
      }



      .month-box {
        .day {
          width: 100%;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;

          .day-text {
            position: relative;
            font-family: PingFang SC, PingFang SC;
            font-weight: 500;
            font-size: 32rpx;
            color: #303133;

            .no_submit {
              position: absolute;
              top: 0;
              right: -6rpx;
              width: 8rpx;
              height: 8rpx;
              background: #ED4014;
              border-radius: 50%;
            }
          }

          .lunar-text {
            font-family: PingFang SC, PingFang SC;
            font-weight: 400;
            font-size: 24rpx;
            color: #C0C4CC;
            margin-top: 6rpx;
          }

          .is_selected {
            width: 50rpx;
            height: 50rpx;
            border-radius: 50%;
            text-align: center;
            background: #1890ff;
            color: #fff !important;
          }
        }
      }
    }
  }
</style>