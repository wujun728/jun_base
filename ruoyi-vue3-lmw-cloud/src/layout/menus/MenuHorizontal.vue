<template>
  <div class="layouts-menu-horizontal">
    <div class="menu-horizontal-logo" v-if="config.layout.menuShowTopBar">
      <Logo />
    </div>
    <el-scrollbar
      ref="horizontalMenusRef"
      class="horizontal-menus-scrollbar"
      always
    >
      <el-menu
        class="menu-horizontal"
        popper-class="menu-horizontal-popper"
        mode="horizontal"
        :default-active="state.defaultActive"
        :key="state.menuKey"
      >
        <div v-for="(route, index) in sidebarRouters" :key="route.path + index">
          <MenuTree :item="route" :basePath="route.path" />
        </div>
      </el-menu>
    </el-scrollbar>
    <NavMenus />
  </div>
</template>

<script setup>
import Logo from '../components/Logo.vue'
import MenuTree from './MenuTree.vue'
import usePermissionStore from '@/store/modules/permission.js'

import { useRoute, onBeforeRouteUpdate } from 'vue-router'

import { useConfig } from '@/store/modules/layout'
// import { useNavTabs } from '/@/stores/navTabs'
import NavMenus from '../navBar/NavMenus.vue'
import { uuid } from '@/utils/random'

const horizontalMenusRef = ref()

const config = useConfig()
const route = useRoute()

const state = reactive({
  menuKey: uuid(),
  defaultActive: '',
})

const permissionStore = usePermissionStore()

const sidebarRouters = computed(() => permissionStore.sidebarRouters)
// 激活当前路由的菜单
const currentRouteActive = (currentRoute) => {
  state.defaultActive = currentRoute.path
}

// 滚动条滚动到激活菜单所在位置
const verticalMenusScroll = () => {
  nextTick(() => {
    let activeMenu = document.querySelector(
      '.el-menu.menu-horizontal li.is-active'
    )
    if (!activeMenu) return false
    horizontalMenusRef.value?.setScrollTop(activeMenu.offsetTop)
  })
}

onMounted(() => {
  currentRouteActive(route)
  verticalMenusScroll()
})

onBeforeRouteUpdate((to) => {
  currentRouteActive(to)
})
</script>

<style scoped lang="scss">
.layouts-menu-horizontal {
  display: flex;
  align-items: center;
  width: 100vw;
  height: 60px;
  background-color: var(--ba-bg-color-overlay);
  border-bottom: solid 1px var(--el-color-info-light-8);
}
.menu-horizontal-logo {
  width: 180px;
  &:hover {
    background-color: v-bind('config.getColorVal("headerBarHoverBackground")');
  }
}
.horizontal-menus-scrollbar {
  flex: 1;
  overflow-y: none;
  :deep(.is-vertical) {
    height: 0;
    display: none;
  }
}
.menu-horizontal {
  border: none;
  --el-menu-bg-color: v-bind('config.getColorVal("menuBackground")');
  --el-menu-text-color: v-bind('config.getColorVal("menuColor")');
  --el-menu-active-color: v-bind('config.getColorVal("menuActiveColor")');
  --el-menu-hover-bg-color: v-bind('config.getColorVal("menuHoverBackground")');
}

.el-sub-menu .icon,
.el-menu-item .icon {
  vertical-align: middle;
  margin-right: 5px;
  width: 24px;
  text-align: center;
  flex-shrink: 0;
}
.is-active .icon {
  color: var(--el-menu-active-color) !important;
}
.el-menu-item.is-active {
  background-color: v-bind('config.getColorVal("menuActiveBackground")');
}
</style>
