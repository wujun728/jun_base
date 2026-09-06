<template>
	<view class="wrap">
		<u-swiper :height="270" :list="imgList" :title="false" @click="imgListClick"></u-swiper>
		<view class="workbench-title">常用应用</view>
		<view class="toolbar">
			<u-grid class="grid" :col="4" :border="false">
				<u-grid-item :index="0" @click="navTo('/pages/sys/workbench/add-form')" >
					<view class="home-icon icon-color01">
						<i class="iconfont icon-qingjia"></i>
					</view>
					<view class="grid-text">请假申请</view>
				</u-grid-item>
				<u-grid-item :index="1" @click="navTo('')">
					<view class="home-icon icon-color04">
						<i class="iconfont icon-hetongguanli"></i>
					</view>
					<view class="grid-text">合同申请</view>
				</u-grid-item>
				<u-grid-item :index="2" @click="navTo('')">
					<view class="home-icon icon-color03">
						<i class="iconfont icon-chucha"></i>
					</view>
					<view class="grid-text">出差申请</view>
				</u-grid-item>
				<u-grid-item :index="2" @click="navTo('')">
					<view class="home-icon icon-color12">
						<i class="iconfont icon-ribao"></i>
					</view>
					<view class="grid-text">日报</view>
				</u-grid-item>
				<u-grid-item  @click="navTo('')">
					<view class="home-icon icon-color04">
						<i class="iconfont icon-tongzhi"></i>
					</view>
					<view class="grid-text">邮件</view>
				</u-grid-item>
				<u-grid-item  @click="navTo('')">
					<view class="home-icon icon-color05">
						<i class="iconfont icon-huiyishi"></i>
					</view>
					<view class="grid-text">会议室</view>
				</u-grid-item>
				<u-grid-item  @click="navTo('/pages/sys/workbench/install')">
					<view class="home-icon icon-color13">
						<i class="iconfont icon-tianjia" style="color:#90949d;"></i>
					</view>
					<view class="grid-text">添加常用</view>
				</u-grid-item>
			</u-grid>
		</view>
		<view class="workbench-title">日常办公</view>
		<view class="toolbar">
			<u-grid class="grid" :col="4" :border="false">
				<u-grid-item :index="0" @click="navTo('')" >
					<view class="home-icon icon-color04">
						<i class="iconfont icon-yongche"></i>
					</view>
					<view class="grid-text">用车</view>
				</u-grid-item>
				<u-grid-item :index="2" @click="navTo('')">
					<view class="home-icon icon-color03">
						<i class="iconfont icon-jiabanshenqing"></i>
					</view>
					<view class="grid-text">加班</view>
				</u-grid-item>
				<u-grid-item :index="2" @click="navTo('')">
					<view class="home-icon icon-color12">
						<i class="iconfont icon-kaoqinchuqin"></i>
					</view>
					<view class="grid-text">考勤</view>
				</u-grid-item>
				<u-grid-item  @click="navTo('')">
					<view class="home-icon icon-color04">
						<i class="iconfont icon-haocaifei"></i>
					</view>
					<view class="grid-text">耗材</view>
				</u-grid-item>
				<u-grid-item  @click="navTo('')">
					<view class="home-icon icon-color01">
						<i class="iconfont icon-gongwujiedai"></i>
					</view>
					<view class="grid-text">接待</view>
				</u-grid-item>
				<u-grid-item  @click="navTo('')">
					<view class="home-icon icon-color04">
						<i class="iconfont icon-baoming"></i>
					</view>
					<view class="grid-text">报名</view>
				</u-grid-item>
			</u-grid>
		</view>
		<view class="workbench-title">财务报销</view>
		<view class="toolbar">
			<u-grid class="grid" :col="4" :border="false">
				<u-grid-item :index="0" @click="navTo('')" >
					<view class="home-icon icon-color04">
						<i class="iconfont icon-finance"></i>
					</view>
					<view class="grid-text">费用报销</view>
				</u-grid-item>
				<u-grid-item :index="2" @click="navTo('')">
					<view class="home-icon icon-color03">
						<i class="iconfont icon-mall-bag"></i>
					</view>
					<view class="grid-text">采购申请</view>
				</u-grid-item>
				<u-grid-item :index="2" @click="navTo('')">
					<view class="home-icon icon-color12">
						<i class="iconfont icon-baoxiaodan"></i>
					</view>
					<view class="grid-text">付款申请</view>
				</u-grid-item>
				<u-grid-item  @click="navTo('')">
					<view class="home-icon icon-color04">
						<i class="iconfont icon-shenpi"></i>
					</view>
					<view class="grid-text">用章申请</view>
				</u-grid-item>
			</u-grid>
		</view>
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
.workbench-title{
	font-size: 32rpx;
	font-weight: bold;
	color: #333333;
	padding: 15px 30rpx;
}
 .home-icon i.icon-tongzhi{
	font-size: 22px;
 }
</style>
