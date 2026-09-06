// @ts-nocheck
/** 是否为函数 */
export const isFunction = (val: unknown): val is Function =>
  typeof val === 'function'