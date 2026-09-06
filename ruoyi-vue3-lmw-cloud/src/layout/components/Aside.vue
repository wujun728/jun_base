<script setup>
import { useConfig } from '@/store/modules/layout.js'
import Logo from './Logo.vue'
import MenuVertical from '../menus/MenuVertical.vue'

const config = useConfig()

const menuWidth = computed(() => config.menuWidth())

defineOptions({
  name: 'layout/aside',
})
</script>
<template>
  <el-aside
    :class="
      'layout-aside-' +
      config.layout.layoutMode +
      ' ' +
      (config.layout.shrink ? 'shrink' : '')
    "
  >
    <Logo v-if="config.layout.menuShowTopBar"></Logo>
    <MenuVertical />
  </el-aside>
</template>

<style scoped lang="scss">
.layout-aside-Default {
  background: var(--ba-bg-color-overlay);
  margin: 16px 0 16px 16px;
  height: calc(100vh - 32px);
  box-shadow: var(--el-box-shadow-light);
  border-radius: var(--el-border-radius-base);
  overflow: hidden;
  transition: width 0.3s ease;
  width: v-bind(menuWidth);
}
.layout-aside-Classic,
.layout-aside-Double {
  background: var(--ba-bg-color-overlay);
  margin: 0;
  height: 100vh;
  overflow: hidden;
  transition: width 0.3s ease;
  width: v-bind(menuWidth);
}
.shrink {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999999;
}
</style>
