import useTagsViewStore from '@/store/modules/tagsView'
import { router } from '@/router'
import { RouteLocationNormalizedLoaded, RouteLocationRaw } from 'vue-router';
import { RoutesAlias } from '@/router/routesAlias';

export default {
  /**
   * 刷新当前tab页签
   * @param {Object | undefined} obj - 页签对象，包含name、path和query等信息。如果未传入，则自动获取当前页签信息。
   */
  async refreshPage(obj: any | undefined) {
    const { path, query, matched } = router.currentRoute.value;
    if (obj === undefined) {
      matched.forEach((m) => {
        if (m.components && m.components.default && m.components.default.name) {
          if (!['Layout', 'ParentView'].includes(m.components.default.name)) {
            obj = { name: m.components.default.name, path: path, query: query };
          }
        }
      });
    }
    await useTagsViewStore().delCachedView(obj);
    const { path: path_1, query: query_1 } = obj;
    router.replace({
      path: '/redirect' + path_1,
      query: query_1
    });
  },

  /**
   * 关闭当前tab页签，并打开新页签
   * @param {RouteLocationRaw} obj - 新页签的路由信息。
   * @returns {Promise} 路由跳转的Promise对象。
   */
  closeOpenPage(obj: RouteLocationRaw) {
    useTagsViewStore().delView(router.currentRoute.value);
    if (obj !== undefined) {
      return router.push(obj);
    }
  },

  /**
   * 关闭指定的tab页签
   * @param {RouteLocationNormalizedLoaded | undefined} obj - 要关闭的页签对象。如果未传入，则关闭当前页签。
   * @returns {Promise} 包含删除结果的Promise对象。
   */
  async closePage(obj: RouteLocationNormalizedLoaded | undefined) {
    if (obj === undefined) {
      const res = await useTagsViewStore().delView(router.currentRoute.value);
      await router.push(res.lastPath || RoutesAlias.Home);
      return res;
    }
    return useTagsViewStore().delView(obj);
  },

  /**
   * 关闭所有tab页签
   * @returns {Promise} 包含删除结果的Promise对象。
   */
  closeAllPage() {
    return useTagsViewStore().delAllViews();
  },

  /**
   * 关闭左侧的tab页签
   * @param {RouteLocationNormalizedLoaded | undefined | null} obj - 基准页签对象。如果未传入，则以当前页签为基准。
   * @returns {Promise} 包含删除结果的Promise对象。
   */
  closeLeftPage(obj: RouteLocationNormalizedLoaded | undefined | null) {
    return useTagsViewStore().delLeftTags(obj || router.currentRoute.value);
  },

  /**
   * 关闭右侧的tab页签
   * @param {RouteLocationNormalizedLoaded | undefined | null} obj - 基准页签对象。如果未传入，则以当前页签为基准。
   * @returns {Promise} 包含删除结果的Promise对象。
   */
  closeRightPage(obj: RouteLocationNormalizedLoaded | undefined | null) {
    return useTagsViewStore().delRightTags(obj || router.currentRoute.value);
  },

  /**
   * 关闭其他tab页签
   * @param {RouteLocationNormalizedLoaded | undefined | null} obj - 保留的页签对象。如果未传入，则保留当前页签。
   * @returns {Promise} 包含删除结果的Promise对象。
   */
  closeOtherPage(obj: RouteLocationNormalizedLoaded | undefined | null) {
    return useTagsViewStore().delOthersViews(obj || router.currentRoute.value);
  },

  /**
   * 打开新的tab页签
   * @param {RouteLocationRaw} url - 新页签的路由信息。
   * @returns {Promise} 路由跳转的Promise对象。
   */
  openPage(url: RouteLocationRaw) {
    return router.push(url);
  },

  /**
   * 修改tab页签信息
   * @param {RouteLocationNormalizedLoaded} obj - 要修改的页签对象。
   * @returns {Promise} 包含更新结果的Promise对象。
   */
  updatePage(obj: RouteLocationNormalizedLoaded) {
    return useTagsViewStore().updateVisitedView(obj);
  }
}
