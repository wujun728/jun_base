import { isRef } from 'vue'
import hasPermi from '@/utils/hasPermi'
export default (config, dictsMap) => {
  const dictsMapValue = isRef(dictsMap) ? dictsMap.value : dictsMap
  for (const [key, dict] of Object.entries(dictsMapValue)) {
    const fromItem = config.formItems?.find((item) => {
      return item.field === key
    })
    if (fromItem) {
      if (isRef(fromItem.options)) {
        fromItem.options.value = dict.value || dict || []
      } else {
        fromItem.options = dict.value || dict || []
      }
    }
  }
  const fromItem = config.formItems.filter((item) => {
    if (!item.permission) return true
    return hasPermi(item.permission)
  })
  config.formItems = fromItem
  return config
}
