/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams(params: anyObj) {
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
export function getNormalPath(p: string) {
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
export async function blobValidate(data: any) {
  try {
    const text = await data.text()
    JSON.parse(text)
    return false
  } catch (error) {
    return true
  }
}

// 转换字符串，undefined,null等转化为""
export function parseStrEmpty(str?: StrNum) {
  if (!str || str == 'undefined' || str == 'null') {
    return ''
  }
  return str
}

export function handleTree(
  data: any,
  id: string | number,
  parentId?: string | number,
  children?: any
) {
  let config = {
    id: id || 'id',
    parentId: parentId || 'parentId',
    childrenList: children || 'children',
  }

  let childrenListMap: any = {}
  let nodeIds: any = {}
  let tree: any = []

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

  function adaptToChildrenList(o: any) {
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

// 回显数据字典
export function selectDictLabel(datas: any, value: any) {
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
export function selectDictLabels(datas: any, value: any, separator: any) {
  if (value === undefined || value.length === 0) {
    return ''
  }
  if (Array.isArray(value)) {
    value = value.join(',')
  }
  var actions: any[] = []
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
