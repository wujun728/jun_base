import { useDictStoreWithOut, Option, dictToOptions } from '@/store/modules/dict';
import { dictDataInfo } from '@/api/system/dict/dictData';
import { DictData } from '@/api/system/dict/dictData.model';

/**
 * 添加一个字典请求状态的缓存
 *
 * 主要解决多次请求重复api的问题(不能用abortController 会导致除了第一个其他的获取的全为空)
 * 比如在一个页面 index表单 modal drawer总共会请求三次 但是获取的都是一样的数据
 */
const dictRequestCache = new Map<string, Promise<void | DictData[]>>();

export function getDict(dictName: string): DictData[] {
  const { getDict, setDictInfo } = useDictStoreWithOut();
  // 这里拿到
  const dictList: DictData[] = getDict(dictName);
  if (dictList.length === 0) {
    // 检查请求状态缓存
    if (!dictRequestCache[dictName]) {
      dictRequestCache[dictName] = dictDataInfo(dictName).then((resp) => {
        // 缓存到store 这样就不用重复获取了
        setDictInfo(dictName, resp);
        // 这里不可用dictList = resp
        dictList.push(...resp);
        // 移除请求状态缓存
        dictRequestCache.delete(dictName);
      });
    }
  }
  return dictList;
}

export function getDictOptions(dictName: string): Option[] {
  const { getDictOptions, setDictInfo } = useDictStoreWithOut();
  const dictOptionList = getDictOptions(dictName);
  if (dictOptionList.length === 0) {
    // 检查请求状态缓存
    if (!dictRequestCache[dictName]) {
      dictRequestCache[dictName] = dictDataInfo(dictName).then((resp) => {
        // 缓存到store 这样就不用重复获取了
        setDictInfo(dictName, resp);
        // 转为options
        const option = dictToOptions(resp);
        // 这里不可用dictOptionList = option
        dictOptionList.push(...option);
        // 移除请求状态缓存
        dictRequestCache.delete(dictName);
      });
    }
  }
  return dictOptionList;
}
