<template>
  <div>
    <a-modal v-model:open="pageData.visible" width="40%" title="重置密码" :destroyOnClose="true" @ok="clickResetPasswordModalOk" @cancel="clickResetPasswordModalCancel">
      <a-form ref="resetPasswordFormRef" name="resetPasswordForm" :model="resetPasswordForm.data" :labelCol="{ span: 8, offset: 0 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
          <a-col span="24">
            <a-form-item name="userName" label="用户账号">
              <span>{{ resetPasswordForm.data.userName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="24">
            <a-form-item name="nickName" label="用户名称">
              <span>{{ resetPasswordForm.data.nickName }}</span>
            </a-form-item>
          </a-col>

          <a-col :span="24">
            <a-form-item name="password" label="新密码" :rules="[{ required: true, message: '密码不能为空！' }]">
              <a-input v-model:value="resetPasswordForm.data.password" :maxlength="30" type="password" placeholder="请填写新密码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item name="password2" label="确认新密码" :rules="[{ required: true, message: '确认密码不能为空！' }]">
              <a-input v-model:value="resetPasswordForm.data.password2" :maxlength="30" type="password" placeholder="请确认新密码"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';
import { resetUserPwd } from '@/api/system/user';
import { message } from 'ant-design-vue';

// 当前页面使用的数据
const pageData = reactive({
  visible: false,
});

//重置密码Form
const resetPasswordFormRef = ref();
const resetPasswordForm = reactive({ data: {} });

// 打开重置密码窗口
const openModal = (record) => {
  pageData.visible = true;
  resetPasswordForm.data.userId = record.userId;
  resetPasswordForm.data.userName = record.userName;
  resetPasswordForm.data.nickName = record.nickName;
};

// 点击窗口确认
const clickResetPasswordModalOk = (e) => {
  resetPasswordFormRef.value
    .validateFields()
    .then((values) => {
      if (resetPasswordForm.data.password === resetPasswordForm.data.password2) {
        resetUserPwd(resetPasswordForm.data.userId, resetPasswordForm.data.password).then((response) => {
          BearJiaUtil.messageSuccess('用户账号[' + resetPasswordForm.data.userName + ']重置密码成功。');
          pageData.visible = false;
          BearJiaUtil.resetFormFieldsToEmpty(resetPasswordForm.data);
        });
      } else {
       BearJiaUtil.ms('两次输入的密码不一致，请重新输入！');
      }
    })
    .catch((info) => {
      console.log('Validate Failed:', info);
    });
};

// 点击窗口取消
const clickResetPasswordModalCancel = () => {
  BearJiaUtil.resetFormFieldsToEmpty(resetPasswordForm.data);
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less"></style>
