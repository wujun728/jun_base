import * as Icons from "@vicons/ionicons5";

/**
 * 简单实现对象的深度克隆
 * 注意：
 *      1、使用该方式会出现一些问题，如值为undefined、函数、Date类型等属性会无法被克隆，此处待完善！！！
 *      2、非得使用undefined的话，可以使用null替代
 * @param obj
 */
export function useDeepClone<T>(obj: T): T {
    // 如果是基本数据类型，直接返回
    if (typeof obj !== 'object' || obj === null) {
        return obj;
    }
    // 根据类型创建新对象或数组
    const newObj = Array.isArray(obj) ? [] : {};
    // 递归复制属性或元素
    for (const key in obj) {
        // @ts-ignore
        newObj[key] = useDeepClone(obj[key]);
    }
    return newObj as T;
}