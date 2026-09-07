<template>
  <BasicDrawer
    v-bind="$attrs"
    title="如何使用代码生成"
    :width="drawerWidth"
    @register="registerDrawer"
  >
    <MarkdownViewer :value="value" />
    <template #footer> </template>
  </BasicDrawer>
</template>

<script setup lang="ts">
  import text from './howToEdit.md?raw';
  import { MarkdownViewer } from '@/components/Markdown';
  import { ref } from 'vue';
  import { BasicDrawer, useDrawer } from '@/components/Drawer';
  import { useMaxWidthOrDefault } from '@/hooks/web/useSize';

  const drawerWidth = useMaxWidthOrDefault(800);

  const [registerDrawer] = useDrawer();

  const value = ref<string>(text);
</script>

<style scoped></style>
