<template>
  <div class="card content-box">
    <iframe :src="src" frameborder="0" class="full-iframe"></iframe>
  </div>
</template>

<script setup lang="ts" name="Swagger">
import { ref } from "vue";

console.log(import.meta.env.VITE_API_URL);
const src = ref(import.meta.env.VITE_API_URL + "/doc.html");
</script>

<style scoped lang="scss">
.full-iframe {
  width: 100%;
  height: 100%;
}
</style>
