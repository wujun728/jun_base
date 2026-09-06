<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="calendar-header">
        <view class="flex-header">
          <view @click="cancel" class="iconfont icon-fanhui bar-return back"></view>
          <view>{{selectTime}} </view>
          <view class="set-schedule iconfont icon-richengshezhi" @click="clickSetsChedule"></view>
        </view>
      </view>
    </view>

    <!-- 日程内容 -->
    <view class="schedule-box">
      <view class="oa-calendar box-shadow">
        <oa-calendar @onClickDay="onClickDay" @getselectTime="getselectTime" :countData="data.countData"></oa-calendar>
        <view class="all-day " v-if="data.allDayData.length>0">
          <view class="left">全天</view>
          <view class="right">
            <view class="day" v-for="(val,index) in data.allDayData" :key="val.id"
              :style="{ borderLeftColor: val.color,background:getColor( val.color, '0.1')}" @click="handleDetail(val)">
              <view class="over-text">{{val.title}}</view>
              <view v-if="userInfo.userId!==val.master.id" @click.stop="scheduleRecord(val)">
                <template v-if="val.finish != 2">
                  <text class="icon-no-check " v-if="val.finish === 0 || val.finish === 1 || val.finish === -1"></text>
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

      <view class="nl-schedule" :style="{'--height':cHeight+'px' }"> <nl-schedule :list="data.listData"
          @detail="handleDetail" @add="handleAdd"></nl-schedule></view>
    </view>
    <view class="add">
      <text class="iconfont icon-xuanfuanniu-jia" @click="handleAdd('')"></text>
    </view>
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index'
  import oaCalendar from '@/components/oaCalendar/index'
  import moment from 'moment'
  import empty from '@/components/empty/index'
  import {
    ref,
    reactive,
    onMounted,
    getCurrentInstance,
    computed
  } from "vue"
  import message from '@/utils/message'
  import {
    getZeroNumber,
    clickNavigateTo,
    clickSwitchTab,
    getColor
  } from '@/utils/helper'
  import {
    scheduleCountApi,
    scheduleListApi,
  } from '@/api/user'

  import {
    useStore
  } from 'vuex'
  const store = useStore()
  const userInfo = computed(() => store.state.app.userInfo)
  import {
    useBarHeight
  } from '@/utils/useVerifyCode'
  const {
    height,
    getBarHeight
  } = useBarHeight()
  const instance = getCurrentInstance() // 获取组件实例
  const selectTime = ref(moment(new Date()).format('YYYY年MM月'))
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
  onMounted(() => {
    getBarHeight('.cr-position-header', instance)
    getScheduleCount()
    setTimeout(() => {
      getScheduleList()
    }, 300)

    uni.pageScrollTo({
      scrollTop: 400,
      duration: 0,
    })
  })

  const cHeight = ref(0)
  const time = ref(moment(new Date()).format('YYYY-MM-DD'))
  const data = reactive({
    isShow: false,
    styleType: 1,
    allDayData: [], // 全天展示列表
    selected: [],
    countData: [],
    listData: [],
    where: {
      startTime: moment(new Date()).startOf('week').format('YYYY-MM-DD'),
      endTime: moment(new Date()).endOf('week').format('YYYY-MM-DD')
    },

  })
  // 动态计算轮播图高度
  const getCurrentSwiperHeight = (element) => {
    let query = uni.createSelectorQuery().in(this);
    query.selectAll(element).boundingClientRect();
    query.exec((res) => {
      if (res[0].length > 0) {
        cHeight.value = 46 + res[0][0].height
      } else {
        cHeight.value = 46
      }
    })
  }

  const onClickDay = (item) => {
    time.value = item.year + '-' + item.month + '-' + item.day
    getScheduleList()
  }
  const getselectTime = (val, date) => {
    selectTime.value = val
    data.where.startTime = date.startTime
    data.where.endTime = date.endTime
    getScheduleCount()
  }

  const cancel = () => {
    if (store.state.app.isNoticeJumpPage) {
      uni.navigateBack({
        delta: 1
      })
      setTimeout(() => {
        // 设置消息跳转按钮
        store.commit('setiINoticeJumpPage', false)
      }, 200)
    } else {
      let pages = getCurrentPages()
      let url = '/pages/workbench/index'

      if (pages.length > 1) {
        url = pages[0].route
      }
      if (clickSwitchTab(url)) {
        clickSwitchTab(url)
      } else {
        uni.navigateBack({
          delta: 1
        })
      }
    }
  }

  const emit = defineEmits(["clickSetsCheduleFn", "scheduleRecord"]);
  const clickSetsChedule = () => {
    emit('clickSetsCheduleFn')
  }

  // 修改日程状态
  const scheduleRecord = (item) => {
    emit('scheduleRecord', item)
    getScheduleList()
  }

  // 查看日程详情
  const handleDetail = (e) => {
    clickNavigateTo(`/pages/users/schedule/detail?id=${e.id}&start=${e.start_time}&end=${e.end_time}`)
  }



  // 新建日程
  const handleAdd = (e) => {
    let obj = {}
    if (e) {
      obj = {
        start_time: time.value + ' ' + e.startHour + ':' + '00',
        end_time: time.value + ' ' + e.endHour + ':' + '00'
      }
    } else {
      obj = {
        start_time: time.value + ' ' + moment().format('HH:mm:ss'),
        end_time: time.value + ' ' + moment().add(1, 'hours').format('HH:mm:ss')
      }
    }
    clickNavigateTo(`/pages/users/schedule/create?time=${ JSON.stringify(obj)}`)
  }

  const change = (e) => {
    time.value = e.fulldate
    getScheduleList()
  }

  // 切换月份
  const monthSwitch = (e) => {
    const date = e.year + '-' + getZeroNumber(e.month)
    data.where.startTime = moment(date).startOf('month').format('YYYY-MM-DD')
    data.where.endTime = moment(date).endOf('month').format('YYYY-MM-DD')
    getScheduleCount()
  }


  //获取数量
  const getScheduleCount = () => {
    const data_s = {
      cid: checkedTypes.value,
      start_time: data.where.startTime,
      end_time: data.where.endTime,
      period: 3
    }

    scheduleCountApi(data_s).then(res => {
      data.countData = res.data
    }).catch((error) => {
      message.error(error.message)
    })
  }

  const handleItem = (e) => {
    data.styleType = e.type
    if (e.type === 1) {
      checkedTypes.value = e.data
    }
    getScheduleCount()
    getScheduleList()
  }

  //获取列表
  const getScheduleList = () => {
    const data_s = {
      cid: checkedTypes.value,
      start_time: `${time.value} 00:00:00`,
      end_time: `${time.value} 23:59:59`,
      period: 1
    }
    data.allDayData = []
    scheduleListApi(data_s).then(res => {
      data.listData = res.data
      res.data.forEach((item, index) => {
        if (item.all_day == 1) {
          data.allDayData.push(item)
        } else if (moment(item.start_time).format('YYYY-MM-DD') !== moment(item.end_time).format(
            'YYYY-MM-DD')) {
          data.allDayData.push(item)
        } else {
          item.startHour = moment(item.start_time).format('HH:mm')
          item.endHour = moment(item.end_time).format('HH:mm')
        }
      })
      data.listData = res.data
      setTimeout(() => {
        getCurrentSwiperHeight('.all-day')

      }, 500)
    }).catch((error) => {
      message.error(error.message)
    })
  }



  // 过滤没有日程的数据
  data.selected = computed(() => {
    let res = []
    if (data.countData.length > 0) {
      data.countData.forEach((value) => {
        if (value.no_submit > -1) {
          res.push({
            date: value.time,
            info: value.no_submit === 0 ? '已完成' : '未完成',
            color: value.no_submit === 0 ? '#1890FF' : '#dd524d'
          })
        }
      })
    }
    return res
  })
</script>
<style scoped lang="scss">
  .content {
    position: relative;
    width: 100%;
    background-color: #FFFFFF;


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
  }

  .schedule-box {
    position: relative;
    padding-top: calc($uni-default-bar-height + var(--status-bar-height));

    .oa-calendar {
      width: 100%;
      position: fixed;
      z-index: 99;
      background: #fff;
      padding-bottom: 8px;
    }

    .nl-schedule {
      padding-top: calc($uni-default-bar-height + var(--status-bar-height) + var(--height));
    }
  }


  .box-shadow {
    box-shadow: 0px 2px 4px 0px rgba(0, 0, 0, 0.06) !important;
  }

  ::v-deep .schedule-content .add-wrap {
    z-index: 2;
  }

  .all-day {
    display: flex;
    // padding-bottom: 10px;
    margin-top: 20rpx;

    .left {
      flex-shrink: 0;
      width: 50px;
      text-align: center;
      font-weight: 500;
      font-size: 12px;
      color: #9E9E9E;
    }

    .right {
      width: calc(100% - 50px);
      padding-right: 20px;

      .day {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        margin-bottom: 2px;
        padding-left: 7px;
        padding-right: 6px;
        height: 22px;

        font-family: PingFang SC, PingFang SC;
        font-weight: 400;
        font-size: 12px;
        color: #303133;
        box-sizing: border-box;
        border-left: 2px solid;
        border-radius: 4px;
        line-height: 22px;


        .iconfont {
          font-size: 34rpx;
          color: $uni-color-primary;

        }

        .icon-no-check {
          display: flex;
          align-items: center;
          width: 32rpx;
          height: 32rpx;
          border-radius: 50%;
          background: #FFFFFF;
          border: 1px solid #EEEEEE;
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
</style>