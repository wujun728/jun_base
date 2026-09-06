import useDictStore from '@/store/modules/dict'
import { getDicts } from '@/api/system/dict/data'
import { toRefs, ref, Ref } from 'vue';

/**
 * 获取字典数据
 */
export function useDict<const T extends string[]>(...args: T) {
  const res = ref({} as Record<T[number], Array<DictValue>>);
  args.forEach((dictType) => {
    res.value[dictType] = [];
    const dicts = useDictStore().getDict(dictType);
    if (dicts) {
      res.value[dictType] = dicts;
    } else {
      getDicts(dictType).then((resp: { data: Array<Dict> }) => {
        res.value[dictType] = resp.data.map(p => ({
          label: p.dictLabel,
          value: p.dictValue,
          elTagType: p.listClass,
          elTagClass: p.cssClass
        }));
        useDictStore().setDict(dictType, res.value[dictType]);
      });
    }
  });
  return toRefs(res.value) as { [K in T[number]]: Ref<Array<DictValue>> };
}