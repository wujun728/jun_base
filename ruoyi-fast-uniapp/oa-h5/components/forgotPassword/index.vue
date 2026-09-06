<template>
  <view class="forget">
    <uni-popup ref="popupRef" type="right" backgroundColor="#fff" :mask-click="false">
      <view class="forget-content">
        <!-- 非h5页面默认工具栏高度获取 -->
        <view class="status_bar"></view>
        <uni-nav-bar left-icon="left" :border="false" title="" @clickLeft="cancel"></uni-nav-bar>
        <view class="pl10 forget-title">忘记密码</view>
        <view class="form plr10">
          <uni-forms ref="forms" :modelValue="formData" label-position="top">
            <uni-forms-item>
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  <text class="label-item">手机号码</text>
                  <text class="iconfont">*</text>
                </view>
              </template>
              <uni-easyinput :maxlength="11" v-model="formData.phone" @input="inputPhone" :clearable="false"
                :styles="{borderColor: 'transparent'}" placeholder="请输入手机号码" />
            </uni-forms-item>
            <uni-forms-item class="forms-item-text" label="验证码">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  <text class="label-item">验证码</text>
                  <text class="iconfont">*</text>
                </view>
              </template>
              <uni-easyinput :maxlength="6" v-model="formData.verification_code" :clearable="false"
                :styles="{borderColor: 'transparent'}" placeholder="请输入验证码" />
              <text class="item-text" :class="isSendCode && !disabled ? 'default-color': 'default-text-color-four'"
                @click="sendVerificationCode">{{text}}</text>
            </uni-forms-item>
            <uni-forms-item class="forms-item-text">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  <text class="label-item">密码</text>
                  <text class="iconfont">*</text>
                </view>
              </template>
              <uni-easyinput v-model="formData.password" :clearable="false" type="password"
                :styles="{borderColor: 'transparent'}" placeholder="请输入密码" />
            </uni-forms-item>
          </uni-forms>
          <button class="preserve" type="primary" :loading="loading"
            :disabled="!(isSendCode && formData.verification_code.length >= 4 && formData.password.length >=6)"
            @click="handlePreserve">提交</button>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
  import { ref, reactive } from 'vue'
  const popupRef = ref(null)
  // 获取验证码-组合式函数
  import { useSendCode, useCmsKeyVerify } from '@/utils/useVerifyCode'
  import message from '@/utils/message'
  let { text, disabled } = useSendCode()
  // 发送验证码逻辑
  let { getKeyVerify } = useCmsKeyVerify()
  let formData = reactive({
    phone: '',
    password: '',
    password_confirm: '',
    verification_code: ''
  })
  let isSendCode = ref(false)
  let loading = ref(false)

  // 打开
  const popupOpen = () => {
    popupRef.value.open()
  }
  // 关闭
  const cancel = () => {
    popupRef.value.close()
  }

  // 重置
  const reset = () => {
    formData.phone = ''
    formData.verification_code = ''
    formData.password = ''
    formData.password_confirm = ''
  }

  // 手机号码验证
  import { phoneReg } from '@/utils/helper'
  const inputPhone = (e) => {
    isSendCode.value = phoneReg.test(e)
  }


  const sendVerificationCode = () => {
    if (!isSendCode.value) return false
    getKeyVerify(formData.phone)
  }

  import { savePasswordApi } from '@/api/public'
  const handlePreserve = () => {
    if (formData.phone && !phoneReg.test(formData.phone)) {
      message.error('电话号码不合法')
      return false
    }

    formData.password_confirm = formData.password
    loading.value = true

    savePasswordApi(formData).then((res) => {
      loading.value = false
      message.success(res.message)
      cancel()
      reset()
    }).catch((error) => {
      loading.value = false
      message.error(error.message)
    })
  }

  defineExpose({ popupOpen })
</script>

<style lang="scss" scoped>
  .forget {
    ::v-deep .uni-popup__wrapper.right {
      padding-top: 0;
    }

    .forget-content {
      width: 100vw;

      .forget-title {
        font-weight: 600;
        padding-top: 52rpx;
        color: #303133;
        font-size: 44rpx;
      }

      .form {
        padding-top: 30rpx;

        input {
          border: none;
        }

        ::v-deep .segmented-control__item {
          flex: none;
          margin-right: 40rpx;
        }

        ::v-deep .uni-forms-item__label {
          height: auto;
          padding: 0;
          font-size: 22rpx;
          line-height: 22rpx;
          color: $nui-text-color-two;

          .iconfont {
            color: #dd524d;
            font-weight: bold;
          }
        }

        ::v-deep .uni-easyinput__content {
          border: none;

          .uni-easyinput__content-input {
            border-bottom: 1px solid #E4E7ED;
            height: 42px;
            padding: 0 !important;
          }

          .uni-easyinput__placeholder-class {
            font-size: $uni-font-size-default;
            color: $uni-text-color-five;
          }

          .uni-input-input {
            font-size: $uni-font-size-default;
            color: $uni-text-color;
          }
        }

        .forms-item-text {
          position: relative;

          .item-text {
            position: absolute;
            right: 0;
            bottom: 13px;
          }
        }

        uni-button {
          font-size: $uni-font-size-default;
          height: 86rpx;
          line-height: 86rpx;
        }
      }
    }
  }
</style>