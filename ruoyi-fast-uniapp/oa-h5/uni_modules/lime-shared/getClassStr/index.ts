// @ts-nocheck
/**
 * 把 class 对象转成字符串
 */
export function getClassStr<T>(obj: T): string {
  let classNames: string[] = [];
  for (let key in obj) {
    if ((obj as any).hasOwnProperty(key) && obj[key]) {
      classNames.push(key);
    }
  }
  return classNames.join(' ');
}