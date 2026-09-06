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
      <!-- <view class="schedule-header">
        <uni-row class="display-align">
          <uni-col :span="16">
            <uni-dateformat class="left" format="M月d日" :date="time"></uni-dateformat>
          </uni-col>
          <uni-col :span="8">
            <view class="right">
              <view class="display-align" @click="clickComplete">
                <text class="iconfont"
                  :class="complete ? 'icon-wodericheng-yincang': 'icon-wodericheng-xianshi'"></text>
                {{ complete === true ? '隐藏' : '显示' }}已完成
              </view>
            </view>
          </uni-col>
        </uni-row>
      </view> -->
    </view>
    <view class="schedule-box">
      <view class="oa-calendar box-shadow">
        <oa-calendar ref="oaCalendarRef" @onClickDay="onClickDay" @getselectTime="getselectTime"
          :countData="data.countData"></oa-calendar>
      </view>
    </view>

    <!-- 日 -->
    <view class="schedule m10" :style=" {'--height':cHeight+'px' }">
      <scroll-view style="height:100vh" :scroll-y="true" :show-scrollbar="false" @scrolltoupper="scrolltoupper"
        @scrolltolower="scrolltolower" :scroll-into-view="data.toView">
        <uni-list :border=" false" v-if="data.listData.length">
          <template v-for="(val, index) in data.listData" :key="index">
            <view class="date" :id="moment(val.date).format('YYYY-M-D')"> {{moment(val.date).format('M月D日')}} </view>
            <view v-if="val.list.length==0">暂无日程安排，<text class="add-text" @click="clickCreate(val)">点击创建</text>
            </view>
            <view v-else>
              <uni-list-item v-for="(item,indexI) in val.list" :key="indexI">
                <template v-slot:header>
                </template>
                <!-- 自定义 body -->
                <template v-slot:body>
                  <view class="list-item"
                    :style="{ borderLeftColor: item.color,background:getColor( item.color, '0.1') }">
                    <uni-row>
                      <uni-col :span="21" @click="scheduleItem(item)">
                        <view class="list-item-left">{{item.title}}</view>
                      </uni-col>
                      <template v-if="userInfo.userId === item.master.id">

                        <uni-col :span="3" @click.stop="scheduleRecord(item)"
                          v-if="isIdInObjectArray(item.user, item.master.id)">
                          <view class="list-item-right text-right">
                            <template v-if="item.finish != 2">
                              <text class="icon-no-check"
                                v-if="item.finish === 0 || item.finish === 1 || item.finish === -1"></text>
                              <text class="iconfont icon-denglu-tongyi" v-if="item.finish === 3"></text>
                            </template>
                            <template v-else>
                              <image class="image-item" src="@/static/image/schedule03.png" mode=""></image>
                            </template>
                          </view>
                        </uni-col>
                      </template>
                      <template v-else>
                        <uni-col :span="3" @click.stop="scheduleRecord(item)">
                          <view class="list-item-right text-right">
                            <template v-if="item.finish != 2">
                              <text class="icon-no-check"
                                v-if="item.finish === 0 || item.finish === 1 || item.finish === -1"></text>
                              <text class="iconfont icon-denglu-tongyi" v-if="item.finish === 3"></text>
                            </template>
                            <template v-else>
                              <image class="image-item" src="@/static/image/schedule03.png" mode=""></image>
                            </template>
                          </view>
                        </uni-col>
                      </template>
                    </uni-row>
                    <view class="list-item-time" @click="scheduleItem(item)">
                      <view>{{getScheduleTime(item.start_time, item.end_time)}} ・ {{item.type?item.type.name:'--'}}
                      </view>
                    </view>
                  </view>
                </template>
              </uni-list-item>
            </view>
          </template>
        </uni-list>
      </scroll-view>

      <empty v-if="data.listData.length<=0 || (!complete && !data.allFinish)" :index="3" title="暂无待办任务～"></empty>
    </view>
    <!-- 新增 -->
    <view class="add">
      <text class="iconfont icon-xuanfuanniu-jia" @click="clickCreate"></text>
    </view>
  </view>
</template>

<script setup>
  import oaCalendar from '@/components/oaCalendar/index'
  import moment from 'moment'
  import empty from '@/components/empty/index'
  import message from '@/utils/message'
  import { getScheduleTime } from '@/utils/schedule'
  import { scheduleCountApi, scheduleListApi, getScheduleListApi } from '@/api/user'
  import { useStore } from 'vuex'
  import { useBarHeight } from '@/utils/useVerifyCode'
  import {
    ref,
    reactive,
    onMounted,
    getCurrentInstance,
    computed
  } from "vue"
  import {
    getColor,
    getZeroNumber,
    clickNavigateTo,
    clickSwitchTab,
    isIdInObjectArray
  } from '@/utils/helper'
  const store = useStore()
  const userInfo = computed(() => store.state.app.userInfo)
  const {
    height,
    getBarHeight
  } = useBarHeight()
  const instance = getCurrentInstance() // 获取组件实例
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
  })
  const selectTime = ref(moment(new Date()).format('YYYY年MM月'))
  const cHeight = ref(55)
  const oaCalendarRef = ref(null)
  const time = ref(moment(new Date()).format('YYYY-MM-DD'))
  const complete = ref(true)
  const data = reactive({
    isShow: false,
    styleType: 1,
    toView: '',
    selected: [],
    countData: [],
    listData: [],
    where: {
      startTime: moment(new Date()).startOf('week').format('YYYY-MM-DD'),
      endTime: moment(new Date()).endOf('week').format('YYYY-MM-DD')
    },
    allFinish: false,
  })
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

  // 数据滚动到最顶部加载数据
  const scrolltoupper = (e) => {
    oaCalendarRef.value.handleSwiper(1, 0)
  }

  // 数据滚动到最底部加载数据
  const scrolltolower = e => {
    oaCalendarRef.value.handleSwiper(1, 1)
  }


  const clickCreate = (item) => {
    let obj = null
    if (item) {
      let date = moment(item.date).format('YYYY-MM-DD')
      obj = {
        start_time: date + ' ' + moment().format('HH:mm:ss'),
        end_time: date + ' ' + moment().add(1, 'hours').format('HH:mm:ss')
      }
      clickNavigateTo(`/pages/users/schedule/create?time=${ JSON.stringify(obj)}`)
    } else {
      clickNavigateTo('/pages/users/schedule/create')
    }
  }

  const scheduleItem = (item) => {
    clickNavigateTo(`/pages/users/schedule/detail?id=${item.id}&start=${item.start_time}&end=${item.end_time}`)
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
  const onClickDay = (item, type) => {
    time.value = item.year + '-' + item.month + '-' + item.day
    setTimeout(() => {
      document.getElementById(time.value).scrollIntoView({ behavior: 'smooth' })
    }, 500)
  }
  const getselectTime = (val, date) => {

    let isLoadData = data.listData.find(item => item.date == date.endTime);
    if (isLoadData) return false
    selectTime.value = val
    // 判断是0左滑还是右滑1
    let type = 0
    if (data.where.startTime > date.startTime) {
      type = 0
    } else {
      type = 1
    }
    data.where.startTime = date.startTime
    data.where.endTime = date.endTime
    getScheduleCount()
    getScheduleList(type)
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
  const getScheduleList = (type) => {
    uni.showLoading({
      title: '加载中'
    })
    const data_s = {
      cid: checkedTypes.value,
      start_time: `${ data.where.startTime} 00:00:00`,
      end_time: `${ data.where.endTime} 23:59:59`,
      period: 2
    }

    getScheduleListApi(data_s).then(res => {
      if (type === 0) {
        data.listData = [...res.data, ...data.listData]
      } else if (type === 1) {
        data.listData = [...data.listData, ...res.data]
      } else {
        data.listData = res.data
      }
      for (let i = 0; i < data.listData.length; i++) {
        let value = data.listData[i]
        if (value.finish !== 3) {
          data.allFinish = true
          break
        }
        data.allFinish = false
      }
      uni.hideLoading()

    }).catch((error) => {
      message.error(error.message)
      uni.hideLoading()
    })
  }

  const getKeyName = (key) => {
    let color = ''
    for (let i = 0; i < typeData.value.length; i++) {
      if (typeData.value[i].key === key) {
        color = typeData.value[i].name
        break
      }
    }
    return color
  }

  // 修改日程状态
  const scheduleRecord = (item) => {
    emit('scheduleRecord', item)
    getScheduleList()
  }

  const clickComplete = () => {
    complete.value = !complete.value
  }

  // 隐藏已完成待办
  const showItem = (item) => {
    if (complete.value) {
      return true
    } else {
      return !(item.finish === 3)
    }
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

      .set-schedule {
        right: 0;
        justify-content: space-between;

        .iconfont {
          font-size: 34rpx;
          color: #FFFFFF;
        }
      }
    }

    .date {
      font-family: PingFang SC, PingFang SC;
      font-weight: 500;
      font-size: 36rpx;
      margin-bottom: 38rpx;
      margin-top: 20rpx;
    }

    .schedule {
      // padding-top:;
      height: 100%;
      overflow: auto;
      margin-top: calc($uni-default-bar-height + var(--status-bar-height) + var(--height));


      ::v-deep .uni-list {
        .uni-list-item {
          height: 100%;
          margin-bottom: 20rpx;
          background-color: #F2F6FC;
          border-radius: 8rpx;

          .uni-list-item__container {
            height: 100%;
            padding: 0;
          }
        }

        .uni-list--border {
          left: auto;
        }
      }

      ::v-deep .empty {
        padding-bottom: 120px;
      }

      .list-item {
        padding: 26rpx 24rpx;
        width: 100%;
        font-size: $uni-font-size-default;
        color: #333333;
        border-left-style: solid;
        border-left-width: 2px;
        border-radius: 8rpx;

        .list-item-left {
          font-family: PingFang SC, PingFang SC;
          font-weight: 400;
          font-size: 14px;
          color: #333333;
          line-height: 45rpx;

        }

        .list-item-time {
          display: block;
          padding-top: 24rpx;
          font-size: 20rpx;
          color: $nui-text-color-four;

          .pb8 {
            padding-bottom: 16rpx;
          }
        }

        .list-item-right {
          .iconfont {
            font-size: 36rpx;
            color: $uni-color-primary;
          }

          .icon-denglu-tongyi {
            font-size: 20px;
          }

          .icon-no-check {
            display: inline-block;
            width: 19px;
            height: 19px;
            border-radius: 50%;
            background: #FFFFFF;
            border: 1px solid #EEEEEE;
          }
        }

        .image-item {
          width: 36rpx;
          height: 36rpx;
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
      padding-bottom: 20rpx;
    }

    .nl-schedule {
      padding-top: 70px;
    }
  }

  .box-shadow {
    box-shadow: 0px 2px 4px 0px rgba(0, 0, 0, 0.06) !important;
  }

  .add-text {
    color: #1890ff;
  }

  ::v-deep .schedule-content .add-wrap {
    z-index: 2;
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