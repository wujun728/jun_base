import { defineStore } from 'pinia';
import { DictData } from '@/api/system/dict/dictData.model';
import { store } from '@/store';

export type Option = { label: string; value: string; disabled?: boolean };

export function dictToOptions(data: DictData[]): Option[] {
  return data.map((item) => ({
    label: item.dictLabel,
    value: item.dictValue,
  }));
}

interface DictState {
  // 一般是dictTag使用
  dictMap: Map<string, DictData[]>;
  // select radio radioButton使用 只能为固定格式(Option)
  dictOptionsMap: Map<string, Option[]>;
}

export const useDictStore = defineStore('dict', {
  state: (): DictState => ({
    dictMap: new Map(),
    dictOptionsMap: new Map(),
  }),
  getters: {
    getDictMap(state) {
      return state.dictMap;
    },
    getDictOptionsMap(state) {
      return state.dictOptionsMap;
    },
  },
  actions: {
    setDictInfo(dictName: string, dictValue: DictData[]): void {
      this.dictMap.set(dictName, dictValue);
      this.dictOptionsMap.set(dictName, dictToOptions(dictValue));
    },
    getDict(dictName: string): DictData[] {
      if (!dictName) return [];
      // 没有key 添加一个空数组
      if (!this.dictMap.has(dictName)) {
        this.dictMap.set(dictName, []);
      }
      // 这里拿到的就不可能为空了
      return this.dictMap.get(dictName)!;
    },
    getDictOptions(dictName: string): Option[] {
      if (!dictName) return [];
      // 没有key 添加一个空数组
      if (!this.dictOptionsMap.has(dictName)) {
        this.dictOptionsMap.set(dictName, []);
      }
      // 这里拿到的就不可能为空了
      return this.dictOptionsMap.get(dictName)!;
    },
    resetCache() {
      this.dictMap.clear();
      this.dictOptionsMap.clear();
    },
  },
});

export function useDictStoreWithOut() {
  return useDictStore(store);
}
