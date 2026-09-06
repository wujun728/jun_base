<template>
  <view class="content">
    <!-- #ifndef APP-PLUS -->
    <image class="logo logo-h5" src="../../../static/image/report-h5-logo.png" mode=""></image>
    <!-- #endif -->

    <!-- #ifdef APP-PLUS -->
    <image class="logo logo-app" src="../../../static/image/report-logo.png" mode=""></image>
    <view class="bar">
      <default-nav-bar jump-url="/pages/workbench/index" background-color="rgba(0,0,0,0)"
        color="#ffffff"></default-nav-bar>
    </view>
    <!-- #endif -->

    <view class="report-con plr10 report-con-h">
      <view class="report-list">
        <navigator class="report-list-item" v-for="item in listData" :key="item.id"
          :url="'/pages/users/report/mine?type='+item.id" animation-type="slide-in-right">
          <view class="text-center">
            <image class="item-logo" :src="item.backgroundImage" mode=""></image>
            <view class="item-title">{{item.title}}</view>
            <view class="item-caption line1">{{item.caption}}</view>
          </view>
        </navigator>
      </view>
    </view>
    <view class="uni-p-b-98"></view>
    <bottom-navigation :type="1" page-path="/pages/users/report/index"></bottom-navigation>
    <global-index />
  </view>
</template>

<script setup lang="ts">
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import bottomNavigation from "@/components/bottomNavigation/index.vue"
  import globalIndex from "@/components/globalIndex/index.vue"
  import { reactive } from "vue";
  const listData = reactive([{
    backgroundImage: '/static/image/report01.png',
    title: '日报',
    caption: '每日沉淀与总结',
    id: 0
  },
  {
    backgroundImage: '/static/image/report02.png',
    title: '周报',
    caption: '每周回顾与规划',
    id: 1
  },
  {
    backgroundImage: '/static/image/report03.png',
    title: '月报',
    caption: '每月重温与展望',
    id: 2
  }, {
    backgroundImage: '/static/image/report04.png',
    title: '汇报',
    caption: '工作汇报与总结',
    id: 3
  },
  ])
</script>

<style scoped lang="scss">
  .content {
    width: 100%;
    position: relative;

    .logo {
      width: 100vw;
    }

    .report-con {
      width: 100%;
      position: absolute;
      left: 0;
      border-radius: 16rpx 16rpx 0 0;
      z-index: 11;
      background-color: #F0F1F5;
      padding-top: 40rpx;
      padding-bottom: 108rpx;

      .report-list {
        width: 100%;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;

        .report-list-item {
          display: block;
          padding: 28rpx 22rpx;
          background-color: #fff;
          height: 282rpx;
          border-radius: 12rpx;
          display: flex;
          justify-content: center;
          align-items: center;
          margin-bottom: 20rpx;

          .item-logo {
            width: 122rpx;
            height: 120rpx;
          }

          .item-title {
            padding-top: 20rpx;
            font-size: 32rpx;
            color: $uni-text-color;
            font-weight: 600;
          }

          .item-caption {
            padding-top: 10rpx;
            font-size: 24rpx;
            color: $nui-text-color-four;
          }
        }
      }
    }

    // #ifndef APP-PLUS
    .logo-h5 {
      height: 300rpx;
    }

    .report-con-h {
      top: 286rpx;
      height: calc(100vh - 286rpx);
    }

    // #endif

    // #ifdef APP-PLUS
    .logo-app {
      height: 408rpx;
    }

    .bar {
      position: absolute;
      left: 0;
      width: 100%;
      top: 88rpx;
    }

    .report-con-h {
      top: 378rpx;
      height: calc(100vh - 378rpx);
    }

    // #endif
  }
</style>