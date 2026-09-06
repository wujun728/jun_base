<template>
  <view class="cr-search-content plr10">
    <picker class="picker-selector" mode="selector" @change="bindPickerChange" :value="data.typeIndex" :range="typeData" range-key="name">
      <view class="search-default-label">{{ data.typeText }} <text class="iconfont icon-zhankai1"></text></view>
    </picker>
    <!-- 选择天 -->
    <uni-datetime-picker v-if="type === 0" ref="timeRef" type="date" :clear-icon="false" :border="false" @change="changeTime">
      <template v-slot:default>
        <view class="search-default-date">
          {{ data.timeText }}
          <text v-if="!formData.time" class="date-open-icon iconfont icon-zhankai1"></text>
          <!-- <text  class="date-open"></text> -->
        </view>
        <text v-if="formData.time" class="iconfont date-clear icon-shenpizhongxin-jujue" @click.stop="clickClear"></text>
      </template>
    </uni-datetime-picker>
    <!-- 选择周 -->
    <uni-datetime-picker
      v-if="type === 1"
      ref="timeRef"
      type="daterange"
      v-model="data.range"
      :clear-icon="false"
      :border="false"
      @change="changeTime"
      @inputChange="inputTime"
    >
      <template v-slot:default>
        <view class="search-default-date">
          {{ data.weekText }}
          <text v-if="!data.weekClearShow" class="date-open-icon iconfont icon-zhankai1"></text>
          <!-- <text  class="date-open"></text> -->
        </view>
        <text v-if="data.weekClearShow" class="iconfont date-clear icon-shenpizhongxin-jujue" @click.stop="clickClear"></text>
      </template>
    </uni-datetime-picker>
    <select-month v-if="type === 2" ref="selectMonthRef" @change="changeMonth"></select-month>
    <!-- <select-frame ref="selectFrame"></select-frame> -->
  </view>
</template>

<script setup>
import { ref, reactive, onMounted, toRefs } from 'vue'
import selectFrame from './selectFrame.vue'
import selectMonth from './selectMonth.vue'
import message from '@/utils/message'
import moment from 'moment'

const props = defineProps({
  type: {
    type: Number,
    default: 0,
  },
})
const { type } = toRefs(props)
const data = reactive({
  typeText: '日报',
  timeText: '今日',
  weekText: '本周',
  usersText: '所有人员',
  typeIndex: 0,
  usersIndex: 0,
  usersData: [],
  range: null,
  currentWeek: moment(new Date()).format('W'),
  weekClearShow: false,
})

const formData = reactive({
  time: '',
  approveId: '',
  status: 0,
})

// 根据某一天时间获取周的开始时间与结束时间
const getWeekRange = (e) => {
  const weekOfday = moment(e, 'YYYY-MM-DD').format('E')
  const start = moment(e)
    .subtract(weekOfday - 1, 'days')
    .format('YYYY-MM-DD') //周一日期
  const end = moment(e)
    .add(7 - weekOfday, 'days')
    .format('YYYY-MM-DD') //周日日期
  return [start, end]
}

const timeRef = ref(null)

let emit = defineEmits(['change'])

const typeData = reactive([
  {
    name: '日报',
    value: 0,
  },
  {
    name: '周报',
    value: 1,
  },
  {
    name: '月报',
    value: 2,
  },
  {
    name: '汇报',
    value: 3,
  },
])

onMounted(() => {
  data.range = getWeekRange(moment(new Date()).format('YYYY-MM-DD'))
  getDailyUsers()
})
import { enterpriseDailyUsersApi } from '@/api/user'
// 获取日报人员筛选
const getDailyUsers = () => {
  enterpriseDailyUsersApi()
    .then((res) => {
      if (res.data) {
        data.usersData = res.data
        data.usersData.forEach((value) => {
          value.name = value.name
        })
        data.usersData.unshift({
          id: '',
          name: '所有人员',
        })
      } else {
        data.usersData = []
      }
    })
    .catch((error) => {
      message.error(error.message)
    })
}

// 类型选择
const bindPickerChange = (e) => {
  const len = e.detail.value
  data.typeText = typeData[len].name
  formData.status = typeData[len].value
  formData.time = ''
  emit('change', formData)
}

// 类型选择
const changeUsers = (e) => {
  const len = e.detail.value
  data.usersText = data.usersData[len].name
  formData.approveId = data.usersData[len].id
  emit('change', formData)
}

// 清除日期
const clickClear = () => {
  timeRef.value.clear()
  if (type.value === 1) {
    data.range = [moment().weekday(1).format('YYYY-MM-DD'), moment().weekday(7).format('YYYY-MM-DD')]
  }
}

//选择时间
const changeTime = (e) => {
  if (type.value === 0) {
    if (e) {
      const time = e.replace(/-/g, '/')
      data.timeText = time.substring(5)
      formData.time = time
    } else {
      data.timeText = '今日'
      formData.time = ''
    }
  } else {
    if (e.length > 0) {
      const curWeek = moment(e[0], 'YYYY-MM-DD').format('W')
      if (curWeek === data.currentWeek) {
        data.weekText = '本周'
        data.weekClearShow = false
      } else {
        data.weekClearShow = true
        data.weekText = e[0].substring(5).replace(/-/g, '/') + '-' + e[1].substring(5).replace(/-/g, '/')
      }
      formData.time = e[0].replace(/-/g, '/') + '-' + e[1].replace(/-/g, '/')
    } else {
      data.weekText = '本周'
      data.weekClearShow = false
      formData.time = ''
    }
  }
  emit('change', formData)
}
//选择时间
const inputTime = (e) => {
  data.range = getWeekRange(e)
}

// 选择月
const changeMonth = (e) => {
  formData.time = e.time
  emit('change', formData)
}
</script>

<style scoped lang="scss"></style>
