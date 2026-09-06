<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统工具', '系统接口']"></Breadcrumb>

    <i-frame :src="state.url"></i-frame>

  </div>
</template>

<script lang="ts" setup>
import IFrame from '@/components/iframe/index.vue';

const env = import.meta.env.VITE_APP_BASE_API;
const state = {
  url: `${env}/swagger-ui/index.html`,
};
</script>

<style scoped>
.container {
  padding: 0 20px 20px 20px;
}
</style>
