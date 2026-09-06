<script setup>
import { storeToRefs } from 'pinia'
import screenfull from 'screenfull'
import modal from '@/plugins/modal'
import { useConfig } from '@/store/modules/layout'
import useUserStore from '@/store/modules/user'
import { toggleDarkLight } from '@/utils/useDark'

defineOptions({ name: 'NavMenus' })

const configStore = useConfig()
const userStore = useUserStore()
const { nickName, name, avatar } = storeToRefs(userStore)
const router = useRouter()

const isFullScreen = ref(false)
const showRightPanel = defineModel('showRightPanel', {
  type: Boolean,
  default: false,
})

const onFullScreen = () => {
  if (!screenfull.isEnabled) {
    modal.msgWarning('当前浏览器不支持全屏')
    return
  }
  screenfull.toggle()
  screenfull.onchange(() => {
    isFullScreen.value = screenfull.isFullscreen
  })
}

const onLogout = () => {
  modal
    .confirm('确定注销并退出系统吗？', '提示')
    .then(() => userStore.logOut())
    .then(() => {
      location.href = '/index'
    })
    .catch(() => {})
}

const onProfile = () => {
  router.push('/user/profile')
}
</script>

<template>
  <div class="nav-menus">
    <a-tooltip title="主题切换">
      <div class="nav-menu-item" @click="toggleDarkLight">
        <SvgIcon
          :iconClass="configStore.layout.isDark ? 'light' : 'dark'"
          :size="16"
        />
      </div>
    </a-tooltip>

    <a-tooltip :title="isFullScreen ? '退出全屏' : '全屏'">
      <div class="nav-menu-item" @click="onFullScreen">
        <SvgIcon
          :icon-class="isFullScreen ? 'fullScreenCancel' : 'fullscreen'"
          :size="16"
        />
      </div>
    </a-tooltip>

    <a-dropdown placement="bottomRight" :trigger="['click']">
      <button type="button" class="nav-menu-user">
        <a-avatar :size="28" :src="avatar">
          <template #icon>
            <span>{{ (nickName || name || 'U').charAt(0) }}</span>
          </template>
        </a-avatar>
        <span class="nav-menu-user-name">{{ nickName || name }}</span>
      </button>
      <template #overlay>
        <a-menu>
          <a-menu-item key="profile" @click="onProfile">个人中心</a-menu-item>
          <a-menu-divider />
          <a-menu-item key="logout" @click="onLogout">退出登录</a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>

    <a-tooltip title="布局配置">
      <div
        class="nav-menu-item"
        :class="{ active: showRightPanel }"
        @click="showRightPanel = !showRightPanel"
      >
        <SvgIcon icon-class="cogs" :size="16" />
      </div>
    </a-tooltip>
  </div>
</template>

<style scoped lang="scss">
.nav-menus {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-shrink: 0;
}

.nav-menu-item {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 8px;
  background: transparent;
  cursor: pointer;
  color: var(--ba-text-color);
  font-size: 16px;
  transition: all 0.2s;

  &:hover,
  &.active {
    background: rgba(0, 0, 0, 0.04);
  }
}

.nav-menu-user {
  display: flex;
  align-items: center;
  gap: 8px;
  height: 36px;
  padding: 0 8px;
  margin-left: 4px;
  border: none;
  border-radius: 8px;
  background: transparent;
  cursor: pointer;
  transition: all 0.2s;
  color: var(--ba-text-color);
  &:hover {
    background: rgba(0, 0, 0, 0.04);
  }
}

.nav-menu-user-name {
  font-size: 13px;
  color: var(--ba-text-color);
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dark {
  .nav-menu-item {
    &:hover,
    &.active {
      background: rgba(255, 255, 255, 0.08);
    }
  }
  .nav-menu-user:hover {
    background: rgba(255, 255, 255, 0.08);
  }
}
</style>
