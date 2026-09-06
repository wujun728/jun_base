<template>
  <view class="statistic-chart-wrapper" :style="styles">
    <view class="inner-wrapper" v-if="statisticCount != ''">
      <view class="count-symbol" v-if="styleConfig">{{ styleConfig.currencySymbol }}</view>
      {{ statisticCount }}
      <view class="count-unit">{{ numericUnit }}</view>
    </view>
  </view>
</template>

<script lang="ts" setup>
import type { StyleValue } from "vue";
import { useChartData, numberToCurrencyNo } from "./utils";

const props = defineProps<{
  config: ChartWidgetDesignData
}>();

interface StatisticChartResponse {
  field_name: string,
  value: number
}

const [chartData] = useChartData<StatisticChartResponse>(props.config);

const statisticCount = computed<string>(() => {
  if (chartData.value) {
    let value = Number(chartData.value.value);
    let valueStr = value + "";

    let { metrics } = props.config.options.setDimensional;

    if (metrics.length > 0) {
      const { thousandsSeparator, showDecimalPlaces, decimalPlaces } = metrics[0];
      if (showDecimalPlaces) {
        valueStr = value.toFixed(decimalPlaces);
      }
      if (thousandsSeparator) {
        valueStr = numberToCurrencyNo(value);
      }
    }

    return valueStr;
  };
  return '';
});

const numericUnit = computed<string>(() => {
  let { metrics } = props.config.options.setDimensional;
  if (metrics.length < 1) {
    return "";
  }
  return metrics[0].numericUnits == '无' ? '' : metrics[0].numericUnits;
});

const styleConfig = computed(() => props.config.options.setChartStyle);

const styles = computed<StyleValue>(() => {
  let numericSize = 60;

  const numericLen = statisticCount.value.toString().length;

  if (numericLen > 10) {
    numericSize = 40;
  } else if (numericLen > 8) {
    numericSize = 50;
  }

  return {
    "--color": props.config.options.setChartStyle.useTextColor,
    "--symbol-size": props.config.options.setChartStyle.currencySymbolSize,
    "--numeric-size": numericSize
  }
});

</script>

<style scoped lang="scss">
.statistic-chart-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  font-size: calc(var(--numeric-size) * 1px);
  color: var(--color);

  .inner-wrapper {
    display: flex;
    align-items: baseline;
  }

  .count-symbol {
    font-size: calc(var(--symbol-size) * 2px);
    margin-right: 16rpx;
  }

  .count-unit {
    margin-left: 16rpx;
    font-size: 16px;
  }
}
</style>
