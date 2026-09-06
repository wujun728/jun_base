import type {RouteRecordNormalized} from 'vue-router';

const modules = import.meta.glob('./modules/*.ts', {eager: true});

const externalModules = import.meta.glob('./externalModules/*.ts', {
    eager: true,
});
const dynamicModules = import.meta.glob('./dynamicModules/*.ts', {
    eager: true,
});
const dashboardModules = import.meta.glob('./modules/dashboard.ts', {
    eager: true,
});

function formatModules(_modules: any, result: RouteRecordNormalized[]) {
  Object.keys(_modules).forEach((key) => {
    const defaultModule = _modules[key].default;
    if (!defaultModule) return;
      const moduleList = Array.isArray(defaultModule)
          ? [...defaultModule]
          : [defaultModule];
      result.push(...moduleList);
  });
    return result;
}

export const appRoutes: RouteRecordNormalized[] = formatModules(modules, []);

export const appExternalRoutes: RouteRecordNormalized[] = formatModules(externalModules, []);

// 数据来自有服务器时存在固定的路由，在这里单独定义
// 动态路由用于tab页面跳转
export const appDynamicRoutes: RouteRecordNormalized[] = formatModules(dynamicModules, []);
// 工作台路由
export const dashboardRoutes: RouteRecordNormalized[] = formatModules(dashboardModules, []);
