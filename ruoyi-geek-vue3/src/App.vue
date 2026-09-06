<template>
  <div v-loading="!settingsStore.inited" style="height: 100%;width: 100%;">
    <router-view />
  </div>
</template>

<script setup>
import useSettingsStore from '@/store/modules/settings'
import { handleThemeStyle } from '@/utils/theme'
const settingsStore = useSettingsStore()
onMounted(() => {
  nextTick(() => {
    settingsStore.initSetting(() => {
      // 初始化主题样式
      handleThemeStyle(settingsStore.theme)
    })
  })
})
</script>
