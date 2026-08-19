/**
 * 数据验证相关工具函数
 */

/**
 * 判断是否是外部链接
 * @param path 路径字符串
 * @returns 是否是外部链接
 *
 * @example
 * ```js
 * isExternal('https://example.com'); // true
 * isExternal('/dashboard'); // false
 * isExternal('mailto:admin@example.com'); // true
 * ```
 */
export function isExternal(path) {
  return /^(https?:|http?:|mailto:|tel:)/.test(path);
}

/**
 * 判断是否是有效的 URL
 * @param url URL 字符串
 * @returns 是否是有效 URL
 *
 * @example
 * ```js
 * isValidURL('https://example.com'); // true
 * isValidURL('not a url'); // false
 * ```
 */
export function isValidURL(url) {
  try {
    new URL(url);
    return true;
  } catch {
    return false;
  }
}

/**
 * 判断是否是邮箱地址
 * @param email 邮箱字符串
 * @returns 是否是有效邮箱
 */
export function isEmail(email) {
  const pattern = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/;
  return pattern.test(email);
}

/**
 * 判断是否是手机号码（中国大陆）
 * @param mobile 手机号字符串
 * @returns 是否是有效手机号
 */
export function isMobile(mobile) {
  const pattern = /^1[3|4|5|6|7|8|9][0-9]\d{8}$/;
  return pattern.test(mobile);
}

/**
 * 表单验证规则生成器
 */
export const VALIDATORS = {
  /** 必填项验证 */
  required(message) {
    return { required: true, message, trigger: "blur" };
  },

  /** 邮箱验证 */
  email: {
    type: "email",
    message: "请输入正确的邮箱地址",
    trigger: "blur",
  },

  /** 手机号验证 */
  mobile: {
    pattern: /^1[3-9]\d{9}$/,
    message: "请输入正确的手机号码",
    trigger: "blur",
  },

  /** URL 验证 */
  url: {
    type: "url",
    message: "请输入正确的URL地址",
    trigger: "blur",
  },

  /** 数字验证 */
  number: {
    type: "number",
    message: "请输入数字",
    trigger: "blur",
  },

  /** 整数验证 */
  integer: {
    type: "integer",
    message: "请输入整数",
    trigger: "blur",
  },
};
