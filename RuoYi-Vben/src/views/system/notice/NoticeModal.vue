<template>
  <BasicModal
    v-bind="$attrs"
    :title="title"
    :width="700"
    :can-fullscreen="true"
    @register="registerInnerModal"
    @ok="handleSubmit"
    @cancel="resetForm"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';
  import { computed, ref, unref } from 'vue';
  import { noticeUpdate, noticeAdd, noticeInfo } from '@/api/system/notice';
  import { modalSchemas } from './notice.data';

  defineOptions({ name: 'NoticeModal' });

  const emit = defineEmits(['register', 'reload']);

  const title = computed<string>(() => {
    return isUpdate.value ? '编辑公告' : '新增公告';
  });

  const isUpdate = ref<boolean>(false);
  const [registerInnerModal, { modalLoading, closeModal }] = useModalInner(
    async (data: { record?: Recordable; update: boolean }) => {
      modalLoading(true);
      const { record, update } = data;
      isUpdate.value = update;
      if (update && record) {
        const ret = await noticeInfo(record.noticeId);
        await setFieldsValue(ret);
      }
      modalLoading(false);
    },
  );

  const [registerForm, { setFieldsValue, validate, resetForm }] = useForm({
    layout: 'vertical',
    labelWidth: 80,
    name: 'notice_modal',
    showActionButtonGroup: false,
    schemas: modalSchemas,
  });

  async function handleSubmit() {
    try {
      modalLoading(true);
      const data = await validate();
      if (unref(isUpdate)) {
        await noticeUpdate(data);
      } else {
        await noticeAdd(data);
      }
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
