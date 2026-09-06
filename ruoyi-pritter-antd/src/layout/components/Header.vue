<script setup>
import DefaultNavBar from '../navBar/Default.vue'

defineOptions({ name: 'LayoutHeader' })

const showRightPanel = defineModel('showRightPanel', { type: Boolean, default: false })
</script>

<template>
  <a-layout-header class="layout-header">
    <DefaultNavBar v-model:show-right-panel="showRightPanel" />
  </a-layout-header>
</template>

<style scoped lang="scss">
.layout-header {
  height: auto;
  line-height: normal;
  padding: 0;
  background: transparent;
}
</style>
