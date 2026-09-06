<template>
  <view class="examine-from">
    <view class="examine-from-list">
      <view v-for="(item,index) in options" :key="index" class="">
        <uni-row class="examine-from-list-item">
          <uni-col :span="8" class="examine-from-left">{{item.label}}</uni-col>
          <uni-col v-if="!Array.isArray(item.value)" :span="16"
            class="examine-from-right">{{item.value||'--'}}</uni-col>
          <uni-col v-else :span="18" class="examine-from-right">
            <upload-from-list v-if="item.value && item.value.length > 0"
              :upload-from-data="item.value"></upload-from-list>
            <text v-else>--</text>
          </uni-col>
        </uni-row>
      </view>
    </view>
  </view>
</template>

<script setup>
  import { reactive, toRefs } from "vue"
  import uploadFromList from './uploadFromList.vue'
  import checkApprovalBill from './checkApprovalBill.vue'
  const props = defineProps({
    options: {
      type: Array,
      default () {
        return []
      }
    }
  })
  const copyText = (val) => {
    uni.setClipboardData({
      data: val,
      success() {
        uni.showToast({
          title: '已复制到剪贴板',
          icon: 'none',
          position: 'top'
        })
      }
    })
  }

  const getLabel = (options, name) => {
    let text = ''
    if (options) {
      options.map((item) => {
        if (item.value == name) {
          text = item.label
        }
      })
      return text
    }
  }
  const { options } = toRefs(props)
  const data = reactive({
    directDisplay: ['input', 'inputNumber', 'moneyFrom', 'datePicker', 'radio']
  })
</script>

<style scoped lang="scss">
  .examine-from {
    width: 100%;
    padding: 36rpx 24rpx;

    .examine-from-list {
      .examine-from-list-item {
        font-size: 28rpx;
        margin-bottom: 20rpx;
        display: flex;
        align-items: center;

        &:last-of-type {
          margin-bottom: 0;
        }

        .examine-from-left {
          color: $nui-text-color-four;
        }

        .examine-from-right {
          color: $uni-text-color;
          line-height: 42rpx;
        }
      }
    }
  }
</style>