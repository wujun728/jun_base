<template>
  <view class="cr-search-content plr10">
    <picker class="picker-selector" mode="selector" @change="bindPickerChange" :value="data.typeIndex" :range="typeData" range-key="name">
      <view class="search-default-label">{{ data.typeText }} <text class="iconfont icon-zhankai1"></text></view>
    </picker>
    <picker
      v-show="type === 'receive'"
      class="picker-selector"
      mode="selector"
      @change="changeUsers"
      :value="data.usersIndex"
      :range="data.usersData"
      range-key="name"
    >
      <view class="search-default-label">{{ data.usersText }} <text class="iconfont icon-zhankai1"></text></view>
    </picker>
    <uni-datetime-picker ref="timeRef" type="daterange" :clear-icon="false" :border="false" @change="changeTime">
      <template v-slot:default>
        <view class="search-default-date">
          {{ data.timeText }}
          <text v-if="!formData.time" class="date-open-icon iconfont icon-zhankai1"></text>
          <!-- <text  class="date-open"></text> -->
        </view>
        <text v-if="formData.time" class="iconfont date-clear icon-shenpizhongxin-jujue" @click.stop="clickClear"></text>
      </template>
    </uni-datetime-picker>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted, toRefs, watch } from 'vue'
import message from '@/utils/message'

const props = defineProps({
  type: {
    type: String,
    default: 'mine',
  },
})
const { type } = toRefs(props)
const data = reactive({
  typeText: '所有汇报',
  timeText: '所有日期',
  usersText: '所有人员',
  typeIndex: 0,
  usersIndex: 0,
  usersData: [],
})

const formData = reactive({
  time: '',
  approveId: '',
  status: '',
})

const timeRef = ref(null)

let emit = defineEmits(['change'])

const typeData = reactive([
  { name: '所有汇报', value: '' },
  { name: '日报', value: '0' },
  { name: '周报', value: '1' },
  { name: '月报', value: '2' },
  { name: '汇报', value: '3' },
])

onMounted(() => {
  getDailyUsers()
})
import { enterpriseDailyUsersApi } from '@/api/user'
// 获取日报人员筛选
const getDailyUsers = () => {
  enterpriseDailyUsersApi()
    .then((res) => {
      data.usersData = res.data ? res.data : []
      if (data.usersData.length > 0) {
        data.usersData.forEach((value) => {
          value.name = value.name
        })
        data.usersData.unshift({ id: '', name: '所有人员' })
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
}

//选择时间
const changeTime = (e) => {
  if (e.length > 0) {
    const time = e[0].replace(/-/g, '/') + '-' + e[1].replace(/-/g, '/')
    data.timeText = e[0].substring(5).replace('-', '/') + '-' + e[1].substring(5).replace('-', '/')
    formData.time = time
  } else {
    data.timeText = '所有日期'
    formData.time = ''
  }
  emit('change', formData)
}

// 数据监听
watch(
  type,
  (newvalue, oldvalue) => {
    data.usersIndex = 0
    data.usersText = '所有人员'
    formData.approveId = ''
    emit('change', formData)
  },
  { deep: true }
)
</script>

<style scoped lang="scss"></style>
