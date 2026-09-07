<template>
  <IFrame :frameSrc="url" />
</template>

<script setup lang="ts">
  import IFrame from '@/layouts/components/iframe/index.vue';
  import { ref } from 'vue';
  import { useGlobSetting } from '@/hooks/setting';

  defineOptions({ name: 'Druid' });

  const { apiUrl } = useGlobSetting();
  const url = ref<string>(apiUrl + '/druid/login.html');
</script>

<style scoped></style>
