import { RouteLocationItem } from "@/types/route";
import { defineStore } from "pinia";
import { RouteLocationNormalizedLoaded, RouteLocationNormalizedLoadedGeneric, RouteRecordName } from "vue-router";
type MatchPattern = string | RegExp;
const useTagsViewStore = defineStore("tags-view", {
  state: () => ({
    visitedViews: Array<RouteLocationItem>(),
    cachedViews: Array<MatchPattern>(),
    iframeViews: Array<RouteLocationNormalizedLoadedGeneric & { title: string }>(),
  }),
  actions: {
    addView(view: RouteLocationItem) {
      if (typeof view.meta?.group === 'function') view.meta.group = view.meta.group(view)
      if (typeof view.meta?.title === 'function') view.meta.title = view.meta.title(view)
      this.addVisitedView(view);
      this.addCachedView(view);
    },
    addIframeView(view: RouteLocationNormalizedLoadedGeneric) {
      if (this.iframeViews.some((v) => v.path === view.path)) return;
      this.iframeViews.push(
        Object.assign({}, view, {
          title: view.meta.title ? view.meta.title + '' : "no-name",
        })
      );
    },
    addVisitedView(view: RouteLocationItem) {
      if (this.visitedViews.some((v) => v.path === view.path)) return;
      const _view = view.meta?.group ? this.visitedViews.find((v) => v.meta?.group == view.meta?.group) : undefined
      if (_view) {
        Object.assign(_view, view);
      } else {
        this.visitedViews.push(
          Object.assign({}, view, {
            title: view.meta?.title || "no-name",
          })
        );
      }
    },
    addCachedView(view: RouteLocationItem) {
      if (this.cachedViews.includes(String(view.name))) return;
      if (!view.meta?.noCache) {
        this.cachedViews.push(String(view.name));
      }
    },
    delView(view: RouteLocationItem) {
      return new Promise<{
        visitedViews: RouteLocationItem[],
        cachedViews: MatchPattern[],
        lastPath: string | undefined
      }>((resolve) => {
        this.delVisitedView(view);
        this.delCachedView(view);
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews],
          lastPath: this.visitedViews[this.visitedViews.length - 1]?.path,
        });
      });
    },
    delVisitedView(view: RouteLocationNormalizedLoadedGeneric) {
      return new Promise((resolve) => {
        for (const [i, v] of this.visitedViews.entries()) {
          if (v.path === view.path) {
            this.visitedViews.splice(i, 1);
            break;
          }
        }
        this.iframeViews = this.iframeViews.filter(
          (item) => item.path !== view.path
        );
        resolve([...this.visitedViews]);
      });
    },
    delIframeView(view: RouteLocationNormalizedLoaded) {
      return new Promise((resolve) => {
        this.iframeViews = this.iframeViews.filter(
          (item) => item.path !== view.path
        );
        resolve([...this.iframeViews]);
      });
    },
    delCachedView(view: RouteLocationItem) {
      return new Promise((resolve) => {
        const index = this.cachedViews.indexOf(String(view.name));
        index > -1 && this.cachedViews.splice(index, 1);
        resolve([...this.cachedViews]);
      });
    },
    delOthersViews(view: RouteLocationItem) {
      return new Promise((resolve) => {
        this.delOthersVisitedViews(view);
        this.delOthersCachedViews(view);
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews],
        });
      });
    },
    delOthersVisitedViews(view: RouteLocationItem) {
      return new Promise((resolve) => {
        this.visitedViews = this.visitedViews.filter((v) => {
          return v.meta?.affix || v.path === view.path;
        });
        this.iframeViews = this.iframeViews.filter(
          (item) => item.path === view.path
        );
        resolve([...this.visitedViews]);
      });
    },
    delOthersCachedViews(view: RouteLocationItem) {
      return new Promise((resolve) => {
        const index = this.cachedViews.indexOf(String(view.name));
        if (index > -1) {
          this.cachedViews = this.cachedViews.slice(index, index + 1);
        } else {
          this.cachedViews = [];
        }
        resolve([...this.cachedViews]);
      });
    },
    delAllViews() {
      return new Promise<{
        visitedViews: RouteLocationItem[],
        cachedViews: MatchPattern[]
      }>((resolve) => {
        this.delAllVisitedViews();
        this.delAllCachedViews();
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews],
        });
      });
    },
    delAllVisitedViews() {
      return new Promise((resolve) => {
        const affixTags = this.visitedViews.filter((tag) => tag.meta?.affix);
        this.visitedViews = affixTags;
        this.iframeViews = [];
        resolve([...this.visitedViews]);
      });
    },
    delAllCachedViews() {
      return new Promise((resolve) => {
        this.cachedViews = [];
        resolve([...this.cachedViews]);
      });
    },
    updateVisitedView(view: RouteLocationNormalizedLoaded) {
      for (let v of this.visitedViews) {
        if (v.path === view.path) {
          v = Object.assign(v, view);
          break;
        }
      }
    },
    delRightTags(view: RouteLocationNormalizedLoaded) {
      return new Promise<RouteLocationItem[]>((resolve) => {
        const index = this.visitedViews.findIndex((v) => v.path === view.path);
        if (index === -1) {
          return;
        }
        this.visitedViews = this.visitedViews.filter((item, idx) => {
          if (idx <= index || (item.meta && item.meta.affix)) {
            return true;
          }
          const i = this.cachedViews.indexOf(String(item.name));
          if (i > -1) {
            this.cachedViews.splice(i, 1);
          }
          if (item.meta?.link) {
            const fi = this.iframeViews.findIndex((v) => v.path === item.path);
            this.iframeViews.splice(fi, 1);
          }
          return false;
        });
        resolve([...this.visitedViews]);
      });
    },
    delLeftTags(view: RouteLocationNormalizedLoaded) {
      return new Promise<RouteLocationItem[]>((resolve) => {
        const index = this.visitedViews.findIndex((v) => v.path === view.path);
        if (index === -1) {
          return;
        }
        this.visitedViews = this.visitedViews.filter((item, idx) => {
          if (idx >= index || (item.meta && item.meta.affix)) {
            return true;
          }
          const i = this.cachedViews.indexOf(String(item.name));
          if (i > -1) {
            this.cachedViews.splice(i, 1);
          }
          if (item.meta?.link) {
            const fi = this.iframeViews.findIndex((v) => v.path === item.path);
            this.iframeViews.splice(fi, 1);
          }
          return false;
        });
        resolve([...this.visitedViews]);
      });
    },
  },
});

export default useTagsViewStore;
