<script setup>
import useTagsViewStore from '@/store/modules/tagsView'
import { useConfig } from '@/store/modules/layout'
const tagsViewStore = useTagsViewStore()
const config = useConfig()
</script>
<template>
  <el-main class="main">
    <router-view v-slot="{ Component, route }">
      <transition :name="config.layout.mainAnimation" mode="out-in">
        <keep-alive :include="tagsViewStore.cachedViews">
          <component
            v-if="!route.meta.link"
            :is="Component"
            :key="route.path"
          />
        </keep-alive>
      </transition>
    </router-view>
  </el-main>
</template>

<style scoped lang="scss">
.main {
  padding: 0;
}
</style>
