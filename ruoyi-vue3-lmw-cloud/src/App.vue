<script setup>
import { provide } from 'vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import { useDict } from '@/utils/dict'
import hasPermi from '@/utils/hasPermi'
import { download } from '@/utils/hsj/service/index'
import {
  parseTime,
  handleTree,
  selectDictLabel,
  selectDictLabels,
} from '@/utils/ruoyi'
import { formatSearchTime } from '@/utils/hsj/utils'
import { tab, auth, modal, $download, isSmallScreen } from '@/plugins/index'
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
// 使用provide可以清晰的知道proxy中放了哪些方法，如果挂载在globalProperties中出现问题很难进行追溯
provide('proxy', proxy)
</script>

<template>
  <el-config-provider :locale="zhCn">
    <router-view></router-view>
  </el-config-provider>
</template>

<style scoped lang="sass"></style>
