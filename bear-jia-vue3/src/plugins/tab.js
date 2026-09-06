import router from '@/router';
import { useTagsViewStore } from '@/stores/tagsView';

export default {
  // 刷新当前tab页签
  refreshPage(obj) {
    const tagsViewStore = useTagsViewStore();
    const currentRoute = router.currentRoute.value;

    const view = obj || {
      name: currentRoute.name,
      path: currentRoute.path,
      query: currentRoute.query,
      meta: currentRoute.meta,
    };

    // 当前项目没有 /redirect 路由，这里用 query 的“时间戳”触发一次重新渲染。
    tagsViewStore.delCachedView(view);
    return router.replace({
      path: view.path,
      query: { ...(view.query || {}), _refresh: Date.now() },
    });
  },
  // 关闭当前tab页签，打开新页签
  closeOpenPage(obj) {
    const tagsViewStore = useTagsViewStore();
    tagsViewStore.delView(router.currentRoute.value);
    if (obj !== undefined) return router.push(obj);
    return Promise.resolve();
  },
  // 关闭指定tab页签
  closePage(obj) {
    const tagsViewStore = useTagsViewStore();
    const view = obj || router.currentRoute.value;
    const { visitedViews } = tagsViewStore.delView(view);
    const lastView = visitedViews[visitedViews.length - 1];
    if (!obj) return router.push(lastView?.path || '/');
    return Promise.resolve();
  },
  // 关闭所有tab页签
  closeAllPage() {
    const tagsViewStore = useTagsViewStore();
    tagsViewStore.delAllViews();
    return router.push('/home/workbench');
  },
  // 关闭左侧tab页签
  closeLeftPage(obj) {
    const tagsViewStore = useTagsViewStore();
    tagsViewStore.delLeftTags(obj || router.currentRoute.value);
    return Promise.resolve();
  },
  // 关闭右侧tab页签
  closeRightPage(obj) {
    const tagsViewStore = useTagsViewStore();
    tagsViewStore.delRightTags(obj || router.currentRoute.value);
    return Promise.resolve();
  },
  // 关闭其他tab页签
  closeOtherPage(obj) {
    const tagsViewStore = useTagsViewStore();
    tagsViewStore.delOthersViews(obj || router.currentRoute.value);
    return Promise.resolve();
  },
  // 添加tab页签
  openPage(title, url) {
    const tagsViewStore = useTagsViewStore();
    var obj = { path: url, meta: { title: title } };
    tagsViewStore.addView(obj);
    return router.push(url);
  },
  // 修改tab页签
  updatePage(obj) {
    const tagsViewStore = useTagsViewStore();
    tagsViewStore.updateVisitedView(obj);
    return Promise.resolve();
  },
};
