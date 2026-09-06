/**
 * 生成指定范围的数组 
 *
 * @private
 * @param {number} start 开始的范围.
 * @param {number} end 结束的范围.
 * @param {number} step 范围的增量 或者 减量.
 * @param {boolean} [fromRight] 反向.
 * @returns {Array} Returns 范围数组.
 */
export function range(start: number, end: number, step: number = 1, fromRight: boolean = false): number[] {
  let index = -1
  let length = Math.max(Math.ceil((end - start) / (step || 1)), 0)
  const result = new Array(length)

  while (length--) {
    result[fromRight ? length : ++index] = start
    start += step
  }
  return result
}