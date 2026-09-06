<template>
  <view>
    <uni-popup ref="popupRef" type="bottom" :mask-click="true">
      <view class="slider">
        <view class="share-header plr10">
          <view class="share-title default-cancel" @click="cancel">取消</view>
          <view class="share-title">自定义重复</view>
          <view class="share-title default-color" @click="handleConfirm">确认</view>
        </view>
        <view class="share-list ml10">
          <scroll-view scroll-y="true" style="height: 100%;">
            <view class="cr-center-list mr10">
              <view class="center-list-item">
                <uni-row class="center-list-item-con">
                  <uni-col :span="8">重复类型</uni-col>
                  <uni-col :span="16" class="display-align right">
                    <picker class="picker-selector" mode="selector" @change="changePeriod" :value="data.periodIndex"
                      :range="periodData" range-key="label">
                      <view class="search-default-label">{{data.periodText}}</view>
                    </picker>
                    <view class="iconfont icon-fanhui"></view>
                  </uni-col>
                </uni-row>
                <uni-row class="center-list-item-con">
                  <uni-col :span="8">频率({{handleRepeat(formData.period)}})<text class="is-required">*</text></uni-col>
                  <uni-col :span="16" class="display-align right">
                    <uni-easyinput v-model="formData.rate" :inputBorder="false" :styles="styles" type="number"
                      :clearable="false" :maxlength="200" placeholder="请输入频率">
                    </uni-easyinput>
                  </uni-col>
                </uni-row>
              </view>
              <!-- 按周重复 -->
              <view class="center-list-item weekDays" v-if="formData.period === 1">
                <view class="weekDays-list">
                  <view class="weekDays-list-item" v-for="(item,index) in week" :key="index"
                    :class="formData.weekDays.includes(item.value) ? 'active': ''" @click="weekDaysItem(item)">
                    {{item.text}}
                  </view>
                </view>
              </view>
              <!--按月重复 -->
              <view class="center-list-item monthDays" v-if="formData.period === 2">
                <view class="monthDays-list">
                  <view class="monthDays-list-item" v-for="(item,index) in 30" :key="index"
                    :class="formData.monthDays.includes(item) ? 'active': ''" @click="monthDaysItem(item)">{{item}}
                  </view>
                </view>
              </view>
              <view class="center-list-item">
                <uni-row class="center-list-item-con">
                  <uni-col :span="8">结束日期</uni-col>
                  <uni-col :span="16" class="display-align right">
                    <xp-picker :disabled="false" v-model="formData.time" mode="ymdhis" actionPosition="top"
                      placeholder="永不结束" :yearRange="[2014,2050]" />
                    <view class="iconfont icon-fanhui"></view>
                  </uni-col>
                </uni-row>
              </view>
            </view>
          </scroll-view>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
  import { ref, reactive, toRefs, watch } from 'vue'
  import message from '@/utils/message'
  const props = defineProps({
    typeData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    isEdit: {
      type: Boolean,
      default: false
    }
  })
  const { typeData, isEdit } = toRefs(props)
  const emit = defineEmits(["change"]);

  const popupRef = ref(null)
  const styles = reactive({
    color: '#303133',
    disableColor: '#ffffff'
  })

  const data = reactive({
    listData: [],
    periodText: '按天重复',
    periodIndex: 0
  })

  const formData = reactive({
    period: 0,
    weekDays: [],
    monthDays: [],
    rate: 1,
    time: ''
  })

  const periodData = reactive([
    { value: 0, label: '按天重复' },
    { value: 1, label: '按周重复' },
    { value: 2, label: '按月重复' },
    { value: 3, label: '按年重复' },
  ])


  const week = reactive([
    { value: 1, text: '星期一' },
    { value: 2, text: '星期二' },
    { value: 3, text: '星期三' },
    { value: 4, text: '星期四' },
    { value: 5, text: '星期五' },
    { value: 6, text: '星期六' },
    { value: 7, text: '星期天' }
  ])


  const changePeriod = (e) => {
    let len = e.detail.value
    data.periodText = periodData[len].label
    formData.period = periodData[len].value
  }

  //周选择
  const weekDaysItem = (item) => {
    let len = formData.weekDays.indexOf(item.value)
    if (len === -1) {
      formData.weekDays.push(item.value)
    } else {
      formData.weekDays.splice(len, 1)
    }
  }
  // 月选择
  const monthDaysItem = (item) => {
    let len = formData.monthDays.indexOf(item)
    if (len === -1) {
      formData.monthDays.push(item)
    } else {
      formData.monthDays.splice(len, 1)
    }
  }

  // 打开弹出
  const popupOpen = () => {
    popupRef.value.open()
  }

  // 关闭
  const cancel = () => {
    popupRef.value.close()
  }

  const handleRepeat = (e) => {
    let str = ''
    if (e === 1) {
      str = '周'
    } else if (e === 2) {
      str = '月'
    } else if (e === 3) {
      str = '年'
    } else {
      str = '天'
    }
    return str
  }

  const handleConfirm = () => {
    if (!formData.rate) {
      message.error('频率不能为空')
      return false
    }
    if (formData.period === 1 && formData.weekDays.length <= 0) {
      message.error('至少选择一天重复')
      return false
    }
    if (formData.period === 2 && formData.monthDays.length <= 0) {
      message.error('至少选择一天重复')
      return false
    }

    emit('change', formData)
    cancel()
  }


  watch(
    () => isEdit,
    (newvalue, oldalue) => {
      if (newvalue.value) {
        formData.period = typeData.value.period
        formData.rate = typeData.value.rate
        if (typeData.value.period === 1) {
          formData.weekDays = typeData.value.days.map(Number)
        } else if (typeData.value.period === 2) {
          formData.monthDays = typeData.value.days.map(Number)
        }
        formData.time = typeData.value.end_time.indexOf('0000-00-00') > -1 ? '' : typeData.value.end_time;
      }
    }, { deep: true, immediate: true },
  )

  defineExpose({ popupOpen })
</script>

<style lang="scss" scoped>
  ::v-deep .uni-popup {
    z-index: 100;
  }

  @import '@/static/css/form-item-list.scss';

  .slider {
    position: relative;
    height: calc(100vh - 464rpx);
    width: 100%;
    background-color: $uni-default-bg;
    border-radius: 16rpx 16rpx 0px 0px;

    .share-header {
      width: 100%;
      padding-top: 40rpx;
      padding-bottom: 28rpx;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .share-title {
        font-size: $uni-font-size-default;
        font-weight: 400;
        color: $uni-text-color;

        &.default-color {
          color: $uni-color-primary;
        }

        &.default-cancel {
          color: $nui-text-color-four;
        }
      }

    }

    ::v-deep .xp-h-full {
      height: 35px;

      .picker-label {
        justify-content: flex-end;
        font-size: $uni-font-size-default;

        &.is-placeholder {
          color: $uni-text-color-five;
        }
      }
    }

    .share-list {
      height: calc(100% - 112rpx);

      .cr-center-list {
        padding-bottom: 20rpx;
        width: auto;
      }

      .weekDays {
        padding: 44rpx 40rpx 24rpx 40rpx;

        .weekDays-list {
          display: flex;
          align-items: center;
          flex-wrap: wrap;
          font-size: $uni-font-size-default;
          color: $uni-text-color;

          .weekDays-list-item {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 20rpx;
            width: 132rpx;
            height: 60rpx;
            margin-right: 34rpx;

            &:nth-last-of-type(4n) {
              margin-right: 0;
            }

            &.active {
              background-color: $uni-color-primary;
              color: #ffffff;
              border-radius: 8rpx;
            }
          }
        }
      }

      .monthDays {
        padding: 44rpx 26rpx 0 26rpx;

        .monthDays-list {
          display: flex;
          flex-wrap: wrap;
          font-size: $uni-font-size-default;
          color: $uni-text-color;

          .monthDays-list-item {
            width: 56rpx;
            height: 56rpx;
            border-radius: 50%;
            margin-right: 44rpx;
            margin-bottom: 34rpx;
            display: flex;
            align-items: center;
            justify-content: center;

            &:nth-of-type(7n) {
              margin-right: 0;
            }

            &.active {
              background-color: $uni-color-primary;
              color: #ffffff;
            }
          }
        }
      }
    }
  }
</style>