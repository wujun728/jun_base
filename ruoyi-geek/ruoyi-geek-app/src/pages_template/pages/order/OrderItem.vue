<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  order: {
    type: Object,
    required: true
  }
});

// 价格小数
const priceDecimal = (val) => {
  if (val !== parseInt(val)) return val.slice(-2);
  else return '00';
};

// 价格整数
const priceInt = (val) => {
  if (val !== parseInt(val)) return val.split('.')[0];
  else return val;
};

// 总价
const totalPrice = (item) => {
  let price = 0;
  item.forEach(val => {
    price += parseFloat(val.price);
  });
  return price.toFixed(2);
};

// 总件数
const totalNum = (item) => {
  let num = 0;
  item.forEach(val => {
    num += val.number;
  });
  return num;
};
</script>

<template>
  <view class="order">
    <view class="top">
      <view class="left">
        <u-icon name="home" :size="30" color="rgb(94,94,94)"></u-icon>
        <view class="store">{{ order.store }}</view>
        <u-icon name="arrow-right" color="rgb(203,203,203)" :size="26"></u-icon>
      </view>
      <view class="right">{{ order.deal }}</view>
    </view>
    <view class="item" v-for="(item, index) in order.goodsList" :key="index">
      <view class="left">
        <image :src="item.goodsUrl" mode="aspectFill"></image>
      </view>
      <view class="content">
        <view class="title u-line-2">{{ item.title }}</view>
        <view class="type">{{ item.type }}</view>
        <view class="delivery-time">发货时间 {{ item.deliveryTime }}</view>
      </view>
      <view class="right">
        <view class="price">
          ￥{{ priceInt(item.price) }}
          <text class="decimal">.{{ priceDecimal(item.price) }}</text>
        </view>
        <view class="number">x{{ item.number }}</view>
      </view>
    </view>
    <view class="total">
      共{{ totalNum(order.goodsList) }}件商品 合计:
      <text class="total-price">
        ￥{{ priceInt(totalPrice(order.goodsList)) }}.
        <text class="decimal">{{ priceDecimal(totalPrice(order.goodsList)) }}</text>
      </text>
    </view>
    <view class="bottom">
      <view class="more"><u-icon name="more-dot-fill" color="rgb(203,203,203)"></u-icon></view>
      <view class="logistics btn">查看物流</view>
      <view class="exchange btn">卖了换钱</view>
      <view class="evaluate btn">评价</view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.order {
  width: 710rpx;
  background-color: #ffffff;
  margin: 20rpx auto;
  border-radius: 20rpx;
  box-sizing: border-box;
  padding: 20rpx;
  font-size: 28rpx;

  .top {
    display: flex;
    justify-content: space-between;

    .left {
      display: flex;
      align-items: center;

      .store {
        margin: 0 10rpx;
        font-size: 32rpx;
        font-weight: bold;
      }
    }

    .right {
      color: $u-warning-dark;
    }
  }

  .item {
    display: flex;
    margin: 20rpx 0 0;

    .left {
      margin-right: 20rpx;

      image {
        width: 200rpx;
        height: 200rpx;
        border-radius: 10rpx;
      }
    }

    .content {
      .title {
        font-size: 28rpx;
        line-height: 50rpx;
      }

      .type {
        margin: 10rpx 0;
        font-size: 24rpx;
        color: $u-tips-color;
      }

      .delivery-time {
        color: #e5d001;
        font-size: 24rpx;
      }
    }

    .right {
      margin-left: 10rpx;
      padding-top: 20rpx;
      text-align: right;

      .decimal {
        font-size: 24rpx;
        margin-top: 4rpx;
      }

      .number {
        color: $u-tips-color;
        font-size: 24rpx;
      }
    }
  }

  .total {
    margin-top: 20rpx;
    text-align: right;
    font-size: 24rpx;

    .total-price {
      font-size: 32rpx;
    }
  }

  .bottom {
    display: flex;
    margin-top: 40rpx;
    padding: 0 10rpx;
    justify-content: space-between;
    align-items: center;

    .btn {
      line-height: 52rpx;
      width: 160rpx;
      border-radius: 26rpx;
      border: 2rpx solid $u-border-color;
      font-size: 26rpx;
      text-align: center;
      color: $u-info-dark;
    }

    .evaluate {
      color: $u-warning-dark;
      border-color: $u-warning-dark;
    }
  }
}
</style>
