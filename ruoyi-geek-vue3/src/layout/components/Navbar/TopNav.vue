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
<script setup>
import { constantRoutes } from "@/router/routes/staticRoutes"
import { isHttp } from '@/utils/validate'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import { RoutesAlias } from '@/router/routesAlias'

// 顶部栏初始数
const visibleNumber = ref(null);
// 当前激活菜单的 index
const currentIndex = ref(null);
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

// 顶部显示菜单
const topMenus = computed(() => {
  let topMenus = [];
  routers.value.map((menu) => {
    if (menu.hidden !== true) {
      // 兼容顶部栏一级菜单内部跳转
      if (menu.path === "" || menu.path === "/") {
        if (menu.children && menu.children[0]) {
          const child = menu.children[0];
          // 确保子路由有正确的path
          child.path = child.path.replace('//', '/');
          if (!child.meta) {
            child.meta = {};
          }
          if (!child.meta.icon) {
            child.meta.icon = 'dashboard';
          }
          topMenus.push(child);
        }
      } else if (menu.meta?.isTopMenu && menu.children?.[0]) {
        // 使用isTopMenu标处理需要显示子路由为顶级菜单的情况
        const firstChild = menu.children[0];
        firstChild.path = menu.path;
        topMenus.push(firstChild);
      } else {
        // 确保menu有meta对象
        if (!menu.meta) {
          menu.meta = {};
        }
        // 确保menu有icon属性
        if (!menu.meta.icon) {
          menu.meta.icon = 'dashboard';
        }
        topMenus.push(menu);
      }
    }
  })
  return topMenus;
})


// 设置子路由
const childrenMenus = computed(() => {
  let childrenMenus = [];
  routers.value.map((router) => {
    for (let item in router.children) {
      if (router.children[item].parentPath === undefined) {
        if (router.path === "/") {
          router.children[item].path = "/" + router.children[item].path;
        } else {
          if (!isHttp(router.children[item].path)) {
            router.children[item].path = router.path + "/" + router.children[item].path;
          }
        }
        router.children[item].parentPath = router.path;
      }
      childrenMenus.push(router.children[item]);
    }
  })
  return constantRoutes.concat(childrenMenus);
})

// 默认激活的菜单
const activeMenu = computed(() => {
  const path = route.path;
  let activePath = path;
  if (path !== undefined && path.lastIndexOf("/") > 0 && hideList.indexOf(path) === -1) {
    const tmpPath = path.substring(1, path.length);
    activePath = "/" + tmpPath.substring(0, tmpPath.indexOf("/"));
    if (!route.meta.link) {
      appStore.toggleSideBarHide(false);
    }
  } else if (!route.children) {
    activePath = path;
    appStore.toggleSideBarHide(true);
  }
  activeRoutes(activePath);
  return activePath;
})

function setVisibleNumber() {
  const width = document.body.getBoundingClientRect().width / 3;
  visibleNumber.value = parseInt(width / 85);
}

function handleSelect(key, keyPath) {
  currentIndex.value = key;
  const route = routers.value.find(item => item.path === key);
  if (isHttp(key)) {
    // http(s):// 路径新窗口打开
    window.open(key, "_blank");
  } else if (!route || !route.children) {
    // 没有子路由路径内部打开
    router.push({ path: key });
    appStore.toggleSideBarHide(true);
  } else {
    // 显示左侧联动菜单
    activeRoutes(key);
    appStore.toggleSideBarHide(false);
  }
}

function activeRoutes(key) {
  let routes = [];
  if (childrenMenus.value && childrenMenus.value.length > 0) {
    childrenMenus.value.map((item) => {
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

onMounted(() => {
  window.addEventListener('resize', setVisibleNumber)
})
onBeforeUnmount(() => {
  window.removeEventListener('resize', setVisibleNumber)
})

onMounted(() => {
  setVisibleNumber()
})
</script>
<template>
  <el-menu :default-active="activeMenu" mode="horizontal" @select="handleSelect" :ellipsis="false"
    class="topmenu-container">
    <template v-for="(item, index) in topMenus">
      <el-menu-item :style="{ '--theme': theme }" :index="item.path" :key="index" v-if="index < visibleNumber">
        <svg-icon :icon-class="item.meta.icon" />
        <span>{{ item.meta.title }}</span>
      </el-menu-item>
    </template>

    <!-- 顶部菜单超出数量折叠 -->
    <el-sub-menu :style="{ '--theme': theme }" index="more" v-if="topMenus.length > visibleNumber">
      <template #title>更多菜单</template>
      <template v-for="(item, index) in topMenus">
        <el-menu-item :index="item.path" :key="index" v-if="index >= visibleNumber">
          <svg-icon :icon-class="item.meta.icon" />
          <span>{{ item.meta.title }}</span>
        </el-menu-item>
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
