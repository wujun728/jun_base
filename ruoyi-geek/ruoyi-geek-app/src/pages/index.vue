<script setup lang="ts">
import tab from '@/plugins/tab';
</script>
<template>
  <view class="content">
    <image class="logo" src="@/static/logo.png"></image>
    <view class="text-area">
      <text class="title"> RuoYi-Geek-App</text>
    </view>
    <view class="text-area">
      <up-text type="primary" text="uview-plus"></up-text>
    </view>
    <view>
      <button @click="tab.navigateTo('/pages_template/pages/template')">查看模板</button>
    </view>
  </view>
</template>
<style scoped lang="scss">
.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.logo {
  height: 200rpx;
  width: 200rpx;
  margin-top: 200rpx;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 50rpx;
}

.text-area {
  display: flex;
  justify-content: center;
}

.title {
  font-size: 36rpx;
  color: #8f8f94;
}
</style>
