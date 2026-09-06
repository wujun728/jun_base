<template>
  <div class="history-nav">
    <!-- 左侧切换按钮 -->
    <div
      v-show="showLeftButton"
      class="nav-button nav-button-left"
      @click="scrollLeft"
    >
      <left-outlined />
    </div>

    <div class="history-tags" ref="tagsContainer" @scroll="handleScroll">
      <a-tag
        v-for="(item, index) in historyList"
        :key="index"
        :closable="item.path !== '/home/workbench'"
        @close.prevent="removeHistory(index)"
        :class="{ active: currentPath === item.path }"
        @click="handleClick(item)"
      >
        {{ item.title }}
        <a-dropdown v-if="index === activeIndex" :trigger="['hover']">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="refreshPage">
                <reload-outlined /> 刷新页面
              </a-menu-item>
              <a-menu-item key="2" @click="closeCurrentPage">
                <close-outlined /> 关闭页面
              </a-menu-item>
              <a-menu-item key="3" @click="closeOtherPages">
                <minus-outlined /> 关闭其他
              </a-menu-item>
              <a-menu-item key="4" @click="closeAllPages">
                <close-circle-outlined /> 关闭所有
              </a-menu-item>
            </a-menu>
          </template>
          <down-outlined class="dropdown-icon" />
        </a-dropdown>
      </a-tag>
    </div>

    <!-- 右侧切换按钮 -->
    <div
      v-show="showRightButton"
      class="nav-button nav-button-right"
      @click="scrollRight"
    >
      <right-outlined />
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, nextTick, onBeforeUnmount } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { message } from 'ant-design-vue';
import { useAppStore } from '@/stores/app';
import { usePermissionStore } from '@/stores/permission';
import {
  ReloadOutlined,
  CloseOutlined,
  MinusOutlined,
  CloseCircleOutlined,
  DownOutlined,
  LeftOutlined,
  RightOutlined
} from '@ant-design/icons-vue';

const router = useRouter();
const route = useRoute();
const appStore = useAppStore();
const permissionStore = usePermissionStore();

// 历史记录列表，使用ref来保持响应性
const historyList = ref([]);
const MAX_HISTORY = 20; // 增加最大历史记录数
const activeIndex = ref(-1); // 当前激活的标签索引
const tagsContainer = ref(null); // 标签容器引用
const showLeftButton = ref(false); // 显示左侧按钮
const showRightButton = ref(false); // 显示右侧按钮

// 获取当前路径
const currentPath = ref(route.path);

// 标准化路径，移除末尾斜杠
const normalizePath = (path) => {
  if (!path) return '';
  return path.endsWith('/') && path.length > 1 ? path.slice(0, -1) : path;
};

// 从store获取菜单名称
const getMenuTitle = async (path) => {
  // 标准化路径
  const normalizedPath = normalizePath(path);

  // 优先从路由配置中获取标题（包括静态路由和动态路由）
  const allRoutes = router.getRoutes();
  const matchedRoute = allRoutes.find(r => normalizePath(r.path) === normalizedPath);
  if (matchedRoute?.meta?.title) {
    return matchedRoute.meta.title;
  }

  let menuList = permissionStore.sidebarRouters || [];

  // 如果menuList为空，等待一段时间后重试
  let retryCount = 0;
  while (menuList.length === 0 && retryCount < 5) {
    await new Promise(resolve => setTimeout(resolve, 300));
    menuList = permissionStore.sidebarRouters || [];
    retryCount++;
  }

  const findMenuTitle = (menus) => {
    for (const menu of menus) {
      if (menu.path === path) return menu.meta?.title || menu.name;
      if (menu.children) {
        const title = findMenuTitle(menu.children);
        if (title) return title;
      }
    }
    return null;
  };

  // 优先使用菜单中的标题，然后是路由meta中的标题，最后才是未知页面
  const menuTitle = findMenuTitle(menuList);
  if (menuTitle) return menuTitle;

  // 检查当前路由meta信息（用于动态路由等情况）
  if (route.meta?.title) return route.meta.title;

  // 如果菜单还没加载完成，返回加载中状态
  if (menuList.length === 0) return '加载中...';

  return '未知页面';
};

// 检查是否需要显示切换按钮
const checkScrollButtons = async () => {
  await nextTick();
  if (!tagsContainer.value) return;

  const { scrollLeft, scrollWidth, clientWidth } = tagsContainer.value;
  showLeftButton.value = scrollLeft > 0;
  showRightButton.value = scrollLeft < scrollWidth - clientWidth - 1;
};

// 处理滚动事件
const handleScroll = () => {
  checkScrollButtons();
};

// 向左滚动
const scrollLeft = () => {
  if (!tagsContainer.value) return;
  const scrollAmount = 200;
  tagsContainer.value.scrollBy({
    left: -scrollAmount,
    behavior: 'smooth'
  });
};

// 向右滚动
const scrollRight = () => {
  if (!tagsContainer.value) return;
  const scrollAmount = 200;
  tagsContainer.value.scrollBy({
    left: scrollAmount,
    behavior: 'smooth'
  });
};

// 滚动到激活标签
const scrollToActiveTag = async () => {
  await nextTick();
  if (!tagsContainer.value || activeIndex.value === -1) return;

  const activeTag = tagsContainer.value.children[activeIndex.value];
  if (!activeTag) return;

  const containerRect = tagsContainer.value.getBoundingClientRect();
  const tagRect = activeTag.getBoundingClientRect();

  if (tagRect.left < containerRect.left || tagRect.right > containerRect.right) {
    activeTag.scrollIntoView({
      behavior: 'smooth',
      block: 'nearest',
      inline: 'center'
    });
  }
};

// 窗口大小变化处理
const handleResize = () => {
  checkScrollButtons();
};

// 添加历史记录
const addHistory = async (item) => {
  try {
    // 检查是否已存在相同路径
    const index = historyList.value.findIndex(h => h.path === item.path);

    // 如果存在，先删除旧的
    if (index > -1) {
      historyList.value.splice(index, 1);
    }

    // 如果是工作台，确保它始终在第一位
    if (item.path === '/home/workbench') {
      historyList.value.unshift(item);
    } else {
      // 非工作台页面，插入到工作台后面
      const workbenchIndex = historyList.value.findIndex(h => h.path === '/home/workbench');
      if (workbenchIndex === -1) {
        // 如果没有工作台，先添加工作台
        historyList.value.unshift({
          path: '/home/workbench',
          title: '工作台'
        });
        historyList.value.push(item);
      } else {
        historyList.value.splice(workbenchIndex + 1, 0, item);
      }
    }

    // 限制历史记录数量
    if (historyList.value.length > MAX_HISTORY) {
      // 保留工作台和最近的记录
      const workbenchItem = historyList.value.find(h => h.path === '/home/workbench');
      historyList.value = [
        workbenchItem,
        ...historyList.value.filter(h => h.path !== '/home/workbench').slice(0, MAX_HISTORY - 1)
      ];
    }

    // 保存到localStorage
    localStorage.setItem('historyList', JSON.stringify(historyList.value));

    // 检查滚动按钮状态
    await checkScrollButtons();
  } catch (error) {
    console.error('添加历史记录失败:', error);
  }
};

// 监听路由变化
watch(
  () => route.path,
  async (newPath) => {
    if (!newPath) return;

    // 获取菜单标题
    const title = await getMenuTitle(newPath);

    // 只有当标题不是"加载中..."时才添加历史记录
    if (title !== '加载中...') {
      // 添加新的历史记录
      await addHistory({
        path: newPath,
        title: title
      });

      // 更新当前路径和激活索引
      currentPath.value = newPath;
      activeIndex.value = historyList.value.findIndex(item => item.path === newPath);
    }
  },
  { immediate: true }
);

// 监听历史列表变化，更新按钮状态
watch(
  () => historyList.value.length,
  async () => {
    await nextTick();
    await checkScrollButtons();
  }
);

// 处理点击事件
const handleClick = async (item) => {
  try {
    if (item.path === currentPath.value) return;
    await router.push(item.path);
  } catch (error) {
    console.error('页面跳转失败:', error);
    message.error('页面跳转失败');
  }
};

// 移除历史记录
const removeHistory = async (index) => {
  try {
    const removedItem = historyList.value[index];

    // 不允许删除工作台标签
    if (removedItem.path === '/home/workbench') {
      return;
    }

    historyList.value.splice(index, 1);

    // 更新localStorage
    localStorage.setItem('historyList', JSON.stringify(historyList.value));

    // 检查滚动按钮状态
    await checkScrollButtons();

    // 如果删除的是当前页面，则跳转到最近的历史记录
    if (currentPath.value === removedItem.path) {
      const targetPath = index === 0 ? historyList.value[0].path : historyList.value[index - 1].path;
      await router.push(targetPath);
    }
  } catch (error) {
    console.error('删除历史记录失败:', error);
    message.error('删除历史记录失败');
  }
};

// 刷新当前页面
const refreshPage = () => {
  router.go(0);
};

// 关闭当前页面
const closeCurrentPage = async () => {
  if (activeIndex.value > -1 && historyList.value[activeIndex.value].path !== '/home/workbench') {
    await removeHistory(activeIndex.value);
  }
};

// 关闭其他页面
const closeOtherPages = () => {
  const currentItem = historyList.value[activeIndex.value];
  const workbenchItem = historyList.value.find(item => item.path === '/home/workbench');

  historyList.value = [workbenchItem];
  if (currentItem.path !== '/home/workbench') {
    historyList.value.push(currentItem);
  }

  localStorage.setItem('historyList', JSON.stringify(historyList.value));
};

// 关闭所有页面
const closeAllPages = async () => {
  historyList.value = [{
    path: '/home/workbench',
    title: '工作台'
  }];
  localStorage.setItem('historyList', JSON.stringify(historyList.value));
  if (currentPath.value !== '/home/workbench') {
    await router.push('/home/workbench');
  }
};

// 组件挂载时初始化历史记录
onMounted(async () => {
  try {
    // 初始化工作台标签
    historyList.value = [{
      path: '/home/workbench',
      title: '工作台'
    }];

    // 如果当前路径不是工作台，添加当前页面
    if (route.path !== '/home/workbench') {
      const title = await getMenuTitle(route.path);
      await addHistory({
        path: route.path,
        title: title
      });
    }

    // 更新当前路径和激活索引
    currentPath.value = route.path;
    activeIndex.value = historyList.value.findIndex(item => item.path === route.path);

    // 保存到localStorage
    localStorage.setItem('historyList', JSON.stringify(historyList.value));

    // 添加窗口大小变化监听
    window.addEventListener('resize', handleResize);

    // 延迟检查按钮状态，确保 DOM 完全渲染
    setTimeout(async () => {
      await checkScrollButtons();
    }, 100);

    // 检查滚动按钮状态
    await checkScrollButtons();
    // 滚动到激活标签
    await scrollToActiveTag();
  } catch (error) {
    console.error('初始化历史记录失败:', error);
  }
});

// 组件卸载时清理事件监听
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>
.history-nav {
  height: 34px;
  padding: 0 12px;
  display: flex;
  align-items: center;
}

.nav-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 26px;
  height: 26px;
  background: var(--component-background);
  border: 1px solid var(--border-color-base);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
  color: var(--text-secondary);
  font-size: 14px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  transform: translateY(-2px);

  &:hover {
    background: var(--primary-color);
    color: #fff;
    border-color: var(--primary-color);
    box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
  }
}

.nav-button-left {
  margin-right: 6px;
}

.nav-button-right {
  margin-left: 6px;
}

.history-tags {
  display: flex;
  flex-wrap: nowrap;
  gap: 4px;
  align-items: center;
  flex: 1;
  overflow-x: auto;
  overflow-y: hidden;
  padding-top: 0;
  padding-bottom: 0;
  position: relative;
  scroll-behavior: smooth;

  /* 隐藏滚动条 */
  scrollbar-width: none;
  -ms-overflow-style: none;

  &::-webkit-scrollbar {
    display: none;
  }
}

.history-tags :deep(.ant-tag) {
  margin: 0;
  padding: 2px 9px;
  cursor: pointer;
  user-select: none;
  transition: all 0.3s;
  border-radius: 6px;
  font-size: 14px;
  display: inline-flex;
  align-items: center;
  white-space: nowrap;
  background: var(--component-background);
  border: 1px solid var(--border-color-base);
  color: var(--text-primary);
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  min-width: 60px;
  flex-shrink: 0;
}

.history-tags :deep(.ant-tag.active) {
  color: var(--primary-color) !important;
  background: color-mix(in srgb, var(--primary-color) 10%, transparent) !important;
  border-color: color-mix(in srgb, var(--primary-color) 38%, transparent) !important;
  box-shadow: 0 0 0 1px color-mix(in srgb, var(--primary-color) 16%, transparent) !important;
}

.history-tags :deep(.ant-tag:hover) {
  color: var(--primary-color);
  border-color: color-mix(in srgb, var(--primary-color) 45%, transparent);
  /* background: var(--primary-1); */
}

.history-tags :deep(.anticon-close) {
  margin-left: 4px;
  font-size: 12px;
  vertical-align: middle;
  opacity: 0.6;
  transition: opacity 0.3s;
}

.history-tags :deep(.ant-tag:hover .anticon-close) {
  opacity: 1;
}

.dropdown-icon {
  margin-left: 4px;
  font-size: 12px;
  opacity: 0.6;
  transition: opacity 0.3s;
}

.history-tags :deep(.ant-tag:hover .dropdown-icon) {
  opacity: 1;
}

:deep(.ant-dropdown-menu-item) {
  min-width: 120px;
  color: var(--text-primary);
}

/* 暗黑模式适配 */
:global(.dark-theme) .nav-button {
  background: var(--component-background);
  border-color: var(--border-color-base);
  color: var(--text-secondary);

  &:hover {
    background: var(--primary-color);
    color: #fff;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .history-tags :deep(.ant-tag) {
    max-width: 120px;
    min-width: 50px;
    font-size: 12px;
    padding: 2px 6px;
  }

  .nav-button {
    width: 30px;
    height: 30px;
    font-size: 12px;
  }
}
</style>
