<!-- 公用页面容器，可为子组件提供安全区域尺寸的 CSS 变量  -->
<template>
  <view class="base-container" :class="baseCssName" :style="styles">
    <slot />
  </view>
</template>

<script setup lang="ts">
import { getSystemInfo } from "@/utils/helper";

const props = withDefaults(
  defineProps<{
    class?: any
  }>(),
  {
    class: ""
  }
);

const { class: baseCssName } = toRefs(props);

const { safeAreaInsets, windowHeight, windowWidth } = getSystemInfo();
const { top: statusBarHeight, bottom: bottomAreaHeight } = safeAreaInsets;

const styles = {
  "--status-bar-height": statusBarHeight + "px",
  "--bottom-area-height": bottomAreaHeight + "px",
  "--full-vw": windowWidth + "px", // 为了兼容移动端 chrome 浏览器 100vh 包含导航栏高度的问题
  "--full-vh": windowHeight + "px"
};

</script>

<style scoped></style>
