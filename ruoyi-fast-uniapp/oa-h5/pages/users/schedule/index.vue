<template>
  <view class="content">
    <!-- 日程 -->
    <view v-if="data.styleType==4">
      <schedule :checkedTypes="data.checkedTypes" :typeData="data.typeData" @clickSetsCheduleFn="clickSetsChedule"
        @scheduleRecord="scheduleRecord">
      </schedule>
    </view>

    <!-- 日 -->
    <view v-if="data.styleType==1">
      <day :checkedTypes="data.checkedTypes" :typeData="data.typeData" @clickSetsCheduleFn="clickSetsChedule"
        @scheduleRecord="scheduleRecord">
      </day>
    </view>
    <!-- 三日 -->
    <view v-if="data.styleType==2">
      <selectThreeDays :checkedTypes="data.checkedTypes" :typeData="data.typeData"
        @clickSetsCheduleFn="clickSetsChedule" @scheduleRecord="scheduleRecord"></selectThreeDays>
    </view>
    <!-- 周 -->
    <view v-if="data.styleType==3">
      <selectWeekDays :checkedTypes="data.checkedTypes" :typeData="data.typeData" @clickSetsCheduleFn="clickSetsChedule"
        @scheduleRecord="scheduleRecord"></selectWeekDays>
    </view>

    <global-index></global-index>
    <set-schedule ref="setScheduleRef" :checked-types="data.checkedTypes" :type-data="data.typeData"
      @handleItem="handleItem"></set-schedule>

  </view>
</template>

<script setup lang="ts">
  import day from './components/day.vue'
  import setSchedule from './components/setSchedule.vue'
  import schedule from './components/schedule.vue'
  import selectThreeDays from './components/selectThreeDays.vue'
  import selectWeekDays from './components/selectWeekDays.vue'
  import globalIndex from "@/components/globalIndex/index.vue"
  import { onLoad, onReachBottom, onPageScroll } from '@dcloudio/uni-app'
  import message from '@/utils/message'
  import { scheduleTypesApi, scheduleRecordApi } from '@/api/user'
  import { clickNavigateTo } from '@/utils/helper'
  import { clientRemindDetailApi, configApproveApi } from '@/api/customer'
  import { ref, reactive } from "vue"
  const setScheduleRef = ref(null)
  const data = reactive({
    checkedTypes: [],
    typeData: [],
    styleType: 4,
    buildData: {},
    cidData: [2, 3, 4]
  })

  onLoad(() => {
    getScheduleTypes()
    getConfigApprove()
  })
  onMounted(() => {
    data.styleType = uni.getStorageSync('scheduleTypes') + '' || 4
  })


  //获取类型
  const getScheduleTypes = () => {
    scheduleTypesApi().then(res => {
      data.typeData = res.data
      if (res.data.length > 0) {
        res.data.map((value) => {
          data.checkedTypes.push(value.id)
        })
      }
    }).catch((error) => {
      message.error(error.message)
    })
  }

  // 修改日程状态
  const scheduleRecord = (item) => {
    if (item.finish === 2 || (data.cidData.includes(item.cid) && item.finish === 3)) return false
    if (!data.cidData.includes(item.cid)) {
      const data_s = {
        end: item.end_time,
        start: item.start_time,
        status: item.finish === 3 ? 1 : 3,
      }
      scheduleRecordApi(item.id, data_s).then(res => {

        message.success(res.message, 'none')
      }).catch((error) => {
        message.error(error.message)
      })
    } else {
      if (item.cid === 3 || item.cid === 4) {
        // 获取提醒详情
        const datas = {
          status: 3,
          end: item.end_time,
          start: item.start_time,
          id: item.id
        }
        uni.setStorageSync('scheduleData', JSON.stringify(datas))
        const id = item.relation.remind_id
        clientRemindDetailApi(id).then(res => {
          const config = res.data
          if (config.types == 0) {
            clickNavigateTo(
              `/pages/users/examine/default?id=${data.buildData.contract_refund_switch}&eid=${config.eid}&cid=${config.cid}&types=schedule`
            )
          } else {
            clickNavigateTo(
              `/pages/users/examine/default?id=${data.buildData.contract_renew_switch}&eid=${config.eid}&cid=${config.cid}&types=schedule`
            )
          }

        }).catch(error => {
          message.error(error.message)
        })
      } else if (item.cid === 2) {
        const query =
          `/pages/customer/list/addFollow?kid=${item.link_id}&type=1&fid=${item.relation.follow_id}`
        clickNavigateTo(query)
      }
    }
  }
  const getConfigApprove = () => {
    configApproveApi().then(res => {
      data.buildData = res.data
    })
  }



  const handleItem = (e) => {
    data.styleType = e.type
    data.checkedTypes = e.data
  }
  const clickSetsChedule = () => {
    setScheduleRef.value.popupOpen()
  }

  // 下拉加载
  // onReachBottom(() => {
  // })
  onPageScroll((e) => {
  })
</script>
<style>
  page {
    background-color: #fff;
  }
</style>
<style scoped lang="scss">
  .content {
    width: 100%;
  }
</style>