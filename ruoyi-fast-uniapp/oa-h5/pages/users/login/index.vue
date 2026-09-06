<template xmlns="">
  <view class="content">
    <image class="login-image" src="@/static/image/login-bg.png"></image>
    <view v-if="serverConfigInfo.length > 1" class="enterprise" @click="goUrl('/pages/users/login/enterprise')">
      <i class="iconfont icon-yunwenjiandanchuang-fuzhizhi"></i>
      <text>切换企业</text>
    </view>
    <view class="content-login p-l-r30">
      <image class="logo" :src="chooseEnterprise?.logo || '../../../static/image/logo.png'"></image>
      <view class="title">{{ chooseEnterprise?.enterprise_name || '登录派瑞集团 · 企业助手' }}</view>
      <uni-segmented-control :current="index" :values="items" @clickItem="onClickItem" styleType="text"
        activeColor="#1890FF"></uni-segmented-control>
      <view class="form">
        <uni-forms ref="forms" :modelValue="formData" label-position="top">
          <template v-if="index === 0">
            <uni-forms-item label="手机号">
              <uni-easyinput v-model="formData.phone" :maxlength="11" :clearable="false" @input="inputPhone"
                :adjust-position="false" :styles="{ borderColor: 'transparent' }" placeholder="请输入手机号码" />
            </uni-forms-item>
            <uni-forms-item class="forms-item-text" label="密码">
              <uni-easyinput v-model="formData.password" :clearable="false" type="password" :adjust-position="false"
                :styles="{ borderColor: 'transparent' }" placeholder="请输入密码" />
              <text class="default-color item-text" @click="handleForget">忘记密码</text>
            </uni-forms-item>
          </template>
          <template v-if="index === 1">
            <uni-forms-item label="手机号">
              <uni-easyinput v-model="formData.phone" :clearable="false" :maxlength="11" :adjust-position="false"
                @input="inputPhone" :styles="{ borderColor: 'transparent' }" placeholder="请输入手机号码" />
            </uni-forms-item>
            <uni-forms-item class="forms-item-text" label="验证码">
              <uni-easyinput v-model="formData.verificationCode" :maxlength="6" :clearable="false"
                :adjust-position="false" :styles="{ borderColor: 'transparent' }" placeholder="请输入验证码" />
              <text class="item-text" :class="isSendCode && !disabled ? 'default-color' : 'default-text-color-four'"
                @click="sendVerificationCode">{{ text }}
              </text>
            </uni-forms-item>
          </template>
        </uni-forms>
      </view>
      <view class="display-align agree-content">
        <checkbox-group @change="changeAgree">
          <label>
            <checkbox style="transform: scale(0.7)" value="1" :checked="agree" />
            我已阅读并同意
          </label>
        </checkbox-group>
        <navigator url="/pages/users/login/service" animation-type="slide-in-right" class="default-color">《服务协议》
        </navigator>
        和
        <navigator url="/pages/users/login/privacy" animation-type="slide-in-right" class="default-color">《隐私协议》
        </navigator>
      </view>
      <button class="preserve" type="primary" :loading="loading" :disabled="handleVerButton()" @click="handlePreserve">登
        录</button>
      <view class="login-text">
        {{ index ? '未注册手机号登录时会自动创建新账号' : '未注册用户请使用短信登录' }}
      </view>
    </view>

    <view class="config" v-if="data.hidshow">
      <view class="copy"> 登陆派瑞集团管理系统 </view>
    </view>
    <!-- 忘记密码 -->
    <forgot-password ref="forgotPasswordRef"></forgot-password>
  </view>
</template>

<script setup>
  import forgotPassword from '@/components/forgotPassword/index.vue'
  import {
    phoneReg
  } from '@/utils/helper'
  import {
    loginApi,
    phoneLoginApi
  } from '@/api/public'
  import message from '@/utils/message'
  import {
    loginInfo,
    loginMenus,
    messageCateApi
  } from '@/api/user'
  import {
    autoLoad
  } from '@/utils/autoload'
  import {
    onLoad
  } from '@dcloudio/uni-app'
  // 获取vuex存取变量
  import {
    useStore
  } from 'vuex'
  import webSocketClass from '@/utils/webSocket'
  import {
    VUE_APP_WS_URL
  } from '@/config/app'
  // 获取验证码-组合式函数
  import {
    useCmsKeyVerify,
    useSendCode
  } from '@/utils/useVerifyCode'
  import {
    onMounted
  } from 'vue'

  let {
    text,
    disabled
  } = useSendCode()
  let {
    getKeyVerify
  } = useCmsKeyVerify()
  let store = useStore()
  let forgotPasswordRef = ref(null)
  // 默认企业
  let serverConfigInfo = uni.getStorageSync('serverConfigInfo') || []
  // 当前企业
  let chooseEnterprise = uni.getStorageSync('chooseEnterprise') || []
  // 切换登录
  let items = reactive(['密码登录', '手机登录'])
  let index = ref(0)
  let agree = ref(false)
  let isSendCode = ref(false)
  let formData = reactive({
    phone: '',
    password: '',
    verificationCode: '',
    agreeArr: [],
  })
  const data = reactive({
    type: 0,
    hidshow: true,
    docmHeight: 0, //默认屏幕高度
    showHeight: 0, //实时屏幕高度

  })
  onLoad((options) => {
    data.hidshow = true
    // #ifdef H5
    data.docmHeight = document.documentElement.clientHeight
    data.showHeight = document.documentElement.clientHeight
    // #endif
    data.type = options.type || 0
  })
  const placeholderStyle = ref('color: #303133')
  onMounted(() => {
    // #ifdef H5
    window.onresize = () => {
      return (() => {
        data.showHeight = document.body.clientHeight
      })()
    }
    // #endif
  })

  // 监听数据变化
  // #ifdef H5
  watch(
    () => [data.showHeight],
    (newvalue) => {
      if (data.docmHeight > newvalue[0]) {
        data.hidshow = false
      } else {
        data.hidshow = true
      }
    }
  )
  // #endif

  // 标签切换
  function onClickItem(e) {
    index.value = e.currentIndex
  }

  // 发送验证码
  const sendVerificationCode = () => {
    if (!isSendCode.value || disabled.value) return false
    getKeyVerify(formData.phone)
  }

  // 忘记密码
  const handleForget = () => {
    forgotPasswordRef.value.popupOpen()
  }

  // 手机号码验证
  const inputPhone = (e) => {
    isSendCode.value = phoneReg.test(e)
  }

  const changeAgree = (e) => {
    formData.agreeArr = e.detail.value
  }

  const handleVerButton = () => {
    if (index.value === 1 && isSendCode.value && formData.verificationCode.length >= 4) {
      return false
    } else if (index.value === 0 && isSendCode.value && formData.password.length >= 6) {
      return false
    } else {
      return true
    }
  }

  // 登录提交
  const handlePreserve = () => {
    if (!formData.phone) {
      message.error('电话号码不能为空')
      return false
    }
    if (index.value === 0 && formData.phone && !phoneReg.test(formData.phone)) {
      message.error('电话号码不合法')
      return false
    }
    if (index.value === 0 && !formData.password) {
      message.error('密码不能为空')
      return false
    }
    if (index.value === 1 && !formData.verificationCode) {
      message.error('验证码不能为空')
      return false
    }
    if (formData.agreeArr.length <= 0) {
      message.error('请阅读用户协议')
      return false
    }
    if (index.value === 0) {
      const data = {
        account: formData.phone,
        password: formData.password,
      }
      //#ifdef APP-PLUS
      let info = plus.push.getClientInfo()
      data.client_id = info.clientid
      //#endif
      login(data)
    } else if (index.value === 1) {
      const data = {
        phone: formData.phone,
        verification_code: formData.verificationCode,
      }
      //#ifdef APP-PLUS
      let info = plus.push.getClientInfo()
      data.client_id = info.clientid
      //#endif
      phoneLogin(data)
    }
    let titleList = []
    uni.setStorageSync('titleList', JSON.stringify(titleList))
  }

  const loading = ref(false)
  // 验证码登录
  const phoneLogin = (data) => {
    loading.value = true
    phoneLoginApi(data)
      .then((res) => {
        loading.value = false
        if (res.status === 200) {
          const data = res.data
          store.commit('login', data)
          message.success('登录成功')
          loginSuccess()
        }
      })
      .catch((error) => {
        loading.value = false
        message.error(error.message)
      })
  }
  // 账号登录
  const login = (data) => {
    loading.value = true
    loginApi(data)
      .then((res) => {
        loading.value = false
        if (res.status === 200) {
          const data = res.data
          store.commit('login', data)
          message.success('登录成功')
          loginSuccess()
        }
      })
      .catch((error) => {
        loading.value = false
        message.error(error.message)
      })
  }
  const getInfo = async () => {
    const res = await loginInfo()
    store.commit('loginInfo', res.data)
  }
  const getMenus = async () => {
    const res = await loginMenus()
    store.commit('loginMenu', res.data)
  }
  const getNum = () => {
    messageCateApi().then((res) => {
      let arr = res.data
      let num = 0

      arr.map((item) => {
        num += Number(item.count)
      })

      if (num === 0) {
        uni.removeTabBarBadge({
          index: 1,
        })
      } else {
        uni.setTabBarBadge({
          index: 1, // 人脉页面在底部菜单栏的索引
          text: num > 99 ? '99+' : num, // 要显示的文本（必须是字符串类型）
        })
      }
    })
  }
  const goUrl = (url) => {
    uni.reLaunch({
      url: url + '?type=' + data.type,
    })
  }

  // 登录成功后页面跳转
  const loginSuccess = async () => {
    await getInfo()
    await getMenus()
    getNum()
    let backUrl = store.state.app.loginBackUrl || '/pages/index/index'
    if (backUrl.indexOf('/pages/users/login/index') !== -1) {
      backUrl = '/pages/index/index'
    }
    store.commit('setLoginBackUrl', '')
    autoLoad()

    // backUrl = '/pages/index/index'
    await uni.reLaunch({
      url: backUrl,
    })
    let token = store.state.app.token
    let wsUrl = `${VUE_APP_WS_URL}/ws?type=ent&token=${token}`
    const socket = new webSocketClass(wsUrl, 10)
    socket.initSocket()
  }
</script>
<style>
  page {
    background-color: #fff;
  }
</style>

<style scoped lang="scss">
  .content {
    position: relative;
    height: 100vh;
    background-color: #fff;
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;

    .login-image {
      width: 100%;
      // height: 420rpx;
    }

    .enterprise {
      font-size: 28rpx;
      color: #303133;
      position: absolute;
      width: 100%;
      right: 30rpx;
      top: 30rpx;
      text-align: right;
      margin-bottom: 60rpx;
      // #ifdef APP-PLUS
      top: 80rpx;

      // #endif
      i {
        margin-right: 4rpx;
      }
    }

    .content-login {
      position: absolute;
      width: 100%;
      // #ifndef APP-PLUS
      top: 80rpx;
      // #endif
      // #ifdef APP-PLUS
      top: 160rpx;
      // #endif

      .logo {
        width: 80rpx;
        height: 80rpx;
      }

      .title {
        font-size: 46rpx;
        padding: 42rpx 0 28rpx 0;
        font-weight: 700;
      }

      .form {
        width: 100%;
        padding-top: 30rpx;
      }

      input {
        border: none;
      }

      ::v-deep .segmented-control__item {
        flex: none;
        color: $nui-text-color-four !important;
        margin-right: 40rpx;
      }

      .preserve {
        margin-top: 40rpx;
        font-size: 30rpx;
        height: 86rpx;
        line-height: 86rpx;
      }

      .login-text {
        width: 100%;
        text-align: center;
        padding-top: 32rpx;
        color: $nui-text-color-four;
        font-size: 26rpx;
      }

      ::v-deep .uni-forms-item__label {
        height: auto;
        font-size: 22rpx;
        line-height: 22rpx;
        color: $nui-text-color-two;
        padding: 0;
      }

      ::v-deep .uni-easyinput__content {
        border: none;

        .uni-easyinput__content-input {
          border-bottom: 1px solid #e4e7ed;
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

      .agree-content {
        margin-top: 10rpx;
        font-size: 26rpx;

        color: $nui-text-color-four;

        ::v-deep .uni-label-pointer {
          display: flex;
          align-items: center;

          .uni-checkbox-input {
            border-radius: 50%;
          }
        }
      }

      .forms-item-text {
        position: relative;

        ::v-deep .content-clear-icon {
          display: none;
        }

        .item-text {
          position: absolute;
          right: 0;
          bottom: 13px;
        }
      }
    }
  }

  .login-bottom {
    width: 100%;
    margin-top: 90%;

    uni-button {
      background-color: transparent;
      border: 1px solid #e4e7ed;
      border-radius: 32rpx;
      color: #606266;
      font-size: 26rpx;
      line-height: 64rpx;
      height: 64rpx;
      width: 328rpx;

      .icon-login {
        font-size: 26rpx;
        padding-right: 14rpx;
      }

      &::after {
        border: none;
      }
    }
  }

  .config {
    position: absolute;
    bottom: 100rpx;
    left: 50%;
    transform: translateX(-50%);

    .config-btn {
      width: 328rpx;
      height: 64rpx;
      border-radius: 32rpx 32rpx 32rpx 32rpx;
      border: 2rpx solid #e4e7ed;
      margin-left: 100rpx;
      display: flex;
      align-items: center;
      justify-content: center;

      image {
        width: 30rpx;
        height: 26rpx;
        margin-right: 14rpx;
      }

      text {
        font-size: 26rpx;
      }
    }

    .copy {
      width: 528rpx;
      font-size: 24rpx;
      color: #909399;
      margin-top: 40rpx;
      text-align: center;
    }
  }
</style>