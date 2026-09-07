<template>
  <PageWrapper>
    <Row clsss="flex justify-center">
      <Col :xs="24" :sm="24" :md="24" :lg="8">
        <BasicForm @register="registerForm" class="mr-10" />
      </Col>
    </Row>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { PageWrapper } from '@/components/Page';
  import { BasicForm, useForm } from '@/components/Form';
  import { useUserStore } from '@/store/modules/user';
  import { userUpdatePassword } from '@/api/system/profile';
  import { useMessage } from '@/hooks/web/useMessage';
  import { Col, Row } from 'ant-design-vue';

  const userStore = useUserStore();

  const { createSuccessModal } = useMessage();
  const [registerForm, { validate }] = useForm({
    labelWidth: 100,
    rulesMessageJoinLabel: true,
    actionColOptions: { span: 24 },
    submitButtonOptions: {
      text: '确认修改密码',
    },
    baseColProps: { span: 24 },
    schemas: [
      {
        field: 'oldPassword',
        label: '旧密码',
        component: 'InputPassword',
        rules: [
          { required: true, message: '请输入旧密码, 长度在 5 到 20 个字符', min: 5, max: 20 },
        ],
      },
      {
        field: 'newPassword',
        label: '新密码',
        component: 'StrengthMeter',
        componentProps: {
          placeholder: '请输入新密码, 长度在 5 到 20 个字符',
        },
        rules: [
          { required: true, message: '请输入新密码, 长度在 5 到 20 个字符', min: 5, max: 20 },
        ],
      },
      {
        field: 'confirmPassword',
        label: '确认密码',
        component: 'InputPassword',
        dynamicRules: ({ values }) => {
          return [
            {
              required: true,
              validator: (_, value) => {
                if (!value) {
                  return Promise.reject('密码不能为空');
                }
                if (value !== values.newPassword) {
                  return Promise.reject('两次输入的密码不一致!');
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
    ],
    async submitFunc() {
      const data = await validate();
      useMessage().createConfirm({
        iconType: 'warning',
        title: '提示',
        content: '是否确认修改密码?',
        onOk: async () => {
          try {
            await userUpdatePassword(data);
            createSuccessModal({ title: '提示', content: '密码修改成功, 请重新登录!' });
            setTimeout(async () => {
              await userStore.logout(true);
            }, 1500);
          } catch (e) {
            console.warn(e);
          }
        },
      });
    },
  });
</script>
<style lang="less" scoped>
  .extra {
    margin-top: 10px;
    margin-right: 30px;
    float: right;
    color: #1890ff;
    font-weight: normal;
    cursor: pointer;
  }
</style>
