<template>
  <component :is="type" v-bind="linkProps()">
    <slot />
  </component>
</template>

<script setup>
import { isExternal } from '@/utils/validate.js'
const props = defineProps({
  to: {
    type: [String, Object],
    required: true,
  },
})
// const router = useRouter()
// const handleClick = () => {
//   if (typeof props.to === 'string') {
//     router.push({
//       path: props.to,
//     })
//   } else {
//     router.push({
//       ...props.to,
//     })
//   }
// }
const isExt = computed(() => {
  return isExternal(props.to)
})

const type = computed(() => {
  if (isExt.value) {
    return 'a'
  }
  return 'router-link'
})

function linkProps() {
  if (isExt.value) {
    return {
      href: props.to,
      target: '_blank',
      rel: 'noopener',
    }
  }
  return {
    to: props.to,
  }
}
</script>
