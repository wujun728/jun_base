const isDev = process.env.NODE_ENV === "development";
const devServerUrl = "https://develop.oa.crmebweb.com";

/**
 * 开发模式下将后端接口返回资源的链接中替换为空，使其走本地代理服务器，避免跨域问题
 * @param {string} url 远端资源的 url
 * @returns 经过处理后的 url
 */
export const processResourceUrl = (url) => isDev ? url.replace(devServerUrl, "") : url;