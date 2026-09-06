<script setup>
import { useEventListener } from '@vueuse/core'
import { useConfig } from '@/store/modules/layout'
import { BEFORE_RESIZE_LAYOUT } from '@/store/constant/cacheKey'
import Local from '@/utils/useStorage'
import { getToken, setToken } from '@/utils/auth'
import { isSmallScreen } from '@/plugins'
import Classic from './container/Classic'

defineOptions({
  name: 'Layout',
  components: { Classic },
})

const config = useConfig()
const state = reactive({
  autoMenuCollapseLock: false,
})

const onAdaptiveLayout = () => {
  const defaultBeforeResizeLayout = {
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
  } else {
    state.autoMenuCollapseLock = false
    const beforeResizeLayoutTemp =
      beforeResizeLayout || defaultBeforeResizeLayout
    config.setLayout('menuCollapse', beforeResizeLayoutTemp.menuCollapse)
    config.setLayout('shrink', false)
  }
}

onBeforeMount(() => {
  const token = getToken()
  if (token) {
    setToken(token)
  }
  isSmallScreen()
  config.init()
  onAdaptiveLayout()
  useEventListener(window, 'resize', () => {
    isSmallScreen()
    onAdaptiveLayout()
  })
})
</script>

<template>
  <component :is="config.layout.layoutMode" />
</template>
