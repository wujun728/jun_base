<template>
  <view class="float-button">
    <u-button :type="type" :icon="icon" :shape="shape" :disabled="disabled" :size="size" @click="$emit('click')"></u-button>
  </view>
</template>

<script>
export default {
  props: {
    // 按钮的预置样式，info，primary，error，warning，success
    type: {
      type: String,
      default: uni.$u.props.button.type
    },
    // 图标
    icon: {
      type: String
    },
    // 按钮尺寸，large，normal，small，mini
    size: {
      type: String,
      default: 'large'
    },
    // 按钮形状，circle（两边为半圆），square（带圆角）
    shape: {
      type: String,
      default: 'circle'
    },
    // 按钮是否镂空
    plain: {
      type: Boolean,
      default: uni.$u.props.button.plain
    },
    // 是否禁止状态
    disabled: {
      type: Boolean,
      default: uni.$u.props.button.disabled
    },
  }
}
</script>

<style lang="scss" scoped>
.float-button {
  bottom: 100px;
  right: 20px;
  width: 52px;
  height: 52px;
  position: fixed;
  z-index: 10;
}
</style>