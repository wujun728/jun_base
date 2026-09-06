import { tansParams } from "./ruoyi";

/**
 * 获取uuid
 * @returns 生成的uuid字符串
 */
export function generateUUID(): string {
  let uuid = "";
  const chars = "0123456789abcdef";

  for (let i = 0; i < 32; i++) {
    if (i === 8 || i === 12 || i === 16 || i === 20) {
      uuid += "-";
    }
    uuid += chars[Math.floor(Math.random() * chars.length)];
  }

  return uuid;
}

/**
 * 获取code
 * @returns 生成的code字符串
 */
export async function getWxCode(appid?: string, redirect_uri?: string): Promise<string> {
  // #ifdef H5
  if (appid == undefined || redirect_uri == undefined) return ""
  let code = "";

  // 截取url中的code方法
  function getUrlCode() {
    let url = location.search;
    console.log(url);
    let theRequest: any = new Object();
    if (url.indexOf("?") != -1) {
      let str = url.substr(1);
      let strs = str.split("&");
      for (let i = 0; i < strs.length; i++) {
        theRequest[strs[i].split("=")[0]] = strs[i].split("=")[1];
      }
    }
    return theRequest as { code: string };
  }

  code = getUrlCode().code; // 截取code
  if (code == undefined || code == "" || code == null) {
    // 如果没有code，则去请求
    let href = "https://open.weixin.qq.com/connect/oauth2/authorize?" + tansParams({
      appid: appid,
      redirect_uri: redirect_uri,
      response_type: "code",
      scope: "snsapi_userinfo",
      state: "STATE",
    }) + "#wechat_redirect";
    setTimeout(() => window.location.href = href, 5000);
  } else {
    return code;
  }
  // #endif

  // #ifdef MP-WEIXIN
  // @ts-ignore
  const res = await wx.login();
  return res.code;
  // #endif
}
