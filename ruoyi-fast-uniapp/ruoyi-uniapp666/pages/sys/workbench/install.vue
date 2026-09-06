<template>
	<view class="wrap">
		<u-navbar class="custom-header" title="常用设置" height="44" >
			<view slot="right">
				<u-button size="mini" type="primary" style="margin-right:10px" @click="navTo('/pages/sys/workbench/index')">保存</u-button>
			</view>
		</u-navbar>
		<view class="search">
			<u-search v-model="keywords" @custom="search" @search="search"></u-search>
		</view>
		<view class="workbench-title">常用应用</view>
		<view class="toolbar">
			<u-grid class="grid" :col="4" :border="false">
				<u-grid-item :index="0" @click="navTo('')" >
					<view class="home-icon icon-color01">
						<i class="iconfont icon-qingjia"></i>
					</view>
					<view class="grid-text">请假申请</view>
					<i class="install-icon iconfont icon-minus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item :index="1" @click="navTo('')">
					<view class="home-icon icon-color04">
						<i class="iconfont icon-hetongguanli"></i>
					</view>
					<view class="grid-text">合同申请</view>
					<i class="install-icon iconfont icon-minus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item :index="2" @click="navTo('')">
					<view class="home-icon icon-color03">
						<i class="iconfont icon-chucha"></i>
					</view>
					<view class="grid-text">出差申请</view>
					<i class="install-icon iconfont icon-minus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item :index="2" @click="navTo('')">
					<view class="home-icon icon-color12">
						<i class="iconfont icon-ribao"></i>
					</view>
					<view class="grid-text">日报</view>
					<i class="install-icon iconfont icon-minus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item @click="navTo('')">
					<view class="home-icon icon-color04">
						<i class="iconfont icon-tongzhi"></i>
					</view>
					<view class="grid-text">邮件</view>
					<i class="install-icon iconfont icon-minus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item @click="navTo('')">
					<view class="home-icon icon-color05">
						<i class="iconfont icon-huiyishi"></i>
					</view>
					<view class="grid-text">会议室</view>
					<i class="install-icon iconfont icon-minus-circle-fill"></i>
				</u-grid-item>
			</u-grid>
		</view>
		<view class="workbench-title">日常办公</view>
		<view class="toolbar">
			<u-grid class="grid" :col="4" :border="false">
				<u-grid-item  :index="0" @click="navTo('')" >
					<view class="home-icon icon-color04">
						<i class="iconfont icon-yongche"></i>
					</view>
					<view class="grid-text">用车</view>
					<i class="addinstall-icon iconfont icon-plus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item  :index="2" @click="navTo('')">
					<view class="home-icon icon-color03">
						<i class="iconfont icon-jiabanshenqing"></i>
					</view>
					<view class="grid-text">加班</view>
					<i class="addinstall-icon iconfont icon-plus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item :index="2" @click="navTo('')">
					<view class="home-icon icon-color12">
						<i class="iconfont icon-kaoqinchuqin"></i>
					</view>
					<view class="grid-text">考勤</view>
					<i class="addinstall-icon iconfont icon-plus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item   @click="navTo('')">
					<view class="home-icon icon-color04">
						<i class="iconfont icon-haocaifei"></i>
					</view>
					<view class="grid-text">耗材</view>
					<i class="addinstall-icon iconfont icon-plus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item  @click="navTo('')">
					<view class="home-icon icon-color01">
						<i class="iconfont icon-gongwujiedai"></i>
					</view>
					<view class="grid-text">接待</view>
					<i class="addinstall-icon iconfont icon-plus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item @click="navTo('')">
					<view class="home-icon icon-color04">
						<i class="iconfont icon-baoming"></i>
					</view>
					<view class="grid-text">报名</view>
					<i class="addinstall-icon iconfont icon-plus-circle-fill"></i>
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
					<i class="addinstall-icon iconfont icon-plus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item :index="2" @click="navTo('')">
					<view class="home-icon icon-color03">
						<i class="iconfont icon-mall-bag"></i>
					</view>
					<view class="grid-text">采购申请</view>
					<i class="addinstall-icon iconfont icon-plus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item :index="2" @click="navTo('')">
					<view class="home-icon icon-color12">
						<i class="iconfont icon-baoxiaodan"></i>
					</view>
					<view class="grid-text">付款申请</view>
					<i class="addinstall-icon iconfont icon-plus-circle-fill"></i>
				</u-grid-item>
				<u-grid-item  @click="navTo('')">
					<view class="home-icon icon-color04">
						<i class="iconfont icon-shenpi"></i>
					</view>
					<view class="grid-text">用章申请</view>
					<i class="addinstall-icon iconfont icon-plus-circle-fill"></i>
				</u-grid-item>
			</u-grid>
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
			show: false,
			
		};
	},
	onLoad() {
		
	},
	methods: {
		navTo(url) {
			uni.reLaunch({
				url: url
			});
		},
		back(){
			uni.navigateBack();
		}
		
	}
};
</script>
<style lang="scss" scoped>
@import 'index.scss';
page {
	background-color: #ffffff;
}
.wrap .search{
	background: #ffffff;
}
.u-swiper-wrap{
	padding:0 10px;
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
 .grid .u-grid-item{
	 position: relative;
	 .install-icon{
	 	position: absolute;
	 	top:-10px;
	 	right:50%;
		margin-right: -30px;
	 	color: #FE0100;
	 }
	 .addinstall-icon{
	 	position: absolute;
	 	top:-10px;
	 	right:50%;
	 	margin-right: -30px;
	 	color: #4285F4;
	 }
 }

</style>
