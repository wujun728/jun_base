<template>
  <Skeleton :loading="loading">
    <Result v-if="success" status="success" title="修改成功">
      <template #extra>
        <a-button type="primary" @click="handleClose"> 关闭 </a-button>
      </template>
    </Result>
    <Result v-else status="error" title="修改失败" :sub-title="errMsg">
      <template #extra>
        <a-button type="primary" @click="emit('jumpto', 0)"> 返回重试 </a-button>
      </template>
    </Result>
  </Skeleton>
</template>

<script setup lang="ts">
  import { Result, Skeleton } from 'ant-design-vue';
  import { useTabs } from '@/hooks/web/useTabs';
  import { useGo } from '@/hooks/web/usePage';
  import { ref, onMounted } from 'vue';

  const { closeCurrent } = useTabs();
  const emit = defineEmits(['jumpto']);

  const loading = ref<boolean>(true);
  const go = useGo();
  async function handleClose() {
    await closeCurrent();
    go('/tool/gen');
  }

  onMounted(() => {
    loading.value = false;
  });

  type Props = {
    success: boolean;
    errMsg: string;
  };
  withDefaults(defineProps<Props>(), {
    success: true,
    errMsg: '',
  });
</script>

<style scoped></style>
