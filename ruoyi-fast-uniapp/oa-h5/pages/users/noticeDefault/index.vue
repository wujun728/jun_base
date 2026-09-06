<template>
  <view>
    <!-- #ifdef APP-PLUS -->
    <view class="cr-position-header">
      <!-- 非h5页面默认工具栏高度获取 -->
      <view class="status_bar"></view>
      <default-nav-bar :index="1"></default-nav-bar>
    </view>
    <!-- #endif -->

    <view class="default plr10">
      <view class="title">{{config.default.title}}</view>
      <view class="caption display-align">
        <uni-dateformat class="time" format="yyyy/MM/dd" :date="config.default.time"></uni-dateformat>
        <view class="pl-14 pr-14">·</view>
        <view class="">{{config.default.visit}} 人阅读</view>
      </view>
      <view class="content">
        <view v-html="config.content"></view>
        <!-- <mp-html :content="config.content" :lazy-load="true" :selectable="true" /> -->
      </view>
    </view>
    <global-index></global-index>
  </view>
</template>

<script setup>
  import { ref, reactive } from 'vue'
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import globalIndex from '@/components/globalIndex/index.vue'
  import message from '@/utils/message'
  import { noticeDetailApi } from '@/api/user'
  import { onLoad } from '@dcloudio/uni-app'
  onLoad((e) => {
    if (e.id) {
      getNoticeCategory(e.id)
    }
  })

  const config = reactive({
    default: {},
    content: ''
  })
  // 获取企业动态详情
  const getNoticeCategory = (id) => {
    noticeDetailApi(id).then(res => {
      config.default = res.data || {}
      if (res.data.content) {
        config.content = res.data.content.replace(/<img/gi, '<img style="max-width:100%;height:auto" ')
      }
    }).catch((error) => {
      message.error(error.message)
    })
  }
</script>
<style>
  page {
    background-color: #fff;
  }
</style>

<style scoped lang="scss">
  .default {
    // #ifdef APP-PLUS
    padding-top: calc($uni-default-bar-height + var(--status-bar-height));
    // #endif

    .title {
      padding-top: 28rpx;
      font-weight: $uni-default-font-weight;
      font-size: 36rpx;
      color: $uni-text-color;
      line-height: 54rpx;
    }

    .caption {
      font-size: 24rpx;
      padding-top: 10rpx;
      color: $nui-text-color-four;
    }

    .content {
      width: 100%;
      margin: 40rpx 0;
      font-size: 30rpx;
      line-height: 60rpx;

      color: $uni-article-detail-color;
    }
  }
</style>