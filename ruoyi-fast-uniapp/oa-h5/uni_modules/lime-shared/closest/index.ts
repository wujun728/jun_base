// @ts-nocheck
/**
 * 最近数值，目标值最近的数值
 * @参数 arr number[] 
 * @参数 target number
 * */
export function closest(arr: number[], target: number) {
  return arr.reduce((pre, cur) =>
    Math.abs(pre - target) < Math.abs(cur - target) ? pre : cur
  );
}