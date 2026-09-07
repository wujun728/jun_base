<template>
  <vue-json-pretty :path="'res'" :deep="3" :showLength="true" :data="data" />
</template>

<script lang="ts" setup>
  import VueJsonPretty from 'vue-json-pretty';
  import 'vue-json-pretty/lib/styles.css';

  defineProps({
    data: Object,
  });
</script>

<style lang="less">
  html[data-theme='dark'] {
    .vjs-tree-node:hover {
      background-color: #333;
    }
  }
</style>
