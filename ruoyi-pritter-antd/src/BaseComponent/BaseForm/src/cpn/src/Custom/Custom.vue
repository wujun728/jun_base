<script setup>
defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
defineModel('value')
</script>

<template>
  <div :id="item.field" :class="`${item.field}Custom customItem`">
    <slot name="custom" />
  </div>
</template>

<style scoped lang="scss">
.customItem {
  width: 100%;
}
</style>
