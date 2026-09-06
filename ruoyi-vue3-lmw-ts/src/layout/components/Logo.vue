<script setup lang="ts">
import { BEFORE_RESIZE_LAYOUT } from '@/store/constant/cacheKey.ts'
import useConfig from '@/store/modules/layout.ts'
import Local from '@/utils/hsj/useStorage'

const title = import.meta.env.VITE_APP_TITLE
const config = useConfig()

const setNavTabsWidth = () => {
  const navTabs: HTMLDivElement = document.querySelector('.nav-tabs')!
  if (!navTabs) {
    return
  }
  const navBar: HTMLDivElement = document.querySelector('.nav-bar')!
  const navMenus: HTMLDivElement = document.querySelector('.nav-menus')!
  const minWidth = navBar?.offsetWidth - (navMenus.offsetWidth + 20)
  navTabs.style.width = minWidth.toString() + 'px'
}
const closeShade = function (closeCallBack?: () => void) {
  const shadeEl = document.querySelector('.ba-layout-shade')
  shadeEl && shadeEl.remove()
  closeCallBack && closeCallBack()
}

const onMenuCollapse = () => {
  if (config.layout.shrink && !config.layout.menuCollapse) {
    closeShade()
  }

  config.setLayout('menuCollapse', !config.layout.menuCollapse)

  Local.set(BEFORE_RESIZE_LAYOUT, {
    layoutMode: config.layout.layoutMode,
    menuCollapse: config.layout.menuCollapse,
  })

  // 等待侧边栏动画结束后重新计算导航栏宽度
  setTimeout(() => {
    setNavTabsWidth()
  }, 350)
}
</script>
<template>
  <div class="layout-logo">
    <img
      v-if="!config.layout.menuCollapse"
      class="logo-img"
      src="@/assets/icons/svg/vite.svg"
      alt="logo"
    />
    <div
      v-if="!config.layout.menuCollapse"
      :style="{ color: config.getColorVal('menuActiveColor') }"
      class="website-name"
    >
      {{ title }}
    </div>
    <svg-icon
      @click="onMenuCollapse"
      v-if="config.layout.layoutMode != 'Streamline'"
      :color="config.getColorVal('menuActiveColor')"
      :iconClass="config.layout.menuCollapse ? 'indent' : 'dedent'"
      size="18"
      class="fold"
      :class="config.layout.menuCollapse ? 'unfold' : ''"
    />
  </div>
</template>

<style scoped lang="scss">
.layout-logo {
  width: 100%;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  padding: 10px;
  background: v-bind(
    'config.layout.layoutMode != "Streamline" ?  config.getColorVal("menuTopBarBackground"):"transparent"'
  );
}
.logo-img {
  width: 28px;
}
.website-name {
  flex: 1;
  display: block;
  padding-left: 4px;
  font-size: var(--el-font-size-extra-large);
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.fold {
  margin-left: auto;
  cursor: pointer;
}
.unfold {
  margin: 0 auto;
}
</style>
