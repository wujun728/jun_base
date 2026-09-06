<template>
  <view class="cr-search-content plr10">

    <template v-if="status === 1">
      <picker class="picker-selector" mode="selector" @change="changeUsers" :value="data.usersIndex"
        :range="data.usersData" range-key="name">
        <view class="search-default-label">{{data.usersText}} <text class="iconfont icon-zhankai1"></text></view>
      </picker>
    </template>
    <view class="picker-selector">
      <view class="search-default-label" @click="changeLabel">
        {{data.labelText}}
        <text class="date-open-icon iconfont icon-zhankai1"></text>
      </view>
    </view>

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
  <selected-label title="客户标签" ref="selectedLabelRef" @changeItem="changeItem"> </selected-label>
</template>

<script setup>
  import { ref, reactive, onMounted, toRefs } from 'vue'
  import selectedLabel from './selectedLabel.vue'
  import message from '@/utils/message'

  const props = defineProps({
    type: {
      type: String,
      default: 'center'
    },
    status: {
      type: Number,
      default: 2
    },
  })
  const { type, status } = toRefs(props)
  const data = reactive({
    typeText: '客户状态',
    timeText: '创建日期',
    usersText: '业务员',
    labelText: '标签筛选',
    typeIndex: 0,
    usersIndex: 0,
    usersData: []
  })

  const formData = reactive({
    time: '',
    salesmanId: '',
    customer_status: '',
    customer_label: []
  })

  const timeRef = ref(null)
  let emit = defineEmits(['change'])

  const typeData = reactive([
    { name: '全部', id: '' },
    { name: '未成交', id: 0 },
    { name: '已成交', id: 1 },
    { name: '已流失', id: 2 }

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

  const selectedLabelRef = ref(null)

  // 标签选择
  const changeLabel = e => {
    selectedLabelRef.value.popupOpen(formData.customer_label)
  }

  // 标签选择回调
  const changeItem = (e, name) => {
    formData.customer_label = e
    if (e.length > 0) {
      data.labelText = name[0]
    } else {
      data.labelText = '标签筛选'
    }
    emit('change', formData)
  }

  // 状态选择
  const bindPickerChange = e => {
    const len = e.detail.value
    data.typeText = typeData[len].name
    if (data.typeText === '全部') {
      data.typeText = '客户状态'
    }
    formData.customer_status = typeData[len].id
    emit('change', formData)
  }

  // 类型选择
  const changeUsers = e => {
    const len = e.detail.value
    data.usersText = data.usersData[len].name
    if (data.usersText === '所有人员') {
      data.usersText = '业务员'
    }
    formData.salesmanId = data.usersData[len].id
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