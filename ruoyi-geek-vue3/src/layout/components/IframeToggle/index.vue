<script setup lang="ts">
import { useRoute } from "vue-router";
import InnerLink from "../InnerLink/index.vue"
import useTagsViewStore from '@/store/modules/tagsView'

const route = useRoute();
const tagsViewStore = useTagsViewStore();
</script>
<template>
  <transition-group name="fade-transform">
    <inner-link v-for="(item, index) in tagsViewStore.iframeViews" v-show="route.path === item.path"
      :src="'' + item.meta?.link" :key="item.path" :iframeId="'iframe' + index" />
  </transition-group>
</template>
