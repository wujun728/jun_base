<template>
  <div>
    <div :class="classObj" style="box-sizing: border-box;">
      <!-- 左侧一级菜单 -->
      <navbar :type="type" />
      {{ sideShow }}
      <!-- 左侧二级菜单 -->
      <sidebar class="sidebar-container" v-show="!sideShow" />
      <!-- 中间区域 -->
      <div :class="['main-container', { hasTagsView: needTagsView }, helpShow ? 'left' : '']">
        <!-- 顶部工具栏 -->
        <headerToolbar :helpIsShow="helpShow" ref="header" :searchShow="searchShow" @handleSearchConfirm="handleConfirm"  @openOrCloseHelp="openHelp"></headerToolbar>
        <!-- 标签栏目 -->
        <tags-view class="tags-view-container" v-show="needTagsView"></tags-view>
        <!-- 中间内容区域 -->
        <app-main />
      </div>
      <!-- 帮助侧边栏 -->
      <help v-show="helpShow" :helpShow="helpShow" @closeHelp="closeHelp"></help>
    </div>
    <!-- 消息组建 -->
   <message-handle-popup ref="messageHandlePopup" :detail="detail"></message-handle-popup>
  </div>
</template>

<script>
import { AppMain, Navbar, Sidebar, TagsView } from './components'
import ResizeMixin from './mixin/ResizeHandler'
import { roterPre } from '@/settings'
import { mapGetters, mapState } from 'vuex'
import { EventBus } from '@/libs/bus'
export default {
  name: 'Layout',
  components: {
    AppMain,
    Navbar,
    Sidebar,
    TagsView,
    headerToolbar: () => import('./components/Header/Toolbar'),
    // breadcrumb: () => import('./components/Header/breadcrumb'),
    help: () => import('./components/Document/help'),
    messageHandlePopup: () => import('@/components/common/messageHandlePopup')
  },
  mixins: [ResizeMixin],
  computed: {
    ...mapState({
      sidebar: (state) => state.app.sidebar,
      word: (state) => state.app.keyWord,
      device: (state) => state.app.device,
      showSettings: (state) => state.settings.showSettings,
      needTagsView: (state) => state.settings.tagsView,
      fixedHeader: (state) => state.settings.fixedHeader,
      sidebarType: (state) => state.app.sidebarType
    }),
    ...mapGetters(['menuList', 'sidebarParentCur']),
    classObj() {
      const sidebarData = this.menuList[this.sidebarParentCur]

      // 二级菜单只有一个，那就用is_show==0来判断显示隐藏
      if (sidebarData) {
        if (sidebarData.children && sidebarData.children.length > 1) {
          this.isSidebarShow = sidebarData.children && sidebarData.children.length > 0
        } else {
          if (sidebarData.children && sidebarData.children[0].is_show == 0) {
            this.isSidebarShow = false
          } else if(!sidebarData.children){
            this.isSidebarShow = false
          } else {
            this.isSidebarShow = true
          }
        }
      }

      return {
        hideSidebar: !this.sidebar.opened && this.isSidebarShow,
        openSidebar: this.sidebar.opened && this.isSidebarShow,
        showSidebar: !this.isSidebarShow
      }
    }
  },

  watch: {
    $route(to, from) {
      let data = {
        list: [],
        type: '',
        additional_search_boolean: '1'
      }
      this.$store.commit('uadatefieldOptions', data)
      this.$store.state.business.conditionDialog = false
      if (to.path == `${roterPre}/user/resume` || to.path == `${roterPre}/user/news/subscribe`) {
        this.sideShow = true
        this.helpShow = false
      } else {
        this.sideShow = false
      }
    },

    sidebarType: {
      handler(nVal, oVal) {
        if (nVal) {
          this.widthNum = 'calc(100% - 270px)'
        } else {
          this.widthNum = 'calc(100% - 70px)'
        }
      },
      deep: true
    },
    word: {
      handler(nVal, oVal) {
        if (nVal) {
          this.keyword = nVal
        }
      },
      deep: true
    }
  },
  data() {
    return {
      widthNum: 'calc(100% - 70px)',
      isSidebarShow: true,
      helpShow: false,
      searchShow: false,
      type: 2,
      keyword: '',
      detail: {},
      sideShow: false
    }
  },
  created() {
    EventBus.$on('messageSuccess', async (e) => {
      this.detail = e.detail
      await this.$nextTick()
      this.$refs.messageHandlePopup.openMessage(e.item)
    })
    if (
      this.$route.fullPath == `${roterPre}/user/resume` ||
      this.$route.fullPath == `${roterPre}/user/news/subscribe`
    ) {
      this.sideShow = true
    } else {
      this.sideShow = false
    }
  },
  mounted() {
    this.widthNum = this.sidebarType ? 'calc(100% - 152px)' : 'calc(100% - 70px)'
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    },
    openHelp(isshow) {
      this.helpShow = isshow
    },
    closeHelp() {
      this.helpShow = false
      this.$refs.header.helpShow = false
    },
    handleConfirm(keyword) {
      this.$router.push({
        path: `${roterPre}/search`,
        query: { keyword: keyword }
      })

      this.searchShow = false
    }
  }
}
</script>

<style lang="scss" scoped>
@import '~@/styles/mixin.scss';
@import '~@/styles/variables.scss';

.flex {
  display: flex;
  align-items: center;
}

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}


.nav-wrapper {
  position: relative;
  z-index: 700;
  width: 100%;
  transition: width 0.28s;
}

//二级菜单展开
.openSidebar {
  .breadcrumb {
    padding-left: 250px !important;
  }
  .main-container {
    margin-left: 219px !important;
  }
}
//二级菜单缩进
.hideSidebar {
  .breadcrumb {
    padding-left: 150px !important;
  }
  .main-container {
    margin-left: 128px !important;
  }
}
//二级菜单隐藏
.showSidebar {
  .breadcrumb {
    padding-left: 100px !important;
  }
  .main-container {
    margin-left: 64px !important;
  }
}

@keyframes fadenum {
  0% {
    margin-right: 0px;
  }
  100% {
    margin-right: 360px;
  }
}
.left {
  animation: fadenum 0.5s;
  animation-fill-mode: forwards;
}
.el-icon-search {
  color: #909399;
}
.top {
  ::v-deep.el-button--text {
    color: #909399;
  }
}
</style>
