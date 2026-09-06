<template>
  <view class="upload-time">
    <uni-forms-item class="input-label">
      <template v-slot:label>
        <view class="uni-forms-item__label">
          <text v-if="configData.effect && configData.effect.required" class="iconfont"> * </text>
          <text class="label-item"> 开始时间 </text>
        </view>
      </template>
      <!-- 天 -->
      <template v-if="configData.props.timeType === 'day'">
        <view class="input-right-conment" @click="pickerDate(1)">
          <view class="picker-input picker-input-placeholder" v-if="!configData.value">
            请选择
            <view class="iconfont icon-fanhui"></view>
          </view>
          <view class="picker-input" v-else>
            {{data.newDateStart ? data.newDateStart : configData.value.dateStart}}
            {{ !data.newDateStart ? (configData.value.timeStart == 1 ? '上午' : '下午') : '' }}
            <view class="iconfont icon-fanhui"></view>
          </view>
        </view>
      </template>
      <!-- 小时 -->
      <template v-else>
        <view class="xp-picker-content">
          <xp-picker v-model="computedDateStart" mode="ymdhi" actionPosition="top" :yearRange="[2014,2050]"
            @confirm="onChange(1, $event)" />
          <view class="iconfont icon-fanhui"></view>
        </view>
      </template>
    </uni-forms-item>
    <uni-forms-item class="input-label">
      <template v-slot:label>
        <view class="uni-forms-item__label">
          <text v-if="configData.effect && configData.effect.required" class="iconfont"> * </text>
          <text class="label-item">结束时间</text>
        </view>
      </template>
      <template v-if="configData.props.timeType === 'day'">
        <view class="input-right-conment" @click="pickerDate(2)">
          <view class="picker-input picker-input-placeholder"
            v-if="!configData.value ||!data.newDataEnd ||! configData.value.dateEnd ">
            请选择
            <view class="iconfont icon-fanhui"></view>
          </view>
          <view class="picker-input" v-else>
            {{data.newDataEnd ? data.newDataEnd : configData.value.dateEnd}}
            {{ !data.newDataEnd ? (configData.value.timeEnd == 1 ? '上午' : '下午') : '' }}

            <view class="iconfont icon-fanhui"></view>
          </view>
        </view>
      </template>

      <template v-else>
        <view class="xp-picker-content">
          <xp-picker v-model="computedDateEnd" mode="ymdhi" actionPosition="top" :yearRange="[2014,2050]"
            @confirm="onChange(2, $event)" />
          <view class="iconfont icon-fanhui"></view>
        </view>
      </template>

    </uni-forms-item>
    <uni-forms-item class="input-label">
      <template v-slot:label>
        <view class="uni-forms-item__label">
          <text v-if="configData.effect && configData.effect.required" class="iconfont"> * </text>
          <text
            class="label-item">{{configData.props.titleIpt}}({{configData.props.timeType === 'day' ? '天' : '小时'}})</text>
        </view>
      </template>
      <view class="input-right-conment">
        <uni-easyinput :inputBorder="false" type="number" v-model="computedDuration" :clearable="false" :styles="styles"
          :placeholder-style="placeholderStyle" :maxlength="256" :autoHeight="true" placeholder="请输入"
          @confirm="changeDuration">
        </uni-easyinput>
      </view>
    </uni-forms-item>
  </view>
  <time-from-picker ref="timeFromPickerRef" type="time" :data-time="data.dataTime"
    @change="changeTimePicker"></time-from-picker>
</template>

<script setup>
  import {
    ref,
    reactive,
    toRefs,
    watch
  } from 'vue'
  import message from '@/utils/message'
  import timeFromPicker from './timeFromPicker.vue'
  import moment from 'moment'
  moment.suppressDeprecationWarnings = true

  const props = defineProps({
    configData: {
      type: Object,
      default: () => {
        return {}
      },
    }
  })
  const {
    configData
  } = toRefs(props)
  const data = reactive({
    dateStart: '',
    dateEnd: '',
    timeStart: '',
    timeEnd: '',
    duration: '',
    type: 0,
    newDateStart: '',
    newDataEnd: '',
    dataTime: '',
    num: '',
    time: moment(new Date()).format('YYYY/MM/DD'),
  })

  const fromData = reactive({
    dateEnd: '',
    dateStart: '',
    duration: '',
    timeEnd: '1',
    timeStart: '1',
    timeType: ''
  })
  // 开始时间
  const computedDateStart = computed(() => {
    // console.log(configData.value, 99999999999)
    if (configData.value.value && configData.value.value.dateStart && !data.dateStart) {
      return configData.value.value.dateStart;
    }
    return data.dateStart;
  });
  // 结束时间
  const computedDateEnd = computed(() => {
    if (configData.value.value && configData.value.value.dateEnd && !data.dateEnd) {
      return configData.value.value.dateEnd;
    }
    return data.dateEnd;
  });
  // 请假时长
  const computedDuration = computed(() => {
    if (configData.value.value && configData.value.value.duration && (!data.dateEnd || !data.dateEnd)) {
      return configData.value.value.duration;
    }
    return data.duration;
  });

  const timeFromPickerRef = ref(null)

  const placeholderStyle = ref('color: #C0C4CC;font-size: 30rpx')
  const styles = reactive({
    color: '#303133',
    disableColor: '#ffffff'
  })

  let emit = defineEmits(['change'])

  const pickerDate = (type) => {
    data.type = type
    if (type === 1) {
      data.dataTime = data.dateStart ? data.dateStart : data.time
    } else {
      data.dataTime = data.dateEnd ? data.dateEnd : data.time
    }
    timeFromPickerRef.value.popupOpen()
  }
  const changeTimePicker = (e) => {
    let time = e.substr(0, e.length - 2)
    if (data.type === 1) {
      data.dateStart = e
      if (configData.value.props.timeType === 'day') {
        data.timeStart = e.substr(e.length - 1, 1)
        const type = data.timeStart == 1 ? '上午' : '下午'
        data.newDateStart = time + ' ' + type
      }
    } else {
      console.log('esjhfv跟我去阿达')
      data.dateEnd = e
      if (configData.value.props.timeType === 'day') {
        data.timeEnd = e.substr(e.length - 1, 1)
        const type = data.timeEnd == 1 ? '上午' : '下午'
        data.newDataEnd = time + ' ' + type
      }
    }
    onChange()
  }

  const changeDuration = () => {
    fromData.duration = data.duration
    emit('change', fromData)
  }

  import {
    divTime
  } from '@/utils/helper'

  const onChange = (type, e) => {
    if (type == 1) {
      data.dateStart = e.value
    }
    if (type == 2) {
      data.dateEnd = e.value
    }
    let time1, time2, date1, date2
    if (configData.value.props.timeType === 'day') {
      date1 = data.newDateStart.substr(0, data.dateStart.length - 2)
      date2 = data.newDataEnd.substr(0, data.dateEnd.length - 2)

      time1 = Date.parse(new Date(date1));
      time2 = Date.parse(new Date(date2));
    } else {
      time1 = Date.parse(new Date(data.dateStart));
      time2 = Date.parse(new Date(data.dateEnd));
      console.log('time1', time1, 'time2', time2);
    }
    if (time1 > time2) {
      message.error('结束时间不能小于开始时间')
      return false
    }
    fromData.timeType = configData.value.props.timeType
    if (configData.value.props.timeType === 'day') {
      data.num = divTime(date1, date2, 'day')
      fromData.dateStart = date1
      fromData.dateEnd = date2
      fromData.timeStart = data.timeStart
      fromData.timeEnd = data.timeEnd
      if (time2 > time1) count()
      if (time2 === time1) count()
    } else {
      fromData.dateStart = data.dateStart
      fromData.dateEnd = data.dateEnd
      if (time2 > time1) data.duration = divTime(data.dateStart, data.dateEnd, 'time');
    }
    fromData.duration = data.duration
    console.log('data.dateStart：', data.dateStart);
    emit('change', fromData)
  }

  const count = () => {
    const len = parseFloat(data.timeStart) - parseFloat(data.timeEnd);
    if (len === 1) {
      data.duration = parseFloat(data.num) + 1;
    } else if (len === 0) {
      data.duration = parseFloat(data.num) + 0.5;
    } else if (len === -1) {
      data.duration = parseFloat(data.num);
    }
  }
</script>

<style lang="scss" scoped>
  .upload-time {
    width: 100%;
    background-color: #fff;

    .iconfont {
      font-size: 36rpx;
    }

    .input-label {
      padding: 18rpx 0 !important;
      margin: 0 24rpx;
      align-items: center;
      margin-bottom: 0 !important;
      border-radius: 0 !important;
      border-bottom: 1px solid #EBEEF5;

      &:first-of-type {
        border-radius: 12rpx 12rpx 0 0 !important;
      }

      &:last-of-type {
        margin-bottom: 20rpx !important;
        border-radius: 0 0 12rpx 12rpx !important;
        border-bottom: none;
      }

      ::v-deep .uni-easyinput__content-input {
        text-align: right;
        padding-right: 0 !important;
      }

      ::v-deep .uni-forms-item__label {
        width: 148rpx;
        display: flex;
        line-height: 1.2;

        .label-item {
          width: calc(100% - 16rpx);
        }

        .iconfont {
          font-size: 16px;
          // width: 16rpx;
        }
      }

      .picker-input {
        text-align: right;
        height: 35px;
        color: $uni-text-color;
        font-size: 30rpx;
        align-items: center;
        display: flex;
        justify-content: flex-end;

        .iconfont {
          padding-right: 16rpx;
          margin-top: 7rpx;
          transform: rotate(180deg);
          font-size: 24rpx;
          color: #C0C4CC;
        }
      }

      .picker-input-placeholder {
        color: #C0C4CC;
      }

      .input-right-conment {
        text-align: right;
        height: 35px;
        line-height: 35px;
        color: $uni-text-color;
      }
    }
  }

  .fz16 {
    font-size: 30rpx;
  }
</style>