import router from "@/routers/index";
import { LOGIN_URL } from "@/config";
import { RouteRecordRaw } from "vue-router";
import { ElNotification } from "element-plus";
import { useUserStore } from "@/stores/modules/user";
import { useAuthStore } from "@/stores/modules/auth";
import { getInfo } from "@/api/ruoyimodule/login";

// 引入 views 文件夹下所有 vue 文件
const modules = import.meta.glob("@/views/**/*.vue");
/**
 * @description 初始化动态路由
 */
export const initDynamicRouter = async () => {
  const userStore = useUserStore();
  const authStore = useAuthStore();

  try {
    let res = await getInfo();
    await userStore.setUserInfo((res as any).user);
    authStore.setAuthButtonList((res as any).permissions);
    // 1.获取菜单列表 && 按钮权限列表
    await authStore.getAuthMenuList((res as any).permissions);

    // 2.判断当前用户有没有菜单权限
    if (!authStore.authMenuListGet.length) {
      ElNotification({
        title: "无权限访问",
        message: "当前账号无任何菜单权限，请联系系统管理员！",
        type: "warning",
        duration: 3000
      });
      userStore.setToken("");
      userStore.setUserInfo({});
      authStore.setAuthButtonList([]);
      router.replace(LOGIN_URL);
      return Promise.reject("No permission");
    }

    // 3.添加动态路由
    authStore.flatMenuListGet.forEach(item => {
      item.children && delete item.children;
      if (item.component && typeof item.component == "string") {
        console.log(item.component);
        item.component = modules["/src/views" + item.component + ".vue"];
      }
      if (item.meta && item.meta.isFull) {
        router.addRoute(item as unknown as RouteRecordRaw);
      } else {
        router.addRoute("layout", item as unknown as RouteRecordRaw);
      }
    });
  } catch (error) {
    // 当按钮 || 菜单请求出错时，重定向到登陆页
    userStore.setToken("");
    userStore.setUserInfo({});
    authStore.setAuthButtonList([]);
    router.replace(LOGIN_URL);
    return Promise.reject(error);
  }
};
