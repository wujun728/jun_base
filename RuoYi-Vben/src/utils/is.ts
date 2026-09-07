export {
  isArguments,
  isArrayBuffer,
  isArrayLike,
  isArrayLikeObject,
  isBuffer,
  isBoolean,
  isDate,
  isElement,
  isEmpty,
  isEqual,
  isEqualWith,
  isError,
  isFunction,
  isFinite,
  isLength,
  isMap,
  isMatch,
  isMatchWith,
  isNative,
  isNil,
  isNumber,
  isNull,
  isObjectLike,
  isPlainObject,
  isRegExp,
  isSafeInteger,
  isSet,
  isString,
  isSymbol,
  isTypedArray,
  isUndefined,
  isWeakMap,
  isWeakSet,
} from 'lodash-es';
const toString = Object.prototype.toString;

export function is(val: unknown, type: string) {
  return toString.call(val) === `[object ${type}]`;
}

export function isDef<T = unknown>(val?: T): val is T {
  return typeof val !== 'undefined';
}

// TODO 此处 isObject 存在歧义
export function isObject(val: any): val is Record<any, any> {
  return val !== null && is(val, 'Object');
}

// TODO 此处 isArray 存在歧义
export function isArray(val: any): val is Array<any> {
  return val && Array.isArray(val);
}

export function isWindow(val: any): val is Window {
  return typeof window !== 'undefined' && is(val, 'Window');
}

export const isServer = typeof window === 'undefined';

export const isClient = !isServer;

export function isHttpUrl(path: string): boolean {
  const reg = /^http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- ./?%&=]*)?/;
  return reg.test(path);
}

export function isPascalCase(str: string): boolean {
  const regex = /^[A-Z][A-Za-z]*$/;
  return regex.test(str);
}

/**
 * 上面的isHttpUrl不支持http://localhost/...
 * 
 * 测试
  https://www.baidu.com: true
  http://www.baidu.com: true
  http://localhost/aaa/bbb: true
  http://localhost:9090/aaa/bbb: true
  http://www.66.com:9090/aaa/bbb: true
  http://www.abc.com5555:9090/aaa/bbb: true
  http://127.0.0.1:9090/aaa/bbb: true
  httpsdsadsa://abcd/aaa/bbb: false
  http://////abcd/aaa/bbb: false
 * 
 * 感觉判断是不是http(s)://开头就行了 也没人用http://做组件路径吧
 * @param path 
 * @returns 
 */
export function isUrl(path: string): boolean {
  /** 支持端口号 */
  const reg = /^(https?|HTTPS?):\/\/[^\s/:$.?#].[^\s]*(:[0-9]+)?(\/[^\s]*)?$/;
  return reg.test(path);
}
