<script setup>
import { useConfig } from '@/store/modules/layout.js'
import { BEFORE_RESIZE_LAYOUT } from '@/store/constant/cacheKey'
import Local from '@/utils/hsj/useStorage'
import { useEventListener } from '@vueuse/core'
import Default from './container/Default.vue'
import Classic from './container/Classic.vue'
import Streamline from './container/Streamline.vue'
import Double from './container/Double.vue'
import { getToken, setToken } from '@/utils/auth'

defineOptions({
  components: { Default, Classic, Streamline, Double },
})
const proxy = inject('proxy')
const config = useConfig()
const state = reactive({
  autoMenuCollapseLock: false,
})
const onAdaptiveLayout = () => {
  let defaultBeforeResizeLayout = {
    layoutMode: config.layout.layoutMode,
    menuCollapse: config.layout.menuCollapse,
  }
  let beforeResizeLayout = Local.get(BEFORE_RESIZE_LAYOUT)
  if (!beforeResizeLayout) {
    Local.set(BEFORE_RESIZE_LAYOUT, defaultBeforeResizeLayout)
  }

  const clientWidth = document.body.clientWidth
  if (clientWidth < 1024) {
    if (!state.autoMenuCollapseLock) {
      state.autoMenuCollapseLock = true
      config.setLayout('menuCollapse', true)
    }
    config.setLayout('shrink', true)
    config.setLayoutMode('Classic')
  } else {
    state.autoMenuCollapseLock = false
    let beforeResizeLayoutTemp = beforeResizeLayout || defaultBeforeResizeLayout
    config.setLayout('menuCollapse', beforeResizeLayoutTemp.menuCollapse)
    config.setLayout('shrink', false)
    config.setLayoutMode(beforeResizeLayoutTemp.layoutMode)
  }
}
const setNavTabsWidth = () => {
  const navTabs = document.querySelector('.nav-tabs')
  if (!navTabs) {
    return
  }
  const navBar = document.querySelector('.nav-bar')
  const navMenus = document.querySelector('.nav-menus')
  const minWidth = navBar.offsetWidth - (navMenus.offsetWidth + 20)
  navTabs.style.width = minWidth.toString() + 'px'
}

onMounted(() => {
  setNavTabsWidth()
  useEventListener(window, 'resize', setNavTabsWidth)
})
onBeforeMount(() => {
  const token = getToken()
  if (token) {
    setToken(token)
  }
  if (proxy.$isSmallScreen) {
    config.setLayoutMode('Classic')
  }
  onAdaptiveLayout()
  useEventListener(window, 'resize', onAdaptiveLayout)
})
</script>
<template>
  <component :is="config.layout.layoutMode"></component>
</template>

<style scoped lang="scss"></style>
