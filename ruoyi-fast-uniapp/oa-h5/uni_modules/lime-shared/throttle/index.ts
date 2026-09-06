// @ts-nocheck
/**
 * 节流
 * @param fn 回调函数
 * @param delay 延迟时间
 * @returns
 */
export function throttle(fn: (...args: any[]) => void, delay: number) {
  let flag = true;
  return (...args: any[]) => {
    if (flag) {
      flag = false;
      fn(...args);
      setTimeout(() => {
        flag = true;
      }, delay);
    }
  };
}