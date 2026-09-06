export const isDesktop = () => {
  const userAgent = navigator.userAgent
  return !/(mobile|android|iphone|ipad|iemobile|ipod touch)/i.test(userAgent)
}
const capitalizeFirstLetter = (string: string) => {
  return `${string.charAt(0).toUpperCase()}${string.slice(1)}`
}
export const formatSearchTime = (params: anyObj, config: anyObj) => {
  if (!params) return {}
  if (!config) return {}
  for (const [key, value] of Object.entries(config)) {
    const query = params[key]
    if (query && Array.isArray(query)) {
      if (value === 'default') {
        params[`start${capitalizeFirstLetter(key)}`] = query[0]
        params[`end${capitalizeFirstLetter(key)}`] = query[1]
        delete params[key]
      } else if (value === 'reverse') {
        params[`${key}Start`] = query[0]
        params[`${key}End`] = query[1]
        delete params[key]
      } else {
        params[value[0]] = query[0]
        params[value[1]] = query[1]
        delete params[key]
      }
    }
  }
  return params
}

export const getDialogWidth = (width: StrNum) => {
  let total = 0
  if (typeof width === 'string') {
    const arr = width.split('px')
    total = Number(arr[0])
  }
  if (typeof width === 'number') {
    total = width
  }
  const winWidth = document.documentElement.offsetWidth
  return winWidth < total ? '100vw' : width
}

export function getElementTotalSize(element: Element) {
  if (!element) {
    return {
      marginTop: 0,
      marginRight: 0,
      marginBottom: 0,
      marginLeft: 0,
      width: 0,
      height: 0,
      paddingTop: 0,
      paddingRight: 0,
      paddingBottom: 0,
      paddingLeft: 0,
    }
  }
  const style = window.getComputedStyle(element)
  const width = style.width
  const height = style.height
  const marginLeft = style.marginLeft
  const marginRight = style.marginRight
  const marginTop = style.marginTop
  const marginBottom = style.marginBottom
  const paddingTop = style.paddingTop
  const paddingRight = style.paddingRight
  const paddingBottom = style.paddingBottom
  const paddingLeft = style.paddingLeft
  // 将字符串中的'px'去掉并转换为数值
  const numericWidth = parseFloat(width)
  const numericMarginLeft = parseFloat(marginLeft)
  const numericMarginRight = parseFloat(marginRight)

  const numericHeight = parseFloat(height)
  const numericMarginTop = parseFloat(marginTop)
  const numericMarginBottom = parseFloat(marginBottom)

  const numericPaddingTop = parseFloat(paddingTop)
  const numericPaddingRight = parseFloat(paddingRight)
  const numericPaddingBottom = parseFloat(paddingBottom)
  const numericPaddingLeft = parseFloat(paddingLeft)
  // 返回元素的总宽度，包括margin
  return {
    marginTop: numericMarginTop,
    marginRight: numericMarginRight,
    marginBottom: numericMarginBottom,
    marginLeft: numericMarginLeft,
    width:
      numericWidth +
      numericMarginLeft +
      numericMarginRight +
      numericPaddingLeft +
      numericPaddingRight,
    height:
      numericHeight +
      numericMarginTop +
      numericMarginBottom +
      numericPaddingTop +
      numericPaddingBottom,
    paddingTop: numericPaddingTop,
    paddingRight: numericPaddingRight,
    paddingBottom: numericPaddingBottom,
    paddingLeft: numericPaddingLeft,
  }
}

export const getDialogMaxHeight = (elementClass: string) => {
  const element = document.querySelector(elementClass)
  if (!element) return 0
  // 查找 element下面的el-dialog__header和el-dialog__footer
  const header = element.querySelector('.el-dialog__header') as HTMLElement
  const footer = element.querySelector('.el-dialog__footer') as HTMLElement

  const { marginTop, marginBottom } = getElementTotalSize(element)
  const isSmall = window.isSmallScreen
  let maxHeight = window.innerHeight - marginTop - 16
  if (header) {
    // 去除header高度
    maxHeight -= header.offsetHeight
  }
  if (footer) {
    maxHeight -= footer.offsetHeight
  }
  if (!isSmall) {
    maxHeight -= marginBottom
  }
  return maxHeight
}
