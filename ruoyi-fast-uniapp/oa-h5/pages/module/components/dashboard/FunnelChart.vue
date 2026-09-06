<!-- 漏斗图 -->
<template>
  <qiun-data-charts type="funnel" :opts="opts" :chartData="chartData" tooltipFormat="tooltipFormatter" />
</template>

<script setup lang="ts">
import { useChartData } from "./utils";
import { baseOptions } from "./config";

const props = defineProps<{
  config: ChartWidgetDesignData
}>();

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
    }).sort((a, b) => b.value - a.value);

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
    dataLabel: true,
    legend: {
      ...baseOptions.legend,
      show: props.config.options.setChartConf.chartShow
    },
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
