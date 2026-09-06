<template>
  <template v-if="!item.hidden">
    <template
      v-if="
        hasOneShowingChild(item, item.children) &&
        (!onlyOneChild?.children || onlyOneChild?.noShowingChildren) &&
        !item.alwaysShow
      "
    >
      <app-link
        v-if="onlyOneChild?.meta"
        :to="resolveQuery(onlyOneChild.path, onlyOneChild.query)"
      >
        <el-menu-item
          :index="resolvePath(onlyOneChild.path)"
          :class="{ 'submenu-title-noDropdown': !isNest }"
        >
          <SvgIcon
            :iconClass="
              onlyOneChild.meta.icon || (item.meta && item.meta.icon)!
            "
            :size="16"
          />
          <template #title>
            <span
              class="menu-title"
              :title="hasTitle(onlyOneChild.meta.title)"
              >{{ onlyOneChild.meta.title }}</span
            >
          </template>
        </el-menu-item>
      </app-link>
    </template>

    <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)">
      <template v-if="item.meta" #title>
        <SvgIcon :iconClass="item.meta && item.meta.icon!" :size="18" />
        <span class="menu-title" :title="hasTitle(item.meta.title)">
          {{ item.meta.title }}
        </span>
      </template>
      <MenuTree
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :basePath="resolvePath(child.path)"
      />
    </el-sub-menu>
  </template>
</template>

<script setup lang="ts">
import { RouteRecordRaw } from 'vue-router'
import { isExternal } from '@/utils/validate.ts'
import AppLink from './Link.vue'
import { getNormalPath } from '@/utils/ruoyi'
interface MenuTreeProps {
  item: PermissionRouterRaw
  basePath: string
  isNest?: boolean
}
const { item, isNest = false, basePath = '' } = defineProps<MenuTreeProps>()

const onlyOneChild = ref<MenuTreeOnlyOneChild>()

function hasOneShowingChild(
  parent: RouteRecordRaw,
  children?: RouteRecordRaw[]
) {
  if (!children) {
    children = []
  }
  const showingChildren = children.filter((item) => {
    if (item.hidden) {
      return false
    } else {
      // Temp set(will be used if only has one showing child)
      onlyOneChild.value = item
      return true
    }
  })

  // When there is only one child router, the child router is displayed by default
  if (showingChildren.length === 1) {
    return true
  }

  // Show parent if there are no child router to display
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
    return true
  }

  return false
}
function resolveQuery(routePath: string, routeQuery?: any) {
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(basePath)) {
    return basePath
  }
  if (routeQuery) {
    let query = JSON.parse(routeQuery)
    return {
      path: getNormalPath(basePath + '/' + routePath),
      query: query,
    }
  }
  return getNormalPath(basePath + '/' + routePath)
}
function resolvePath(routePath: string) {
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(basePath)) {
    return basePath
  }
  return getNormalPath(basePath + '/' + routePath)
}

function hasTitle(title: string) {
  if (title.length > 5) {
    return title
  } else {
    return ''
  }
}
</script>

<style lang="scss" scoped>
.menu-title {
  margin-left: 5px;
}
</style>
