<template>
  <div class="art-full-height"
    ><ElCard
      ><template #header
        ><span>{{ title }}</span></template
      ><ElSkeleton v-if="loading" :rows="8" animated /><pre
        v-else
        class="overflow-auto whitespace-pre-wrap"
        >{{ JSON.stringify(data, null, 2) }}</pre
      >
    </ElCard></div
  >
</template>
<script setup lang="ts">
  import { get } from '@/api/ruoyi'
  const props = defineProps<{ title: string; endpoint: string }>()
  const loading = ref(true)
  const data = ref<unknown>()
  onMounted(async () => {
    try {
      data.value = await get(props.endpoint)
    } finally {
      loading.value = false
    }
  })
</script>
