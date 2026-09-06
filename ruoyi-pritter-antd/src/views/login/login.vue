<script setup>
import { getCodeImg } from '@/api/login.js'
import { useConfig } from '@/store/modules/layout.js'
import useUserStore from '@/store/modules/user.js'
import {
  clearStoredLoginCredentials,
  readStoredLoginCredentials,
  writeStoredLoginCredentials,
} from './utils/loginRememberCipher'
import modal from '@/plugins/modal'
import LoginCharacters from './cpns/LoginCharacters.vue'
import {
  LOGIN_PAGE_APP_TITLE,
  resolveLoginNavigateTarget,
} from './utils/loginUtils.js'
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const configStore = useConfig()
const isDark = computed(() => configStore.layout.isDark)

const form = reactive({
  username: 'admin',
  password: 'admin123',
  code: '',
  remember: false,
})

const uuid = ref('')
const captchaEnabled = ref(false)
const captchaImgUrl = ref('')
const captchaLoading = ref(false)

const passwordVisible = ref(false)
const isTyping = ref(false)
const charactersShakeNonce = ref(0)

const rules = computed(() => {
  const base = {
    username: [
      { required: true, message: '请输入邮箱或用户名', trigger: 'blur' },
    ],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  }
  if (!captchaEnabled.value) return base
  return {
    ...base,
    code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
  }
})

async function fetchCaptcha() {
  captchaLoading.value = true
  try {
    const res = await getCodeImg()
    uuid.value = res.uuid || ''
    captchaEnabled.value = Boolean(res.captchaEnabled)
    captchaImgUrl.value = res.img ? `data:image/gif;base64,${res.img}` : ''
  } finally {
    captchaLoading.value = false
  }
}

onMounted(() => {
  fetchCaptcha()
  const saved = readStoredLoginCredentials()
  form.remember = Boolean(saved)
  if (saved) {
    form.username = saved.username
    form.password = saved.password
  }
})

const passwordLen = computed(() => form.password?.length ?? 0)

const userStore = useUserStore()
const router = useRouter()
const route = useRoute()
const loginLoading = ref(false)

function refreshCaptchaIfIdle() {
  if (!captchaLoading.value) fetchCaptcha()
}

async function submitLogin() {
  loginLoading.value = true
  try {
    await userStore.login({
      username: form.username,
      password: form.password,
      code: captchaEnabled.value ? form.code : '',
      uuid: uuid.value,
    })

    if (form.remember) {
      writeStoredLoginCredentials({
        username: form.username,
        password: form.password,
      })
    } else {
      clearStoredLoginCredentials()
    }

    const { path, query } = resolveLoginNavigateTarget(route.query)
    router.push({ path, query })
  } catch {
    charactersShakeNonce.value += 1
    if (captchaEnabled.value) fetchCaptcha()
    loginLoading.value = false
  }
}
</script>

<template>
  <div class="login-page-grid">
    <div class="login-left-panel">
      <div class="login-left-brand-z">
        <div class="login-desktop-brand-row">
          <div class="login-desktop-brand-icon">
            <SvgIcon
              icon-class="ant-icon-ThunderboltOutlined"
              :size="16"
              class-name="login-brand-thunderbolt"
            />
          </div>
          <span>{{ LOGIN_PAGE_APP_TITLE }}</span>
        </div>
      </div>

      <LoginCharacters
        :password-len="passwordLen"
        :show-password="passwordVisible"
        :is-typing="isTyping"
        :failure-shake-nonce="charactersShakeNonce"
      />

      <footer class="login-footer-row">
        <a href="#" @click.prevent>隐私政策</a>
        <a href="#" @click.prevent>服务条款</a>
        <a href="#" @click.prevent>联系我们</a>
      </footer>

      <div class="login-blur-orb" />
      <div class="login-blur-glow-bl" />
    </div>

    <div
      class="login-right-panel"
      :class="{ 'login-right-panel--dark': isDark }"
    >
      <div class="login-form-wrap">
        <div class="login-mobile-logo-row">
          <div
            class="login-mobile-brand-icon"
            :class="{ 'login-mobile-brand-icon--dark': isDark }"
          >
            <SvgIcon
              icon-class="ant-icon-ThunderboltOutlined"
              :size="16"
              class-name="login-brand-thunderbolt"
            />
          </div>
          <span :class="{ 'login-mobile-logo-title--dark': isDark }">{{
            LOGIN_PAGE_APP_TITLE
          }}</span>
        </div>

        <div class="login-auth-header">
          <h1
            class="login-auth-title"
            :class="{ 'login-auth-title--dark': isDark }"
          >
            欢迎回来
          </h1>
          <p
            class="login-auth-subtitle"
            :class="{ 'login-auth-subtitle--dark': isDark }"
          >
            请填写您的登录信息
          </p>
        </div>

        <div aria-hidden="true" class="login-autofill-trap">
          <input type="text" tabindex="-1" autocomplete="off" />
          <input type="password" tabindex="-1" autocomplete="off" />
        </div>

        <a-form
          :model="form"
          :rules="rules"
          layout="vertical"
          :required-mark="false"
          @finish="submitLogin"
        >
          <a-form-item label="用户名" name="username">
            <a-input
              v-model:value="form.username"
              size="large"
              placeholder="请输入用户名"
              autocomplete="off"
              :spellcheck="false"
              @focus="isTyping = true"
              @blur="isTyping = false"
            />
          </a-form-item>

          <a-form-item label="密码" name="password">
            <a-input
              v-model:value="form.password"
              size="large"
              placeholder="请输入密码"
              autocomplete="new-password"
              :spellcheck="false"
              :type="passwordVisible ? 'text' : 'password'"
            >
              <template #suffix>
                <SvgIcon
                  v-if="passwordVisible"
                  icon-class="ant-icon-EyeOutlined"
                  class-name="login-password-toggle"
                  :size="14"
                  @click.stop="passwordVisible = false"
                />
                <SvgIcon
                  v-else
                  icon-class="ant-icon-EyeInvisibleOutlined"
                  class-name="login-password-toggle"
                  :size="14"
                  @click.stop="passwordVisible = true"
                />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item v-if="captchaEnabled" label="验证码" name="code">
            <div class="login-captcha-row flex">
              <a-input
                v-model:value="form.code"
                class="login-captcha-input"
                size="large"
                placeholder="验证码"
                :maxlength="4"
                autocomplete="off"
                :spellcheck="false"
              />
              <a-spin
                :spinning="captchaLoading"
                class="login-captcha-spin-wrap"
              >
                <img
                  :src="captchaImgUrl"
                  alt="captcha"
                  class="login-captcha-img"
                  draggable="false"
                  @click="refreshCaptchaIfIdle"
                />
              </a-spin>
            </div>
          </a-form-item>

          <div class="login-remember-row">
            <a-checkbox v-model:checked="form.remember">记住密码</a-checkbox>
            <a-button
              type="link"
              size="small"
              @click="modal.msg('请联系管理员重置密码')"
            >
              忘记密码？
            </a-button>
          </div>

          <a-form-item>
            <a-button
              type="primary"
              html-type="submit"
              size="large"
              :loading="loginLoading"
              class="login-submit-btn"
            >
              登录
            </a-button>
          </a-form-item>
        </a-form>

        <div
          class="login-signup-footer"
          :class="{ 'login-signup-footer--dark': isDark }"
        >
          还没有账号？
          <a-button type="link" @click="modal.msg('请联系管理员为您开通账号')">
            联系管理员开通
          </a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
$pl-primary: oklch(0.205 0 0);
$pl-fg: oklch(0.985 0 0);
$pd-primary: oklch(0.922 0 0);
$pd-background: oklch(0.145 0 0);
$pd-muted-fg: oklch(0.708 0 0);
$surface-light: #ffffff;

.login-page-grid {
  min-height: 100vh;
  display: grid;
  @media (min-width: 1024px) {
    grid-template-columns: 1fr 1fr;
  }
}

.login-left-panel {
  position: relative;
  display: none;
  flex-direction: column;
  justify-content: space-between;
  padding: 48px;
  color: $pl-fg;
  background: #000000;
  @media (min-width: 1024px) {
    display: flex;
  }
}

.login-blur-orb {
  position: absolute;
  top: 25%;
  right: 25%;
  width: 256px;
  height: 256px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  filter: blur(48px);
  pointer-events: none;
}

.login-blur-glow-bl {
  position: absolute;
  bottom: 25%;
  left: 25%;
  width: 384px;
  height: 384px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.05);
  filter: blur(48px);
  pointer-events: none;
}

.login-right-panel {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px;
  background: $surface-light;

  &--dark {
    background: $pd-background;

    .ant-form-item-label > label {
      color: $pl-fg;
    }

    .ant-checkbox-wrapper {
      color: $pl-fg;
    }
  }
}

.login-form-wrap {
  width: 100%;
  max-width: 420px;
}

.login-mobile-logo-row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 48px;
  @media (min-width: 1024px) {
    display: none;
  }
}

.login-footer-row {
  display: flex;
  align-items: center;
  gap: 32px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.55);
  position: relative;
  z-index: 20;

  a {
    color: inherit;
    text-decoration: none;
    transition: color 0.2s;
    &:hover {
      color: $pl-fg;
    }
  }
}

.login-remember-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 4px;
}

.login-characters-wrap {
  position: relative;
  z-index: 20;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  height: 500px;
}

.login-char-body-layer {
  position: absolute;
  bottom: 0;
  transition: all 0.7s ease-in-out;
}

.login-char-eyes-row-loose {
  position: absolute;
  display: flex;
  gap: 32px;
  transition: all 0.7s ease-in-out;
}

.login-char-eyes-row-black {
  position: absolute;
  display: flex;
  gap: 24px;
  transition: all 0.7s ease-in-out;
}

.login-char-eyes-row-orange {
  position: absolute;
  display: flex;
  gap: 32px;
  transition: all 0.2s ease-out;
}

.login-char-eyes-row-yellow {
  position: absolute;
  display: flex;
  gap: 24px;
  transition: all 0.2s ease-out;
}

.login-yellow-mouth-bar {
  position: absolute;
  width: 80px;
  height: 4px;
  background: #2d2d2d;
  border-radius: 9999px;
  transition: all 0.2s ease-out;
  left: var(--login-mouth-left, 0px);
  top: var(--login-mouth-top, 0px);
}

.login-left-brand-z {
  position: relative;
  z-index: 20;
}

.login-desktop-brand-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
}

.login-desktop-brand-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-mobile-brand-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: color-mix(in oklch, $pl-primary 12%, transparent);
  display: flex;
  align-items: center;
  justify-content: center;
  color: $pl-primary;

  &--dark {
    background: color-mix(in oklch, $pd-primary 12%, transparent);
    color: $pd-primary;
  }
}

.login-mobile-logo-title--dark {
  color: $pl-fg;
}

.login-auth-header {
  text-align: center;
  margin-bottom: 40px;
}

.login-auth-title {
  font-size: 28px;
  font-weight: 700;
  letter-spacing: -0.02em;
  margin: 0 0 8px;
  color: oklch(0.145 0 0);

  &--dark {
    color: oklch(0.985 0 0);
  }
}

.login-auth-subtitle {
  margin: 0;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.45);

  &--dark {
    color: $pd-muted-fg;
  }
}

.login-captcha-img {
  display: block;
  height: 40px;
  cursor: pointer;
  border-radius: 0 8px 8px 0;
}

.login-brand-thunderbolt {
  font-size: 16px;
}

.login-characters-stage {
  position: relative;
  width: 550px;
  height: 400px;

  &--shake {
    animation: login-chars-shake 0.45s ease-in-out both;
  }
}

@keyframes login-chars-shake {
  0%,
  100% {
    transform: translateX(0);
  }
  15% {
    transform: translateX(-2.5px);
  }
  30% {
    transform: translateX(2.5px);
  }
  45% {
    transform: translateX(-1.5px);
  }
  60% {
    transform: translateX(1.5px);
  }
  75% {
    transform: translateX(-0.5px);
  }
}

.login-pupil-anchor {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: var(--login-pupil-size, 12px);
  height: var(--login-pupil-size, 12px);
}

.login-pupil-translate {
  border-radius: 9999px;
  width: var(--login-pupil-size, 12px);
  height: var(--login-pupil-size, 12px);
  background-color: var(--login-pupil-fill, #000000);
  transform: translate(var(--login-pupil-x, 0px), var(--login-pupil-y, 0px));
  transition: transform 0.1s ease-out;
}

.login-eye-ball-shell {
  border-radius: 9999px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.15s ease;
  width: var(--login-eye-size, 48px);
  height: var(--login-eye-height, 48px);
  background-color: var(--login-eye-fill, #ffffff);
  overflow: hidden;
}

.login-eye-ball-pupil {
  border-radius: 9999px;
  width: var(--login-eye-pupil-size, 16px);
  height: var(--login-eye-pupil-size, 16px);
  background-color: var(--login-eye-pupil-fill, #000000);
  transform: translate(
    var(--login-eye-pupil-x, 0px),
    var(--login-eye-pupil-y, 0px)
  );
  transition: transform 0.1s ease-out;
}

.login-purple-character-shell {
  left: 70px;
  width: 180px;
  height: var(--login-char-h, 400px);
  background-color: #6c3ff5;
  border-radius: 10px 10px 0 0;
  z-index: 1;
  transform: var(--login-char-tf, skewX(0deg));
  transform-origin: bottom center;
}

.login-black-character-shell {
  left: 240px;
  width: 120px;
  height: 310px;
  background-color: #2d2d2d;
  border-radius: 8px 8px 0 0;
  z-index: 2;
  transform: var(--login-char-tf, skewX(0deg));
  transform-origin: bottom center;
}

.login-orange-character-shell {
  left: 0;
  width: 240px;
  height: 200px;
  z-index: 3;
  background-color: #ff9b6b;
  border-radius: 120px 120px 0 0;
  transform: var(--login-char-tf, skewX(0deg));
  transform-origin: bottom center;
}

.login-yellow-character-shell {
  left: 310px;
  width: 140px;
  height: 230px;
  background-color: #e8d754;
  border-radius: 70px 70px 0 0;
  z-index: 4;
  transform: var(--login-char-tf, skewX(0deg));
  transform-origin: bottom center;
}

.login-char-eyes-shift {
  left: var(--login-eye-left, 0px);
  top: var(--login-eye-top, 0px);
}

.login-signup-footer {
  text-align: center;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.45);
  margin-top: 32px;

  &--dark {
    color: $pd-muted-fg;
  }
}

.login-captcha-row {
  width: 100%;
}

.login-captcha-input {
  flex: 1;
}

.login-autofill-trap {
  position: fixed;
  left: -100vw;
  top: 0;
  width: 0;
  height: 0;
  opacity: 0;
  overflow: hidden;
  pointer-events: none;
}

.login-password-toggle {
  cursor: pointer;
}

.login-captcha-row.flex {
  display: flex;
  width: 100%;
  align-items: stretch;
  gap: 0;
}

.login-captcha-spin-wrap {
  min-width: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-submit-btn {
  width: 100%;
}
</style>
