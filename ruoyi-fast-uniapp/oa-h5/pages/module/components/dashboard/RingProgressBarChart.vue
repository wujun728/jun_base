<!-- 环状进度条  -->
<template>
  <qiun-data-charts type="arcbar" :opts="opts" :chartData="chartData" tooltipFormat="tooltipFormatter" />
</template>

<script setup lang="ts">
import { baseOptions } from "./config";
import { useChartData } from "./utils";

const props = defineProps<{
  config: ChartWidgetDesignData
}>();



interface RingProgressBarChartResponse {
  field_name: string,
  value: number
}

const [baseChartData] = useChartData<RingProgressBarChartResponse>(props.config);

const percent = computed(() => {
  if (!baseChartData.value) return 0;

  return baseChartData.value.value / props.config.options.setDimensional.targetValue;
});

const chartData = computed(() => {
  const series = [];

  if (baseChartData.value) {
    series.push({
      name: baseChartData.value.field_name,
      data: percent.value
    });
  }

  return {
    series
  };
});

const opts = computed(() => {

  if (!baseChartData.value) {
    return baseOptions;
  }

  return {
    ...baseOptions,
    title: {
      name: Math.round(percent.value * 100) + "%",
      fontSize: uni.upx2px(40),
      color: "#909399"
    }
  };
});


</script>

<style scoped lang="scss"></style>