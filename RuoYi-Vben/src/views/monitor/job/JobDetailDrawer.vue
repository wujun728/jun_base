<template>
  <BasicDrawer
    v-bind="$attrs"
    title="任务信息"
    :width="drawerWidth"
    :show-footer="false"
    @register="registerDrawerInner"
  >
    <Description @register="registerDescription" />
  </BasicDrawer>
</template>

<script setup lang="ts">
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { jobInfo } from '@/api/monitor/job';
  import { descSchemas } from './job.data';
  import { useMaxWidthOrDefault } from '@/hooks/web/useSize';
  import { useDescription, Description } from '@/components/Description';

  defineOptions({ name: 'JobLogDrawer' });

  const drawerWidth = useMaxWidthOrDefault(600);

  const [registerDrawerInner, { drawerLoading }] = useDrawerInner(async (jobId: string) => {
    drawerLoading(true);

    const data = await jobInfo(jobId);
    setDescProps({ data });

    drawerLoading(false);
  });

  const [registerDescription, { setDescProps }] = useDescription({
    column: 1,
    schema: descSchemas,
  });
</script>

<style scoped></style>
