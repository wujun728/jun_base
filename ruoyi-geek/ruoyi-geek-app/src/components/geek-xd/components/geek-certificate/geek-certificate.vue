<template>
  <view class="upload">
    <view class="imagebox">
      <view class="imageborder">
        <view class="main">
          <slot></slot>
        </view>
      </view>
    </view>
    <view class="text">
      <text>{{ text }}</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';

const text = ref("")
</script>

<style scoped lang="scss">
.upload {
  height: 400rpx;
  width: 90%;
  border-radius: 20rpx;
  overflow: hidden;

  .imagebox {
    height: 80%;
    background-color: #eff8ff;
    align-items: center;
    justify-content: center;
    display: flex;

    .imageborder {
      border: 5px #319fea solid;
      position: relative;
      width: 70%;
      height: 80%;
      border-radius: 30rpx;

      &::after {
        position: absolute;
        content: ' ';
        background-color: #eff8ff;
        height: 80%;
        width: 120%;
        top: 10%;
        left: -10%;
      }

      &::before {
        position: absolute;
        content: ' ';
        background-color: #eff8ff;
        top: -10%;
        left: 10%;
        height: 120%;
        width: 80%;
      }

      .main {
        position: absolute;
        background-color: #eff8ff;
        top: 5%;
        left: 5%;
        height: 90%;
        width: 90%;
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 2;
      }
    }
  }

  .text {
    height: 20%;
    background-color: #319fea;
    display: flex;
    justify-content: center;
    align-items: center;

    text {
      color: #ffffff;
    }
  }
}
</style>
