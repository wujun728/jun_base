<template>
  <view class="cr-search-content plr10">
    <template v-if="status == 1">
      <picker class="picker-selector" mode="selector" @change="changeUsers" :value="data.usersIndex"
        :range="data.usersData" range-key="name">
        <view class="search-default-label">{{data.usersText}} <text class="iconfont icon-zhankai1"></text></view>
      </picker>
    </template>

    <picker class="picker-selector" mode="selector" @change="changeStatus" :value="data.typeIndex"
      :range="invoiceStatus" range-key="name">
      <view class="search-default-label">{{data.labelText}} <text class="iconfont icon-zhankai1"></text></view>
    </picker>

    <picker class="picker-selector" mode="selector" @change="bindPickerChange" :value="data.typeIndex" :range="typeData"
      range-key="name">
      <view class="search-default-label">{{data.typeText}} <text class="iconfont icon-zhankai1"></text></view>
    </picker>

    <uni-datetime-picker ref="timeRef" type="daterange" :clear-icon="false" :border="false" @change="changeTime">
      <template v-slot:default>
        <view class="search-default-date">
          {{data.timeText}}
          <text v-if="!formData.time" class="date-open-icon iconfont icon-zhankai1"></text>
          <!-- <text  class="date-open"></text> -->
        </view>
        <text v-if="formData.time" class="iconfont date-clear  icon-shenpizhongxin-jujue"
          @click.stop="clickClear"></text>
      </template>
    </uni-datetime-picker>
  </view>
</template>

<script setup>
  import { ref, reactive, onMounted, toRefs } from 'vue'
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
    timeText: '创建日期',
    usersText: '业务员',
    labelText: '审核状态',
    typeIndex: 0,
    usersIndex: 0,
    usersData: []
  })

  const formData = reactive({
    time: '',
    salesmaId: '',

    status: '',
    type: ''
  })

  const timeRef = ref(null)

  let emit = defineEmits(['change'])

  const typeData = reactive([
    { name: '所有类型', id: '' },
    { name: '个人普通发票', id: 1 },
    { name: '企业普通发票', id: 2 },
    { name: '企业专用发票', id: 3 },
  ])

  const invoiceStatus = reactive([
    { name: '所有状态', id: '' },
    { name: '发票作废', id: -1 },
    { name: '待审核', id: 0 },
    { name: '待开票', id: 1 },
    { name: '已拒绝', id: 2 },
    { name: '撤回开票', id: 3 },
    { name: '申请作废', id: 4 },
    { name: '已开票', id: 5 },

  ])

  onMounted(() => {
    getSalesman()
  })

  import { enterpriseSalesmanApi } from '@/api/public'
  const getSalesman = () => {
    enterpriseSalesmanApi().then(res => {
      const datas = res.data ? res.data : []
      datas.unshift({ name: '所有人员', id: '' })
      data.usersData = datas
    }).catch(error => {
      message.error(error.message)
    })
  }

  // 状态选择
  const bindPickerChange = e => {
    const len = e.detail.value
    data.typeText = typeData[len].name
    if (data.typeText === '所有类型') {
      data.typeText = '发票类型'
    }
    formData.type = typeData[len].id
    emit('change', formData)
  }

  // 类型选择
  const changeUsers = e => {
    const len = e.detail.value
    data.usersText = data.usersData[len].name
    if (data.usersText === '所有人员') {
      data.usersText = '业务员'
    }
    formData.salesmaId = data.usersData[len].id
    emit('change', formData)
  }

  // 类型状态
  const changeStatus = e => {
    const len = e.detail.value
    data.labelText = invoiceStatus[len].name
    if (data.labelText === '所有状态') {
      data.labelText = '审核状态'
    }
    formData.status = invoiceStatus[len].id
    emit('change', formData)
  }

  // 清除日期
  const clickClear = () => {
    timeRef.value.clear()
  }

  //选择时间
  const changeTime = e => {
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
</script>

<style scoped lang="scss"></style>