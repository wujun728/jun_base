// 公共方法
import Clipboard from 'clipboard'

export const addWindowResizeHandler = function (handler) {
  let oldHandler = window.onresize
  if (typeof window.onresize != 'function') {
    window.onresize = handler
  } else {
    window.onresize = function () {
      oldHandler()
      handler()
    }
  }
}

/**
 * 将第二个对象的属性值覆盖到第一个对象上
 * @param {Object} obj1 - 目标对象
 * @param {Object} obj2 - 源对象
 */
export const overwriteObj = function (obj1, obj2) {
  // 遍历第二个对象的所有属性
  Object.keys(obj2).forEach((prop) => {
    // 将第二个对象的属性值覆盖到第一个对象上
    obj1[prop] = obj2[prop]
  })
}

// // 普通页面的表格高度
// export const tableHeight = function () {
//   return
// }

/**
 * 获取默认表单配置对象
 * @returns {Object} 默认表单配置对象
 */
export function getDefaultFormConfig() {
  return {
    // 表单数据模型名称
    modelName: 'formData',
    // 表单引用名称
    refName: 'vForm',
    // 表单验证规则名称
    rulesName: 'rules',
    // 标签宽度
    labelWidth: 80,
    // 标签位置
    labelPosition: 'left',
    // 表单尺寸
    size: 'small',
    // 标签对齐方式
    labelAlign: 'label-left-align',
    // CSS样式代码
    cssCode: '',
    // 自定义类名数组
    customClass: [],
    // 自定义函数
    functions: '',
    // 布局类型
    layoutType: 'PC',

    // 表单创建事件处理函数
    onFormCreated: '',
    // 表单挂载事件处理函数
    onFormMounted: '',
    // 表单数据改变事件处理函数
    onFormDataChange: ''
  }
}

export function isNull(value) {
  return value === null || value === undefined
}

export function isNotNull(value) {
  return value !== null && value !== undefined
}

export function isEmptyStr(str) {
  //return (str === undefined) || (!str) || (!/[^\s]/.test(str));
  return str === undefined || (!str && str !== 0 && str !== '0') || !/[^\s]/.test(str)
}
/**
 * 生成唯一ID
 * @returns {number} 生成的唯一ID
 */
export const generateId = function () {
  return Math.floor(Math.random() * 100000 + Math.random() * 20000 + Math.random() * 5000)
}

/**
 * 深度克隆对象
 * @param {Object} origin - 原始对象
 * @returns {Object} 克隆后的新对象
 */
export const deepClone = function (origin) {
  if (origin === undefined) {
    return undefined
  }

  return JSON.parse(JSON.stringify(origin))
}
export function getQueryParam(variable) {
  let query = window.location.search.substring(1)
  let vars = query.split('&')
  for (let i = 0; i < vars.length; i++) {
    let pair = vars[i].split('=')
    if (pair[0] == variable) {
      return pair[1]
    }
  }

  return undefined
}

export const insertCustomCssToHead = function (cssCode, formId = '') {
  let head = document.getElementsByTagName('head')[0]
  let oldStyle = document.getElementById('vform-custom-css')
  if (!!oldStyle) {
    head.removeChild(oldStyle) //先清除后插入！！
  }
  if (!!formId) {
    oldStyle = document.getElementById('vform-custom-css' + '-' + formId)
    !!oldStyle && head.removeChild(oldStyle) //先清除后插入！！
  }

  let newStyle = document.createElement('style')
  newStyle.type = 'text/css'
  newStyle.rel = 'stylesheet'
  newStyle.id = !!formId ? 'vform-custom-css' + '-' + formId : 'vform-custom-css'
  try {
    newStyle.appendChild(document.createTextNode(cssCode))
  } catch (ex) {
    newStyle.styleSheet.cssText = cssCode
  }

  head.appendChild(newStyle)
}

export function setColumnFormatter(columnObj) {
  if (columnObj.type === 'Reference') {
    columnObj.formatter = formatRefColumn
  } else if (columnObj.type === 'ReferenceList') {
    columnObj.formatter = formatRefListColumn
  } else if (columnObj.type === 'Option') {
    columnObj.formatter = formatOptionColumn
  } /*else if (columnObj.type === 'Tag') {
  columnObj.formatter = formatTagColumn
} */ else if (columnObj.type === 'Percent') {
    columnObj.formatter = formatPercentColumn
  } else if (columnObj.type === 'Boolean') {
    columnObj.formatter = formatBooleanColumn
  }
}

export function copyToClipboard(content, clickEvent, $message, successMsg, errorMsg) {
  const clipboard = new Clipboard(clickEvent.target, {
    text: () => content
  })

  clipboard.on('success', () => {
    $message.success(successMsg)
    clipboard.destroy()
  })

  clipboard.on('error', () => {
    $message.error(errorMsg)
    clipboard.destroy()
  })

  clipboard.onClick(clickEvent)
}

/**
 *
 * @param {*} showDecimalPlaces 是否开启小数位
 * @param {*} decimalPlaces 小数位是几
 * @param {*} thousandsSeparator 是否开启千分符
 * @param {*} val 值
 * @returns
 */
export const getPreviewNum = (showDecimalPlaces, decimalPlaces, thousandsSeparator, val) => {
  let previewStr = val
  if (showDecimalPlaces) {
    previewStr = Number(previewStr).toFixed(decimalPlaces)
  }
  if (thousandsSeparator) {
    previewStr = numberToCurrencyNo(previewStr)
  }
  return previewStr
}
export const mlShortcutkeys = (cb) => {
  let shiftKeyFlag = 0,
    altKeyFlag = 0,
    mKeyFlag = 0,
    lKeyFlag = 0
  document.onkeydown = (e) => {
    let keyCode = e.keyCode || e.which || e.charCode
    if (keyCode === 16) { // shift
      shiftKeyFlag = 1
    } else if (keyCode === 18) { // alt
      altKeyFlag = 1
    } else if (keyCode === 76 || keyCode === 108) { // l or L
      lKeyFlag = 1
    } else if (keyCode === 77 || keyCode === 109) { // m or M
      mKeyFlag = 1
    }
    if (shiftKeyFlag && altKeyFlag && mKeyFlag && lKeyFlag) { // shift + alt + m + l
      cb()
    }
  }
  document.onkeyup = (e) => {
    let keyCode = e.keyCode || e.which || e.charCode
    if (keyCode === 16) { // shift
      shiftKeyFlag = 0
    } else if (keyCode === 18) { // alt
      altKeyFlag = 0
    } else if (keyCode === 76 || keyCode === 108) { // l or L
      lKeyFlag = 0
    } else if (keyCode === 77 || keyCode === 109) { // m or M
      mKeyFlag = 0
    }
  }
}
