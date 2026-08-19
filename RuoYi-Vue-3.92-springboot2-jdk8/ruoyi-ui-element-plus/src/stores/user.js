import { store } from "@/stores";
import { usePermissionStoreHook } from "@/stores/permission";
import { useDictStoreHook } from "@/stores/dict";
import { useTagsViewStore } from "@/stores";
import { cleanupSseServices } from "@/composables";

import AuthAPI from "@/api/auth";

import { AuthStorage } from "@/utils/auth";
import defAvatar from "@/assets/images/profile.jpg";

export const useUserStore = defineStore("user", () => {
  // 用户信息
  const userInfo = ref({});
  // 记住我状态
  const rememberMe = ref(AuthStorage.getRememberMe());

  // ============================================
  // 若依页面兼容字段（options 式访问：useUserStore().roles 等）
  // ============================================
  const token = computed(() => AuthStorage.getAccessToken());
  const id = computed(() => userInfo.value.userId ?? "");
  const name = computed(() => userInfo.value.userName ?? "");
  const nickName = computed(() => userInfo.value.nickName ?? "");
  const avatar = computed(() => userInfo.value.avatar ?? "");
  const roles = computed(() => userInfo.value.roles ?? []);
  const permissions = computed(() => userInfo.value.perms ?? []);
  const perms = computed(() => userInfo.value.perms ?? []);

  /**
   * 登录
   */
  async function login(loginRequest) {
    const data = await AuthAPI.login({
      username: loginRequest.username,
      password: loginRequest.password,
      code: loginRequest.captchaCode ?? loginRequest.code,
      uuid: loginRequest.captchaId ?? loginRequest.uuid,
    });
    AuthStorage.setToken(data.token);
    userInfo.value = {};
  }

  /**
   * 获取用户信息
   */
  async function getUserInfo() {
    const data = await AuthAPI.getInfo();
    if (!data || !data.user) {
      throw new Error("获取用户信息失败，请重新登录");
    }
    const user = data.user;
    // 头像处理：空则默认头像，相对路径拼 baseURL
    let avatar = user.avatar || "";
    if (!avatar.startsWith("http://") && !avatar.startsWith("https://")) {
      avatar = avatar ? import.meta.env.VITE_APP_BASE_API + avatar : defAvatar;
    }
    userInfo.value = {
      ...user,
      avatar,
      roles: data.roles || [],
      perms: data.permissions || [],
    };
    return userInfo.value;
  }

  /**
   * 登出
   */
  async function logout() {
    await AuthAPI.logout();
    resetAllState();
  }

  /**
   * 重置所有系统状态
   */
  function resetAllState() {
    // 1. 重置用户状态
    resetUserState();

    // 2. 重置其他模块状态
    usePermissionStoreHook().resetRouter();
    useDictStoreHook().clearDictCache();
    useTagsViewStore().delAllViews();

    // 3. 清理 SSE 连接
    cleanupSseServices();
  }

  /**
   * 重置用户状态
   */
  function resetUserState() {
    AuthStorage.clearAuth();
    userInfo.value = {};
  }

  return {
    userInfo,
    rememberMe,
    // 若依兼容字段
    token,
    id,
    name,
    nickName,
    avatar,
    roles,
    permissions,
    perms,
    isLoggedIn: () => !!AuthStorage.getAccessToken(),
    login,
    logout,
    logOut: logout,
    getUserInfo,
    getInfo: getUserInfo,
    resetAllState,
    resetUserState,
  };
});

/**
 * 在组件外部使用 UserStore 的钩子函数
 */
export function useUserStoreHook() {
  return useUserStore(store);
}