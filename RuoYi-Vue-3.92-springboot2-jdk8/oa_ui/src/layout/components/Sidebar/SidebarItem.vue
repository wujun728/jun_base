<template>
  <div v-if="!item.hidden">
    <template
      v-if="
        hasOneShowingChild(item.children, item) &&
        (!onlyOneChild.children || onlyOneChild.noShowingChildren) &&
        !item.alwaysShow
      "
    >
      <app-link v-if="onlyOneChild" :to="resolvePath(onlyOneChild.menu_path, 0)">
        <el-menu-item
          :index="resolvePath(onlyOneChild.menu_path, 0)"
          :router="true"
          :class="{ 'submenu-title-noDropdown': !isNest }"
      
          v-show="item.is_show"
         
        >
   
          <i class="iconfont" v-if="onlyOneChild.icon" :class="onlyOneChild.icon"></i>
          <span slot="title" class="overText" >{{ item.menu_name }}</span>
        </el-menu-item>
      </app-link>
    </template>

    <el-submenu v-else ref="subMenu" :index="resolvePath(item.menu_path, 1)" popper-append-to-body>
      <template slot="title">
     
        <i v-if="item.icon" class="iconfont" :class="item.icon"></i>
        <span >{{ item.menu_name }}</span>
      </template>

      <sidebar-item
        v-for="(child, index) in item.children"
        :key="'sidebars' + index"
        :is-nest="true"
        :item="child"
        :base-path="includeRouteOrNot(child.menu_path)"
        v-show="child.is_show"
        class="nest-menu"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from 'path'
import { isExternal } from '@/utils/validate'
import FixiOSBug from './FixiOSBug'

export default {
  name: 'SidebarItem',
  components: { Item: () => import('./Item'), AppLink: () => import('./Link') },
  mixins: [FixiOSBug],
  computed: {
    includeRouteOrNot() {
      return function (menuPath) {
        const { path } = this.$route
        if (path.startsWith(menuPath)) {
          return path
        }
        return menuPath
      }
    }
  },
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    // To fix https://github.com/PanJiaChen/vue-admin-template/issues/237
    // TODO: refactor with render function
    this.onlyOneChild = null
    return {}
  },
  methods: {
    hasOneShowingChild(children = [], parent) {
      let len = 0
      const showingChildren = children.filter((item) => {
        if (item.position === 1) {
          len++
        }
        if (item.hidden) {
          return false
        } else {
          // 测试
          // Temp set(will be used if only has one showing child)
          this.onlyOneChild = item
          return true
        }
      })

      if (showingChildren.length === 0) {
        this.onlyOneChild = { ...parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    },
    keyClass(item) {
      if (item.top_position && item.top_position.length > 0) {
        if (this.$route.path.indexOf(item.menu_path)) {
          return 'router-link-exact-active'
        }
      }
    },

    resolvePath(routePath, index) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      if (index == 1) {
        return path.resolve(this.basePath, routePath)
      } else {
        return this.basePath
      }
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-submenu__icon-arrow {
  position: absolute;
  right: 32px;
  top: 55%;
}
.iconfont {
  font-size: 20px;
}
.overText {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;

}
</style>
