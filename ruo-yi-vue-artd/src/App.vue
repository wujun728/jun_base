<template>
  <ElConfigProvider
    size="default"
    :locale="locales[language]"
    :z-index="3000"
    :card="{
      shadow: 'never'
    }">
    <RouterView></RouterView>
  </ElConfigProvider>
</template>

<script setup lang="ts">
import en from 'element-plus/es/locale/lang/en'
import zh from 'element-plus/es/locale/lang/zh-cn'

import { initializeTheme } from './hooks/core/useTheme'
import { useUserStore } from './store/modules/user'
import { checkStorageCompatibility } from './utils/storage'
import { systemUpgrade } from './utils/sys'
import { toggleTransition } from './utils/ui/animation'

const userStore = useUserStore()
const { language } = storeToRefs(userStore)

const locales = {
  zh: zh,
  en: en
}

onBeforeMount(() => {
  toggleTransition(true)
  initializeTheme()
})

onMounted(() => {
  checkStorageCompatibility()
  toggleTransition(false)
  systemUpgrade()
})
</script>
