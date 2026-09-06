// @ts-nocheck
/**
 * 补0
 * @param number
 * @returns
 */

export function fillZero(number: number, length:number = 2): string {
	// if(isMillieconds) {
	// 	return `${number}`.padStart(3, '0')
	// }
	return `${number}`.padStart(length, '0')
}