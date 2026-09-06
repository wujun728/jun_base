<script setup lang="ts">
import useConfig from '@/store/modules/layout.ts'
import MenuTree from './MenuTree.vue'
import usePermissionStore from '@/store/modules/permission.ts'
const permissionStore = usePermissionStore()
const sidebarRouters = computed(() => {
  return permissionStore.sidebarRouters
})
const config = useConfig()
const route = useRoute()

const activeMenu = computed(() => {
  const { meta, path } = route
  // if set path, the sidebar will highlight the path you set
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})

const verticalMenusScrollbarHeight = computed(() => {
  let menuTopBarHeight = 0
  if (config.layout.menuShowTopBar) {
    menuTopBarHeight = 50
  }
  if (config.layout.layoutMode == 'Default') {
    return 'calc(100vh - ' + (32 + menuTopBarHeight) + 'px)'
  } else {
    return 'calc(100vh - ' + menuTopBarHeight + 'px)'
  }
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
