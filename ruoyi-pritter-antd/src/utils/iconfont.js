import { nextTick } from 'vue'
import * as aIcons from '@ant-design/icons-vue'

export function getUrl() {
  return location.origin
}

export function loadCss(url) {
  const link = document.createElement('link')
  link.rel = 'stylesheet'
  link.href = url
  link.crossOrigin = 'anonymous'
  document.getElementsByTagName('head')[0].appendChild(link)
}
export function loadJs(url) {
  const link = document.createElement('script')
  link.src = url
  document.body.appendChild(link)
}
/**
 * 动态加载的 css 和 js
 */
const cssUrls = ['//at.alicdn.com/t/font_3135462_5axiswmtpj.css']
const jsUrls = []

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
 * 获取当前页面中从指定域名加载到的样式表内容
 * 样式表未载入前无法获取
 */
function getStylesFromDomain(domain) {
  const sheets = []
  const styles = document.styleSheets
  for (const key in styles) {
    if (styles[key].href && styles[key].href.indexOf(domain) > -1) {
      sheets.push(styles[key])
    }
  }
  return sheets
}

/**
 * 获取Vite开发服务/编译后的样式表内容
 * @param devID style 标签的 viteDevId，只开发服务有
 */
function getStylesFromVite(devId) {
  const sheets = []
  const styles = document.styleSheets
  if (import.meta.env.MODE == 'production') {
    const url = getUrl()
    for (const key in styles) {
      if (styles[key].href && styles[key].href?.indexOf(url) === 0) {
        sheets.push(styles[key])
      }
    }
    return sheets
  }
  for (const key in styles) {
    const ownerNode = styles[key].ownerNode
    if (
      ownerNode &&
      ownerNode.dataset?.viteDevId &&
      ownerNode.dataset.viteDevId.indexOf(devId) > -1
    ) {
      sheets.push(styles[key])
    }
  }
  return sheets
}

/*
 * 获取本地自带的图标
 * /src/assets/icons文件夹内的svg文件
 */

export function getLocalIconfontNames() {
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

const ANT_ICON_PREFIX = 'ant-icon-'
const ANT_ICON_KEY_RE = /(?:Outlined|Filled|TwoTone)$/

export function isAntdIconKey(key, component = aIcons[key]) {
  return (
    ANT_ICON_KEY_RE.test(key) &&
    typeof component === 'function' &&
    component.displayName === key
  )
}

export function resolveAntdIcon(iconClass) {
  if (!iconClass?.startsWith(ANT_ICON_PREFIX)) return null
  const key = iconClass.slice(ANT_ICON_PREFIX.length)
  return isAntdIconKey(key) ? aIcons[key] : null
}

/*
 * 获取 Ant Design Vue 自带的图标
 */
export function getAntdIconfontNames() {
  return new Promise((resolve, reject) => {
    nextTick(() => {
      const iconfonts = []
      for (const key in aIcons) {
        if (isAntdIconKey(key)) {
          iconfonts.push(`${ANT_ICON_PREFIX}${key}`)
        }
      }
      if (iconfonts.length > 0) {
        resolve(iconfonts)
      } else {
        reject('No Ant Design Icons')
      }
    })
  })
}
