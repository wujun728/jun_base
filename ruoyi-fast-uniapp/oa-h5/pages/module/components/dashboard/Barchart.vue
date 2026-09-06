<!-- 条形图 / 堆叠条形图 -->
<template>
  <qiun-data-charts type="bar" :opts="opts" :chartData="chartData" tooltipFormat="tooltipFormatter" />
</template>

<script setup lang="ts">
import { useChartData } from "./utils";
import { baseOptions } from "./config";
import { addLegendShapeToArrObjs, calcXAxisIntMaxValue, getMaxValue } from "./utils";

const props = defineProps<{
  config: ChartWidgetDesignData
}>();

const isStackBarChart = computed(() => props.config.options.chartStyle === 2);


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

const getBarChartMaxValue = () => getMaxValue(chartData.value.series.map(i => i.data).flat());

const getStackBarChartMaxValue = () => {
  const sumByCategory = chartData.value.categories.map((_, index) => {
    return chartData.value.series.reduce((sum, item) => sum + item.data[index], 0);
  });

  return getMaxValue(sumByCategory);
}

const opts = computed(() => {
  let maxXAxisValue = 0;

  if (chartData.value.categories.length) {
    // 堆叠柱状图采用不同的处理方式
    const maxDataValue = isStackBarChart.value ? getStackBarChartMaxValue() : getBarChartMaxValue();
    maxXAxisValue = (Math.floor(maxDataValue / 10) + 1) * 10;
  }

  const barConfig = { ...baseOptions.extra.bar };

  if (isStackBarChart.value) {
    Object.assign(barConfig, {
      type: "stack",
      width: uni.upx2px(20)
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
      bar: barConfig
    },
    xAxis: {
      ...baseOptions.xAxis,
      axisLine: true,
      gridType: "dash",
      dashLength: 10,
      gridColor: "#E5E6EB",
      tofix: 2,
      max: calcXAxisIntMaxValue(maxXAxisValue),
      splitNumber: 7,
      splitNumberMarginLeft: uni.upx2px(22),
      splitNumberMarginRight: uni.upx2px(54),
      axisLineOffsetLeft: uni.upx2px(30)
    },
    yAxis: {
      data: [
        {
          type: "categories",
          axisLine: false
        }
      ]
    }
  };
});

</script>

<style scoped lang="scss"></style>