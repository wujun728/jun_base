<script setup lang="ts">
import { encrypt, decrypt } from "@/utils/jsencrypt";
import useUserStore from '@/store/modules/user'
import { useRouter } from "vue-router";
import { onMounted, ref } from "vue";
import { getCodeImg, sendEmailCode, sendPhoneCode } from "@/api/login";
import { RoutesAlias } from "@/router/routesAlias";
import { useStorage } from "@vueuse/core";

const props = defineProps<{
  register: boolean,
  captchaEnabled: boolean,
  method: 'password' | 'phone' | 'email'
}>()

const userStore = useUserStore()
const router = useRouter();

const loginForm = ref({
  username: "",
  password: "",
  email: '',
  phonenumber: '',
  code: "",
  uuid: "",
  rememberMe: false,
  autoRegister: false
});

const loginFormState = useStorage('loginForm', loginForm.value);

const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
};

const codeUrl = ref("");
const loading = ref(false);
const redirect = ref(undefined);
const loginRef = ref<any | null>(null)

function handleLogin() {
  loginRef.value?.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      userStore.login(loginForm.value, props.method).then(() => {
        router.push({ path: redirect.value || "/" });
      }).catch(() => {
        getCode();
      }).finally(() => {
        loading.value = false;
        if (loginForm.value.rememberMe) {
          loginFormState.value.username = loginForm.value.username;
          loginFormState.value.email = loginForm.value.email;
          loginFormState.value.phonenumber = loginForm.value.phonenumber;
          loginFormState.value.password = encrypt(loginForm.value.password);
          loginFormState.value.rememberMe = loginForm.value.rememberMe;
        } else {
          loginFormState.value.username = '';
          loginFormState.value.password = '';
          loginFormState.value.email = '';
          loginFormState.value.phonenumber = '';
          loginFormState.value.rememberMe = false;
        }
      });
    }
  });
}

function getCode() {
  if (!props.captchaEnabled) return
  getCodeImg().then((res: any) => {
    codeUrl.value = "data:image/gif;base64," + res.img;
    loginForm.value.uuid = res.uuid;
  });
}

function sendCode() {
  if (props.method === 'email') {
    sendEmailCode(loginForm.value, 'login')
  } else if (props.method === 'phone') {
    sendPhoneCode(loginForm.value, 'login')
  }
}

function getCookie() {
  loginForm.value = {
    username: loginFormState.value.username ?? '',
    password: loginFormState.value.password ? decrypt(loginFormState.value.password) : '',
    email: loginFormState.value.email ?? '',
    phonenumber: loginFormState.value.phonenumber ?? '',
    code: "",
    uuid: "",
    rememberMe: !loginFormState.value.rememberMe ? false : loginFormState.value.rememberMe,
    autoRegister: false
  };
}

onMounted(() => {
  getCookie();
  getCode();
})
</script>

<template>
  <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
    <el-form-item prop="email" v-if="method === 'email'">
      <el-input v-model="loginForm.email" type="text" size="large" auto-complete="off" placeholder="邮箱">
        <template #prefix>
          <svg-icon icon-class="email" class="el-input__icon input-icon" />
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="phonenumber" v-else-if="method === 'phone'">
      <el-input v-model="loginForm.phonenumber" type="text" size="large" auto-complete="off" placeholder="手机号">
        <template #prefix>
          <svg-icon icon-class="phone" class="el-input__icon input-icon" />
        </template>
      </el-input>
    </el-form-item>
    <div v-else>
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" type="text" size="large" auto-complete="off" placeholder="账号">
          <template #prefix><svg-icon icon-class="user" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input v-model="loginForm.password" type="password" size="large" auto-complete="off" placeholder="密码"
          show-password @keyup.enter="handleLogin">
          <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>
    </div>
    <el-form-item prop="code" v-if="method === 'email' || method === 'phone'">
      <el-input size="large" v-model="loginForm.code" auto-complete="off" placeholder="验证码" style="width: 63%"
        @keyup.enter="handleLogin">
        <template #prefix>
          <svg-icon icon-class="validCode" class="el-input__icon input-icon" />
        </template>
      </el-input>
      <div class="login-code">
        <el-button class="login-code-img" @click="sendCode">发送验证码</el-button>
      </div>
    </el-form-item>
    <el-form-item prop="code" v-if="captchaEnabled && method === 'password'">
      <el-input v-model="loginForm.code" size="large" auto-complete="off" placeholder="验证码" style="width: 63%"
        @keyup.enter="handleLogin">
        <template #prefix><svg-icon icon-class="validCode" class="el-input__icon input-icon" /></template>
      </el-input>
      <div class="login-code">
        <img :src="codeUrl" @click="getCode" class="login-code-img" />
      </div>
    </el-form-item>
    <div style="width:100%;display:flex;justify-content:space-between;align-items:center;">
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住我！</el-checkbox>
      <el-checkbox v-if="method != 'password'" v-model="loginForm.autoRegister"
        style="margin:0px 0px 25px 0px;">若无账号自动注册~</el-checkbox>
    </div>
    <el-form-item style="width:100%;">
      <el-button :loading="loading" size="large" type="primary" style="width:100%;" @click.prevent="handleLogin">
        <span v-if="!loading">登 录</span>
        <span v-else>登 录 中...</span>
      </el-button>
      <div class="register-link" v-if="register">
        <span class="question-text">没有账号？</span>
        <router-link class="link-type" :to="RoutesAlias.Register">立即注册</router-link>
      </div>
    </el-form-item>
  </el-form>
</template>
<style lang='scss' scoped>
.login-form {
  .el-input {
    height: 40px;

    input {
      height: 40px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}

.login-code {
  width: 37%;
  height: 40px;
  float: right;

  .login-code-img {
    cursor: pointer;
    vertical-align: middle;
    width: calc(100% - 12px);
    height: 40px;
    margin-left: 12px;
  }
}


.register-link {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 5px;

  .question-text {
    color: #606266;
    margin-right: 8px;
    font-size: 14px;
  }

  .link-type {
    color: #409EFF;
    text-decoration: none;
    font-weight: 500;
    font-size: 14px;
    position: relative;
    transition: all 0.3s ease;

    &::after {
      content: '';
      position: absolute;
      bottom: -2px;
      left: 0;
      width: 0;
      height: 2px;
      background-color: #409EFF;
      transition: width 0.3s ease;
    }

    &:hover {
      color: #66b1ff;

      &::after {
        width: 100%;
      }
    }
  }
}
</style>
