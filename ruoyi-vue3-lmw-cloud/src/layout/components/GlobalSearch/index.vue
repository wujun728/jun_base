<script setup>
import { useRouter } from 'vue-router'
import usePermissionStore from '@/store/modules/permission'
import { isHttp } from '@/utils/validate'
import { getNormalPath } from '@/utils/ruoyi'
import { computed, nextTick, watch } from 'vue'

const routes = computed(() => usePermissionStore().sidebarRouters)
const router = useRouter()
const visible = defineModel('visible')
const searchValue = ref('')
const inputRef = ref(null)
const searchPool = ref([])
const activeIndex = ref(0)
const scrollRef = ref(null)
const history = []
const generateRoutes = (routes, basePath = '', prefixTitle = []) => {
  let res = []
  for (const r of routes) {
    if (r.hidden) {
      continue
    }
    const p = r.path.length > 0 && r.path[0] === '/' ? r.path : '/' + r.path
    const data = {
      path: !isHttp(r.path) ? getNormalPath(basePath + p) : r.path,
      title: [...prefixTitle],
      icon: r.meta?.icon ?? 'tags',
    }
    if (r.meta?.title) {
      data.title = [...data.title, r.meta.title]
      if (r.redirect !== 'noRedirect') {
        res.push(data)
      }
    }
    if (r.query) {
      data.query = r.query
    }
    if (r.children) {
      const tempRoutes = generateRoutes(r.children, data.path, data.title)
      if (tempRoutes.length >= 1) {
        res = [...res, ...tempRoutes]
      }
    }
  }
  return res
}
const filterRouter = computed(() => {
  if (!searchValue.value) {
    return history
  }
  return searchPool.value.filter((item) => {
    return (
      item.title.toString().includes(searchValue.value) ||
      item.path.includes(searchValue.value)
    )
  })
})
const keyEvent = (e) => {
  if (e.key === 'ArrowDown') {
    if (activeIndex.value < filterRouter.value.length - 1) {
      ++activeIndex.value
    } else {
      activeIndex.value = 0
    }
  }
  if (e.key === 'ArrowUp') {
    if (activeIndex.value > 0) {
      --activeIndex.value
    } else {
      activeIndex.value = filterRouter.value.length - 1
    }
  }
  if (e.key === 'Enter') {
    const item = filterRouter.value[activeIndex.value]
    if (item) {
      jump(item)
    }
  }
}
watch(
  () => activeIndex.value,
  (val) => {
    if (val > 6) {
      const top = 52 * (val - 6)
      scrollRef.value.setScrollTop(top)
    } else {
      scrollRef.value.setScrollTop(0)
    }
  }
)
const jump = (item) => {
  const { path, query } = item
  if (isHttp(path)) {
    window.open(path, '_blank')
    return
  }
  if (query) {
    router.push({ path: path, query: JSON.parse(query) })
  } else {
    router.push(path)
  }
  const isIncludes = history.find((current) => {
    return current.path === item.path
  })
  if (!isIncludes) {
    history.unshift(item)
  }
  nextTick(() => {
    visible.value = false
  })
}
const opened = () => {
  inputRef.value?.focus()
}
const closed = () => {
  activeIndex.value = 0
}
onMounted(() => {
  searchPool.value = generateRoutes(routes.value)
})

watchEffect(() => {
  searchPool.value = generateRoutes(routes.value)
})

document.addEventListener('keydown', function (event) {
  if (event.ctrlKey && event.key === 'k') {
    visible.value = true
  }
})
</script>
<template>
  <div class="searchDialog" @keydown="keyEvent">
    <el-dialog
      v-model="visible"
      :width="getWidth(600)"
      :show-close="false"
      draggable
      @opened="opened"
      @closed="closed"
    >
      <template #header>
        <div class="header flexCenter">
          <el-input
            ref="inputRef"
            placeholder="菜单搜索(ctrl+k)"
            class="searchInput"
            v-model="searchValue"
          >
            <template #prefix>
              <SvgIcon
                size="18"
                color="var(--el-text-color-primary)"
                iconClass="el-icon-Search"
              ></SvgIcon>
            </template>
          </el-input>
        </div>
      </template>
      <el-scrollbar max-height="424" ref="scrollRef">
        <div class="filter">
          <div
            v-for="(item, index) in filterRouter"
            class="filterItem"
            :class="{
              active: activeIndex === index,
            }"
            @click="jump(item)"
            :key="item.path"
          >
            <div class="filterIcon flexCenter">
              <SvgIcon :iconClass="item.icon" size="16"></SvgIcon>
            </div>
            <div class="title">
              <span v-for="(title, index) in item.title">
                {{ title }}
                <SvgIcon
                  v-if="index !== item.title.length - 1"
                  iconClass="angle-right"
                ></SvgIcon>
              </span>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.searchDialog {
  :deep(.el-dialog__header) {
    padding: 0;
  }
  :deep(.el-dialog) {
    border-radius: 10px;
    overflow: hidden;
  }
  :deep(.el-dialog__body) {
    padding: 0;
  }
  :deep(.el-dialog__header) {
    border: none;
  }
  :deep(.el-input__inner) {
    height: 36px;
    color: var(--el-text-color-primary);
  }
}
.searchInput {
  font-size: 18px;
  height: 50px;
  :deep(.el-input__wrapper) {
    background-color: var(--ba-bg-color-overlay);
    backdrop-filter: blur(30px);
    box-shadow: rgba(142, 142, 142, 0.19) 0px 6px 15px 0px;
    color: rgb(255, 255, 255);
  }
}
.active {
  background-color: #8f97fb !important;
  color: #fff !important;
}
.filter {
  padding: 0px 8px 0 8px;
}
.filterItem {
  display: flex;
  align-items: center;
  height: 44px;
  border-radius: 10px;
  margin: 8px 0;
  .filterIcon {
    width: 50px;
    height: 50px;
  }
  &:hover {
    cursor: pointer;
    background-color: var(--el-fill-color-lighter);
  }
}

.dark {
  .searchInput {
    :deep(.el-input__wrapper) {
      backdrop-filter: blur(30px);
      box-shadow: rgba(14, 14, 14, 0.19) 0px 6px 15px 0px;
      color: rgb(128, 128, 128);
    }
  }
}
</style>
