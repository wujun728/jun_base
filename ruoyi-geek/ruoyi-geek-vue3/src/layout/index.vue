<script setup lang="ts">
import { AppMain, Settings, TagsView, Sidebar, Navbar } from './components'
import { computed, ref, useTemplateRef, watchEffect } from 'vue'
import { useWindowSize } from '@vueuse/core'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'

const settingsStore = useSettingsStore()
const appStore = useAppStore()
const theme = computed(() => settingsStore.theme);
const needTagsView = computed(() => settingsStore.tagsView);
const fixedHeader = computed(() => settingsStore.fixedHeader);
const sidebarOption = computed(() => appStore.sidebar);
const device = computed(() => appStore.device);
const showSidebar = computed(() => !settingsStore.isTopMenu && !sidebarOption.value.hide);
const sidebarHidden = computed(() => settingsStore.isTopMenu || sidebarOption.value.hide);

const classObj = computed(() => ({
  hideSidebar: !sidebarOption.value.opened,
  openSidebar: sidebarOption.value.opened,
  withoutAnimation: sidebarOption.value.withoutAnimation,
  mobile: device.value === 'mobile'
}))

const WIDTH = 992; // 参考 Bootstrap 的响应式设计
const { width, height } = useWindowSize();

watchEffect(() => {
  if (device.value === 'mobile' && sidebarOption.value.opened) {
    appStore.closeSideBar(false)
  }
  if (width.value - 1 < WIDTH) {
    appStore.toggleDevice('mobile')
    appStore.closeSideBar(true)
  } else {
    appStore.toggleDevice('desktop')
  }
})

const settingRef = useTemplateRef('settingRef')
</script>
<template>
  <div :class="classObj" class="app-wrapper" :style="{ '--current-color': theme }">
    <div v-if="device === 'mobile' && showSidebar && sidebarOption.opened" class="drawer-bg"
      @click="appStore.closeSideBar(false)" />
    <!-- 侧边栏 -->
    <sidebar v-if="showSidebar" />
    <div :class="{ hasTagsView: needTagsView, sidebarHide: sidebarHidden }" class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <!-- 导航栏/面包屑 -->
        <navbar @setLayout="settingRef?.openSetting()" />
        <!-- 标签页 -->
        <tags-view v-if="needTagsView" />
      </div>
      <!-- 主界面区 -->
      <app-main />
      <!-- 布局设置 -->
      <settings ref="settingRef" />
    </div>
  </div>
</template>
<style lang="scss" scoped>
@use "@/assets/styles/mixin.scss";
@use "@/assets/styles/variables.module.scss";

.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
  @include mixin.clearfix;


  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - variables.$base-sidebar-width);
  transition: width 0.28s;

  .hideSidebar & {
    width: calc(100% - variables.$hide-sidebar-width);
  }

  .sidebarHide & {
    width: 100%;
  }

  .mobile & {
    width: 100%;
  }
}
</style>