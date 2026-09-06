<template>
  <div :class="layoutClasses" :style="cssVars" class="app-container">
    <!-- 侧边栏布局（默认） -->
    <a-layout v-if="!layoutSettings.navMode || layoutSettings.navMode === 'side'" class="layout-container layout-side">
      <SideMenu ref="sideMenuRef" v-model:collapsed="collapsed" :layout-settings="layoutSettings" :menu-data="sidebarRouters" @menu-select="handleMenuSelect" />
      <a-layout style="background: var(--page-background-gradient); flex: 1; min-width: 0">
        <HeaderBar
          v-model:collapsed="collapsed"
          :current-father-menu-title="headerInfo.currentFatherMenuTitle"
          :current-menu-title="headerInfo.currentMenuTitle"
          :layout-settings="layoutSettings"
          :loading="loading"
          @logout="handleLogout"
          @show-settings="showDrawer"
          @personal-center="enterPersonalCenter"
          @refresh-page="refreshCurrentPage"
        />
        <HistoryNav v-if="layoutSettings.multiTab" class="layout-container__history" />
        <a-layout-content class="layout-container__content">
          <div class="content-wrapper" :style="{ height: layoutSettings.multiTab ? 'calc(100vh - 96px)' : 'calc(100vh - 64px)' }">
            <a-config-provider :locale="zhCN">
              <router-view></router-view>
            </a-config-provider>
          </div>
        </a-layout-content>
      </a-layout>
    </a-layout>

    <!-- 顶部菜单布局 -->
    <a-layout v-else-if="layoutSettings.navMode === 'top'" class="layout-container layout-top">
      <HeaderBar
        ref="topMenuRef"
        v-model:collapsed="collapsed"
        :current-father-menu-title="headerInfo.currentFatherMenuTitle"
        :current-menu-title="headerInfo.currentMenuTitle"
        :layout-settings="layoutSettings"
        :loading="loading"
        :menu-data="sidebarRouters"
        @logout="handleLogout"
        @show-settings="showDrawer"
        @personal-center="enterPersonalCenter"
        @refresh-page="refreshCurrentPage"
        @menu-select="handleMenuSelect"
      />
      <HistoryNav v-if="layoutSettings.multiTab" class="layout-container__history" />
      <a-layout-content class="layout-container__content">
        <div class="content-wrapper" :style="{ height: layoutSettings.multiTab ? 'calc(100vh - 96px)' : 'calc(100vh - 64px)' }">
          <a-config-provider :locale="zhCN">
            <router-view></router-view>
          </a-config-provider>
        </div>
      </a-layout-content>
    </a-layout>

    <!-- 混合布局 -->
    <a-layout v-else-if="layoutSettings.navMode === 'mix'" class="layout-container layout-mix">
      <HeaderBar
        ref="mixTopMenuRef"
        v-model:collapsed="collapsed"
        :current-father-menu-title="headerInfo.currentFatherMenuTitle"
        :current-first-level-menu="currentFirstLevelMenu"
        :current-menu-title="headerInfo.currentMenuTitle"
        :layout-settings="layoutSettings"
        :loading="loading"
        :menu-data="sidebarRouters"
        @logout="handleLogout"
        @show-settings="showDrawer"
        @personal-center="enterPersonalCenter"
        @refresh-page="refreshCurrentPage"
        @menu-select="handleMenuSelect"
        @first-level-menu-select="handleFirstLevelMenuSelect"
      />
      <a-layout>
        <MixSideMenu ref="mixSideMenuRef" v-model:collapsed="collapsed" :current-first-level-menu="currentFirstLevelMenu" :layout-settings="layoutSettings" @menu-select="handleMenuSelect" />
        <a-layout style="background: var(--page-background-gradient); min-width: 0">
          <HistoryNav v-if="layoutSettings.multiTab" class="layout-container__history" />
          <a-layout-content class="layout-container__content">
            <div class="content-wrapper" :style="{ height: layoutSettings.multiTab ? 'calc(100vh - 96px)' : 'calc(100vh - 64px)' }">
              <a-config-provider :locale="zhCN">
                <router-view></router-view>
              </a-config-provider>
            </div>
          </a-layout-content>
        </a-layout>
      </a-layout>
    </a-layout>

    <!-- 分栏布局 -->
    <a-layout v-else-if="layoutSettings.navMode === 'column'" class="layout-container layout-column">
      <a-layout>
        <!-- 一级菜单栏 -->
        <a-layout-sider class="first-level-menu" theme="light" width="80">
          <div class="column-first-menu">
            <!-- Logo 区域 -->
            <div class="menu-logo">
              <img :src="logoUrl" alt="BearJia Logo" class="logo-img" />
            </div>
            <a-menu :selectedKeys="firstLevelSelectedKeys" mode="inline">
              <a-menu-item v-for="(router, index) in sidebarRouters" :key="`${router.path}-${index}`" @click="handleFirstLevelMenuSelect(router)">
                <template #icon>
                  <BearJiaIcon :icon="router.meta?.icon || 'MenuOutlined'" />
                </template>
                <a-tooltip :title="router.meta?.title" placement="right">
                  <BearJiaIcon :icon="router.meta?.icon || 'MenuOutlined'" />
                </a-tooltip>
              </a-menu-item>
            </a-menu>
          </div>
        </a-layout-sider>
        <!-- 二级菜单栏 -->
        <a-layout-sider class="second-level-menu" theme="light" width="200">
          <div class="column-second-menu">
            <!-- 系统名称区域 -->
            <div class="system-title">
              <span class="title-text">{{ SYSTEM_INFO.name }}</span>
            </div>
            <a-menu
              v-model:selectedKeys="columnSecondLevelSelectedKeys"
              v-model:openKeys="columnSecondLevelOpenKeys"
              mode="inline"
            >
              <!-- 工作台菜单 -->
              <a-menu-item v-if="!currentFirstLevelMenu" key="workbench" @click="handleWorkbenchClick">
                <template #icon>
                  <BearJiaIcon icon="HomeOutlined" />
                </template>
                工作台
              </a-menu-item>

              <!-- 当前一级菜单的子菜单 -->
              <template v-for="(children, childIndex) in currentFirstLevelMenu.children" v-if="currentFirstLevelMenu" :key="`${children.path}-${childIndex}`">
                <!-- 有多个三级菜单或设置了 alwaysShow 的二级菜单 -->
                <a-sub-menu v-if="children.children && (children.children.length > 1 || (children.alwaysShow && children.children.length === 1))" :key="`${children.path}-${childIndex}`">
                  <template #icon>
                    <BearJiaIcon :icon="children.meta?.icon || 'AppstoreOutlined'" />
                  </template>
                  <template #title>{{ children.meta?.title }}</template>
                  <a-menu-item
                    v-for="(threeLevelChildren, threeIndex) in children.children"
                    :key="`${threeLevelChildren?.path}-${threeIndex}`"
                    @click="handleThreeLevelMenuClick(currentFirstLevelMenu.path, children, threeLevelChildren)"
                  >
                    <template #icon>
                      <BearJiaIcon :icon="threeLevelChildren.meta?.icon || 'BarsOutlined'" />
                    </template>
                    {{ threeLevelChildren.meta?.title }}
                  </a-menu-item>
                </a-sub-menu>

                <!-- 只有一个三级菜单且设置了 alwaysShow 的二级菜单 -->
                <a-sub-menu v-else-if="children.children && children.children.length === 1 && children.alwaysShow" :key="`${children.path}-${childIndex}`">
                  <template #icon>
                    <BearJiaIcon :icon="children.meta?.icon || 'AppstoreOutlined'" />
                  </template>
                  <template #title>{{ children.meta?.title }}</template>
                  <a-menu-item
                    v-for="(threeLevelChildren, threeIndex) in children.children"
                    :key="`${threeLevelChildren?.path}-${threeIndex}`"
                    @click="handleThreeLevelMenuClick(currentFirstLevelMenu.path, children, threeLevelChildren)"
                  >
                    <template #icon>
                      <BearJiaIcon :icon="threeLevelChildren.meta?.icon || 'BarsOutlined'" />
                    </template>
                    {{ threeLevelChildren.meta?.title }}
                  </a-menu-item>
                </a-sub-menu>

                <!-- 没有三级菜单的二级菜单 -->
                <a-menu-item
                  v-else
                  :key="`single-${children.path}-${childIndex}`"
                  @click="handleMenuClick(currentFirstLevelMenu.name, currentFirstLevelMenu.path, currentFirstLevelMenu.meta?.title, children.name, children.path, children.meta?.title, children.component)"
                >
                  <template #icon>
                    <BearJiaIcon :icon="children.meta?.icon || 'BarsOutlined'" />
                  </template>
                  {{ children.meta?.title }}
                </a-menu-item>
              </template>

              <!-- 没有选择菜单时的提示 -->
              <div v-if="!currentFirstLevelMenu && !firstLevelSelectedKeys.includes('workbench')" class="no-menu-tip">
                <p>请选择左侧菜单</p>
              </div>
            </a-menu>
          </div>
        </a-layout-sider>

        <!-- 内容区域 -->
        <a-layout style="background: var(--bg-color); min-width: 0">
          <!-- 分栏布局专用头部 -->
          <a-layout-header class="column-header">
            <HeaderBar
              v-model:collapsed="collapsed"
              :current-father-menu-title="headerInfo.currentFatherMenuTitle"
              :current-menu-title="headerInfo.currentMenuTitle"
              :layout-settings="layoutSettings"
              :loading="loading"
              @logout="handleLogout"
              @show-settings="showDrawer"
              @personal-center="enterPersonalCenter"
              @refresh-page="refreshCurrentPage"
            />
          </a-layout-header>
          <HistoryNav v-if="layoutSettings.multiTab" class="layout-container__history column-history" />
          <a-layout-content class="layout-container__content">
            <div class="content-wrapper" :style="{ height: layoutSettings.multiTab ? 'calc(100vh - 96px)' : 'calc(100vh - 64px)' }">
              <a-config-provider :locale="zhCN">
                <router-view></router-view>
              </a-config-provider>
            </div>
          </a-layout-content>
        </a-layout>
      </a-layout>
    </a-layout>

    <!-- 抽屉布局 -->
    <div v-else-if="layoutSettings.navMode === 'drawer'" class="layout-container layout-drawer">
      <HeaderBar
        :current-father-menu-title="headerInfo.currentFatherMenuTitle"
        :current-menu-title="headerInfo.currentMenuTitle"
        :layout-settings="layoutSettings"
        :loading="loading"
        @logout="handleLogout"
        @show-settings="showDrawer"
        @personal-center="enterPersonalCenter"
        @refresh-page="refreshCurrentPage"
        @toggle-menu="toggleDrawerMenu"
      />
      <HistoryNav v-if="layoutSettings.multiTab" class="layout-container__history drawer-history" />
      <div class="layout-container__content drawer-content-area" @click="handleContentClick">
        <div class="content-wrapper" :style="{ height: layoutSettings.multiTab ? 'calc(100vh - 96px)' : 'calc(100vh - 64px)' }">
          <a-config-provider :locale="zhCN">
            <router-view></router-view>
          </a-config-provider>
        </div>
      </div>

      <!-- 自定义侧边菜单（替代抽屉组件） -->
      <div v-if="drawerMenuVisible" class="custom-drawer-overlay" @click="handleDrawerOverlayClick">
        <div class="custom-drawer-menu" @click.stop>
          <div class="drawer-header">
            <span class="drawer-title">导航菜单</span>
            <button class="drawer-close" @click="toggleDrawerMenu">
              <BearJiaIcon icon="CloseOutlined" />
            </button>
          </div>
          <div class="drawer-content">
            <SideMenu ref="drawerSideMenuRef" :collapsed="false" :layout-settings="layoutSettings" :menu-data="sidebarRouters" @menu-select="handleMenuSelectAndClose" />
          </div>
        </div>
      </div>
    </div>

    <!-- 设置抽屉组件 -->
    <SettingDrawer v-model:visible="drawerVisible" :settings="layoutSettings" @update:settings="handleSettingsChange" />
  </div>
</template>

<script setup>
import { computed, nextTick, onMounted, reactive, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { useAppStore } from '@/stores/app';
import { usePermissionStore } from '@/stores/permission';
import { useTagsViewStore } from '@/stores/tagsView';
// 国际化：显示中文
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
import { message } from 'ant-design-vue';
import { SYSTEM_INFO } from '@/config/system.config';
import logoUrl from '@/assets/images/logo.png';

// 将侧边栏和头部组件拆分
import SideMenu from '@/components/layout/SideMenu.vue';
import MixSideMenu from '@/components/layout/MixSideMenu.vue';
import HeaderBar from '@/components/layout/HeaderBar.vue';
import HistoryNav from '@/components/layout/HistoryNav.vue';
// 将布局设置抽屉组件拆分
import SettingDrawer from '@/components/layout/SettingDrawer.vue';
// 导入图标组件
import { BearJiaIcon } from '@/utils/BearJiaIcon.js';
// 导入外链判断函数
import { isExternal } from '@/utils/bearjia.js';
import { themeService } from '@/utils/theme';

dayjs.locale('zh-cn');

const vueRouter = useRouter();
const vueRoute = useRoute();
const userRouters = vueRouter.getRoutes();

// 获取 store
const userStore = useUserStore();
const appStore = useAppStore();
const permissionStore = usePermissionStore();
const tagsViewStore = useTagsViewStore();

// 左侧边栏是否折叠：默认不折叠
const collapsed = ref(false);
const loading = ref(false);
const drawerVisible = ref(false);

// 初始化 headerInfo
const headerInfo = reactive({
  currentFatherMenuTitle: '主页',
  currentMenuTitle: '工作台',
});

// 菜单相关状态数据
const menuState = reactive({
  // 所有一级菜单节点
  rootSubmenuKeys: [],
  // 展开的父菜单节点
  openedFaterMenuKeys: [],
  // 当前选中的菜单项
  selectedMenuKeys: [],
});

// 混合模式状态
const currentFirstLevelMenu = ref(null); // 当前选中的一级菜单

// 分栏布局状态
const firstLevelSelectedKeys = ref(['workbench']); // 一级菜单选中状态
const columnSecondLevelSelectedKeys = ref([]); // 二级菜单选中状态
const columnSecondLevelOpenKeys = ref([]); // 二级菜单展开状态

// 抽屉布局状态
const drawerMenuVisible = ref(false); // 抽屉菜单显示状态

// 菜单组件引用
const sideMenuRef = ref(null);
const topMenuRef = ref(null);
const mixTopMenuRef = ref(null);
const mixSideMenuRef = ref(null);
const drawerSideMenuRef = ref(null);

// 通过从后端获取的用户可以访问的菜单信息生产前端菜单列表
const sidebarRouters = computed(() => permissionStore.sidebarRouters || []);

// 布局设置
const layoutSettings = computed(
  () =>
    appStore.layoutSettings || {
      primaryColor: '#1677ff',
      darkMode: false,
      navMode: 'side',
      menuTheme: 'light',
      layout: 'mix',
      contentWidth: 'fluid',
      fixedHeader: true,
      fixedSidebar: true,
      splitMenus: false,
      colorWeak: false,
      multiTab: true,
    },
);

// 计算 CSS 变量
const cssVars = computed(() =>
  themeService.getLayoutCSSVariables(layoutSettings.value?.primaryColor || '#1677ff')
);

// 计算布局类名
const layoutClasses = computed(() => {
  return {
    'layout-side': layoutSettings.value.navMode === 'side',
    'layout-top': layoutSettings.value.navMode === 'top',
    'layout-mix': layoutSettings.value.navMode === 'mix',
    'layout-column': layoutSettings.value.navMode === 'column',
    'layout-drawer': layoutSettings.value.navMode === 'drawer',
    'fixed-header': layoutSettings.value.fixedHeader,
    'fixed-sidebar': layoutSettings.value.fixedSidebar,
    'dark-theme': layoutSettings.value.darkMode,
    'color-weak': layoutSettings.value.colorWeak,
  };
});

// 设置一级父菜单
const initRootMenus = () => {
  if (sidebarRouters.value && Array.isArray(sidebarRouters.value)) {
    sidebarRouters.value.forEach((element) => {
      menuState.rootSubmenuKeys.push(element.path);
    });
  }
  // 将工作台菜单添加到一级菜单节点数组中
  menuState.rootSubmenuKeys.push('workbench');
};

// 初始化路由状态
const initRouteState = () => {
  if (vueRoute.path) {
    let arr = vueRoute.path.split('/');
    // 如果有父菜单，获取父菜单key
    if (arr.length > 2) {
      menuState.openedFaterMenuKeys = [];
      menuState.openedFaterMenuKeys.push('/' + arr[1]);
      menuState.selectedMenuKeys = [];
      menuState.selectedMenuKeys.push(arr[2]);
    } else {
      // 如果没有父菜单
      if ('/home' === vueRoute.path) {
        // 如果是主页路由，则选中工作台菜单
        menuState.selectedMenuKeys.push('workbench');
      } else {
        menuState.openedFaterMenuKeys = [];
        menuState.selectedMenuKeys = [];
        menuState.selectedMenuKeys.push(arr[1]);
      }
    }

    // 刷新页面后，重新设置顶部的菜单名称
    if (sidebarRouters.value && Array.isArray(sidebarRouters.value)) {
      sidebarRouters.value.forEach((element) => {
        if (element.path === '/' + arr[1]) {
          headerInfo.currentFatherMenuTitle = element.meta?.title || '主页';
          if (element.children) {
            element.children.forEach((child) => {
              if (child.path === arr[2]) {
                headerInfo.currentMenuTitle = child.meta?.title || '';
              }
            });
          }
        }
      });
    }
  }
};

// 添加错误边界处理
const handleError = (error) => {
  console.error('页面发生错误:', error);
  message.error('操作失败，请稍后重试');
};

// 处理混合模式一级菜单选择
const handleFirstLevelMenuSelect = (menuInfo) => {
  try {
    console.log('一级菜单选择:', menuInfo);
    currentFirstLevelMenu.value = menuInfo;

    // 更新分栏布局的选中状态
    if (menuInfo) {
      const menuIndex = sidebarRouters.value.findIndex((item) => item.path === menuInfo.path);
      firstLevelSelectedKeys.value = [`${menuInfo.path}-${menuIndex}`];
    } else {
      firstLevelSelectedKeys.value = ['workbench'];
      currentFirstLevelMenu.value = null;
    }
  } catch (error) {
    console.error('一级菜单选择错误:', error);
  }
};

// 处理工作台点击
const handleWorkbenchClick = () => {
  firstLevelSelectedKeys.value = ['workbench'];
  currentFirstLevelMenu.value = null;

  // 跳转到工作台
  handleMenuSelect({
    fatherName: 'HomePage',
    fatherPath: '/home',
    fatherTitle: '主页',
    name: 'Workbench',
    path: '/home/workbench',
    title: '工作台',
    component: null,
  });
};

// 修复抽屉模式样式的辅助函数
const fixDrawerLayoutStyles = () => {
  nextTick(() => {
    const isDark = document.documentElement.classList.contains('dark-theme');
    const correctBgColor = isDark ? '#1f1f1f' : '#f0f2f5';

    // 修复主容器背景色
    const appContainer = document.querySelector('.app-container.layout-drawer');
    if (appContainer) {
      appContainer.style.backgroundColor = correctBgColor;
    }

    // 修复布局容器背景色
    const layoutContainer = document.querySelector('.layout-drawer .layout-container');
    if (layoutContainer) {
      layoutContainer.style.backgroundColor = correctBgColor;
    }

    // 修复内容区域背景色
    const contentElements = document.querySelectorAll('.layout-drawer .layout-container__content, .layout-drawer .content-wrapper');
    contentElements.forEach((el) => {
      el.style.backgroundColor = correctBgColor;
      el.style.background = correctBgColor;
    });

    console.log('抽屉模式样式修复完成，背景色:', correctBgColor);
  });
};

// 处理抽屉菜单切换
const toggleDrawerMenu = () => {
  console.log('toggleDrawerMenu 被调用，当前状态:', drawerMenuVisible.value);
  drawerMenuVisible.value = !drawerMenuVisible.value;
  console.log('切换后状态:', drawerMenuVisible.value);
};

// 处理内容区域点击（关闭抽屉）
const handleContentClick = () => {
  if (drawerMenuVisible.value) {
    drawerMenuVisible.value = false;
  }
};

// 处理抽屉遮罩层点击（关闭抽屉）
const handleDrawerOverlayClick = () => {
  drawerMenuVisible.value = false;
};

// 处理菜单选择并关闭抽屉
const handleMenuSelectAndClose = (menuInfo) => {
  handleMenuSelect(menuInfo);
  drawerMenuVisible.value = false;
};

// 处理二级菜单点击（分栏布局使用）
const handleMenuClick = (fatherName, fatherPath, fatherTitle, name, path, title, component) => {
  try {
    handleMenuSelect({
      fatherName,
      fatherPath,
      fatherTitle,
      name,
      path,
      title,
      component,
    });
  } catch (error) {
    console.error('菜单点击错误:', error);
  }
};

// 处理三级菜单点击（分栏布局使用）
const handleThreeLevelMenuClick = (greatFatherPath, father, menu) => {
  try {
    handleMenuSelect({
      greatFatherPath,
      father,
      menu,
    });
  } catch (error) {
    console.error('三级菜单点击错误:', error);
  }
};

// 处理菜单选择
const handleMenuSelect = (menuInfo) => {
  try {
    console.log('菜单选择信息:', menuInfo);

    // 如果没有子路径，说明是父菜单，不进行导航
    if (!menuInfo.path && !menuInfo.menu?.path) {
      return;
    }

    if (menuInfo.greatFatherPath) {
      // 处理三级菜单
      console.log('处理三级菜单');
      clickThreeLevelMenuItem(menuInfo.greatFatherPath, menuInfo.father.name, menuInfo.father.path, menuInfo.father.meta.title, menuInfo.menu.name, menuInfo.menu.path, menuInfo.menu.meta.title, menuInfo.menu.component);
    } else {
      // 处理二级菜单
      console.log('处理二级菜单');
      clickMenuItem(menuInfo.fatherName, menuInfo.fatherPath, menuInfo.fatherTitle, menuInfo.name, menuInfo.path, menuInfo.title, menuInfo.component);
    }
  } catch (error) {
    console.error('菜单选择错误:', error);
    message.error('菜单选择失败，请重试');
  }
};

// 优化路由跳转错误处理
const clickMenuItem = async (fatherMenuName, fatherMenuPath, fatherTitle, menuName, menuPath, menuTitle, menuComponent) => {
  try {
    loading.value = true;
    // 确保路径有效
    if (!fatherMenuPath || !menuPath) {
      throw new Error('无效的菜单路径');
    }

    // 检查是否是外链
    if (isExternal(menuPath)) {
      // 外链在新窗口中打开
      window.open(menuPath, '_blank');
      return;
    }

    if (menuPath === 'workbench') {
      //如果点击的是工作台菜单，则关闭其他已展开的父菜单
      menuState.openedFaterMenuKeys = [];
      await vueRouter.push({
        path: '/home/workbench',
        meta: { title: '工作台' },
      });
    } else {
      let routePathStr = fatherMenuPath + '/' + menuPath;
      console.log('点击菜单后请求路由=' + routePathStr);
      await vueRouter.push({
        path: routePathStr,
        meta: { title: menuTitle },
      });

      headerInfo.currentFatherMenuTitle = fatherTitle;
      headerInfo.currentMenuTitle = menuTitle;
    }
  } catch (error) {
    handleError(error);
  } finally {
    loading.value = false;
  }
};

// 点击菜单项：跳转到对应的路由
const clickThreeLevelMenuItem = async (greatFatherMenuPath, fatherMenuName, fatherMenuPath, fatherTitle, menuName, menuPath, menuTitle, menuComponent) => {
  try {
    loading.value = true;

    console.log('三级菜单点击参数:', {
      greatFatherMenuPath,
      fatherMenuName,
      fatherMenuPath,
      fatherTitle,
      menuName,
      menuPath,
      menuTitle,
      menuComponent,
    });

    // 检查是否是外链
    if (isExternal(menuPath)) {
      // 外链在新窗口中打开
      window.open(menuPath, '_blank');
      return;
    }

    // 检查当前注册的路由
    const routes = vueRouter.getRoutes();
    console.log(
      '当前注册的路由:',
      routes.map((r) => r.path),
    );

    // 确保路径有效
    if (!menuPath) {
      throw new Error('无效的菜单路径');
    }

    headerInfo.currentFatherMenuTitle = fatherTitle;
    headerInfo.currentMenuTitle = menuTitle;

    // 根据后端路由扁平化处理，三级菜单的路径应该是完整的扁平路径
    // 例如：/monitor/operlog 而不是 /monitor/log/operlog
    let routePathStr;

    // 如果 menuPath 已经是完整路径（包含父路径），直接使用
    if (menuPath.includes('/')) {
      routePathStr = menuPath.startsWith('/') ? menuPath : '/' + menuPath;
    } else {
      // 否则拼接完整路径
      routePathStr = `/${greatFatherMenuPath}/${fatherMenuPath}/${menuPath}`.replace(/\/+/g, '/');
    }

    console.log('点击菜单后请求路由=' + routePathStr);

    // 检查路由是否存在
    const targetRoute = routes.find((r) => r.path === routePathStr);
    if (!targetRoute) {
      console.error('路由不存在:', routePathStr);
      console.log(
        '可用路由:',
        routes.filter((r) => r.path.includes('monitor') || r.path.includes('operlog') || r.path.includes('logininfor')),
      );
    }

    await vueRouter.push({
      path: routePathStr,
      meta: { title: menuTitle },
    });
  } catch (error) {
    console.error('三级菜单路由跳转错误:', error);
    handleError(error);
  } finally {
    loading.value = false;
  }
};

// 显示设置抽屉
const showDrawer = () => {
  drawerVisible.value = true;
};

// 优化的退出登录处理
const handleLogout = async () => {
  try {
    loading.value = true;
    await userStore.logout();
    message.success('退出登录成功');
    // 跳转到登录页面
    vueRouter.push({ path: '/login' });
  } catch (error) {
    message.error('退出登录失败');
  } finally {
    loading.value = false;
  }
};

// 进入个人中心
const enterPersonalCenter = () => {
  vueRouter.push({ path: '/system/user/profile' });
};

// 初始化加载
onMounted(() => {
  // 应用主题设置
  appStore.applyTheme();

  // 设置系统标题
  nextTick(() => {
    if (appStore.systemConfig.title) {
      document.title = appStore.systemConfig.title;
    }
  });

  initRootMenus();
  initRouteState();

  // 初始化菜单激活状态
  nextTick(() => {
    updateMenuActiveState();
  });
});

// 更新菜单激活状态
const updateMenuActiveState = () => {
  const currentPath = vueRoute.path;
  console.log('更新菜单激活状态，当前路径:', currentPath);

  // 根据不同的布局模式，调用对应菜单组件的 setCurrentMenu 方法
  const navMode = layoutSettings.value.navMode;

  if (navMode === 'side') {
    // 侧边栏模式
    if (sideMenuRef.value && sideMenuRef.value.setCurrentMenu) {
      console.log('调用 SideMenu.setCurrentMenu');
      sideMenuRef.value.setCurrentMenu(currentPath);
    }
  } else if (navMode === 'top') {
    // 顶部菜单模式 - TopMenu 在 HeaderBar 内部
    nextTick(() => {
      if (topMenuRef.value && topMenuRef.value.topMenuRef) {
        const topMenu = topMenuRef.value.topMenuRef;
        if (topMenu && topMenu.setCurrentMenu) {
          console.log('调用 TopMenu.setCurrentMenu');
          topMenu.setCurrentMenu(currentPath);
        }
      }
    });
  } else if (navMode === 'mix') {
    // 混合模式 - 需要先确定一级菜单，然后激活二级菜单
    const firstLevelMenu = findFirstLevelMenuByPath(currentPath);
    console.log('混合模式找到一级菜单:', firstLevelMenu);

    if (firstLevelMenu) {
      currentFirstLevelMenu.value = firstLevelMenu;

      nextTick(() => {
        // 激活顶部的一级菜单
        if (mixTopMenuRef.value && mixTopMenuRef.value.mixTopMenuRef) {
          const mixTopMenu = mixTopMenuRef.value.mixTopMenuRef;
          if (mixTopMenu && mixTopMenu.setCurrentMenu) {
            console.log('调用 MixTopMenu.setCurrentMenu');
            mixTopMenu.setCurrentMenu(currentPath);
          }
        }

        // 激活侧边的二级菜单
        if (mixSideMenuRef.value && mixSideMenuRef.value.setCurrentMenu) {
          console.log('调用 MixSideMenu.setCurrentMenu');
          mixSideMenuRef.value.setCurrentMenu(currentPath);
        }
      });
    }
  } else if (navMode === 'drawer') {
    // 抽屉模式 - 菜单通过 v-if 条件渲染，需要等待渲染后才能激活
    // 如果抽屉已打开，直接激活菜单
    if (drawerMenuVisible.value && drawerSideMenuRef.value && drawerSideMenuRef.value.setCurrentMenu) {
      console.log('调用 Drawer SideMenu.setCurrentMenu（抽屉已打开）');
      nextTick(() => {
        drawerSideMenuRef.value.setCurrentMenu(currentPath);
      });
    } else {
      // 如果抽屉未打开，等待下次打开时激活
      console.log('抽屉模式：菜单未渲染，将在打开时激活');
    }
  } else if (navMode === 'column') {
    // 分栏模式 - 需要手动设置菜单激活状态
    const firstLevelMenu = findFirstLevelMenuByPath(currentPath);
    if (firstLevelMenu) {
      console.log('分栏模式找到一级菜单:', firstLevelMenu);
      currentFirstLevelMenu.value = firstLevelMenu;
      const menuIndex = sidebarRouters.value.findIndex(item => item.path === firstLevelMenu.path);
      firstLevelSelectedKeys.value = [`${firstLevelMenu.path}-${menuIndex}`];

      // 设置二级菜单的激活状态
      nextTick(() => {
        // 查找当前路径对应的二级菜单项
        if (firstLevelMenu.children) {
          for (const [childIndex, child] of firstLevelMenu.children.entries()) {
            const childFullPath = `${firstLevelMenu.path}/${child.path}`;

            // 检查是否匹配二级菜单
            if (child.path === currentPath || childFullPath === currentPath) {
              columnSecondLevelSelectedKeys.value = [`single-${child.path}-${childIndex}`];
              console.log('分栏模式激活二级菜单:', columnSecondLevelSelectedKeys.value);
              return;
            }

            // 检查是否匹配三级菜单
            if (child.children && child.children.length > 0) {
              for (const [threeIndex, threeLevel] of child.children.entries()) {
                const threeLevelFullPath = `${firstLevelMenu.path}/${child.path}/${threeLevel.path}`;
                if (threeLevel.path === currentPath || threeLevelFullPath === currentPath) {
                  columnSecondLevelSelectedKeys.value = [`${threeLevel.path}-${threeIndex}`];
                  columnSecondLevelOpenKeys.value = [`${child.path}-${childIndex}`];
                  console.log('分栏模式激活三级菜单:', columnSecondLevelSelectedKeys.value, '展开:', columnSecondLevelOpenKeys.value);
                  return;
                }
              }
            }
          }
        }
      });
    }
  }
};

// 根据路径查找所属的一级菜单
const findFirstLevelMenuByPath = (path) => {
  for (const router of sidebarRouters.value) {
    if (router.children) {
      for (const child of router.children) {
        // 构建完整路径进行匹配
        const childFullPath = `${router.path}/${child.path}`;

        // 检查二级菜单 - 同时匹配完整路径和相对路径
        if (child.path === path || childFullPath === path) {
          return router;
        }

        // 检查三级菜单
        if (child.children && child.children.length > 0) {
          for (const threeLevel of child.children) {
            const threeLevelFullPath = `${router.path}/${child.path}/${threeLevel.path}`;
            if (threeLevel.path === path || threeLevelFullPath === path) {
              return router;
            }
          }
        }
      }
    }
  }
  return null;
};

// 监听路由变化，更新菜单激活状态
watch(() => vueRoute.path, () => {
  nextTick(() => {
    updateMenuActiveState();
  });
});

// 监听布局设置变化并保存
watch(
  layoutSettings,
  (newSettings, oldSettings) => {
    try {
      console.log('布局设置变化:', newSettings);
      localStorage.setItem('layoutSettings', JSON.stringify(newSettings));

      // 应用主题变化
      appStore.applyTheme();

      // 检查是否是布局模式变化
      if (oldSettings && newSettings.navMode !== oldSettings.navMode) {
        console.log('布局模式切换:', oldSettings.navMode, '->', newSettings.navMode);

        // 等待新布局渲染完成后，重新激活菜单
        nextTick(() => {
          // 延迟执行，确保组件已完全渲染
          setTimeout(() => {
            updateMenuActiveState();
          }, 100);
        });
      }
    } catch (error) {
      console.error('布局设置变化处理失败:', error);
    }
  },
  { deep: true },
);

// 监听抽屉状态变化，当抽屉打开时激活菜单
watch(drawerMenuVisible, (newValue) => {
  console.log('抽屉状态变化:', newValue);

  // 当抽屉打开时，激活当前路由对应的菜单
  if (newValue && layoutSettings.value.navMode === 'drawer') {
    nextTick(() => {
      const currentPath = vueRoute.path;
      if (drawerSideMenuRef.value && drawerSideMenuRef.value.setCurrentMenu) {
        console.log('抽屉打开，激活菜单:', currentPath);
        drawerSideMenuRef.value.setCurrentMenu(currentPath);
      }
    });
  }
});

// 处理设置变更
const handleSettingsChange = (newSettings) => {
  try {
    console.log('BaseLayout 接收到设置变更:', newSettings);
    appStore.updateSettings(newSettings);

    nextTick(() => {
      console.log('当前布局设置:', layoutSettings.value);
    });
  } catch (error) {
    console.error('设置变更处理失败:', error);
  }
};
// 刷新当前页面内容
const refreshCurrentPage = async () => {
  try {
    loading.value = true;

    // 使用provide/inject方式刷新页面，避免路由重定向
    // 生成一个随机key，强制router-view重新渲染
    const timestamp = new Date().getTime();
    // 保存当前滚动位置
    const scrollPosition = document.documentElement.scrollTop || document.body.scrollTop;

    // 强制重新获取路由数据
    if (vueRoute.meta && vueRoute.meta.keepAlive === false) {
      // 如果页面配置了不缓存，则重新加载数据
      const currentComponent = vueRoute.matched[vueRoute.matched.length - 1].instances.default;
      if (currentComponent && typeof currentComponent.fetchData === 'function') {
        await currentComponent.fetchData();
      }
    } else {
      // 通过修改查询参数的方式刷新页面
      const { fullPath, query } = vueRoute;
      const newQuery = { ...query, _refresh: timestamp };
      await vueRouter.replace({ path: vueRoute.path, query: newQuery });
    }

    // 恢复滚动位置
    nextTick(() => {
      window.scrollTo(0, scrollPosition);
    });

    message.success('页面刷新成功');
  } catch (error) {
    console.error('页面刷新失败:', error);
    message.error('页面刷新失败，请重试');
  } finally {
    loading.value = false;
  }
};
</script>

<style lang="less" scoped>
.app-container {
  height: 100vh;
  box-sizing: border-box;
  overflow: hidden;

  .layout-container {
    height: 100%;
    background: var(--page-background-gradient);
    border-radius: 0;
    overflow: hidden;
    box-shadow: none;

    &__content {
      // margin: 16px;
      overflow: hidden;
      width: 100%;
      max-width: 100%;
      flex: 1; // 确保在 flex 布局中正确占据空间
      min-width: 0; // 关键：防止 flex 子元素被内容撑开导致溢出

      .content-wrapper {
        background: var(--page-background-gradient);
        border-radius: 0; // 移除圆角，让滚动条贴近边缘
        min-height: calc(100vh - 170px);
        padding: 0 0 12px 12px; // 顶部与 Header 使用同一背景，避免断层
        margin-right: 4px;
        overflow-y: auto;
        overflow-x: hidden;
        width: calc(100% - 4px);
        max-width: calc(100% - 4px);
        box-sizing: border-box;
        min-width: 0; // 关键：防止 flex 子元素被内容撑开导致溢出
        scrollbar-width: thin;
        scrollbar-color: rgba(148, 163, 184, 0.45) transparent;

        &::-webkit-scrollbar {
          width: 8px;
        }

        &::-webkit-scrollbar-track {
          margin-block: 10px;
          background: transparent;
        }

        &::-webkit-scrollbar-thumb {
          background: rgba(148, 163, 184, 0.45);
        }

        &::-webkit-scrollbar-thumb:hover {
          background: rgba(100, 116, 139, 0.58);
        }
      }
    }
  }

  // 抽屉模式特殊处理
  &.layout-drawer {
    .layout-container {
      display: flex;
      flex-direction: column;
      height: 100vh;
      background: var(--page-background-gradient);

      .drawer-content-area {
        flex: 1;
        overflow: hidden;
        background: var(--page-background-gradient);

        .content-wrapper {
          background: var(--page-background-gradient);
          border-radius: 0;
          min-height: calc(100vh - 170px);
          padding: 0 0 12px 12px;
          margin-right: 4px;
          overflow-y: auto;
          overflow-x: hidden;
          width: calc(100% - 4px);
          max-width: calc(100% - 4px);
          scrollbar-width: thin;
          scrollbar-color: rgba(148, 163, 184, 0.45) transparent;

          &::-webkit-scrollbar {
            width: 8px;
          }

          &::-webkit-scrollbar-track {
            margin-block: 10px;
            background: transparent;
          }

          &::-webkit-scrollbar-thumb {
            background: rgba(148, 163, 184, 0.45);
          }

          &::-webkit-scrollbar-thumb:hover {
            background: rgba(100, 116, 139, 0.58);
          }
        }
      }
    }
  }
}

// 分栏布局logo样式
.menu-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;

  .logo-img {
    height: 40px;
    width: auto;
    transition: transform 0.3s ease;

    &:hover {
      transform: scale(1.05);
    }
  }
}
</style>
