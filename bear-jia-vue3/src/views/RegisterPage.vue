<template>
  <div id="userLayout" :class="['user-layout-wrapper']">
    <div class="container">
      <div class="loginbox">
        <div class="top">
          <div class="header">
            <a href="/" class="brand-link">
              <div class="logo-container">
                <img :src="logoUrl" class="logo" alt="BearJia Logo" />
              </div>
              <div class="title-container">
                <span class="title">BearJia</span>
                <span class="subtitle">Admin</span>
              </div>
            </a>
          </div>
        </div>
        <div class="main">
          <a-form id="registerForm" ref="registerFormRef" class="user-layout-login" :model="registerFormModel" :rules="registerFormRules" :scrollToFirstError="true">
            <a-page-header title="          用户注册" />
            <a-form-item name="username">
              <a-input v-model:value="registerFormModel.username" size="large" placeholder="用户名">
                <template #prefix>
                  <UserOutlined style="padding-right: 5px; color: blue" />
                </template>
              </a-input>
            </a-form-item>
            <a-form-item name="password">
              <a-input-password v-model:value="registerFormModel.password" size="large" placeholder="密码">
                <template #prefix>
                  <LockOutlined style="padding-right: 5px; color: blue" />
                </template>
              </a-input-password>
            </a-form-item>
            <a-form-item name="confirmPassword">
              <a-input-password v-model:value="registerFormModel.confirmPassword" size="large" placeholder="确认密码">
                <template #prefix>
                  <LockOutlined style="padding-right: 5px; color: blue" />
                </template>
              </a-input-password>
            </a-form-item>
            <a-form-item name="code">
              <a-row :gutter="16">
                <a-col class="gutter-row" :span="16">
                  <a-input v-model:value="registerFormModel.code" size="large" type="text" autocomplete="off" placeholder="验证码">
                    <template #prefix>
                      <SecurityScanOutlined style="padding-right: 5px; color: blue" />
                    </template>
                  </a-input>
                </a-col>
                <a-col class="gutter-row" :span="8">
                  <img class="getCaptcha" :src="registerFormModel.codeUrl" @click="getVerifyCode" />
                </a-col>
              </a-row>
            </a-form-item>
            <a-form-item>
              <a-button size="large" type="primary" htmlType="submit" class="login-button" :disabled="registerFormModel.registerButtonDisabled" :loading="registerFormModel.registerButtonLoading" @click="submitForm()">{{ registerFormModel.registerButtonName }}</a-button>
            </a-form-item>
            <a-form-item>
              <div style="float: left; line-height: 30px">
                已有账号？
                <a-button type="link" @click="goToLogin">立即登录</a-button>
              </div>
            </a-form-item>
          </a-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { getVerifyCodeImg, register } from '@/api/login.js';
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useAppStore } from '@/stores/app';
import { UserOutlined, LockOutlined, SecurityScanOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import logger from '@/utils/logger';
import logoUrl from '@/assets/images/logo.png';

const vueRouter = useRouter();
const vueStore = useAppStore();

const registerFormRef = ref();

const registerFormModel = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  code: '',
  uuid: '',
  codeUrl: '',
  registerButtonDisabled: false,
  registerButtonLoading: false,
  registerButtonName: '注册',
});

const registerFormRules = reactive({
  username: [
    { required: true, message: '请输入用户名！', trigger: 'blur' },
    { min: 4, max: 20, message: '用户名长度必须在4-20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码！', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度必须在6-20个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码！', trigger: 'blur' },
    {
      validator: (rule, value) => {
        if (value === registerFormModel.password) {
          return Promise.resolve();
        }
        return Promise.reject('两次输入的密码不一致！');
      },
      trigger: 'blur'
    }
  ],
  code: [{ required: true, message: '请输入验证码！', trigger: 'blur' }],
});

const getVerifyCode = () => {
  getVerifyCodeImg().then((res) => {
    if (res.captchaEnabled){
      if (res.img.startsWith('data:image')) {
        registerFormModel.codeUrl = res.img;
      } else {
        //适配若依的验证码
        registerFormModel.codeUrl = "data:image/gif;base64," + res.img
      }
    }
    registerFormModel.uuid = res.uuid;
  });
};

const submitForm = () => {
  registerFormModel.registerButtonDisabled = true;
  registerFormModel.registerButtonLoading = true;
  registerFormModel.registerButtonName = '注册中...';
  registerFormRef.value
    .validate()
    .then(() => {
      // 调用注册API
      register({
        username: registerFormModel.username,
        password: registerFormModel.password,
        code: registerFormModel.code,
        uuid: registerFormModel.uuid
      })
        .then(res => {
          message.success('注册成功，请登录');
          vueRouter.push({ path: '/login' });
        })
        .catch(error => {
          logger.error('注册失败:', error);
          registerFormModel.registerButtonDisabled = false;
          registerFormModel.registerButtonLoading = false;
          registerFormModel.registerButtonName = '注册';
          getVerifyCode();
        });
    })
    .catch((error) => {
      registerFormModel.registerButtonDisabled = false;
      registerFormModel.registerButtonLoading = false;
      registerFormModel.registerButtonName = '注册';
      logger.warn('表单验证失败:', error);
    });
};

const goToLogin = () => {
  vueRouter.push('/login');
};

getVerifyCode();
</script>

<style lang="less" scoped>
#userLayout.user-layout-wrapper {
  height: 100%;
  .container {
    width: 100%;
    min-height: 100%;
    background: #e2effc url(/src/assets/images/pages/login/loginPageBackground.jpg) no-repeat center top;
    background-size: 100%;
    vertical-align: middle;
    display: flex;

    a {
      text-decoration: none;
    }
    .loginbox {
      width: 800px;
      height: 500px;
      margin: auto;
      background: var(--component-background);
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
      border-radius: 8px;
    }
    .top {
      text-align: center;
      width: 355px;
      height: 500px;
      background: url(/src/assets/images/pages/login/loginPageLogoBackground.png) no-repeat center top;
      float: left;

      .header {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
        padding-top: 80px;

        .brand-link {
          display: flex;
          flex-direction: column;
          align-items: center;
          text-decoration: none;
          transition: all 0.3s ease;

          &:hover {
            transform: translateY(-2px);
          }

          .logo-container {
            margin-bottom: 20px;
            
            .logo {
              height: 80px;
              width: 80px;
              object-fit: contain;
              border-radius: 12px;
              box-shadow: 0 4px 12px rgba(255, 255, 255, 0.2);
              transition: all 0.3s ease;
              
              &:hover {
                box-shadow: 0 6px 20px rgba(255, 255, 255, 0.3);
              }
            }
          }

          .title-container {
            text-align: center;
            
            .title {
              display: block;
              font-size: 32px;
              color: #ffffff;
              font-family: 'Helvetica Neue', Arial, sans-serif;
              font-weight: 700;
              letter-spacing: 1px;
              margin-bottom: 4px;
              text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
            }

            .subtitle {
              display: block;
              font-size: 16px;
              color: rgba(255, 255, 255, 0.9);
              font-family: 'Helvetica Neue', Arial, sans-serif;
              font-weight: 400;
              letter-spacing: 2px;
              text-transform: uppercase;
              text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
            }
          }
        }
      }
    }

    .main {
      width: 445px;
      height: 500px;
      margin: 0 auto;
      float: left;
      display: block;
      padding: 0 30px;
      position: relative;
      background: var(--component-background);
    }
    .user-layout-login {
      .ant-page-header {
        padding: 60px 0 45px 0;
        
        .ant-page-header-heading-title {
          color: var(--text-primary);
        }
      }
      .getCaptcha {
        display: block;
        width: 100%;
        height: 40px;
      }

      button.login-button {
        padding: 0 15px;
        font-size: 16px;
        height: 40px;
        width: 100%;
        border-radius: 4px;
      }
      
      // 输入框样式
      .ant-input,
      .ant-input-password {
        background: var(--input-bg);
        color: var(--text-primary);
        border-color: var(--border-color-base);
        
        &::placeholder {
          color: var(--text-secondary);
        }
      }
      
      // 按钮样式
      .ant-btn {
        color: var(--text-primary);
        border-color: var(--border-color-base);
        
        &.ant-btn-primary {
          background: var(--primary-color);
          border-color: var(--primary-color);
          color: #fff;
        }
        
        &.ant-btn-link {
          color: var(--primary-color);
        }
      }
    }
  }
  
  // 暗黑模式下的特殊样式
  &.dark-theme {
    .container {
      background: #1a1a1a url(/src/assets/images/pages/login/loginPageBackground.jpg) no-repeat center top;
      background-size: 100%;
      
      .loginbox {
        background: var(--component-background);
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
      }
      
      .main {
        background: var(--component-background);
      }
    }
  }
}
</style>
