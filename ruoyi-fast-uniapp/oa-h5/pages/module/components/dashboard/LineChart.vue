<!-- 折线图 -->
<template>
  <qiun-data-charts :type="chartType" :opts="opts" :chartData="chartData" tooltipFormat="tooltipFormatter" />
</template>

<script setup lang="ts">
import { useChartData } from "./utils";
import { baseOptions } from "./config";
import { addLegendShapeToArrObjs } from "./utils";

const props = defineProps<{
  config: ChartWidgetDesignData
}>();

// 是否曲线类型描边，采用区域 charts
const isCurveLineStyle = computed(() => props.config.options.chartStyle === 2);

const [baseChartData] = useChartData<CommonChartDataResponse>(props.config);

const chartType = computed(() => isCurveLineStyle.value ? "area" : "line");

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
    dataPointShape: false,
    yAxis: {
      ...baseOptions.yAxis,
      data: yAxisData
    }
  };
});

</script>

<style scoped lang="scss"></style>
