/**
 * 节流
 * @param {*} fn 执行函数
 * @param {*} wait 节流时间,毫秒
 */
export const useThrottle = function (fn: Function, wait?: number) {
    let timer: any;
    return function (this: any) {
        if (!timer) {
            timer = setTimeout(() => {
                fn.apply(this, arguments)
                timer = null
            }, wait)
        }
    }
}

/**
 * 防抖
 * @param {*} fn 执行函数
 * @param {*} wait 防抖时间,毫秒
 */
export const useDebounce = function (fn: Function, wait: number) {
    let timer: any
    return function (this: any) {
        // 如果多次触发将上次记录延迟清除掉
        if (timer !== null) {
            clearTimeout(timer)
        } else {
            timer = setTimeout(() => {
                fn.apply(this, arguments)
                timer = null
            }, wait)
        }
    }
}