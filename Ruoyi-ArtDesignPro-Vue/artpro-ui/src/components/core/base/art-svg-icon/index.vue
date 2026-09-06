<!-- 图标组件 -->
<template>
  <Icon v-if="icon" :icon="localIcon" v-bind="bindAttrs" class="art-svg-icon inline" />
</template>

<script setup lang="ts">
  import { Icon } from '@iconify/vue'
  import { normalizeLocalIcon } from '@/utils/ui/iconify-loader'

  defineOptions({ name: 'ArtSvgIcon', inheritAttrs: false })

  interface Props {
    /** Iconify icon name */
    icon?: string
  }

  const props = defineProps<Props>()
  const localIcon = computed(() => normalizeLocalIcon(props.icon))

  const attrs = useAttrs()

  const bindAttrs = computed<{ class: string; style: string }>(() => ({
    class: (attrs.class as string) || '',
    style: (attrs.style as string) || ''
  }))
</script>
