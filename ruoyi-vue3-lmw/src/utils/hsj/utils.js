// 判断是否是手机端
export const isMobile = () => {
  let flag = navigator.userAgent.match(
    /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
  )
  return flag
}
// formConfig排序
export const sortConfig = (items) => {
  items.sort((cur, pre) => {
    if (!cur.order) {
      cur.order = 0
    }
    if (!pre.order) {
      pre.order = 0
    }
    return cur.order - pre.order
  })
}

// 数据字典数据存储
export function dictMap(dict, list) {
  dict.value = list.map((item) => {
    return {
      label: item.dictLabel,
      value: item.dictValue,
    }
  })
}

// 表格合并
export const objectSpanMethod = (rowObj, config, tableData, unique) => {
  let { row, column, rowIndex, columnIndex } = rowObj
  // 通过递归获取单元格所占大小
  let nowRowSpan = 1
  const recursionRowSpan = (row, rowIndex, str, data) => {
    let num = 0
    // row[str] == data[rowIndex - 1][str]这个用来判断是否合并，可以改为自己的判断方法
    // 判断上一行字段的值与当前行值是否一致
    if (
      nowRowSpan == 1 &&
      rowIndex > 0 &&
      row[str] == data[rowIndex - 1][str] &&
      row[unique] == data[rowIndex - 1][unique]
    ) {
      return 0
    }
    // 判断下一行字段的值与该行值是否一致
    if (
      rowIndex + 1 < data.length &&
      row[str] == data[rowIndex + 1][str] &&
      row[unique] == data[rowIndex + 1][unique]
    ) {
      nowRowSpan++
      num = rowIndex + 1
      return recursionRowSpan(data[num], num, str, data)
    } else {
      num = nowRowSpan
      nowRowSpan = 1
      return num
    }
  }

  let num = 1
  for (const [key, value] of Object.entries(config)) {
    if (columnIndex == key) {
      num = recursionRowSpan(row, rowIndex, value, tableData)
    }
  }
  return {
    rowspan: num,
    colspan: 1,
  }
}

/**
 * @param {Function} fn 要防抖的函数
 * @param {Number} time 防抖时间
 * @param {Boolean} now 是否立即执行传入的函数
 * @param {Function} callback 接收传入函数的返回值的回调
 * @returns
 */

export function antiShake(fn, time = 500, now = false, callback) {
  //防抖
  let timer
  let isNow = false
  let debounce = function (...args) {
    if (timer) clearTimeout(timer)
    if (!isNow && now) {
      const res = fn.apply(this, args)
      if (res) {
        callback && callback(res)
      }
      isNow = true
    } else {
      timer = setTimeout(() => {
        const res = fn.apply(this, args)
        if (res) {
          callback && callback(res)
        }
        isNow = false
      }, time)
    }
  }
  debounce.cancel = function () {
    if (timer) clearTimeout(timer)
    timer = null
    isNow = false
  }
  return debounce
}

// 生成随机值
export const generateUnique = () => {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    let r = (Math.random() * 16) | 0
    let v = c === 'x' ? r : (r & 0x3) | 0x8
    return v.toString(16)
  })
}

// 深拷贝
export const deepClone = (obj) => {
  if (obj === null) return obj
  if (obj === undefined) return obj
  if (obj instanceof Date) return new Date(obj)
  if (obj instanceof RegExp) return new RegExp(obj)
  if (typeof obj !== 'object') return obj
  let cloneObj = new obj.constructor()
  for (let i in obj) {
    if (obj.hasOwnProperty(i)) {
      cloneObj[i] = deepClone(obj[i])
    }
  }
  return cloneObj
}

export const isDesktop = () => {
  const userAgent = navigator.userAgent
  return !/(mobile|android|iphone|ipad|iemobile|ipod touch)/i.test(userAgent)
}

const capitalizeFirstLetter = (string) => {
  return `${string.charAt(0).toUpperCase()}${string.slice(1)}`
}
export const formatSearchTime = (params, config) => {
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

export const getDialogWidth = (width) => {
  if (typeof width !== 'number' && typeof width !== 'string') {
    return width
  }
  let total = 0
  if (typeof width === 'string') {
    const arr = width.split('px')
    total = arr[0]
  }
  if (typeof width === 'number') {
    total = width
  }
  const winWidth = document.documentElement.offsetWidth
  return winWidth < total ? '100vw' : width
}

export function getElementTotalSize(element) {
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

export const getDialogMaxHeight = (elementClass) => {
  const element = document.querySelector(elementClass)
  if (!element) return 0
  // 查找 element下面的el-dialog__header和el-dialog__footer
  const header = element.querySelector('.el-dialog__header')
  const footer = element.querySelector('.el-dialog__footer')

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
