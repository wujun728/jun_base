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
  import { postAdd, postUpdate, postInfo } from '@/api/system/post';
  import { modalSchemas } from './post.data';
  import { useMaxWidthOrDefault } from '@/hooks/web/useSize';

  defineOptions({ name: 'PostDrawer' });

  const emit = defineEmits(['register', 'reload']);

  const drawerWidth = useMaxWidthOrDefault(600);
  const isUpdate = ref<boolean>(false);
  const title = computed<string>(() => {
    return isUpdate.value ? '编辑岗位' : '新增岗位';
  });

  const [registerDrawerInner, { drawerLoading, closeDrawer }] = useDrawerInner(openDrawerCallback);
  async function openDrawerCallback(data: { record?: Recordable; update: boolean }) {
    drawerLoading(true);
    const { record, update } = data;
    isUpdate.value = update;
    if (update && record) {
      const ret = await postInfo(record.postId);
      await setFieldsValue(ret);
    }

    drawerLoading(false);
  }

  const [registerForm, { setFieldsValue, resetForm, validate }] = useForm({
    labelWidth: 80,
    name: 'post_modal',
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
    schemas: modalSchemas,
  });

  async function handleSubmit() {
    try {
      drawerLoading(true);
      const data = await validate();
      if (unref(isUpdate)) {
        await postUpdate(data);
      } else {
        await postAdd(data);
      }
      emit('reload');
      closeDrawer();
      await resetForm();
    } catch (e) {
      console.warn(e);
    } finally {
      drawerLoading(false);
    }
  }
</script>

<style scoped></style>
