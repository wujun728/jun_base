// @ts-nocheck
type Timeout = ReturnType<typeof setTimeout> | null;
/**
 * 防抖
 * @param fn 回调函数
 * @param wait 延迟时间
 * @returns
 */
export function debounce(fn : (...args : any[]) => void, wait = 300) {
	let timer : Timeout = null;
	return function (this : any, ...args : any[]) {
		if (timer) clearTimeout(timer);

		timer = setTimeout(() => {
			fn.apply(this, args);
		}, wait);
	};
}