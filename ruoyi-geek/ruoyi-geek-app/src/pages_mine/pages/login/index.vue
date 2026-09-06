<script setup>
import modal from '@/plugins/modal'
import { onMounted, ref } from "vue";
import config from '@/config.js'
import useUserStore from '@/store/modules/user'
import { getWxCode } from '@/utils/geek';
import { wxLogin } from '@/api/oauth';
import { setToken } from '@/utils/auth';
import Verify from "@/pages_mine/components/verify/verify"
import { postAction } from '@/utils/request';
const userStore = useUserStore()
const codeUrl = ref("");
const captchaEnabled = ref(true); // 是否开启验证码
const useWxLogin = ref(false); // 是否使用微信登录
const captchaType = ref('blockPuzzle') // < 'char' | 'math' | 'clickWord' | 'blockPuzzle' >
const verify = ref(null);
// #if MP-WEIXIN
useWxLogin.value = true
// #endif
const globalConfig = ref(config);
const loginForm = ref({
  username: "admin",
  password: "admin123",
  code: "",
  uuid: '',
  captcha: {}
});

async function handleLoginByWx() {
  const code = await getWxCode();
  console.log("code:", code);
  const res = await wxLogin('miniapp', code);
  if (res.token == null || res.token == undefined || res.token == "") return
  setToken(res.token);
  await userStore.getInfo();
  uni.switchTab({ url: '/pages/index' });
}

async function handleCheck(data) {
  loginForm.value.captcha = data
  try {
    modal.loading("登录中，请耐心等待...")
    await userStore.login(loginForm.value)
    modal.closeLoading()
    await userStore.getInfo()
    uni.switchTab({ url: '/pages/index' });
  } catch {
    getCode();
    modal.closeLoading()
    throw new Error("验证失败");
  }
}

async function handleLogin() {
  if (loginForm.value.username === "") {
    modal.msgError("请输入您的账号")
  } else if (loginForm.value.password === "") {
    modal.msgError("请输入您的密码")
  } else if (loginForm.value.code === "" && captchaEnabled.value && ['char', 'math'].includes(captchaType.value)) {
    modal.msgError("请输入验证码")
  } else {
    if (['char', 'math'].includes(captchaType.value)) {
      handleCheck({
        ...loginForm.value.captcha,
        captchaType: captchaType.value,
        wordList: loginForm.value.code.split('')
      })
    } else {
      verify.value.show();
    }
  }
};

// 获取图形验证码
function getCode() {
  if (!captchaEnabled.value) return
  if (!['char', 'math'].includes(captchaType.value)) return
  postAction('/captcha/get', { captchaType: captchaType.value }).then(res => {
    codeUrl.value = 'data:image/png;base64,' + res.data.originalImageBase64
    loginForm.value.captcha = res.data
  });
};

// 隐私协议
function handlePrivacy() {
  let site = globalConfig.value.appInfo.agreements[0];
  uni.navigateTo({
    url: `/pages/common/webview/index?title=${site.title}&url=${site.url}`
  });
};
// 用户协议
function handleUserAgrement() {
  let site = globalConfig.value.appInfo.agreements[1]
  uni.navigateTo({
    url: `/pages/common/webview/index?title=${site.title}&url=${site.url}`
  });
};

onMounted(() => {
  getCode();
})
</script>
<template>
  <view class="normal-login-container">
    <view class="logo-content align-center justify-center flex">
      <image style="width: 100rpx;height: 100rpx;" :src="globalConfig.appInfo.logo" mode="widthFix">
      </image>
      <text class="title">若依移动端登录</text>
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
      <view class="input-item flex align-center" style="width: 60%;margin: 0px;"
        v-if="captchaEnabled && ['char', 'math'].includes(captchaType)">
        <view class="iconfont icon-code icon"></view>
        <input v-model="loginForm.code" class="input" placeholder="请输入验证码" maxlength="4" />
        <view class="login-code">
          <image :src="codeUrl" @click="getCode" class="login-code-img"></image>
        </view>
      </view>
      <Verify v-else :mode="'pop'" :captchaType="captchaType" ref="verify" :check="handleCheck"
        :imgSize="{ width: '310px', height: '155px' }" />
      <view class="action-btn">
        <button @click="handleLogin" class="login-btn cu-btn block bg-blue lg round">登录</button>
        <button @click="handleLoginByWx" v-if="useWxLogin"
          class="login-btn cu-btn block bg-green lg round">微信一键登录</button>
      </view>
    </view>

    <view class="xieyi text-center">
      <text class="text-grey1">登录即代表同意</text>
      <text @click="handleUserAgrement" class="text-blue">《用户协议》</text>
      <text @click="handlePrivacy" class="text-blue">《隐私协议》</text>
    </view>
  </view>
</template>
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

    .action-btn {
      margin-top: 40px;

      .login-btn {
        height: 45px;

        &+.login-btn {
          margin-top: 20px;
        }
      }
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
