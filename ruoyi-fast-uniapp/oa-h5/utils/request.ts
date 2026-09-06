import { HTTP_REQUEST_URL, HEADER, TOKENNAME, BASEAPI, APPVERSION } from "@/config/app";
import { toLogin } from "@/libs/login";
import type { Res } from "@/utils/typeHelper";
import message from "./message";
import store from "../store";
/**
 * 发送请求
 */
function baseRequest(url: string, method: UniNamespace.RequestOptions["method"], data: object, { noAuth = false, noVerify = false }) {
  // let Url = HTTP_REQUEST_URL
  const Url = uni.getStorageSync("chooseApiUrl") || HTTP_REQUEST_URL;
  const header = HEADER;
  let networkType = "";
  if (store.state.app.token) {
    header[TOKENNAME] = "Bearer " + store.state.app.token;
  } else {
    header[TOKENNAME] = "Bearer ";
  }
  // #ifdef APP-PLUS
  header[APPVERSION] = uni.getSystemInfoSync().appWgtVersion;
  // #endif
  // #ifdef H5
  header[APPVERSION] = uni.getSystemInfoSync().appVersion;

  // 判断是否在问卷调查页面，如果是且本地保存了 UNIQUE，则添加到请求头中
  if (location.pathname.includes("/pages/module/questionnaire")) {
    const CURD_UNIQUE = uni.getStorageSync("QUESTIONNAIRE_CURD_UNIQUE");
    if (CURD_UNIQUE) {
      header["Curd-Unique"] = CURD_UNIQUE;
    }
  }

  // #endif
  uni.getNetworkType({
    success: (res) => {
      networkType = res.networkType;
    }
  });
  return new Promise((reslove, reject) => {
    if (networkType === "none") {
      message.error("网络开小差了！");
      return false;
    }
    uni.request({
      url: Url + BASEAPI + url,
      method: method,
      header: header,
      data: data || {},
      success: (res: UniApp.RequestSuccessCallbackResult | Res) => {
        if (noVerify)
          reslove(res.data);
        else if (res.data.status == 200)
          reslove(res.data);
        else if ([410000, 410001, 410002, 410003].indexOf(res.data.status) !== -1) {
          toLogin();
          res.data.message = "登录已过期，请重新登录";
          reject(res.data);
        } else {
          reject(res.data);
        }
      },
      fail: () => {
        reject({ message: "请求失败" });
      }
    });

    uni.addInterceptor("request", {
      invoke(args) {
        // 处理get请求发送数组的问题
        const { data, method } = args;
        if (method === "get" && Object.keys(data).length > 0) {
          const arrKey = Object.keys(data);
          const arrValue = Object.values(data) as any[];
          let str = "";
          for (let i = 0; i < arrValue.length; i++) {
            if (typeof arrValue[i] === "object") {
              for (let j = 0; j < arrValue[i].length; j++) {
                str += `&${arrKey[i]}[]=${arrValue[i][j]}`;
              }
            } else {
              str += `&${arrKey[i]}=${arrValue[i]}`;
            }
          }

          str = str.slice(1);
          args.data = {};
          args.url = `${args.url}?${str}`;
        }
      },
      success() { },
      fail() { },
      complete() { }
    });
  });
}

const request: { [method: string]: (api: string, data?: object, opt?: object) => any } = {};

["options", "get", "post", "put", "head", "delete", "trace", "connect"].forEach((method) => {
  request[method] = (api: string, data: object, opt: object) => baseRequest(api, method as UniNamespace.RequestOptions["method"], data, opt || {});
});

export default request;
