<script setup>
import { useTemplateRef } from 'vue'
import tab from '@/plugins/tab'
import useTagsViewStore from '@/store/modules/tagsView'
import ContextMenu from '@/layout/components/ContextMenu/index.vue'
import horizontalScroll from '@/utils/horizontalScroll'

defineOptions({ name: 'NavTabs' })

const route = useRoute()
const router = useRouter()
const tagsViewStore = useTagsViewStore()

const tabScrollbarRef = useTemplateRef('tabScrollbarRef')
const contextmenuRef = useTemplateRef('contextmenuRef')

const selectedTag = ref({})
const activeIndex = ref()
const scrollInstance = ref(null)

const visitedViews = computed(() => tagsViewStore.visitedViews)

const contextmenuItems = reactive([
  { name: 'refresh', label: '重新加载', icon: 'sync-alt', disabled: false },
  { name: 'close', label: '关闭标签', icon: 'close', disabled: false },
  {
    name: 'closeOther',
    label: '关闭其他标签',
    icon: 'minus',
    disabled: false,
  },
  { name: 'closeAll', label: '关闭全部标签', icon: 'stop', disabled: false },
])

const activeBoxStyle = reactive({
  width: '0',
  transform: 'translateX(0px)',
})

watch(
  () => route.fullPath,
  () => {
    addTags()
    moveToCurrentTag()
  }
)

watch(activeIndex, (index) => {
  setScroll(index)
})

function isActive(tag, index) {
  if (tag.path === route.path) {
    activeIndex.value = index
    return true
  }
  return false
}

function addTags() {
  const { name } = route
  if (name) {
    tagsViewStore.addView(route)
    if (route.meta.link) {
      tagsViewStore.addIframeView(route)
    }
  }
}

function moveToCurrentTag() {
  nextTick(() => {
    for (const item of visitedViews.value) {
      if (item.path === route.path && item.fullPath !== route.fullPath) {
        tagsViewStore.updateVisitedView(route)
      }
    }
  })
}

function setScroll(index) {
  if (index !== 0 && !index) return
  nextTick(() => {
    const container = tabScrollbarRef.value
    const dom = container?.querySelectorAll('.ba-nav-tab')[index]
    if (!dom || !container) return

    activeBoxStyle.width = `${dom.clientWidth}px`
    activeBoxStyle.transform = `translateX(${dom.offsetLeft}px)`

    const scrollLeft = dom.offsetLeft + dom.clientWidth - container.clientWidth
    if (dom.offsetLeft < container.scrollLeft) {
      container.scrollTo(dom.offsetLeft, 0)
    } else if (scrollLeft > container.scrollLeft) {
      container.scrollTo(scrollLeft, 0)
    }
  })
}

function refreshSelectedTag(view) {
  tab.refreshPage(view)
  if (route.meta.link) {
    tagsViewStore.delIframeView(route)
  }
}

function toLastView(views, view) {
  const latestView = views.slice(-1)[0]
  if (latestView) {
    router.push(latestView.fullPath)
  } else if (view?.name === 'Index') {
    router.replace({ path: `/redirect${view.fullPath}` })
  } else {
    router.push('/')
  }
}

function closeSelectedTag(view) {
  tab.closePage(view).then(({ visitedViews: views }) => {
    if (isActive(view)) {
      toLastView(views, view)
    }
  })
}

function closeOthersTags() {
  const target = selectedTag.value
  router.push(target.fullPath || target.path).catch(() => {})
  tab.closeOtherPage(target).then(() => {
    moveToCurrentTag()
  })
}

function closeAllTags(view) {
  tab.closeAllPage().then(({ visitedViews: views }) => {
    toLastView(views, view)
  })
}

function onContextmenuItem(item) {
  const { name, menu } = item
  if (!menu) return
  switch (name) {
    case 'refresh':
      refreshSelectedTag(menu)
      break
    case 'close':
      closeSelectedTag(menu)
      break
    case 'closeOther':
      closeOthersTags()
      break
    case 'closeAll':
      closeAllTags(menu)
      break
  }
}

function openMenu(tag, event) {
  selectedTag.value = tag
  contextmenuItems[0].disabled = route.path !== tag.path
  contextmenuItems[2].disabled = contextmenuItems[3].disabled =
    visitedViews.value.length === 1

  contextmenuRef.value?.onShowContextmenu(tag, {
    x: event.clientX,
    y: event.clientY,
  })
}

onMounted(() => {
  addTags()
  if (tabScrollbarRef.value) {
    scrollInstance.value = new horizontalScroll(tabScrollbarRef.value)
  }
})

onBeforeUnmount(() => {
  scrollInstance.value = null
})
</script>

<template>
  <div class="nav-tabs-panel">
    <div ref="tabScrollbarRef" class="nav-tabs">
      <router-link
        v-for="(tag, index) in visitedViews"
        :key="tag.path"
        class="ba-nav-tab"
        :class="{ active: isActive(tag, index) }"
        :data-path="tag.path"
        :to="{ path: tag.path, query: tag.query }"
        @click.middle="closeSelectedTag(tag)"
        @contextmenu.prevent="openMenu(tag, $event)"
      >
        <span class="ba-nav-tab-title">{{ tag.title }}</span>
        <SvgIcon
          icon-class="ant-icon-CloseOutlined"
          class-name="close-icon"
          :size="10"
          @click.prevent.stop="closeSelectedTag(tag)"
        />
      </router-link>
      <div :style="activeBoxStyle" class="nav-tabs-active-box"></div>
    </div>

    <ContextMenu
      ref="contextmenuRef"
      :items="contextmenuItems"
      @contextmenu-item-click="onContextmenuItem"
    />
  </div>
</template>

<style scoped lang="scss">
.nav-tabs-panel {
  width: 100%;
}

.nav-tabs {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  min-height: 40px;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: none;

  &::-webkit-scrollbar {
    display: none;
  }
}

.ba-nav-tab {
  position: relative;
  z-index: 1;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  height: 32px;
  padding: 0 12px;
  margin-right: 4px;
  border-radius: 6px;
  white-space: nowrap;
  font-size: 13px;
  color: var(--layout-menu-color, rgba(0, 0, 0, 0.65));
  text-decoration: none;
  transition:
    color 0.2s,
    background-color 0.2s;

  &:hover {
    color: var(--layout-menu-active-color, rgba(0, 0, 0, 0.88));
    background: var(--layout-menu-hover-bg, rgba(0, 0, 0, 0.04));
  }

  &.active {
    color: var(--layout-menu-active-color, rgba(0, 0, 0, 0.88));
    background: var(--layout-menu-active-bg, rgba(0, 0, 0, 0.06));
  }
}

.ba-nav-tab-title {
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.close-icon {
  font-size: 10px;
  opacity: 0.6;
  transition: opacity 0.2s;

  &:hover {
    opacity: 1;
  }
}

.nav-tabs-active-box {
  position: absolute;
  top: 0;
  left: 0;
  height: 32px;
  border-radius: 6px;
  pointer-events: none;
  transition:
    transform 0.2s ease,
    width 0.2s ease;
  display: none;
}

.dark {
  .ba-nav-tab.active .close-icon {
    color: var(--layout-menu-active-color, rgba(255, 255, 255, 0.88));
  }
}
</style>
