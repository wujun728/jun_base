<script setup lang="ts">
import { onMounted, nextTick } from 'vue';
import useSettingsStore from '@/store/modules/settings'
import { getDarkColor, getLightColor, handleThemeStyle } from '@/utils/theme'
const settingsStore = useSettingsStore()
onMounted(() => {
  nextTick(() => {
    settingsStore.initSetting().then(() => {
      // 初始化主题样式
      handleThemeStyle(settingsStore.theme)
      console.log(`\n%cRuoYi%cGeek%c\n`, `padding: 8px; background: ${getDarkColor(settingsStore.theme, 0.1)}; font-weight: bold; font-size: large; color: white;`, `padding: 8px; background: ${getLightColor(settingsStore.theme, 0.1)}; font-size: large; color: #eee;`, '');
    })
  })
})
</script>
<template>
  <div v-loading="!settingsStore.inited" class="app-container">
    <router-view />
  </div>
</template>
<style scoped>
.app-container {
  height: 100%;
  width: 100%;
}
</style>