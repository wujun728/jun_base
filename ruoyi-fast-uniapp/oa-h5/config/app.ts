// 接口请求地址 格式:`https://您的域名`
let httpApi = "https://oa.pairui.vip";
// let httpApi = "";
// 长连接请求地址 格式:`wss://您的域名 可以不用填写,下面会根据接口请求地址配置的`
let httpWs = "";

if (!uni.getStorageSync("chooseApiUrl")) {
  uni.setStorageSync("chooseApiUrl", httpApi);
} else {
  httpApi = uni.getStorageSync("chooseApiUrl");
}

// H5端配置:H5接口是浏览器地址，非单独部署不用修改
// #ifdef H5
httpApi = window.location.protocol + "//" + window.location.host;

httpWs = `${window.location.protocol.indexOf("https") > -1 ? "wss:" : "ws:"}//` + window.location.host;
// #endif

// APP端配置
// #ifdef APP-VUE
const match = httpApi.match(/(https?|http):\/\/(www\.)?([^\/]+)/);
if (match) {
  httpWs = `${httpApi.indexOf("https") > -1 ? "wss:" : "ws:"}//` + match[3];
}
// #endif

export const HTTP_REQUEST_URL = httpApi;
export const VUE_APP_WS_URL = httpWs;

// 以下配置在不做二开的前提下,不需要做任何的修改
// tslint:disable-next-line:interface-name
interface Header {
  "content-type": string;
  "Form-type": string;
  [key: string]: any;
}
export const HEADER: Header = {
  "content-type": "application/json",
  // #ifdef H5
  "Form-type": navigator.userAgent.toLowerCase().indexOf("micromessenger") !== -1 ? "wechat" : "h5",
  // #endif
  // #ifdef APP-VUE
  "Form-type": "app",
  // #endif
  // @ts-ignore
  // #ifdef MP-WEIXIN
  "Form-type": "wechat",
  // #endif
};
// 会话密钥名称 请勿修改此配置
export const TOKENNAME = "Authorization";
export const APPVERSION = "AppVersion";
// 缓存时间 0 永久
export const EXPIRE = 0;
// 分页最多显示条数
export const LIMIT = 10;

// 默认全局api入口，一般不用修改
export const BASEAPI = "/api/uni/";
