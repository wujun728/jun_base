<template>
  <view class="cr-search-content plr10">
    <template v-if="status !== 1">
      <picker class="picker-selector" mode="selector" @change="changeStatus" :range="invoiceStatus" range-key="name">
        <view class="search-default-label">{{data.labelText}} <text class="iconfont icon-zhankai1"></text></view>
      </picker>
    </template>

    <picker class="picker-selector" mode="selector" @change="bindPickerChange" :value="data.typeIndex" :range="typeData"
      range-key="name">
      <view class="search-default-label">
        <view class="line1-1">{{data.typeText}} </view>
        <text class="iconfont icon-zhankai1"></text>
      </view>
    </picker>

    <uni-datetime-picker ref="payTimeRef" type="daterange" :clear-icon="false" :border="false" @change="payChangeTime">
      <template v-slot:default>
        <view class="search-default-date">
          {{data.payTime}}
          <text v-if="!formData.date" class="date-open-icon iconfont icon-zhankai1"></text>
          <!-- <text  class="date-open"></text> -->
        </view>
        <text v-if="formData.date" class="iconfont date-clear  icon-shenpizhongxin-jujue"
          @click.stop="clickClear(1)"></text>
      </template>
    </uni-datetime-picker>

    <uni-datetime-picker ref="timeRef" type="daterange" :clear-icon="false" :border="false" @change="changeTime">
      <template v-slot:default>
        <view class="search-default-date">
          {{data.timeText}}
          <text v-if="!formData.time" class="date-open-icon iconfont icon-zhankai1"></text>
          <!-- <text  class="date-open"></text> -->
        </view>
        <text v-if="formData.time" class="iconfont date-clear  icon-shenpizhongxin-jujue"
          @click.stop="clickClear(2)"></text>
      </template>
    </uni-datetime-picker>
  </view>
</template>

<script setup>
  import message from '@/utils/message'

  const props = defineProps({
    type: {
      type: String,
      default: 'center'
    },
    status: {
      type: Number,
      default: -2
    },
  })
  const { type, status } = toRefs(props)
  const data = reactive({
    typeText: '发票类型',
    timeText: '申请日期',
    payTime: '开票日期',
    labelText: '审核状态',
    typeIndex: 0,
  })

  const formData = reactive({
    time: '',
    date: '',
    status: '',
    type: ''
  })

  const timeRef = ref(null)
  const payTimeRef = ref(null)

  let emit = defineEmits(['change'])

  const typeData = reactive([
    { name: '所有类型', id: '' },
    { name: '个人普通发票', id: 1 },
    { name: '企业普通发票', id: 2 },
    { name: '企业专用发票', id: 3 },
  ])

  const invoiceStatus = reactive([
    { name: '所有状态', id: '' },
    { name: '待开票', id: 1 },
    { name: '已开票', id: 5 },
    { name: '申请作废', id: 4 },
    { name: '已作废', id: -1 },
  ])

  // 状态选择
  const bindPickerChange = e => {
    const len = e.detail.value
    data.typeText = typeData[len].name
    formData.type = typeData[len].id
    if (formData.type === '') {
      data.typeText = '所有类型'
    }
    emit('change', formData)
  }

  // 类型状态
  const changeStatus = e => {
    const len = e.detail.value
    data.labelText = invoiceStatus[len].name
    formData.status = invoiceStatus[len].id
    if (formData.status === '') {
      data.labelText = '审核状态'
    }

    emit('change', formData)
  }

  // 清除日期
  const clickClear = (type) => {
    if (type === 1) {
      payTimeRef.value.clear()
    } else {
      timeRef.value.clear()
    }

    emit('change', formData)
  }

  //付款时间
  const payChangeTime = e => {
    const time = timeOk(e)
    data.payTime = time.text ? time.text : '开票时间'
    formData.date = time.time
    emit('change', formData)
  }

  //申请时间
  const changeTime = e => {
    const time = timeOk(e)
    data.timeText = time.text ? time.text : '申请时间'
    formData.time = time.time
    emit('change', formData)
  }

  const timeOk = (e) => {
    let time = {}
    if (e.length > 0) {
      time.time = e[0].replace(/-/g, '/') + '-' + e[1].replace(/-/g, '/')
      time.text = e[0].substring(5).replace('-', '/') + '-' + e[1].substring(5).replace('-', '/')
    } else {
      time.time = ''
      time.text = ''
    }
    return time
  }
</script>

<style scoped lang="scss"></style>