<template>
  <!--  <div class="sidebar-container" v-if="isShowMenu"></div>-->
  <div class="nav-bar" v-if="isShowMenu && menuList[sidebarParentCur] && menuList[sidebarParentCur].children" :style="{ width: !isCollapse ? '156px !important' : '64px !important' }">
    <div v-if="isShow" class="child-bar" :class="!isCollapse ? 'plr12' : ''">
      <div
        class="over-text1"
        v-if="menuList[sidebarParentCur.length > 13 ? sidebarParentCur.slice(13) : sidebarParentCur]"
        :class="!isCollapse ? 'title-box-open' : 'title-box-close'"
      >
        {{ menuList[sidebarParentCur.length > 13 ? sidebarParentCur.slice(13) : sidebarParentCur].menu_name }}
      </div>
      <el-scrollbar style="height: 100%;" wrap-class="scrollbar-wrapper">
        <!-- <div class="scroll-box"> -->
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :background-color="variables.menuBg"
          :text-color="variables.menuText"
          :unique-opened="true"
          :active-text-color="variables.menuActiveText"
          :collapse-transition="false"
          :router="true"
          mode="vertical"
          @select="selectMenu"
        >
          <template v-if="menuList[sidebarParentCur.length > 13 ? sidebarParentCur.slice(13) : sidebarParentCur]">
            <sidebar-item
              v-for="route in menuList[sidebarParentCur.length > 13 ? sidebarParentCur.slice(13) : sidebarParentCur]
                .children"
              :key="'sidebar' + route.id"
              :item="route"
              :base-path="includeRouteOrNot(route.menu_path)"
              @click.native="routerClick(route)"
            />
          </template>
        </el-menu>
        <!-- </div> -->
      </el-scrollbar>
    </div>
    <div class="nav-open" @click="clickOutside">
      <i class="el-icon-arrow-left" :class="isCollapse ? 'active' : ''"></i>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import variables from '@/styles/variables.scss'
export default {
  components: { SidebarItem: () => import('./SidebarItem'), Logo: () => import('./Logo') },
  data() {
    return {
      parentCur: 0,
      childcur: 0,
      isShow: false,
      activePath: '',
      menuName: ''
    }
  },
  computed: {
    ...mapGetters([
      'permission_routes',
      'sidebar',
      'menuList',
      'sidebarType',
      'parentMenuId',
      'defaultOpen',
      'isClickTab',
      'menuStatus'
    ]),
    sidebarParentCur: {
      get() {
        return this.$store.state.app.sidebarParentCur
      },
      set() {}
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route

      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    isShowMenu() {
      let index = this.sidebarParentCur
      if (index.length > 13) {
        const index_ = JSON.stringify(index)
        const indexClone = JSON.parse(index_)
        index = indexClone.slice(13)
      }
      // 二级菜单只有一个，那就用is_show==1来判断显示隐藏
      if (this.menuList && this.menuList[index] && this.menuList[index].children && this.menuList[index].children.length > 1) {
        return this.menuList[index].children.length > 0
      } else {
        if (this.menuList && this.menuList[index] && this.menuList[index].children && this.menuList[index].children[0].is_show === 0) {
          return false
        } else {
          return true
        }
      }
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    },
    includeRouteOrNot() {
      return function (menuPath) {
        const { path } = this.$route
        if (this.activeMenu.startsWith(menuPath)) {
          return path
        }
        return menuPath
      }
    }
  },
  watch: {
    sidebarParentCur: {
      handler(nVal, oVal) {
        let nVals = nVal
        if (nVal.length > 13) {
          nVals = nVal.slice(13)
        }
        this.isShow = true
        if (this.isClickTab) {
          if (
            this.menuList[nVals].children &&
            this.menuList[nVals].children[0].children !== undefined &&
            this.menuList[nVals].children[0].children.length
          ) {
            this.$store.commit('app/SET_CLICK_TAB', false)
            this.$router.push({
              path: this.menuList[nVals].children[0].children[0].menu_path
            })
          } else {
            this.$store.commit('app/SET_CLICK_TAB', false)
            this.$router.push({
              path:  this.menuList[nVals].children?this.menuList[nVals].children[0].menu_path:''
            })
          }
        }
        this.sidebarParentCur = nVals
      },
      deep: true
    }
  },
  created() {},
  mounted() {
    this.$nextTick(() => {
      if (this.sidebarParentCur >= 0) this.isShow = true
      this.menuList.forEach((nav, i) => {
        if (nav.id === this.parentMenuId) {
          this.parentCur = i
        }
      })

      if (this.menuList[this.parentCur].children) {
        this.$store.commit('app/SET_BARTYPE', true)
      } else {
        this.$store.commit('app/SET_BARTYPE', false)
      }
    })
  },
  methods: {
    ...mapMutations('user', ['SET_MENU_LIST']),
    // 父级导航点击
    handelParentClick(item, index) {
      this.parentCur = index
      this.$store.commit('app/SETPID', item.id)
      const childUrl = ''
      let chiidLink = ''
      if (item.children) {
        if (item.children[0].children) {
          var recursiveFunction = function () {
            const getStr = function (list) {
              list.children.forEach(function (row, index) {
                if (row.children) {
                  getStr(row)
                } else {
                  if (index == 0) {
                    return (chiidLink = row.menu_path)
                  }
                }
              })
            }
            getStr(item.children[0])
          }
          recursiveFunction()
        } else {
          chiidLink = item.children[0].menu_path
        }

        this.childcur = 0
        this.$router.push({
          path: chiidLink
        })
        this.$store.commit('app/SET_BARTYPE', true)
      } else {
        this.$router.push({
          path: item.menu_path
        })
        this.$store.commit('app/SET_BARTYPE', false)
      }
    },
    routerClick(item) {
      if (item.children) {
        item.children.forEach((el) => {
          if (el.top_position && el.top_position.length > 0) {
            if (this.activePath == el.menu_path) {
              this.$store.commit('app/SETPID', el.pid)
            }
          }
        })
      } else {
        this.$store.commit('app/SETPID', item.pid)
      }
    },
    selectMenu(index) {
      this.activePath = index
    },
    clickOutside() {
      if (this.isCollapse) {
        this.$store.dispatch('app/openSideBar')
      } else {
        this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.nav-bar {
  width: 70px;
  display: flex;
  position: relative;
  height: 100%;
  overflow: hidden;

  .nav-open {
    width: 10px;
    height: 35px;
    background: rgba(0, 0, 0, 0.08);
    cursor: pointer;
    position: absolute;
    right: 0;
    top: 50%;
    transform: translate(0, -50%);
    z-index: 9;
    border-radius: 4px 0 0 4px;
    display: flex;
    align-items: center;
    i {
      color: #606266;
      font-size: 13px;
      transition: all 0.5s;
      transform-origin: center;
      &.active {
        transform: rotate(-180deg);
      }
    }
  }
}

.scroll-box {
  height: calc(100vh - 50px);
  overflow-y: auto;
  padding-bottom: 20px;
}

.scroll-box::-webkit-scrollbar {
  height: 0;
  width: 0;
}

.parent-bar {
  width: 100%;
  height: 100%;
  background: #001529;
  .logo {
    padding: 33px 0;
    text-align: center;
    img {
      width: 41px;
      height: 41px;
      border-radius: 50%;
    }
  }
  .parent-nav-item {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    width: 100%;
    margin: 10px 0;
    padding: 10px 0;
    color: #fff;
    font-size: 14px;
    cursor: pointer;
    .icon {
      margin-bottom: 5px;
      font-size: 18px;
    }
    &.on {
      background: #1890ff;
    }
  }
}
::v-deep .el-menu--popup {
  min-width: 114px;
}
.child-bar {
  width: 100%;
  height: 100%;
  color: #000;
  &.plr12 {
    // padding: 0 12px;
  }
  font-size: 13px;
  ::v-deep .el-menu {
    border-right: none;
  }
  ::v-deep .el-menu-item {
    font-size: 14px !important;
    display: flex;
    align-items: center;
  }
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  ::v-deep .router-link-exact-active.router-link-active li {
    background-color: #f1f9ff !important;
    color: #1890ff !important;
    border-radius: 4px;
  }
  ::v-deep .router-link-exact-active.router-link-active li i {
    color: #1890ff !important;
  }
  ::v-deep .el-submenu__title {
    font-size: 14px;
  }
  ::v-deep .el-menu .el-submenu .el-submenu__title > i {
    width: auto !important;
  }

  ::v-deep .el-submenu.is-active > .el-submenu__title {
    color: #1890ff !important;
  }
  ::v-deep .el-submenu.is-active > .el-submenu__title > i {
    color: #1890ff !important;
  }

  .title-box-open {
    padding: 15px 15px 15px 23px;
    font-size: 18px;
    font-weight: 800;
  }
  .title-box-close {
    padding: 15px 15px 15px 20px;
    font-size: 13px;
    font-weight: 800;
  }
  .child-nav-item {
    .txt {
      position: relative;
      display: flex;
      align-items: center;
      padding-left: 42px;
      height: 36px;
      font-size: 14px;
      cursor: pointer;
      &.on {
        background: #f0f2f5;
      }
    }
    .link {
      position: relative;
      height: 36px;
      line-height: 36px;
      color: #000 !important;
      font-size: 14px;
      padding-left: 42px;
      &:hover {
        color: #2d8cf0 !important;
      }
      &.router-link-active {
        background: #f0f2f5;
      }
    }
    .icon {
      position: absolute;
      left: 18px;
      top: 50%;
      transform: translateY(-50%);
      margin-right: 10px;
      font-size: 16px;
    }
  }
}
.small-txt {
  font-size: 14px !important;
}
.icon {
  font-size: 18px;
}
</style>
