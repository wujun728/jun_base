<!-- 水波图  -->
<template>
  <qiun-data-charts echartsH5 echartsApp :eopts="eopts" :chartData="chartData" directory="/work/" />
</template>

<script setup lang="ts">
import { useChartData } from "./utils";

const props = defineProps<{
  config: ChartWidgetDesignData
}>();


interface LiquidFillChartResponse {
  field_name: string,
  value: number
}

const [baseChartData] = useChartData<LiquidFillChartResponse>(props.config);

const percent = computed(() => {
  if (!baseChartData.value) return 0;
  return baseChartData.value.value / props.config.options.setDimensional.targetValue;
});

const eopts = {
  containerWidth: uni.upx2px(710),
  containerHeight: uni.upx2px(388)
};

const chartData = computed(() => {
  let barWidth = uni.upx2px(20);
  barWidth = barWidth < 10 ? 10 : barWidth;

  if (!baseChartData.value) return {
    series: []
  }

  const progressLabel = Math.round(percent.value * 100) + "%";

  return {
    series: [
      {
        type: 'liquidFill',
        radius: '76%',
        center: [
          '50%',
          '50%'
        ],
        itemStyle: {
          opacity: 0.95,
          shadowColor: 'rgba(0, 0, 0, 0)'
        },
        amplitude: 10,
        data: [
          percent.value,
          percent.value,
          percent.value,
        ],
        color: [
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: 'rgba(24, 144, 255, 0.5)'
              },
              {
                offset: 1,
                color: '#79BEFF'
              }
            ],
            globalCoord: false
          }
        ],
        backgroundStyle: {
          borderWidth: 1,
          color: '#fff'
        },
        label: {
          position: [
            '50%',
            '75%'
          ],
          formatter: progressLabel,
          fontSize: uni.upx2px(36),
          color: '#59AFFF',
          show: true
        },
        outline: {
          borderDistance: 0,
          itemStyle: {
            borderWidth: 2,
            borderColor: '#59AFFF'
          }
        }
      }
    ]
  };
});

</script>

<style scoped lang="scss"></style>