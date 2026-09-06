<!-- 雷达图 -->
<template>
  <qiun-data-charts type="radar" :opts="opts" :chartData="chartData" tooltipFormat="tooltipFormatter" />
</template>

<script setup lang="ts">
import { useChartData } from "./utils";
import { baseOptions } from "./config";
import { addLegendShapeToArrObjs, getMaxValue } from "./utils";

const props = defineProps<{
  config: ChartWidgetDesignData
}>();

const [baseChartData] = useChartData<CommonChartDataResponse>(props.config);

const chartData = computed<ColumnChartData>(() => {
  let categories = <any>[];
  let series = <any>[];

  if (baseChartData.value) {
    const { xAxis, series: seriesResponse } = baseChartData.value;
    categories = xAxis;
    addLegendShapeToArrObjs(seriesResponse);
    series = seriesResponse;
  }

  return {
    categories,
    series
  }
});



const opts = computed(() => {
  let maxValue = 0;

  if (chartData.value.categories.length) {
    maxValue = getMaxValue(chartData.value.series.map(i => i.data).flat());
  }

  return {
    ...baseOptions,
    legend: {
      ...baseOptions.legend,
      show: props.config.options.setChartConf.chartShow
    },
    extra: {
      ...baseOptions.extra,
      radar: {
        ...baseOptions.extra.radar,
        max: maxValue + maxValue / 3
      }
    }
  };
});


</script>

<style scoped lang="scss"></style>