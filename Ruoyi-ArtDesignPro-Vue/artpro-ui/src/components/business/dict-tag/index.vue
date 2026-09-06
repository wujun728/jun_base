<template>
  <ElTag v-if="item" :type="tagType">{{ item.dictLabel }}</ElTag>
  <span v-else>{{ value }}</span>
</template>

<script setup lang="ts">
  import type { DictData } from '@/api/common'

  const props = defineProps<{ options: DictData[]; value?: string | number }>()
  const item = computed(() =>
    props.options.find((option) => String(option.dictValue) === String(props.value))
  )
  const tagType = computed(() => {
    const value = item.value?.listClass
    return ['success', 'info', 'warning', 'danger'].includes(value || '')
      ? (value as 'success' | 'info' | 'warning' | 'danger')
      : undefined
  })
</script>
