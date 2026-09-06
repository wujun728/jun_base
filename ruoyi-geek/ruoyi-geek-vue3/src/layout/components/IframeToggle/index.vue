<script setup lang="ts">
import { useRoute } from "vue-router";
import InnerLink from "../InnerLink/index.vue"
import useTagsViewStore from '@/store/modules/tagsView'

const route = useRoute();
const tagsViewStore = useTagsViewStore();
</script>
<template>
  <transition-group name="fade-transform">
    <inner-link v-for="(item, index) in tagsViewStore.iframeViews" :key="item.path" v-show="route.path === item.path"
      :src="'' + item.meta?.link" :iframeId="'iframe' + index" />
  </transition-group>
</template>
