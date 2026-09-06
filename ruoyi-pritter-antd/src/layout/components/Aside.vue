<script setup>
import { storeToRefs } from 'pinia'
import { useConfig } from '@/store/modules/layout'
import useUserStore from '@/store/modules/user'
import MenuVertical from '../menus/MenuVertical.vue'
import Logo from './Logo.vue'

defineOptions({ name: 'LayoutAside' })

const config = useConfig()
const userStore = useUserStore()
const { nickName, name, avatar } = storeToRefs(userStore)

const siderCollapsedWidth = computed(() => config.menuWidth())
</script>

<template>
  <a-layout-sider
    class="layout-aside"
    :class="{ shrink: config.layout.shrink }"
    :width="config.layout.menuWidth"
    :collapsed-width="siderCollapsedWidth"
    :collapsed="config.layout.menuCollapse"
    :trigger="null"
  >
    <div class="sidebar-user" v-if="!config.layout.menuCollapse">
      <a-avatar :size="36" :src="avatar">
        <template #icon>
          <span>{{ (nickName || name || 'U').charAt(0) }}</span>
        </template>
      </a-avatar>
      <span class="sidebar-user-name">{{ nickName || name || '用户' }}</span>
    </div>

    <div class="sidebar-user-collapsed" v-else>
      <a-avatar :size="32" :src="avatar">
        <template #icon>
          <span>{{ (nickName || name || 'U').charAt(0) }}</span>
        </template>
      </a-avatar>
    </div>

    <MenuVertical />
    <Logo />
  </a-layout-sider>
</template>

<style scoped lang="scss">
.layout-aside {
  display: flex;
  flex-direction: column;
  background: var(--layout-menu-bg);
  border-right: 1px solid var(--ba-border-color);
  height: 100vh;
  overflow: hidden;
  user-select: none;

  :deep(.ant-layout-sider-children) {
    display: flex;
    flex-direction: column;
    height: 100%;
  }

  &.shrink {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 9999999;
    box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
  }
}

.sidebar-user {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 20px 16px 12px;
  flex-shrink: 0;
}

.sidebar-user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--layout-menu-active-color, rgba(0, 0, 0, 0.88));
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.sidebar-user-collapsed {
  display: flex;
  justify-content: center;
  padding: 16px 0 8px;
  flex-shrink: 0;
}
</style>
