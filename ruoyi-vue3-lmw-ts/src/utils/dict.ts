import useDictStore from '@/store/modules/dict'
import { getDicts } from '@/api/system/dict/data.ts'
import { reactive } from 'vue'
type DictItem = {
  label: string
  value: string
  elTagType?: string
  elTagClass?: string
}
type DictOptions = DictItem[]
/**
 * 获取字典数据
 */
export function useDict<T extends string[]>(...args: T) {
  const res = reactive(
    Object.fromEntries(args.map((key) => [key, new Array()]))
  ) as Record<T[number], DictOptions>

  args.forEach((dictType: T[number]) => {
    res[dictType] = []
    const dicts = useDictStore().getDict(dictType)
    if (dicts) {
      res[dictType] = dicts
    } else {
      getDicts(dictType).then((resp) => {
        res[dictType] = resp.data.map((p) => ({
          label: p.dictLabel,
          value: p.dictValue,
          elTagType: p.listClass,
          elTagClass: p.cssClass,
        }))
        useDictStore().setDict(dictType, res[dictType])
      })
    }
  })
  return toRefs(res)
}
