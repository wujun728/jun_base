import { Storage } from "./storage";
import { ROLE_ROOT } from "@/constants";
import { useUserStoreHook } from "@/stores/user";
import router from "@/router";

// ============================================
// Token 存储管理（若依后端：单 token）
// ============================================

const TOKEN_KEY = "Admin-Token";

export const AuthStorage = {
  getAccessToken() {
    return Storage.get(TOKEN_KEY, "");
  },

  setToken(token) {
    Storage.set(TOKEN_KEY, token);
  },

  clearAuth() {
    Storage.remove(TOKEN_KEY);
    Storage.sessionRemove(TOKEN_KEY);
  },

  getRememberMe() {
    return false;
  },
};

// 若依组件兼容导出（Editor / FileUpload / ImageUpload / ExcelImportDialog 等）
export const getToken = () => AuthStorage.getAccessToken();
export const setToken = (token) => AuthStorage.setToken(token);
export const removeToken = () => AuthStorage.clearAuth();

// ============================================
// 权限判断
// ============================================

/**
 * 权限判断
 * @param {string | string[]} value 权限值或角色值
 * @param {"button" | "role"} type 判断类型
 * @returns {boolean}
 */
export function hasPerm(value, type = "button") {
  const { roles, perms } = useUserStoreHook().userInfo;

  if (!roles || !perms) {
    return false;
  }

  // 超级管理员拥有所有权限
  if (type === "button" && roles.includes(ROLE_ROOT)) {
    return true;
  }

  const auths = type === "button" ? perms : roles;
  return typeof value === "string"
    ? auths.includes(value)
    : value.some((perm) => auths.includes(perm));
}

// ============================================
// 重定向到登录页面
// ============================================

/**
 * 重定向到登录页面
 * @param {string} message 提示消息
 * @param {boolean} notify 是否显示通知
 */
let redirectingToLogin = false;

export async function redirectToLogin(message = "请重新登录", notify = true) {
  if (redirectingToLogin) return;
  redirectingToLogin = true;

  if (notify) {
    ElNotification({
      title: "提示",
      message,
      type: "warning",
      duration: 3000,
    });
  }

  await useUserStoreHook().resetAllState();

  try {
    // 跳转到登录页，保留当前路由用于登录后跳转
    const currentPath = router.currentRoute.value.fullPath;
    await router.push(`/login?redirect=${encodeURIComponent(currentPath)}`);
  } catch (error) {
    console.error("Redirect to login error:", error);
    // 强制跳转，即使路由重定向失败
    window.location.href = "/login";
  } finally {
    redirectingToLogin = false;
  }
}
