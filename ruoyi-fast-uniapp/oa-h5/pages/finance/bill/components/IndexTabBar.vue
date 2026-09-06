<template>
  <view class="index-tabbar-wrapper">
    <navigator hover-class="none" url="/pages/finance/bill/index" open-type="redirect" class="index-tabbar-item" :class="[props.index === 0 && 'active']">
      <view class="iconfont icon-shouzhijizhang" />
      收支记账
    </navigator>
    <navigator hover-class="none" url="/pages/finance/bill/statistic" open-type="redirect" class="index-tabbar-item" :class="[props.index === 1 && 'active']">
      <view class="iconfont icon-shouzhitongji" />
      收支统计
    </navigator>
  </view>
</template>

<script setup lang="ts">

const props = defineProps<{
  index: number
}>();

</script>

<style scoped lang="scss">
.index-tabbar-wrapper {
  position: fixed;
  left: 0;
  bottom: 0;
  right: 0;
  padding-top: 16rpx;
  padding-bottom: calc(16rpx + var(--bottom-area-height));

  display: flex;
  justify-content: center;

  background-color: #fff;
  box-shadow: 0rpx 0rpx 16rpx 0rpx rgba(215, 215, 215, 0.5);
}

.index-tabbar-item {
  display: flex;
  flex-flow: column nowrap;
  align-items: center;
  text-align: center;

  font-size: 22rpx;
  line-height: 22rpx;
  color: var(--color, #687383);

  &.active {
    --color: #{$uni-color-primary};
  }

  .iconfont {
    font-size: 40rpx;
    line-height: 40rpx;
    margin-bottom: 12rpx;
    color: var(--color, #{$uni-text-color-five});
  }

  &:first-child {
    margin-right: 214rpx;
  }
}
</style>
