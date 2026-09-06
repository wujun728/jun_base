/**
 * 日志工具类 - 在生产环境中自动禁用日志输出
 */
const isDev = import.meta.env.DEV;

/**
 * 开发环境日志
 * @param {...any} args 日志参数
 */
export const log = (...args) => {
  if (isDev) {
    console.log(...args);
  }
};

/**
 * 开发环境警告日志
 * @param {...any} args 日志参数
 */
export const warn = (...args) => {
  if (isDev) {
    console.warn(...args);
  }
};

/**
 * 错误日志（生产环境也会输出）
 * @param {...any} args 日志参数
 */
export const error = (...args) => {
  console.error(...args);
};

/**
 * 开发环境信息日志
 * @param {...any} args 日志参数
 */
export const info = (...args) => {
  if (isDev) {
    console.info(...args);
  }
};

/**
 * 开发环境调试日志
 * @param {...any} args 日志参数
 */
export const debug = (...args) => {
  if (isDev) {
    console.debug(...args);
  }
};

export default {
  log,
  warn,
  error,
  info,
  debug,
};
