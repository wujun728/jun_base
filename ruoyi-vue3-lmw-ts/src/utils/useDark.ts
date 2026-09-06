import { useDark, useToggle } from '@vueuse/core'
import useConfig from '@/store/modules/layout'
import { onMounted, onUnmounted, ref, watch } from 'vue'

/**
 * 切换暗黑模式
 */
const toggleDark = () => {
  const isDark = useDark({
    onChanged(dark) {
      const config = useConfig()
      updateHtmlDarkClass(dark)
      config.setLayout('isDark', dark)
      config.onSetLayoutColor()
      const metaThemeColor = document.querySelector(
        'meta[name="theme-color"]'
      ) as HTMLMetaElement
      if (metaThemeColor) {
        metaThemeColor.content = dark ? '#1d1e1f' : '#ffffff'
      }
    },
  })
  useToggle(isDark)()
}

/**
 * 切换当前页面的暗黑模式
 */
export function togglePageDark(val: boolean) {
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

export function updateHtmlDarkClass(val: boolean) {
  const htmlEl = document.getElementsByTagName('html')[0]
  if (val) {
    htmlEl.setAttribute('class', 'dark')
  } else {
    htmlEl.setAttribute('class', '')
  }
}

export default toggleDark
