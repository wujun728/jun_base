// @ts-nocheck
/**判断字符串是否表示一个数值*/
export function isNumeric(value: string | number) {
	return /^(-)?\d+(\.\d+)?$/.test(value);
}
