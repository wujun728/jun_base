<template>
  <BasicModal v-bind="$attrs" title="查看登录日志" @register="registerModal">
    <Description @register="registerDescription" />
    <template #footer> </template>
  </BasicModal>
</template>

<script setup lang="ts">
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { Description, useDescription } from '@/components/Description';
  import { modalSchemas } from './login.data';

  const [registerDescription, { setDescProps }] = useDescription({
    column: 1,
    schema: modalSchemas,
  });
  const [registerModal] = useModalInner((data: Recordable) => {
    setDescProps({ data });
  });
</script>

<style scoped></style>
