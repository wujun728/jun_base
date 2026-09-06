<template>
	<view class="wrap">
		<head-nav-bar
		title="首页"
		/>
		<u-swiper :height="270" :list="imgList" :title="false" @click="imgListClick"></u-swiper>
				<u-collapse-item style="display: none;">
				</u-collapse-item>

		<u-divider>已经到底了</u-divider>
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
			show: false,
			head: '/static/aidex/images/head.png',
			imgList: [
				{image: '/static/aidex/banner/banner01.png'},
				{image: '/static/aidex/banner/banner02.png'}, 
				{image: '/static/aidex/banner/banner03.png'}
			],
			todoCount: 3
		};
	},
	onLoad() {
		
	},
	methods: {
		navTo(url) {
			uni.navigateTo({
				url: url
			});
		},
		imgListClick(index) {
			console.log(`点击了第${index + 1}页图片`)
		},
		itemClick(index) {
			console.log(index);
		}
	}
};
</script>
<style lang="scss">
@import 'index.scss';
.banner-box{
	padding: 0 2%;
	width: 96%;
	height: 170rpx;
	margin: 30rpx 0 30rpx;
}
.u-swiper-wrap{
	padding:0 10px;
}

.banner-pic{
	width: 47%;
	float: left;
	display: inline-block;
	margin: 0 1.5%;
}
.banner-pic image{
	width: 100%;
	height: 170rpx;
	border-radius: 12rpx;
	box-shadow: 0px 0px 10px rgba(0,0,0,0.1);
}

.u-mode-light-info{
    background-color: #ffffff;
    color: #666666;
    border: 1px solid #e9ecf6;
	font-size: 12px;
	padding: 2px 8px;
	position: relative;
	top:-3px;
}

</style>
