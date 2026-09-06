<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :index="0" :default-type="2" :is-right="true" :right-data="rightIcon" :default-title="selectTime"
        @handleNarItem="handleNarItem"></default-nav-bar>
      <select-three-days></select-three-days>
    </view>
    <view class="schedule m10">
    </view>
    <set-schedule ref="setScheduleRef" :checked-types="data.checkedTypes" :type-data="data.typeData"
      @handleItem="handleItem"></set-schedule>
  </view>
</template>

<script setup>
  import moment from 'moment'
  import { onReachBottom, onPullDownRefresh, onPageScroll } from '@dcloudio/uni-app'
  import defaultNavBar from '@/components/defaultNavBar/index'
  import setSchedule from './setSchedule'
  import selectThreeDays from './selectThreeDays.vue'
  import empty from '@/components/empty/index'
  import { ref, reactive, onMounted, getCurrentInstance, computed } from "vue"
  import message from '@/utils/message'
  import { scheduleTypesApi, scheduleCountApi, scheduleListApi, scheduleRecordApi } from '@/api/user'
  const setScheduleRef = ref(null)
  const currentIndex = ref(1)
  import { useBarHeight } from '@/utils/useVerifyCode'
  const { height, getBarHeight } = useBarHeight()
  const instance = getCurrentInstance() // 获取组件实例

  onMounted(() => {
    getBarHeight('.cr-position-header', instance)
    getScheduleTypes()
  })
  const rightIcon = reactive([
    { type: 1, icon: 'icon-richengshezhi' },
    { type: 2, icon: 'icon-a-gengduo2' },
  ])

  const selectTime = ref(moment(new Date()).format('YYYY年MM月'))
  const complete = ref(true)
  const data = reactive({
    checkedTypes: [],
    typeData: [],
    selected: [],
    countData: [],
    listData: [],
  })

  const handleNarItem = (e) => {
    if (e.type === 1) {
      setScheduleRef.value.popupOpen()
    } else {
      uni.navigateTo({
        url: '/pages/users/schedule/create',
        animationType: 'slide-in-right'
      });
    }
  }
  onPullDownRefresh(() => {
    uni.stopPullDownRefresh()
  })


  //获取类型
  const getScheduleTypes = () => {
    scheduleTypesApi().then(res => {
      data.typeData = res.data
      if (res.data.length > 0) {
        res.data.forEach((value) => {
          data.checkedTypes.push(value.key)
        })
      }
      getScheduleCount()
      getScheduleList()
    }).catch((error) => {
      message.error(error.message)
    })
  }

  //获取数量
  const getScheduleCount = () => {
    const data_s = {
      types: data.checkedTypes,
      time: data.where.startTime,
      end: data.where.endTime
    }
    scheduleCountApi(data_s).then(res => {
      data.countData = res.data
    }).catch((error) => {
      message.error(error.message)
    })
  }

  const handleItem = (e) => {
    if (e.type === 1) {
      data.checkedTypes = e.data
    }
    getScheduleCount()
    getScheduleList()
  }

  //获取列表
  const getScheduleList = () => {
    const data_s = {
      types: data.checkedTypes,
      time: time.value,
      period: 0
    }
    scheduleListApi(data_s).then(res => {
      data.listData = res.data[0].list
    }).catch((error) => {
      message.error(error.message)
    })
  }

  const getKeyColor = (key) => {
    let color = ''
    for (let i = 0; i < data.typeData.length; i++) {
      if (data.typeData[i].key === key) {
        color = data.typeData[i].color
        break
      }
    }
    return color
  }

  // 修改日程状态
  const scheduleRecord = (item) => {
    const data_s = {
      time: time.value,
      status: item.finish === 0 ? 1 : 0,
    }
    scheduleRecordApi(item.id, data_s).then(res => {
      getScheduleCount()
      getScheduleList()
      message.success(res.message, 'none')
    }).catch((error) => {
      message.error(error.message)
    })
  }

  const clickComplete = () => {
    complete.value = !complete.value
  }

  // 隐藏已完成待办
  const showItem = (item) => {
    if (complete.value) {
      return true
    } else {
      return !(item.finish === 1)
    }
  }

  // 过滤没有日程的数据
  data.selected = computed(() => {
    let res = []
    if (data.countData.length > 0) {
      data.countData.forEach((value) => {
        if (value.count > 0) {
          res.push({
            date: value.date,
            info: value.count === value.finish ? '已完成' : '未完成'
          })
        }
      })
    }
    return res
  })
</script>
<style scoped lang="scss">
  .content {
    width: 100%;

    .days-header {
      width: 100%;
      height: 120rpx;
      box-shadow: 0px 2px 4px 0px rgba(0, 0, 0, 0.06);

      .swiper {
        width: 100%;
        height: 100%;
      }

      .days-swiper-item {
        width: 100%;
        height: 120rpx;
        display: flex;
        justify-content: space-around;
        align-items: center;

        .days-header-item {
          &.active {

            .top,
            .bottom {
              color: $uni-color-primary;
            }
          }

          .top {
            font-size: 20rpx;
            transform: scale(0.93);
            color: $nui-text-color-four;
          }

          .bottom {
            font-size: 40rpx;
            font-weight: 500;
            color: $uni-text-color;
          }
        }
      }
    }
  }

  ::v-deep .schedule-content .add-wrap {
    z-index: 2;
  }
</style>