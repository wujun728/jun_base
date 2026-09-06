<template>
  <LoginPage
    ref="loginPageRef"
    :brand-name="title"
    title="欢迎回来"
    subtitle="请输入账号密码完成登录"
    :show-google-login="false"
    :register="register"
    :default-username="loginForm.username"
    :default-password="loginForm.password"
    :default-remember="loginForm.rememberMe"
    :captcha-enabled="captchaEnabled"
    :captcha-image="codeUrl"
    @submit="handleLogin"
    @refresh-captcha="getCode"
  />
</template>

<script setup>
import { ref, watch } from "vue"
import { useRoute, useRouter } from "vue-router"
import Cookies from "js-cookie"
import { getCodeImg } from "@/api/login"
import { encrypt, decrypt } from "@/utils/jsencrypt"
import useUserStore from "@/store/modules/user"
import LoginPage from "@/components/Login/LoginPage.vue"

const title = import.meta.env.VITE_APP_TITLE
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const loginPageRef = ref(null)

const loginForm = ref({
  username: "admin",
  password: "admin123",
  rememberMe: false,
  code: "",
  uuid: ""
})

const codeUrl = ref("")
const captchaEnabled = ref(true)
const register = ref(false)
const redirect = ref(undefined)

watch(route, (newRoute) => {
  redirect.value = newRoute.query && newRoute.query.redirect
}, { immediate: true })

function setLoading(value) {
  loginPageRef.value?.setLoading?.(value)
}

function setError(message) {
  loginPageRef.value?.setError?.(message)
}

function saveRememberMe() {
  if (loginForm.value.rememberMe) {
    Cookies.set("username", loginForm.value.username, { expires: 30 })
    Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 })
    Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 })
  } else {
    Cookies.remove("username")
    Cookies.remove("password")
    Cookies.remove("rememberMe")
  }
}

function getCode() {
  getCodeImg().then((res) => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img
      loginForm.value.uuid = res.uuid
      loginForm.value.code = ""
      loginPageRef.value?.setCaptchaCode?.("")
    }
  })
}

function getCookie() {
  const username = Cookies.get("username")
  const password = Cookies.get("password")
  const rememberMe = Cookies.get("rememberMe")
  loginForm.value.username = username === undefined ? loginForm.value.username : username
  loginForm.value.password = password === undefined ? loginForm.value.password : decrypt(password)
  loginForm.value.rememberMe = rememberMe === undefined ? false : Boolean(rememberMe)
}

function handleLogin(payload) {
  loginForm.value.username = payload.username
  loginForm.value.password = payload.password
  loginForm.value.rememberMe = payload.remember
  loginForm.value.code = payload.code

  if (!loginForm.value.username.trim()) {
    setError("请输入您的账号")
    return
  }
  if (!loginForm.value.password) {
    setError("请输入您的密码")
    return
  }
  if (captchaEnabled.value && !loginForm.value.code) {
    setError("请输入验证码")
    return
  }

  setError("")
  setLoading(true)
  saveRememberMe()

  userStore.login(loginForm.value).then(() => {
    const query = route.query
    const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
      if (cur !== "redirect") {
        acc[cur] = query[cur]
      }
      return acc
    }, {})
    router.push({ path: redirect.value || "/", query: otherQueryParams })
  }).catch((error) => {
    const message = error?.message || "登录失败，请检查账号密码"
    setError(message)
    if (captchaEnabled.value) {
      getCode()
    }
  }).finally(() => {
    setLoading(false)
  })
}

getCookie()
getCode()
</script>
