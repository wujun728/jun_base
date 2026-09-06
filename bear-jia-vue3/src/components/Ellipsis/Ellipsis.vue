<script setup>
import { computed, h } from 'vue'
import Tooltip from 'ant-design-vue/es/tooltip'
import BearJiaUtil from '@/utils/BearJiaUtil.js'

const props = defineProps({
  prefixCls: {
    type: String,
    default: 'ant-pro-ellipsis'
  },
  tooltip: {
    type: Boolean
  },
  length: {
    type: Number,
    required: true
  },
  lines: {
    type: Number,
    default: 1
  },
  fullWidthRecognition: {
    type: Boolean,
    default: false
  }
})

const slots = defineSlots()

const getStrDom = (str, fullLength) => {
  return h('span', null, [BearJiaUtil.cutStrByFullLength(str, props.length) + (fullLength > props.length ? '...' : '')])
}

const getTooltip = (fullStr, fullLength) => {
  return h(Tooltip, { title: fullStr }, {
    default: () => getStrDom(fullStr, fullLength)
  })
}

const renderContent = computed(() => {
  const str = slots.default?.().map(vNode => vNode.children).join('') || ''
  const fullLength = BearJiaUtil.getStrFullLength(str)
  return props.tooltip && fullLength > props.length ? getTooltip(str, fullLength) : getStrDom(str, fullLength)
})
</script>

<template>
  <component :is="renderContent" />
</template>
