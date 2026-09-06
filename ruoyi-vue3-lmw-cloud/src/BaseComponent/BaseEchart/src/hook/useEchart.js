import * as echarts from 'echarts'
import darkTheme from './dark.json'

echarts.registerTheme('customDark', darkTheme)

const getScale = (containRatio = true, baseWidth = 1920) => {
  const currentScale = document.documentElement.clientWidth / baseWidth
  const formattedScale = currentScale > 1 ? currentScale : 1
  const resultScale = containRatio
    ? formattedScale * window.devicePixelRatio
    : formattedScale
  return Math.ceil(resultScale)
}
const ratio = getScale()
export default function (el, theme) {
  const eachartInstance = echarts.init(el, theme, {
    locale: 'ZH',
    renderer: 'svg',
    devicePixelRatio: ratio,
  })
  const setOption = (option, flag = false) => {
    eachartInstance.setOption(option, flag)
  }
  window.addEventListener('resize', () => {
    eachartInstance.resize()
  })
  const updateResize = () => {
    eachartInstance.resize()
  }
  return {
    eachartInstance,
    setOption,
    updateResize,
  }
}
