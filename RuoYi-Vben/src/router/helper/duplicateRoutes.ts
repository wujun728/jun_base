import { AppRouteRecordRaw } from '@/router/types';
import { basicRoutes } from '@/router/routes';
import { eachTree } from 'xe-utils';

/**
 * 检测是否存在重复的路由名称
 * @param dynamicRoutes 动态路由
 */
export function duplicateRoutesChecker(dynamicRoutes: AppRouteRecordRaw[]) {
  /**
   * 获取所有路由 包括本地+动态路由
   * 过滤掉 PageNotFound
   */
  const allRoutes = [...basicRoutes, ...dynamicRoutes].filter(
    (route) => route.name !== 'PageNotFound',
  );

  const routeNameList: string[] = [];
  /** 遍历 判断 */
  eachTree(allRoutes, (route) => {
    const componentName = route.component?.name;
    const path = route.path ?? '';
    const name = route.name ?? '';

    /**
     * 不检测外链 外链不会被添加到路由
     * 内嵌iframe path是去除http的 会被检测
     */
    if (path.startsWith('http')) {
      return;
    }
    /**
     * 根组件 不负责显示
     */
    if (componentName === 'LAYOUT') {
      return;
    }
    if (routeNameList.includes(name)) {
      console.error(`路由名称重复: ${name} 会造成 404 组件路径: ${componentName}`);
      return;
    }
    routeNameList.push(name);
  });
}
