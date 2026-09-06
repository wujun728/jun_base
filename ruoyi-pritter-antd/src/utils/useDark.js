import { useDark, useToggle } from '@vueuse/core'
import { useConfig } from '@/store/modules/layout'
import { syncAntTheme } from '@/utils/antTheme'
import { nextTick, onMounted, onUnmounted, ref, watch } from 'vue'

/**
 * 切换暗黑模式
 */
let isDark = ref(false)

function syncMetaThemeColor(dark) {
  const metaThemeColor = document.querySelector('meta[name="theme-color"]')
  if (metaThemeColor) {
    metaThemeColor.content = dark ? '#1d1e1f' : '#ffffff'
  }
}

function applyDarkState(dark) {
  syncAntTheme(dark)
  updateHtmlDarkClass(dark)
}

export const initDark = () => {
  const config = useConfig()
  isDark = useDark({
    onChanged(dark) {
      applyDarkState(dark)
      config.setLayout('isDark', dark)
      syncMetaThemeColor(dark)
    },
  })
  if (isDark.value !== config.layout.isDark) {
    isDark.value = config.layout.isDark
  } else {
    applyDarkState(isDark.value)
    syncMetaThemeColor(isDark.value)
  }
}

export const setDark = (darkValue) => {
  isDark.value = darkValue
}

const toggleDark = () => {
  useToggle(isDark)()
}

/**
 * 切换当前页面的暗黑模式
 */
export function togglePageDark(val) {
  const config = useConfig()
  const isDark = ref(config.layout.isDark)
  onMounted(() => {
    if (isDark.value !== val) updateHtmlDarkClass(val)
  })
  onUnmounted(() => {
    updateHtmlDarkClass(isDark.value)
  })
  watch(
    () => config.layout.isDark,
    (newVal) => {
      isDark.value = newVal
      if (isDark.value !== val) updateHtmlDarkClass(val)
    }
  )
}

export function updateHtmlDarkClass(val) {
  document.documentElement.classList.toggle('dark', val)
  document.documentElement.style.colorScheme = val ? 'dark' : 'light'
}

async function waitForPaint() {
  await nextTick()
  await new Promise((resolve) => {
    requestAnimationFrame(() => requestAnimationFrame(resolve))
  })
}

/**
 * Ant Design 使用 CSS-in-JS，与 View Transition 快照不兼容，直接同步切换主题。
 */
export const toggleDarkLight = async () => {
  const html = document.documentElement
  html.classList.add('theme-switching')
  toggleDark()
  await waitForPaint()
  html.classList.remove('theme-switching')
}

export default toggleDark
