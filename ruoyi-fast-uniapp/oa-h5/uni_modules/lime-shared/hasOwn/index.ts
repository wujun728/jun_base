// @ts-nocheck
const hasOwnProperty = Object.prototype.hasOwnProperty
/**
 * 检查对象是否具有该属性
 */
export function hasOwn(obj: Object | Array<any>, key: string): boolean {
  return hasOwnProperty.call(obj, key)
}