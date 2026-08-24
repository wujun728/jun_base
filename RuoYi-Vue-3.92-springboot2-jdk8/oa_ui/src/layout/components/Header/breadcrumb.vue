<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item v-for="(item, index) in levaList" :key="index"> {{ item }}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script>
import { roterPre } from '@/settings'
export default {
  name: 'Breadcrumb',
  data() {
    return {
      levaList: [],
      path: ''
    }
  },
  watch: {
    $route(to, from) {
      this.getBreadcrumb()
    }
  },
  mounted() {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb() {
      let newPath = this.$route.fullPath
      newPath = newPath.split('?')[0]
      // 获取缓存菜单
      let routerInfo = this.$store.getters.menuList
      if (newPath == `${roterPre}/search`) {
        this.levaList = ['搜索']
      } else {
        if (routerInfo) {
          this.find(routerInfo, newPath)
        }
      }
    },
    find(array, path) {
      let stack = []
      let going = true
      let walker = (array, path) => {
        array.forEach((item) => {
          if (!going) return
          stack.push(item['menu_name'])
          if (item['menu_path'] === path) {
            going = false
          } else if (item['children']) {
            walker(item['children'], path)
          } else {
            stack.pop()
          }
        })
        if (going) stack.pop()
      }
      walker(array, path)

      this.levaList = stack
      if (this.levaList.length <= 2) {
        localStorage.setItem('navTitle', JSON.stringify(this.levaList))
      } else {
        localStorage.setItem('navTitle', JSON.stringify(this.levaList))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-breadcrumb__item:last-child .el-breadcrumb__inner {
  color: #303133;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  font-size: 14px;
}
::v-deep .el-breadcrumb__inner {
  font-size: 14px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
}
</style>
