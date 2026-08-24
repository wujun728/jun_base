<template>
  <div :class="{ 'my-full-screen': isFullScreen }" :style="{ zIndex: currentZIndex }">
    <div class="mask" @click="handlePopoverHide"></div>
    <slot></slot>
  </div>
</template>
<script>
import { PopupManager } from 'element-ui/src/utils/popup'

export default {
  data() {
    return {
      isFullScreen: false,
      currentZIndex: null
    }
  },
  methods: {
    request() {
      this.isFullScreen = true
      this.currentZIndex = PopupManager.nextZIndex()
    },
    // 调用slot中的方法
    handlePopoverHide() {
      this.$emit('call-parent-method')
    }
  }
}
</script>
<style scoped>
.mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0);
  z-index: 1;
}
.my-full-screen {
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  right: 0 !important;
  bottom: 0 !important;
  width: 100% !important;
  height: 100% !important;
}
</style>
