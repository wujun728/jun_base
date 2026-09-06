<template>
  <div class="top-menu">
    <a-menu
      mode="horizontal"
      :selectedKeys="selectedKeys"
      :openKeys="openKeys"
      :disabledOverflow="true"
      @select="handleMenuSelect"
      @openChange="handleOpenChange"
      class="top-menu-container"
    >
      <!-- 工作台菜单 -->
      <a-menu-item key="workbench" @click="handleWorkbenchClick">
        <template #icon>
          <BearJiaIcon icon="HomeOutlined" />
        </template>
        工作台
      </a-menu-item>

      <!-- 动态菜单 - 参考 SideMenu 写法 -->
      <template v-for="(router, index) in menuData">
        <!-- 有多个子菜单或设置了 alwaysShow 的菜单，显示为下拉菜单 -->
        <a-sub-menu
          v-if="router.children && (router.children.length > 1 || (router.alwaysShow && router.children.length === 1))"
          :key="`${router.path}-${index}`"
        >
          <template #icon>
            <BearJiaIcon :icon="router.meta?.icon || 'MenuOutlined'" />
          </template>
          <template #title>{{ router.meta?.title }}</template>

          <template v-for="(children, childIndex) in router.children" :key="`${children.path}-${childIndex}`">
          <!-- 有三级菜单的二级菜单 -->
          <a-sub-menu
            v-if="children.children"
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
              @click="handleThreeLevelMenuClick(router.path, children, threeLevelChildren)"
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
            :key="`${children.path}-${childIndex}`"
            @click="handleMenuClick(router.name, router.path, router.meta?.title, children.name, children.path, children.meta?.title, children.component)"
          >
            <template #icon>
              <BearJiaIcon :icon="children.meta?.icon || 'BarsOutlined'" />
            </template>
            {{ children.meta?.title }}
          </a-menu-item>
          </template>
        </a-sub-menu>

        <!-- 只有一个子菜单且没有设置 alwaysShow 的菜单，直接显示为单个菜单项 -->
        <a-menu-item
          v-else-if="router.children && router.children.length === 1 && !router.alwaysShow"
          :key="`single-${router.path}-${index}`"
          @click="handleMenuClick(router.name, router.path, router.meta?.title, router.children[0].name, router.children[0].path, router.children[0].meta?.title, router.children[0].component)"
        >
          <template #icon>
            <BearJiaIcon :icon="router.children[0].meta?.icon || router.meta?.icon || 'MenuOutlined'" />
          </template>
          {{ router.children[0].meta?.title }}
        </a-menu-item>
      </template>
    </a-menu>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { BearJiaIcon } from '@/utils/BearJiaIcon.js';

const props = defineProps({
  menuData: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['menuSelect']);

// 菜单状态
const selectedKeys = ref([]);
const openKeys = ref([]);

// 处理菜单选择
const handleMenuSelect = ({ key }) => {
  selectedKeys.value = [key];
};

// 处理子菜单展开/收起
const handleOpenChange = (keys) => {
  openKeys.value = keys;
};

// 设置当前激活菜单
const setCurrentMenu = (fullPath) => {
  console.log('TopMenu.setCurrentMenu 被调用，路径:', fullPath);

  if (!props.menuData || props.menuData.length === 0) {
    console.log('TopMenu 菜单数据为空');
    return;
  }

  // 检查是否是工作台
  if (fullPath === '/home/workbench') {
    selectedKeys.value = ['workbench'];
    openKeys.value = [];
    console.log('TopMenu 设置工作台激活');
    return;
  }

  // 遍历菜单查找匹配的路径
  for (const [index, router] of props.menuData.entries()) {
    if (router.children) {
      for (const [childIndex, child] of router.children.entries()) {
        const childFullPath = `${router.path}/${child.path}`;

        // 检查三级菜单
        if (child.children && child.children.length > 0) {
          for (const [threeIndex, threeLevel] of child.children.entries()) {
            const threeFullPath = `${router.path}/${threeLevel.path}`;

            if (threeLevel.path === fullPath || threeFullPath === fullPath) {
              selectedKeys.value = [`${threeLevel.path}-${threeIndex}`];
              openKeys.value = [`${router.path}-${index}`, `${child.path}-${childIndex}`];
              console.log('TopMenu 找到三级菜单');
              return;
            }
          }
        } else {
          // 检查二级菜单
          if (child.path === fullPath || childFullPath === fullPath) {
            selectedKeys.value = [`${child.path}-${childIndex}`];
            openKeys.value = [`${router.path}-${index}`];
            console.log('TopMenu 找到二级菜单');
            return;
          }
        }
      }
    }
  }

  console.log('TopMenu 未找到匹配的菜单项');
};

defineExpose({ setCurrentMenu });

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
};

// 处理菜单点击
const handleMenuClick = (fatherName, fatherPath, fatherTitle, name, path, title, component) => {
  try {
    console.log('TopMenu 菜单点击:', { fatherName, fatherPath, fatherTitle, name, path, title });

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
    console.log('TopMenu 三级菜单点击:', { greatFatherPath, father, menu });

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

<style scoped>
.top-menu {
  flex: 1;
  display: flex;
  justify-content: flex-start;
  overflow-x: auto;
  overflow-y: hidden;
  max-width: 100%;
}

/* 隐藏滚动条但保持滚动功能 */
.top-menu::-webkit-scrollbar {
  height: 0;
  display: none;
}

.top-menu-container {
  border-bottom: none;
  background: transparent;
  line-height: 56px;
  height: 56px;
  flex-wrap: nowrap;
  white-space: nowrap;
}

.top-menu-container :deep(.ant-menu-item),
.top-menu-container :deep(.ant-menu-submenu) {
  border-bottom: none;
  height: 56px;
  line-height: 56px;
}

.top-menu-container :deep(.ant-menu-item:hover),
.top-menu-container :deep(.ant-menu-submenu:hover) {
  color: var(--primary-color);
}

.top-menu-container :deep(.ant-menu-item-selected) {
  color: var(--primary-color);
  border-bottom: 2px solid var(--primary-color);
}

.top-menu-container :deep(.ant-menu-submenu-title:hover) {
  color: var(--primary-color);
}

/* 禁用菜单溢出折叠 */
.top-menu-container :deep(.ant-menu-overflow) {
  flex-wrap: nowrap;
}

/* 隐藏更多菜单按钮 */
.top-menu-container :deep(.ant-menu-overflow-item-rest) {
  display: none;
}

/* 确保溢出的菜单项显示 */
.top-menu-container :deep(.ant-menu-overflow-item) {
  flex: none;
  opacity: 1;
  visibility: visible;
  order: 0;
}

/* 让菜单项更紧凑 */
.top-menu-container :deep(.ant-menu-item) {
  padding: 0 8px;
}

.top-menu-container :deep(.ant-menu-submenu-title) {
  padding: 0 8px;
}

.top-menu-container :deep(.ant-menu-title-content) {
  margin-left: 4px;
}
</style>
