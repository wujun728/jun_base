<script setup lang="ts">
import AppLink from './Link.vue'
import { computed, ref } from 'vue';
import { RouteItem } from '@/types/route';
import { isExternal } from '@/utils/validate'
import { getNormalPath } from '@/utils/ruoyi'

const props = defineProps<{
  item: RouteItem;
  isNest?: boolean;
  basePath?: string;
}>()

const onlyOneChild = ref<RouteItem & { noShowingChildren: boolean } | null>(null);

function hasOneShowingChild(children: RouteItem[] = [], parent: RouteItem) {
  if (!children) children = [];
  const showingChildren = children.filter(item => {
    if (item.hidden) return false

    // Temp set(will be used if only has one showing child)
    onlyOneChild.value = { ...item, noShowingChildren: false };
    return true;
  })

  // When there is only one child router, the child router is displayed by default
  if (showingChildren.length === 1) return true

  // Show parent if there are no child router to display
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
    return true
  }

  return false;
};

function resolvePath(routePath: string, routeQuery?: any) {
  if (isExternal(routePath)) return routePath
  if (isExternal(props.basePath!)) return props.basePath

  if (routeQuery) {
    return {
      path: getNormalPath(props.basePath + '/' + routePath),
      query: JSON.parse(routeQuery)
    };
  }
  return getNormalPath(props.basePath + '/' + routePath)
}

function hasTitle(title?: string) {
  if (!!title && title.length > 5) {
    return title;
  } else {
    return "";
  }
}

function getTitle(route: RouteItem) {
  if (!route.meta?.title) return "";
  if (typeof route.meta.title === "function") return route.meta.title(route);
  return route.meta.title;
}

const show = computed(() => {
  return hasOneShowingChild(props.item.children, props.item)
    && (!onlyOneChild.value?.children || onlyOneChild.value?.noShowingChildren)
    && !props.item.alwaysShow
})
</script>
<template>
  <div v-if="!item.hidden">
    <template v-if="show">
      <app-link v-if="onlyOneChild?.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{ 'submenu-title-noDropdown': !isNest }">
          <svg-icon :icon-class="onlyOneChild.meta.icon || (item.meta && item.meta.icon)" />
          <template #title>
            <span class="menu-title" :title="hasTitle(getTitle(onlyOneChild))">
              {{ getTitle(onlyOneChild) }}
            </span>
          </template>
        </el-menu-item>
      </app-link>
    </template>

    <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)" teleported>
      <template v-if="item.meta" #title>
        <svg-icon :icon-class="item.meta?.icon" />
        <span class="menu-title" :title="hasTitle(getTitle(item))">{{ getTitle(item) }}</span>
      </template>

      <sidebar-item v-for="child in item.children" :key="child.path" :is-nest="true" :item="child"
        :base-path="resolvePath(child.path)" class="nest-menu" />
    </el-sub-menu>
  </div>
</template>
