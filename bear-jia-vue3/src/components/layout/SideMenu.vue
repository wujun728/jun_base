<template>
  <a-layout-sider
      width="200"
      :theme="layoutSettings.darkMode ? 'dark' : 'light'"
      :collapsed="collapsed"
      :collapsed-width="64"
      :trigger="null"
      collapsible
      :class="['side-layout-sider', { 'is-collapsed': collapsed, 'dark-theme': layoutSettings.darkMode, 'colorful-icon': layoutSettings.sidebarColorfulIcon }]"
      @collapse="$emit('update:collapsed', $event)"
  >
    <!-- Logo区域 -->
    <div class="layout-logo">
      <img :src="logoUrl" class="logo-img" :alt="`${SYSTEM_INFO.shortName} Logo`" />
      <span class="logo-title" v-show="!collapsed">{{ SYSTEM_INFO.name }}</span>
    </div>

    <!-- 菜单区域 -->
    <a-menu
        v-model:selectedKeys="selectedKeys"
        v-model:openKeys="openKeys"
        :theme="layoutSettings.darkMode ? 'dark' : 'light'"
        mode="inline"
        class="side-menu"
        @openChange="handleOpenChange"
    >
      <!-- 工作台 (保持不变) -->
      <a-menu-item key="workbench" @click="handleMenuClick('HomePage', '/home', '主页', 'Workbench', 'workbench', '工作台')">
        <template #icon>
          <BearJiaIcon icon="HomeOutlined" />
        </template>
        工作台
      </a-menu-item>

      <!-- 动态菜单 -->
      <template v-for="(router, index) in menuData">
        <!-- 情况1: 当父菜单有多个子菜单，或者设置了 alwaysShow 且有子菜单时，渲染成可折叠的 SubMenu -->
        <a-sub-menu
            v-if="router.children && (router.children.length > 1 || (router.alwaysShow && router.children.length === 1))"
            :key="`${router.path}-${index}`"
        >
          <template #icon>
            <BearJiaIcon :icon="router.meta?.icon || 'MenuOutlined'" />
          </template>
          <template #title>
            <span class="menu-title-row">
              <span class="menu-title-text">{{ router.meta?.title }}</span>
              <span v-if="isExternal(router.path)" class="external-link-icon">
                <BearJiaIcon icon="LinkOutlined" />
              </span>
            </span>
          </template>

          <!-- 内部子菜单渲染逻辑 -->
          <template v-for="(children, childIndex) in router.children">
            <!-- 二级父菜单 -->
            <a-sub-menu
                v-if="children.children && children.children.length > 0"
                :key="`${children.path}-${childIndex}`"
            >
              <template #icon>
                <BearJiaIcon :icon="children.meta?.icon || 'AppstoreOutlined'" />
              </template>
              <template #title>
                <span class="menu-title-row">
                  <span class="menu-title-text">{{ children.meta?.title }}</span>
                  <span v-if="isExternal(children.path)" class="external-link-icon">
                    <BearJiaIcon icon="LinkOutlined" />
                  </span>
                </span>
              </template>
              <!-- 三级菜单 -->
              <a-menu-item
                  v-for="(threeLevelChildren, threeIndex) in children.children"
                  :key="`${threeLevelChildren?.path}-${threeIndex}`"
                  @click="handleThreeLevelMenuClick(router.path, children, threeLevelChildren)"
              >
                <template #icon>
                  <BearJiaIcon :icon="threeLevelChildren.meta?.icon || 'BarsOutlined'" />
                </template>
                <span class="menu-title-row">
                  <span class="menu-title-text">{{ threeLevelChildren.meta?.title }}</span>
                  <span v-if="isExternal(threeLevelChildren.path)" class="external-link-icon">
                    <BearJiaIcon icon="LinkOutlined" />
                  </span>
                </span>
              </a-menu-item>
            </a-sub-menu>
            <!-- 二级菜单 -->
            <a-menu-item
                v-else
                :key="`single-${children.path}-${childIndex}`"
                :class="{ 'external-menu-item': isExternal(children.path) }"
                @click="handleMenuClick(router.name, router.path, router.meta.title, children.name, children.path, children.meta.title, children.component)"
            >
              <template #icon>
                <BearJiaIcon :icon="children.meta?.icon || 'BarsOutlined'" />
              </template>
              <span class="menu-title-row">
                <span class="menu-title-text">{{ children.meta?.title }}</span>
                <span v-if="isExternal(children.path)" class="external-link-icon">
                  <BearJiaIcon icon="LinkOutlined" />
                </span>
              </span>
            </a-menu-item>
          </template>
        </a-sub-menu>

        <!-- 情况2: 当父菜单只有一个子菜单且没有设置 alwaysShow 时 (包括外链)，直接渲染成一个普通的 MenuItem -->
        <a-menu-item
            v-else-if="router.children && router.children.length === 1 && !router.alwaysShow"
            :key="`single-child-wrapper-${router.children[0].path}`"
            :class="{ 'external-menu-item': isExternal(router.children[0].path) }"
            @click="handleMenuClick(router.name, router.path, router.meta.title, router.children[0].name, router.children[0].path, router.children[0].meta.title, router.children[0].component)"
        >
          <template #icon>
            <BearJiaIcon :icon="router.children[0].meta?.icon || router.meta?.icon || 'MenuOutlined'" />
          </template>
          <span class="menu-title-row">
            <span class="menu-title-text">{{ router.children[0].meta?.title }}</span>
            <span v-if="isExternal(router.children[0].path)" class="external-link-icon">
              <BearJiaIcon icon="LinkOutlined" />
            </span>
          </span>
        </a-menu-item>

        <!-- 情况3 (可选，容错): 没有子菜单的顶级菜单 -->
        <a-menu-item
            v-else-if="!router.children || router.children.length === 0"
            :key="`no-child-${router.path}-${index}`"
            :class="{ 'external-menu-item': isExternal(router.path) }"
            @click="handleMenuClick(null, null, null, router.name, router.path, router.meta.title, router.component)"
        >
          <template #icon>
            <BearJiaIcon :icon="router.meta?.icon || 'MenuOutlined'" />
          </template>
          <span class="menu-title-row">
            <span class="menu-title-text">{{ router.meta?.title }}</span>
            <span v-if="isExternal(router.path)" class="external-link-icon">
              <BearJiaIcon icon="LinkOutlined" />
            </span>
          </span>
        </a-menu-item>
      </template>
    </a-menu>
  </a-layout-sider>
</template>

<script setup>
import { ref } from 'vue';
import { BearJiaIcon } from '@/utils/BearJiaIcon.js';
import { isExternal } from '@/utils/bearjia.js';
import { SYSTEM_INFO } from '@/config/system.config';
import logoUrl from '@/assets/images/logo.png';

const props = defineProps({
  collapsed: Boolean,
  menuData: Array,
  layoutSettings: Object
});

const emit = defineEmits(['update:collapsed', 'menuSelect']);

const selectedKeys = ref([]);
const openKeys = ref([]);

const handleOpenChange = (keys) => {
  if (keys.length <= 1) {
    openKeys.value = keys;
    return;
  }

  const latestKey = keys[keys.length - 1];
  const isTopLevel = props.menuData.some((item, idx) => `${item.path}-${idx}` === latestKey);

  if (isTopLevel) {
    openKeys.value = [latestKey];
  } else {
    openKeys.value = keys;
  }
};

const handleMenuClick = (fatherName, fatherPath, fatherTitle, name, path, title, component) => {
  try {
    // 检查是否是外链
    if (isExternal(path)) {
      window.open(path, '_blank');
      return;
    }

    if (path === 'workbench') {
      selectedKeys.value = ['workbench'];
      openKeys.value = [];
    } else {
      const fatherIndex = props.menuData.findIndex(item => item.path === fatherPath);
      const childIndex = props.menuData[fatherIndex]?.children.findIndex(item => item.path === path);
      const menuKey = `${path}-${childIndex}`;
      const fatherKey = `${fatherPath}-${fatherIndex}`;

      selectedKeys.value = [menuKey];
      openKeys.value = [fatherKey];
    }
    emit('menuSelect', {
      fatherName, fatherPath, fatherTitle, name, path, title, component
    });
  } catch (error) {
    console.error('菜单点击错误:', error);
  }
};

const handleThreeLevelMenuClick = (greatFatherPath, father, menu) => {
  try {
    // 检查是否是外链
    if (isExternal(menu.path)) {
      // 外链直接在新窗口打开，不触发路由跳转
      window.open(menu.path, '_blank');
      return;
    }

    const greatFatherIndex = props.menuData.findIndex(item => item.path === greatFatherPath);
    const fatherIndex = props.menuData[greatFatherIndex]?.children.findIndex(item => item.path === father.path);
    const menuIndex = father?.children.findIndex(item => item.path === menu.path);

    selectedKeys.value = [`${menu.path}-${menuIndex}`];
    openKeys.value = [
      `${greatFatherPath}-${greatFatherIndex}`,
      `${father.path}-${fatherIndex}`
    ];

    emit('menuSelect', {
      greatFatherPath, father, menu
    });
  } catch (error) {
    console.error('三级菜单点击错误:', error);
  }
};

const setCurrentMenu = (fullPath) => {
  console.log('SideMenu.setCurrentMenu 被调用，路径:', fullPath);

  // 检查是否是工作台
  if (fullPath === '/home/workbench') {
    selectedKeys.value = ['workbench'];
    openKeys.value = [];
    console.log('设置工作台激活');
    return;
  }

  if (!props.menuData || props.menuData.length === 0) {
    console.log('菜单数据为空');
    return;
  }

  // 遍历菜单数据查找匹配的路径
  for (const [routerIndex, router] of props.menuData.entries()) {
    if (!router.children) continue;

    for (const [childIndex, child] of router.children.entries()) {
      // 构建完整路径进行比较
      const childFullPath = `${router.path}/${child.path}`;

      // 检查三级菜单
      if (child.children && child.children.length > 0) {
        for (const [threeIndex, threeLevel] of child.children.entries()) {
          const threeFullPath = `${router.path}/${threeLevel.path}`;

          // 同时匹配完整路径和相对路径
          if (threeLevel.path === fullPath || threeFullPath === fullPath) {
            selectedKeys.value = [`${threeLevel.path}-${threeIndex}`];
            openKeys.value = [
              `${router.path}-${routerIndex}`,
              `${child.path}-${childIndex}`
            ];
            console.log('找到三级菜单:', {
              匹配路径: threeLevel.path,
              完整路径: threeFullPath,
              selectedKeys: selectedKeys.value,
              openKeys: openKeys.value
            });
            return;
          }
        }
      } else {
        // 检查二级菜单 - 同时匹配完整路径和相对路径
        if (child.path === fullPath || childFullPath === fullPath) {
          selectedKeys.value = [`single-${child.path}-${childIndex}`];
          openKeys.value = [`${router.path}-${routerIndex}`];
          console.log('找到二级菜单:', {
            子菜单路径: child.path,
            完整路径: childFullPath,
            selectedKeys: selectedKeys.value,
            openKeys: openKeys.value
          });
          return;
        }
      }
    }

    // 检查只有一个子菜单的情况
    if (router.children.length === 1) {
      const onlyChild = router.children[0];
      const onlyChildFullPath = `${router.path}/${onlyChild.path}`;

      if (onlyChild.path === fullPath || onlyChildFullPath === fullPath) {
        selectedKeys.value = [`single-child-wrapper-${onlyChild.path}`];
        openKeys.value = [];
        console.log('找到单子菜单:', {
          子菜单路径: onlyChild.path,
          完整路径: onlyChildFullPath,
          selectedKeys: selectedKeys.value
        });
        return;
      }
    }
  }

  console.log('未找到匹配的菜单项，路径:', fullPath);
  console.log('菜单数据示例:', props.menuData.slice(0, 2).map(r => ({
    path: r.path,
    children: r.children?.slice(0, 2).map(c => ({
      path: c.path,
      hasChildren: !!c.children
    }))
  })));
};

defineExpose({ setCurrentMenu });
</script>

<style lang="less">
@import '@/style/components/menu.less';

.side-layout-sider {
  flex: 0 0 200px !important;
  max-width: 200px !important;
  min-width: 200px !important;
  width: 200px !important;
  margin: 0;
  overflow: hidden;
  background: var(--sidebar-background) !important;
  transition: all 0.2s ease;

  &.ant-layout-sider-collapsed,
  &.is-collapsed {
    flex: 0 0 64px !important;
    max-width: 64px !important;
    min-width: 64px !important;
    width: 64px !important;

    .layout-logo {
      justify-content: center;
      padding: 0;
    }

    .layout-logo .logo-img {
      width: 28px;
      height: 28px;
      margin-right: 0;
    }

    .side-menu.ant-menu-inline-collapsed {
      width: 64px;
    }

    .side-menu.ant-menu-inline-collapsed > .ant-menu-item,
    .side-menu.ant-menu-inline-collapsed > .ant-menu-submenu > .ant-menu-submenu-title {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 44px;
      height: 44px;
      margin: 8px auto;
      padding: 0 !important;
      line-height: 44px;
    }

    .side-menu.ant-menu-inline-collapsed .ant-menu-title-content,
    .side-menu.ant-menu-inline-collapsed .ant-menu-submenu-arrow,
    .side-menu.ant-menu-inline-collapsed .external-link-icon {
      display: none !important;
      width: 0 !important;
      margin: 0 !important;
      opacity: 0 !important;
    }

    .side-menu.ant-menu-inline-collapsed .ant-menu-item-icon,
    .side-menu.ant-menu-inline-collapsed .anticon,
    .side-menu.ant-menu-inline-collapsed .svg-icon {
      display: inline-flex !important;
      align-items: center;
      justify-content: center;
      flex: none;
      margin: 0 !important;
      font-size: 16px !important;
      line-height: 1 !important;
      opacity: 1 !important;
      visibility: visible !important;
    }
  }

  /* ========== 暗黑模式 - 高级现代化设计 ========== */
  &.dark-theme {
    /* 步骤1: 使用 Level 2 背景（侧边栏专用） */
    background: var(--sidebar-background) !important;

    /* 步骤2: 细边框增强质感 */
    border: 1px solid var(--border-color);
    box-shadow: 2px 0 12px rgba(0, 0, 0, 0.4);

    .layout-logo {
      background-color: var(--sidebar-background) !important;
      /* 步骤2: 底部细边框分隔 */
      border-bottom: 1px solid var(--border-color);
      padding: 20px 16px;
    }

    // 强制覆盖 Ant Design 的默认暗色菜单背景
    .ant-menu {
      background: var(--sidebar-background) !important;

      &.ant-menu-dark {
        background: var(--sidebar-background) !important;
      }
    }
  }

  &:not(.dark-theme) {
    .layout-logo {
      background-color: var(--sidebar-background);
    }
  }
}

.layout-logo {
  display: flex;
  align-items: center;
  padding: 0 14px;
  background-color: var(--sidebar-background);
  transition: all 0.3s ease;

  .logo-img {
    height: 32px;
    margin-right: 8px;
    transition: transform 0.3s ease;

    &:hover {
      transform: scale(1.05);
    }
  }

  .logo-title {
    /* 步骤3: 一级文字 */
    color: var(--text-primary);
    font-size: 18px;
    font-weight: 600;
    white-space: nowrap;
    opacity: 1;
    transition: all 0.3s;
  }
}

.side-menu {
  height: calc(100% - 64px);
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: rgba(148, 163, 184, 0.45) transparent;
  -ms-overflow-style: auto;

  .ant-menu-item,
  .ant-menu-submenu-title {
    position: relative !important;
    display: flex;
    align-items: center;
    width: calc(100% - 20px);
    height: 42px;
    margin: 4px 10px;
    border-radius: 8px;
    padding: 0 48px 0 14px !important;
  }

  .ant-menu-sub .ant-menu-item,
  .ant-menu-sub .ant-menu-submenu-title {
    width: calc(100% - 28px);
    margin-left: 18px;
    margin-right: 10px;
  }

  .ant-menu-sub .ant-menu-sub .ant-menu-item,
  .ant-menu-sub .ant-menu-sub .ant-menu-submenu-title {
    width: calc(100% - 36px);
    margin-left: 26px;
  }

  .ant-menu-title-content {
    display: flex;
    align-items: center;
    width: 100%;
    min-width: 0;
    overflow: visible !important;
  }

  .menu-title-row {
    display: flex;
    align-items: center;
    flex: 1 1 auto !important;
    width: 100%;
    min-width: 0;
    line-height: 1;
  }

  .menu-title-text {
    display: block;
    flex: 1 1 auto;
    min-width: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .ant-menu-submenu-arrow,
  .external-link-icon {
    position: absolute !important;
    top: 50% !important;
    right: 14px !important;
    display: inline-flex !important;
    align-items: center !important;
    justify-content: center !important;
    flex: none !important;
    width: 20px !important;
    height: 20px !important;
    padding: 0 !important;
    margin: 0 !important;
    border-radius: 0 !important;
    background: transparent !important;
    color: var(--text-tertiary);
    line-height: 1 !important;
    transform: translateY(-50%) !important;
    transition: color 0.3s ease;
  }

  .external-link-icon .anticon,
  .external-link-icon .svg-icon {
    display: inline-flex !important;
    align-items: center !important;
    justify-content: center !important;
    width: 16px !important;
    height: 16px !important;
    padding: 0 !important;
    margin: 0 !important;
    border-radius: 0 !important;
    background: transparent !important;
    color: inherit !important;
    filter: none !important;
    font-size: 16px !important;
    line-height: 1 !important;
  }

  .ant-menu-submenu-arrow::before,
  .ant-menu-submenu-arrow::after {
    top: 50% !important;
    margin: 0 !important;
    transform-origin: center !important;
  }

  /* 优化滚动条样式（Webkit） */
  &::-webkit-scrollbar {
    width: 8px;
    height: 8px;
  }

  &::-webkit-scrollbar-track {
    border-radius: 999px;
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    border: 2px solid transparent;
    border-radius: 999px;
    background: rgba(148, 163, 184, 0.45);
    background-clip: content-box;

    &:hover {
      background: rgba(100, 116, 139, 0.58);
      background-clip: content-box;
    }
  }
}
// 菜单右侧辅助图标由 .side-menu 统一控制对齐

/* ========== 暗黑模式菜单项优化 - 精致线条设计 ========== */
.ant-layout-sider.dark-theme {
  .external-link-icon {
    color: var(--text-tertiary);
  }

  /* 菜单项基础样式 */
  .side-menu .ant-menu-item,
  .side-menu .ant-menu-submenu-title {
    transition: all 0.2s ease;
    position: relative;
    border: none;
    background: transparent !important;

    /* 左侧线条预留位置 */
    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 3px;
      height: 0;
      background: var(--primary-color);
      border-radius: 0 2px 2px 0;
      transition: height 0.2s ease;
    }

    &:hover {
      background: var(--hover-bg) !important;
      color: var(--primary-color) !important;

      .anticon {
        color: var(--primary-color) !important;
      }

      /* 悬停时显示短线条 */
      &::before {
        height: 50%;
      }
    }
  }

  /* 激活菜单项 - 精致线条设计 */
  .side-menu .ant-menu-item-selected {
    /* 使用半透明蓝色背景 */
    background: var(--menu-item-active-bg) !important;
    /* 移除边框和阴影 */
    border: none !important;
    box-shadow: none !important;
    /* 文字使用主题色 */
    color: var(--primary-color) !important;

    /* 左侧 3px 亮蓝色线条 */
    &::before {
      height: 60%;
      background: var(--menu-active-border);
    }

    .anticon {
      color: var(--primary-color) !important;
    }

    /* 悬停时保持样式 */
    &:hover {
      background: rgba(96, 165, 250, 0.15) !important;

      &::before {
        height: 70%;
      }
    }
  }

  /* 子菜单展开状态 */
  .side-menu .ant-menu-submenu-selected {
    > .ant-menu-submenu-title {
      color: var(--primary-color) !important;

      .anticon {
        color: var(--primary-color) !important;
      }
    }
  }

  /* 子菜单内的菜单项 */
  .side-menu .ant-menu-sub {
    background: transparent !important;

    .ant-menu-item {
      padding-left: 14px !important;
    }
  }
}

/* ========== 彩色图标样式 ========== */
.side-layout-sider.colorful-icon {
  .side-menu .ant-menu-item,
  .side-menu .ant-menu-submenu-title {
    --menu-icon-color: #1890ff;
    --menu-icon-bg: rgba(24, 144, 255, 0.18);
    --menu-icon-filter: brightness(0) saturate(100%) invert(47%) sepia(96%) saturate(1743%)
      hue-rotate(194deg) brightness(101%) contrast(101%);
  }

  .side-menu .ant-menu-item > .anticon.ant-menu-item-icon,
  .side-menu .ant-menu-submenu-title > .anticon.ant-menu-item-icon {
    position: relative;
    display: inline-flex !important;
    align-items: center !important;
    justify-content: center !important;
    flex: 0 0 32px;
    width: 32px !important;
    height: 32px !important;
    margin-right: 12px !important;
    border-radius: 8px !important;
    background: var(--menu-icon-bg) !important;
    color: var(--menu-icon-color) !important;
    transition: all 0.3s ease !important;
    font-size: 16px !important;
  }

  .side-menu .ant-menu-item > .svg-icon,
  .side-menu .ant-menu-submenu-title > .svg-icon {
    box-sizing: content-box;
    flex: 0 0 16px;
    width: 16px !important;
    height: 16px !important;
    padding: 8px;
    margin-right: 12px !important;
    border-radius: 8px !important;
    background: var(--menu-icon-bg) !important;
    color: var(--menu-icon-color) !important;
    filter: var(--menu-icon-filter);
    object-fit: contain;
    transition: all 0.3s ease !important;
  }

  .side-menu .ant-menu-submenu-arrow,
  .side-menu .external-link-icon {
    position: absolute !important;
    top: 50% !important;
    right: 14px !important;
    display: inline-flex !important;
    align-items: center !important;
    justify-content: center !important;
    flex: none !important;
    width: 20px !important;
    height: 20px !important;
    padding: 0 !important;
    margin: 0 !important;
    border-radius: 0 !important;
    background: transparent !important;
    color: var(--text-tertiary) !important;
    filter: none !important;
    line-height: 1 !important;
    transform: translateY(-50%) !important;
  }

  .side-menu .ant-menu-item:nth-child(8n + 2),
  .side-menu .ant-menu-submenu:nth-child(8n + 2) > .ant-menu-submenu-title {
    --menu-icon-color: #52c41a;
    --menu-icon-bg: rgba(82, 196, 26, 0.18);
    --menu-icon-filter: brightness(0) saturate(100%) invert(65%) sepia(57%) saturate(466%)
      hue-rotate(73deg) brightness(95%) contrast(87%);
  }

  .side-menu .ant-menu-item:nth-child(8n + 3),
  .side-menu .ant-menu-submenu:nth-child(8n + 3) > .ant-menu-submenu-title {
    --menu-icon-color: #faad14;
    --menu-icon-bg: rgba(250, 173, 20, 0.18);
    --menu-icon-filter: brightness(0) saturate(100%) invert(74%) sepia(65%) saturate(1574%)
      hue-rotate(359deg) brightness(99%) contrast(98%);
  }

  .side-menu .ant-menu-item:nth-child(8n + 4),
  .side-menu .ant-menu-submenu:nth-child(8n + 4) > .ant-menu-submenu-title {
    --menu-icon-color: #f5222d;
    --menu-icon-bg: rgba(245, 34, 45, 0.18);
    --menu-icon-filter: brightness(0) saturate(100%) invert(24%) sepia(95%) saturate(3180%)
      hue-rotate(346deg) brightness(98%) contrast(94%);
  }

  .side-menu .ant-menu-item:nth-child(8n + 5),
  .side-menu .ant-menu-submenu:nth-child(8n + 5) > .ant-menu-submenu-title {
    --menu-icon-color: #722ed1;
    --menu-icon-bg: rgba(114, 46, 209, 0.18);
    --menu-icon-filter: brightness(0) saturate(100%) invert(28%) sepia(73%) saturate(2154%)
      hue-rotate(250deg) brightness(88%) contrast(91%);
  }

  .side-menu .ant-menu-item:nth-child(8n + 6),
  .side-menu .ant-menu-submenu:nth-child(8n + 6) > .ant-menu-submenu-title {
    --menu-icon-color: #13c2c2;
    --menu-icon-bg: rgba(19, 194, 194, 0.18);
    --menu-icon-filter: brightness(0) saturate(100%) invert(59%) sepia(91%) saturate(421%)
      hue-rotate(131deg) brightness(92%) contrast(92%);
  }

  .side-menu .ant-menu-item:nth-child(8n + 7),
  .side-menu .ant-menu-submenu:nth-child(8n + 7) > .ant-menu-submenu-title {
    --menu-icon-color: #eb2f96;
    --menu-icon-bg: rgba(235, 47, 150, 0.18);
    --menu-icon-filter: brightness(0) saturate(100%) invert(32%) sepia(77%) saturate(2856%)
      hue-rotate(306deg) brightness(96%) contrast(92%);
  }

  .side-menu .ant-menu-item:nth-child(8n),
  .side-menu .ant-menu-submenu:nth-child(8n) > .ant-menu-submenu-title {
    --menu-icon-color: #fa8c16;
    --menu-icon-bg: rgba(250, 140, 22, 0.18);
    --menu-icon-filter: brightness(0) saturate(100%) invert(63%) sepia(68%) saturate(1243%)
      hue-rotate(350deg) brightness(100%) contrast(96%);
  }

  .side-menu .ant-menu-item:hover > .anticon.ant-menu-item-icon,
  .side-menu .ant-menu-item:hover > .svg-icon,
  .side-menu .ant-menu-submenu-title:hover > .anticon.ant-menu-item-icon,
  .side-menu .ant-menu-submenu-title:hover > .svg-icon,
  .side-menu .ant-menu-item-selected > .anticon.ant-menu-item-icon,
  .side-menu .ant-menu-item-selected > .svg-icon {
    transform: scale(1.05) !important;
  }
}

.side-layout-sider.ant-layout-sider-collapsed,
.side-layout-sider.is-collapsed {
  .side-menu.ant-menu-inline-collapsed > .ant-menu-item,
  .side-menu.ant-menu-inline-collapsed > .ant-menu-submenu > .ant-menu-submenu-title {
    display: flex !important;
    align-items: center !important;
    justify-content: center !important;
    width: 44px !important;
    margin: 8px auto !important;
    padding: 0 !important;
  }

  .side-menu.ant-menu-inline-collapsed .ant-menu-item-icon,
  .side-menu.ant-menu-inline-collapsed .anticon,
  .side-menu.ant-menu-inline-collapsed .svg-icon {
    display: inline-flex !important;
    align-items: center !important;
    justify-content: center !important;
    flex: none !important;
    margin: 0 !important;
    opacity: 1 !important;
    visibility: visible !important;
  }

  &.colorful-icon {
    .side-menu.ant-menu-inline-collapsed .anticon {
      flex: 0 0 32px !important;
      width: 32px !important;
      height: 32px !important;
    }

    .side-menu.ant-menu-inline-collapsed .svg-icon {
      flex: 0 0 16px !important;
      width: 16px !important;
      height: 16px !important;
      padding: 8px !important;
    }
  }
}
</style>
