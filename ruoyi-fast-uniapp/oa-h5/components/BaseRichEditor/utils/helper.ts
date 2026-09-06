import { EDITOR_RUN_COMMAND } from "../constant";

/**
 * 从点击元素的 dataset 属性中获取需要的值
 * @param e Event 点击事件
 * @param key 要从dataset中提取的 key 值
 * @returns null | string
 */
export const getDataSetValue = <T = string>(e: Event, key: string) => {
  if ((e.target as HTMLDivElement).dataset[key] !== undefined) {
    return (e.target as HTMLDivElement).dataset[key] as T;
  }
  return null;
}

/**
 * 统一向顶级编辑器组件发送指令，以便运行指定的指令
 * @param args editorContext 能执行的命令
 */
export const runCommand = (...args: any[]) => {
  uni.$emit(EDITOR_RUN_COMMAND, ...args);
}

export type EventHandler = (e: Event) => void;

/**
 * 根据 enum 值获取 enum 的 key
 * @param enumObj Enum枚举
 * @param value enum 值
 * @returns enum key
 */
export const getEnumKeyByValue = <T extends { [key: string]: U }, U>(
  enumObj: T,
  value: U
): keyof T => {
  const index = Object.values(enumObj).indexOf(value);
  return Object.keys(enumObj)[index];
}


/**
 * 节流函数
 * @param fn 
 * @param delay 
 * @returns 
 */
export const throttle = (fn: Function, delay: number = 300) => {
  let timeoutFlag: number | undefined;
  return (...args: any[]) => {
    if (timeoutFlag) return;
    fn(...args);
    timeoutFlag = setTimeout(() => {
      clearTimeout(timeoutFlag);
      timeoutFlag = undefined;
    }, delay);
  };
}

/**
 * 等待 time 毫秒
 * @param time 等待时间
 * @returns Promies
 */
export const sleep = (time: number = 300) => {
  return new Promise((resolve) => {
    setTimeout(resolve, time)
  });
}