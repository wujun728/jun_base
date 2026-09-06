<template>
  <div class="login-form-wrapper">
    <div class="login-form-title">{{ '登录 RuoYi Arco Design Pro' }}</div>
    <div class="login-form-sub-title">{{ '登录 RuoYi Arco Design Pro' }}</div>
    <div class="login-form-error-msg">{{ errorMessage }}</div>
    <a-form
        ref="loginForm"
        :model="userInfo"
        class="login-form"
        layout="vertical"
        @submit="handleSubmit"
    >
      <a-form-item
          field="username"
          :rules="[{ required: true, message: '用户名不能为空' }]"
          :validate-trigger="['change', 'blur']"
          hide-label
      >
        <a-input
            v-model="userInfo.username"
            :placeholder="'用户名：admin'"
        >
          <template #prefix>
            <icon-user/>
          </template>
        </a-input>
      </a-form-item>
      <a-form-item
          field="password"
          :rules="[{ required: true, message: '密码不能为空' }]"
          :validate-trigger="['change', 'blur']"
          hide-label
      >
        <a-input-password
            v-model="userInfo.password"
            :placeholder="'密码：admin'"
            allow-clear
        >
          <template #prefix>
            <icon-lock/>
          </template>
        </a-input-password>
      </a-form-item>
      <a-form-item
          v-if="userInfo.captchaEnabled"
          field="code"
          :rules="[{ required: true, message: '验证码不能为空' }]"
          :validate-trigger="['change', 'blur']"
          hide-label
      >
        <a-input
            v-model="userInfo.code"
            :placeholder="'请输入验证码'"
            allow-clear
            style="width: 73%"
        >
          <template #prefix>
            <icon-code-block/>
          </template>
        </a-input>
        <div class="login-code">
          <img
              :src="userInfo.codeUrl"
              class="login-code-img"
              @click="fetchImgCode"
          />
        </div>
      </a-form-item>

      <a-space :size="16" direction="vertical">
        <div class="login-form-password-actions">
          <a-checkbox
              checked="rememberPassword"
              :model-value="loginConfig.rememberPassword"
              @change="setRememberPassword"
          >
            {{ '记住密码' }}
          </a-checkbox>
          <a-link>{{ '忘记密码' }}</a-link>
        </div>
        <a-button type="primary" html-type="submit" long :loading="loading">
          {{ '登录' }}
        </a-button>
        <a-button type="text" long class="login-form-register-btn">
          {{ '注册账号' }}
        </a-button>
      </a-space>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import {ref, reactive} from 'vue';
import {useRouter} from 'vue-router';
import {Message} from '@arco-design/web-vue';
import {ValidatedError} from '@arco-design/web-vue/es/form/interface';
import {useStorage} from '@vueuse/core';
import {useUserStore} from '@/store';
import useLoading from '@/hooks/loading';
import {LoginData, CodeImg, getCodeImg} from '@/api/login';

const router = useRouter();
const errorMessage = ref('');
const {loading, setLoading} = useLoading();
const userStore = useUserStore();

const loginConfig = useStorage('login-config', {
  rememberPassword: true,
  username: 'admin', // 演示默认值
  password: 'admin123', // demo default value
});
const userInfo = reactive({
  username: loginConfig.value.username,
  password: loginConfig.value.password,
  code: '',
  uuid: '',
  codeUrl: '',
  captchaEnabled: true,
});

const fetchImgCode = async () => {
  const res = await getCodeImg();
  const data = res as unknown as CodeImg;
  const base64Prefix = 'data:image/gif;base64,';
  userInfo.captchaEnabled =
      data.captchaEnabled === undefined ? true : data.captchaEnabled;
  if (userInfo.captchaEnabled) {
    userInfo.codeUrl = base64Prefix + data.img;
    userInfo.uuid = data.uuid;
  }
};

fetchImgCode();

const handleSubmit = async ({errors, values,}: {
  errors: Record<string, ValidatedError> | undefined;
  values: Record<string, any>;
}) => {
  if (loading.value) return;
  if (!errors) {
    setLoading(true);
    try {
      await userStore.login(values as LoginData);
      const {redirect, ...othersQuery} = router.currentRoute.value.query;
      router.push({
        name: (redirect as string) || 'Workplace',
        query: {
          ...othersQuery,
        },
      });
      Message.success('登录成功');
      const {rememberPassword} = loginConfig.value;
      const {username, password} = values;
      // 实际生产环境需要进行加密存储。
      // The actual production environment requires encrypted storage.
      loginConfig.value.username = rememberPassword ? username : '';
      loginConfig.value.password = rememberPassword ? password : '';
    } catch (err) {
      errorMessage.value = (err as Error).message;
    } finally {
      setLoading(false);
    }
  }
};
const setRememberPassword = (value: boolean) => {
  loginConfig.value.rememberPassword = value;
};
</script>

<style lang="less" scoped>
.login-form {
  &-wrapper {
    width: 320px;
  }

  &-title {
    color: var(--color-text-1);
    font-weight: 500;
    font-size: 24px;
    line-height: 32px;
  }

  &-sub-title {
    color: var(--color-text-3);
    font-size: 16px;
    line-height: 24px;
  }

  &-error-msg {
    height: 32px;
    color: rgb(var(--red-6));
    line-height: 32px;
  }

  &-password-actions {
    display: flex;
    justify-content: space-between;
  }

  &-register-btn {
    color: var(--color-text-3) !important;
  }

  .login-code {
    float: right;

    img {
      height: 30px;
      margin: 0 3px;
      cursor: pointer;
      vertical-align: middle;
    }
  }
}
</style>
