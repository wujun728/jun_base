import { useDark, useToggle } from '@vueuse/core'
import { useConfig } from '@/store/modules/layout'
import { onMounted, onUnmounted, ref, watch } from 'vue'

/**
 * 切换暗黑模式
 */
let isDark = ref(false)
export const initDark = () => {
  isDark = useDark({
    onChanged(dark) {
      const config = useConfig()
      updateHtmlDarkClass(dark)
      config.setLayout('isDark', dark)
      config.onSetLayoutColor()
      const metaThemeColor = document.querySelector('meta[name="theme-color"]')
      if (metaThemeColor) {
        metaThemeColor.content = dark ? '#1d1e1f' : '#ffffff'
      }
    },
  })
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
  const htmlEl = document.getElementsByTagName('html')[0]
  let htmlClass = htmlEl.getAttribute('class') ?? ''
  if (val) {
    // 判断是否有dark
    if (!htmlClass.includes('dark')) {
      htmlClass += ' dark'
      htmlEl.setAttribute('class', htmlClass.trim())
    }
  } else {
    htmlClass = htmlClass.replace('dark', '')
    htmlEl.setAttribute('class', htmlClass.trim())
  }
}
export const toggleDarkLight = (e) => {
  if (!document.startViewTransition) {
    toggleDark()
    return
  }
  const configStore = useConfig()
  const transition = document.startViewTransition(() => toggleDark())
  transition.ready.then(() => {
    // 由于我们要从鼠标点击的位置开始做动画，所以我们需要先获取到鼠标的位置
    const { clientX, clientY } = e

    // 计算半径，以鼠标点击的位置为圆心，到四个角的距离中最大的那个作为半径
    const radius = Math.hypot(
      Math.max(clientX, innerWidth - clientX),
      Math.max(clientY, innerHeight - clientY)
    )
    const clipPath = [
      `circle(0% at ${clientX}px ${clientY}px)`,
      `circle(${radius}px at ${clientX}px ${clientY}px)`,
    ]
    const isDark = configStore.layout.isDark
    // 自定义动画
    document.documentElement.animate(
      {
        clipPath: isDark ? [...clipPath].reverse() : clipPath,
      },
      {
        duration: 600,
        easing: 'ease',
        pseudoElement: isDark
          ? '::view-transition-old(root)'
          : '::view-transition-new(root)',
      }
    )
  })
}
export default toggleDark
