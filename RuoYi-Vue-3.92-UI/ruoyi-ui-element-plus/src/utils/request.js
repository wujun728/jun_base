import axios from "axios";
import qs from "qs";
import { ApiCodeEnum } from "@/enums/api";
import { AuthStorage, redirectToLogin } from "@/utils/auth";
import { ElMessage, ElLoading } from "element-plus";
import { tansParams, blobValidate } from "@/utils/ruoyi";

// HTTP 请求实例
const http = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 50000,
  headers: { "Content-Type": "application/json;charset=utf-8" },
  paramsSerializer: (params) => qs.stringify(params, { arrayFormat: "repeat" }),
});

// 请求拦截器
http.interceptors.request.use(
  (config) => {
    const token = AuthStorage.getAccessToken();

    if (config.headers.isToken === false) {
      delete config.headers.isToken;
    } else if (config.headers.Authorization === "no-auth") {
      delete config.headers.Authorization;
    } else if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器
http.interceptors.response.use(
  (response) => {
    const { responseType } = response.config;

    // 二进制数据直接返回
    if (responseType === "blob" || responseType === "arraybuffer") {
      return response;
    }

    const { code, msg } = response.data;

    if (code === ApiCodeEnum.SUCCESS) {
      return response.data;
    }

    // 未授权 / Token 失效
    if (code === ApiCodeEnum.UNAUTHORIZED) {
      redirectToLogin("登录状态已过期，请重新登录", false);
      return Promise.reject(new Error(msg || "登录状态已过期"));
    }

    ElMessage.error(msg || "系统出错");
    return Promise.reject(new Error(msg || "系统出错"));
  },

  (error) => {
    const { response } = error;

    if (!response) {
      ElMessage.error("网络连接失败");
      return Promise.reject(error);
    }

    ElMessage.error(response.data?.msg || "请求失败");
    return Promise.reject(new Error(response.data?.msg || "请求失败"));
  }
);

// 通用下载方法（若依后端导出：POST + blob）
export function download(url, params, filename, config) {
  const loadingInstance = ElLoading.service({
    text: "正在下载数据，请稍候",
    background: "rgba(0, 0, 0, 0.7)",
  });
  return http
    .post(url, params, {
      transformRequest: [(p) => tansParams(p)],
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      responseType: "blob",
      ...config,
    })
    .then(async (res) => {
      const blobData = res.data;
      const isBlob = blobValidate(blobData);
      if (isBlob) {
        const blob = new Blob([blobData]);
        const downloadUrl = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = downloadUrl;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(downloadUrl);
      } else {
        const resText = await blobData.text();
        const rspObj = JSON.parse(resText);
        ElMessage.error(rspObj.msg || "下载失败");
      }
      loadingInstance.close();
    })
    .catch(() => {
      ElMessage.error("下载文件出现错误，请联系管理员！");
      loadingInstance.close();
    });
}

export default http;