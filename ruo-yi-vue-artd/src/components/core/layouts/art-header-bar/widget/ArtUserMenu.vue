<!-- 用户菜单 -->
<template>
  <ElPopover
    ref="userMenuPopover"
    placement="bottom-end"
    :width="240"
    :hide-after="0"
    :offset="10"
    trigger="hover"
    :show-arrow="false"
    popper-class="user-menu-popover"
    popper-style="padding: 5px 16px;">
    <template #reference>
      <img
        class="c-p mr-5 size-8.5 rounded-full max-sm:mr-[16px] max-sm:h-6.5 max-sm:w-6.5"
        src="@imgs/user/avatar.webp"
        alt="avatar" />
    </template>
    <template #default>
      <div class="pt-3">
        <div class="flex-c px-0 pb-1">
          <img
            class="float-left mr-3 ml-0 h-10 w-10 overflow-hidden rounded-full"
            src="@imgs/user/avatar.webp" />
          <div class="h-full w-[calc(100%-60px)]">
            <span class="block truncate text-sm font-medium text-g-800">
              {{ userInfo.userName }}
            </span>
            <span class="mt-0.5 block truncate text-xs text-g-500">
              {{ userInfo.email }}
            </span>
          </div>
        </div>
        <ul class="mt-3 border-t border-g-300/80 py-4">
          <li class="btn-item" @click="goPage('/user-center/detail')">
            <ArtSvgIcon icon="ri:user-3-line" />
            <span>{{ $t('topBar.user.userCenter') }}</span>
          </li>
          <li class="btn-item" @click="toDocs()">
            <ArtSvgIcon icon="ri:book-2-line" />
            <span>{{ $t('topBar.user.docs') }}</span>
          </li>
          <li class="btn-item" @click="toGithub()">
            <ArtSvgIcon icon="ri:github-line" />
            <span>{{ $t('topBar.user.github') }}</span>
          </li>
          <li class="btn-item" @click="lockScreen()">
            <ArtSvgIcon icon="ri:lock-line" />
            <span>{{ $t('topBar.user.lockScreen') }}</span>
          </li>
          <div class="my-2 h-px w-full bg-g-300/80"></div>
          <div class="log-out c-p" @click="loginOut">
            {{ $t('topBar.user.logout') }}
          </div>
        </ul>
      </div>
    </template>
  </ElPopover>
</template>

<script setup lang="ts">
import { ElMessageBox } from 'element-plus'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'

import { useUserStore } from '@/store/modules/user'
import { WEB_LINKS } from '@/utils/constants'
import { mittBus } from '@/utils/sys'

defineOptions({ name: 'ArtUserMenu' })

const router = useRouter()
const { t } = useI18n()
const userStore = useUserStore()

const { getUserInfo: userInfo } = storeToRefs(userStore)
const userMenuPopover = ref()

/**
 * 页面跳转
 * @param {string} path - 目标路径
 */
const goPage = (path: string): void => {
  router.push(path)
}

/**
 * 打开文档页面
 */
const toDocs = (): void => {
  window.open(WEB_LINKS.DOCS)
}

/**
 * 打开 GitHub 页面
 */
const toGithub = (): void => {
  window.open(WEB_LINKS.GITHUB)
}

/**
 * 打开锁屏功能
 */
const lockScreen = (): void => {
  mittBus.emit('openLockScreen')
}

/**
 * 用户登出确认
 */
const loginOut = (): void => {
  closeUserMenu()
  setTimeout(() => {
    ElMessageBox.confirm(t('common.logOutTips'), t('common.tips'), {
      confirmButtonText: t('common.confirm'),
      cancelButtonText: t('common.cancel'),
      customClass: 'login-out-dialog'
    }).then(() => {
      userStore.logOut()
    })
  }, 200)
}

/**
 * 关闭用户菜单弹出层
 */
const closeUserMenu = (): void => {
  setTimeout(() => {
    userMenuPopover.value.hide()
  }, 100)
}
</script>

<style scoped>
@reference '@styles/core/tailwind.css';

@layer components {
  .btn-item {
    @apply mb-3 flex cursor-pointer items-center rounded-md p-2 select-none last:mb-0;

    span {
      @apply text-sm;
    }

    .art-svg-icon {
      @apply mr-2 text-base;
    }

    &:hover {
      background-color: var(--art-gray-200);
    }
  }
}

.log-out {
  @apply mt-5 rounded-md border border-g-400 py-1.5 text-center text-xs transition-all duration-200 hover:shadow-xl;
}
</style>
