<template>
  <BaseContainer class="custom-container" v-if="formInfo.unique">
    <Verify @success="handleCaptchaVerifySuccess" :captchaType="captchaInfo.type"
      :imgSize="{ width: '330px', height: '155px' }" v-if="isVerifyVisible" defaultVisible @close="isVerifyVisible = false" />

    <view class="nav-bar-wrapper" :style="{ '--bg-color': formInfo.backgroundColor }">
      <DefaultNavbar :default-title="formInfo.title" color="#fff" background-color="transparent" :is-left="false" />
    </view>
    <template v-if="formInfo.designInfo.length">
      <view class="form-body">
        <ModuleForm :listData="formInfo.designInfo" :addressData="formInfo.addressConfig" :formInfo="formInfo._formInfo"
          @submitOk="submitOk" ref="formRef" />
      </view>
      <button class="submit-btn" @click="handleSubmit">提交</button>
    </template>
    <view class="success-mask" v-if="formInfo.submitSuccess">
      <image src="@/static/image/attendance/dk-success.png" class="success-img" mode="aspectFit" />
      <view class="success-text">您的问卷已经提交，感谢您的参与！</view>
    </view>
  </BaseContainer>
</template>

<script setup lang="ts">
import {
  crudModuleQuestionnaireDesignApi,
  getDictTreeListApi,
  crudModuleQuestionnaireSaveDataApi
} from "@/api/crud";
import BaseContainer from "@/components/BaseContainer/index.vue";
import DefaultNavbar from "@/components/defaultNavBar/index.vue";
import Verify from "@/components/VerifyCaptcha/index.vue";
import ModuleForm from "@/components/moduleForm/index.vue";
import message from "@/utils/message";
import { useCheckLogin } from "@/composables/useCheckLogin";

const isVerifyVisible = ref(false);
const formRef = ref();

const captchaInfo = reactive({
  type: "clickWord",
  verificationCode: null
});

const formInfo = reactive({
  title: "问卷调查",
  backgroundColor: null,
  _formInfo: {},

  unique: null,
  tableNameEn: null,
  designInfo: [] as QuestionnaireDesignInfo[],
  addressConfig: [],

  submitSuccess: false
});

const [isLogin] = useCheckLogin();

interface LoadOptions {
  unique?: string
}

interface VerifyCallbackEvent {
  captchaVerification: string
}

const handleCaptchaVerifySuccess = (e: VerifyCallbackEvent) => {
  isVerifyVisible.value = false;
  captchaInfo.verificationCode = e.captchaVerification;
  formRef.value.handleConfirm();
}

const getFormInfo = async () => {
  uni.showLoading({
    mask: true
  });

  try {
    const result = await crudModuleQuestionnaireDesignApi(formInfo.unique);
    uni.hideLoading();
    const column = result.data.column as QuestionnaireDesignInfo[];

    formInfo.designInfo = column;
    formInfo.tableNameEn = result.data.crud.table_name_en;
    formInfo.title = "新增" + result.data.crud.table_name;
    formInfo._formInfo = result.data.form_info;

    if (column.some(item => item.form_value === "cascader_address")) {
      const result = await getDictTreeListApi({
        type_id: 2
      });

      formInfo.addressConfig = result.data;
    }
  } catch (err) {
    uni.hideLoading();
    message.error(err.message, "none");
  }
}

const handleSubmit = () => {
  if (isLogin.value) {
    formRef.value.handleConfirm();
  } else {
    isVerifyVisible.value = true;
  }
}

const submitOk = async (data: any) => {
  uni.showLoading({
    title: "提交中...",
    mask: true
  });

  if (captchaInfo.verificationCode) {
    data.captchaVerification = captchaInfo.verificationCode;
    data.captchaType = captchaInfo.type;
  }

  try {
    const result = await crudModuleQuestionnaireSaveDataApi(formInfo.tableNameEn, formInfo.unique, data);
    uni.hideLoading();
    message.success(result.message, "none");
    formInfo.submitSuccess = true;
  } catch (error) {
    uni.hideLoading();
    message.error(error.message, "none");
  }
}

onLoad((options: LoadOptions) => {
  if (!options.unique) return;
  formInfo.unique = options.unique;
  getFormInfo();
});

</script>

<style scoped lang="scss">
.nav-bar-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  padding-top: var(--status-bar-height);
  background-image: linear-gradient(90deg, var(--bg-color, #459fff) 0%, var(--bg-color, #388aef) 100%, var(--bg-color, #3384e7) 100%);
  z-index: 1;

  :deep(.right-text) {
    color: #fff;
  }
}

.custom-container {
  padding-top: calc(var(--status-bar-height) + #{$uni-default-bar-height});
  padding-bottom: max(var(--bottom-area-height), 50rpx);
}

.success-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;

  background-color: #fff;
  padding-top: 10vh;
  z-index: 1;

  display: flex;
  flex-direction: column;
  align-items: center;
}

.success-img {
  width: 60vw;
}

.success-text {
  text-align: center;
}

.submit-btn {
  height: 86rpx;
  margin: 44rpx 20rpx 0;
  background-color: #1890ff;
  color: #fff;
}
</style>
