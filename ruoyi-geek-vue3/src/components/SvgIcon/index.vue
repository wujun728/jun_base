<template>
  <svg :class="svgClass" aria-hidden="true">
    <use :xlink:href="iconName" :fill="color" />
  </svg>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = withDefaults(defineProps<{
  iconClass: string
  className?: string
  color?: string
}>(), {
  className: '',
  color: ''
})

const iconName = computed(() => `#icon-${props.iconClass}`)
const svgClass = computed(() => props.className ? `svg-icon ${props.className}` : 'svg-icon')
</script>

<style scope lang="scss">
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
