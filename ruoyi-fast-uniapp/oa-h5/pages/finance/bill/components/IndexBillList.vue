<template>
  <view class="index-bill-list-wrapper">
    <view class="nav-bar-wrapper">
      <DefaultNavBar color="#fff" backgroundColor="transparent" class="nav-bar" isRight :rightData="rightIconList"
        @handleNarItem="handleNavbarRightClick" />
    </view>
    <IndexBillListFilter @change="handleFilterChange" />
    <view class="index-bill-list">
      <template v-if="billList.length">
        <view class="bill-date-item" v-for="itemByDate of billList" :key="itemByDate.days">
          <view class="bill-date-info">
            <view class="bill-date">{{ itemByDate.date }}</view>
            <view class="bill-day second-color">{{ itemByDate.day }}</view>
            <view class="bill-income-expend-wrapper">
              <view class="bill-income-expend over-text">
                <text class="income-expend-type second-color">支</text>
                {{ itemByDate.expend }}
              </view>
              <view class="bill-income-expend over-text">
                <text class="income-expend-type second-color">收</text>
                {{ itemByDate.income }}
              </view>
            </view>
          </view>

          <view class="bill-item-wrapper">
            <navigator class="bill-item" v-for="itemBill of itemByDate.data" :key="itemBill.id"
              :url="`/pages/finance/bill/details?id=${itemBill.id}`" :render-link="false" hover-class="none">
              <view class="bill-item-left">
                <view class="bill-item-name over-text">
                  {{ itemBill.cate }}
                </view>
                <view class="bill-item-info second-color">
                  <view class="bill-item-time"> {{ itemBill.time }} </view>
                  <view class="bill-item-info-dividing" />
                  <view class="bill-item-pay-type">{{ itemBill.pay_type }}</view>
                </view>
              </view>
              <view class="bill-item-income-expend-price" :class="[itemBill.types === 1 ? 'income' : 'expend']">
                {{ itemBill.num }}
              </view>
            </navigator>
          </view>
        </view>
      </template>
      <view class="index-bill-list-empty" v-else>
        <image src="@/static/image/empty10.png" class="empty-img" />
        <text class="empty-tips">当前暂无统计数据</text>
      </view>
    </view>
  </view>
</template>

<script lang="ts" setup>
import DefaultNavBar from "@/components/defaultNavBar/index.vue";
import IndexBillListFilter from './IndexBillListFilter.vue';

import { useIndexBillListData } from '../hooks/useIndexBillListData';
import { useIndexNavBarConfig } from "../hooks/useIndexNavBarConfig";

const filterContent = ref<BillListFilterContent>({
  payType: 0,
  inComeAndExpendType: "",
  searchText: "",
  dateRange: "",
  billCateIds: []
});

const [rightIconList, handleNavbarRightClick] = useIndexNavBarConfig();
const [billList] = useIndexBillListData(filterContent);

const handleFilterChange = (nextFilterContent: BillListFilterContent) => {
  filterContent.value = nextFilterContent;
}
</script>

<style scoped lang="scss">
@import "@/static/css/din-condensed-font.scss";

.nav-bar-wrapper {
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  padding-top: var(--status-bar-height);
  background-image: linear-gradient(90deg, #459FFF 0%, #388AEF 100%, #3384E7 100%);
}

.second-color {
  color: #909399;
}


.index-bill-list-wrapper {
  --filter-height: 154rpx;
}

.index-bill-list {
  margin-top: var(--filter-height);
  padding: 20rpx;
  font-size: 24rpx;
}

.bill-date-item {
  background-color: #fff;
  border-radius: 14rpx;

  &+& {
    margin-top: 22rpx;
  }
}

.bill-date-info {
  display: flex;
  align-items: center;
  height: 84rpx;
  padding: 0 24rpx;
  border-bottom: 1px solid $uni-line-style-color-three;
  font-weight: 500;

  .bill-day {
    margin-left: 18rpx;
    margin-right: auto;
  }

  .bill-income-expend-wrapper {
    flex: 1;
    display: flex;
    justify-content: flex-end;
    overflow: hidden;
  }

  .bill-income-expend {
    min-width: 100rpx;
    text-align: right;
    color: #606266;
    display: flex;
    align-items: baseline;
    justify-content: flex-end;

    &+.bill-income-expend {
      margin-left: 18rpx;
    }

    .income-expend-type {
      font-weight: 400;
      margin-right: 8rpx;
    }
  }
}

.bill-item-wrapper {
  padding: 40rpx 24rpx 30rpx;
}

.bill-item {
  display: flex;
  align-items: center;

  &+& {
    margin-top: 40rpx;
  }
}

.bill-item-income-expend-price {
  font-weight: 600;
  font-size: 32rpx;
  font-family: DIN-Condensed-Bold;
  min-width: 130rpx;
  text-align: right;

  &.income {
    color: #19BE6B;

    &::before {
      content: "+";
    }
  }

  &.expend {
    color: $uni-color-two;

    &::before {
      content: "-";
    }
  }
}

.bill-item-name {
  font-weight: 500;
  font-size: 28rpx;
  line-height: 40rpx;
  margin-bottom: 12rpx;
}

.bill-item-left {
  flex: 1;
  overflow: hidden;
}

.bill-item-info {
  display: flex;
  align-items: center;
  font-size: 24rpx;
  line-height: 34rpx;
}

.bill-item-info-dividing {
  height: 18rpx;
  width: 2rpx;
  background-color: $uni-line-style-color-three;
  margin: 0 16rpx;
}

.index-bill-list-empty {
  margin-top: 90rpx;
  text-align: center;

  .empty-img {
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