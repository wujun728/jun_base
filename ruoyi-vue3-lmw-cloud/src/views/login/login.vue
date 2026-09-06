<script setup>
import { getCodeImg } from '@/api/login.js'
import useUserStore from '@/store/modules/user.js'
import getFormConfig from './config/formConfig.js'
import { encrypt, decrypt } from '@/utils/jsencrypt.js'
import Cookies from 'js-cookie'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const title = ref(import.meta.env.VITE_APP_TITLE)
const formRef = ref(null)
const formData = ref({
  code: '',
  username: '',
  password: '',
  uuid: '',
  rememberMe: false,
})
const codeUrl = ref('')
// 验证码开关
const captchaEnabled = ref(true)
const hideItems = ref([])
// 生成验证码
const generateCode = async () => {
  const res = await getCodeImg()
  if (res) {
    codeUrl.value = 'data:image/gif;base64,' + res.img
    formData.value.uuid = res.uuid
    captchaEnabled.value = res.captchaEnabled === true
  }
}
const getCookie = () => {
  const username = Cookies.get('username')
  const password = Cookies.get('password')
  const rememberMe = Cookies.get('rememberMe')
  formData.value = {
    username: username || 'admin',
    password: decrypt(password) || 'admin123',
    rememberMe: rememberMe ? false : Boolean(rememberMe),
    code: '',
    uuid: '',
  }
}

const config = {
  codeHide: !captchaEnabled.value,
  listener: {
    keyup: (event) => {
      if (event.key === 'Enter') {
        submit()
      }
    },
  },
}
const formConfig = getFormConfig(config)
const formConfigComputed = computed(() => {
  if (!captchaEnabled.value) {
    hideItems.value = ['code']
  } else {
    hideItems.value = []
  }
  formConfig.hideItems = hideItems
  return formConfig
})
const redirect = ref(undefined)

watch(
  route,
  (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect
  },
  { immediate: true }
)
const loginLoading = ref(false)
const submit = async () => {
  let flag = await formRef.value?.getFormValidate()
  if (flag) {
    loginLoading.value = true
    try {
      await userStore.login(formData.value)
      const query = route.query
      const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
      if (formData.value.rememberMe) {
        Cookies.set('username', formData.value.username, { expires: 30 })
        Cookies.set('password', encrypt(formData.value.password), {
          expires: 30,
        })
        Cookies.set('rememberMe', formData.value.rememberMe, { expires: 30 })
      } else {
        // 否则移除
        Cookies.remove('username')
        Cookies.remove('password')
        Cookies.remove('rememberMe')
      }
      router.push({ path: redirect.value || '/', query: otherQueryParams })
    } catch (error) {
      if (captchaEnabled.value) {
        generateCode()
      }
      loginLoading.value = false
    }
  }
}

const init = () => {
  generateCode()
  getCookie()
}
init()
onUnmounted(() => {
  loginLoading.value = false
})
</script>
<template>
  <div class="login">
    <div class="loginForm">
      <h3 class="title">{{ title }}</h3>
      <BaseForm
        v-bind="formConfigComputed"
        v-model:data="formData"
        ref="formRef"
      >
        <template #usernamePrefix>
          <SvgIcon iconClass="user" size="16"></SvgIcon>
        </template>
        <template #passwordPrefix>
          <SvgIcon iconClass="unlock" size="16"></SvgIcon>
        </template>
        <template #codeCustom>
          <div class="flex code">
            <el-input
              v-model="formData.code"
              placeholder="验证码"
              class="input mr20"
              size="large"
              @keyup.enter="submit"
            >
              <template #prefix>
                <SvgIcon iconClass="shield-halved" :size="16" />
              </template>
            </el-input>
            <div class="img" @click="generateCode">
              <el-image class="elImage" :src="codeUrl">
                <template #error>
                  <el-icon><DocumentDelete /></el-icon>
                </template>
                <template #placeholder>
                  <el-icon class="is-loading">
                    <Loading />
                  </el-icon>
                </template>
              </el-image>
            </div>
          </div>
        </template>
        <template #footer>
          <div class="footer">
            <el-button
              size="large"
              @click="submit"
              type="primary"
              :loading="loginLoading"
            >
              登录
            </el-button>
          </div>
        </template>
      </BaseForm>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url('../../assets/images/login-background.jpg');
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: var(--el-text-color-primary);
}
.loginForm {
  border-radius: 6px;
  background-color: var(--ba-bg-color-overlay);
  width: 400px;
  padding: 25px 25px 5px 25px;
}
.code {
  width: 100%;
  .input {
    flex: 2;
  }
  .img {
    height: 38px;
    flex: 1;
    cursor: pointer;
    .elImage {
      width: 100%;
      height: 100%;
    }
    :deep(.el-image__wrapper) {
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
}
.footer {
  width: 100%;
  :deep(.el-button) {
    width: 100%;
  }
}
</style>
