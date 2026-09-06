<template>
  <div class="page-container">
    <slot name="search"></slot>
    <a-card :style="{ marginTop: hasSearchSlot ? '12px' : '0' }" class="page-card">
      <div v-if="hasActionsSlot">
        <slot name="actions"></slot>
      </div>
      <slot></slot>
    </a-card>
  </div>
</template>

<script setup>
import {computed, useSlots} from 'vue';

const slots = useSlots();
const hasSearchSlot = computed(() => !!slots.search);
const hasActionsSlot = computed(() => !!slots.actions);
</script>

<style scoped>
.page-container {
  width: 100%;
  max-width: 100%;
  min-height: 100%;
  overflow-x: hidden;
  overflow-y: visible;
  box-sizing: border-box;
  background: var(--bg-color);
  padding: 0 16px 0 0;
  margin: 0;
}

.page-card {
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  background: var(--component-background);
  margin: 0 0 12px 0; /* 减小底部间距 */
  overflow: hidden; /* 确保卡片内容不会溢出 */
}

/* 确保卡片内容区域宽度受限，支持内部横向滚动 */
.page-card :deep(.ant-card-body) {
  max-width: 100%;
  overflow-x: hidden; /* 内部表格会处理自己的滚动，卡片本身不应滚动 */
}

/* 确保卡片内容区域正确处理溢出 */
:deep(.ant-card-body) {
  padding: 12px;
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  background: var(--component-background);
  color: var(--text-primary);
  overflow-x: hidden; /* 允许横向滚动 */
  overflow-y: visible; /* 垂直方向保持可见 */
}

/* 表格容器滚动条 - 8px宽度，与页面滚动条保持一致 */
:deep(.ant-table-wrapper) {
  overflow-x: auto !important; /* 强制允许横向滚动 */
  scrollbar-gutter: stable; /* 保持布局稳定性 */
}

:deep(.ant-table-content) {
  overflow-x: auto !important;
}

:deep(.ant-table-wrapper)::-webkit-scrollbar {
  height: 8px;
  width: 8px;
  display: block !important;
}

:deep(.ant-table-wrapper)::-webkit-scrollbar-thumb {
  background: rgba(148, 163, 184, 0.45) !important;
  transition: background 0.3s ease;
}

:deep(.ant-table-wrapper)::-webkit-scrollbar-thumb:hover {
  background: rgba(100, 116, 139, 0.58) !important;
}

:deep(.ant-table-wrapper)::-webkit-scrollbar-track {
  background: transparent;
}

:deep(.ant-table-wrapper)::-webkit-scrollbar-corner {
  background: transparent;
}

/* 暗黑模式滚动条适配 */
:global(.dark-theme) :deep(.ant-table-wrapper)::-webkit-scrollbar-thumb {
  background: rgba(148, 163, 184, 0.45);
}

:global(.dark-theme) :deep(.ant-table-wrapper)::-webkit-scrollbar-thumb:hover {
  background: rgba(100, 116, 139, 0.58);
}

:global(.dark-theme) :deep(.ant-table-wrapper)::-webkit-scrollbar-track {
  background: transparent;
}
</style>
