<template>
  <view class="normal-login-container">
    <view class="logo-content align-center justify-center flex">
      <image style="width: 100rpx;height: 100rpx;" :src="globalConfig.appInfo.logo" mode="widthFix">
      </image>
      <text class="title">若依管理系统登录</text>
    </view>
    <view class="login-form-content">
      <view class="input-item flex align-center">
        <view class="iconfont icon-user icon"></view>
        <input v-model="loginForm.username" class="input" type="text" placeholder="请输入账号" maxlength="30" />
      </view>
      <view class="input-item flex align-center">
        <view class="iconfont icon-password icon"></view>
        <input v-model="loginForm.password" type="password" class="input" placeholder="请输入密码" maxlength="20" />
      </view>
      <view class="input-item flex align-center" style="width: 60%;margin: 0px;" v-if="captchaEnabled">
        <view class="iconfont icon-code icon"></view>
        <input v-model="loginForm.code" type="number" class="input" placeholder="请输入验证码" maxlength="4" />
        <view class="login-code"> 
          <image :src="codeUrl" @click="getCode" class="login-code-img"></image>
        </view>
      </view>
      <view class="action-btn">
        <button @click="handleLogin" class="login-btn cu-btn block bg-blue lg round">登录</button>
      </view>
      <view class="reg text-center" v-if="register">
        <text class="text-grey1">没有账号？</text>
        <text @click="handleUserRegister" class="text-blue">立即注册</text>
      </view>
      <view class="xieyi text-center">
        <text class="text-grey1">登录即代表同意</text>
        <text @click="handleUserAgrement" class="text-blue">《用户协议》</text>
        <text @click="handlePrivacy" class="text-blue">《隐私协议》</text>
      </view>
    </view>
    
    <!-- SMS Verification Modal -->
    <view class="cu-modal" :class="smsDialogVisible ? 'show' : ''">
      <view class="cu-dialog">
        <view class="cu-bar bg-white justify-end">
          <view class="content">请输入手机验证码</view>
          <view class="action" @click="cancelSms">
            <text class="cuIcon-close text-red"></text>
          </view>
        </view>
        <view class="padding-xl">
           <input v-model="loginForm.smsCode" class="uni-input" type="number" placeholder="请输入收到的验证码" />
        </view>
        <view class="cu-bar bg-white justify-end">
          <view class="action">
            <button class="cu-btn line-green text-green" @click="cancelSms">取消</button>
            <button class="cu-btn bg-green margin-left" @click="handleLoginBySms">确定</button>
          </view>
        </view>
      </view>
    </view>
     
  </view>
</template>

<script setup>
  import { ref, getCurrentInstance } from "vue"
  import { onLoad } from  "@dcloudio/uni-app"
  import { getToken } from '@/utils/auth'
  import { getCodeImg } from '@/api/login'
  import { useConfigStore, useUserStore } from '@/store'

  const { proxy } = getCurrentInstance()
  const globalConfig = useConfigStore().config
  const codeUrl = ref("")
  // 验证码开关
  const captchaEnabled = ref(true)
  // 用户注册开关
  const register = ref(false)
  const loginForm = ref({
    username: "admin",
    password: "jyx_692483",
    code: "",
    uuid: "",
    smsCode: ""
  })
  const smsDialogVisible = ref(false)
  const secondaryVerificationEnabled = globalConfig.secondaryVerificationEnabled === undefined ? false : globalConfig.secondaryVerificationEnabled

  // 用户注册
  function handleUserRegister() {
    proxy.$tab.redirectTo(`/pages/register`)
  }

  // 隐私协议
  function handlePrivacy() {
    let site = globalConfig.appInfo.agreements[0]
    proxy.$tab.navigateTo(`/pages/common/webview/index?title=${site.title}&url=${site.url}`)
  }

  // 用户协议
  function handleUserAgrement() {
    let site = globalConfig.appInfo.agreements[1]
    proxy.$tab.navigateTo(`/pages/common/webview/index?title=${site.title}&url=${site.url}`)
  }

  // 获取图形验证码
  function getCode() {
    getCodeImg().then(res => {
      captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (captchaEnabled.value) {
          codeUrl.value = 'data:image/gif;base64,' + res.img
          loginForm.value.uuid = res.uuid
        }
    })
  }

  // 登录方法
  async function handleLogin() {
    if (loginForm.value.username === "") {
      proxy.$modal.msgError("请输入账号")
    } else if (loginForm.value.password === "") {
      proxy.$modal.msgError("请输入密码")
    } else if (loginForm.value.code === "" && captchaEnabled.value) {
      proxy.$modal.msgError("请输入验证码")
    } else {
      proxy.$modal.loading("登录中，请耐心等待...")
      pwdLogin()
    }
  }

  // 密码登录
  async function pwdLogin() {
    useUserStore().login(loginForm.value).then(() => {
      proxy.$modal.closeLoading()
      loginSuccess()
    }).catch((e) => {
      if (e === "sms" && secondaryVerificationEnabled) {
        proxy.$modal.closeLoading()
        smsDialogVisible.value = true
      } else {
        if (captchaEnabled.value) {
          getCode()
        }
      }
    })
  }

  function cancelSms() {
    smsDialogVisible.value = false;
    loginForm.value.smsCode = "";
  }

  function handleLoginBySms() {
     if (loginForm.value.smsCode && loginForm.value.smsCode.length === 6) {
        smsDialogVisible.value = false;
        proxy.$modal.loading("验证中...")
        pwdLogin();
     } else {
        proxy.$modal.msgError("请输入6位验证码")
     }
  }

  // 登录成功后，处理函数
  function loginSuccess(result) {
    // 设置用户信息
    useUserStore().getInfo().then(res => {
      proxy.$tab.reLaunch('/pages/index')
    })
  }

  onLoad(() => {
    //#ifdef H5
    if (getToken()) {
      proxy.$tab.reLaunch('/pages/index')
    }
    //#endif
  })

  getCode()
</script>

<style lang="scss" scoped>
  page {
    background-color: #ffffff;
  }

  .normal-login-container {
    width: 100%;

    .logo-content {
      width: 100%;
      font-size: 21px;
      text-align: center;
      padding-top: 15%;

      image {
        border-radius: 4px;
      }

      .title {
        margin-left: 10px;
      }
    }

    .login-form-content {
      text-align: center;
      margin: 20px auto;
      margin-top: 15%;
      width: 80%;

      .input-item {
        margin: 20px auto;
        background-color: #f5f6f7;
        height: 45px;
        border-radius: 20px;

        .icon {
          font-size: 38rpx;
          margin-left: 10px;
          color: #999;
        }

        .input {
          width: 100%;
          font-size: 14px;
          line-height: 20px;
          text-align: left;
          padding-left: 15px;
        }

      }

      .login-btn {
        margin-top: 40px;
        height: 45px;
      }
      
      .reg {
        margin-top: 15px;
      }
      
      .xieyi {
        color: #333;
        margin-top: 20px;
      }
      
      .login-code {
        height: 38px;
        float: right;
      
        .login-code-img {
          height: 38px;
          position: absolute;
          margin-left: 10px;
          width: 200rpx;
        }
      }
    }
  }
</style>
