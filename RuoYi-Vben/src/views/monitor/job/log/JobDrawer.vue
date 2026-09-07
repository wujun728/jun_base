<template>
  <BasicDrawer
    v-bind="$attrs"
    :title="title"
    :width="drawerWidth"
    :show-footer="false"
    @register="registerDrawerInner"
  >
    <Description @register="registerDescription" />
  </BasicDrawer>
</template>

<script setup lang="ts">
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { computed, ref } from 'vue';
  import { jobLogInfo } from '@/api/monitor/job/log';
  import { descSchemas } from './job.data';
  import { useMaxWidthOrDefault } from '@/hooks/web/useSize';
  import { useDescription, Description } from '@/components/Description';

  defineOptions({ name: 'JobLogDrawer' });

  const isUpdate = ref<boolean>(false);
  const drawerWidth = useMaxWidthOrDefault(600);
  const title = computed<string>(() => {
    return isUpdate.value ? '编辑' : '新增';
  });

  const [registerDrawerInner, { drawerLoading }] = useDrawerInner(async (jobLogId: string) => {
    drawerLoading(true);

    const data = await jobLogInfo(jobLogId);
    setDescProps({ data });

    drawerLoading(false);
  });

  const [registerDescription, { setDescProps }] = useDescription({
    column: 1,
    schema: descSchemas,
  });
</script>

<style scoped></style>
