<template>
  <view class="cr-search-content plr10">

    <template v-if="status == 5">
      <picker class="picker-selector" mode="selector" @change="changeUsers" :value="data.usersIndex"
        :range="data.usersData" range-key="name">
        <view class="search-default-label">{{data.usersText}} <text class="iconfont icon-zhankai1"></text></view>
      </picker>
    </template>

    <picker class="picker-selector" mode="selector" @change="labelChange" :value="data.labelIndex"
      :range="data.labelData" range-key="name">
      <view class="search-default-label">{{data.labelText}} <text class="iconfont icon-zhankai1"></text></view>
    </picker>

    <picker class="picker-selector" mode="selector" @change="bindPickerChange" :value="data.typeIndex"
      :range="data.typeData" range-key="name">
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
  import { dictSelectApi } from '@/api/customer'
  import { ref, reactive, onMounted, toRefs } from 'vue'
  import message from '@/utils/message'

  const props = defineProps({
    type: {
      type: String,
      default: 'center'
    },
    status: {
      type: Number,
      default: 6
    },
  })
  const { type, status } = toRefs(props)
  const data = reactive({
    typeText: '合同状态',
    timeText: '创建日期',
    usersText: '业务员',
    labelText: '全部',
    typeIndex: 0,
    usersIndex: 0,
    labelIndex: 0,
    usersData: [],
    labelData: [],
    typeData: []
  })

  const formData = reactive({
    time: '',
    salesmaId: '',
    status: '',
    label: ''
  })

  const timeRef = ref(null)

  let emit = defineEmits(['change'])


  onMounted(() => {
    getSalesman()
    getlabel()
    getType()
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
  // 签约状态
  const getlabel = () => {
    let obj = {
      types: 'signing_status'
    }
    dictSelectApi(obj).then(res => {
      const datas = res.data ? res.data : []
      datas.unshift({ name: '全部', id: '' })
      data.labelData = datas
    }).catch(error => {
      message.error(error.message)
    })
  }
  // 合同状态
  const getType = () => {
    let obj = {
      types: 'contract_status'
    }
    dictSelectApi(obj).then(res => {
      const datas = res.data ? res.data : []
      datas.unshift({ name: '全部', id: '' })
      data.typeData = datas
    }).catch(error => {
      message.error(error.message)
    })
  }

  // 状态选择
  const bindPickerChange = e => {
    const len = e.detail.value
    data.typeText = data.typeData[len].name
    if (data.typeText === '全部') {
      data.typeText = '合同状态'
    }
    formData.status = data.typeData[len].id
    emit('change', formData)
  }

  // 续费类型选择
  const labelChange = e => {
    const len = e.detail.value
    data.labelText = data.labelData[len].name
    if (data.id === '') {
      data.labelText = '全部'
    }
    formData.label = data.labelData[len].id
    emit('change', formData)
  }

  // 选择业务员
  const changeUsers = e => {
    const len = e.detail.value
    data.usersText = data.usersData[len].name
    formData.salesmaId = data.usersData[len].id
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