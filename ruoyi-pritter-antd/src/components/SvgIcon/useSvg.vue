<template>
  <svg :class="svgClass" aria-hidden="true" :style="{ fontSize: size + 'px' }">
    <use :key="iconClass" :xlink:href="iconName" :fill="color" />
  </svg>
</template>

<script setup>
const props = defineProps({
  iconClass: {
    type: String,
    required: true,
  },
  className: {
    type: String,
    default: '',
  },
  color: {
    type: String,
    default: '',
  },
  size: {
    type: [String, Number],
    default: 14,
  },
})

const iconName = computed(() => `#icon-${props.iconClass}`)
const svgClass = computed(() =>
  props.className ? `svg-icon ${props.className}` : 'svg-icon'
)
</script>

<style scoped lang="scss">
.svg-icon {
  width: 1em;
  height: 1em;
  fill: currentColor;
  vertical-align: -0.125em;
}
</style>
