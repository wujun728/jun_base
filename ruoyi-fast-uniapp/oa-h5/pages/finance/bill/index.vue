<template>
  <BaseContainer class="custom-container">
    <IndexBillList />
    <IndexTabBar :index="0" />
  </BaseContainer>
</template>

<script setup lang="ts">
import BaseContainer from "@/components/BaseContainer/index.vue";
import IndexTabBar from "./components/IndexTabBar.vue";
import IndexBillList from "./components/IndexBillList.vue";
</script>

<style scoped lang="scss">
.custom-container {
  --nav-height: calc(var(--status-bar-height) + #{$uni-default-bar-height});
  --tabbar-height: calc(110rpx + var(--bottom-area-height));
  padding-top: var(--nav-height);
  padding-bottom: var(--tabbar-height);
}
</style>
