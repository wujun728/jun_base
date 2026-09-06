<script setup lang="ts">
import variables from '@/assets/styles/variables.module.scss'
import logo from '@/assets/logo/logo.png'
import useSettingsStore from '@/store/modules/settings'
import { computed, ref } from 'vue';

defineProps({
  collapse: {
    type: Boolean,
    required: true
  }
})

const title = computed(() => import.meta.env.VITE_APP_TITLE || '后台管理系统');
const settingsStore = useSettingsStore();
const sideTheme = computed(() => settingsStore.sideTheme);
const sidebarTitleStyle = computed(() => ({ color: sideTheme.value === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }));
</script>
<template>
  <div class="sidebar-logo-container" :class="{ 'collapse': collapse }"
    :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 v-else class="sidebar-title" :style="sidebarTitleStyle">{{ title }}</h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 class="sidebar-title" :style="sidebarTitleStyle">{{ title }}</h1>
      </router-link>
    </transition>
  </div>
</template>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 50px;
  line-height: 50px;
  background: #2b2f3a;
  text-align: center;
  overflow: hidden;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    & .sidebar-logo {
      width: 36px;
      height: 36px;
      vertical-align: middle;
      margin-right: 12px;
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      line-height: 50px;
      font-size: 18px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }
  }

  &.collapse {
    .sidebar-logo {
      margin-right: 0px;
    }
  }
}
</style>