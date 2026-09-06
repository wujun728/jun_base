<script setup lang="ts">
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import useConfig from '@/store/modules/layout.ts'
import { updateHtmlDarkClass } from '@/utils/useDark'
const config = useConfig()

updateHtmlDarkClass(config.layout.isDark)
</script>

<template>
  <el-config-provider :locale="zhCn">
    <router-view></router-view>
  </el-config-provider>
</template>

<style scoped lang="sass"></style>
