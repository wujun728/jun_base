/** 
 * 通配符比较
 * @param {*} str 待比较的字符串
 * @param {*} pattern 含有*或者?通配符的字符串
 * @returns 
 */
export function wildcardCompare(str: string, pattern: string): boolean {
    const regex = pattern.replace(/[*?]/g, (match) => {
        if (match === '*') {
            return '.*';
        } else if (match === '?') {
            return '.';
        } else {
            return match
        }
    });
    const regexPattern = new RegExp('^' + regex + '$');
    return regexPattern.test(str);
}

/**
 * 深度复制
 * @param obj 待复制的对象
 * @returns 复制的对象
 */
export function deepClone(obj: any) {
    if (obj == null || typeof obj !== 'object') {
        return obj;
    }
    let result;
    if (Array.isArray(obj)) {
        result = [];
    } else {
        result = new Map();
    }
    for (let [key, value] of Object.entries(obj)) {
        // @ts-ignore
        result[key] = deepClone(value);
    }
    return result;
}

/**
 * 深度复制
 * @param obj 待复制的对象
 * @param result 要复制到的对象
 * @returns 复制的对象
 */
export function deepCloneTo<T>(obj: T, result: T) {
    if (obj == null || typeof obj !== 'object') {
        return obj;
    }
    for (let [key, value] of Object.entries(obj)) {
        // @ts-ignore
        result[key] = deepClone(value);
    }
    return result;
}

/**
 * 获取uuid
 * @returns 生成的uuid字符串
 */
export function generateUUID(): string {
    let uuid = '';
    const chars = '0123456789abcdef';
  
    for (let i = 0; i < 32; i++) {
      if (i === 8 || i === 12 || i === 16 || i === 20) {
        uuid += '-';
      }
      uuid += chars[Math.floor(Math.random() * chars.length)];
    }
  
    return uuid;
  }