<template>
  <BasicDrawer
    v-bind="$attrs"
    title="查看日志详情"
    :width="drawerWidth"
    @register="registerInnerDrawer"
  >
    <Description @register="registerDescription" />
  </BasicDrawer>
</template>

<script setup lang="ts">
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { Description, useDescription } from '@/components/Description/index';
  import { OperateLog } from '@/api/monitor/operlog/model';
  import { descSchema } from './operlog.data';
  import { useMaxWidthOrDefault } from '@/hooks/web/useSize';

  defineOptions({ name: 'InfoDrawer' });

  const drawerWidth = useMaxWidthOrDefault(700);

  const [registerInnerDrawer] = useDrawerInner((data: OperateLog) => {
    setDescProps({ data });
  });

  const [registerDescription, { setDescProps }] = useDescription({
    column: 1,
    schema: descSchema,
  });
</script>

<style scoped></style>
