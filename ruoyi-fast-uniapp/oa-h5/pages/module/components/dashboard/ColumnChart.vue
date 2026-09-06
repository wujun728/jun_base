<!-- 柱状图 / 堆叠柱状图  -->
<template>
  <qiun-data-charts type="column" :opts="opts" :chartData="chartData" tooltipFormat="tooltipFormatter" />
</template>

<script setup lang="ts">
import { useChartData } from "./utils";
import { baseOptions } from "./config";
import { addLegendShapeToArrObjs } from "./utils";

const props = defineProps<{
  config: ChartWidgetDesignData
}>();

const isStackColumnChart = computed(() => props.config.options.chartStyle === 2);


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
  const column = {
    ...baseOptions.extra.column
  };

  if (isStackColumnChart.value) {
    Object.assign(column, {
      type: "stack",
      width: uni.upx2px(20)
    });
  }

  const yAxisData = [];

  if (baseChartData.value) {
    const { yAxis } = baseChartData.value;
    yAxisData.push({
      axisLine: false,
      title: yAxis[0],
      titleOffsetX: -14 + yAxis[0].length * 4,
      titleOffsetY: -4
    });
  }

  return {
    ...baseOptions,
    legend: {
      ...baseOptions.legend,
      show: props.config.options.setChartConf.chartShow
    },
    extra: {
      ...baseOptions.extra,
      column
    },
    yAxis: {
      ...baseOptions.yAxis,
      data: yAxisData
    }
  };
});

</script>

<style scoped lang="scss"></style>
