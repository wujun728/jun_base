<template>
  <IFrame :src="src"></IFrame>
</template>

<script setup lang="ts">
import IFrame from '@/components/business/art-iframe/index.vue'

const src = import.meta.env.VITE_API_URL + '/swagger-ui/index.html'
</script>
