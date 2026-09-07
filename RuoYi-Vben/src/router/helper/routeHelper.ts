import type { AppRouteModule, AppRouteRecordRaw } from '@/router/types';
import type { Router, RouteRecordNormalized } from 'vue-router';

import { getParentLayout, LAYOUT, EXCEPTION_COMPONENT } from '@/router/constant';
import { cloneDeep, omit } from 'lodash-es';
import { warn } from '@/utils/log';
import { createRouter, createWebHashHistory } from 'vue-router';
import type { Menu } from '@/api/auth/model';

export type LayoutMapKey = 'LAYOUT';
const IFRAME = () => import('@/layouts/components/iframe/FrameBlank.vue');

const LayoutMap = new Map<string, () => Promise<typeof import('*.vue')>>();

LayoutMap.set('LAYOUT', LAYOUT);
/** 内嵌iframe */
LayoutMap.set('INNERLINK', IFRAME);
/**
 * 适配Log里的
 * log.ts:4 [RuoYi Plus Vben warn]:
 * 在src/views/下找不到`ParentView.vue` 或 `ParentView.tsx`, 请自行创建!
 *
 * 官方的element需要用这个组件来处理二级菜单 vben不需要 只需要加上标识
 */
LayoutMap.set('PARENTVIEW', 'ParentView' as any);

let dynamicViewsModules: Record<string, () => Promise<Recordable>>;

/**
 * 转换器  转换后端返回的路由为vben能用的路由
 * @param menuList 后端返回的menuList
 * @returns
 */
export function transformToAppRoutes(menuList: Menu[]) {
  if (!menuList) return [];
  const appRoutesList: AppRouteRecordRaw[] = [];
  menuList.forEach((item) => {
    // 根目录为菜单形式
    // 固定有一个children  children为当前菜单
    if (item.path === '/' && item.children && item.children.length === 1) {
      item = item.children[0];
    }
    const appRoute: AppRouteRecordRaw = {
      path: item.path,
      name: item.name,
      component: item.component,
      meta: {
        title: item.meta.title,
        icon: item.meta.icon,
        ignoreKeepAlive: item.meta.noCache,
        // 当前路由不在菜单显示 但是可以通过链接访问
        // 不可访问的路由由后端控制隐藏(不返回对应路由)
        hideMenu: item.hidden,
      },
    };
    /**
     * 判断是否为内嵌iframe
     * 内嵌条件link不为空 http开头  使用根组件为InnerLink
     */
    const { link } = item.meta;

    if (link && link.startsWith('http') && item.component === 'InnerLink') {
      /**
       * 需要判断特殊情况  比如vue的hash是带#的
       * 比如链接 aaa.com/#/bbb  path会转换为 aaa/com/#/bbb
       * 比如链接 aaa.com/?bbb=xxx
       * 需要去除#  否则无法被添加到路由
       */
      if (item.path.includes('#')) {
        item.path = item.path.replace('#', '');
      }
      if (item.path.includes('?') || item.path.includes('&')) {
        item.path = item.path.replace('?', '');
        item.path = item.path.replace('&', '');
      }
      // 设置到frameSrc  后续由vben处理
      appRoute.meta.frameSrc = link;
      // path也要设置为转换后的
      appRoute.path = item.path;
    }
    const { children } = item;
    if (children) {
      appRoute.children = transformToAppRoutes(children);
    }
    appRoutesList.push(appRoute);
  });
  return appRoutesList;
}

// Dynamic introduction
function asyncImportRoute(routes: AppRouteRecordRaw[] | undefined) {
  dynamicViewsModules = dynamicViewsModules || import.meta.glob('../../views/**/*.{vue,tsx}');
  if (!routes) return;
  routes.forEach((item) => {
    const { component, name } = item;
    const { children } = item;
    if (component) {
      const layoutFound = LayoutMap.get(component.toUpperCase());
      if (layoutFound) {
        /** 如果是内嵌iframe */
        if (item.meta?.frameSrc) {
          item.component = IFRAME;
        } else {
          item.component = layoutFound;
        }
      } else {
        item.component = dynamicImport(dynamicViewsModules, component as string);
      }
    } else if (name) {
      item.component = getParentLayout();
    }
    children && asyncImportRoute(children);
  });
}

function dynamicImport(
  dynamicViewsModules: Record<string, () => Promise<Recordable>>,
  component: string,
) {
  const keys = Object.keys(dynamicViewsModules);
  const matchKeys = keys.filter((key) => {
    const k = key.replace('../../views', '');
    const startFlag = component.startsWith('/');
    const endFlag = component.endsWith('.vue') || component.endsWith('.tsx');
    const startIndex = startFlag ? 0 : 1;
    const lastIndex = endFlag ? k.length : k.lastIndexOf('.');
    return k.substring(startIndex, lastIndex) === component;
  });
  if (matchKeys?.length === 1) {
    const matchKey = matchKeys[0];
    return dynamicViewsModules[matchKey];
  } else if (matchKeys?.length > 1) {
    warn(
      'Please do not create `.vue` and `.TSX` files with the same file name in the same hierarchical directory under the views folder. This will cause dynamic introduction failure',
    );
    return;
  } else {
    warn('在src/views/下找不到`' + component + '.vue` 或 `' + component + '.tsx`, 请自行创建!');
    return EXCEPTION_COMPONENT;
  }
}

// Turn background objects into routing objects
// 将背景对象变成路由对象
export function transformObjToRoute<T = AppRouteModule>(routeList: AppRouteModule[]): T[] {
  routeList.forEach((route) => {
    const component = route.component as string;
    if (component) {
      if (component.toUpperCase() === 'LAYOUT') {
        route.component = LayoutMap.get(component.toUpperCase());
      } else {
        route.children = [cloneDeep(route)];
        route.component = LAYOUT;

        //某些情况下如果name如果没有值， 多个一级路由菜单会导致页面404
        if (!route.name) {
          warn('找不到菜单对应的name, 请检查数据!' + JSON.stringify(route));
        }
        route.name = `${route.name}Parent`;
        route.path = '';
        const meta = route.meta || {};
        meta.single = true;
        meta.affix = false;
        route.meta = meta;
      }
    } else {
      warn('请正确配置路由：' + route?.name + '的component属性');
    }
    route.children && asyncImportRoute(route.children);
  });
  return routeList as unknown as T[];
}

/**
 * Convert multi-level routing to level 2 routing
 * 将多级路由转换为 2 级路由
 */
export function flatMultiLevelRoutes(routeModules: AppRouteModule[]) {
  const modules: AppRouteModule[] = cloneDeep(routeModules);

  for (let index = 0; index < modules.length; index++) {
    const routeModule = modules[index];
    // 判断级别是否 多级 路由
    if (!isMultipleRoute(routeModule)) {
      // 声明终止当前循环， 即跳过此次循环，进行下一轮
      continue;
    }
    // 路由等级提升
    promoteRouteLevel(routeModule);
  }
  return modules;
}

// Routing level upgrade
// 路由等级提升
function promoteRouteLevel(routeModule: AppRouteModule) {
  // Use vue-router to splice menus
  // 使用vue-router拼接菜单
  // createRouter 创建一个可以被 Vue 应用程序使用的路由实例
  let router: Router | null = createRouter({
    routes: [routeModule as unknown as RouteRecordNormalized],
    history: createWebHashHistory(),
  });
  // getRoutes： 获取所有 路由记录的完整列表。
  const routes = router.getRoutes();
  // 将所有子路由添加到二级路由
  addToChildren(routes, routeModule.children || [], routeModule);
  router = null;

  // omit lodash的函数 对传入的item对象的children进行删除
  routeModule.children = routeModule.children?.map((item) => omit(item, 'children'));
}

// Add all sub-routes to the secondary route
// 将所有子路由添加到二级路由
function addToChildren(
  routes: RouteRecordNormalized[],
  children: AppRouteRecordRaw[],
  routeModule: AppRouteModule,
) {
  for (let index = 0; index < children.length; index++) {
    const child = children[index];
    const route = routes.find((item) => item.name === child.name);
    if (!route) {
      continue;
    }
    routeModule.children = routeModule.children || [];
    if (!routeModule.children.find((item) => item.name === route.name)) {
      routeModule.children?.push(route as unknown as AppRouteModule);
    }
    if (child.children?.length) {
      addToChildren(routes, child.children, routeModule);
    }
  }
}

// Determine whether the level exceeds 2 levels
// 判断级别是否超过2级
function isMultipleRoute(routeModule: AppRouteModule) {
  // Reflect.has 与 in 操作符 相同, 用于检查一个对象(包括它原型链上)是否拥有某个属性
  if (!routeModule || !Reflect.has(routeModule, 'children') || !routeModule.children?.length) {
    return false;
  }

  const children = routeModule.children;

  let flag = false;
  for (let index = 0; index < children.length; index++) {
    const child = children[index];
    if (child.children?.length) {
      flag = true;
      break;
    }
  }
  return flag;
}
