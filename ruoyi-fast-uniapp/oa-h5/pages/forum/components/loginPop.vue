<template>
  <view>
    <!-- 输入框示例 -->
    <uni-popup ref="inputDialog" type="dialog" :is-mask-click="true">
      <view class="login">
        <view class="login-text">
          登录
        </view>
        <uni-forms ref="forms" :modelValue="formData" label-position="left">
          <uni-forms-item>
            <view class="flex">
              <view class="iconfont icon-danchuang-shoujihao" />
              <uni-easyinput type="number" v-model="formData.phone" :clearable="true" :maxlength="11"
                @input="inputPhone" :styles="{borderColor: 'transparent'}" placeholder="请输入手机号码" />
            </view>
          </uni-forms-item>
          <uni-forms-item>
            <view class="flex">
              <view class="iconfont icon-danchuang-yanzhengma" />
              <uni-easyinput type="number" v-model="formData.verificationCode" :maxlength="6" :clearable="false"
                :styles="{borderColor: 'transparent'}" placeholder="请输入验证码" />
              <text class="item-text" :class="isSendCode && !disabled ? 'default-color': 'default-text-color-four'"
                @click="sendVerificationCode">{{text}}</text>
            </view>
          </uni-forms-item>
          <button class="preserve" type="primary" :loading="loading" @click="handlePreserve">登录</button>
        </uni-forms>
      </view>

    </uni-popup>

  </view>
</template>

<script setup lang="ts">
  import { phoneReg } from '@/utils/helper'
  import { articleUserLoginApi } from '@/api/forum'
  import { useStore } from 'vuex'
  import { useSendCode, useCmsKeyVerify } from '@/utils/useVerifyCode'
  const store = useStore()

  import message from '@/utils/message'
  const userInfo = computed(() => store.state.app.userInfo)
  let { text, disabled, sendCode } = useSendCode()
  let { getKeyVerify } = useCmsKeyVerify()
  let formData = reactive({
    phone: '',
    verificationCode: '',
  })
  const loading = ref(false)
  const inputDialog = ref(null)
  let isSendCode = ref(true)
  // 登录对话框
  const inputDialogToggle = () => {
    formData.phone = userInfo.value.phone
    inputDialog.value.open()
  }
  // 登录
  const handlePreserve = () => {
    if (!formData.phone) {
      message.error('电话号码不能为空')
      return false
    }
    if (!formData.verificationCode) {
      message.error('验证码不能为空')
      return false
    }
    const data = {
      phone: formData.phone,
      captcha: formData.verificationCode
    }
    // const emit = defineEmits(['loginOk'])
    // console.log(emit, 8888)

    loading.value = true
    articleUserLoginApi(data).then((res : Res) => {
      loading.value = false
      if (res.status === 200) {
        message.success('登录成功')
        // emit('loginOk')
        inputDialog.value.close()
      }
    }).catch((error : Res) => {
      loading.value = false
      message.error(error.message)
    })
  }

  // 发送验证码
  const sendVerificationCode = () => {
    if (!isSendCode.value || disabled.value) return false
    getKeyVerify(formData.phone, 1)
  }
  const inputPhone = (e : string) => {
    isSendCode.value = phoneReg.test(e)
  }
  defineExpose({ inputDialogToggle })
</script>

<style lang="scss">
  .cr-position-header {
    position: sticky;
    background-color: #fff;
  }

  .flex {
    display: flex;
  }

  .iconfont {
    font-size: 38rpx !important;
    margin-top: 22rpx;
    margin-right: 32rpx;
    color: #303133;
  }

  .item-text {
    position: absolute;
    right: 0;
    bottom: 13px;
    font-size: 24rpx;
  }

  .preserve {
    font-size: 30rpx;
    font-family: PingFang SC-Regular, PingFang SC,serif;
    font-weight: 400;
    color: #FFFFFF;
    background-color: #1890FF;
    height: 76rpx;
    line-height: 76rpx;
  }

  .login {
    border-radius: 8px;
    width: 560rpx;
    height: 520rpx;
    padding: 0 13px;
    background-image: url(@/static/image/login.png);
    background-repeat: no-repeat;
    background-size: 560rpx 314rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;
    background-color: #fff;

    .login-text {
      font-size: 36rpx;
      font-family: PingFang SC-Medium, PingFang SC,serif;
      font-weight: 500;
      color: #303133;
      margin: 0 auto 64rpx;
    }
  }

  .forum-line-style {
    border-top: 1px solid $uni-line-style-color-three;
  }

  .content {
    width: 100%;
  }

  ::v-deep .uni-popup__error {
    color: #1890FF;

  }

  ::v-deep .uni-easyinput__content {
    border: none;

    .uni-easyinput__content-input {
      border-bottom: 1px solid #E4E7ED;
      height: 42px;
      padding: 0 !important;

    }

    .uni-easyinput__placeholder-class {
      font-size: 30rpx;
      color: $uni-text-color-five;

    }

    .uni-input-input {
      font-size: $uni-font-size-default;
      color: $uni-text-color;
    }

  }
</style>