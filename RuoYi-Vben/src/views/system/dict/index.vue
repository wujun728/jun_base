<template>
  <PageWrapper dense>
    <div class="flex flex-wrap lg:flex-nowrap">
      <DictType class="w-1/2 <lg:w-full" @get-dict-type="getDictType" />
      <DictData class="w-1/2 <lg:w-full" v-model:dict-type="dictTypeRef" />
    </div>
    <div> </div>
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import DictType from './type/DictType.vue';
  import DictData from './data/DictData.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'Dict' });

  const dictTypeRef = ref<string>('');
  function getDictType(dictType: string) {
    dictTypeRef.value = dictType;
  }
</script>

<style lang="less" scoped>
  :deep(.vben-basic-table-form-container) {
    padding: 8px;
  }
</style>
