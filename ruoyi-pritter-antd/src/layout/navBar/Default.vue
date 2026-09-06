<script setup>
import { useConfig } from '@/store/modules/layout'
import { BEFORE_RESIZE_LAYOUT } from '@/store/constant/cacheKey'
import Local from '@/utils/useStorage'
import GlobalSearch from '../components/GlobalSearch/GlobalSearch.vue'
import NavMenus from './NavMenus.vue'
import NavTabs from './Tabs.vue'
import { showShade } from '@/utils/pageShade'

defineOptions({ name: 'DefaultNavBar' })

const config = useConfig()
const showRightPanel = defineModel('showRightPanel', {
  type: Boolean,
  default: false,
})

const onMenuCollapse = () => {
  if (config.layout.shrink) {
    config.setLayout('menuCollapse', false)
    showShade('ba-aside-menu-shade', () => {
      config.setLayout('menuCollapse', true)
    })
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
  <div class="nav-bar">
    <div class="nav-bar-left">
      <div class="nav-icon-btn" @click="onMenuCollapse">
        <SvgIcon
          :icon-class="config.layout.menuCollapse ? 'indent' : 'dedent'"
          :size="16"
        />
      </div>
      <NavTabs class="nav-bar-tabs" />
    </div>

    <div class="nav-bar-right">
      <GlobalSearch />
      <NavMenus v-model:show-right-panel="showRightPanel" />
    </div>
  </div>
</template>

<style scoped lang="scss">
.nav-bar {
  display: flex;
  align-items: center;
  gap: 16px;
  height: 56px;
  padding: 0 20px;
  background: var(--ba-bg-color-overlay);
  border-bottom: 1px solid var(--ba-border-color);
}

.nav-bar-left {
  display: flex;
  align-items: center;
  gap: 4px;
  flex: 1;
  min-width: 0;
}

.nav-bar-tabs {
  flex: 1;
  min-width: 0;
}

.nav-bar-right {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.nav-icon-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 8px;
  background: transparent;
  cursor: pointer;
  color: var(--ba-text-color);
  font-size: 16px;
  transition: all 0.2s;
  flex-shrink: 0;

  &:hover {
    background: rgba(0, 0, 0, 0.04);
  }
}

.dark {
  .nav-bar {
    background: var(--ba-bg-color-overlay);
    border-bottom: 1px solid var(--ba-border-color);
  }
}

.dark .nav-icon-btn {
  &:hover {
    background: rgba(255, 255, 255, 0.08);
  }
}
</style>
