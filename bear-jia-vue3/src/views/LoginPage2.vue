<template>
  <div :class="['login-container', { 'dark-theme': isDarkMode }]">
    <div class="login-box">
      <!-- 左侧品牌展示区 -->
      <div class="brand-side">
        <div class="brand-header">
           <img :src="logoUrl" class="logo" alt="Logo" />
           <span class="brand-name">BearJia</span>
        </div>
        <div class="brand-content">
          <h2 class="slogan">构建高效、智能的<br/>企业级管理平台</h2>
          <p class="desc">现代化技术栈，极致的用户体验，助力企业数字化转型。</p>
          <div class="feature-list">
             <div class="feature-item">
               <span class="icon">🚀</span>
               <span>极速开发体验</span>
             </div>
             <div class="feature-item">
               <span class="icon">🎨</span>
               <span>现代化 UI 设计</span>
             </div>
             <div class="feature-item">
               <span class="icon">🔐</span>
               <span>企业级安全保障</span>
             </div>
          </div>
        </div>
        <div class="brand-footer">
          © 2025 BearJia Team. All rights reserved.
        </div>

        <!-- 装饰背景圆 -->
        <div class="decor-circle c1"></div>
        <div class="decor-circle c2"></div>
      </div>

      <!-- 右侧登录表单区 -->
      <div class="form-side">
        <div class="form-header">
          <h3>欢迎登录</h3>
          <p class="subtitle">请输入您的账号信息以继续</p>
        </div>

        <a-form
          class="login-form"
          ref="loginFormRef"
          :model="loginFormModel"
          :rules="loginFormRules"
          :scrollToFirstError="true"
        >
          <a-form-item name="username">
            <a-input
              v-model:value="loginFormModel.username"
              size="large"
              placeholder="请输入用户名"
              class="custom-input"
              :bordered="false"
            >
              <template #prefix>
                <UserOutlined class="input-icon" />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item name="password">
            <a-input-password
              v-model:value="loginFormModel.password"
              size="large"
              placeholder="请输入密码"
              class="custom-input"
              :bordered="false"
            >
              <template #prefix>
                <LockOutlined class="input-icon" />
              </template>
            </a-input-password>
          </a-form-item>

          <a-form-item name="code" v-if="loginFormModel.captchaEnabled">
            <div class="verify-row">
              <a-input
                v-model:value="loginFormModel.code"
                size="large"
                placeholder="验证码"
                class="custom-input verify-input"
                :maxLength="4"
                :bordered="false"
                @keyup.enter="submitForm"
              >
                <template #prefix>
                  <SafetyOutlined class="input-icon" />
                </template>
              </a-input>
              <div class="verify-img-box" @click="getVerifyCode">
                <img :src="loginFormModel.codeUrl" alt="验证码" v-if="loginFormModel.codeUrl"/>
                <div v-else class="img-placeholder"><LoadingOutlined /></div>
              </div>
            </div>
          </a-form-item>

          <div class="form-options">
            <a-checkbox v-model:checked="loginFormModel.rememberMe">记住我</a-checkbox>
            <a class="forgot-link">忘记密码？</a>
          </div>

          <a-button
            type="primary"
            block
            size="large"
            class="submit-btn"
            :loading="loginFormModel.loginButtonLoading"
            @click="submitForm"
          >
            {{ loginFormModel.loginButtonName }}
          </a-button>

          <div class="form-footer-action">
            <span>没有账号？</span>
            <a-button type="link" @click="goToRegister">立即注册</a-button>
            <span class="divider">|</span>
            <a-button type="link" @click="switchLoginStyle">切换风格</a-button>
          </div>
        </a-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { getVerifyCodeImg } from '@/api/login.js';
import { ref, reactive, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { useAppStore } from '@/stores/app';
import {
  UserOutlined,
  LockOutlined,
  SafetyOutlined,
  LoadingOutlined
} from '@ant-design/icons-vue';
import { usePermissionStore } from '@/stores/permission';
import logoUrl from '@/assets/images/logo.png';

const vueRouter = useRouter();
const vueStore = useUserStore();
const appStore = useAppStore();
const loginFormRef = ref();

// 计算暗黑模式状态
const isDarkMode = computed(() => appStore.layoutSettings.darkMode);

const loginFormModel = reactive({
  username: 'admin',
  password: 'admin123',
  code: '',
  uuid: '',
  codeUrl: '',
  loginButtonDisabled: false,
  loginButtonLoading: false,
  loginButtonName: '登录',
  rememberMe: false,
  captchaEnabled: true
});

const loginFormRules = computed(() => ({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  code: loginFormModel.captchaEnabled ? [{ required: true, message: '请输入验证码', trigger: 'blur' }] : [],
}));

const getVerifyCode = () => {
  getVerifyCodeImg().then((res) => {
    loginFormModel.captchaEnabled = res.captchaEnabled;
    if (res.captchaEnabled){
      if (res.img.startsWith('data:image')) {
        loginFormModel.codeUrl = res.img;
      } else {
        //适配若依的验证码
        loginFormModel.codeUrl = "data:image/gif;base64," + res.img
      }
    }
    loginFormModel.uuid = res.uuid;
  });
};

const submitForm = async () => {
  try {
    loginFormModel.loginButtonDisabled = true;
    loginFormModel.loginButtonLoading = true;
    loginFormModel.loginButtonName = '登录中...';

    await loginFormRef.value.validate();
    await vueStore.login(loginFormModel);
    await vueStore.getInfo();

    const permissionStore = usePermissionStore();
    const accessRoutes = await permissionStore.generateRoutes();
    accessRoutes.forEach(route => {
      vueRouter.addRoute(route);
    });

    await vueRouter.push({ path: '/home/workbench' });
  } catch (error) {
    console.error('登录失败:', error);
    loginFormModel.loginButtonDisabled = false;
    loginFormModel.loginButtonLoading = false;
    loginFormModel.loginButtonName = '登录';
    getVerifyCode();
  }
};

const goToRegister = () => {
  vueRouter.push('/register');
};

const switchLoginStyle = () => {
  vueRouter.push('/login3');
};

onMounted(() => {
  getVerifyCode();
});
</script>

<style lang="less" scoped>
// 变量定义
@primary-color: #3b82f6;
@bg-light: #f0f2f5;
@bg-dark: #111827;
@card-bg-light: #ffffff;
@card-bg-dark: #1f2937;
@text-main-light: #1f2937;
@text-main-dark: #f9fafb;
@text-sub-light: #6b7280;
@text-sub-dark: #9ca3af;
@brand-gradient: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
@input-border-color: #d1d5db;

.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: @bg-light;
  background-image: radial-gradient(#e5e7eb 1px, transparent 1px);
  background-size: 20px 20px;
  padding: 20px;
  transition: all 0.3s ease;

  &.dark-theme {
    background-color: @bg-dark;
    background-image: radial-gradient(#374151 1px, transparent 1px);

    .login-box {
      background: @card-bg-dark;
      box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.5);
      border: 1px solid rgba(255, 255, 255, 0.05);

      .form-side {
        .form-header {
           h3 { color: @text-main-dark; }
           p { color: @text-sub-dark; }
        }

        .custom-input {
          background: rgba(0,0,0,0.2);
          border: 1px solid rgba(255,255,255,0.1);
          color: @text-main-dark;

          :deep(.ant-input) {
            background: transparent;
            color: @text-main-dark;
            &::placeholder { color: @text-sub-dark; }
          }

          &:hover, &:focus-within {
             border-color: @primary-color;
          }

          :deep(.ant-input-prefix) { color: @text-sub-dark; }
          :deep(.ant-input-password-icon) { color: @text-sub-dark; }
        }

        .verify-img-box {
          border-color: rgba(255,255,255,0.1);
          .img-placeholder { background: rgba(0,0,0,0.2); color: @text-sub-dark; }
        }

        .form-options {
          color: @text-sub-dark;
          :deep(.ant-checkbox-wrapper) { color: @text-sub-dark; }
        }

        .form-footer-action {
          color: @text-sub-dark;
          .divider { color: rgba(255,255,255,0.1); }
        }
      }
    }
  }

  .login-box {
    width: 960px;
    height: 600px;
    background: @card-bg-light;
    border-radius: 20px;
    box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
    display: flex;
    overflow: hidden;
    transition: all 0.3s ease;

    // 左侧品牌区
    .brand-side {
      width: 45%;
      background: @brand-gradient;
      padding: 60px 40px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      position: relative;
      overflow: hidden;
      color: white;

      @media (max-width: 768px) {
        display: none;
      }

      .brand-header {
        display: flex;
        align-items: center;
        gap: 12px;
        position: relative;
        z-index: 2;

        .logo { width: 40px; height: 40px; border-radius: 8px; background: white; padding: 2px; }
        .brand-name { font-size: 24px; font-weight: 700; letter-spacing: 1px; }
      }

      .brand-content {
        position: relative;
        z-index: 2;

        .slogan {
          font-size: 32px;
          font-weight: 800;
          line-height: 1.2;
          margin-bottom: 24px;
        }

        .desc {
          font-size: 16px;
          opacity: 0.9;
          margin-bottom: 40px;
          line-height: 1.6;
        }

        .feature-list {
          display: flex;
          flex-direction: column;
          gap: 16px;

          .feature-item {
             display: flex;
             align-items: center;
             gap: 12px;
             font-size: 15px;
             font-weight: 500;
             background: rgba(255,255,255,0.1);
             padding: 12px 16px;
             border-radius: 12px;
             backdrop-filter: blur(10px);
             transition: transform 0.3s;

             &:hover { transform: translateX(5px); background: rgba(255,255,255,0.2); }
             .icon { font-size: 18px; }
          }
        }
      }

      .brand-footer {
        position: relative;
        z-index: 2;
        font-size: 12px;
        opacity: 0.6;
      }

      // 装饰圆
      .decor-circle {
        position: absolute;
        border-radius: 50%;
        background: rgba(255,255,255,0.1);

        &.c1 { width: 300px; height: 300px; top: -50px; right: -100px; }
        &.c2 { width: 200px; height: 200px; bottom: -50px; left: -50px; }
      }
    }

    // 右侧表单区
    .form-side {
      flex: 1;
      padding: 60px 80px;
      display: flex;
      flex-direction: column;
      justify-content: center;

      @media (max-width: 992px) {
        padding: 40px;
      }

      .form-header {
        margin-bottom: 40px;
        h3 { font-size: 28px; font-weight: 700; color: @text-main-light; margin-bottom: 8px; }
        .subtitle { color: @text-sub-light; font-size: 15px; }
      }

      // 自定义输入框外层容器
      .custom-input {
        display: flex;
        align-items: center;
        height: 40px;
        background: #ffffff;
        border: 1px solid @input-border-color;
        border-radius: 6px;
        padding: 0 11px;
        transition: all 0.3s;

        &:hover, &:focus-within {
           border-color: @primary-color;
           box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
        }

        // 覆盖 Ant Design 默认样式
        :deep(.ant-input) {
           background: transparent !important;
           border: none !important;
           box-shadow: none !important;
           height: 100%;
           padding: 0;
           font-size: 14px;

           &:focus { box-shadow: none !important; }
        }

        :deep(.ant-input-prefix) { margin-right: 8px; color: #9ca3af; font-size: 16px; }

        :deep(.ant-input-password-icon) {
            color: #9ca3af;
            cursor: pointer;
            &:hover { color: @text-main-light; }
        }
      }

      .verify-row {
        display: flex;
        gap: 16px;
        align-items: center;

        .verify-input {
          flex: 1;
          margin-bottom: 0;
        }

        .verify-img-box {
           width: 120px;
           height: 40px;
           border: 1px solid @input-border-color;
           border-radius: 6px;
           overflow: hidden;
           cursor: pointer;
           transition: all 0.3s;
           flex-shrink: 0;

           &:hover { opacity: 0.8; border-color: @primary-color; }

           img { width: 100%; height: 100%; object-fit: cover; }

           .img-placeholder {
             width: 100%; height: 100%;
             background: #f9fafb;
             display: flex; align-items: center; justify-content: center;
             color: #9ca3af;
           }
        }
      }

      .form-options {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 30px;

        .forgot-link {
          color: @primary-color;
          &:hover { text-decoration: underline; }
        }
      }

      .submit-btn {
        height: 40px;
        font-size: 16px;
        font-weight: 600;
        border-radius: 6px;
        background: @primary-color;
        border-color: @primary-color;
        box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.2);

        &:hover {
          background: #2563eb;
          transform: translateY(-1px);
          box-shadow: 0 6px 8px -1px rgba(59, 130, 246, 0.3);
        }
      }

      .form-footer-action {
        margin-top: 24px;
        text-align: center;
        font-size: 14px;
        color: @text-sub-light;

        .divider { margin: 0 10px; color: #e5e7eb; }
      }
    }
  }
}
</style>
