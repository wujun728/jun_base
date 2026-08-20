<template>
  <div v-if="!item.meta || !item.meta.hidden">
    <!--【叶子节点】显示叶子节点或唯一子节点且父节点未配置始终显示 -->
    <template
      v-if="
        // 未配置始终显示，使用唯一子节点替换父节点显示为叶子节点
        (hasOneShowingChild(item.children, item) &&
          !item.meta?.alwaysShow &&
          (!onlyOneChild.children || onlyOneChild.noShowingChildren)) ||
        // 即使配置了始终显示，但无子节点，也显示为叶子节点
        (item.meta?.alwaysShow && !item.children)
      "
    >
      <AppLink
        v-if="onlyOneChild.meta"
        :to="{
          path: resolvePath(onlyOneChild.path),
          query: onlyOneChild.meta.params,
        }"
      >
        <el-menu-item
          :index="resolvePath(onlyOneChild.path)"
          :class="{ 'submenu-title-noDropdown': !isNest }"
        >
          <template v-if="onlyOneChild.meta">
            <LayoutMenuIcon :icon="onlyOneChild.meta.icon || item.meta?.icon" />
            <span
              v-if="onlyOneChild.meta.title"
              class="ml-1"
              :title="translateRouteTitle(onlyOneChild.meta.title)"
            >
              {{ translateRouteTitle(onlyOneChild.meta.title) }}
            </span>
          </template>
        </el-menu-item>
      </AppLink>
    </template>

    <!--【非叶子节点】显示含多个子节点的父菜单，或始终显示的单子节点 -->
    <el-sub-menu v-else :index="resolvePath(item.path)" :data-path="item.path" teleported>
      <template #title>
        <template v-if="item.meta">
          <LayoutMenuIcon :icon="item.meta.icon" />
          <span v-if="item.meta.title" class="ml-1" :title="translateRouteTitle(item.meta.title)">
            {{ translateRouteTitle(item.meta.title) }}
          </span>
        </template>
      </template>

      <LayoutSidebarItem
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
      />
    </el-sub-menu>
  </div>
</template>

<script setup>
import path from "path-browserify";
import { isExternal } from "@/utils";
import { translateRouteTitle } from "@/lang/utils";
import LayoutMenuIcon from "./LayoutMenuIcon.vue";

defineOptions({
  name: "LayoutSidebarItem",
  inheritAttrs: false,
});

const props = defineProps({
  /**
   * 当前路由对象
   */
  item: {
    type: Object,
    required: true,
  },

  /**
   * 父级完整路径
   */
  basePath: {
    type: String,
    required: true,
  },

  /**
   * 是否为嵌套路由
   */
  isNest: {
    type: Boolean,
    default: false,
  },
});

// 可见的唯一子节点
const onlyOneChild = ref();

/**
 * 检查是否仅有一个可见子节点
 *
 * @param children 子路由数组
 * @param parent 父级路由
 * @returns 是否仅有一个可见子节点
 */
function hasOneShowingChild(children = [], parent) {
  // 过滤出可见子节点
  const showingChildren = children.filter((route) => {
    if (!route.meta?.hidden) {
      onlyOneChild.value = route;
      return true;
    }
    return false;
  });

  // 仅有一个节点
  if (showingChildren.length === 1) {
    return true;
  }

  // 无子节点
  if (showingChildren.length === 0) {
    // 父节点设置为唯一显示节点，并标记为无子节点
    onlyOneChild.value = { ...parent, path: "", noShowingChildren: true };
    return true;
  }
  return false;
}

/**
 * 获取完整路径，适配外部链接
 *
 * @param routePath 路由路径
 * @returns 绝对路径
 */
function resolvePath(routePath) {
  if (isExternal(routePath)) return routePath;
  if (isExternal(props.basePath)) return props.basePath;

  // 拼接父路径和当前路径
  return path.resolve(props.basePath, routePath);
}
</script>
