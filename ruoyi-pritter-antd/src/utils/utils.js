// 回显数据字典
export function selectDictLabel(datas, value) {
  if (value === undefined) {
    return ''
  }
  var actions = []
  if (datas) {
    Object.keys(datas).some((key) => {
      if (datas[key].value == '' + value) {
        actions.push(datas[key].label)
        return true
      }
    })
    if (actions.length === 0) {
      actions.push(value)
    }
  }

  return actions.join('')
}

// 回显数据字典（字符串数组）
export function selectDictLabels(datas, value, separator) {
  if (value === undefined || value.length === 0) {
    return ''
  }
  if (Array.isArray(value)) {
    value = value.join(',')
  }
  var actions = []
  var currentSeparator = undefined === separator ? ',' : separator
  var temp = value.split(currentSeparator)
  Object.keys(value.split(currentSeparator)).some((val) => {
    var match = false
    Object.keys(datas).some((key) => {
      if (datas[key].value == '' + temp[val]) {
        actions.push(datas[key].label + currentSeparator)
        match = true
      }
    })
    if (!match) {
      actions.push(temp[val] + currentSeparator)
    }
  })
  return actions.join('').substring(0, actions.join('').length - 1)
}

// 字符串格式化(%s )
export function sprintf(str) {
  var args = arguments,
    flag = true,
    i = 1
  str = str.replace(/%s/g, function () {
    var arg = args[i++]
    if (typeof arg === 'undefined') {
      flag = false
      return ''
    }
    return arg
  })
  return flag ? str : ''
}

// 转换字符串，undefined,null等转化为""
export function parseStrEmpty(str) {
  if (!str || str == 'undefined' || str == 'null') {
    return ''
  }
  return str
}

// 数据合并
export function mergeRecursive(source, target) {
  for (var p in target) {
    try {
      if (target[p].constructor == Object) {
        source[p] = mergeRecursive(source[p], target[p])
      } else {
        source[p] = target[p]
      }
    } catch (e) {
      source[p] = target[p]
    }
  }
  return source
}

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data, id, parentId, children) {
  let config = {
    id: id || 'id',
    parentId: parentId || 'parentId',
    childrenList: children || 'children',
  }

  var childrenListMap = {}
  var nodeIds = {}
  var tree = []

  for (let d of data) {
    let parentId = d[config.parentId]
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = []
    }
    nodeIds[d[config.id]] = d
    childrenListMap[parentId].push(d)
  }

  for (let d of data) {
    let parentId = d[config.parentId]
    if (nodeIds[parentId] == null) {
      tree.push(d)
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t)
  }

  function adaptToChildrenList(o) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]]
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c)
      }
    }
  }
  return tree
}

/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams(params) {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName]
    let part = encodeURIComponent(propName) + '='

    if (value !== null && value !== '' && typeof value !== 'undefined') {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (
            value[key] !== null &&
            value[key] !== '' &&
            typeof value[key] !== 'undefined'
          ) {
            let params = propName + '[' + key + ']'
            var subPart = encodeURIComponent(params) + '='
            result += subPart + encodeURIComponent(value[key]) + '&'
          }
        }
      } else {
        result += part + encodeURIComponent(value) + '&'
      }
    }
  }
  return result
}

// 返回项目路径
export function getNormalPath(p) {
  if (p.length === 0 || !p || p == 'undefined') {
    return p
  }
  let res = p.replace('//', '/')
  if (res[res.length - 1] === '/') {
    return res.slice(0, res.length - 1)
  }
  return res
}

// 验证是否为blob格式
export async function blobValidate(data) {
  try {
    const text = await data.text()
    JSON.parse(text)
    return false
  } catch (error) {
    return true
  }
}

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
  const target = document.querySelector(elementClass)
  if (!target) return 0

  const modal = target.classList.contains('ant-modal')
    ? target
    : target.querySelector('.ant-modal') || target

  const header = modal.querySelector('.ant-modal-header')
  const footer = modal.querySelector('.ant-modal-footer')
  const body = modal.querySelector('.ant-modal-content')

  const { marginBottom } = getElementTotalSize(modal)
  const isSmall = window.isSmallScreen
  const topOffset = modal.getBoundingClientRect().top
  const bottomGap = isSmall ? 0 : marginBottom + 50

  let maxHeight = window.innerHeight - topOffset - bottomGap

  if (header) {
    maxHeight -= header.offsetHeight
  }
  if (footer) {
    maxHeight -= footer.offsetHeight
  }
  if (body) {
    const { paddingTop, paddingBottom } = getElementTotalSize(body)
    maxHeight -= paddingTop + paddingBottom
  }
  return Math.max(maxHeight, 0)
}
