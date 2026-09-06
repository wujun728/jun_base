<script setup>
const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
  allDisabled: {
    type: Boolean,
  },
})
const value = defineModel('value')
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
