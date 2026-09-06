<script setup>
import usePermissionStore from '@/store/modules/permission'
import { getDialogWidth, getNormalPath } from '@/utils/utils'
import { isHttp } from '@/utils/validate'

defineOptions({ name: 'GlobalSearch' })

const visible = defineModel('visible', { type: Boolean, default: false })

const router = useRouter()
const permissionStore = usePermissionStore()

const routes = computed(() => permissionStore.sidebarRouters)
const searchValue = ref('')
const searchPool = ref([])
const activeIndex = ref(0)
const history = ref([])

const inputRef = useTemplateRef('inputRef')
const listRef = useTemplateRef('listRef')

function generateRoutes(routeList, basePath = '', prefixTitle = []) {
  let res = []
  for (const route of routeList) {
    if (route.hidden) continue

    const p =
      route.path.length > 0 && route.path[0] === '/'
        ? route.path
        : `/${route.path}`
    const data = {
      path: !isHttp(route.path) ? getNormalPath(basePath + p) : route.path,
      title: [...prefixTitle],
      icon: route.meta?.icon ?? 'tags',
    }

    if (route.meta?.title) {
      data.title = [...data.title, route.meta.title]
      if (route.redirect !== 'noRedirect') {
        res.push(data)
      }
    }

    if (route.query) {
      data.query = route.query
    }

    if (route.children) {
      const tempRoutes = generateRoutes(route.children, data.path, data.title)
      if (tempRoutes.length >= 1) {
        res = [...res, ...tempRoutes]
      }
    }
  }
  return res
}

const filterRouter = computed(() => {
  if (!searchValue.value) {
    return history.value
  }
  return searchPool.value.filter((item) => {
    const titleText = item.title.join('/')
    return (
      titleText.includes(searchValue.value) ||
      item.path.includes(searchValue.value)
    )
  })
})

watch(
  () => routes.value,
  (newRoutes) => {
    searchPool.value = generateRoutes(newRoutes)
  },
  { immediate: true, deep: true }
)

watch(activeIndex, (val) => {
  nextTick(() => {
    const container = listRef.value
    const activeEl = container?.querySelector('.filter-item.active')
    if (activeEl) {
      activeEl.scrollIntoView({ block: 'nearest' })
    } else if (val <= 6) {
      container && (container.scrollTop = 0)
    }
  })
})

watch(visible, (open) => {
  if (!open) {
    activeIndex.value = 0
    searchValue.value = ''
  } else {
    nextTick(() => {
      inputRef.value?.focus()
    })
  }
})

function jump(item) {
  const { path, query } = item
  if (isHttp(path)) {
    window.open(path, '_blank')
    return
  }
  if (query) {
    router.push({
      path,
      query: typeof query === 'string' ? JSON.parse(query) : query,
    })
  } else {
    router.push(path)
  }

  const exists = history.value.find((current) => current.path === item.path)
  if (!exists) {
    history.value.unshift(item)
  }

  nextTick(() => {
    visible.value = false
  })
}

function keyEvent(e) {
  if (!visible.value || !filterRouter.value.length) return

  if (e.key === 'ArrowDown') {
    e.preventDefault()
    activeIndex.value =
      activeIndex.value < filterRouter.value.length - 1
        ? activeIndex.value + 1
        : 0
  }
  if (e.key === 'ArrowUp') {
    e.preventDefault()
    activeIndex.value =
      activeIndex.value > 0
        ? activeIndex.value - 1
        : filterRouter.value.length - 1
  }
  if (e.key === 'Enter') {
    e.preventDefault()
    const item = filterRouter.value[activeIndex.value]
    if (item) jump(item)
  }
}

function openSearch() {
  visible.value = true
}

function onGlobalKeydown(e) {
  if ((e.ctrlKey || e.metaKey) && (e.key === 'k' || e.key === '/')) {
    e.preventDefault()
    visible.value = true
  }
}

onMounted(() => {
  window.addEventListener('keydown', onGlobalKeydown)
})

onUnmounted(() => {
  window.removeEventListener('keydown', onGlobalKeydown)
})
</script>

<template>
  <div class="global-search" @keydown="keyEvent">
    <button type="button" class="nav-search" @click="openSearch">
      <SvgIcon
        icon-class="ant-icon-SearchOutlined"
        :size="14"
        class-name="nav-search-icon"
      />
      <span class="nav-search-placeholder">搜索...</span>
      <kbd class="nav-search-kbd">⌘K</kbd>
    </button>

    <a-modal
      v-model:open="visible"
      :footer="null"
      :closable="false"
      centered
      :width="getDialogWidth(600)"
      wrap-class-name="global-search-modal"
    >
      <div class="global-search-header">
        <a-input
          ref="inputRef"
          v-model:value="searchValue"
          class="global-search-input"
          placeholder="菜单搜索 (Ctrl+K / ⌘K)"
          allow-clear
          name="global-search-input"
          @keydown="keyEvent"
        >
          <template #prefix>
            <SvgIcon icon-class="ant-icon-SearchOutlined" :size="14" />
          </template>
        </a-input>
      </div>

      <div ref="listRef" class="global-search-list">
        <div
          v-for="(item, index) in filterRouter"
          :key="item.path"
          class="filter-item"
          :class="{ active: activeIndex === index }"
          @click="jump(item)"
          @mouseenter="activeIndex = index"
        >
          <div class="filter-icon">
            <SvgIcon :icon-class="item.icon" :size="16" />
          </div>
          <div class="filter-title">
            <template
              v-for="(title, titleIndex) in item.title"
              :key="titleIndex"
            >
              <span>{{ title }}</span>
              <SvgIcon
                v-if="titleIndex !== item.title.length - 1"
                icon-class="angle-right"
                :size="12"
              />
            </template>
          </div>
        </div>
        <a-empty
          v-if="!filterRouter.length"
          class="global-search-empty"
          description="暂无匹配菜单"
        />
      </div>
    </a-modal>
  </div>
</template>

<style scoped lang="scss">
.nav-search {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 160px;
  height: 28px;
  padding: 0 12px;
  border: none;
  border-radius: 8px;
  background: var(--ba-bg-color);
  cursor: pointer;
  transition: all 0.2s;
  color: #c2c2c2;
}

.nav-search-icon {
  font-size: 14px;
}

.nav-search-placeholder {
  flex: 1;
  text-align: left;
  font-size: 13px;
}

.nav-search-kbd {
  display: inline-flex;
  align-items: center;
  padding: 2px 6px;
  border-radius: 4px;
  background: var(--ba-bg-color);
  font-size: 11px;
  font-family: inherit;
}

.global-search-header {
  margin-bottom: 8px;
}

.global-search-input {
  :deep(.ant-input) {
    height: 40px;
    font-size: 16px;
  }
}

.global-search-list {
  max-height: 424px;
  overflow-y: auto;
  padding: 0 4px;
}

.filter-item {
  display: flex;
  align-items: center;
  min-height: 44px;
  padding: 0 8px;
  margin: 4px 0;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover,
  &.active {
    background: var(--layout-menu-hover-bg, rgba(0, 0, 0, 0.04));
  }

  &.active {
    background: var(--layout-menu-active-bg, rgba(0, 0, 0, 0.06));
    color: var(--layout-menu-active-color, rgba(0, 0, 0, 0.88));
  }
}

.filter-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  flex-shrink: 0;
}

.filter-title {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-wrap: wrap;
  font-size: 14px;
}

.global-search-empty {
  padding: 24px 0;
}
.dark {
  .nav-search {
    color: #525252;
  }
}
@media (max-width: 768px) {
  .nav-search {
    display: none;
  }
}
</style>

<style lang="scss">
.global-search-modal {
  .ant-modal-content {
    border-radius: 10px;
    overflow: hidden;
  }

  .ant-modal-body {
    padding: 16px;
  }
}
</style>
