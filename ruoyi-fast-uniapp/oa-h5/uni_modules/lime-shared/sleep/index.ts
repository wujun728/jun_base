// @ts-nocheck
/**
 * @param delay 延时时间
 */
export const sleep = (delay: number = 300) => new Promise(resolve => setTimeout(resolve, delay))