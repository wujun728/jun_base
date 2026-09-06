/**
 * 字典数据组合式函数
 * 简化字典的使用方式
 */
import { reactive, toRefs } from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 字典数据缓存
const dictCache = reactive({});

/**
 * 使用字典数据
 * @param {...string} dictTypes - 字典类型数组
 * @returns {Object} 字典数据对象
 *
 * @example
 * const { sys_notice_status, sys_notice_type } = useDict("sys_notice_status", "sys_notice_type");
 */
export function useDict(...dictTypes) {
  const dicts = reactive({});

  // 异步加载字典数据
  const loadDictData = async (dictType) => {
    try {
      console.log(`🔄 开始加载字典数据: ${dictType}`);
      const data = await BearJiaUtil.getDictsByType(dictType);

      // 更新缓存和响应式数据
      dictCache[dictType].splice(0, dictCache[dictType].length, ...data);

      console.log(`✅ 字典数据加载完成: ${dictType}`, data);
    } catch (error) {
      console.error(`❌ 字典数据加载失败: ${dictType}`, error);
    }
  };

  // 为每个字典类型创建响应式数据
  dictTypes.forEach((dictType) => {
    // 如果缓存中已存在，直接使用缓存
    if (dictCache[dictType]) {
      dicts[dictType] = dictCache[dictType];
    } else {
      // 创建新的响应式数据
      dicts[dictType] = [];
      dictCache[dictType] = dicts[dictType];

      // 异步加载字典数据
      loadDictData(dictType);
    }
  });

  // 返回响应式的字典数据
  return toRefs(dicts);
}

/**
 * 获取字典标签
 * @param {string|number} value - 字典值
 * @param {Array} options - 字典选项数组
 * @returns {string} 字典标签
 */
export function getDictLabel(value, options) {
  if (!options || !Array.isArray(options)) {
    return value;
  }

  // 兼容两种数据结构：
  // 1. BearJiaUtil 格式：{ value, label }
  // 2. 标准格式：{ dictValue, dictLabel }
  const item = options.find((option) => {
    const dictValue = option.dictValue || option.value;
    return dictValue === String(value);
  });

  if (item) {
    return item.dictLabel || item.label;
  }

  return value;
}

/**
 * 获取字典值
 * @param {string} label - 字典标签
 * @param {Array} options - 字典选项数组
 * @returns {string} 字典值
 */
export function getDictValue(label, options) {
  if (!options || !Array.isArray(options)) {
    return label;
  }

  // 兼容两种数据结构
  const item = options.find((option) => {
    const dictLabel = option.dictLabel || option.label;
    return dictLabel === label;
  });

  if (item) {
    return item.dictValue || item.value;
  }

  return label;
}

/**
 * 根据字典值获取样式类名
 * @param {string|number} value - 字典值
 * @param {Array} options - 字典选项数组
 * @returns {string} 样式类名
 */
export function getDictClass(value, options) {
  if (!options || !Array.isArray(options)) {
    return '';
  }

  // 兼容两种数据结构
  const item = options.find((option) => {
    const dictValue = option.dictValue || option.value;
    return dictValue === String(value);
  });

  return item ? item.cssClass || '' : '';
}

/**
 * 根据字典值获取标签类型
 * @param {string|number} value - 字典值
 * @param {Array} options - 字典选项数组
 * @returns {string} 标签类型
 */
export function getDictTagType(value, options) {
  if (!options || !Array.isArray(options)) {
    return 'default';
  }

  // 兼容两种数据结构
  const item = options.find((option) => {
    const dictValue = option.dictValue || option.value;
    return dictValue === String(value);
  });

  return item ? item.listClass || 'default' : 'default';
}

/**
 * 清除字典缓存
 * @param {string} dictType - 字典类型，不传则清除所有缓存
 */
export function clearDictCache(dictType) {
  if (dictType) {
    delete dictCache[dictType];
    console.log(`🗑️ 已清除字典缓存: ${dictType}`);
  } else {
    Object.keys(dictCache).forEach((key) => {
      delete dictCache[key];
    });
    console.log('🗑️ 已清除所有字典缓存');
  }
}

/**
 * 刷新字典数据
 * @param {string} dictType - 字典类型
 */
export function refreshDict(dictType) {
  if (dictCache[dictType]) {
    clearDictCache(dictType);
    // 重新加载
    const { [dictType]: refreshedDict } = useDict(dictType);
    return refreshedDict;
  }
}

// 默认导出
export default {
  useDict,
  getDictLabel,
  getDictValue,
  getDictClass,
  getDictTagType,
  clearDictCache,
  refreshDict,
};
