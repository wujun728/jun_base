<template>
  <el-header class="layout-header">
    <component :is="config.layout.layoutMode + 'NavBar'"></component>
  </el-header>
</template>
<script setup>
import { useConfig } from '@/store/modules/layout'
import DefaultNavBar from '../navBar/Default.vue'
import ClassicNavBar from '../navBar/Classic.vue'
import StreamlineNavBar from '../menus/MenuHorizontal.vue'
import DoubleNavBar from '../navBar/Double.vue'

defineOptions({
  name: 'layout/header',
  components: { DefaultNavBar, ClassicNavBar, StreamlineNavBar, DoubleNavBar },
})

const config = useConfig()
</script>

<style scoped lang="scss">
.layout-header {
  height: auto;
  padding: 0;
}
</style>
