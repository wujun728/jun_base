<template>
	<view class="wrap">
		<view class="list-cell">
			<image class="user-images" src="/static/aidex/images/user06.png"></image>
			<view class="user-name">
				<span>安静</span><u-icon size="32" color="#f04d94" name="woman"></u-icon><br>
				<span class="user-other">员工编号：8888 8888</span>
			</view>
		</view>
		<u-gap height="20" bg-color="#f5f5f5"></u-gap>
		<u-cell-group :border="false">
			<u-cell-item title="公司" value="科技有限公司" icon="home" :title-width="180" :arrow="false"></u-cell-item>
			<u-cell-item title="所属部门"  value="人事部" icon="file-text" :title-width="180" :arrow="false"></u-cell-item>
			<u-cell-item title="岗位"  value="秘书" icon="account" :title-width="180" :arrow="false"></u-cell-item>
			<u-cell-item title="手机" class="connect" value="13898987878" icon="phone" :title-width="180" :arrow="false"></u-cell-item>
			<u-cell-item title="邮箱" class="connect" value="upugpbvrtge@188.com" icon="email" :title-width="180" :arrow="false"></u-cell-item>
			<u-gap height="20" bg-color="#f5f5f5"></u-gap>
		</u-cell-group>
	</view>
	
</template>
<script>
	 import HeadNavBar from '@/components/headnavbar/index';
/**
 * Copyright (c) 2013-Now http://aidex.vip All rights reserved.
 */
export default {
	components: {
	  HeadNavBar
	},
	data() {
		return {
		}
		},
	onLoad() {
		
	},
	methods: {
		navTo(url) {
			uni.navigateTo({
				url: url
			});
		},
	}
};
</script>
<style lang="scss">
@import 'index.scss';
page {
	background-color: #f5f5f5;
}
.list-cell {
	display: flex;
	box-sizing: border-box;
	width: 100%;
	padding: 10px 24rpx;
	overflow: hidden;
	color: #323233;
	font-size: 14px;
	line-height: 24px;
	background-color: #fff;
	align-items: center;
	
}
.user-images{
	width: 80rpx;
	height: 80rpx;
	margin-right: 10px;
}
.user-name{
	font-size: 32rpx;
	color: #000000;
	span{
	}
	.user-other{
		font-size: 26rpx;
		color: #999999;
		font-weight: normal;
	}
}

</style>
