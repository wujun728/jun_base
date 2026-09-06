<script setup lang="ts">
import type { RouteItem } from '@/types/route'

defineOptions({ name: 'TopNavItem' })

const props = defineProps<{
  item: RouteItem
  resolvePath: (path: string, parentPath?: string) => string
  getChildren: (route: RouteItem) => RouteItem[]
}>()

function getItemIndex(route: RouteItem) {
  return props.resolvePath(route.path, route.parentPath)
}
</script>

<template>
  <el-menu-item v-if="!getChildren(item).length" :index="getItemIndex(item)">
    <svg-icon :icon-class="item.meta?.icon || 'dashboard'" />
    <span>{{ item.meta?.title }}</span>
  </el-menu-item>

  <el-sub-menu v-else :index="getItemIndex(item)">
    <template #title>
      <svg-icon :icon-class="item.meta?.icon || 'dashboard'" />
      <span>{{ item.meta?.title }}</span>
    </template>
    <top-nav-item v-for="child in getChildren(item)" :key="getItemIndex(child)" :item="child"
      :resolve-path="resolvePath" :get-children="getChildren" />
  </el-sub-menu>
</template>