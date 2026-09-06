<!-- 饼状图 / 环形图 -->
<template>
  <qiun-data-charts :type="chartType" :opts="opts" :chartData="chartData" tooltipFormat="tooltipFormatter" />
</template>

<script setup lang="ts">
import { useChartData } from "./utils";
import { baseOptions } from "./config";

const props = defineProps<{
  config: ChartWidgetDesignData
}>();

// 是否环形图
const isRingChart = computed(() => props.config.options.chartStyle === 2);

const chartType = computed(() => isRingChart.value ? "ring" : "pie");

const [baseChartData] = useChartData<FunnelChartDataResponse>(props.config);

const chartData = computed<FunnelChartData>(() => {
  let series: FunnelChartDataSeries[] = [];

  if (baseChartData.value) {
    const data: FunnelChartDataSeriesDataItem[] = baseChartData.value.map(({ name, value }) => {
      return {
        name: name || " ",
        value,
        legendShape: "roundedRect",
        labelTips: name,
        labelText: value + ""
      };
    });

    series.push({
      data
    });
  }

  return {
    series
  };
});

const opts = computed(() => {

  return {
    ...baseOptions,
    fontSize: uni.upx2px(24),
    legend: {
      ...baseOptions.legend,
      show: props.config.options.setChartConf.chartShow
    },
    dataLabel: true,
    extra: {
      ...baseOptions.extra,
      tooltip: {
        ...baseOptions.extra.tooltip,
        showCategory: false,
      }
    }
  };
});


</script>

<style scoped lang="scss"></style>