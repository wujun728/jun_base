import { useStore } from "vuex";

type CheckLoginFunc = (callback?: () => void) => void;

type CheckLoginHookReturnType = [
  Ref<boolean>,
  CheckLoginFunc
];

/**
 * 检查当前是否处于登录态，如果处于登录态则执行执行相关回调（可选）
 * 如果未登录，则保存当前路径和参数，并跳转到登录页面，登录完成后将跳转回当前页面
 */
export const useCheckLogin = (): CheckLoginHookReturnType => {
  const store = useStore();
  const isLogin = computed(() => store.state.app.isLogin);

  const handleCheckLogin: CheckLoginFunc = (callback) => {
    if (isLogin.value) {
      if (callback) {
        callback();
      }
    } else {
      const pages = getCurrentPages();
      const currentPath = pages[pages.length - 1].$vm.$page.fullPath;
      store.commit("setLoginBackUrl", currentPath);
      uni.redirectTo({
        url: "/pages/users/login/index"
      });
    }
  };

  return [
    isLogin,
    handleCheckLogin
  ];
};
