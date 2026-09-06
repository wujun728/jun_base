<template>
  <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" size="large">
    <el-form-item prop="username">
      <el-input v-model="loginForm.username" placeholder="用户名">
        <template #prefix>
          <el-icon class="el-input__icon">
            <user />
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input v-model="loginForm.password" type="password" placeholder="密码" show-password autocomplete="new-password">
        <template #prefix>
          <el-icon class="el-input__icon">
            <lock />
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="code">
      <el-input v-model="loginForm.code" size="large" auto-complete="off" placeholder="验证码" style="width: 63%">
        <template #prefix><SvgIcon name="validCode" :icon-style="{ width: '14px', height: '14px' }" /></template>
      </el-input>
      <div class="login-code">
        <img :src="codeUrl" @click="getCode" class="login-code-img" />
      </div>
    </el-form-item>
  </el-form>
  <div class="login-btn">
    <el-button :icon="CircleClose" round size="large" @click="resetForm(loginFormRef)"> 重置 </el-button>
    <el-button :icon="UserFilled" round size="large" type="primary" :loading="loading" @click="login(loginFormRef)">
      登录
    </el-button>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { HOME_URL } from "@/config";
import { getTimeState } from "@/utils";
import { Login } from "@/api/interface";
import { ElNotification } from "element-plus";
import { getCodeImg, loginApi } from "@/api/ruoyimodule/login";
import { useUserStore } from "@/stores/modules/user";
import { useTabsStore } from "@/stores/modules/tabs";
import { useKeepAliveStore } from "@/stores/modules/keepAlive";
import { initDynamicRouter } from "@/routers/modules/dynamicRouter";
import { CircleClose, UserFilled } from "@element-plus/icons-vue";
import type { ElForm } from "element-plus";
import SvgIcon from "@/components/SvgIcon/index.vue";

const router = useRouter();
const userStore = useUserStore();
const tabsStore = useTabsStore();
const keepAliveStore = useKeepAliveStore();

type FormInstance = InstanceType<typeof ElForm>;
const loginFormRef = ref<FormInstance>();
const loginRules = reactive({
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }]
});
const codeUrl = ref("");

const loading = ref(false);
const loginForm = reactive<Login.ReqLoginForm>({
  username: "",
  password: "",
  code: "",
  uuid: ""
});

// login
const login = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.validate(async valid => {
    if (!valid) return;
    loading.value = true;
    try {
      // 1.执行登录接口
      const res: any = await loginApi({ ...loginForm });
      userStore.setToken(res.token);

      // 2.添加动态路由
      await initDynamicRouter();

      // 3.清空 tabs、keepAlive 数据
      tabsStore.setTabs([]);
      keepAliveStore.setKeepAliveName([]);

      // 4.跳转到首页
      router.push(HOME_URL);
      ElNotification({
        title: getTimeState(),
        message: "欢迎登录 Geeker-Admin",
        type: "success",
        duration: 3000
      });
    } finally {
      loading.value = false;
      getCode();
    }
  });
};

// resetForm
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
};

function getCode() {
  getCodeImg().then((res: any) => {
    codeUrl.value = "data:image/gif;base64," + res.img;
    loginForm.uuid = res.uuid;
    loginForm.code = "";
  });
}

getCode();

onMounted(() => {
  // 监听 enter 事件（调用登录）
  document.onkeydown = (e: KeyboardEvent) => {
    e = (window.event as KeyboardEvent) || e;
    if (e.code === "Enter" || e.code === "enter" || e.code === "NumpadEnter") {
      if (loading.value) return;
      login(loginFormRef.value);
    }
  };
});
if (import.meta.env.MODE === "development") {
  loginForm.username = "admin";
  loginForm.password = "admin123";
}
</script>

<style scoped lang="scss">
@import "../index.scss";
.login-code {
  float: right;
  width: 33%;
  height: 40px;
  img {
    vertical-align: middle;
    cursor: pointer;
  }
}
.login-code-img {
  height: 40px;
  padding-left: 12px;
}
</style>
