/**
 * @description 数组转
 * @param {Array} data  数据
 * @param {Object} props `{ parent: 'pid', children: 'children' }`
 */

import { cloneDeep } from 'lodash'
import moment from 'moment'
import CryptoJS from 'crypto-js'
export function arrayToTree(data = [], props = { id: 'id', parentId: 'pid', children: 'children' }) {
  data = cloneDeep(data)
  const { id, parentId, children } = props
  const result = []
  const map = new Map()
  data.forEach((item) => {
    map.set(item[id], item)
    const parent = map.get(item[parentId])
    if (parent) {
      parent[children] = parent[children] ? parent[children] : []
      parent[children].push(item)
    } else {
      result.push(item)
    }
  })
  const tree = []
  result.forEach((val) => {
    if (val[parentId] === 0) {
      tree.push(val)
    }
  })
  return tree
}

/**
 * @description 判断列表1中是否包含了列表2中的某一项
 * 因为用户权限 access 为数组，includes 方法无法直接得出结论
 * */
export function includeArray(list1, list2) {
  let status = false
  if (list1 === true) {
    return true
  } else {
    if (typeof list2 !== 'object') {
      return false
    }
    list2.forEach((item) => {
      if (list1.includes(item)) status = true
    })
    return status
  }
}

export function getAccount(phone, uuid) {
  let plaintext = {
    phone: phone,
    uuid: uuid
  }
  let key = CryptoJS.enc.Hex.parse('47a0715f25197583c9eec4d503602b62')
  let iv = CryptoJS.enc.Hex.parse('b515e18aa3fbe7d264d7ca5a95ef73e1')
  let encrypted = CryptoJS.AES.encrypt(JSON.stringify(plaintext), key, {
    iv: iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7
  }).toString()

  return btoa(encrypted)
}


export function loginRegex(type, length) {
  const regexObj = {
    0: {
      text: '纯数字至少'+length+'位',
      val: '^[0-9]{'+length+',}$'
    },
    // /^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\d)(?=.*?[!#@*&.])[a-zA-Z\d!#@*&.]{8,}$/
    '01': {
      text: '数字+大写字母至少'+length+'位',
      val: '^(?=.*[0-9])(?=.*[A-Z]).{'+length+',}$'
    },
    '02': {
      text: '数字+小写字母至少'+length+'位',
      val: '^(?=.*[0-9])(?=.*[a-z]).{'+length+',}$'
    },
    '03': {
      text: '数字+特殊字符至少'+length+'位',
      val: '^(?=.*[0-9])(?=.*?[!#@*&$%.]).{'+length+',}$'
    },
    12: {
      text: '大写字母+小写字母至少'+length+'位',
      val: '^(?=.*[a-z])(?=.*[A-Z])[a-zA-Z]{' + length + ',}$'
    },
    13: {
      text: '大写字母+特殊字符至少'+length+'位',
      val: '^(?=.*[A-Z])(?=.*?[!#@*&$%.])[A-Z!#@*&$%.].{' + length + ',}$'
    },
    23: {
      text: '小写字母+特殊字符至少'+length+'位',
      val:  '^(?=.*[a-z])(?=.*?[!#@*&$%.])[a-z!#@*&$%.].{' + length + ',}$'
    },
    '012': {
      text: '数字加小写字母加大写字母至少'+length+'位',
      val: '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{' + length + ',}$'
    },
    '0123': {
      text: '数字+小写字母+大写字母+特殊字符至少'+length+'位',
      val:
      '^(?=.*?[a-z])(?=.*?[A-Z])(?=.*[0-9])(?=.*?[!#@*&%$.])[a-zA-Z0-9!#@*&$%.]{'+length +',}$'
    },
    
    '013': {
      text: '数字+大写字母+特殊字符至少'+length+'位',
      val: '^(?=.*[A-Z])(?=.*[0-9])(?=.*?[!#@*&$%.])[A-Z0-9!#@*&$%.]{' + length + ',}$'
    },
    '023': {
      text: '数字+小写字母+特殊字符至少'+length+'位',
      val: '^(?=.*[a-z])(?=.*[0-9])(?=.*?[!#@*&%$.])[a-z0-9!#@*&$%.]{' + length + ',}$'
    },

    123: {
      text: '小写字母+大写字母+特殊字符至少'+length+'位',
      val: '^(?=.*[a-z])(?=.*[A-Z])(?=.*[!#@*&$%.])[A-Za-z!#@*&$%.]{' + length + ',}'
    }
  }


  return regexObj[type]
}

// 客户-合同 上传文件格式类型判断
export function toSrcFn(e) {
  if (!e) return false
  const index = e.lastIndexOf('.')
  const type = e.substring(index + 1, e.length)
  // 文档
  if (['doc', 'dot', 'wps', 'wpt', 'docx', 'dotx', 'docm', 'dotm', 'rtf'].includes(type)) {
    return 1
  }
  // ppt
  if (['ppt', 'pptx', 'pptm', 'ppsx', 'ppsm', 'pps', 'potx', 'potm', 'dpt', 'dps'].includes(type)) {
    return 2
  }
  // 表
  if (['xls', 'xlt', 'et', 'xlsx', 'xltx', 'csv', 'xlsm', 'xltm'].includes(type)) {
    return 3
  }
  // 图片
  if (['jpg', 'png', 'gif', 'jpeg', 'PNG'].includes(type)) {
    return 4
  }
  // pdf
  if (type == 'pdf') {
    return 5
  }
}

// 颜色转为带有透明度rgba背景颜色
export function getColor(thisColor, thisOpacity) {
  var theColor = thisColor.toLowerCase()
  var r = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/
  if (theColor && r.test(theColor)) {
    if (theColor.length === 4) {
      var sColorNew = '#'
      for (var i = 1; i < 4; i += 1) {
        sColorNew += theColor.slice(i, i + 1).concat(theColor.slice(i, i + 1))
      }
      theColor = sColorNew
    }
    var sColorChange = []
    for (var i = 1; i < 7; i += 2) {
      sColorChange.push(parseInt('0x' + theColor.slice(i, i + 2)))
    }
    return 'rgba(' + sColorChange.join(',') + ',' + thisOpacity + ')'
  }
}



export function toGetWeek(date) {
  // 参数时间戳
  let week = moment(date).day()
  const isWeek = {
    1: '周一',
    2: '周二',
    3: '周三',
    4: '周四',
    5: '周五',
    6: '周六',
    0: '周日'
  }
  return isWeek[week]
}
