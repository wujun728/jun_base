<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="appStore.sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <breadcrumb v-if="!settingsStore.topNav" id="breadcrumb-container" class="breadcrumb-container" />
    <top-nav v-if="settingsStore.topNav" id="topmenu-container" class="topmenu-container" />

    <div class="right-menu">
<!--      <template v-if="appStore.device !== 'mobile'">-->
<!--        <header-search id="header-search" class="right-menu-item" />-->


<!--        <screenfull id="screenfull" class="right-menu-item hover-effect" />-->

<!--        <el-tooltip content="主题模式" effect="dark" placement="bottom">-->
<!--          <div class="right-menu-item hover-effect theme-switch-wrapper" @click="toggleTheme">-->
<!--            <svg-icon v-if="settingsStore.isDark" icon-class="sunny" />-->
<!--            <svg-icon v-if="!settingsStore.isDark" icon-class="moon" />-->
<!--          </div>-->
<!--        </el-tooltip>-->

<!--        <el-tooltip content="布局大小" effect="dark" placement="bottom">-->
<!--          <size-select id="size-select" class="right-menu-item hover-effect" />-->
<!--        </el-tooltip>-->
<!--      </template>-->

      <el-dropdown @command="handleCommand" class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="userStore.avatar" class="user-avatar" />
          <div class="user-info">
            <div class="welcome-text">欢迎，{{ userStore.nickName }}</div>
            <div class="login-status">已登录</div>
          </div>
          <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu class="custom-dropdown-menu">
            <div class="dropdown-header">
              <img :src="userStore.avatar" class="dropdown-avatar" />
            </div>
            <el-divider style="margin: 8px 0;" />
            <router-link to="/user/profile" class="dropdown-link">
              <el-dropdown-item class="custom-dropdown-item">
                <el-icon class="item-icon"><User /></el-icon>
                <span>个人中心</span>
              </el-dropdown-item>
            </router-link>
            <el-dropdown-item class="custom-dropdown-item logout-item" command="logout">
              <el-icon class="item-icon"><SwitchButton /></el-icon>
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
<!--      <div class="right-menu-item hover-effect setting" @click="setLayout" v-if="settingsStore.showSettings">-->
<!--        <svg-icon icon-class="more-up" />-->
<!--      </div>-->
    </div>
  </div>
</template>

<script setup>
import { ElMessageBox } from 'element-plus'
import { ArrowDown, User, SwitchButton } from '@element-plus/icons-vue'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import HeaderSearch from '@/components/HeaderSearch'
import useAppStore from '@/store/modules/app'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'

const appStore = useAppStore()
const userStore = useUserStore()
const settingsStore = useSettingsStore()

function toggleSideBar() {
  appStore.toggleSideBar()
}

function handleCommand(command) {
  switch (command) {
    case "setLayout":
      setLayout()
      break
    case "logout":
      logout()
      break
    default:
      break
  }
}

function logout() {
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logOut().then(() => {
      location.href = '/index'
    })
  }).catch(() => { })
}

const emits = defineEmits(['setLayout'])
function setLayout() {
  emits('setLayout')
}

function toggleTheme() {
  settingsStore.toggleTheme()
}
</script>

<style lang='scss' scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: var(--navbar-bg);
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;
    display: flex;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }

      &.theme-switch-wrapper {
        display: flex;
        align-items: center;

        svg {
          transition: transform 0.3s;
          
          &:hover {
            transform: scale(1.15);
          }
        }
      }
    }

    .avatar-container {
      margin-right: 15px;
      padding: 0 15px;
      border-radius: 8px;
      transition: all 0.3s ease;

      &:hover {
        background: rgba(0, 0, 0, 0.05);
      }

      .avatar-wrapper {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 8px 0;
        cursor: pointer;

        .user-avatar {
          width: 36px;
          height: 36px;
          border-radius: 50%;
          border: 2px solid #e1e5e9;
          transition: all 0.3s ease;
          object-fit: cover;

          &:hover {
            border-color: #409eff;
            transform: scale(1.05);
          }
        }

        .user-info {
          display: flex;
          flex-direction: column;
          align-items: flex-start;
          line-height: 1.2;

          .welcome-text {
            font-size: 14px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 2px;
          }

          .login-status {
            font-size: 12px;
            color: #67c23a;
            font-weight: 500;
          }
        }

        .dropdown-icon {
          font-size: 14px;
          color: #909399;
          transition: transform 0.3s ease;
        }

        &:hover .dropdown-icon {
          transform: rotate(180deg);
        }
      }
    }
  }
}

// 下拉菜单样式
:deep(.custom-dropdown-menu) {
  min-width: 220px;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  border: 1px solid #e4e7ed;
  padding: 8px;

  .dropdown-header {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 16px;
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    border-radius: 8px;
    margin-bottom: 8px;

    .dropdown-avatar {
      width: 48px;
      height: 48px;
      border-radius: 50%;
      border: 3px solid #fff;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      object-fit: cover;
    }
  }

  .custom-dropdown-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 16px;
    border-radius: 8px;
    margin: 4px 0;
    transition: all 0.3s ease;
    cursor: pointer;

    .item-icon {
      font-size: 16px;
      color: #606266;
      transition: color 0.3s ease;
    }

    span {
      font-size: 14px;
      color: #303133;
      font-weight: 500;
    }

    &:hover {
      background: linear-gradient(135deg, #409eff 0%, #66b3ff 100%);
      color: white;
      transform: translateY(-1px);

      .item-icon {
        color: white;
      }

      span {
        color: white;
      }
    }

    &.logout-item {
      &:hover {
        background: linear-gradient(135deg, #f56c6c 0%, #ff8a8a 100%);
      }
    }
  }

  .dropdown-link {
    text-decoration: none;
    color: inherit;

    &:hover {
      text-decoration: none;
    }
  }
}
</style>
