<template>
  <a-layout-sider
    :theme="layoutSettings.darkMode ? 'dark' : 'light'"
    :collapsed="collapsed"
    :trigger="null"
    collapsible
    :class="{
      'dark-theme': layoutSettings.darkMode,
      'mix-side-menu': true
    }"
    @collapse="$emit('update:collapsed', $event)"
  >
    <!-- 当前一级菜单的子菜单 -->
    <a-menu
      :theme="layoutSettings.darkMode ? 'dark' : 'light'"
      :selectedKeys="selectedKeys"
      :openKeys="openKeys"
      mode="inline"
      @select="handleMenuSelect"
      @openChange="handleOpenChange"
      class="mix-side-menu-container"
    >
      <!-- 显示当前一级菜单的子菜单 -->
      <template v-if="currentMenuChildren && currentMenuChildren.length > 0">
        <template v-for="(children, childIndex) in currentMenuChildren" :key="`${children.path}-${childIndex}`">
          <!-- 有多个三级菜单或设置了 alwaysShow 的二级菜单 -->
          <a-sub-menu
            v-if="children.children && (children.children.length > 1 || (children.alwaysShow && children.children.length === 1))"
            :key="`${children.path}-${childIndex}`"
          >
            <template #icon>
              <BearJiaIcon :icon="children.meta?.icon || 'AppstoreOutlined'" />
            </template>
            <template #title>{{ children.meta?.title }}</template>

            <!-- 三级菜单 -->
            <a-menu-item
              v-for="(threeLevelChildren, threeIndex) in children.children"
              :key="`${threeLevelChildren?.path}-${threeIndex}`"
              @click="handleThreeLevelMenuClick(currentFirstLevelMenu?.path, children, threeLevelChildren)"
            >
              <template #icon>
                <BearJiaIcon :icon="threeLevelChildren.meta?.icon || 'BarsOutlined'" />
              </template>
              {{ threeLevelChildren.meta?.title }}
            </a-menu-item>
          </a-sub-menu>

          <!-- 只有一个三级菜单且设置了 alwaysShow 的二级菜单 -->
          <a-sub-menu
            v-else-if="children.children && children.children.length === 1 && children.alwaysShow"
            :key="`${children.path}-${childIndex}`"
          >
            <template #icon>
              <BearJiaIcon :icon="children.meta?.icon || 'AppstoreOutlined'" />
            </template>
            <template #title>{{ children.meta?.title }}</template>

            <!-- 三级菜单 -->
            <a-menu-item
              v-for="(threeLevelChildren, threeIndex) in children.children"
              :key="`${threeLevelChildren?.path}-${threeIndex}`"
              @click="handleThreeLevelMenuClick(currentFirstLevelMenu?.path, children, threeLevelChildren)"
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
            @click="handleMenuClick(currentFirstLevelMenu?.name, currentFirstLevelMenu?.path, currentFirstLevelMenu?.meta?.title, children.name, children.path, children.meta?.title, children.component)"
          >
            <template #icon>
              <BearJiaIcon :icon="children.meta?.icon || 'BarsOutlined'" />
            </template>
            {{ children.meta?.title }}
          </a-menu-item>
        </template>
      </template>

      <!-- 没有选中一级菜单时的提示 -->
      <div v-else class="no-menu-tip">
        <p>请选择顶部菜单</p>
      </div>
    </a-menu>
  </a-layout-sider>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { BearJiaIcon } from '@/utils/BearJiaIcon.js';
import { isExternal } from '@/utils/bearjia.js';

const props = defineProps({
  collapsed: Boolean,
  layoutSettings: Object,
  currentFirstLevelMenu: Object, // 当前选中的一级菜单
});

const emit = defineEmits(['update:collapsed', 'menuSelect']);

// 菜单状态
const selectedKeys = ref([]);
const openKeys = ref([]);

// 计算当前一级菜单的子菜单
const currentMenuChildren = computed(() => {
  return props.currentFirstLevelMenu?.children || [];
});

// 监听一级菜单变化，重置选中状态
watch(() => props.currentFirstLevelMenu, (newMenu) => {
  selectedKeys.value = [];
  openKeys.value = [];
  console.log('混合模式侧边栏菜单数据更新:', newMenu?.meta?.title, newMenu?.children?.length);
}, { immediate: true });


const setCurrentMenu = (fullPath) => {
  console.log('MixSideMenu.setCurrentMenu 被调用，路径:', fullPath);

  if (!currentMenuChildren.value || currentMenuChildren.value.length === 0) {
    console.log('MixSideMenu 菜单数据为空');
    return;
  }

  const firstLevelPath = props.currentFirstLevelMenu?.path;

  for (const [childIndex, child] of currentMenuChildren.value.entries()) {
    // 存在三级菜单
    if (child.children && child.children.length > 0) {
      for (const [threeIndex, threeLevel] of child.children.entries()) {
        const threeFullPath = firstLevelPath ? `${firstLevelPath}/${threeLevel.path}` : threeLevel.path;

        if (threeLevel.path === fullPath || threeFullPath === fullPath) {
          openKeys.value = [`${child.path}-${childIndex}`];
          selectedKeys.value = [`${threeLevel.path}-${threeIndex}`];
          console.log('MixSideMenu 找到三级菜单');
          return;
        }
      }
    } else {
      // 只有二级菜单
      const childFullPath = firstLevelPath ? `${firstLevelPath}/${child.path}` : child.path;

      if (child.path === fullPath || childFullPath === fullPath) {
        selectedKeys.value = [`single-${child.path}-${childIndex}`];
        openKeys.value = [];
        console.log('MixSideMenu 找到二级菜单');
        return;
      }
    }
  }

  console.log('MixSideMenu 未找到匹配的菜单项');
};


defineExpose({ setCurrentMenu });

// 处理菜单选择
const handleMenuSelect = ({ key }) => {
  selectedKeys.value = [key];
};

// 处理子菜单展开/收起
const handleOpenChange = (keys) => {
  openKeys.value = keys;
};

// 处理二级菜单点击
const handleMenuClick = (fatherName, fatherPath, fatherTitle, name, path, title, component) => {
  try {
    console.log('混合模式二级菜单点击:', { fatherName, fatherPath, fatherTitle, name, path, title });

    // 检查是否是外链
    if (isExternal(path)) {
      // 外链直接在新窗口打开，不触发路由跳转
      window.open(path, '_blank');
      return;
    }

    emit('menuSelect', {
      fatherName,
      fatherPath,
      fatherTitle,
      name,
      path,
      title,
      component
    });
  } catch (error) {
    console.error('菜单点击错误:', error);
  }
};

// 处理三级菜单点击
const handleThreeLevelMenuClick = (greatFatherPath, father, menu) => {
  try {
    console.log('混合模式三级菜单点击:', { greatFatherPath, father, menu });

    // 检查是否是外链
    if (isExternal(menu.path)) {
      // 外链直接在新窗口打开，不触发路由跳转
      window.open(menu.path, '_blank');
      return;
    }

    emit('menuSelect', {
      greatFatherPath,
      father,
      menu
    });
  } catch (error) {
    console.error('三级菜单点击错误:', error);
  }
};
</script>

<style lang="less">
.mix-side-menu {
  margin-top: 0;
  height: 100%;
  background: var(--sidebar-background) !important;

  :deep(.ant-layout-sider) {
    background: var(--sidebar-background) !important;
  }

  // 暗色主题下的强制覆盖
  &.dark-theme {
    background: var(--sidebar-background) !important;

    :deep(.ant-layout-sider) {
      background: var(--sidebar-background) !important;
    }

    .ant-menu {
      background: var(--sidebar-background) !important;

      &.ant-menu-dark {
        background: var(--sidebar-background) !important;
      }
    }
  }
}

.mix-side-menu-container {
  height: 100%;
  border-right: none;
  background: var(--sidebar-background) !important;

  &:deep(.ant-menu) {
    background: var(--sidebar-background) !important;
  }
}

.no-menu-tip {
  padding: 20px;
  text-align: center;
  color: var(--text-secondary);

  p {
    margin: 0;
    font-size: 14px;
    color: var(--text-secondary);
  }
}
</style>
