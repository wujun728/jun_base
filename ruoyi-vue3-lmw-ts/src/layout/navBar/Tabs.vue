<template>
  <div class="nav-tabs" ref="tabScrollbarRef">
    <router-link
      v-for="(tag, index) in visitedViews"
      class="ba-nav-tab"
      :key="tag.path"
      :data-path="tag.path"
      :class="isActive(tag, index) ? 'active' : ''"
      :to="{ path: tag.path, query: tag.query }"
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

<script setup lang="ts">
import { nextTick } from 'vue'
import { RouterLink } from 'vue-router'
import { getNormalPath } from '@/utils/ruoyi'
import useTagsViewStore from '@/store/modules/tagsView'
import useConfig from '@/store/modules/layout'
import usePermissionStore from '@/store/modules/permission'
import { useTemplateRefsList } from '@vueuse/core'
import ContextMenu from '../components/ContextMenu/index.vue'
import horizontalScroll from '@/utils/horizontalScroll.ts'
import { proxy } from '@/utils/provide'
const visible = ref(false)
const selectedTag = ref<TagsType>({})
const affixTags = ref<PermissionRouterRaw[]>([])
const tabScrollbarRef = useTemplateRef('tabScrollbarRef')
const config = useConfig()
const route = useRoute()
const router = useRouter()
const visitedViews = computed(() => useTagsViewStore().visitedViews)
const routes = computed(() => usePermissionStore().routes)
const tabsRefs = useTemplateRefsList<any>()
const contextmenuRef = useTemplateRef('contextmenuRef')
const state = reactive({
  contextmenuItems: [
    { name: 'refresh', label: '重新加载', icon: 'refresh', disabled: false },
    { name: 'close', label: '关闭标签', icon: 'times', disabled: false },
    {
      name: 'closeOther',
      label: '关闭其他标签',
      icon: 'minus',
      disabled: false,
    },
    { name: 'closeAll', label: '关闭全部标签', icon: 'stop', disabled: false },
  ],
})

const onContextmenuItem = (item: ContextMenuItem) => {
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

const activeBoxStyle = reactive({
  width: '0',
  transform: 'translateX(0px)',
})

const activeIndex = ref<number | undefined>()

watch(activeIndex, () => {
  setScroll(activeIndex.value)
})

const setScroll = (index?: number) => {
  if (!index && index !== 0) return
  nextTick(() => {
    let dom = tabsRefs.value[index]?.$el
    if (dom) {
      activeBoxStyle.width = dom.clientWidth + 'px'
      activeBoxStyle.transform = `translateX(${dom.offsetLeft}px)`

      let scrollLeft =
        dom.offsetLeft + dom.clientWidth - tabScrollbarRef.value?.clientWidth!
      if (dom.offsetLeft < tabScrollbarRef.value?.scrollLeft!) {
        tabScrollbarRef.value?.scrollTo(dom.offsetLeft, 0)
      } else if (scrollLeft > tabScrollbarRef.value?.scrollLeft!) {
        tabScrollbarRef.value?.scrollTo(scrollLeft, 0)
      }
    }
  })
}

function isActive(r: TagsType, index?: number) {
  if (r.path === route.path) {
    activeIndex.value = index
    return true
  } else {
    return false
  }
}

function isAffix(tag: TagsType) {
  return tag.meta && tag.meta.affix
}

function filterAffixTags(routes: PermissionRouterRaw[], basePath = '') {
  let tags: PermissionRouterRaw[] = []
  routes.forEach((route) => {
    if (route.meta && route.meta.affix) {
      const tagPath = getNormalPath(basePath + '/' + route.path)
      tags.push({
        ...route,
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
function refreshSelectedTag(view: TagsType) {
  proxy.$tab.refreshPage(view)
  if (route.meta.link) {
    useTagsViewStore().delIframeView(route)
  }
}
function closeSelectedTag(view: TagsType) {
  proxy.$tab.closePage(view).then(({ visitedViews }) => {
    if (isActive(view)) {
      toLastView(visitedViews, view)
    }
  })
}
function closeOthersTags() {
  router.push(selectedTag.value).catch(() => {})
  proxy.$tab.closeOtherPage(selectedTag.value).then(() => {
    moveToCurrentTag()
  })
}
function closeAllTags(view: TagsType) {
  proxy.$tab.closeAllPage().then(({ visitedViews }) => {
    if (affixTags.value.some((tag) => tag.path === route.path)) {
      return
    }
    toLastView(visitedViews, view)
  })
}
function toLastView(visitedViews: TagsType[], view?: TagsType) {
  const latestView = visitedViews.slice(-1)[0]
  if (latestView) {
    router.push(latestView.fullPath!)
  } else {
    if (view?.name === 'Index') {
      router.replace({ path: '/redirect' + view.fullPath })
    } else {
      router.push('/')
    }
  }
}
function openMenu(tag: TagsType, el: any) {
  selectedTag.value = tag
  // 禁用刷新
  state.contextmenuItems[0].disabled = route.path !== tag.path
  // // 禁用关闭其他和关闭全部
  state.contextmenuItems[2].disabled = state.contextmenuItems[3].disabled =
    visitedViews.value.length == 1 ? true : false

  const { clientX, clientY } = el
  contextmenuRef.value?.onShowContextmenu(tag, {
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
  new horizontalScroll(tabScrollbarRef.value!)
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
}
.ba-nav-tab {
  white-space: nowrap;
  height: 40px;
}
</style>
