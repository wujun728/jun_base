// @ts-nocheck
/** 将数字字符转成数字类型 */
export function toNumber(val: string): number | string {
  const n = parseFloat(val)
  return isNaN(n) ? val : n
}