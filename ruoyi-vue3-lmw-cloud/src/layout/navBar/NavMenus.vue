<template>
  <div class="nav-menus" :class="configStore.layout.layoutMode">
    <router-link class="h100" target="_blank" title="首页" to="/">
      <div class="nav-menu-item pb1">
        <el-icon
          :color="configStore.getColorVal('headerBarTabColor')"
          size="18"
          class="nav-menu-icon icon"
        >
          <Monitor></Monitor>
        </el-icon>
      </div>
    </router-link>
    <div @click="handleSearch" class="nav-menu-item">
      <SvgIcon
        :color="configStore.getColorVal('headerBarTabColor')"
        class="nav-menu-icon icon"
        icon-class="el-icon-Search"
        size="18"
      />
    </div>
    <div
      @click="onFullScreen"
      class="nav-menu-item"
      :class="state.isFullScreen ? 'hover' : ''"
    >
      <svg-icon
        :color="configStore.getColorVal('headerBarTabColor')"
        class="nav-menu-icon icon"
        icon-class="fullScreenCancel"
        size="18"
        v-if="state.isFullScreen"
      />

      <el-icon
        v-else
        :color="configStore.getColorVal('headerBarTabColor')"
        size="18"
        class="nav-menu-icon icon"
      >
        <FullScreen></FullScreen>
      </el-icon>
    </div>
    <el-dropdown
      @visible-change="onCurrentNavMenu($event, 'clear')"
      class="h100"
      size="large"
      :hide-timeout="50"
      placement="bottom"
      trigger="click"
      :hide-on-click="true"
    >
      <div
        class="nav-menu-item"
        :class="state.currentNavMenu == 'clear' ? 'hover' : ''"
      >
        <el-icon
          :color="configStore.getColorVal('headerBarTabColor')"
          size="18"
          class="nav-menu-icon icon"
        >
          <Delete></Delete>
        </el-icon>
      </div>
      <template #dropdown>
        <el-dropdown-menu class="dropdown-menu-box">
          <el-dropdown-item @click="onClearCache('tp')">
            清除系统缓存
          </el-dropdown-item>
          <el-dropdown-item @click="onClearCache('storage')">
            清除浏览器缓存
          </el-dropdown-item>
          <el-dropdown-item @click="onClearCache('all')" divided>
            一键清理所有
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
    <el-popover
      @show="onCurrentNavMenu(true, 'adminInfo')"
      @hide="onCurrentNavMenu(false, 'adminInfo')"
      placement="bottom-end"
      :hide-after="0"
      :width="260"
      trigger="click"
      popper-class="admin-info-box"
      v-model:visible="state.showAdminInfoPopover"
    >
      <template #reference>
        <div
          class="admin-info"
          :class="state.currentNavMenu == 'adminInfo' ? 'hover' : ''"
        >
          <el-avatar :size="25" fit="fill">
            <img :src="fullUrl(adminInfo.avatar)" alt="" />
          </el-avatar>
          <div class="admin-name">{{ adminInfo.nickName }}</div>
        </div>
      </template>
      <div>
        <div class="admin-info-base">
          <el-avatar :size="70" fit="fill">
            <img :src="fullUrl(adminInfo.avatar)" alt="" />
          </el-avatar>
          <div class="admin-info-other">
            <div class="admin-info-name">{{ adminInfo.nickName }}</div>
            <div class="admin-info-lasttime">
              {{ adminInfo.loginTime }}
            </div>
          </div>
        </div>
        <div class="admin-info-footer">
          <el-button @click="onAdminInfo" type="primary" plain>
            个人资料
          </el-button>
          <el-button @click="onLogout" type="danger" plain>退出登录</el-button>
        </div>
      </div>
    </el-popover>
    <div
      @click="configStore.setLayout('showDrawer', true)"
      class="nav-menu-item"
    >
      <SvgIcon
        :color="configStore.getColorVal('headerBarTabColor')"
        class="nav-menu-icon icon"
        iconClass="cogs"
        size="20"
      />
    </div>
    <Config />
    <GlobalSearch v-model:visible="searchVisable"></GlobalSearch>
  </div>
</template>

<script setup>
import { ElMessageBox } from 'element-plus'
import { useConfig } from '@/store/modules/layout.js'
import screenfull from 'screenfull'
import Avatar from '@/assets/images/avatar.png'
import Session from '@/utils/hsj/useSession'
import Storage from '@/utils/hsj/useStorage'
import { getToken, setToken } from '@/utils/auth'
import useUsers from '@/store/modules/user'
import Config from '../components/Config/index.vue'
import GlobalSearch from '../components/GlobalSearch/index.vue'
const proxy = inject('proxy')
const configStore = useConfig()
const router = useRouter()
const state = reactive({
  isFullScreen: false,
  currentNavMenu: '',
  showLayoutDrawer: false,
  showAdminInfoPopover: false,
})
const adminInfo = useUsers()

const fullUrl = () => {
  return adminInfo.avatar || Avatar
}

const onFullScreen = () => {
  if (!screenfull.isEnabled) {
    proxy.$modal.notifyWarning('禁止全屏')
    return false
  }
  screenfull.toggle()
  screenfull.onchange(() => {
    state.isFullScreen = screenfull.isFullscreen
  })
}
const searchVisable = ref(false)
const handleSearch = () => {
  searchVisable.value = true
}

const onCurrentNavMenu = (status, name) => {
  state.currentNavMenu = status ? name : ''
}

const onClearCache = (type) => {
  if (type == 'storage' || type == 'all') {
    const token = getToken()
    Session.clear()
    Storage.clear()
    setToken(token)
    if (type == 'storage') return
  }
}

const onLogout = () => {
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      adminInfo.logOut().then(() => {
        proxy.$tab.closeAllPage().then(() => {
          location.href = '/index'
        })
      })
    })
    .catch(() => {})
}

const onAdminInfo = () => {
  state.showAdminInfoPopover = false
  router.push({
    path: '/user/profile',
  })
}
</script>

<style scoped lang="scss">
.nav-menus.Default {
  border-radius: var(--el-border-radius-base);
  box-shadow: var(--el-box-shadow-light);
}
.nav-menus {
  display: flex;
  align-items: center;
  height: 100%;
  margin-left: auto;
  background-color: v-bind('configStore.getColorVal("headerBarBackground")');
  .nav-menu-item {
    height: 100%;
    width: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    .nav-menu-icon {
      box-sizing: content-box;
      color: v-bind('configStore.getColorVal("headerBarTabColor")');
    }
    &:hover {
      .icon {
        animation: twinkle 0.3s ease-in-out;
      }
    }
  }
  .admin-info {
    display: flex;
    height: 100%;
    padding: 0 10px;
    align-items: center;
    cursor: pointer;
    user-select: none;
    color: v-bind('configStore.getColorVal("headerBarTabColor")');
  }
  .admin-name {
    padding-left: 6px;
    white-space: nowrap;
  }
  .nav-menu-item:hover,
  .admin-info:hover,
  .nav-menu-item.hover,
  .admin-info.hover {
    background: v-bind('configStore.getColorVal("headerBarHoverBackground")');
  }
}
.dropdown-menu-box :deep(.el-dropdown-menu__item) {
  justify-content: center;
}
.admin-info-base {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  padding-top: 10px;
  .admin-info-other {
    display: block;
    width: 100%;
    text-align: center;
    padding: 10px 0;
    .admin-info-name {
      font-size: var(--el-font-size-large);
    }
  }
}
.admin-info-footer {
  padding: 10px 0;
  margin: 0 -12px -12px -12px;
  display: flex;
  justify-content: space-around;
}
.pt2 {
  padding-top: 2px;
}

@keyframes twinkle {
  0% {
    transform: scale(0);
  }
  80% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}
</style>
