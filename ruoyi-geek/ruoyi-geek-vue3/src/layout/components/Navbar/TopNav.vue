/**
* TopNav组件
*
* 功能说明：
* 1. 顶部导航栏组件，支持显示一级菜单和更多菜单折叠
* 2. 支持两种菜单来源：
* - 仅显示后端动态路由（关闭TopNav导入本地路由时）
* - 混合显示本地路由和后端动态路由（开启TopNav导入本地路由时）
* 3. 特殊处理：
* - 空路径("")或根路径("/")：显示其第一个子路由为顶级菜单
* - isTopMenu: 将该路由的第一个子路由显示为顶级菜单
*
* 配置说明：
* 1. 在系统设置中可配置：
* - 开启/关闭TopNav
* - 开启/关闭TopNav导入本地路由
* 2. 路由配置中可使用meta.isTopMenu控制菜单行为
*/
<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { constantRoutes } from "@/router/routes/staticRoutes"
import { isHttp } from '@/utils/validate'
import { getNormalPath } from '@/utils/ruoyi'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import { RoutesAlias } from '@/router/routesAlias'
import { useEventListener } from "@vueuse/core"
import type { RouteItem } from '@/types/route'
import TopNavItem from './TopNavItem.vue'


// 隐藏侧边栏路由
const hideList = [RoutesAlias.Home, '/user/profile'];

const appStore = useAppStore()
const settingsStore = useSettingsStore()
const permissionStore = usePermissionStore()
const route = useRoute();
const router = useRouter();

// 主题颜色
const theme = computed(() => settingsStore.theme);
// 所有的路由信息
const routers = computed(() => permissionStore.topbarRouters);
const isMixMenu = computed(() => settingsStore.isMixMenu)
const isTopMenu = computed(() => settingsStore.isTopMenu)

function normalizeMenu(route: RouteItem, parentPath?: string): RouteItem {
  const currentPath = resolvePath(route.path, parentPath)
  const currentRoute = {
    ...route,
    path: currentPath,
    parentPath
  } as RouteItem & { parentPath?: string }

  if (!currentRoute.meta) {
    currentRoute.meta = {}
  }
  if (!currentRoute.meta.icon) {
    currentRoute.meta.icon = 'dashboard'
  }

  if (route.children?.length) {
    currentRoute.children = route.children
      .filter(item => item.hidden !== true)
      .map(child => normalizeMenu(child, currentPath))
  }

  return currentRoute
}

function resolvePath(path: string, parentPath?: string) {
  if (isHttp(path)) {
    return path
  }
  if (!parentPath || path.startsWith('/')) {
    return getNormalPath(path)
  }
  return getNormalPath(`${parentPath}/${path}`)
}

function getMenuChildren(route: RouteItem) {
  return (route.children || []).filter((item): item is RouteItem => item.hidden !== true)
}

// 顶部显示菜单
const topMenus = computed(() => {
  const menus: RouteItem[] = [];
  routers.value.forEach((menu) => {
    if (menu.hidden !== true) {
      if (menu.path === "" || menu.path === "/") {
        if (menu.children?.[0]) {
          const child = menu.children[0]
          const childPath = resolvePath(child.path, menu.path || '/')
          const normalizedChild = normalizeMenu({
            ...child,
            path: childPath,
            meta: {
              ...child.meta,
              icon: child.meta?.icon || 'dashboard'
            }
          })
          menus.push(normalizedChild)
        }
      } else if (menu.meta?.isTopMenu && menu.children?.[0]) {
        const firstChild = normalizeMenu({
          ...menu.children[0],
          path: menu.path,
          meta: {
            ...menu.children[0].meta,
            icon: menu.children[0].meta?.icon || menu.meta?.icon || 'dashboard'
          }
        })
        menus.push(firstChild)
      } else {
        menus.push(normalizeMenu(menu))
      }
    }
  })
  return menus;
})

// 设置子路由
const childrenMenus = computed(() => {
  const menuChildren: RouteItem[] = [];
  routers.value.forEach((router) => {
    const children = getMenuChildren(router)
    for (const item of children) {
      if (item.parentPath === undefined) {
        if (router.path === "/") {
          item.path = "/" + item.path;
        } else if (!isHttp(item.path)) {
          item.path = router.path + "/" + item.path;
        }
        item.parentPath = router.path;
      }
      menuChildren.push(item);
    }
  })
  return constantRoutes.concat(menuChildren);
})

function activeRoutes(key: string) {
  const routes: RouteItem[] = [];
  if (childrenMenus.value && childrenMenus.value.length > 0) {
    childrenMenus.value.forEach((item) => {
      if (key == item.parentPath || (key == "index" && "" == item.path)) {
        routes.push(item);
      }
    });
  }
  if (routes.length > 0) {
    permissionStore.setSidebarRouters(routes);
  } else {
    appStore.toggleSideBarHide(true);
  }
  return routes;
}

function findTopMenuByPath(path: string, menus: RouteItem[] = topMenus.value): RouteItem | undefined {
  for (const item of menus) {
    if (item.path === path) {
      return item
    }
    const found = findTopMenuByPath(path, getMenuChildren(item))
    if (found) {
      return found
    }
  }
  return undefined
}

// 默认激活的菜单
const activeMenu = computed(() => {
  const path = route.path;
  let activePath = path;
  if (isTopMenu.value) {
    const matchedTopMenu = topMenus.value.find(item => path === item.path || path.startsWith(`${item.path}/`))
    if (matchedTopMenu) {
      return path === matchedTopMenu.path ? matchedTopMenu.path : path
    }
    return path
  }
  if (path !== undefined && path.lastIndexOf("/") > 0 && hideList.indexOf(path) === -1) {
    const tmpPath = path.substring(1, path.length);
    activePath = "/" + tmpPath.substring(0, tmpPath.indexOf("/"));
    if (!route.meta.link) {
      appStore.toggleSideBarHide(false);
    }
  } else {
    activePath = path;
    appStore.toggleSideBarHide(true);
  }
  activeRoutes(activePath);
  return activePath;
})

// 当前激活菜单的 index
const currentIndex = ref<string | null>(null);
function handleSelect(key: string) {
  currentIndex.value = key;
  const topRoute = findTopMenuByPath(key) || routers.value.find(item => item.path === key);
  if (isHttp(key)) {
    window.open(key, "_blank");
    return
  }

  if (isTopMenu.value) {
    router.push({ path: key });
    appStore.toggleSideBarHide(true);
    return
  }

  if (!topRoute || !topRoute.children?.length) {
    router.push({ path: key });
    appStore.toggleSideBarHide(true);
    return
  }

  activeRoutes(key);
  appStore.toggleSideBarHide(false);
}

/** 顶部栏初始数 */
const visibleNumber = ref(0);
const setVisibleNumber = () => {
  const width = document.body.getBoundingClientRect().width / 3;
  visibleNumber.value = Math.floor(width / 85);
}
useEventListener("resize", setVisibleNumber);
onMounted(() => { setVisibleNumber() })
</script>
<template>
  <el-menu :default-active="activeMenu" mode="horizontal" @select="handleSelect" :ellipsis="false"
    class="topmenu-container">
    <template v-for="(item, index) in topMenus" :key="item.path">
      <el-menu-item v-if="index < visibleNumber && isMixMenu" :style="{ '--theme': theme }" :index="item.path">
        <svg-icon :icon-class="item.meta?.icon || 'dashboard'" />
        <span>{{ item.meta?.title }}</span>
      </el-menu-item>

      <top-nav-item v-else-if="index < visibleNumber" :item="item" :resolve-path="resolvePath"
        :get-children="getMenuChildren" :style="{ '--theme': theme }" />
    </template>

    <!-- 顶部菜单超出数量折叠 -->
    <el-sub-menu :style="{ '--theme': theme }" index="more" v-if="topMenus.length > visibleNumber">
      <template #title>更多菜单</template>
      <template v-for="(item, index) in topMenus" :key="`${item.path}-more`">
        <el-menu-item v-if="index >= visibleNumber && isMixMenu" :index="item.path">
          <svg-icon :icon-class="item.meta?.icon || 'dashboard'" />
          <span>{{ item.meta?.title }}</span>
        </el-menu-item>
        <top-nav-item v-else-if="index >= visibleNumber" :item="item" :resolve-path="resolvePath"
          :get-children="getMenuChildren" />
      </template>
    </el-sub-menu>
  </el-menu>
</template>

<style lang="scss" scoped>
@use "@/assets/styles/variables.module.scss";

.topmenu-container {
  --el-menu-bg-color: variables.$navbar-color;

  &.el-menu--horizontal>:deep(.el-menu-item) {
    float: left;
    height: variables.$navbar-height;
    line-height: variables.$navbar-height;
    color: #999093;
    padding: 0 5px;
    margin: 0 10px;
    background-color: transparent;


    &:hover {
      color: var(--el-menu-active-color);
    }
  }

  &.el-menu--horizontal>:deep(.el-menu-item.is-active),
  &.el-menu--horizontal>.el-sub-menu.is-active :deep(.el-sub-menu__title) {
    border-bottom: 2px solid transparent;
    position: relative;

    &::after {
      content: '';
      position: absolute;
      bottom: 6px;
      left: 25%;
      width: 50%;
      height: 2px;
      background-color: var(--el-menu-active-color);
    }

    &>.el-sub-menu__icon-arrow {
      position: absolute;
      right: -8px;
    }
  }

  &.el-menu--horizontal>.el-sub-menu :deep(.el-sub-menu__title) {
    float: left;
    height: variables.$navbar-height;
    line-height: variables.$navbar-height;
    color: #999093;
    padding: 0 5px;
    margin: 0 10px;
  }

}
</style>
