<script setup lang="ts">
import { AppMain, Settings, TagsView, Sidebar, Navbar } from './components'
import { computed, ref, watchEffect } from 'vue'
import { useWindowSize } from '@vueuse/core'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'

const settingsStore = useSettingsStore()
const theme = computed(() => settingsStore.theme);
const needTagsView = computed(() => settingsStore.tagsView);
const fixedHeader = computed(() => settingsStore.fixedHeader);
const sidebarOption = computed(() => useAppStore().sidebar);
const device = computed(() => useAppStore().device);

const classObj = computed(() => ({
  hideSidebar: !sidebarOption.value.opened,
  openSidebar: sidebarOption.value.opened,
  withoutAnimation: sidebarOption.value.withoutAnimation,
  mobile: device.value === 'mobile'
}))

const { width, height } = useWindowSize();
const WIDTH = 992; // refer to Bootstrap's responsive design

watchEffect(() => {
  if (device.value === 'mobile' && sidebarOption.value.opened) {
    useAppStore().closeSideBar(false)
  }
  if (width.value - 1 < WIDTH) {
    useAppStore().toggleDevice('mobile')
    useAppStore().closeSideBar(true)
  } else {
    useAppStore().toggleDevice('desktop')
  }
})

function handleClickOutside() {
  useAppStore().closeSideBar(false)
}

const settingRef = ref<typeof Settings>();
function setLayout() {
  settingRef.value!.openSetting();
}
</script>
<template>
  <div :class="classObj" class="app-wrapper" :style="{ '--current-color': theme }">
    <div v-if="device === 'mobile' && sidebarOption.opened" class="drawer-bg" @click="handleClickOutside" />
    <!-- 侧边栏 -->
    <sidebar v-if="!sidebarOption.hide" />
    <div :class="{ hasTagsView: needTagsView, sidebarHide: sidebarOption.hide }" class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <!-- 导航栏/面包屑 -->
        <navbar @setLayout="setLayout" />
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
}

.hideSidebar .fixed-header {
  width: calc(100% - variables.$hide-sidebar-width);
}

.sidebarHide .fixed-header {
  width: 100%;
}

.mobile .fixed-header {
  width: 100%;
}
</style>