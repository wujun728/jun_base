<template>
  <div class="navbar-news">
    <div class="navbar">
      <div v-if="webConfig" class="logo-wrapper">
        <img :src="webConfig.site_logo" alt="" />
      </div>
      <div ref="leftBox" class="menu-box">
        <el-scrollbar style="height: 100%;">
          <template v-if="showMore">
          
            <div class="nav-box">
              <template v-for="(item, index) in menuList">
                <div
                  v-if="item.is_show != 0 && item.menu_path !== settingMenuUniquePath"
                  :key="index"
                  :class="{ on: parentcur === index }"
                  class="nav-item"
                  @click="handleParentCur(index, 1,item)"
                >
                  <div class="nav-items">
                    <i :class="getIconCss(item.icon)"></i>
                    <span> {{ item.menu_name }} </span>
                  </div>
                </div>
              </template>
            </div>
          </template>
          <template v-else>
            <div class="nav-box">
              <template v-for="(item, index) in menuList.slice(0, showMax)">
                <div
                  v-if="item.is_show != 0 && item.menu_path !== settingMenuUniquePath"
                  :key="index"
                  :class="{ on: parentcur === index }"
                  class="nav-item"
                  @click="handleParentCur(index, 1,item)"
                >
                  <div class="nav-items">
                    <i :class="item.icon" class="iconfont"></i>
                    <span> {{ item.menu_name }}</span>
                  </div>
                </div>
            </template>
              <el-popover v-if="newMenuListArr.length - 1 > showMax" placement="right" trigger="hover" width="176">
                <div class="popover-content">
                  <template v-for="(item, index) in menuList.slice(showMax, menuList.length)">
                    <div
                      v-if="item.is_show !== 0 && item.menu_path !== settingMenuUniquePath"
                      :key="'pop' + index"
                      class="nav-item"
                      @click="handleParentCur(index, 2, item)"
                    >
                      <div class="nav-items">
                        <i :class="getIconCss(item.icon)"></i>
                        <span> {{ item.menu_name }}</span>
                      </div>
                    </div>
                  </template>
                </div>
                <div slot="reference" class="nav-item popover-nav-item">
                  <div class="nav-items">
                    <i class="iconfont icongenjinjilu-gengduo"></i>
                    <span>更多</span>
                  </div>
                </div>
              </el-popover>
            </div>
          </template>
        </el-scrollbar>
      </div>
      <div class="below-bar" v-if="settingMenuInfo && settingMenuInfo.is_show !== 0">
        <el-tooltip
          :content="settingMenuInfo.menu_name"
          placement="right"
        >
          <div
            class="nav-item"
            @click="handleSettingMenuClick"
            :class="{ on: parentcur == settingMenuIndex }"
          >
            <i :class="getIconCss(settingMenuInfo.icon)"></i>
            <!-- <i :class="settingMenuInfo.icon" class="iconfont"></i> -->
          </div>
        </el-tooltip>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { appConfigApi } from '@/api/public'
import { roterPre } from '@/settings'
import Cookies from 'js-cookie'
import { generateTitle } from '@/utils/i18ns'
import defaultSettings from '@/settings'

const settingMenuUniquePath = '/admin/setting';

export default {
  props: ['type'],
  computed: {
    ...mapGetters(['sidebar', 'avatar', 'device', 'sidebarParentCur', 'menuList', 'enterprise']),
    settingMenuIndex() {
      return this.menuList.findIndex(menu => menu.menu_path === settingMenuUniquePath)
    },
    settingMenuInfo() {
      if (this.settingMenuIndex === -1) return;
      return this.menuList[this.settingMenuIndex];
    },
    newMenuListArr() {
      return this.menuList.filter((val) => val.is_show !== 0)
    },
    activeMenus() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    }
  },
  data() {
    return {
      settingMenuUniquePath,
      roterPre: roterPre,
      adminInfo: Cookies.set('AdminName'),
      levelList: null,
      parentcur: this.sidebarParentCur,
      webConfig: null,
      showMax: 5,
      newMenuList: [],
      showMore: true,
    }
  },

  watch: {
    $route(route) {
      // if you go to the redirect page, do not update the breadcrumbs
      if (route.path.startsWith('/redirect/')) {
        return
      }
      this.getBreadcrumb()
    }
  },
  created() {
    appConfigApi().then((res) => {
      this.webConfig = res.data
      localStorage.setItem('isWebConfig', res.data.global_watermark_status)
      localStorage.setItem('webConfig', JSON.stringify(res.data))
      // 加入企业
      if (res.data.enterprise_name) {
        defaultSettings.title = res.data.enterprise_name
        document.title = res.data.enterprise_name
      }
    })
    this.getShowMore = this.getShowMore.bind(this)
  },
  mounted() {
    this.getBreadcrumb()
    this.getShowMore()
    window.addEventListener('resize',  this.getShowMore)
  },
  destroyed() {
    window.removeEventListener('resize', this.getShowMore)
  },
  methods: {
    getIconCss(cssName) {
      if (/^el-icon-/.test(cssName)) {
        return [cssName]
      } else {
        return ['iconfont', cssName]
      }
    },
    getShowMore() {
      const navHeight = 60
      const height = this.$refs.leftBox.clientHeight
      const len = this.newMenuListArr.length
      this.showMore = navHeight * len <= height
      this.showMax = Math.floor(height / navHeight) - 1
    },
    generateTitle,
    getBreadcrumb() {
      // only show routes with meta.title
      const matched = this.$route.matched.filter((item) => item.meta && item.meta.title)
      this.levelList = matched.filter((item) => item.meta && item.meta.title && item.meta.breadcrumb !== false)
      if (this.activeMenus === `${roterPre}/dashboard`) {
        return this.$store.commit('app/SET_PARENTCUR', 2)
      }
      this.filterArr(this.menuList, this.activeMenus)
    },
    handleSettingMenuClick() {
      this.handleParentCur(this.settingMenuIndex, 1, this.settingMenuInfo);
    },
    // 父级菜单切换
    handleParentCur(index, type, row) {
      if (type === 1) {
        this.parentcur = index
        this.$store.commit('app/SET_CLICK_TAB', true)
        this.$store.commit('app/SET_PARENTCUR', this.parentcur)
        if(!row.children){
          this.$router.push({
            path: row.menu_path
          })
        }
      } else {
        this.parentcur = new Date().getTime() + `${this.showMax - 1}`
        const len4 = this.menuList[this.showMax - 1]
        this.menuList[this.showMax - 1] = row
        this.menuList[this.showMax + index] = len4
        this.$store.commit('user/SET_MENU_LIST', this.menuList)
        this.$store.commit('app/SET_CLICK_TAB', true)
        this.$store.commit('app/SET_PARENTCUR', this.parentcur)
        if(!row.children){
          this.$router.push({
          path: row.menu_path
        })
      
      }
      }
    },
    filterArr(arr, url) {
      const self = this
      var recursiveFunction = function (url) {
        const getStr = function (arr) {
          arr.forEach(function (row) {
            if (row.children) {
              getStr(row.children)
} else {
              if (row.menu_path === url) {
                // OA 菜单用 path[0] 存父级 id，若依转型菜单用 pid
                const pid = row.path && row.path.length ? row.path[0] : row.pid
                if (pid) {
                  self.getParentIndex(pid)
                }
              }
            }
          })
        }
        getStr(arr)
      }
      recursiveFunction(url)
    },
    // 设置父级下标
    getParentIndex(pid) {
      this.menuList.forEach((item, index) => {
        if (item.id === pid) {
          this.parentcur = index
          this.$store.commit('app/SET_PARENTCUR', index)
        }
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.fontSize {
  font-size: 14px !important;
}
//::v-deep .user-dropdown {
//  //left: 50px !important;
//}
//::v-deep .divBox {
//  padding-top: 0 !important;
//}
//.popover-user {
//  z-index: 200;
//  margin: -12px;
//}
.navbar {
  z-index: 70;
  overflow: hidden;
  position: fixed;
  left: 0;
  top: 0;
  width: 64px;
  bottom: 0;
  background: #1e84e3;
  box-shadow: 0px 1px 4px 0px rgba(0, 21, 41, 0.12);
  color: #fff;
  min-height: 670px;

  //.right-menu {
  //  float: right;
  //  height: 100%;
  //  line-height: 50px;
  //
  //  &:focus {
  //    outline: none;
  //  }
  //
  //  .right-menu-item {
  //    display: inline-block;
  //    padding: 0 8px;
  //    height: 100%;
  //    font-size: 18px;
  //    color: #5a5e66;
  //    vertical-align: text-bottom;
  //
  //    &.hover-effect {
  //      cursor: pointer;
  //      transition: background 0.3s;
  //
  //      &:hover {
  //        background: rgba(0, 0, 0, 0.025);
  //      }
  //    }
  //  }
  //
  //  //.avatar-container {
  //  //  margin-right: 30px;
  //  //
  //  //  .avatar-wrapper {
  //  //    margin-top: 5px;
  //  //    position: relative;
  //  //
  //  //    .user-avatar {
  //  //      cursor: pointer;
  //  //      width: 40px;
  //  //      height: 40px;
  //  //      border-radius: 10px;
  //  //    }
  //  //
  //  //    .el-icon-caret-bottom {
  //  //      cursor: pointer;
  //  //      position: absolute;
  //  //      right: -20px;
  //  //      top: 25px;
  //  //      font-size: 13px;
  //  //    }
  //  //  }
  //  //}
  //}
}
.below-bar {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  padding: 0 10px 27px;

  .nav-item {
    width: 100%;
    height: 46px;
    background-color: transparent;
    border-radius: 8px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;

    i {
      font-size: 18px;
    }

    &:hover {
      background-color: rgba($color: #000000, $alpha: .1);
    }

    &.on {
      background-color: #fff;
      i {
        color: #1890ff;
      }
    }
  }

}
.menu-box {
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  width: 100%;
  height: calc(100% - 220px - 15px - 87px);
  min-height: 390px;
  .nav-box {
    flex: 1;
    display: flex;
    flex-wrap: wrap;
    .nav-item {
      position: relative;
      cursor: pointer;
      font-size: 13px;
      width: 50px;
      height: 50px;
      margin-left: 7px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 10px;
      color: #fdfdfd;
      &.on {
        background-color: #fff !important;
        border-radius: 8px;
        font-weight: 600;
        color: #1890ff !important;
        i {
          color: #1890ff;
          font-weight: normal;
        }
      }
      &:last-of-type {
        margin-bottom: 0;
      }
      &:hover {
        background-color: rgba(0, 0, 0, 0.1);
        border-radius: 8px;
      }
      .nav-items {
        display: inline;
        text-align: center;
      }
      i,
      span {
        display: block;
        font-size: 14px;
      }
      i {
        font-size: 18px;
        color: #fdfdfd;
        margin-bottom: 4px;
      }
    }
    .popover-nav-item {
      margin-top: 10px;
    }
  }
}

.popover-content {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  .nav-item {
    position: relative;
    cursor: pointer;
    font-size: 13px;
    width: 50px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    .nav-items {
      text-align: center;
      .iconfont {
        font-size: 20px;
        color: #1890ff;
      }
      span {
        display: block;
      }
    }
  }
}
.logo-wrapper {
  margin: 10px 0 22px 7px;
  width: 50px;
  height: 50px;
  img {
    width: 50px;
    height: 50px;
    object-fit: cover;
  }
}
//.down-item {
//  text-align: center;
//  line-height: 40px;
//}


//.drop-config {
//  display: flex;
//  flex-direction: column;
//  padding: 18px 14px 14px 18px;
//}
//.drop-txt {
//  padding: 0 17px;
//  font-size: 14px;
//  color: #606266;
//  &:hover,
//  &:focus {
//    background-color: #e8f4ff;
//    color: #46a6ff;
//  }
//}
//.drop-body {
//  border-bottom: 1px solid #f5f5f5;
//}
//.pop-box {
//  padding: 6px 6px 0;
//  font-size: 14px;
//  color: #000;
//  .pop-item {
//    margin-bottom: 18px;
//    cursor: pointer;
//    &:last-child {
//      margin-bottom: 6px;
//    }
//  }
//}
.active {
  color: #1890ff;
}

</style>

