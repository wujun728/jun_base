<template>
  <view class="content">

    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar jump-url="/pages/workbench/index"></default-nav-bar>
    </view>


    <view class="report-con plr10">
      <view class="examine-search-title">申请表单</view>
      <view class="report-list">
        <view class="report-list-item" v-for="item in data.listData" :key="item.id">
          <view class="text-center" @click="handleExamineItem(item)">
            <view class="item-logo">
              <view class="iconfont" :class="getIconChange(item.icon)" :style="{color: item.color}"></view>
            </view>
            <view class="item-caption line1">{{item.name}}</view>
          </view>
        </view>
      </view>
    </view>
    <view class="uni-p-b-98"></view>
    <bottom-navigation :type="2" page-path="/pages/users/examine/index"></bottom-navigation>
    <global-index></global-index>
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import bottomNavigation from "@/components/bottomNavigation/index.vue"
  import globalIndex from '@/components/globalIndex/index.vue'
  import { reactive, onMounted } from "vue"
  import message from "@/utils/message"
  import { examineTabData } from '@/utils/assessment'
  const data = reactive({
    customStyle: { border: 'none', lineHeight: '20px', background: '#ED4014' },
    examineTabData: examineTabData,
    listData: []
  })

  onMounted(() => {
    getConfigSearch(0)
  })
  import { approveConfigSearchApi } from '@/api/business'
  const getConfigSearch = (id) => {
    approveConfigSearchApi(id).then((res) => {
      const datas = res.data ? res.data : []
      data.listData = datas
    }).catch((error) => {
      message.error(error.message)
    });
  }


  const getIconChange = (icon) => {
    if (icon.indexOf('-') > -1) {
      return icon
    } else {
      return `icon-${icon.slice(4)}`
    }
  }

  import { clickNavigateTo } from '@/utils/helper'
  const handleExamineItem = (item) => {
    clickNavigateTo(`/pages/users/examine/default?id=${item.id}&name=${item.name}`)
  }

  const examineSearchItem = (item) => {
    clickNavigateTo(`/pages/users/examine/center?id=${item}`)
  }
</script>
<style>
  page {
    background-color: #fff;
  }
</style>

<style scoped lang="scss">
  .content {
    width: 100%;

    .cr-position-header {
      position: sticky;
    }

    .report-con {
      width: 100%;
      z-index: 11;
      padding-top: 36rpx;
      padding-bottom: 108rpx;

      .examine-search-title {
        font-size: $uni-font-size-default;
        font-weight: 600;
        color: $uni-text-color;
      }

      .report-list {
        padding-top: 36rpx;
        width: 100%;
        display: flex;
        flex-wrap: wrap;

        .report-list-item {
          width: 25%;
          margin-bottom: 50rpx;

          .item-logo {
            display: inline-block;
            width: 90rpx;
            height: 90rpx;

            .iconfont {
              font-size: 90rpx;
              color: #fff;
            }
          }

          .item-title {
            padding-top: 20rpx;
            font-size: 32rpx;
            color: $uni-text-color;
            font-weight: 600;
          }

          .item-caption {
            padding-top: 16rpx;
            font-size: 28rpx;
            color: $nui-text-color-two;
          }
        }
      }
    }
  }
</style>