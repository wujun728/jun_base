<!-- 登录页面 -->
<template>
  <div class="flex h-screen w-full">
    <LoginLeftView />

    <div class="relative flex-1">
      <AuthTopBar />

      <div class="auth-right-wrap">
        <div class="form">
          <h3 class="title">{{ $t('login.title') }}</h3>
          <p class="sub-title">{{ $t('login.subTitle') }}</p>
          <ElForm
            ref="formRef"
            :model="formData"
            :rules="rules"
            :key="formKey"
            @keyup.enter="handleSubmit"
            style="margin-top: 25px">
            <!-- <ElFormItem prop="account">
              <ElSelect v-model="formData.account" @change="setupAccount">
                <ElOption v-for="account in accounts" :key="account.key" :label="account.label" :value="account.key">
                  <span>{{ account.label }}</span>
                </ElOption>
              </ElSelect>
            </ElFormItem> -->
            <ElFormItem prop="username">
              <ElInput
                class="custom-height"
                :placeholder="$t('login.placeholder.username')"
                v-model.trim="formData.username" />
            </ElFormItem>
            <ElFormItem prop="password">
              <ElInput
                class="custom-height"
                :placeholder="$t('login.placeholder.password')"
                v-model.trim="formData.password"
                type="password"
                autocomplete="off"
                show-password />
            </ElFormItem>

            <!-- 验证码 -->
            <ElFormItem prop="code">
              <div class="flex w-full items-center justify-between">
                <ElInput
                  class="custom-height w-[63%]!"
                  v-model.trim="formData.code"
                  placeholder="请输入验证码" />
                <div
                  class="custom-height captcha w-[33%] overflow-hidden rounded-lg">
                  <img
                    :src="codeUrl"
                    @click="getCode"
                    class="custom-height w-full rounded-lg" />
                </div>
              </div>
            </ElFormItem>

            <!-- 推拽验证 -->
            <!-- <div class="relative pb-5 mt-6">
              <div class="relative z-[2] overflow-hidden select-none rounded-lg border border-transparent tad-300"
                :class="{ '!border-[#FF4E4F]': !isPassing && isClickPass }">
                <ArtDragVerify ref="dragVerify" v-model:value="isPassing" :text="$t('login.sliderText')"
                  textColor="var(--art-gray-700)" :successText="$t('login.sliderSuccessText')"
                  progressBarBg="var(--main-color)" :background="isDark ? '#26272F' : '#F1F1F4'"
                  handlerBg="var(--default-box-color)" />
              </div>
              <p class="absolute top-0 z-[1] px-px mt-2 text-xs text-[#f56c6c] tad-300"
                :class="{ 'translate-y-10': !isPassing && isClickPass }">
                {{ $t('login.placeholder.slider') }}
              </p>
            </div> -->

            <div class="flex-cb mt-2 text-sm">
              <ElCheckbox v-model="formData.rememberPassword">
                {{ $t('login.rememberPwd') }}
              </ElCheckbox>
              <!-- <RouterLink class="text-theme" :to="{ name: 'ForgetPassword' }">
                {{ $t('login.forgetPwd') }}
              </RouterLink> -->
            </div>

            <div style="margin-top: 30px">
              <ElButton
                class="custom-height w-full"
                type="primary"
                @click="handleSubmit"
                :loading="loading"
                v-ripple>
                {{ $t('login.btnText') }}
              </ElButton>
            </div>

            <!-- <div class="mt-5 text-sm text-gray-600">
              <span>{{ $t('login.noAccount') }}</span>
              <RouterLink class="text-theme" :to="{ name: 'Register' }">
                {{ $t('login.register') }}
              </RouterLink>
            </div> -->
          </ElForm>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElNotification, type FormInstance, type FormRules } from 'element-plus'
import { useI18n } from 'vue-i18n'

import AppConfig from '@/config'
import { useSettingStore } from '@/store/modules/setting'
import { useUserStore } from '@/store/modules/user'
import { HttpError } from '@/utils/http/error'

import { fetchLogin, fetchGetCaptcha } from './api'

defineOptions({ name: 'Login' })

const settingStore = useSettingStore()
const { isDark } = storeToRefs(settingStore)
const { t, locale } = useI18n()
const formKey = ref(0)

// 监听语言切换，重置表单
watch(locale, () => {
  formKey.value++
})

type AccountKey = 'super' | 'admin' | 'user'

export interface Account {
  key: AccountKey
  label: string
  userName: string
  password: string
  roles: string[]
}

const accounts = computed<Account[]>(() => [
  {
    key: 'super',
    label: t('login.roles.super'),
    userName: 'Super',
    password: '123456',
    roles: ['R_SUPER']
  },
  {
    key: 'admin',
    label: t('login.roles.admin'),
    userName: 'Admin',
    password: '123456',
    roles: ['R_ADMIN']
  },
  {
    key: 'user',
    label: t('login.roles.user'),
    userName: 'User',
    password: '123456',
    roles: ['R_USER']
  }
])

const dragVerify = ref()

const userStore = useUserStore()
const router = useRouter()
const route = useRoute()
// const isPassing = ref(false)
// const isClickPass = ref(false)

const systemName = AppConfig.systemInfo.name
const formRef = ref<FormInstance>()

const formData = reactive({
  uuid: '',
  account: '',
  username: '',
  password: '',
  code: '',
  rememberPassword: true
})

const rules = computed<FormRules>(() => ({
  username: [
    {
      required: true,
      message: t('login.placeholder.username'),
      trigger: 'blur'
    }
  ],
  password: [
    {
      required: true,
      message: t('login.placeholder.password'),
      trigger: 'blur'
    }
  ]
}))

const loading = ref(false)

onMounted(() => {
  getCode()
  loadRememberedPassword()
})

// 验证码是否启用
const captchaEnabled = ref(true)
// 验证码图片
const codeUrl = ref('')

// 获取验证码
async function getCode() {
  const res = await fetchGetCaptcha()
  captchaEnabled.value =
    res.captchaEnabled === undefined ? true : res.captchaEnabled
  if (captchaEnabled.value) {
    codeUrl.value = 'data:image/gif;base64,' + res.img
    formData.uuid = res.uuid
  }
}

// 登录
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    // 表单验证
    const valid = await formRef.value.validate()
    if (!valid) return

    // 拖拽验证
    // if (!isPassing.value) {
    //   isClickPass.value = true
    //   return
    // }

    loading.value = true

    // 登录请求
    const { username, password, code, uuid } = formData

    const loginRes = await fetchLogin({
      username,
      password,
      code,
      uuid
    })
    const { token, refreshToken } = loginRes

    // 验证token
    if (!loginRes.token) {
      throw new Error('Login failed - no token received')
    }

    // 存储 token 和登录状态
    userStore.setToken(token, refreshToken)
    saveRememberedPassword()
    userStore.setLoginStatus(true)

    // 登录成功处理
    showLoginSuccessNotice()

    // 获取 redirect 参数，如果存在则跳转到指定页面，否则跳转到首页
    const redirect = route.query.redirect as string
    router.push(redirect || '/')
  } catch (error) {
    getCode()
    formData.code = ''
    // 处理 HttpError
    if (error instanceof HttpError) {
      // console.log(error.code)
    } else {
      // 处理非 HttpError
      // ElMessage.error('登录失败，请稍后重试')
      console.error('[Login] Unexpected error:', error)
    }
  } finally {
    loading.value = false
    resetDragVerify()
  }
}

// 记住密码
const REMEMBER_KEY = 'remember_password'
const REMEMBER_DATA_KEY = 'remember_password_data'

function loadRememberedPassword() {
  const remembered = localStorage.getItem(REMEMBER_KEY)
  if (remembered === 'true') {
    const data = localStorage.getItem(REMEMBER_DATA_KEY)
    if (data) {
      try {
        const parsed = JSON.parse(data)
        formData.username = parsed.username || ''
        formData.password = parsed.password || ''
        formData.rememberPassword = true
      } catch {
        // ignore
      }
    }
  }
}

function saveRememberedPassword() {
  if (formData.rememberPassword) {
    localStorage.setItem(REMEMBER_KEY, 'true')
    localStorage.setItem(
      REMEMBER_DATA_KEY,
      JSON.stringify({
        username: formData.username,
        password: formData.password
      })
    )
  } else {
    localStorage.removeItem(REMEMBER_KEY)
    localStorage.removeItem(REMEMBER_DATA_KEY)
  }
}

// 重置拖拽验证
const resetDragVerify = () => {
  dragVerify.value.reset()
}

// 登录成功提示
const showLoginSuccessNotice = () => {
  setTimeout(() => {
    ElNotification({
      title: t('login.success.title'),
      type: 'success',
      duration: 2500,
      zIndex: 10000,
      message: `${t('login.success.message')}, ${systemName}!`
    })
  }, 1000)
}
</script>

<style scoped>
@import './style.css';
</style>

<style lang="scss" scoped>
:deep(.el-select__wrapper) {
  height: 40px !important;
}
.captcha {
  border: 1px solid var(--el-border-color, var(--el-border-color));
  img {
    transform: scale(1.1);
  }
}
</style>
