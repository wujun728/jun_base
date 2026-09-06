<template>
  <div class="nav-tabs" ref="tabScrollbarRef">
    <router-link
      v-for="(tag, index) in visitedViews"
      class="ba-nav-tab"
      :key="tag.path"
      :data-path="tag.path"
      :class="isActive(tag, index) ? 'active' : ''"
      :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }"
      :ref="tabsRefs.set"
      @click.middle="!isAffix(tag) ? closeSelectedTag(tag) : ''"
      @contextmenu.prevent="openMenu(tag, $event)"
    >
      {{ tag.title }}

      <transition name="el-fade-in">
        <el-icon
          class="close-icon"
          size="16"
          @click.prevent.stop="closeSelectedTag(tag)"
        >
          <Close />
        </el-icon>
      </transition>
    </router-link>
    <div :style="activeBoxStyle" class="nav-tabs-active-box"></div>
  </div>
  <ContextMenu
    ref="contextmenuRef"
    :items="state.contextmenuItems"
    @contextmenuItemClick="onContextmenuItem"
  />
</template>

<script setup>
import { getNormalPath } from '@/utils/ruoyi'
import useTagsViewStore from '@/store/modules/tagsView'
import { useConfig } from '@/store/modules/layout'
import usePermissionStore from '@/store/modules/permission'
import { useTemplateRefsList } from '@vueuse/core'
import { nextTick } from 'vue'
import ContextMenu from '../components/ContextMenu/index.vue'
import horizontalScroll from '@/utils/horizontalScroll'
const visible = ref(false)
const selectedTag = ref({})
const affixTags = ref([])
const tabScrollbarRef = ref(null)
const config = useConfig()
const proxy = inject('proxy')
const route = useRoute()
const router = useRouter()
const visitedViews = computed(() => useTagsViewStore().visitedViews)
const routes = computed(() => usePermissionStore().routes)
const tabsRefs = useTemplateRefsList()
const contextmenuRef = ref(null)

const state = reactive({
  contextmenuItems: [
    { name: 'refresh', label: '重新加载', icon: 'refresh' },
    { name: 'close', label: '关闭标签', icon: 'times' },
    // { name: 'fullScreen', label: '当前标签全屏', icon: 'el-icon-FullScreen' },
    { name: 'closeOther', label: '关闭其他标签', icon: 'minus' },
    { name: 'closeAll', label: '关闭全部标签', icon: 'stop' },
  ],
})

const onContextmenuItem = (item) => {
  const { name, menu } = item
  if (!menu) return
  switch (name) {
    case 'refresh':
      refreshSelectedTag(selectedTag.value)
      break
    case 'close':
      closeSelectedTag(selectedTag.value)
      break
    case 'closeOther':
      closeOthersTags()
      break
    case 'closeAll':
      closeAllTags(selectedTag.value)
      break
    // case 'fullScreen':
    //   if (route.path !== menu?.path) {
    //     router.push(menu?.path)
    //   }
    //   navTabs.setFullScreen(true)
    //   break
  }
}

watch(route, () => {
  addTags()
  moveToCurrentTag()
})
watch(visible, (value) => {
  if (value) {
    document.body.addEventListener('click', closeMenu)
  } else {
    document.body.removeEventListener('click', closeMenu)
  }
})
onMounted(() => {
  initTags()
  addTags()
})

const activeBoxStyle = reactive({
  width: '0',
  transform: 'translateX(0px)',
})

const activeIndex = ref()

watch(activeIndex, () => {
  setScroll(activeIndex.value)
})

const setScroll = (index) => {
  nextTick(() => {
    let dom = tabsRefs.value[index]?.$el
    if (dom) {
      activeBoxStyle.width = dom.clientWidth + 'px'
      activeBoxStyle.transform = `translateX(${dom.offsetLeft}px)`

      let scrollLeft =
        dom.offsetLeft + dom.clientWidth - tabScrollbarRef.value.clientWidth
      if (dom.offsetLeft < tabScrollbarRef.value.scrollLeft) {
        tabScrollbarRef.value.scrollTo(dom.offsetLeft, 0)
      } else if (scrollLeft > tabScrollbarRef.value.scrollLeft) {
        tabScrollbarRef.value.scrollTo(scrollLeft, 0)
      }
    }
  })
}

function isActive(r, index) {
  if (r.path === route.path) {
    activeIndex.value = index
    return true
  } else {
    return false
  }
}

function isAffix(tag) {
  return tag.meta && tag.meta.affix
}
function isFirstView() {
  try {
    return (
      selectedTag.value.fullPath === '/index' ||
      selectedTag.value.fullPath === visitedViews.value[1].fullPath
    )
  } catch (err) {
    return false
  }
}
function isLastView() {
  try {
    return (
      selectedTag.value.fullPath ===
      visitedViews.value[visitedViews.value.length - 1].fullPath
    )
  } catch (err) {
    return false
  }
}
function filterAffixTags(routes, basePath = '') {
  let tags = []
  routes.forEach((route) => {
    if (route.meta && route.meta.affix) {
      const tagPath = getNormalPath(basePath + '/' + route.path)
      tags.push({
        fullPath: tagPath,
        path: tagPath,
        name: route.name,
        meta: { ...route.meta },
      })
    }
    if (route.children) {
      const tempTags = filterAffixTags(route.children, route.path)
      if (tempTags.length >= 1) {
        tags = [...tags, ...tempTags]
      }
    }
  })
  return tags
}
function initTags() {
  const res = filterAffixTags(routes.value)
  affixTags.value = res
  for (const tag of res) {
    if (tag.name) {
      useTagsViewStore().addVisitedView(tag)
    }
  }
}
function addTags() {
  const { name } = route
  if (name) {
    useTagsViewStore().addView(route)
    if (route.meta.link) {
      useTagsViewStore().addIframeView(route)
    }
  }
  return false
}
function moveToCurrentTag() {
  nextTick(() => {
    for (const r of visitedViews.value) {
      if (r.path === route.path) {
        if (r.fullPath !== route.fullPath) {
          useTagsViewStore().updateVisitedView(route)
        }
      }
    }
  })
}
function refreshSelectedTag(view) {
  proxy.$tab.refreshPage(view)
  if (route.meta.link) {
    useTagsViewStore().delIframeView(route)
  }
}
function closeSelectedTag(view) {
  proxy.$tab.closePage(view).then(({ visitedViews }) => {
    if (isActive(view)) {
      toLastView(visitedViews, view)
    }
  })
}
function closeRightTags() {
  proxy.$tab.closeRightPage(selectedTag.value).then((visitedViews) => {
    if (!visitedViews.find((i) => i.fullPath === route.fullPath)) {
      toLastView(visitedViews)
    }
  })
}
function closeLeftTags() {
  proxy.$tab.closeLeftPage(selectedTag.value).then((visitedViews) => {
    if (!visitedViews.find((i) => i.fullPath === route.fullPath)) {
      toLastView(visitedViews)
    }
  })
}
function closeOthersTags() {
  router.push(selectedTag.value).catch(() => {})
  proxy.$tab.closeOtherPage(selectedTag.value).then(() => {
    moveToCurrentTag()
  })
}
function closeAllTags(view) {
  proxy.$tab.closeAllPage().then(({ visitedViews }) => {
    if (affixTags.value.some((tag) => tag.path === route.path)) {
      return
    }
    toLastView(visitedViews, view)
  })
}
function toLastView(visitedViews, view) {
  const latestView = visitedViews.slice(-1)[0]
  if (latestView) {
    router.push(latestView.fullPath)
  } else {
    if (view.name === 'Index') {
      router.replace({ path: '/redirect' + view.fullPath })
    } else {
      router.push('/')
    }
  }
}
function openMenu(tag, el) {
  selectedTag.value = tag
  // 禁用刷新
  state.contextmenuItems[0].disabled = route.path !== tag.path
  // // 禁用关闭其他和关闭全部
  state.contextmenuItems[2].disabled = state.contextmenuItems[3].disabled =
    visitedViews.value.length == 1 ? true : false

  const { clientX, clientY } = el
  contextmenuRef.value.onShowContextmenu(tag, {
    x: clientX,
    y: clientY,
  })
}
function closeMenu() {
  visible.value = false
}
onMounted(() => {
  initTags()
  addTags()
  new horizontalScroll(tabScrollbarRef.value)
})
</script>

<style scoped lang="scss">
.dark {
  .close-icon {
    color: v-bind('config.getColorVal("headerBarTabColor")') !important;
  }
  .ba-nav-tab.active {
    .close-icon {
      color: v-bind('config.getColorVal("headerBarTabActiveColor")') !important;
    }
  }
}
.nav-tabs {
  width: 100%;
  overflow-x: auto;
  overflow-y: hidden;
  margin-right: var(--ba-main-space);
  scrollbar-width: none;
  // &::-webkit-scrollbar-thumb {
  //   background: #eaeaea;
  //   border-radius: var(--el-border-radius-base);
  //   box-shadow: none;
  //   -webkit-box-shadow: none;
  // }
  // &::-webkit-scrollbar-track {
  //   background: v-bind(
  //     'config.layout.layoutMode == "Default" ? "none":config.getColorVal("headerBarBackground")'
  //   );
  // }
  // &:hover {
  //   &::-webkit-scrollbar-thumb:hover {
  //     background: #c8c9cc;
  //   }
  // }
}
.ba-nav-tab {
  white-space: nowrap;
  height: 40px;
}
</style>
