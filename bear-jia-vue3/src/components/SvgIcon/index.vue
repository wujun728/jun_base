<template>
  <!-- 外部链接图标 -->
  <div
    v-if="isExternalIcon"
    :style="styleExternalIcon"
    class="svg-external-icon svg-icon"
    v-on="listeners"
  />
  <!-- Sprite 图标（使用 symbol） -->
  <svg
    v-else-if="useSpriteIcon"
    :class="svgClass"
    aria-hidden="true"
    v-on="listeners"
    :style="iconStyle"
  >
    <use :xlink:href="iconName" />
  </svg>
  <!-- 直接加载 SVG 文件 -->
  <img
    v-else
    :src="iconUrl"
    :alt="props.iconClass"
    :class="svgClass"
    v-on="listeners"
    :style="iconStyle"
  />
</template>

<script setup>
import { computed, useAttrs } from 'vue'
import { isExternal } from '@/utils/BearJia'

const props = defineProps({
  iconClass: {
    type: String,
    required: true
  },
  className: {
    type: String,
    default: ''
  },
  color: {
    type: String,
    default: 'currentColor'
  },
  size: {
    type: [String, Number],
    default: '1em'
  }
})

// 事件监听透传
const listeners = useAttrs()

// 判断是否为外部链接
const isExternalIcon = computed(() => isExternal(props.iconClass))

// 判断是否使用 sprite 图标（以 # 开头或在 DOM 中存在）
const useSpriteIcon = computed(() => {
  return props.iconClass.startsWith('#') ||
         (typeof document !== 'undefined' && document.getElementById(`icon-${props.iconClass}`))
})

// Sprite 图标名称
const iconName = computed(() => {
  if (props.iconClass.startsWith('#')) {
    return props.iconClass
  }
  return `#icon-${props.iconClass}`
})

// 直接加载的 SVG 文件 URL
const iconUrl = computed(() => {
  try {
    return new URL(`../../assets/icons/${props.iconClass}.svg`, import.meta.url).href
  } catch (e) {
    console.warn(`Icon ${props.iconClass} not found`)
    return ''
  }
})

// CSS 类名
const svgClass = computed(() =>
  props.className ? 'svg-icon ' + props.className : 'svg-icon'
)

// 外部图标样式
const styleExternalIcon = computed(() => ({
  mask: `url(${props.iconClass}) no-repeat 50% 50%`,
  '-webkit-mask': `url(${props.iconClass}) no-repeat 50% 50%`,
  backgroundColor: props.color
}))

// 图标样式
const iconStyle = computed(() => {
  const sizeValue = typeof props.size === 'number' ? `${props.size}px` : props.size
  const style = {
    width: sizeValue,
    height: sizeValue,
    color: props.color,
    fill: props.color
  }

  // 对于 img 标签,如果指定了非默认颜色,使用 filter 来改变颜色
  if (!useSpriteIcon.value && props.color !== 'currentColor') {
    // 将颜色转换为 filter
    style.filter = getColorFilter(props.color)
  }

  return style
})

// 将颜色转换为 CSS filter
const getColorFilter = (color) => {
  // 预定义的颜色映射
  const colorFilters = {
    '#1890ff': 'brightness(0) saturate(100%) invert(47%) sepia(96%) saturate(1743%) hue-rotate(194deg) brightness(101%) contrast(101%)',
    '#52c41a': 'brightness(0) saturate(100%) invert(65%) sepia(57%) saturate(466%) hue-rotate(73deg) brightness(95%) contrast(87%)',
    '#faad14': 'brightness(0) saturate(100%) invert(74%) sepia(65%) saturate(1574%) hue-rotate(359deg) brightness(99%) contrast(98%)',
    '#f5222d': 'brightness(0) saturate(100%) invert(24%) sepia(95%) saturate(3180%) hue-rotate(346deg) brightness(98%) contrast(94%)',
  }

  return colorFilters[color] || 'none'
}
</script>

<style scoped>
.svg-icon {
  display: inline-block;
  vertical-align: -0.15em;
  fill: currentColor;
  color: currentColor;
  overflow: hidden;
  transition: all 0.3s ease;
  object-fit: contain;
}

.svg-icon use {
  fill: currentColor;
  color: currentColor;
}

.svg-external-icon {
  mask-size: cover !important;
  transition: background-color 0.3s ease;
}

/* 支持图片模式的图标 */
img.svg-icon {
  /* filter 由 JavaScript 动态设置 */
}
</style>
