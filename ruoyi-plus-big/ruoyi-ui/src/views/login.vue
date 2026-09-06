<template>
  <div class="login">
    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">{{ title }}</h3>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          size="large"
          auto-complete="off"
          placeholder="账号"
        >
          <template #prefix><svg-icon icon-class="user" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          size="large"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter="handleLogin"
          show-password
        >
          <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          size="large"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter="handleLogin"
        >
          <template #prefix><svg-icon icon-class="validCode" class="el-input__icon input-icon" /></template>
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="large"
          type="primary"
          style="width:100%;"
          @click.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2018-2025 ruoyi.vip All Rights Reserved.</span>
    </div>
  </div>
</template>

<script setup>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from "@/utils/jsencrypt"
import useUserStore from '@/store/modules/user'

const title = import.meta.env.VITE_APP_TITLE
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()

const loginForm = ref({
  username: "admin",
  password: "",
  rememberMe: false,
  code: "",
  uuid: ""
})

const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
}

const codeUrl = ref("")
const loading = ref(false)
// 验证码开关
const captchaEnabled = ref(true)
// 注册开关
const register = ref(false)
const redirect = ref(undefined)

watch(route, (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect
}, { immediate: true })

function handleLogin() {
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 30 })
        Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 })
        Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 })
      } else {
        // 否则移除
        Cookies.remove("username")
        Cookies.remove("password")
        Cookies.remove("rememberMe")
      }
      // 调用action的登录方法
      userStore.login(loginForm.value).then(() => {
        const query = route.query
        const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
          if (cur !== "redirect") {
            acc[cur] = query[cur]
          }
          return acc
        }, {})
        router.push({ path: redirect.value || "/", query: otherQueryParams })
      }).catch(() => {
        loading.value = false
        // 重新获取验证码
        if (captchaEnabled.value) {
          getCode()
        }
      })
    }
  })
}

function getCode() {
  getCodeImg().then(res => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img
      loginForm.value.uuid = res.uuid
    }
  })
}

function getCookie() {
  const username = Cookies.get("username")
  const password = Cookies.get("password")
  const rememberMe = Cookies.get("rememberMe")
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  }
}

getCode()
getCookie()
</script>

<style lang='scss' scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background:
    radial-gradient(circle at left top, rgba(47, 123, 255, 0.22), transparent 28%),
    radial-gradient(circle at right top, rgba(52, 208, 255, 0.16), transparent 22%),
    linear-gradient(180deg, rgba(2, 11, 29, 0.98) 0%, rgba(1, 7, 19, 1) 100%);
  position: relative;

  &::before {
    content: "";
    position: absolute;
    inset: 0;
    background-image:
      linear-gradient(rgba(55, 126, 255, 0.08) 1px, transparent 1px),
      linear-gradient(90deg, rgba(55, 126, 255, 0.08) 1px, transparent 1px);
    background-size: 24px 24px;
    pointer-events: none;
  }
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: var(--screen-text-primary);
  letter-spacing: 0.12em;
}

.login-form {
  border-radius: 18px;
  background: linear-gradient(180deg, rgba(10, 28, 60, 0.96) 0%, rgba(7, 20, 43, 0.96) 100%);
  width: 400px;
  padding: 30px 28px 10px 28px;
  z-index: 1;
  border: 1px solid var(--screen-border);
  box-shadow: var(--screen-shadow), 0 0 30px rgba(52, 208, 255, 0.08);
  backdrop-filter: blur(14px);

  .el-input {
    height: 40px;
    --el-input-bg-color: transparent;
    --el-fill-color-blank: transparent;
    --el-input-border-color: rgba(72, 145, 255, 0.18);
    --el-input-hover-border-color: rgba(52, 208, 255, 0.36);
    --el-input-focus-border-color: rgba(52, 208, 255, 0.5);
    --el-input-text-color: #eef6ff;
    --el-text-color-placeholder: rgba(189, 211, 242, 0.58);

    input {
      height: 40px;
    }
  }

  :deep(.el-input__wrapper) {
    background: transparent !important;
    box-shadow: 0 0 0 1px rgba(72, 145, 255, 0.18) inset !important;
    transition: all 0.3s ease;
  }

  :deep(.el-input__wrapper:hover) {
    background: transparent !important;
    box-shadow: 0 0 0 1px rgba(52, 208, 255, 0.36) inset !important;
  }

  :deep(.el-input.is-focus .el-input__wrapper) {
    background: transparent !important;
    box-shadow: 0 0 0 1px rgba(52, 208, 255, 0.5) inset !important;
  }

  :deep(.el-input__inner) {
    color: #eef6ff !important;
    background: transparent !important;
    caret-color: #34d0ff;
    -webkit-text-fill-color: #eef6ff !important;
  }

  :deep(.el-input__inner::placeholder) {
    color: rgba(189, 211, 242, 0.58) !important;
    -webkit-text-fill-color: rgba(189, 211, 242, 0.58) !important;
  }

  :deep(.el-input__inner:-webkit-autofill),
  :deep(.el-input__inner:-webkit-autofill:hover),
  :deep(.el-input__inner:-webkit-autofill:focus),
  :deep(.el-input__inner:-webkit-autofill:active) {
    background: transparent !important;
    -webkit-text-fill-color: #eef6ff !important;
    box-shadow: 0 0 0 1000px transparent inset !important;
    transition: background-color 99999s ease-out 0s;
  }

  :deep(.el-input__prefix),
  :deep(.el-input__suffix),
  :deep(.el-input__suffix-inner) {
    color: var(--screen-primary-light) !important;
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
    color: var(--screen-primary-light);
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: var(--screen-text-secondary);
}
.login-code {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: rgba(238, 246, 255, 0.72);
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 40px;
  padding-left: 12px;
  border-radius: 8px;
}
</style>
