<script setup name="Swagger">
import IFrame from '@/components/iFrame/index.vue'

const url = ref(import.meta.env.VITE_APP_BASE_API + '/swagger-ui/index.html')
</script>

<template>
  <div class="swagger-page">
    <IFrame v-model:src="url" />
  </div>
</template>

<style scoped lang="scss">
.swagger-page {
  margin: var(--ba-main-space);
}
</style>
