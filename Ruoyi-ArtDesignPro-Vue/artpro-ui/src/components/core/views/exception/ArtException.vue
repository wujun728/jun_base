<template>
  <div class="page-content !border-0 !bg-transparent min-h-screen flex-cc">
    <div class="flex-cc max-md:!block max-md:text-center">
      <ThemeSvg :src="data.imgUrl" size="100%" class="!w-100" />
      <div class="ml-15 w-75 max-md:mx-auto max-md:mt-10 max-md:w-full max-md:text-center">
        <p class="text-xl leading-7 text-g-600 max-md:text-lg">{{ data.desc }}</p>
        <ElButton type="primary" size="large" @click="backHome" v-ripple class="mt-5">{{
          data.btnText
        }}</ElButton>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { useCommon } from '@/hooks/core/useCommon'
  import { useUserStore } from '@/store/modules/user'

  const router = useRouter()
  const userStore = useUserStore()

  interface ExceptionData {
    /** 标题 */
    title: string
    /** 描述 */
    desc: string
    /** 按钮文本 */
    btnText: string
    /** 图片地址 */
    imgUrl: string
  }

  withDefaults(
    defineProps<{
      data: ExceptionData
    }>(),
    {}
  )

  const { homePath } = useCommon()

  const forceReloadTo = (path: string) => {
    const targetPath = path.startsWith('/') ? path : `/${path}`
    const cleanUrl = `${window.location.origin}${window.location.pathname}#${targetPath}`
    window.history.replaceState(null, '', cleanUrl)
    window.location.reload()
  }

  const backHome = () => {
    const targetHomePath = homePath.value || '/'

    if (!userStore.isLogin) {
      router.push({
        name: 'Login',
        query: { redirect: targetHomePath }
      })
      return
    }

    // 异常页通常意味着动态路由未恢复或初始化失败，SPA 内部跳转可能仍停在坏状态。
    // 先把地址栏替换为干净首页地址，再整页重载，重新恢复登录态、菜单和动态路由。
    if (
      router.currentRoute.value.path === targetHomePath ||
      ['Exception403', 'Exception404', 'Exception500'].includes(
        String(router.currentRoute.value.name || '')
      )
    ) {
      forceReloadTo(targetHomePath)
      return
    }

    router.push(targetHomePath).catch(() => forceReloadTo(targetHomePath))
  }
</script>
