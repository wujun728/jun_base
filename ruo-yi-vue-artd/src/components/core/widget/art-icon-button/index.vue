<!-- 按钮组件 -->
<template>
  <div
    class="flex-cc c-p tad-300 inline-flex size-8.5 rounded text-xl text-g-600 hover:bg-hover-color dark:text-g-800"
    :class="{ 'rounded-full': circle }">
    <ArtSvgIcon :icon="icon"></ArtSvgIcon>
    <slot></slot>
  </div>
</template>

<script lang="ts" setup>
defineOptions({ name: 'ArtIconButton' })

interface Props {
  /** 图标名称 */
  icon: string
  /** 圆角按钮 */
  circle?: boolean
}

withDefaults(defineProps<Props>(), {})
</script>
