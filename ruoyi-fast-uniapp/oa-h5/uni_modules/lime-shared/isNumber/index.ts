// @ts-nocheck
/**判断是否为数字类型*/
export function isNumber(value: number|string) {
  return typeof value === 'number' ///^(-)?\d+(\.\d+)?$/.test(value);
}
