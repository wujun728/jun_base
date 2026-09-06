<script setup>
import { ref } from 'vue';
import citySelect from '@/pages_template/components/u-city-select/u-city-select.vue';
const value = ref(false);
const input = ref('');

const cityChange = (e) => {
	input.value = e.province.label + '-' + e.city.label + '-' + e.area.label;
	input.value += e.province.value + '-' + e.city.value + '-' + e.area.value;
};
</script>
<template>
	<view class="u-demo">
		<view class="u-demo-wrap">
			<view class="u-demo-title">演示效果</view>
			<view class="u-demo-area">
				<city-select v-model="value" @city-change="cityChange"></city-select>
				<view class="u-demo-result-line">{{ input ? input : 'Picker值' }}</view>
			</view>
		</view>
		<view class="u-config-wrap">
			<view class="u-config-title u-border-bottom">参数配置</view>
			<view class="u-config-item">
				<view class="u-item-title">状态</view>
				<u-button @click="value = true">打开Picker</u-button>
			</view>
		</view>
	</view>
</template>
<style scoped>
.btn-wrap {
	margin: 100rpx 30rpx;
}
</style>
