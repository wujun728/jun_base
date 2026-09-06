import { RouteRecordRaw } from "vue-router";
import { HOME_URL, LOGIN_URL } from "@/config";

/**
 * staticRouter (静态路由)
 */
export const staticRouter: RouteRecordRaw[] = [
  {
    path: "/",
    redirect: HOME_URL
  },
  {
    path: LOGIN_URL,
    name: "login",
    component: () => import("@/views/login/index.vue"),
    meta: {
      title: "登录"
    }
  },
  {
    path: "/layout",
    name: "layout",
    component: () => import("@/layouts/index.vue"),
    // component: () => import("@/layouts/indexAsync.vue"),
    redirect: HOME_URL,
    children: []
  }
];

/**
 * errorRouter (错误页面路由)
 */
export const errorRouter = [
  {
    path: "/403",
    name: "403",
    component: () => import("@/components/ErrorMessage/403.vue"),
    meta: {
      title: "403页面"
    }
  },
  {
    path: "/404",
    name: "404",
    component: () => import("@/components/ErrorMessage/404.vue"),
    meta: {
      title: "404页面"
    }
  },
  {
    path: "/500",
    name: "500",
    component: () => import("@/components/ErrorMessage/500.vue"),
    meta: {
      title: "500页面"
    }
  },
  // Resolve refresh page, route warnings
  {
    path: "/:pathMatch(.*)*",
    component: () => import("@/components/ErrorMessage/404.vue")
  }
];

export const dynamicRoutes = [
  {
    path: "/system/dict-data/index/:dictId",
    permissions: ["system:dict:list"],
    component: "/system/dict/data",
    name: "Data",
    meta: { title: "字典数据", activeMenu: "/system/dict", isHide: true }
  },
  {
    path: "/monitor/job-log/index/:jobId",
    name: "JobLog",
    component: "/monitor/job/log",
    permissions: ["monitor:job:list"],
    meta: { title: "调度日志", activeMenu: "/monitor/job", isHide: true }
  },
  {
    path: "/tool/gen-edit/index/:tableId",
    component: "/tool/gen/editTable",
    permissions: ["tool:gen:edit"],
    meta: { title: "修改生成配置", activeMenu: "/tool/gen", isHide: true }
  }
];
