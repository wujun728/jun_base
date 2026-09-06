<script setup name="Druid">
import IFrame from '@/components/iFrame/index.vue'

const url = ref(import.meta.env.VITE_APP_BASE_API + '/druid/login.html')
</script>

<template>
  <div class="druid-page">
    <IFrame v-model:src="url" />
  </div>
</template>

<style scoped lang="scss"></style>
