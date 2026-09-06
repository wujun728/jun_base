<template>
  <BaseContainer class="relative">
    <view class="top-background fixed">
      <view class="top-background-mask" />
    </view>
    <view class="navbar-wrapper fixed">
      <DefaultNavBar :defaultTitle="navBarTitle" backgroundColor="transparent" color="#fff" />
    </view>
    <view class="dashboard-body">
      <view class="widget-item-wrapper" v-for="item of combineWidgetConfigList" :key="item.id" :class="item.css">
        <view class="widget-name">{{ item.label }}</view>
        <component :is="item.comp" :config="item.config" />
      </view>
    </view>
  </BaseContainer>
</template>

<script setup lang="ts">
import { onLoad } from "@dcloudio/uni-app";
import type { Component } from "vue";

import BaseContainer from "@/components/BaseContainer/index.vue";
import DefaultNavBar from "@/components/defaultNavBar/index.vue";
import ColumnChart from "./components/dashboard/ColumnChart.vue";
import FunnelChart from "./components/dashboard/FunnelChart.vue";
import LineChart from "./components/dashboard/LineChart.vue";
import PieChart from "./components/dashboard/PieChart.vue";
import RadarChart from "./components/dashboard/RadarChart.vue";
import BarChart from "./components/dashboard/BarChart.vue";
import StatisticChart from "./components/dashboard/StatisticChart.vue";
import RingProgressBarChart from "./components/dashboard/RingProgressBarChart.vue";
import ProgressBarChart from "./components/dashboard/ProgressBarChart.vue";
import LiquidFillChart from "./components/dashboard/LiquidFillChart.vue";
import ListTableChart from "./components/dashboard/ListTableChart.vue";

import { crudModuleDashboardDesignApi } from "@/api/crud";

const navBarTitle = ref(" ");

const dashboardBaseData = reactive<{
  dashboardId: string,
  designData: ChartWidgetDesignData[]
}>({
  dashboardId: null,
  designData: []
});

interface CombineWidgetConfig {
  id: string,
  comp: Component,
  css?: any,
  label: string,
  config: ChartWidgetDesignData
}

const combineWidgetConfigList = computed<CombineWidgetConfig[]>(() => {
  return dashboardBaseData.designData.map(config => {
    let css, comp;

    const { chartStyle, label } = config.options;

    switch (config.type) {
      case "statistic":
        // 统计数值
        comp = StatisticChart;
        break;
      case "progressbar":
        if (chartStyle === 1) {
          // 环形进度条
          comp = RingProgressBarChart;
          css = ["h-454"];
        } else if (chartStyle === 2) {
          // 横向进度条
          comp = ProgressBarChart;
          css = ["h-228"];
        } else if (chartStyle === 3) {
          // 水波图
          comp = LiquidFillChart;
          css = ["h-454"];
        }
        break;
      case "barChart":
        // 柱状图
        comp = ColumnChart;
        break;
      case "barXChart":
        // 条形图
        comp = BarChart;
        css = ["h-512"];
        break;
      case "lineChart":
        // 折线图
        comp = LineChart;
        break;
      case "funnelChart":
        // 漏斗图
        comp = FunnelChart;
        break;
      case "pieChart":
        // 饼状图
        comp = PieChart;
        break;
      case "radarChart":
        // 雷达图
        comp = RadarChart;
        css = ["h-512"];
        break;
      case "listTable":
      default:
        // 数据列表
        comp = ListTableChart;
        css = ["fit-content"];
        break;
    }

    return {
      id: config.id,
      comp,
      label,
      css,
      config
    };
  });
});

onLoad(async (options: PageLoadOptions) => {
  if (options.id) {
    dashboardBaseData.dashboardId = options.id;
    let { chartData, name } = (await crudModuleDashboardDesignApi(dashboardBaseData.dashboardId)).data;
    navBarTitle.value = name;
    dashboardBaseData.designData = JSON.parse(chartData).widgetList[0].widgetList;
  }
});

</script>

<style scoped lang="scss">
.fixed {
  position: fixed;
}

.top-background {
  top: 0;
  left: 0;
  width: 100%;
  height: 546rpx;
  background: url(@/static/image/module-dashboard-nav-bg.png) no-repeat top left / 100% auto;
  display: flex;
  align-items: flex-end;
}

.top-background-mask {
  width: 100%;
  background: linear-gradient(180deg, rgba(245, 245, 245, 0) 0%, #F5F5F5 85%, #F5F5F5 100%);
  height: 348rpx;
}

.navbar-wrapper {
  top: 0;
  left: 0;
  width: 100%;
  padding-top: var(--status-bar-height);
  z-index: 1;
  background: url(@/static/image/module-dashboard-nav-bg.png) no-repeat top left / 100% auto;
}

.dashboard-body {
  $padding-top: calc(20rpx + #{$uni-default-bar-height} + var(--status-bar-height));
  padding: #{$padding-top} 20rpx calc(20rpx + var(--bottom-area-height));
  position: relative;
}

.widget-item-wrapper {
  background: #fff;
  border-radius: 16rpx;
  height: 504rpx;
  display: flex;
  flex-flow: column nowrap;
  position: relative;

  &.h-512 {
    height: 512rpx;
  }

  &.h-454 {
    height: 454rpx;
  }

  &.h-228 {
    height: 228rpx;
  }

  &.h-596 {
    height: 596rpx;
  }

  &.fit-content {
    height: fit-content;
  }

  &+.widget-item-wrapper {
    margin-top: 20rpx;
  }

  .widget-name {
    padding: {
      top: 24rpx;
      left: 24rpx;
      // bottom: 20rpx;
    }

    font-weight: 500;
    font-size: 30rpx;
    line-height: 42rpx;
  }
}
</style>
