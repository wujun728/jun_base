<template>
  <IFrame :frameSrc="url" />
</template>

<script setup lang="ts">
  import IFrame from '@/layouts/components/iframe/index.vue';
  import { ref } from 'vue';
  import { useGlobSetting } from '@/hooks/setting';

  defineOptions({ name: 'Swagger' });

  const { apiUrl } = useGlobSetting();
  const url = ref<string>(apiUrl + '/swagger-ui/index.html');
</script>

<style scoped></style>
