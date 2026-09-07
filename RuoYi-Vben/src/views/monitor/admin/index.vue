<template>
  <IFrame :frameSrc="url" />
</template>

<script setup lang="ts">
  import IFrame from '@/layouts/components/iframe/index.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'Admin' });

  const url = ref<string>('http://127.0.0.1:9090/admin/applications');
</script>

<style scoped></style>
