<template>
  <div
    ref="chartRef"
    class="relative w-full"
    :style="{ height: props.height }"
    v-loading="props.loading"></div>
</template>

<script setup lang="ts">
import type { EChartsOption } from '@/plugins/echarts'
import type { GaugeChartProps } from '@/types/component/chart'

import { useChartOps, useChartComponent } from '@/hooks/core/useChart'

defineOptions({ name: 'ArtGaugeChart' })

const props = withDefaults(defineProps<GaugeChartProps>(), {
  height: useChartOps().chartHeight,
  loading: false,
  isEmpty: false,
  colors: () => useChartOps().colors,
  value: 0,
  name: '数据',
  min: 0,
  max: 100,
  unit: '',
  detailText: '',
  startAngle: 210,
  endAngle: -30,
  radius: '85%',
  center: () => ['50%', '55%'],
  splitNumber: 5,
  showTooltip: true
})

const { chartRef, isDark, getAnimationConfig, getTooltipStyle } =
  useChartComponent({
    props,
    checkEmpty: () => !Number.isFinite(props.value),
    watchSources: [
      () => props.value,
      () => props.name,
      () => props.min,
      () => props.max,
      () => props.unit,
      () => props.detailText,
      () => props.colors,
      () => props.showTooltip
    ],
    generateOptions: (): EChartsOption => {
      const mainColor = props.colors[0]
      const value = Math.min(Math.max(props.value, props.min), props.max)
      const detailText = props.detailText || `${props.value}${props.unit}`

      return {
        tooltip: props.showTooltip
          ? getTooltipStyle('item', {
              formatter: () => `${props.name}<br/>${detailText}`
            })
          : undefined,
        series: [
          {
            name: props.name,
            type: 'gauge',
            min: props.min,
            max: props.max,
            radius: props.radius,
            center: props.center,
            startAngle: props.startAngle,
            endAngle: props.endAngle,
            splitNumber: props.splitNumber,
            progress: {
              show: true,
              roundCap: true,
              width: 14,
              itemStyle: {
                color: mainColor || useChartOps().themeColor
              }
            },
            axisLine: {
              roundCap: true,
              lineStyle: {
                width: 14,
                color: [
                  [1, isDark.value ? 'rgba(255, 255, 255, 0.12)' : '#edf0f7']
                ]
              }
            },
            axisTick: {
              show: false
            },
            splitLine: {
              length: 8,
              distance: 4,
              lineStyle: {
                width: 2,
                color: isDark.value ? '#555' : '#d8dbe6'
              }
            },
            axisLabel: {
              distance: 20,
              color: isDark.value ? '#999' : '#8a8f9f',
              fontSize: 11
            },
            pointer: {
              show: true,
              length: '58%',
              width: 5,
              itemStyle: {
                color: mainColor || useChartOps().themeColor
              }
            },
            anchor: {
              show: true,
              showAbove: true,
              size: 10,
              itemStyle: {
                color: mainColor || useChartOps().themeColor
              }
            },
            title: {
              offsetCenter: [0, '72%'],
              color: isDark.value ? '#aaa' : '#8a8f9f',
              fontSize: 13,
              fontWeight: 400
            },
            detail: {
              valueAnimation: true,
              offsetCenter: [0, '45%'],
              formatter: detailText,
              color: isDark.value ? '#ddd' : '#303133',
              fontSize: 22,
              fontWeight: 600
            },
            data: [
              {
                value,
                name: props.name
              }
            ],
            ...getAnimationConfig()
          }
        ]
      }
    }
  })
</script>
