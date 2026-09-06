/**
 * 该文件为开发时使用的工具类，正式环境打包时请勿引入
 */

/**
 * 生成随机个数的指定汉字
 * @param count 要生成文字的数量
 * @returns string
 */
export const generateRandomZHText = (count: number): string => {
  return Array.from(Array(count))
    .map(() => {
      return String.fromCodePoint(Math.round(Math.random() * 10901) + 19968);
    })
    .join("");
};
