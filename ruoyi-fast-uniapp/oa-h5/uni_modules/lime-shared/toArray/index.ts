// @ts-nocheck
/**
 * 转成数组
 * */
export const toArray = <T>(item: T | T[]): T[] => Array.isArray(item) ? item : [item];