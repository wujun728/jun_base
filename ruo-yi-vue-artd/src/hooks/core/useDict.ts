import { ref, onMounted } from 'vue'

import useDictStore from '@/store/modules/dict'

import { getDicts } from './api'

/**
 * 获取字典数据
 */
export function useDict(...args: string[]) {
  const dictRefs: Record<string, ReturnType<typeof ref<any[]>>> = {}

  for (const dictType of args) {
    dictRefs[dictType] = ref<any[]>([])
  }

  const load = async () => {
    for (const dictType of args) {
      const dicts = useDictStore().getDict(dictType)
      if (dicts) {
        dictRefs[dictType].value = dicts
      } else {
        const resp = await getDicts(dictType)
        dictRefs[dictType].value = (resp || []).map((p: any) => ({
          label: p.dictLabel,
          value: p.dictValue,
          elTagType: p.listClass,
          elTagClass: p.cssClass
        }))
        useDictStore().setDict(dictType, dictRefs[dictType].value || [])
      }
    }
  }

  onMounted(async () => {
    await load()
  })

  return dictRefs
}
