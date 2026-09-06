<template>
	<view class="content">
		<scroll-view v-if="true" scroll-y class="data_body" :style="{height:scrollHeight}">
			<!--数据进度条-->
			<view class="progress">
				<data-progress :progressList="userHealthyLineBar" :borderRadius="20" padMiddle="true"></data-progress>
			</view>
			<view class="split_line"></view>
			
			<!-- 新增小程序会员趋势-->
			<view class="friend_operate">
				<text-block :content="baseData"></text-block>
				<view class="trend_title">新增小程序会员趋势</view>
				<view class="charts-box">
				  <qiun-data-charts 
				  type="mix" 
				  canvasId="three_a"
				  :canvas2d="isCanvas2d"
				  :reshow="delayload"
				  :opts="{yAxis:{data:[{position: 'left',title: '销售额/万',max:userTrand?userTrand.yAxis[0].max:0,min:userTrand?userTrand.yAxis[0].min:0},{position: 'right',title: '',max:userTrand?userTrand.yAxis[1].max:0,min:userTrand?userTrand.yAxis[1].min:0,unit:'%'}]}}" 
				  :chartData="userTrand"/>
				</view>
			</view>
			<view class="split_line"></view>
			
			<!-- 会员扫码率趋势-->
			<view class="friend_operate">
				<text-block :content="scanTrand"></text-block>
				<view class="trend_title">会员扫码率趋势</view>
				<view class="charts-box">
				  <qiun-data-charts 
				  type="mix" 
				  canvasId="three_b"
				  :canvas2d="isCanvas2d"
				  :reshow="delayload"
				  :opts="{yAxis:{data:[{position: 'left',title: '',max:scanTrandPrecent?scanTrandPrecent.yAxis[0].max:0,min:scanTrandPrecent?scanTrandPrecent.yAxis[0].min:0,unit:'%'}]}}" 
				  :chartData="scanTrandPrecent"/>
				</view>
			</view>
			<view class="split_line"></view>
			
			<!-- 小程序活跃会员占比-->
			<view class="friend_operate">
				<text-block :content="miniActive"></text-block>
				<view class="charts-box">
				  <qiun-data-charts 
				  type="mix" 
				  canvasId="three_c"
				  :canvas2d="isCanvas2d"
				  :reshow="delayload"
				  :opts="{yAxis:{data:[{position: 'left',title: '销售额/万',max:miniActivePrecent?miniActivePrecent.yAxis[0].max:0,min:miniActivePrecent?miniActivePrecent.yAxis[0].min:0},{position: 'right',title: '',max:miniActivePrecent?miniActivePrecent.yAxis[1].max:0,min:miniActivePrecent?miniActivePrecent.yAxis[1].min:0,unit:'%'}]}}" 
				  :chartData="miniActivePrecent"
				  />
				</view>
			</view>
		</scroll-view>
		<view  v-else class="container padding_stand-big normal_color">
			<li class="iconfont icon-cry cry"></li>暂无数据
		</view>
	</view>
</template>

<script>
	import DataProgress from "../data-progress/data-progress.vue"
	
	import userHealthyLineBar from '../../static/json/user-healthy/1.json';
	import baseData from '../../static/json/user-healthy/2.json';
	import userTrand from '../../static/json/user-healthy/3.json';
	
	import scanTrand from '../../static/json/user-healthy/4.json';
	import scanTrandPrecent from '../../static/json/user-healthy/5.json';
	import miniActive from '../../static/json/user-healthy/6.json';
	import miniActivePrecent from '../../static/json/user-healthy/7.json';

	import Config from '../../static/js/config'
	
	var _self;
	export default {
		name:'user-healthy',
		props: {
			scrollHeight:{
				type:String,
				default:"600px"
			}
		},
		components:{
			DataProgress
		},
		data() {
			return {
				userHealthyLineBar,
				baseData,
				userTrand,
				scanTrand,
				scanTrandPrecent,
				miniActive,
				miniActivePrecent,
				delayload:false,
				isCanvas2d:Config.ISCANVAS2D,
			}
		},
		mounted() {
			this.getData();
		},
		methods:{
			async getData(){
				uni.showLoading();
				await setTimeout(() => {
					this.delayload = true;
					uni.hideLoading();
				}, 1000)
			}
		}
	}
</script>

<style scoped lang="less">
	.content{
		padding-top: 10rpx;
		.progress,.firend_operate{
			padding: 0 10rpx;
		}
		.progress{
			margin-bottom: 20rpx;
		}
		.friend_operate{
			padding: 30rpx 10rpx;
			.title{
				text-align:left;
				margin-bottom: 20rpx;
			}
			.trend_title{
				text-align: right;
				font-size: 22rpx;
				color: #ff9900;
				margin-top: 20rpx;
			}
		}
	}
	
</style>
