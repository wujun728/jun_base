// 值存在
export function isDef(value) {
  return value !== undefined && value !== null
}

/**
 * 从对象中获取指定路径的值
 * @param {Object} object - 目标对象
 * @param {string} path - 属性路径，支持点号分隔符
 * @returns {*} - 返回属性值或null
 */
export function get(object, path) {
  // 将路径字符串按点号分隔成数组
  const keys = path.split('.')
  // 初始化结果为目标对象
  let result = object

  // 遍历路径数组，逐级获取属性值
  keys.forEach((key) => {
    // 判断当前结果和属性值是否都存在，若存在则获取属性值，否则置为null
    result = isDef(result) && isDef(result[key]) ? result[key] : null
  })

  // 返回最终结果
  return result
}

/**
 * 判断一个值是否为对象或函数类型
 * @param {*} x - 需要判断的值
 * @returns {boolean} - 如果是对象或函数类型则返回true，否则返回false
 */
export function isObj(x) {
  // 获取x的类型
  const type = typeof x
  // 如果x不为null并且类型为对象或函数，则返回true，否则返回false
  return x !== null && (type === 'object' || type === 'function')
}


/**
 * 从源对象中复制指定属性到目标对象中
 * @param {Object} to - 目标对象
 * @param {Object} from - 源对象
 * @param {string} key - 属性名
 */
const { hasOwnProperty } = Object.prototype

function assignKey(to, from, key) {
  // 获取源对象中指定属性的值
  const val = from[key]

  // 如果源对象中指定属性的值不存在，则直接返回
  if (!isDef(val)) {
    return
  }

  // 如果目标对象中不存在指定属性或者指定属性的值不是一个对象，则直接将源对象中指定属性的值赋给目标对象
  if (!hasOwnProperty.call(to, key) || !isObj(val)) {
    to[key] = val
  } else {
    // 否则，递归调用deepAssign函数，将源对象中指定属性的值深度合并到目标对象中指定属性的值上
    to[key] = deepAssign(Object(to[key]), from[key])
  }
}


/**
 * 深度合并对象
 * @param {Object} to - 目标对象
 * @param {Object} from - 源对象
 * @returns {Object} - 合并后的目标对象
 */
export function deepAssign(to, from) {
  // 遍历源对象的所有属性
  Object.keys(from).forEach((key) => {
    // 调用assignKey函数进行属性合并
    assignKey(to, from, key)
  })

  // 返回合并后的目标对象
  return to
}
