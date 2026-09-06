<script setup>
import { useConfig } from '@/store/modules/layout'
import { closeShade } from '@/utils/pageShade'
import { BEFORE_RESIZE_LAYOUT } from '@/store/constant/cacheKey'
import Local from '@/utils/useStorage'

const appTitle = import.meta.env.VITE_APP_TITLE || '若依管理系统'
const config = useConfig()

const onMenuCollapse = () => {
  if (config.layout.shrink) {
    config.setLayout('menuCollapse', true)
    closeShade()
  } else {
    config.setLayout('menuCollapse', !config.layout.menuCollapse)
  }
  Local.set(BEFORE_RESIZE_LAYOUT, {
    layoutMode: config.layout.layoutMode,
    menuCollapse: config.layout.menuCollapse,
  })
}
</script>

<template>
  <div class="sidebar-brand">
    <img
      v-if="!config.layout.menuCollapse"
      class="sidebar-brand-icon"
      src="@/assets/icons/svg/vite.svg"
      alt="logo"
    />
    <span v-if="!config.layout.menuCollapse" class="sidebar-brand-name">
      {{ appTitle }}
    </span>
    <button
      type="button"
      class="sidebar-brand-collapse"
      :class="{ 'is-collapsed': config.layout.menuCollapse }"
      @click="onMenuCollapse"
    >
      <SvgIcon
        :iconClass="
          config.layout.menuCollapse
            ? 'ant-icon-MenuUnfoldOutlined'
            : 'ant-icon-MenuFoldOutlined'
        "
        :size="16"
      />
    </button>
  </div>
</template>

<style scoped lang="scss">
.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  border-top: 1px solid var(--layout-menu-border, rgba(0, 0, 0, 0.06));
  margin-top: auto;
  flex-shrink: 0;
}

.sidebar-brand-icon {
  width: 24px;
  height: 24px;
}

.sidebar-brand-name {
  flex: 1;
  font-size: 14px;
  font-weight: 600;
  color: var(--layout-menu-active-color, rgba(0, 0, 0, 0.88));
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.sidebar-brand-collapse {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 6px;
  background: transparent;
  cursor: pointer;
  color: var(--layout-menu-color, rgba(0, 0, 0, 0.45));
  transition: all 0.2s;

  &:hover {
    background: var(--layout-menu-hover-bg, rgba(0, 0, 0, 0.04));
    color: var(--layout-menu-active-color, rgba(0, 0, 0, 0.65));
  }

  &.is-collapsed {
    margin: 0 auto;
  }
}
</style>
