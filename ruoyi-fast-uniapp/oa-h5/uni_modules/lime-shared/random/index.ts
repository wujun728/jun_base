// @ts-nocheck
/**
 * 返回区域内随机数
 * @param min 最小值
 * @param max 最大值
 * @param fixed 保留几位小数，默认0
 * @returns
 */
export function random(min: number, max: number, fixed: number = 0) {
  min = +min || 0;
  max = +max || 0;
  const num = Math.random() * (max - min) + min
  return fixed == 0 ? Math.round(num) : Number(num.toFixed(fixed))
}
