<script setup>
import Aside from '../components/Aside.vue'
import Header from '../components/Header.vue'
import Main from '../components/Main.vue'
import RightPanel from '../components/Config/RightPanel.vue'

defineOptions({ name: 'ClassicLayout' })

const showRightPanel = ref(false)
</script>

<template>
  <a-layout class="layout-container">
    <Aside />
    <a-layout class="content-wrapper">
      <Header v-model:show-right-panel="showRightPanel" />
      <Main />
    </a-layout>
    <RightPanel v-model:open="showRightPanel" />
  </a-layout>
</template>

<style scoped lang="scss">
.layout-container {
  min-height: 100vh;
  background: var(--body-background, #f5f7fa);
}

.content-wrapper {
  flex-direction: column;
  min-height: 100vh;
  overflow: hidden;
}
</style>
