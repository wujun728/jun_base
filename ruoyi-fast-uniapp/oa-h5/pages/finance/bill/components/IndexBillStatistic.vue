<template>
  <view class="statistic-wrapper">
    <view class="statistic-bg" />
    <view class="nav-bar-wrapper">
      <DefaultNavBar color="#fff" backgroundColor="transparent" class="nav-bar" default-title="收支统计" />
    </view>
    <view class="statistic-body">
      <IndexBillStatisticFilter @change="handleFilterChange" />
      <view class="statistic-card">
        <view class="card-title">统计简报</view>
        <IndexBillStatisticTotal :census="totalInfo.census" />
      </view>
      <view class="statistic-card">
        <view class="card-title">收入占比统计</view>
        <template v-if="totalInfo.incomeRank.length">
          <IndexBillStatisticChart :filter="filterParams" :data="totalInfo.incomeRank"
            topBreadcrumbText="收入" default-type="1" />
        </template>
        <view class="statistic-empty" v-else>
          <image src="@/static/image/empty10.png" class="empty-img" />
          <text class="empty-tips">暂无收入统计图</text>
        </view>
      </view>
      <view class="statistic-card">
        <view class="card-title">支出占比统计</view>
        <template v-if="totalInfo.expendRank.length">
          <IndexBillStatisticChart :filter="filterParams" :data="totalInfo.expendRank"
            topBreadcrumbText="支出" default-type="0" />
        </template>
        <view class="statistic-empty" v-else>
          <image src="@/static/image/empty10.png" class="empty-img" />
          <text class="empty-tips">暂无支出统计图</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts" setup>
import DefaultNavBar from "@/components/defaultNavBar/index.vue";
import IndexBillStatisticFilter from "./IndexBillStatisticFilter.vue";
import IndexBillStatisticTotal from "./IndexBillStatisticTotal.vue";
import IndexBillStatisticChart from "./IndexBillStatisticChart.vue";

import { BillInComeAndExpendTypes } from "../hooks/useBillFilterIncomeAndExpend";

import { financeBillStatisticTotalApi } from "@/api/finance";
import type { FilterContent } from "../types/filter";

const filterParams = reactive<FilterContent>({
  type: BillInComeAndExpendTypes.ALL,
  cateId: [],
  time: ""
});

const totalInfo = reactive<{
  census: BillStatisticCensus,
  expendRank: BillStatisticRankItem[],
  incomeRank: BillStatisticRankItem[],
}>({
  census: {
    income: 0,
    expend: 0,
    count: 0,
    profit: 0
  },
  expendRank: [],
  incomeRank: []
});

const handleFilterChange = (filterContent: FilterContent) => {
  Object.assign(filterParams, filterContent);
}

const handleRequestData = async () => {
  const {
    time,
    type,
    cateId: cate_id
  } = filterParams;

  const result = await financeBillStatisticTotalApi({
    time,
    type,
    cate_id
  });

  Object.assign(totalInfo, result.data);
};

watch(
  filterParams,
  handleRequestData
)

</script>

<style scoped lang="scss">
.nav-bar-wrapper {
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  padding-top: var(--status-bar-height);
  z-index: 1;
  background-color: #308bf8;
}

.statistic-bg {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: calc(492rpx + var(--status-bar-height));
  $status-bar-real-height: calc(var(--status-bar-height) + #{$uni-default-bar-height});
  background-image: linear-gradient(to bottom, #308bf8 0%, #308bf8 #{$status-bar-real-height}, whitesmoke 100%);
}

.statistic-body {
  position: relative;
  padding: 10rpx 20rpx 28rpx;
}

.statistic-card {
  border-radius: 16rpx;
  background-color: #fff;
  margin-top: 20rpx;
}

.card-title {
  height: 82rpx;
  display: flex;
  align-items: center;
  font-weight: bold;
  background: url(@/static/image/per-b.png) no-repeat center / cover;

  &::before {
    content: "";
    width: 4rpx;
    height: 26rpx;
    background: #308BF8;
    margin-right: 20rpx;
  }
}

.statistic-empty {
  height: 554rpx;
  text-align: center;

  .empty-img {
    margin-top: 100rpx;
    width: 264rpx;
    height: 248rpx;
  }

  .empty-tips {
    display: block;
    color: #999999;
    font-size: 26rpx;
    margin-top: 16rpx;
  }
}
</style>
