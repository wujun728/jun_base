<script setup lang="ts">
import { isExternal } from '@/utils/validate'
import { computed } from 'vue'

const props = defineProps({
  to: {
    type: String,
    required: true
  }
})

const isExt = computed(() => isExternal(props.to))
const type = computed(() => isExt.value ? 'a' : 'router-link')

function linkProps() {
  if (isExt.value) {
    return {
      href: props.to,
      target: '_blank',
      rel: 'noopener'
    }
  }
  return { to: props.to }
}
</script>


<template>
  <component :is="type" v-bind="linkProps()">
    <slot />
  </component>
</template>
