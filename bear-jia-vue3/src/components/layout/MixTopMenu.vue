<template>
  <div class="mix-top-menu">
    <a-menu
      mode="horizontal"
      :selectedKeys="selectedKeys"
      @select="handleMenuSelect"
      class="mix-top-menu-container"
    >
      <!-- 工作台菜单 -->
      <a-menu-item key="workbench" @click="handleWorkbenchClick">
        <template #icon>
          <BearJiaIcon icon="HomeOutlined" />
        </template>
        工作台
      </a-menu-item>

      <!-- 一级菜单 - 只显示一级，不显示下拉 -->
      <a-menu-item
        v-for="(router, index) in menuData"
        :key="`${router.path}-${index}`"
        @click="handleFirstLevelMenuClick(router)"
      >
        <template #icon>
          <BearJiaIcon :icon="router.meta?.icon || 'MenuOutlined'" />
        </template>
        {{ router.meta?.title }}
      </a-menu-item>
    </a-menu>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { BearJiaIcon } from '@/utils/BearJiaIcon.js';

const props = defineProps({
  menuData: {
    type: Array,
    default: () => []
  },
  currentFirstLevelMenu: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['firstLevelMenuSelect', 'menuSelect']);

// 菜单状态
const selectedKeys = ref(['workbench']); // 默认选中工作台

// 监听当前一级菜单变化
watch(() => props.currentFirstLevelMenu, (newValue) => {
  if (newValue) {
    const menuIndex = props.menuData.findIndex(item => item.path === newValue.path);
    if (menuIndex !== -1) {
      selectedKeys.value = [`${newValue.path}-${menuIndex}`];
    }
  } else {
    selectedKeys.value = ['workbench'];
  }
}, { immediate: true });

// 设置当前激活菜单（用于页面刷新后恢复状态）
const setCurrentMenu = (fullPath) => {
  console.log('MixTopMenu.setCurrentMenu 被调用，路径:', fullPath);

  if (!props.menuData || props.menuData.length === 0) {
    console.log('MixTopMenu 菜单数据为空');
    return;
  }

  // 检查是否是工作台
  if (fullPath === '/home/workbench') {
    selectedKeys.value = ['workbench'];
    console.log('MixTopMenu 设置工作台激活');
    return;
  }

  // 查找路径所属的一级菜单
  for (const [index, router] of props.menuData.entries()) {
    if (router.children) {
      for (const child of router.children) {
        const childFullPath = `${router.path}/${child.path}`;

        // 检查二级菜单
        if (child.path === fullPath || childFullPath === fullPath) {
          selectedKeys.value = [`${router.path}-${index}`];
          console.log('MixTopMenu 找到二级菜单，激活一级菜单');
          return;
        }

        // 检查三级菜单
        if (child.children && child.children.length > 0) {
          for (const threeLevel of child.children) {
            const threeFullPath = `${router.path}/${threeLevel.path}`;

            if (threeLevel.path === fullPath || threeFullPath === fullPath) {
              selectedKeys.value = [`${router.path}-${index}`];
              console.log('MixTopMenu 找到三级菜单，激活一级菜单');
              return;
            }
          }
        }
      }
    }
  }

  console.log('MixTopMenu 未找到匹配的菜单项');
};

defineExpose({ setCurrentMenu });

// 处理菜单选择
const handleMenuSelect = ({ key }) => {
  selectedKeys.value = [key];
};

// 处理工作台点击
const handleWorkbenchClick = () => {
  selectedKeys.value = ['workbench'];

  emit('menuSelect', {
    fatherName: 'HomePage',
    fatherPath: '/home',
    fatherTitle: '主页',
    name: 'Workbench',
    path: 'workbench',
    title: '工作台',
    component: null
  });

  // 清空一级菜单选择
  emit('firstLevelMenuSelect', null);
};

// 处理一级菜单点击
const handleFirstLevelMenuClick = (router) => {
  try {
    console.log('混合模式一级菜单点击:', router);

    const menuIndex = props.menuData.findIndex(item => item.path === router.path);
    selectedKeys.value = [`${router.path}-${menuIndex}`];

    // 通知父组件一级菜单选择变化
    emit('firstLevelMenuSelect', router);
  } catch (error) {
    console.error('一级菜单点击错误:', error);
  }
};
</script>

<style scoped>
.mix-top-menu {
  flex: 1;
  display: flex;
  justify-content: flex-start;
  overflow-x: auto;
  overflow-y: hidden;
  max-width: 100%;
}

/* 隐藏滚动条但保持滚动功能 */
.mix-top-menu::-webkit-scrollbar {
  height: 0;
  display: none;
}

.mix-top-menu-container {
  border-bottom: none;
  background: transparent;
  line-height: 56px;
  height: 56px;
  flex-wrap: nowrap;
  white-space: nowrap;
}

.mix-top-menu-container .ant-menu-item {
  border-bottom: none;
  height: 56px;
  line-height: 56px;
}

.mix-top-menu-container .ant-menu-item:hover {
  color: var(--primary-color);
}

.mix-top-menu-container .ant-menu-item-selected {
  color: var(--primary-color);
  border-bottom: 2px solid var(--primary-color);
}
</style>

<style>
/* 禁用 Ant Design Menu 的 overflow 折叠功能 */
.mix-top-menu .ant-menu-overflow {
  flex-wrap: nowrap !important;
}

/* 隐藏更多菜单的 ... 按钮 */
.mix-top-menu .ant-menu-overflow-item-rest {
  display: none !important;
}

/* 确保所有菜单项正常显示，不被隐藏 */
.mix-top-menu .ant-menu-overflow-item {
  flex: none !important;
  opacity: 1 !important;
  visibility: visible !important;
  position: relative !important;
  pointer-events: auto !important;
}

/* 确保被标记为隐藏的菜单项也能显示 */
.mix-top-menu .ant-menu-overflow-item-hidden {
  display: inline-flex !important;
  opacity: 1 !important;
  visibility: visible !important;
  width: auto !important;
  height: auto !important;
  position: relative !important;
  overflow: visible !important;
}

/* 让菜单项更紧凑 */
.mix-top-menu .ant-menu-item {
  padding: 0 12px !important;
  margin: 0 2px !important;
}

.mix-top-menu .ant-menu-title-content {
  margin-left: 4px !important;
}
</style>
