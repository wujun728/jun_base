<template>
  <div>
    <iFrame v-model:src="url"></iFrame>
  </div>
</template>

<script setup>
import iFrame from '@/components/iFrame/index.vue'

import { ref } from 'vue'

const url = ref(import.meta.env.VITE_APP_BASE_API + '/druid/login.html')
</script>
