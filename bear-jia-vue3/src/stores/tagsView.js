import { defineStore } from 'pinia';

export const useTagsViewStore = defineStore('tagsView', {
  state: () => ({
    visitedViews: [],
    cachedViews: [],
  }),

  actions: {
    addView(view) {
      this.addVisitedView(view);
      this.addCachedView(view);
    },

    addVisitedView(view) {
      if (this.visitedViews.some((v) => v.path === view.path)) return;
      this.visitedViews.push(
        Object.assign({}, view, {
          title: view.meta.title || 'no-name',
        })
      );
    },

    addCachedView(view) {
      if (typeof view?.name !== 'string') return;
      if (this.cachedViews.includes(view.name)) return;
      if (!view?.meta?.noCache) this.cachedViews.push(view.name);
    },

    delView(view) {
      this.delVisitedView(view);
      this.delCachedView(view);
      return {
        visitedViews: [...this.visitedViews],
        cachedViews: [...this.cachedViews],
      };
    },

    delVisitedView(view) {
      for (const [i, v] of this.visitedViews.entries()) {
        if (v.path === view.path) {
          this.visitedViews.splice(i, 1);
          break;
        }
      }
    },

    delCachedView(view) {
      if (typeof view?.name !== 'string') return;
      const index = this.cachedViews.indexOf(view.name);
      index > -1 && this.cachedViews.splice(index, 1);
    },

    delOthersViews(view) {
      this.delOthersVisitedViews(view);
      this.delOthersCachedViews(view);
      return {
        visitedViews: [...this.visitedViews],
        cachedViews: [...this.cachedViews],
      };
    },

    delOthersVisitedViews(view) {
      this.visitedViews = this.visitedViews.filter((v) => {
        return v.meta.affix || v.path === view.path;
      });
    },

    delOthersCachedViews(view) {
      if (typeof view?.name !== 'string') {
        this.cachedViews = [];
        return;
      }
      const index = this.cachedViews.indexOf(view.name);
      if (index > -1) {
        this.cachedViews = this.cachedViews.slice(index, index + 1);
      } else {
        this.cachedViews = [];
      }
    },

    delLeftTags(view) {
      const index = this.visitedViews.findIndex((v) => v.path === view.path);
      if (index === -1) {
        return {
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews],
        };
      }

      // 保留 affix 标签 + 当前标签及其右侧标签
      const affixTags = this.visitedViews.filter((tag) => tag.meta?.affix);
      const rightTags = this.visitedViews.slice(index);

      const merged = [];
      for (const tag of [...affixTags, ...rightTags]) {
        if (!merged.some((v) => v.path === tag.path)) merged.push(tag);
      }
      this.visitedViews = merged;

      const keepNames = new Set(this.visitedViews.map((v) => v.name).filter(Boolean));
      this.cachedViews = this.cachedViews.filter((name) => keepNames.has(name));

      return {
        visitedViews: [...this.visitedViews],
        cachedViews: [...this.cachedViews],
      };
    },

    delRightTags(view) {
      const index = this.visitedViews.findIndex((v) => v.path === view.path);
      if (index === -1) {
        return {
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews],
        };
      }

      // 保留 affix 标签 + 当前标签及其左侧标签
      const affixTags = this.visitedViews.filter((tag) => tag.meta?.affix);
      const leftTags = this.visitedViews.slice(0, index + 1);

      const merged = [];
      for (const tag of [...affixTags, ...leftTags]) {
        if (!merged.some((v) => v.path === tag.path)) merged.push(tag);
      }
      this.visitedViews = merged;

      const keepNames = new Set(this.visitedViews.map((v) => v.name).filter(Boolean));
      this.cachedViews = this.cachedViews.filter((name) => keepNames.has(name));

      return {
        visitedViews: [...this.visitedViews],
        cachedViews: [...this.cachedViews],
      };
    },

    delAllViews() {
      this.delAllVisitedViews();
      this.delAllCachedViews();
      return {
        visitedViews: [...this.visitedViews],
        cachedViews: [...this.cachedViews],
      };
    },

    delAllVisitedViews() {
      // keep affix tags
      const affixTags = this.visitedViews.filter((tag) => tag.meta.affix);
      this.visitedViews = affixTags;
    },

    delAllCachedViews() {
      this.cachedViews = [];
    },

    updateVisitedView(view) {
      for (let v of this.visitedViews) {
        if (v.path === view.path) {
          v = Object.assign(v, view);
          break;
        }
      }
    },
  },
});
