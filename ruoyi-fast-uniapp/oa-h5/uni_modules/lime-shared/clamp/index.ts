// @ts-nocheck
/**
 * 夹在min和max之间的值，不能小于min，也不能大于max
 * @参数 min number
 * @参数 max number
 * @参数 val number
 * @返回 number
 * */
export function clamp(min:number, max:number, val:number):number {
  return Math.max(min, Math.min(max, val))
}