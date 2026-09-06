import { nextTick } from 'vue'
import * as elIcons from '@element-plus/icons-vue'

export function getUrl() {
  return location.origin
}

export function loadCss(url: string) {
  const link = document.createElement('link')
  link.rel = 'stylesheet'
  link.href = url
  link.crossOrigin = 'anonymous'
  document.getElementsByTagName('head')[0].appendChild(link)
}
export function loadJs(url: string) {
  const link = document.createElement('script')
  link.src = url
  document.body.appendChild(link)
}
/**
 * 动态加载的 css 和 js
 */
const cssUrls = ['//at.alicdn.com/t/font_3135462_5axiswmtpj.css']
const jsUrls: string[] = []

/*
 * 加载预设的字体图标资源
 */
export default function init() {
  if (cssUrls.length > 0) {
    cssUrls.map((v) => {
      loadCss(v)
    })
  }

  if (jsUrls.length > 0) {
    jsUrls.map((v) => {
      loadJs(v)
    })
  }
}

/*
 * 获取本地自带的图标
 * /src/assets/icons文件夹内的svg文件
 */

export function getLocalIconfontNames(): Promise<string[]> {
  return new Promise((resolve, reject) => {
    nextTick(() => {
      let icons = []
      const modules = import.meta.glob('../assets/icons/svg/*.svg')
      for (const path in modules) {
        const p = path.split('assets/icons/svg/')[1].split('.svg')[0]
        icons.push(p)
      }
      if (icons.length > 0) {
        resolve(icons)
      } else {
        reject('No Local Icons')
      }
    })
  })
}

/*
 * 获取element plus 自带的图标
 */
export function getElementPlusIconfontNames(): Promise<string[]> {
  return new Promise((resolve, reject) => {
    nextTick(() => {
      const iconfonts = []
      const icons = elIcons
      for (const icon of Object.values(icons)) {
        iconfonts.push(`el-icon-${icon.name}`)
      }
      if (iconfonts.length > 0) {
        resolve(iconfonts)
      } else {
        reject('No ElementPlus Icons')
      }
    })
  })
}
