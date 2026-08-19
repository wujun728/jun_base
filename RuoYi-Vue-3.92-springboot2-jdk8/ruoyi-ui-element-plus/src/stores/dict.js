import { store } from "@/stores";
import DictAPI from "@/api/system/dict";
import { STORAGE_KEYS } from "@/constants";

export const useDictStore = defineStore("dict", () => {
  // 字典数据缓存
  const dictCache = useStorage(STORAGE_KEYS.DICT_CACHE, {});

  // ============================================
  // 若依兼容：字典数组缓存（options 式访问）
  // ============================================
  const dict = ref([]);

  // 请求队列（防止重复请求）
  const requestQueue = {};

  /**
   * 缓存字典数据
   * @param dictCode 字典编码
   * @param data 字典项列表
   */
  const cacheDictItems = (dictCode, data) => {
    dictCache.value[dictCode] = data;
  };

  /**
   * 加载字典数据（如果缓存中没有则请求）
   * @param dictCode 字典编码
   */
  const loadDictItems = async (dictCode) => {
    if (dictCache.value[dictCode]) return;
    // 防止重复请求
    if (!requestQueue[dictCode]) {
      requestQueue[dictCode] = DictAPI.getDictItems(dictCode)
        .then((data) => {
          cacheDictItems(dictCode, data);
          Reflect.deleteProperty(requestQueue, dictCode);
        })
        .catch((error) => {
          // 请求失败，清理队列，允许重试
          Reflect.deleteProperty(requestQueue, dictCode);
          throw error;
        });
    }
    await requestQueue[dictCode];
  };

  /**
   * 获取字典项列表
   * @param dictCode 字典编码
   * @returns 字典项列表
   */
  const getDictItems = (dictCode) => {
    return dictCache.value[dictCode] || [];
  };

  /**
   * 移除指定字典项
   * @param dictCode 字典编码
   */
  const removeDictItem = (dictCode) => {
    if (dictCache.value[dictCode]) {
      Reflect.deleteProperty(dictCache.value, dictCode);
    }
  };

  /**
   * 清空字典缓存
   */
  const clearDictCache = () => {
    dictCache.value = {};
  };

  // ============================================
  // 若依兼容接口：getDict / setDict / removeDict / cleanDict
  // ============================================
  const getDict = (key) => {
    if (key == null || key === "") return null;
    const found = dict.value.find((item) => item.key === key);
    return found ? found.value : null;
  };

  const setDict = (key, value) => {
    if (key !== null && key !== "") {
      dict.value.push({ key, value });
    }
  };

  const removeDict = (key) => {
    const index = dict.value.findIndex((item) => item.key === key);
    if (index !== -1) {
      dict.value.splice(index, 1);
      return true;
    }
    return false;
  };

  const cleanDict = () => {
    dict.value = [];
  };

  return {
    loadDictItems,
    getDictItems,
    removeDictItem,
    clearDictCache,
    // 若依兼容接口
    dict,
    getDict,
    setDict,
    removeDict,
    cleanDict,
  };
});

export function useDictStoreHook() {
  return useDictStore(store);
}
