<template>
  <div :class="['login-container', { 'dark-theme': isDarkMode }]">
    <!-- 顶部导航栏 -->
    <header class="login-header">
       <div class="header-content">
          <div class="logo-box">
             <img :src="logoUrl" class="logo" alt="Logo" />
             <span class="system-name">BearJia 管理系统</span>
          </div>
          <div class="header-links">
             <a href="#" class="link">帮助文档</a>
             <a href="#" class="link">关于我们</a>
          </div>
       </div>
    </header>

    <!-- 主体内容 -->
    <main class="login-main">
       <div class="login-card">
          <div class="card-header">
             <h2 class="title">账号登录</h2>
             <p class="subtitle">欢迎使用企业级数字化管理平台</p>
          </div>

          <a-form
             class="login-form"
             ref="loginFormRef"
             :model="loginFormModel"
             :rules="loginFormRules"
          >
             <a-form-item name="username">
                <a-input
                   v-model:value="loginFormModel.username"
                   size="large"
                   placeholder="用户名/手机号/邮箱"
                   class="formal-input"
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
                   class="formal-input"
                >
                   <template #prefix>
                      <LockOutlined class="input-icon" />
                   </template>
                </a-input-password>
             </a-form-item>

             <a-form-item name="code" v-if="loginFormModel.captchaEnabled">
                <div class="verify-box">
                   <a-input
                      v-model:value="loginFormModel.code"
                      size="large"
                      placeholder="验证码"
                      class="formal-input verify-input"
                      @keyup.enter="submitForm"
                   >
                      <template #prefix>
                         <SafetyOutlined class="input-icon" />
                      </template>
                   </a-input>
                   <div class="code-img-wrapper" @click="getVerifyCode">
                      <img :src="loginFormModel.codeUrl" v-if="loginFormModel.codeUrl" alt="验证码" />
                      <div v-else class="img-placeholder"><LoadingOutlined /></div>
                   </div>
                </div>
             </a-form-item>

             <div class="form-actions">
                <a-checkbox v-model:checked="loginFormModel.rememberMe">自动登录</a-checkbox>
                <a class="forgot-pwd">忘记密码？</a>
             </div>

             <a-button
                type="primary"
                size="large"
                block
                class="submit-btn"
                :loading="loginFormModel.loginButtonLoading"
                @click="submitForm"
             >
                {{ loginFormModel.loginButtonName }}
             </a-button>

             <div class="form-footer">
                <a-button type="link" @click="goToRegister">注册账户</a-button>
                <span class="divider">|</span>
                <a-button type="link" @click="switchLoginStyle">切换风格</a-button>
             </div>
          </a-form>
       </div>
    </main>

    <!-- 底部版权 -->
    <footer class="login-footer">
       <p class="copyright">Copyright © 2025 BearJia Technology Co., Ltd. All Rights Reserved.</p>
       <div class="links">
          <a href="#">隐私政策</a>
          <a href="#">服务条款</a>
       </div>
    </footer>
  </div>
</template>

<script setup>
import { getVerifyCodeImg } from '@/api/login.js';
import { ref, reactive, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { useAppStore } from '@/stores/app';
import { UserOutlined, LockOutlined, SafetyOutlined, LoadingOutlined } from '@ant-design/icons-vue';
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
  vueRouter.push('/login2');
};

onMounted(() => {
  getVerifyCode();
});
</script>

<style lang="less" scoped>
// 变量定义 - 正式商务风格
@primary-color: #1677ff; // 经典的阿里蓝
@bg-color: #f0f2f5;
@card-bg: #ffffff; // 恢复纯白，强调稳重
@text-main: #000000e0; // 85% 黑
@text-sub: #00000073; // 45% 黑
@border-color: #d9d9d9;

// 暗黑模式变量
@dark-bg: #000000;
@dark-card-bg: rgba(20, 20, 20, 0.85); // 暗黑模式下也稍微透明
@dark-text-main: #ffffffd9; // 85% 白
@dark-text-sub: #ffffff73; // 45% 白
@dark-border: #424242;

.login-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  // 使用高端商务建筑背景图
  background-image: url('https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=2670&auto=format&fit=crop');
  background-size: cover;
  background-position: center;
  position: relative;

  // 叠加深色蒙版，确保背景不喧宾夺主，且文字清晰
  &::before {
    content: '';
    position: absolute;
    top: 0; left: 0; right: 0; bottom: 0;
    background-color: rgba(0, 21, 41, 0.7); // 经典的深蓝遮罩
  }

  &.dark-theme {
     // 暗黑模式下加深遮罩
     &::before {
        background-color: rgba(0, 0, 0, 0.85);
     }

     .login-header .header-content {
        .logo-box .system-name { color: @dark-text-main; }
        .header-links .link { color: @dark-text-sub; &:hover { color: @dark-text-main; } }
     }

     .login-card {
        background: @dark-card-bg;
        box-shadow: 0 8px 32px rgba(0,0,0,0.6); // 更深的阴影
        border: 1px solid rgba(255,255,255,0.05);
        backdrop-filter: blur(8px);

        .card-header {
           .title { color: @dark-text-main; }
           .subtitle { color: @dark-text-sub; }
        }

        .form-actions {
           color: @dark-text-sub;
           :deep(.ant-checkbox-wrapper) { color: @dark-text-sub; }
        }

        .form-footer .divider { color: @dark-border; }

        .formal-input, .verify-box .code-img-wrapper {
           border-color: @dark-border;
           background: rgba(255,255,255,0.02);

           :deep(.ant-input) { color: @dark-text-main; }
           :deep(.ant-input-prefix), :deep(.ant-input-password-icon) { color: @dark-text-sub; }

           &:hover, &:focus-within { border-color: @primary-color; }
        }
     }

     .login-footer {
        .copyright { color: @dark-text-sub; }
        .links a { color: @dark-text-sub; &:hover { color: @dark-text-main; } }
     }
  }
}

// 顶部导航
.login-header {
   height: 64px;
   display: flex;
   justify-content: center;
   position: relative;
   z-index: 10;

   .header-content {
      width: 1200px;
      max-width: 95%;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .logo-box {
         display: flex;
         align-items: center;
         gap: 12px;

         .logo { height: 32px; width: 32px; }
         .system-name {
            font-size: 18px;
            font-weight: 600;
            color: #ffffff; // 默认深色背景下文字为白
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial;
            text-shadow: 0 2px 4px rgba(0,0,0,0.2);
         }
      }

      .header-links {
         .link {
            margin-left: 24px;
            color: rgba(255,255,255,0.8); // 默认白色
            font-size: 14px;
            transition: color 0.3s;
            &:hover { color: #ffffff; }
         }
      }
   }
}

// 主体内容
.login-main {
   flex: 1;
   display: flex;
   justify-content: center;
   align-items: center;
   padding: 32px 0;
   position: relative;
   z-index: 10;

   .login-card {
      width: 420px;
      background: @card-bg;
      border-radius: 4px; // 恢复较小的圆角，更显严谨
      padding: 48px 40px 36px;
      box-shadow: 0 4px 24px rgba(0, 0, 0, 0.2); // 柔和的投影
      // 移除边框高光和模糊，回归纯粹

      .card-header {
         text-align: center;
         margin-bottom: 40px;

         .title {
            font-size: 24px;
            font-weight: 600;
            color: @text-main;
            margin-bottom: 8px;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial;
         }

         .subtitle {
            font-size: 14px;
            color: @text-sub;
         }
      }

      // 输入框样式统一
      .formal-input {
         height: 40px;
         border-radius: 4px;

         :deep(.ant-input-prefix) { margin-right: 8px; color: rgba(0,0,0,0.25); }
      }

      .verify-box {
         display: flex;
         gap: 12px;

         .verify-input { flex: 1; }
         .code-img-wrapper {
            width: 110px;
            height: 40px;
            border: 1px solid @border-color;
            border-radius: 4px;
            cursor: pointer;
            overflow: hidden;
            flex-shrink: 0;

            img { width: 100%; height: 100%; object-fit: cover; }
            .img-placeholder {
               width: 100%; height: 100%;
               display: flex; align-items: center; justify-content: center;
               background: rgba(0,0,0,0.02);
               color: rgba(0,0,0,0.25);
            }
         }
      }

      .form-actions {
         display: flex;
         justify-content: space-between;
         margin-bottom: 24px;

         .forgot-pwd { color: @primary-color; cursor: pointer; &:hover { opacity: 0.8; } }
      }

      .submit-btn {
         height: 40px;
         font-size: 16px;
         border-radius: 4px;
      }

      .form-footer {
         margin-top: 24px;
         text-align: center;
         font-size: 14px;

         .divider { margin: 0 8px; color: @border-color; }
      }
   }
}

// 底部页脚
.login-footer {
   padding: 24px 0 32px;
   text-align: center;
   position: relative;
   z-index: 10;

   .copyright {
      color: rgba(255,255,255,0.65); // 默认背景深色，文字调亮
      font-size: 14px;
      margin-bottom: 8px;
   }

   .links {
      a {
         color: rgba(255,255,255,0.65);
         margin: 0 12px;
         font-size: 14px;
         &:hover { color: #ffffff; }
      }
   }
}
</style>
