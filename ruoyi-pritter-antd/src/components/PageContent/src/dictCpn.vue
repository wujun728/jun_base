<script setup>
import DictTag from '@/components/DictTag/index.vue'

const props = defineProps({
  value: {
    type: [String, Number],
  },
  options: {
    type: [Object, Array],
    default: () => [],
  },
})

const optionComputed = computed(() => props.options || [])
const valueComputed = computed(() => props.value ?? '')
</script>

<template>
  <DictTag :options="optionComputed" :value="valueComputed" />
</template>
