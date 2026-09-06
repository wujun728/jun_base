<script setup>
const props = defineProps({
  value: {
    type: [String, Number],
  },
  options: {
    type: [Object, Array],
    default: () => [],
  },
})
const optionComputed = computed(() => {
  return props.options || []
})
const valueComputed = computed(() => {
  return props.value || ''
})
</script>

<template>
  <DictTag :options="optionComputed" :value="valueComputed"></DictTag>
</template>

<style scoped lang="scss"></style>
