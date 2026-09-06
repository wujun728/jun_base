<template>
  <div class="flex w-full h-screen"
    ><LoginLeftView /><div class="relative flex-1"
      ><AuthTopBar /><div class="auth-right-wrap"
        ><div class="form">
          <h3 class="title">{{ $t('register.title') }}</h3>
          <ElForm ref="formRef" :model="form" :rules="rules" class="mt-7.5">
            <ElFormItem prop="username"
              ><ElInput
                v-model.trim="form.username"
                class="custom-height"
                :placeholder="$t('register.placeholder.username')"
            /></ElFormItem>
            <ElFormItem prop="password"
              ><ElInput
                v-model.trim="form.password"
                class="custom-height"
                type="password"
                show-password
                :placeholder="$t('register.placeholder.password')"
            /></ElFormItem>
            <ElFormItem prop="confirmPassword"
              ><ElInput
                v-model.trim="form.confirmPassword"
                class="custom-height"
                type="password"
                show-password
                :placeholder="$t('register.placeholder.confirmPassword')"
            /></ElFormItem>
            <ElFormItem v-if="isImageCaptcha" prop="code"
              ><div class="flex w-full gap-3"
                ><ElInput v-model="form.code" placeholder="请输入验证码" /><img
                  class="h-10 cursor-pointer"
                  :src="image"
                  @click="loadCaptcha" /></div
            ></ElFormItem>
            <div v-if="isSliderCaptcha" id="tianai-register-captcha-box" class="mt-6">
              <ElButton
                class="w-full custom-height"
                plain
                :loading="captchaLoading"
                @click="openSliderCaptcha"
              >
                <ArtSvgIcon
                  :icon="form.captchaToken ? 'ri:shield-check-line' : 'ri:shield-keyhole-line'"
                  class="mr-1"
                />
                {{ form.captchaToken ? $t('login.sliderSuccessText') : $t('login.sliderText') }}
              </ElButton>
            </div>
            <ElButton
              class="w-full mt-6 custom-height"
              type="primary"
              :loading="loading"
              @click="register"
              >注册</ElButton
            >
            <div class="mt-5 text-sm"
              ><RouterLink class="text-theme" :to="{ name: 'Login' }">{{
                $t('register.toLogin')
              }}</RouterLink></div
            >
          </ElForm>
        </div></div
      ></div
    ></div
  >
</template>
<script setup lang="ts">
  import { fetchCaptcha, fetchRegister, type CaptchaInfo } from '@/api/auth'
  import type { FormInstance, FormRules } from 'element-plus'
  import ArtSvgIcon from '@/components/core/base/art-svg-icon/index.vue'
  import { verifyByTianaiCaptcha } from '@/utils/tianai-captcha'
  const router = useRouter()
  const formRef = ref<FormInstance>()
  const captcha = reactive<CaptchaInfo>({
    captchaEnabled: true,
    captchaType: 'slider',
    registerEnabled: false
  })
  const form = reactive({
    username: '',
    password: '',
    confirmPassword: '',
    code: '',
    uuid: '',
    captchaToken: ''
  })
  const loading = ref(false)
  const captchaLoading = ref(false)
  const image = computed(() => (captcha.img ? `data:image/gif;base64,${captcha.img}` : ''))
  const isCaptchaEnabled = computed(() => captcha.captchaEnabled)
  const isImageCaptcha = computed(() => isCaptchaEnabled.value && captcha.captchaType === 'image')
  const isSliderCaptcha = computed(() => isCaptchaEnabled.value && captcha.captchaType === 'slider')
  const rules = computed<FormRules>(() => ({
    username: [{ required: true, message: '请输入用户名' }],
    password: [{ required: true, message: '请输入密码' }],
    confirmPassword: [
      {
        required: true,
        validator: (_rule, value, callback) =>
          callback(value === form.password ? undefined : new Error('两次密码输入不一致'))
      }
    ],
    code: [{ required: isImageCaptcha.value, message: '请输入验证码' }]
  }))
  onMounted(loadCaptcha)
  async function loadCaptcha() {
    Object.assign(captcha, await fetchCaptcha())
    form.code = ''
    form.uuid = captcha.uuid || ''
    form.captchaToken = ''
  }
  async function register() {
    await formRef.value?.validate()
    if (isSliderCaptcha.value && !form.captchaToken) {
      await openSliderCaptcha()
      if (!form.captchaToken) return
    }
    loading.value = true
    try {
      const captchaParams = isCaptchaEnabled.value
        ? {
            code: form.code,
            uuid: form.uuid,
            captchaToken: form.captchaToken
          }
        : {}
      await fetchRegister({
        username: form.username,
        password: form.password,
        confirmPassword: form.confirmPassword,
        ...captchaParams
      })
      await router.push({ name: 'Login' })
    } finally {
      loading.value = false
    }
  }
  async function openSliderCaptcha() {
    if (captchaLoading.value) return
    captchaLoading.value = true
    try {
      form.captchaToken = await verifyByTianaiCaptcha('#tianai-register-captcha-box')
    } catch {
      form.captchaToken = ''
    } finally {
      captchaLoading.value = false
    }
  }
</script>
<style scoped>
  @import '../login/style.css';
</style>
