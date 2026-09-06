// 用于收集contentConfig的所有插槽名称
export const collectObjectsWithSlotName = (data, exceptSlot = []) => {
  const collectedObjects = []
  function collect(data, exceptSlot = []) {
    if (Array.isArray(data)) {
      // 如果当前层级是数组
      data.forEach((item) => {
        collect(item, exceptSlot)
      })
    } else if (typeof data === 'object' && data !== null) {
      // 如果当前层级是对象
      if ('slotName' in data && !exceptSlot.includes(data.slotName)) {
        collectedObjects.push(data) // 当前对象有 slotName 属性，将其加入结果数组
      }
      Object.values(data).forEach((value) => {
        collect(value, exceptSlot)
      })
    }
  }
  collect(data, exceptSlot)
  return collectedObjects
}

// 用于判断父组件使用是否有某插槽
export const hasSlot = (slots, arr) => {
  return arr.some((key) => slots.hasOwnProperty(key))
}
