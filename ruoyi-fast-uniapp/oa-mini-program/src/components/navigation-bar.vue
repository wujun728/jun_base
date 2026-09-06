<template>
    <!-- 自定义顶部栏 -->
    <view class="nav-bar">
        <view>标题</view>
    </view>

    <!-- 占位，高度与顶部栏一样 -->
    <view class="nav-bar"></view>
</template>

<script lang="ts" setup>
// import { usePhoneStore } from '@/store';
// const PhoneStore = usePhoneStore()
</script>

<style scoped lang="scss">
.nav-bar {
    height: var(--height);
}
</style>