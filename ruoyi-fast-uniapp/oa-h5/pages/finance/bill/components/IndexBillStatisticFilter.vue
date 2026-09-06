<template>
  <view class="statistic-filter-wrapper">
    <view class="filter-item-wrapper income-type">
      <picker mode="selector" :range="inComeAndExpendTypeConfig" range-key="name"
        @change="handleIncomeAndExpendTypeChange">
        <view class="filter-item">
          <text class="iconfont icon-yeji-shijianduan" />
          {{ inComeAndExpendTypeText }}
          <text class="iconfont icon-zhankai1" />
        </view>
      </picker>
    </view>
    <view class="filter-item-wrapper bill-cate" @click="handleShowBillCatePicker">
      <view class="filter-item">
        <text class="iconfont icon-yeji-shijianduan" />
        <view class="over-text">
          {{ billCateNames || DEFAULT_CATE_TEXT }}
        </view>
        <text class="iconfont icon-zhankai1" />
      </view>
    </view>
    <view class="filter-item-wrapper bill-date-range">
      <uni-datetime-picker type="daterange" :clear-icon="false" :border="false"
        @change="handleSelectDateRange" :modelValue="currentMonthRange">
        <view class="filter-item">
          <text class="iconfont icon-yeji-shijianduan" />
          <view class="over-text">
            {{ dateRange || "收支日期" }}
          </view>
          <text class="iconfont icon-zhankai1" />
        </view>
      </uni-datetime-picker>
    </view>


    <BaTreePicker ref="billCateRef" :localdata="billCateList" @select-change="handleBillCateChange" selectShow
      :clearTree="false" allowEmpty />
  </view>
</template>


<script setup lang="ts">
import BaTreePicker from "@/components/baTreePicker/index.vue";

import { useBillFilterIncomeAndExpend } from '../hooks/useBillFilterIncomeAndExpend';
import { useBillCate } from "../hooks/useBillCate";
import { useBillFilterDate } from "../hooks/useBillFilterDate";

import { FilterContent } from "../types/filter";
import { getCurrentMonthRange } from "@/utils/date";

const DEFAULT_CATE_TEXT = "账目分类";
const currentMonthRange = getCurrentMonthRange();
const billCateRef = ref();

const [
  inComeAndExpendType,
  inComeAndExpendTypeText,
  inComeAndExpendTypeConfig,
  handleIncomeAndExpendTypeChange
] = useBillFilterIncomeAndExpend();

const [
  billCateList,
  billCateIds,
  billCateNames,
  handleBillCateChange
] = useBillCate(true, DEFAULT_CATE_TEXT, inComeAndExpendType);

const [dateRange, handleSelectDateRange] = useBillFilterDate(currentMonthRange.join("-"));

const handleShowBillCatePicker = () => billCateRef.value.show();

const emit = defineEmits<{
  change: [content: FilterContent]
}>();

watch(
  [
    inComeAndExpendType,
    billCateIds,
    dateRange
  ],
  () => {
    emit("change", {
      type: inComeAndExpendType.value,
      cateId: billCateIds.value as number[][],
      time: dateRange.value
    });
  },
  {
    immediate: true
  }
)

</script>

<style scoped lang="scss">
.statistic-filter-wrapper {
  background-color: rgba($color: #fff, $alpha: .12);
  height: 70rpx;
  border-radius: 8rpx;

  display: flex;
  align-items: center;
}

.filter-item-wrapper {
  display: flex;
  justify-content: center;
  overflow: hidden;

  color: #fff;
  font-size: 26rpx;

  .icon-yeji-shijianduan {
    font-size: 28rpx;
    margin: 0 6rpx;
  }

  .icon-zhankai1 {
    font-size: 16rpx;
    margin: 0 8rpx;
  }

  &.income-type {
    width: 184rpx;
  }

  &.bill-cate {
    width: 238rpx;
    border-left: 1px solid rgba($color: #fff, $alpha: .4);
    border-right: 1px solid rgba($color: #fff, $alpha: .4);
  }

  &.bill-date-range {
    flex: 1;
  }
}

.filter-item {
  display: flex;
  align-items: center;

  min-width: 0;
}
</style>
