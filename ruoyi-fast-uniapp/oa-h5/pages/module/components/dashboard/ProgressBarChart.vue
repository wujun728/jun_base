<!-- 普通横向进度条  -->
<template>
  <qiun-data-charts echartsH5 echartsApp :eopts="opts" :chartData="chartData" directory="/work/" />
  <view class="progress-label-wrapper" v-if="chartData.series.length">
    <view class="progressbar-label" :style="{ '--progress': progressLabel }">{{ progressLabel }}</view>
  </view>
</template>

<script setup lang="ts">
import { useChartData } from "./utils";

const props = defineProps<{
  config: ChartWidgetDesignData
}>();

interface ProgressBarChartResponse {
  field_name: string,
  value: number
}

const [baseChartData] = useChartData<ProgressBarChartResponse>(props.config);

const percent = computed(() => {
  if (!baseChartData.value) return 0;

  return baseChartData.value.value / props.config.options.setDimensional.targetValue;
});


const progressLabel = computed(() => Math.round(percent.value * 100) + "%");

const chartData = computed(() => {
  let barWidth = uni.upx2px(20);
  barWidth = barWidth < 10 ? 10 : barWidth;

  if (!baseChartData.value) return {
    series: []
  };

  return {
    series: [
      {
        type: "bar",
        barWidth,
        data: [1],
        barGap: "-100%",
        animation: false,
        itemStyle: {
          borderRadius: barWidth,
          color: "#f3f3f3"
        },
        emphasis: {
          disabled: true
        }
      },
      {
        type: 'bar',
        barWidth,
        data: [percent.value],
        animationDuration: 1200,
        animationDurationUpdate: 1200,
        animationEasing: "cubicOut",
        animationEasingUpdate: "cubicOut",
        itemStyle: {
          borderRadius: barWidth,
        },
        emphasis: {
          disabled: true
        }
      }
    ],
  };
});

const opts = {
  containerWidth: uni.upx2px(710),
  containerHeight: uni.upx2px(162),
  color: ["#1890FF"],
  grid: {
    left: uni.upx2px(30),
    top: uni.upx2px(60),
    right: uni.upx2px(30)
  },
  xAxis: {
    show: false,
    type: 'value',
    splitLine: { show: false },
    axisLabel: { show: false },
    axisTick: { show: false },
    axisLine: { show: false },
  },
  yAxis: {
    type: 'category',
    axisTick: { show: false },
    axisLine: { show: false },
    axisLabel: { show: false }
  }
};


</script>

<style scoped lang="scss">
@keyframes grow-to-right {
  0% {
    left: 0;
  }

  100% {
    left: calc(var(--progress) - 38rpx);
  }
}

.progress-label-wrapper {
  position: absolute;
  left: 30rpx;
  right: 30rpx;
  bottom: 44rpx;
}

.progressbar-label {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 76rpx;
  height: 32rpx;
  background: $uni-color-primary;
  border-radius: 32rpx;

  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22rpx;
  color: #fff;

  animation: grow-to-right 1.2s .2s forwards cubic-bezier(0.215, 0.61, 0.355, 1);

  &::before {
    content: "";
    width: 10rpx;
    height: 10rpx;
    background-color: $uni-color-primary;
    position: absolute;
    left: 50%;
    top: 0;
    transform: translate(-50%, -50%) rotate(45deg);
  }
}
</style>