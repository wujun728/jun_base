<script setup>
import usePermissionStore from '@/store/modules/permission'
import { RoutesAlias } from '@/router/routesAlias'

const route = useRoute()
const router = useRouter();
const levelList = ref([])
const permissionStore = usePermissionStore()

const sidebarRouters = computed(() => permissionStore.sidebarRouters);
function getTitle(item) {
  return typeof item.meta.title === 'function' ? item.meta.title(route) : item.meta.title
}
function findPathNum(str, char = "/") {
  let index = str.indexOf(char)
  let num = 0
  while (index !== -1) {
    num++
    index = str.indexOf(char, index + 1)
  }
  return num
}
function getMatched(pathList, routeList, matched) {
  if (!Array.isArray(routeList) || routeList.length === 0) return
  const data = routeList.find((item) => item.path == pathList[0])
  if (!data) return
  matched.push(data)
  if (data.children && pathList.length) {
    pathList.shift()
    getMatched(pathList, data.children, matched)
  }
}
function getBreadcrumb() {
  // only show routes with meta.title
  let matched = [];
  const pathNum = findPathNum(route.path)
  if (pathNum > 2) {
    const reg = /\/\w+/gi
    const pathList = route.path.match(reg).map((item, index) => {
      if (index !== 0) item = item.slice(1)
      return item
    })
    getMatched(pathList, sidebarRouters.value, matched)
  } else {
    matched = route.matched.filter((item) => item.meta && item.meta.title)
  }
  // 判断是否为首页
  if (!isDashboard(matched[0])) {
    matched = [{ path: RoutesAlias.Home, meta: { title: '首页' } }].concat(matched)
  }
  levelList.value = matched.filter((item) => item.meta && item.meta.title && item.meta.breadcrumb !== false)
}
function isDashboard(route) {
  const name = route && route.name
  if (!name) {
    return false
  }
  return name.trim() === 'Index'
}
function handleLink(item) {
  const { redirect, path } = item
  if (redirect) {
    router.push(redirect)
    return
  }
  router.push(path)
}

watchEffect(() => {
  // if you go to the redirect page, do not update the breadcrumbs
  if (route.path.startsWith('/redirect/')) {
    return
  }
  getBreadcrumb()
})
getBreadcrumb();
</script>
<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
        <span v-if="item.redirect === 'noRedirect' || index == levelList.length - 1" class="no-redirect">
          {{ getTitle(item) }}
        </span>
        <a v-else @click.prevent="handleLink(item)">{{ getTitle(item) }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>
<style lang='scss' scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}
</style>