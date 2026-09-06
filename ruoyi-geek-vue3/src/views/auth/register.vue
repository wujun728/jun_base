<script setup lang="ts">
import { ElMessageBox } from "element-plus";
import { getCodeImg, sendEmailCode, sendPhoneCode } from "@/api/login";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import useUserStore from "@/store/modules/user";
import { RoutesAlias } from "@/router/routesAlias";

const router = useRouter();
const userStore = useUserStore()
const props = defineProps<{
  register: boolean,
  captchaEnabled: boolean,
  method: 'password' | 'phone' | 'email'
}>()


const registerForm = ref({
  username: "",
  password: "",
  confirmPassword: "",
  email: '',
  phonenumber: '',
  code: "",
  uuid: "",
});

const equalToPassword = (rule: any, value: any, callback: Function) => {
  if (registerForm.value.password !== value) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};

const registerRules = {
  username: [
    { required: true, trigger: "blur", message: "请输入您的账号" },
    { min: 2, max: 20, message: "用户账号长度必须介于 2 和 20 之间", trigger: "blur" }
  ],
  password: [
    { required: true, trigger: "blur", message: "请输入您的密码" },
    { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
    { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
  ],
  confirmPassword: [
    { required: true, trigger: "blur", message: "请再次输入您的密码" },
    { required: true, validator: equalToPassword, trigger: "blur" }
  ],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
};

const codeUrl = ref("");
const loading = ref(false);

const registerRef = ref<any | null>(null)
function handleRegister() {
  registerRef.value?.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      userStore.register(registerForm.value, props.method).then(() => {
        const username = registerForm.value.username;
        ElMessageBox.alert("<font color='red'>恭喜你，您的账号 " + username + " 注册成功！</font>", "系统提示", {
          dangerouslyUseHTMLString: true,
          type: "success",
        }).then(() => {
          router.push(RoutesAlias.Login);
        }).catch(() => { });
      }).catch(() => {
        getCode();
      }).finally(() => {
        loading.value = false;
      });
    }
  });
}

function getCode() {
  if (!props.captchaEnabled) return
  getCodeImg().then((res: any) => {
    codeUrl.value = "data:image/gif;base64," + res.img;
    registerForm.value.uuid = res.uuid;
  });
}

function sendCode() {
  if (props.method === 'email') {
    sendEmailCode(registerForm.value, 'register')
  } else if (props.method === 'phone') {
    sendPhoneCode(registerForm.value, 'register')
  }
}

onMounted(() => {
  getCode();
})

</script>
<template>
  <el-form ref="registerRef" :model="registerForm" :rules="registerRules" class="register-form">
    <el-form-item prop="email" v-if="method === 'email'">
      <el-input v-model="registerForm.email" type="text" size="large" auto-complete="off" placeholder="邮箱">
        <template #prefix>
          <svg-icon icon-class="email" class="el-input__icon input-icon" />
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="phonenumber" v-else-if="method === 'phone'">
      <el-input v-model="registerForm.phonenumber" type="text" size="large" auto-complete="off" placeholder="手机号">
        <template #prefix>
          <svg-icon icon-class="phone" class="el-input__icon input-icon" />
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="username" v-else>
      <el-input v-model="registerForm.username" type="text" size="large" auto-complete="off" placeholder="账号">
        <template #prefix>
          <svg-icon icon-class="user" class="el-input__icon input-icon" />
        </template>
      </el-input>
    </el-form-item>

    <el-form-item prop="password">
      <el-input v-model="registerForm.password" type="password" size="large" auto-complete="off" placeholder="密码"
        @keyup.enter="handleRegister">
        <template #prefix>
          <svg-icon icon-class="password" class="el-input__icon input-icon" />
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="confirmPassword">
      <el-input v-model="registerForm.confirmPassword" type="password" size="large" auto-complete="off"
        placeholder="确认密码" @keyup.enter="handleRegister">
        <template #prefix>
          <svg-icon icon-class="password" class="el-input__icon input-icon" />
        </template>
      </el-input>
    </el-form-item>

    <el-form-item prop="code" v-if="method === 'email' || method === 'phone'">
      <el-input size="large" v-model="registerForm.code" auto-complete="off" placeholder="验证码" style="width: 63%"
        @keyup.enter="handleRegister">
        <template #prefix>
          <svg-icon icon-class="validCode" class="el-input__icon input-icon" />
        </template>
      </el-input>
      <div class="register-code">
        <el-button class="register-code-img" @click="sendCode">发送验证码</el-button>
      </div>
    </el-form-item>
    <el-form-item prop="code" v-if="captchaEnabled && method === 'password'">
      <el-input size="large" v-model="registerForm.code" auto-complete="off" placeholder="验证码" style="width: 63%"
        @keyup.enter="handleRegister">
        <template #prefix>
          <svg-icon icon-class="validCode" class="el-input__icon input-icon" />
        </template>
      </el-input>
      <div class="register-code">
        <img :src="codeUrl" @click="getCode" class="register-code-img" />
      </div>
    </el-form-item>
    <el-form-item style="width:100%;">
      <el-button :loading="loading" size="large" type="primary" style="width:100%;" @click.prevent="handleRegister">
        <span v-if="!loading">注 册</span>
        <span v-else>注 册 中...</span>
      </el-button>
      <div class="login-link">
        <span class="question-text">已有账号？</span>
        <router-link class="link-type" :to="RoutesAlias.Login">立即登录</router-link>
      </div>
    </el-form-item>
  </el-form>
</template>

<style lang='scss' scoped>
.register-form {

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


.register-code {
  width: 37%;
  height: 40px;
  float: right;

  .register-code-img {
    cursor: pointer;
    vertical-align: middle;
    width: calc(100% - 12px);
    height: 40px;
    margin-left: 12px;
  }
}

.login-link {
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
