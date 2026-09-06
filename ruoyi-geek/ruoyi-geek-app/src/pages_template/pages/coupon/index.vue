<script setup>
import MeituanCoupon from './components/meituan-coupon.vue';
import JingdongCoupon from './components/jingdong-coupon.vue';
import TaobaoCoupon from './components/taobao-coupon.vue';
</script>
<template>
	<view class="u-wrap">
		<MeituanCoupon />
		<JingdongCoupon />
		<TaobaoCoupon />
	</view>
</template>

<style lang="scss" scoped>
page {
	height: 100%;
	background-color: rgb(244, 244, 244);
}

.u-wrap {
	padding: 24rpx;
}
</style>
