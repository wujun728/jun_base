<script setup>
import PageContent from './PageContent.vue'
import MobileContent from './MobileContent.vue'
import { useConfig } from '@/store/modules/layout'
import { computed, getCurrentInstance, h } from 'vue'
const props = defineProps({
  useMobile: {
    type: Boolean,
    default: true,
  },
})
const vm = getCurrentInstance()
const config = useConfig()
const layoutType = computed(() => {
  if (props.useMobile && config.layout.isMobile) {
    return MobileContent
  } else {
    return PageContent
  }
})
const changeRef = (instance) => {
  vm.exposed = instance
}
</script>
<template>
  <component
    :is="h(layoutType, { ...$attrs, ...props, ref: changeRef }, $slots)"
  >
  </component>
</template>

<style scoped lang="scss"></style>
