<script setup>
import { useConfig } from '@/store/modules/layout'
import usePermissionStore from '@/store/modules/permission'
import { getMenuSubmenuKey, resolveSubMenuKey } from './menuPath'
import MenuTree from './MenuTree.vue'

const permissionStore = usePermissionStore()
const config = useConfig()
const route = useRoute()

const sidebarRouters = computed(() => permissionStore.sidebarRouters)

const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})

const openKeys = ref([])
const preOpenKeys = ref([])
const skipOpenChange = ref(false)

function isSubMenuRoute(route) {
  if (route.hidden) {
    return false
  }
  const children = (route.children || []).filter((child) => !child.hidden)
  if (!children.length) {
    return false
  }
  if (children.length === 1 && !route.alwaysShow) {
    const onlyChild = children[0]
    const grandChildren = (onlyChild.children || []).filter(
      (child) => !child.hidden
    )
    if (!grandChildren.length) {
      return false
    }
  }
  return true
}

const rootSubmenuKeys = computed(() =>
  sidebarRouters.value
    .filter(isSubMenuRoute)
    .map((route) => resolveSubMenuKey(route.path, route.path, false))
)

function getOpenKeys() {
  const targetPath = activeMenu.value
  const keys = []

  function collectOpenKeys(routes, basePath) {
    routes.forEach((menuRoute) => {
      if (menuRoute.hidden) {
        return
      }

      const menuKey = getMenuSubmenuKey(menuRoute, basePath)
      const children = (menuRoute.children || []).filter(
        (child) => !child.hidden
      )

      if (!children.length) {
        return
      }

      const isActiveBranch =
        targetPath === menuKey || targetPath.startsWith(`${menuKey}/`)

      if (!isActiveBranch) {
        return
      }

      if (isSubMenuRoute(menuRoute)) {
        keys.push(menuKey)
      }

      collectOpenKeys(children, menuKey)
    })
  }

  collectOpenKeys(sidebarRouters.value, '')
  return keys
}

function syncOpenKeysFromRoute() {
  if (config.layout.menuCollapse) {
    return
  }
  skipOpenChange.value = true
  const routeKeys = getOpenKeys()
  if (config.layout.menuUniqueOpened) {
    openKeys.value = routeKeys
  } else {
    openKeys.value = [...new Set([...openKeys.value, ...routeKeys])]
  }
  nextTick(() => {
    skipOpenChange.value = false
  })
}

watch(
  () => activeMenu.value,
  () => {
    syncOpenKeysFromRoute()
  },
  { immediate: true }
)

watch(sidebarRouters, () => syncOpenKeysFromRoute())

watch(
  () => config.layout.menuCollapse,
  (collapsed) => {
    if (collapsed) {
      preOpenKeys.value = [...openKeys.value]
      openKeys.value = []
    } else {
      openKeys.value = preOpenKeys.value.length
        ? preOpenKeys.value
        : getOpenKeys()
    }
  }
)

function onOpenChange(keys) {
  if (skipOpenChange.value) {
    return
  }

  if (!config.layout.menuUniqueOpened) {
    openKeys.value = keys
    return
  }

  const latestOpenKey = keys.find((key) => !openKeys.value.includes(key))

  if (latestOpenKey && rootSubmenuKeys.value.includes(latestOpenKey)) {
    openKeys.value = keys.filter((key) => {
      if (rootSubmenuKeys.value.includes(key)) {
        return key === latestOpenKey
      }
      return key.startsWith(`${latestOpenKey}/`)
    })
    return
  }

  openKeys.value = keys
}
</script>

<template>
  <div
    class="vertical-menus"
    :class="{ 'is-collapsed': config.layout.menuCollapse }"
  >
    <a-menu
      class="layouts-menu-vertical"
      mode="inline"
      theme="light"
      :selected-keys="[activeMenu]"
      :open-keys="openKeys"
      @openChange="onOpenChange"
    >
      <MenuTree
        v-for="(menuRoute, index) in sidebarRouters"
        :item="menuRoute"
        :base-path="menuRoute.path"
        :key="menuRoute.path + index"
      />
    </a-menu>
  </div>
</template>

<style scoped lang="scss">
.vertical-menus {
  flex: 1;
  overflow: hidden auto;
  padding: 0 12px 12px;

  :deep(.layouts-menu-vertical) {
    border-inline-end: none !important;
    background: transparent;

    .ant-menu-item,
    .ant-menu-submenu-title {
      margin: 2px 0;
      border-radius: 8px;
      width: 100%;
      color: var(--layout-menu-color, rgba(0, 0, 0, 0.65));
      transition: all 0.2s;

      &:hover {
        color: var(--layout-menu-active-color, rgba(0, 0, 0, 0.88));
        background: var(--layout-menu-hover-bg, rgba(0, 0, 0, 0.04));
      }
    }

    .ant-menu-item-selected {
      color: var(--layout-menu-active-color, rgba(0, 0, 0, 0.88));
      background: var(--layout-menu-active-bg, rgba(0, 0, 0, 0.06));
      font-weight: 500;
    }

    .ant-menu-submenu-selected > .ant-menu-submenu-title {
      color: var(--layout-menu-active-color, rgba(0, 0, 0, 0.88));
      font-weight: 500;
    }

    .ant-menu-sub {
      background: transparent !important;
    }

    .ant-menu-item a,
    .ant-menu-title-content a {
      color: inherit;
      text-decoration: none;
    }

    &.ant-menu-inline-collapsed {
      width: 100%;

      .ant-menu-item,
      .ant-menu-submenu-title {
        padding-inline: calc(50% - 8px) !important;
      }

      .ant-menu-submenu-arrow {
        display: none;
      }
    }
  }

  &.is-collapsed {
    padding: 0 4px 12px;
  }
}
</style>
