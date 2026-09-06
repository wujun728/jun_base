<script setup lang="ts">
import { isExternal } from '@/utils/validate'
import { computed } from 'vue'

const props = defineProps<{ to: string }>()
const isExt = computed(() => isExternal(props.to))
const type = computed(() => isExt.value ? 'a' : 'router-link')
const linkProps = computed(() => {
  if (isExt.value) {
    return {
      href: props.to,
      target: '_blank',
      rel: 'noopener'
    }
  }
  return { to: props.to }
})
</script>
<template>
  <component :is="type" v-bind="linkProps">
    <slot />
  </component>
</template>
