<template>
  <template v-if="!item.hidden">
    <template
      v-if="
        hasOneShowingChild(item.children, item) &&
        (!onlyOneChild.children || onlyOneChild.noShowingChildren) &&
        !item.alwaysShow
      "
    >
      <a-menu-item
        v-if="onlyOneChild.meta"
        :key="resolvePath(onlyOneChild.path)"
        :class="{ 'submenu-title-noDropdown': !isNest }"
      >
        <template #icon>
          <SvgIcon
            :icon-class="
              resolveMenuIcon(
                onlyOneChild.meta.icon || (item.meta && item.meta.icon)
              )
            "
            :size="16"
          />
        </template>
        <app-link :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
          <span class="menu-title" :title="hasTitle(onlyOneChild.meta.title)">
            {{ onlyOneChild.meta.title }}
          </span>
        </app-link>
      </a-menu-item>
    </template>

    <a-sub-menu v-else :key="resolveSubMenuKey(basePath, item.path, isNest)">
      <template #icon>
        <SvgIcon
          :icon-class="resolveMenuIcon(item.meta && item.meta.icon, 'nested')"
          :size="16"
        />
      </template>
      <template v-if="item.meta" #title>
        <span class="menu-title" :title="hasTitle(item.meta.title)">
          {{ item.meta.title }}
        </span>
      </template>
      <MenuTree
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
      />
    </a-sub-menu>
  </template>
</template>

<script setup>
import { isExternal } from '@/utils/validate'
import { useConfig } from '@/store/modules/layout'
import { resolveMenuPath, resolveSubMenuKey } from './menuPath'
import AppLink from './Link.vue'

defineOptions({ name: 'MenuTree' })

const config = useConfig()

const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
  isNest: {
    type: Boolean,
    default: false,
  },
  basePath: {
    type: String,
    default: '',
  },
})

const onlyOneChild = ref({})

function resolveMenuIcon(icon, fallback = 'dashboard') {
  if (icon && !String(icon).startsWith('fa ')) {
    return icon
  }
  const defaultIcon = config.layout.menuDefaultIcon
  if (defaultIcon && !String(defaultIcon).startsWith('fa ')) {
    return defaultIcon
  }
  return fallback
}

function hasOneShowingChild(children, parent) {
  if (!children) {
    children = []
  }
  const showingChildren = children.filter((item) => {
    if (item.hidden) {
      return false
    }
    onlyOneChild.value = item
    return true
  })

  if (showingChildren.length === 1) {
    return true
  }

  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
    return true
  }

  return false
}

function resolvePath(routePath, routeQuery) {
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  if (routeQuery) {
    const query = JSON.parse(routeQuery)
    return {
      path: resolveMenuPath(props.basePath, routePath),
      query,
    }
  }
  return resolveMenuPath(props.basePath, routePath)
}

function hasTitle(title) {
  return title?.length > 5 ? title : ''
}
</script>

<style lang="scss" scoped>
.menu-title {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
