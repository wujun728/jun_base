<script setup>
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import { useConfig } from '@/store/modules/layout'
import { antThemeConfig } from '@/utils/antTheme'
import { syncLayoutCssVars } from '@/utils/layoutTheme'
import { initDark } from '@/utils/useDark'
import { useDict } from '@/utils/dict'
import hasPermi from '@/utils/hasPermi'
import { download } from '@/utils/service/index'
import {
  handleTree,
  selectDictLabel,
  selectDictLabels,
  formatSearchTime,
} from '@/utils/utils'
import { parseTime } from '@/utils/timeFormat'
import { tab, auth, modal, $download, isSmallScreen } from '@/plugins/index'

dayjs.locale('zh-cn')

const layoutStore = useConfig()

initDark()

watch(
  () => [
    layoutStore.layout.menuBackground,
    layoutStore.layout.menuColor,
    layoutStore.layout.menuHoverBackground,
    layoutStore.layout.menuActiveBackground,
    layoutStore.layout.menuActiveColor,
  ],
  () => syncLayoutCssVars(layoutStore),
  { deep: true }
)

const proxy = {
  download,
  useDict,
  parseTime,
  handleTree,
  selectDictLabel,
  selectDictLabels,
  hasPermi,
  formatSearchTime,
  $tab: tab,
  $auth: auth,
  $modal: modal,
  $download: $download,
  $isSmallScreen: isSmallScreen(),
}
provide('proxy', proxy)

onMounted(() => {
  syncLayoutCssVars(layoutStore)
})
</script>

<template>
  <a-config-provider :locale="zhCN" :theme="antThemeConfig">
    <router-view></router-view>
  </a-config-provider>
</template>
