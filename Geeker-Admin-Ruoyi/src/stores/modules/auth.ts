import { defineStore } from "pinia";
import { AuthState } from "@/stores/interface";
import { getFlatMenuList, getShowMenuList, getAllBreadcrumbList, filterDynamicRoutes } from "@/utils";
import { getRouters } from "@/api/ruoyimodule/login";
import { dynamicRoutes } from "@/routers/modules/staticRouter";

export const useAuthStore = defineStore({
  id: "geeker-auth",
  state: (): AuthState => ({
    // 按钮权限列表
    authButtonList: [],
    // 菜单权限列表
    authMenuList: [],
    // 当前页面的 router name，用来做按钮权限筛选
    routeName: ""
  }),
  getters: {
    // 按钮权限列表
    authButtonListGet: state => state.authButtonList,
    // 菜单权限列表 ==> 这里的菜单没有经过任何处理
    authMenuListGet: state => state.authMenuList,
    // 菜单权限列表 ==> 左侧菜单栏渲染，需要剔除 isHide == true
    showMenuListGet: state => getShowMenuList(state.authMenuList),
    // 菜单权限列表 ==> 扁平化之后的一维数组菜单，主要用来添加动态路由
    flatMenuListGet: state => getFlatMenuList(state.authMenuList),
    // 递归处理后的所有面包屑导航列表
    breadcrumbListGet: state => getAllBreadcrumbList(state.authMenuList)
  },
  actions: {
    // Get AuthMenuList
    async getAuthMenuList(premission: any) {
      const { data } = await getRouters();
      let menuListData: AuthState["authMenuList"] = filterRouter(data);
      const asyncRoute = filterDynamicRoutes(premission, dynamicRoutes);
      this.authMenuList = [
        {
          path: "/home/index",
          name: "home",
          component: "/home/index",
          meta: {
            icon: "HomeFilled",
            title: "首页",
            isLink: "",
            isHide: false,
            isFull: false,
            isAffix: true,
            isKeepAlive: true
          }
        } as any,
        ...asyncRoute,
        ...menuListData
      ];
    },
    setAuthButtonList(authButtonList) {
      this.authButtonList = authButtonList;
    },
    // Set RouteName
    async setRouteName(name: string) {
      this.routeName = name;
    }
  }
});

function filterRouter(data) {
  let list;
  list = filterAsyncRouter(data);
  list = filterRedirect(list);
  return list;
}

//eslint-disable-next-line @typescript-eslint/no-unused-vars
function filterAsyncRouter(asyncRouterMap, lasRouter: any = false, type = false) {
  return asyncRouterMap.map(route => {
    if (route.path === "/") {
      route = {
        ...route.children[0],
        path: "/" + route.children[0].path
      };
    }
    if (route.children && route.children.length) {
      route.children = filterChildren(route.children, route.path);
    }
    return route;
  });
}

// 过滤path
function filterChildren(childrenMap: AuthState["authMenuList"], parentRoutePath: string) {
  const routeList: AuthState["authMenuList"] = [];
  childrenMap.forEach(item => {
    item.component = "/" + item.component;
    item.path = parentRoutePath + "/" + item.path;
    if (item.children && item.children.length) {
      item.children = filterChildren(item.children, item.path);
    }
    routeList.push(item);
  });
  return routeList;
}

function filterRedirect(childrenMap: AuthState["authMenuList"]) {
  childrenMap.forEach((authMenuListItem: any) => {
    if (authMenuListItem.meta) {
      authMenuListItem.meta = {
        ...authMenuListItem.meta,
        isLink: authMenuListItem.meta.link,
        isKeepAlive: !authMenuListItem.meta.noCache,
        isAffix: authMenuListItem.meta.affix
      };
    }
    if (authMenuListItem.children && authMenuListItem.children.length) {
      authMenuListItem.redirect = authMenuListItem.children[0].path;
      filterRedirect(authMenuListItem.children);
    }
  });
  return childrenMap;
}
