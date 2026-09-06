// @ts-nocheck
import {isString} from '../isString'
import {isNumeric} from '../isNumeric'
// export const unitConvert = (value: number | string): number => {
//   if (typeof value === 'string') {
//     if (value.includes('rpx')) {
//       return (parseInt(value, 10) * (systemInfo?.screenWidth ?? 750)) / 750;
//     }
//     return parseInt(value, 10);
//   }
//   return value;
// };

/**
 * 将 rpx | px 转成 number
 */
export function unitConvert(value: string | number) : number{
	// 如果是字符串数字
	if (isNumeric(value)) {return Number(value)}
	// 如果有单位
	if (isString(value)) {
		const reg = /^-?([0-9]+)?([.]{1}[0-9]+){0,1}(em|rpx|px|%)$/g
		const results = reg.exec(value);
		if (!value || !results) {
			return 0;
		}
		const unit = results[3];
		value = parseFloat(value);
		if (unit === 'rpx') {
			return uni.upx2px(value);
		} 
		if (unit === 'px') {
			return value * 1;
		} 
	}
	return 0
}