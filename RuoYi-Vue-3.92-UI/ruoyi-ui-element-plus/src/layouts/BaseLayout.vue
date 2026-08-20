<template>
  <div class="layout-root" :class="layoutClass">
    <!-- 移动端遮罩层（混布局 MixLayout 中传 false 关闭） -->
    <div
      v-if="showOverlay && isMobile && isSidebarOpen"
      class="layout-root__overlay"
      @click="closeSidebar"
    />

    <!-- 布局内容插槽 -->
    <slot />
  </div>
</template>

<script setup>
import { useLayout } from "./composables/useLayout";

defineProps({
  showOverlay: {
    type: Boolean,
    default: true,
  },
});

const { layoutClass, isSidebarOpen, isMobile, closeSidebar } = useLayout();
</script>

<style lang="scss" scoped>
.layout-root {
  width: 100%;
  height: 100%;

  &__overlay {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 999;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.3);
  }
}
</style>
