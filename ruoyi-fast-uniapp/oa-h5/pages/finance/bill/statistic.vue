<template>
  <BaseContainer class="custom-container">
    <IndexBillStatistic />
    <IndexTabBar :index="1" />
  </BaseContainer>
</template>

<script setup lang="ts">
import BaseContainer from "@/components/BaseContainer/index.vue";
import IndexTabBar from "./components/IndexTabBar.vue";
import IndexBillStatistic from "./components/IndexBillStatistic.vue";
</script>

<style scoped lang="scss">
.custom-container {
  --nav-height: calc(var(--status-bar-height) + #{$uni-default-bar-height});
  --tabbar-height: calc(110rpx + var(--bottom-area-height));
  padding-top: var(--nav-height);
  padding-bottom: var(--tabbar-height);
}
</style>
