<template>
  <view>
    <uni-popup ref="popupRef" type="dialog" :mask-click="true">
      <uni-popup-dialog ref="inputClose" mode="input" :before-close="true" :title="configData.title"
        :value="configData.value" @close="cancel" @confirm="dialogInputConfirm">
        <uni-easyinput v-model="folderName" type="textarea" :inputBorder="false" :maxlength="255"
          :placeholder="configData.placeholder" placeholderStyle="color: #C0C4CC;font-size: 30rpx;">
        </uni-easyinput>
      </uni-popup-dialog>
    </uni-popup>
  </view>
</template>

<script setup>
  import { ref, toRefs, watch } from 'vue'
  import message from '@/utils/message'
  import { mailboxReg } from '@/utils/helper'
  const props = defineProps({
    configData: {
      type: Object,
      default: () => {
        return {}
      },
      required: true
    }
  })

  const emit = defineEmits(['change'])
  const { configData } = toRefs(props)
  const popupRef = ref(null)
  const folderName = ref('')
  const popupOpen = val => {
    popupRef.value.open()
    if (val) {
      folderName.value = val
    }
  }

  // 关闭验证码
  const cancel = () => {
    popupRef.value.close()
  }

  const dialogInputConfirm = () => {
    if (!folderName.value) {
      message.error('输入的内容不能为空')
      return false
    }
    const data = {
      value: folderName.value,
      type: configData.value.type
    }
    emit('change', data)
    folderName.value = ''
    cancel()
  }

  watch(configData, (newvalue, oldvalue) => {
    //内容监听
    if (newvalue.text) {
      folderName.value = newvalue.text
    }
  }, {
    deep: false,
    immediate: true,
  })

  defineExpose({ popupOpen })
</script>

<style lang="scss" scoped>
  ::v-deep .uni-popup-dialog {
    .uni-dialog-title {
      padding-top: 40rpx;

      .uni-dialog-title-text {
        font-size: $uni-font-size-default;
        font-weight: $uni-default-font-weight;
        color: #2B2C32;
      }
    }

    .uni-dialog-button-text {
      font-size: $uni-font-size-default;
    }
  }

  ::v-deep .uni-dialog-content {
    padding: 24rpx 32rpx 36rpx 32rpx;
    flex-wrap: wrap;
  }

  ::v-deep .uni-easyinput__content {
    background-color: #F0F1F5 !important;
    border-radius: 12rpx;

    .uni-easyinput__content-textarea {
      font-size: 28rpx;
      margin: 0;
      padding: 20rpx;
    }

    .content-clear-icon {
      color: #C0C4CC !important;
    }
  }
</style>