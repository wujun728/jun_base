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

      <el-dropdown
        @command="handleCommand"
        class="avatar-container right-menu-item hover-effect"
        trigger="click"
        popper-class="navbar-user-popper"
      >
        <div class="avatar-wrapper">
          <div class="user-info">
            <div class="welcome-text">{{ userStore.nickName }}</div>
            <div class="login-status">{{ roleLabel }}</div>
          </div>
          <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu class="custom-dropdown-menu">
            <div class="dropdown-actions">
              <el-dropdown-item class="custom-dropdown-item action-primary" command="profile">
                <el-icon class="item-icon"><User /></el-icon>
                <span>个人中心</span>
              </el-dropdown-item>
              <el-dropdown-item class="custom-dropdown-item logout-item" command="logout">
                <el-icon class="item-icon"><SwitchButton /></el-icon>
                <span>退出登录</span>
              </el-dropdown-item>
            </div>
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
import useAppStore from '@/store/modules/app'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'

const appStore = useAppStore()
const userStore = useUserStore()
const settingsStore = useSettingsStore()
const router = useRouter()

const roleLabel = computed(() => {
  const [firstRole = 'ROLE_DEFAULT'] = userStore.roles || []
  if (firstRole === 'admin') return '超级管理员'
  if (firstRole === 'ROLE_DEFAULT') return '默认角色'
  return firstRole.replace(/^ROLE_/, '').replace(/_/g, ' ')
})

function toggleSideBar() {
  appStore.toggleSideBar()
}

function handleCommand(command) {
  switch (command) {
    case "profile":
      router.push('/user/profile')
      break
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
</script>

<style lang='scss' scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: var(--navbar-bg);
  border-bottom: 1px solid var(--screen-border);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.18);
  backdrop-filter: blur(14px);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: var(--navbar-hover);
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
      color: var(--screen-text-regular);
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: var(--navbar-hover);
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
      border-radius: 12px;
      transition: all 0.3s ease;
      border: 1px solid transparent;

      &:hover {
        background: rgba(47, 123, 255, 0.12);
        border-color: rgba(72, 145, 255, 0.18);
      }

      .avatar-wrapper {
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 8px 0;
        cursor: pointer;

        .user-info {
          display: flex;
          flex-direction: column;
          align-items: flex-start;
          justify-content: center;
          line-height: 1.15;
          min-width: 0;

          .welcome-text {
            font-size: 14px;
            font-weight: 600;
            color: var(--screen-text-primary);
            max-width: 116px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .login-status {
            font-size: 12px;
            color: var(--screen-text-secondary);
            font-weight: 500;
            margin-top: 3px;
          }
        }

        .dropdown-icon {
          font-size: 14px;
          color: var(--screen-text-secondary);
          transition: transform 0.3s ease;
        }

        &:hover .dropdown-icon {
          transform: rotate(180deg);
        }
      }
    }
  }
}

</style>

<style lang='scss'>
.navbar-user-popper {
  width: 126px !important;
  min-width: 126px !important;
  max-width: 126px !important;
  padding: 0 !important;
  border: 1px solid rgba(72, 145, 255, 0.22) !important;
  border-radius: 14px !important;
  background: linear-gradient(180deg, rgba(8, 24, 52, 0.98) 0%, rgba(5, 18, 40, 0.98) 100%) !important;
  box-shadow: 0 18px 40px rgba(0, 0, 0, 0.28), 0 0 0 1px rgba(52, 208, 255, 0.06) inset !important;
  overflow: hidden !important;

  .el-popper__arrow::before {
    background: rgba(8, 24, 52, 0.98) !important;
    border: 1px solid rgba(72, 145, 255, 0.22) !important;
  }

  .custom-dropdown-menu {
    width: 100% !important;
    min-width: 100% !important;
    max-width: 100% !important;
    padding: 8px;
    border: none !important;
    background: transparent !important;
    box-sizing: border-box;
    overflow: hidden;
  }

  .dropdown-actions {
    display: grid;
    grid-template-columns: 1fr;
    gap: 8px;
    padding: 4px 0;
    justify-items: center;
  }

  .custom-dropdown-item {
    display: inline-flex !important;
    align-items: center;
    gap: 10px;
    justify-content: center;
    min-height: 40px;
    width: auto !important;
    min-width: 94px;
    padding: 0 14px;
    border-radius: 10px;
    margin: 0;
    transition: all 0.25s ease;
    cursor: pointer;
    border: 1px solid rgba(72, 145, 255, 0.14);
    background: rgba(47, 123, 255, 0.06);

    .item-icon {
      font-size: 15px;
      color: var(--screen-text-secondary);
      transition: color 0.25s ease;
      margin-right: 0;
    }

    span {
      font-size: 13px;
      color: var(--screen-text-primary);
      font-weight: 600;
    }

    &.action-primary {
      background: linear-gradient(135deg, rgba(47, 123, 255, 0.16) 0%, rgba(52, 208, 255, 0.12) 100%);
    }
  }

  .el-dropdown-menu__item.custom-dropdown-item:not(.is-disabled):hover,
  .el-dropdown-menu__item.custom-dropdown-item:not(.is-disabled):focus {
    background: linear-gradient(135deg, rgba(19, 58, 122, 0.96) 0%, rgba(11, 40, 93, 0.98) 100%) !important;
    background-color: rgba(11, 40, 93, 0.98) !important;
    transform: translateY(-1px);
    border-color: rgba(52, 208, 255, 0.3) !important;
    box-shadow: 0 8px 18px rgba(5, 17, 39, 0.36), inset 0 0 0 1px rgba(52, 208, 255, 0.08) !important;

    .item-icon,
    span {
      color: #ffffff;
    }
  }

  .el-dropdown-menu__item.custom-dropdown-item.logout-item:not(.is-disabled):hover,
  .el-dropdown-menu__item.custom-dropdown-item.logout-item:not(.is-disabled):focus {
    background: linear-gradient(135deg, rgba(110, 36, 68, 0.96) 0%, rgba(78, 24, 49, 0.98) 100%) !important;
    background-color: rgba(78, 24, 49, 0.98) !important;
    border-color: rgba(255, 107, 139, 0.3) !important;
    box-shadow: 0 8px 18px rgba(36, 8, 20, 0.34), inset 0 0 0 1px rgba(255, 107, 139, 0.08) !important;
  }
}
</style>
