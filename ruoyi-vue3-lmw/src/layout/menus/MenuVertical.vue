<script setup>
import { useConfig } from '@/store/modules/layout.js'
import MenuTree from './MenuTree.vue'
import usePermissionStore from '@/store/modules/permission.js'
const permissionStore = usePermissionStore()
const sidebarRouters = computed(() => {
  return permissionStore.sidebarRouters
})
const config = useConfig()
const route = useRoute()

const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})

const verticalMenusScrollbarHeight = ref('100%')
const resize = () => {
  let menuTopBarHeight = 0
  const windowHeight = window.innerHeight
  if (config.layout.menuShowTopBar) {
    menuTopBarHeight = 50
  }
  if (config.layout.layoutMode == 'Default') {
    verticalMenusScrollbarHeight.value =
      windowHeight - (32 + menuTopBarHeight) + 'px'
  } else {
    verticalMenusScrollbarHeight.value = windowHeight - menuTopBarHeight + 'px'
  }
}
onMounted(() => {
  resize()
  window.addEventListener('resize', resize)
})
onUnmounted(() => {
  window.removeEventListener('resize', resize)
})
</script>
<template>
  <el-scrollbar ref="verticalMenusRef" class="vertical-menus-scrollbar">
    <el-menu
      class="layouts-menu-vertical"
      :collapse-transition="false"
      :unique-opened="config.layout.menuUniqueOpened"
      :default-active="activeMenu"
      :collapse="config.layout.menuCollapse"
      mode="vertical"
    >
      <MenuTree
        v-for="(route, index) in sidebarRouters"
        :item="route"
        :basePath="route.path"
        :key="route.path + index"
      />
    </el-menu>
  </el-scrollbar>
</template>

<style scoped lang="scss">
.vertical-menus-scrollbar {
  height: v-bind(verticalMenusScrollbarHeight);
  background-color: v-bind('config.getColorVal("menuBackground")');
  :deep(.el-menu--collapse .el-sub-menu__icon-arrow) {
    display: none;
  }
  :deep(.el-menu--collapse .menu-title) {
    margin-left: 0px;
  }
  :deep(.el-menu--collapse .el-tooltip__trigger) {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  :deep(.menu-title) {
    margin-left: 5px;
  }
}
.layouts-menu-vertical {
  border: 0;
  padding-bottom: 30px;
  --el-menu-bg-color: v-bind('config.getColorVal("menuBackground")');
  --el-menu-text-color: v-bind('config.getColorVal("menuColor")');
  --el-menu-active-color: v-bind('config.getColorVal("menuActiveColor")');
  --el-menu-hover-bg-color: v-bind('config.getColorVal("menuHoverBackground")');
}
</style>
