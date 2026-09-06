<template>
  <view class="chart-wrapper">
    <view class="chart-inner-wrapper">
      <view class="breadcrumb-list" @click="handleBreadcrumbClick">
        <template v-for="(item, index) in breadcrumbCombineConfig" :key="item.cateId">
          <view class="iconfont icon-jinru-copy" v-if="index" />
          <view class="breadcrumb-item" :data-cate-id="item.cateId">
            {{ item.name }}
          </view>
        </template>
      </view>
      <view class="chart-body">
        <qiun-data-charts echartsH5 echartsApp :eopts="eopts" :chartData="chartData" directory="/work/"
          @getIndex="handleChartClick" />
      </view>
    </view>
    <view class="cate-list">
      <view class="cate-item" v-for="(item, index) of baseChartData.data" :key="item.id">
        <view class="cate-item-value over-text">{{ Number(item.value).toFixed(2) }}</view>
        <view class="cate-item-name" :style="{ '--dot-color': eopts.color[index % eopts.color.length] }">{{ item.name }}
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts" setup>
  import { financeBillStatisticChartApi } from '@/api/finance';
  import type { FilterContent } from '../types/filter';


  const props = defineProps<{
    data : BillStatisticRankItem[],
    topBreadcrumbText : string,
    filter : FilterContent,
    defaultType : string
  }>();

  interface BreadcurmbItem {
    name : string,
    cateId : number
  }

  const generateSeriesFromRankData = (rankData : BillStatisticRankItem[]) => {
    return rankData.map(i => {
      return {
        value: Number(i.sum),
        id: i.cate_id,
        name: i.name
      };
    });
  }

  const initDataFromProps = () => {
    return {
      data: generateSeriesFromRankData(props.data),
      currentCateId: DEFAULT_TOP_CATE_ID,
      currentCateName: props.topBreadcrumbText
    }
  }

  const DEFAULT_TOP_CATE_ID = 0;
  const baseChartData = reactive(initDataFromProps());
  const breadcrumbConfig = ref<BreadcurmbItem[]>([]);
  const breadcrumbCombineConfig = computed<BreadcurmbItem[]>(() => {
    return [
      {
        name: props.topBreadcrumbText,
        cateId: DEFAULT_TOP_CATE_ID
      },
      ...breadcrumbConfig.value
    ]
  });


  const eopts = computed(() => {

    const title = baseChartData.data.reduce((sum, item) => sum + item.value, 0);

    return {
      containerWidth: uni.upx2px(662),
      containerHeight: uni.upx2px(452),
      color: [
        "#308bfb",
        "#ff9900",
        "#a277ff",
        "#19be6b",
      ],
      title: {
        text: (title / 10000).toFixed(2) + "万",
        left: "center",
        top: "40%",
        textStyle: {
          fontSize: 18,
          color: "#303133",
        },
        subtext: "总计合同金额(元)",
        subtextStyle: {
          fontSize: 12,
          color: '#909399',
        }
      }
    };
  });

  const chartData = computed(() => {

    return {
      series: [
        {
          name: baseChartData.currentCateName,
          type: "pie",
          radius: [
            "48%",
            "75%"
          ],
          label: {
            overflow: "none",
            formatter: "{d}%\n{b}",
            color: "inherit",
            lineHeight: 17,
            // alignTo: "labelLine"
          },
          labelLine: {
            show: true,
            showAbove: true,
          },
          labelLayout: {
            moveOverlap: true
          },
          emphasis: {
            disabled: true
          },
          data: baseChartData.data,
          animationThreshold: 30000
        }
      ]
    };
  });


  const handleUpdateChartData = async (cateId : number, cateName : string) => {
    const {
      time,
      type
    } = props.filter;
    const { data } = await financeBillStatisticChartApi({
      time,
      types: Number(type || props.defaultType),
      cate_id: cateId
    });

    Object.assign(baseChartData, {
      data: generateSeriesFromRankData(data),
      currentCateId: cateId,
      currentCateName: cateName
    });
  }

  const handleChartClick = async (e : EchartIndexEvent) => {
    const cateIsAlreadyExists = !!breadcrumbConfig.value.find(i => i.cateId === e.value.id);
    if (cateIsAlreadyExists) return;

    breadcrumbConfig.value.push({
      name: e.value.name,
      cateId: e.value.id
    });

    await handleUpdateChartData(e.value.id, e.value.name);
  }


  // 父级数据发生变化时, 恢复初始状态
  const restoreDataFromProps = () => {
    Object.assign(baseChartData, initDataFromProps());
    breadcrumbConfig.value = [];
  }

  const handleBreadcrumbClick = async (e : Event) => {
    if ((e.target as HTMLDialogElement).dataset.cateId === undefined) return;
    const cateId = Number((e.target as HTMLDialogElement).dataset.cateId);
    if (cateId === baseChartData.currentCateId) return;

    if (cateId === DEFAULT_TOP_CATE_ID) {
      restoreDataFromProps();
      return;
    }

    const cateIndex = breadcrumbConfig.value.findIndex(cate => cate.cateId === cateId);
    const cate = breadcrumbConfig.value[cateIndex];
    if (!cate) return;
    const { name } = cate;

    await handleUpdateChartData(cateId, name);
    breadcrumbConfig.value = breadcrumbConfig.value.slice(0, cateIndex + 1);
  }

  watch(
    () => props.data,
    restoreDataFromProps
  )
</script>

<style scoped lang="scss">
  .chart-wrapper {
    padding: 40rpx 0 30rpx;
  }

  .chart-inner-wrapper {
    padding: 0 24rpx;
  }

  .breadcrumb-list {
    display: flex;
    font-size: 28rpx;
    color: $uni-color-primary;
    line-height: 40rpx;

    .icon-jinru-copy {
      font-size: 20rpx;
      color: #C0C4CC;
      margin: 0 14rpx;
    }

    .breadcrumb-item {
      &:not(:first-child):last-child {
        color: #909399;
      }
    }
  }

  .chart-body {
    height: 452rpx;
  }

  .cate-list {
    display: flex;
    flex-flow: row wrap;
    gap: 30rpx 0;
  }

  .cate-item {
    width: 33.33%;
    height: 92rpx;
    padding-left: 34rpx;

    .cate-item-value {
      margin-top: 6rpx;
      font-weight: 500;
      font-size: 34rpx;
      line-height: 48rpx;
    }

    .cate-item-name {
      margin-top: 4rpx;
      font-size: 24rpx;
      color: #909399;
      line-height: 24rpx;

      display: flex;
      align-items: center;

      &::before {
        content: "";
        width: 10rpx;
        height: 10rpx;
        background-color: var(--dot-color);
        border-radius: 10rpx;
        margin-right: 6rpx;
      }
    }
  }
</style>