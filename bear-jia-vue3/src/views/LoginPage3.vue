<template>
  <div :class="['login-container', { 'dark-theme': isDarkMode }]">
    <!-- 动态背景 -->
    <div class="background-animate">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="shape shape-4"></div>
    </div>

    <!-- 登录卡片 -->
    <div class="login-card glass-effect">
      <div class="card-left">
        <div class="welcome-content">
          <div class="logo-box">
             <img :src="logoUrl" class="logo" alt="BearJia Logo" />
             <span class="brand-name">BearJia</span>
          </div>
          <h2 class="welcome-title">Welcome Back!</h2>
          <p class="welcome-desc">探索高效的现代化管理平台，开启您的工作之旅。</p>
          <div class="illustration">
             <!-- 使用 CSS 绘制的简单图形替代图片，保持轻量 -->
             <div class="circle c1"></div>
             <div class="circle c2"></div>
             <div class="card-preview"></div>
          </div>
        </div>
      </div>

      <div class="card-right">
        <div class="form-header">
           <h3>账号登录</h3>
           <p>请输入您的账号信息</p>
        </div>

        <a-form
          class="login-form"
          :model="loginFormModel"
          :rules="loginFormRules"
          ref="loginFormRef"
        >
          <a-form-item name="username">
            <a-input
              v-model:value="loginFormModel.username"
              placeholder="用户名"
              size="large"
              class="custom-input"
            >
              <template #prefix>
                <UserOutlined class="input-icon" />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item name="password">
            <a-input-password
              v-model:value="loginFormModel.password"
              placeholder="密码"
              size="large"
              class="custom-input"
            >
              <template #prefix>
                <LockOutlined class="input-icon" />
              </template>
            </a-input-password>
          </a-form-item>

          <div class="verify-wrapper" v-if="loginFormModel.captchaEnabled">
             <a-form-item name="code" class="verify-input">
               <a-input
                 v-model:value="loginFormModel.code"
                 placeholder="验证码"
                 size="large"
                 class="custom-input"
                 :maxLength="4"
                 @keyup.enter="submitForm"
               >
                 <template #prefix>
                   <SafetyOutlined class="input-icon" />
                 </template>
               </a-input>
             </a-form-item>
             <div class="verify-img" @click="getVerifyCode" title="点击刷新">
               <img :src="loginFormModel.codeUrl" v-if="loginFormModel.codeUrl" alt="验证码" />
               <div v-else class="img-placeholder"><LoadingOutlined /></div>
             </div>
          </div>

          <div class="form-options">
            <a-checkbox v-model:checked="loginFormModel.rememberMe">记住我</a-checkbox>
            <a class="forgot-pwd">忘记密码?</a>
          </div>

          <a-button
            type="primary"
            class="submit-btn"
            size="large"
            block
            :loading="loginFormModel.loginButtonLoading"
            @click="submitForm"
          >
            {{ loginFormModel.loginButtonName }}
          </a-button>

          <div class="other-login">
             <span class="divider-text">其他方式登录</span>
             <div class="social-icons">
               <div class="icon-btn wechat"><WechatOutlined /></div>
               <div class="icon-btn alipay"><AlipayCircleOutlined /></div>
               <div class="icon-btn github"><GithubOutlined /></div>
             </div>
          </div>

          <div class="bottom-actions">
            <span>还没有账号?</span>
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
  LoadingOutlined,
  WechatOutlined,
  AlipayCircleOutlined,
  GithubOutlined
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
  vueRouter.push('/login');
};

onMounted(() => {
  getVerifyCode();
});
</script>

<style lang="less" scoped>
// 变量定义
@primary-color: #6366f1;
@primary-gradient: linear-gradient(135deg, #6366f1 0%, #a855f7 100%);
@bg-light: #f3f4f6;
@bg-dark: #0f172a;
@card-light: rgba(255, 255, 255, 0.8);
@card-dark: rgba(30, 41, 59, 0.7);
@text-main-light: #1f2937;
@text-main-dark: #f1f5f9;
@text-sub-light: #6b7280;
@text-sub-dark: #94a3b8;

.login-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  background-color: @bg-light;
  transition: all 0.3s ease;

  &.dark-theme {
    background-color: @bg-dark;

    .background-animate .shape {
       opacity: 0.3;
    }

    .login-card {
      background: @card-dark;
      border: 1px solid rgba(255, 255, 255, 0.1);

      .card-left {
        background: rgba(0, 0, 0, 0.2);
        .welcome-title { color: white; }
        .welcome-desc { color: @text-sub-dark; }
        .brand-name { color: white; }
      }

      .card-right {
        .form-header h3 { color: @text-main-dark; }
        .form-header p { color: @text-sub-dark; }

        .custom-input {
           :deep(.ant-input) {
             background: rgba(0,0,0,0.3);
             border-color: rgba(255,255,255,0.1);
             color: white;
             &::placeholder { color: rgba(255,255,255,0.3); }
           }
           :deep(.ant-input-prefix) { color: rgba(255,255,255,0.5); }
        }

        .form-options {
          color: @text-sub-dark;
          :deep(.ant-checkbox-wrapper) { color: @text-sub-dark; }
        }

        .other-login .divider-text {
           color: @text-sub-dark;
           &::before, &::after { background: rgba(255,255,255,0.1); }
        }

        .icon-btn {
           background: rgba(255,255,255,0.05);
           color: @text-sub-dark;
           &:hover { background: rgba(255,255,255,0.1); color: white; }
        }
      }
    }
  }
}

// 动态背景
.background-animate {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;

  .shape {
    position: absolute;
    filter: blur(80px);
    opacity: 0.6;
    animation: float 20s infinite ease-in-out;
  }

  .shape-1 {
    top: -10%;
    left: -10%;
    width: 600px;
    height: 600px;
    background: #6366f1;
    border-radius: 50%;
    animation-delay: 0s;
  }

  .shape-2 {
    bottom: -10%;
    right: -5%;
    width: 500px;
    height: 500px;
    background: #ec4899;
    border-radius: 40% 60% 70% 30% / 40% 50% 60% 50%;
    animation-delay: -5s;
  }

  .shape-3 {
    top: 30%;
    right: 20%;
    width: 300px;
    height: 300px;
    background: #06b6d4;
    border-radius: 50%;
    animation-delay: -10s;
  }

  .shape-4 {
    bottom: 10%;
    left: 20%;
    width: 250px;
    height: 250px;
    background: #8b5cf6;
    border-radius: 50%;
    animation-delay: -15s;
  }
}

// 登录卡片
.login-card {
  position: relative;
  z-index: 10;
  display: flex;
  width: 900px;
  max-width: 95vw;
  height: 580px;
  background: @card-light;
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 24px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.5);

  // 左侧区域
  .card-left {
    flex: 1;
    background: rgba(99, 102, 241, 0.05);
    padding: 40px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    position: relative;
    overflow: hidden;

    @media (max-width: 768px) {
      display: none;
    }

    .welcome-content {
      position: relative;
      z-index: 2;
    }

    .logo-box {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 40px;

      .logo { width: 40px; height: 40px; }
      .brand-name {
        font-size: 24px;
        font-weight: 800;
        background: @primary-gradient;
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }
    }

    .welcome-title {
      font-size: 36px;
      font-weight: 800;
      color: @text-main-light;
      margin-bottom: 16px;
      line-height: 1.2;
    }

    .welcome-desc {
      font-size: 16px;
      color: @text-sub-light;
      line-height: 1.6;
      max-width: 80%;
    }

    .illustration {
      margin-top: 60px;
      position: relative;
      height: 200px;

      .circle {
        position: absolute;
        border-radius: 50%;
        background: linear-gradient(135deg, rgba(255,255,255,0.4), rgba(255,255,255,0.1));
        backdrop-filter: blur(10px);
        border: 1px solid rgba(255,255,255,0.3);
        box-shadow: 0 8px 32px rgba(0,0,0,0.05);
      }

      .c1 { width: 80px; height: 80px; top: 20px; left: 20px; animation: float 6s ease-in-out infinite; }
      .c2 { width: 60px; height: 60px; bottom: 40px; right: 60px; animation: float 8s ease-in-out infinite reverse; }

      .card-preview {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 240px;
        height: 140px;
        background: linear-gradient(135deg, rgba(255,255,255,0.8), rgba(255,255,255,0.4));
        border-radius: 16px;
        border: 1px solid rgba(255,255,255,0.5);
        box-shadow: 0 10px 40px rgba(0,0,0,0.1);
        backdrop-filter: blur(5px);
        animation: float 7s ease-in-out infinite;

        &::before {
          content: '';
          position: absolute;
          top: 20px; left: 20px;
          width: 40px; height: 40px;
          border-radius: 50%;
          background: rgba(99, 102, 241, 0.2);
        }

        &::after {
          content: '';
          position: absolute;
          bottom: 20px; left: 20px; right: 20px;
          height: 8px;
          border-radius: 4px;
          background: rgba(0,0,0,0.05);
        }
      }
    }
  }

  // 右侧表单
  .card-right {
    flex: 1.2;
    padding: 50px;
    display: flex;
    flex-direction: column;
    justify-content: center;

    .form-header {
      margin-bottom: 32px;
      text-align: center;

      h3 { font-size: 24px; font-weight: 700; color: @text-main-light; margin-bottom: 8px; }
      p { color: @text-sub-light; font-size: 14px; }
    }

    .custom-input {
      :deep(.ant-input) {
        border-radius: 8px;
        background: rgba(255,255,255,0.5);
        border: 1px solid transparent;
        transition: all 0.3s;
        padding-left: 36px;

        &:hover, &:focus {
          background: #fff;
          border-color: @primary-color;
          box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.1);
        }
      }

      :deep(.ant-input-prefix) { margin-right: 10px; color: #9ca3af; }
    }

    .verify-wrapper {
      display: flex;
      gap: 12px;
      margin-bottom: 12px;

      .verify-input { flex: 1; margin-bottom: 0; }

      .verify-img {
        width: 100px;
        height: 40px;
        border-radius: 8px;
        overflow: hidden;
        cursor: pointer;
        border: 1px solid rgba(0,0,0,0.05);
        transition: all 0.3s;

        &:hover { transform: scale(1.02); box-shadow: 0 2px 8px rgba(0,0,0,0.05); }

        img { width: 100%; height: 100%; object-fit: cover; }

        .img-placeholder {
          width: 100%; height: 100%;
          background: rgba(0,0,0,0.05);
          display: flex; align-items: center; justify-content: center;
          color: @primary-color;
        }
      }
    }

    .form-options {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;

      .forgot-pwd { color: @primary-color; cursor: pointer; transition: 0.3s; &:hover { text-decoration: underline; } }
    }

    .submit-btn {
      height: 44px;
      border-radius: 8px;
      font-size: 16px;
      font-weight: 600;
      background: @primary-gradient;
      border: none;
      box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
      transition: all 0.3s;

      &:hover { transform: translateY(-1px); box-shadow: 0 6px 16px rgba(99, 102, 241, 0.4); }
      &:active { transform: translateY(0); }
    }

    .other-login {
      margin-top: 30px;
      text-align: center;

      .divider-text {
        position: relative;
        color: @text-sub-light;
        font-size: 12px;
        display: block;
        margin-bottom: 20px;

        &::before, &::after {
          content: '';
          position: absolute;
          top: 50%;
          width: 30%;
          height: 1px;
          background: #e5e7eb;
        }
        &::before { left: 0; }
        &::after { right: 0; }
      }

      .social-icons {
        display: flex;
        justify-content: center;
        gap: 16px;

        .icon-btn {
          width: 40px; height: 40px;
          border-radius: 50%;
          display: flex; align-items: center; justify-content: center;
          border: 1px solid rgba(0,0,0,0.05);
          cursor: pointer;
          transition: all 0.3s;
          color: @text-sub-light;
          font-size: 18px;

          &:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
          &.wechat:hover { color: #07c160; background: rgba(7, 193, 96, 0.1); }
          &.alipay:hover { color: #1677ff; background: rgba(22, 119, 255, 0.1); }
          &.github:hover { color: #333; background: rgba(0,0,0,0.1); }
        }
      }
    }

    .bottom-actions {
      margin-top: 24px;
      text-align: center;
      font-size: 14px;
      color: @text-sub-light;

      .divider { margin: 0 8px; color: #e5e7eb; }
    }
  }
}

@keyframes float {
  0% { transform: translate(0, 0); }
  50% { transform: translate(10px, 20px); }
  100% { transform: translate(0, 0); }
}
</style>
