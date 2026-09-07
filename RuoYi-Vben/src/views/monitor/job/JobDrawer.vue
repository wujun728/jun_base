<template>
  <BasicDrawer
    v-bind="$attrs"
    :title="title"
    :width="drawerWidth"
    :show-footer="true"
    @register="registerDrawerInner"
    @ok="handleSubmit"
    @close="resetForm"
  >
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>

<script setup lang="ts">
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { BasicForm, useForm } from '@/components/Form';
  import { computed, ref, unref } from 'vue';
  import { jobAdd, jobUpdate, jobInfo } from '@/api/monitor/job';
  import { modalSchemas } from './job.data';
  import { useMaxWidthOrDefault } from '@/hooks/web/useSize';

  defineOptions({ name: 'ConfigModal' });

  const emit = defineEmits(['register', 'reload']);

  const isUpdate = ref<boolean>(false);
  const drawerWidth = useMaxWidthOrDefault(600);
  const title = computed<string>(() => {
    return isUpdate.value ? '编辑' : '新增';
  });

  const [registerDrawerInner, { drawerLoading, closeDrawer }] = useDrawerInner(
    async (data: { record?: Recordable; update: boolean }) => {
      drawerLoading(true);
      const { record, update } = data;
      isUpdate.value = update;
      if (update && record) {
        const ret = await jobInfo(record.jobId);
        await setFieldsValue(ret);
      }
      drawerLoading(false);
    },
  );

  const [registerForm, { setFieldsValue, resetForm, validate }] = useForm({
    baseColProps: {
      span: 24,
    },
    labelWidth: 150,
    layout: 'vertical',
    name: 'job_modal',
    showActionButtonGroup: false,
    schemas: modalSchemas,
  });

  async function handleSubmit() {
    try {
      drawerLoading(true);
      const data = await validate();
      if (unref(isUpdate)) {
        await jobUpdate(data);
      } else {
        await jobAdd(data);
      }
      emit('reload');
      closeDrawer();
      await resetForm();
    } catch (e) {
      console.log(e);
    } finally {
      drawerLoading(false);
    }
  }
</script>

<style scoped></style>
