<template>
  <div class="baseEchart">
    <div ref="echartRef" :style="{ height: height, width: width }"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, watchEffect, watch } from 'vue'
import useEchart from './hook/useEchart'
import { useConfig } from '@/store/modules/layout'
const props = defineProps({
  width: {
    type: String,
    default: '100%',
  },
  height: {
    type: String,
    default: '300px',
  },
  options: {
    type: Object,
    required: true,
  },
})
const echartRef = ref(null)
const layoutConfig = useConfig()
let myChart = void 0
let setEchartOption
let echartResize
watch(
  () => layoutConfig.layout.isDark,
  () => {
    myChart.dispose()
    initEchart()
    setEchartOption?.(props.options, true)
  }
)
const initEchart = () => {
  let isDark = layoutConfig.layout.isDark ? 'customDark' : void 0
  const { setOption, updateResize, eachartInstance } = useEchart(
    echartRef.value,
    isDark
  )
  echartResize = updateResize
  setEchartOption = setOption
  myChart = eachartInstance
}
onMounted(() => {
  if (echartRef.value) {
    initEchart()
    watchEffect(() => {
      setEchartOption?.(props.options, true)
    })
    watch(
      () => props.height,
      () => {
        nextTick(() => {
          echartResize?.()
        })
      },
      {
        immediate: true,
      }
    )
  }
})
</script>
