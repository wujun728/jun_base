<template>
  <LoginFormTitle v-show="getShow" class="enter-x" />
  <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef" v-show="getShow">
    <FormItem name="account" class="enter-x">
      <Input size="large" v-model:value="formData.account" :placeholder="t('sys.login.userName')" />
    </FormItem>
    <FormItem name="password" class="enter-x">
      <InputPassword
        size="large"
        visibilityToggle
        v-model:value="formData.password"
        :placeholder="t('sys.login.password')"
        @keypress.enter="handleLogin"
      />
    </FormItem>
    <FormItem name="code" class="enter-x" v-if="image.requiredCaptcha">
      <Input
        ref="imageCodeRef"
        size="large"
        v-model:value="formData.code"
        placeholder="输入验证码"
        @keypress.enter="handleLogin"
      >
        <template #addonAfter>
          <Image
            class="rounded-r-8px"
            :preview="false"
            :height="40"
            :width="105"
            :src="image.imageInfo"
            @click="refreshCaptchaImage"
          />
        </template>
      </Input>
    </FormItem>
    <ARow class="enter-x">
      <ACol :span="12">
        <FormItem>
          <!-- No logic, you need to deal with it yourself -->
          <Checkbox v-model:checked="rememberMe" size="small">
            {{ t('sys.login.rememberMe') }}
          </Checkbox>
        </FormItem>
      </ACol>
      <ACol :span="12">
        <FormItem :style="{ 'text-align': 'right' }">
          <!-- No logic, you need to deal with it yourself -->
          <Button
            :disabled="true"
            type="link"
            size="small"
            @click="setLoginState(LoginStateEnum.RESET_PASSWORD)"
          >
            {{ t('sys.login.forgetPassword') }}
          </Button>
        </FormItem>
      </ACol>
    </ARow>

    <FormItem class="enter-x">
      <a-button
        type="primary"
        size="large"
        block
        :disabled="!submitBtnEnable"
        @click="handleLogin"
        :loading="loading"
      >
        {{ t('sys.login.loginButton') }}
      </a-button>
      <!-- <Button size="large" class="mt-4 enter-x" block @click="handleRegister">
        {{ t('sys.login.registerButton') }}
      </Button> -->
    </FormItem>
    <!-- <ARow class="enter-x" :gutter="[16, 16]">
      <ACol :md="8" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.MOBILE)" disabled>
          {{ t('sys.login.mobileSignInFormTitle') }}
        </Button>
      </ACol>
      <ACol :md="8" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.QR_CODE)" disabled>
          {{ t('sys.login.qrSignInFormTitle') }}
        </Button>
      </ACol>
      <ACol :md="8" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.REGISTER)" disabled>
          {{ t('sys.login.registerButton') }}
        </Button>
      </ACol>
    </ARow> -->

    <!-- <Divider class="enter-x">{{ t('sys.login.otherSignIn') }}</Divider> -->

    <!-- <div class="flex justify-evenly enter-x" :class="`${prefixCls}-sign-in-way`">
      <GithubFilled />
      <WechatFilled />
      <AlipayCircleFilled />
      <GoogleCircleFilled />
      <TwitterCircleFilled />
    </div> -->
  </Form>
</template>
<script lang="ts" setup>
  import { reactive, ref, unref, computed, onMounted } from 'vue';

  import { Checkbox, Form, Input, Row, Col, Button, Image } from 'ant-design-vue';
  // import {
  //   GithubFilled,
  //   WechatFilled,
  //   AlipayCircleFilled,
  //   GoogleCircleFilled,
  //   TwitterCircleFilled,
  // } from '@ant-design/icons-vue';
  import LoginFormTitle from './LoginFormTitle.vue';

  import { useI18n } from '@/hooks/web/useI18n';
  import { useMessage } from '@/hooks/web/useMessage';

  import { useUserStore } from '@/store/modules/user';
  import { LoginStateEnum, useLoginState, useFormRules, useFormValid } from './useLogin';
  import { useDesign } from '@/hooks/web/useDesign';

  import { captchaImage } from '@/api/auth/captcha';

  import { createLocalStorage } from '@/utils/cache';

  const ACol = Col;
  const ARow = Row;
  const FormItem = Form.Item;
  const InputPassword = Input.Password;
  const { t } = useI18n();
  const { notification, createErrorModal } = useMessage();
  const { prefixCls } = useDesign('login');
  const userStore = useUserStore();

  const { setLoginState, getLoginState } = useLoginState();
  const { getFormRules } = useFormRules();

  const formRef = ref();
  const loading = ref(false);

  // 默认租户ID
  const defaultTenantId = '000000';
  const formData: any = reactive({
    account: 'admin',
    password: 'admin123',
    // account: '',
    // password: '',
    code: '',
    uuid: '',
  });

  // 验证码信息
  const image = reactive({
    requiredCaptcha: false,
    imageInfo: '',
    loadSuccess: false,
  });

  async function refreshCaptchaImage() {
    try {
      const ret = await captchaImage();
      // 验证码UUID
      formData.uuid = ret.uuid;

      image.requiredCaptcha = ret.captchaEnabled;
      image.imageInfo = `data:image/gif;base64,${ret.img}`;
      // 清空输入框
      formData.code = '';
      // 获取焦点
      unref(imageCodeRef)?.focus();
      // 设置为image加载成功
      image.loadSuccess = true;
    } catch (e) {
      console.error('验证码异常');
      image.loadSuccess = false;
    }
  }

  // 验证码加载ok 登录按钮才可用
  const submitBtnEnable = computed(() => image.loadSuccess);

  // prod环境默认加密 RUOYI_PLUS__PRODUCTION__版本__REMEMBERME
  const storage = createLocalStorage();
  const cacheKey = 'rememberMe';
  const rememberMe = ref<boolean>(!!storage.get(cacheKey));

  function handleRememberMe(data: Recordable) {
    const { tenantId = defaultTenantId, username, password } = data;
    const rememberMeData = { tenantId, username, password };
    if (rememberMe.value) {
      storage.set(cacheKey, rememberMeData);
    } else {
      storage.remove(cacheKey);
    }
  }

  function setRememberMeData() {
    const data = storage.get(cacheKey);
    if (data) {
      console.log(data);
      const { tenantId = defaultTenantId, username = '', password = '' } = data;
      formData.tenantId = tenantId;
      formData.account = username;
      formData.password = password;
    }
  }

  onMounted(async () => {
    // 验证码
    await refreshCaptchaImage();
    // 设置记住我数据
    setRememberMeData();
  });

  const { validForm } = useFormValid(formRef);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);

  const imageCodeRef = ref<HTMLInputElement>();

  async function handleLogin() {
    // 由于keypress.enter  这里需要再次判断
    if (!submitBtnEnable.value) return;
    // 验证表单
    const data = await validForm();
    if (!data) return;
    try {
      loading.value = true;
      const requestParam: any = {
        username: data.account,
        password: data.password,
      };
      // 验证码启用需要添加参数
      if (image.requiredCaptcha) {
        requestParam.uuid = formData.uuid;
        requestParam.code = data.code;
      }
      const userInfo = await userStore.login({
        ...requestParam,
        mode: 'none', //不要默认的错误提示
      });
      if (userInfo) {
        // 记住我
        handleRememberMe(requestParam);
        notification.success({
          message: t('sys.login.loginSuccessTitle'),
          description: `${t('sys.login.loginSuccessDesc')}: ${userInfo.nickName}`,
          duration: 3,
        });
      }
    } catch (error) {
      await refreshCaptchaImage();
      const content = (error as unknown as Error).message || t('sys.api.networkExceptionMsg');
      createErrorModal({
        title: t('sys.api.errorTip'),
        content,
        getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
        onOk() {
          // 后端验证码失效/错误并没有返回唯一code 只能这样判断
          if (
            image.requiredCaptcha &&
            (content.includes('验证码') || content.includes('Captcha'))
          ) {
            formData.code = '';
            unref(imageCodeRef)?.focus();
          }
        },
      });
    } finally {
      loading.value = false;
    }
  }
</script>

<style lang="less" scoped>
  /** 表单左右宽度 */
  :deep(.ant-input-group-addon) {
    padding: 0;
    border: none;
  }
</style>
