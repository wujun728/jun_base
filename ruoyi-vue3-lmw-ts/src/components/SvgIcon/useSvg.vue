<template>
  <svg :class="svgClass" aria-hidden="true" :style="{ fontSize: size }">
    <use :href="iconName" :fill="color" />
  </svg>
</template>

<script setup lang="ts">
type Props = {
  iconClass: string
  className?: string
  color?: string
  size: StrNum
}
const {
  iconClass,
  className = '',
  color = '',
  size = 14,
} = defineProps<Props>()
const iconName = computed(() => {
  return `#icon-${iconClass}`
})
const svgClass = computed(() => {
  if (className) {
    return `svg-icon ${className}`
  }
  return 'svg-icon'
})
</script>

<style scoped lang="scss">
.sub-el-icon,
.nav-icon {
  display: inline-block;
  font-size: 15px;
  margin-right: 12px;
  position: relative;
}

.svg-icon {
  width: 1em;
  height: 1em;
  position: relative;
  fill: currentColor;
  vertical-align: -2px;
}
</style>
