<!--
 * 表单构建器 - 中间设计区域容器
 * 包裹 FormCanvas，提供白色画布背景和滚动支持。
 -->
<template>
  <div class="flex h-full min-w-[400px] flex-1 flex-col overflow-hidden">
    <div
      class="mx-auto h-full w-full flex-1 overflow-y-auto bg-white p-4 shadow-sm">
      <FormCanvas />
    </div>
  </div>
</template>

<script setup lang="ts">
import FormCanvas from './FormCanvas.vue'
</script>
