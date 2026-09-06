<template>
	<view class="wrap">
		<u-gap height="20" bg-color="#f5f5f5"></u-gap>
		<u-cell-group :border="false">
			<u-cell-item title="清除缓存" :arrow="false">
				<view solt="right-icon">
					15M
				</view>
			</u-cell-item>
		</u-cell-group>
		<view class="u-m-40">
			<u-button type="primary" @click="logout" :hair-line="false">清除缓存</u-button>
		</view>
	</view>
</template>
<script>
/**
 * Copyright (c) 2013-Now http://aidex.vip All rights reserved.
 */
export default {
	data() {
		return {
			
		};
	},
	methods: {
		navTo(url) {
			uni.navigateTo({
				url: url
			});
		}
	}
};
</script>
<style lang="scss">
@import '../home/index.scss';

page {
	background-color: #f8f8f8;
}

/deep/ .u-cell-title {
	padding: 25rpx 30rpx;
	font-size: 30rpx;
}
</style>
