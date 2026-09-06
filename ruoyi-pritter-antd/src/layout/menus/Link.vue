<template>
  <component :is="type" v-bind="linkProps()">
    <slot />
  </component>
</template>

<script setup>
import { isExternal } from '@/utils/validate'

const props = defineProps({
  to: {
    type: [String, Object],
    required: true,
  },
})

const isExt = computed(() => isExternal(props.to))

const type = computed(() => (isExt.value ? 'a' : 'router-link'))

function linkProps() {
  if (isExt.value) {
    return {
      href: props.to,
      target: '_blank',
      rel: 'noopener',
    }
  }
  return { to: props.to }
}
</script>
