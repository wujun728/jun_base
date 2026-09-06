<script setup lang="ts">
import type { FormItemTool } from '../../types/index.ts'

defineProps<{
  item: FormItemTool<'custom'>
  allDisabled: boolean
}>()
defineModel<unknown>('value')
</script>
<template>
  <div :class="`${item.field}Custom customItem`">
    <slot name="custom"> </slot>
  </div>
</template>

<style scoped lang="scss">
.customItem {
  width: 100%;
}
</style>
