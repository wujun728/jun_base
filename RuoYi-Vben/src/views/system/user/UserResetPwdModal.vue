<template>
  <BasicModal
    v-bind="$attrs"
    :minHeight="100"
    title="修改密码"
    @register="registerInnerModal"
    @ok="handleSubmit"
    @cancel="resetForm"
  >
    <Description @register="registerDescription" />
    <BasicForm class="mt-5" @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';
  import { userResetPassword } from '@/api/system/user';
  import { resetPasswordSchemas } from './user.data';
  import { useDescription, Description } from '@/components/Description';

  defineOptions({ name: 'UserResetPwdModal' });

  const emit = defineEmits(['register', 'reload']);

  const [registerInnerModal, { modalLoading, closeModal }] = useModalInner(
    async (record: Recordable) => {
      const { userId } = record;
      setDescProps({ data: record });
      await setFieldsValue({ userId });
    },
  );

  const [registerDescription, { setDescProps }] = useDescription({
    column: 1,
    schema: [
      {
        label: '用户ID',
        field: 'userId',
      },
      {
        label: '用户名',
        field: 'userName',
      },
      {
        label: '昵称',
        field: 'nickName',
      },
    ],
  });

  const [registerForm, { setFieldsValue, resetForm, validate }] = useForm({
    labelWidth: 80,
    name: 'user-pwd-modal',
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
    schemas: resetPasswordSchemas,
  });

  async function handleSubmit() {
    try {
      modalLoading(true);
      const data = await validate();
      await userResetPassword(data as any);
      emit('reload');
      closeModal();
      await resetForm();
    } catch (e) {
      console.warn(e);
    } finally {
      modalLoading(false);
    }
  }
</script>

<style scoped></style>
