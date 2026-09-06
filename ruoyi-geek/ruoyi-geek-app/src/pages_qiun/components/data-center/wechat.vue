<template>
	<view class="content">
		<scroll-view v-if="true" scroll-y class="data_body" :style="{height:scrollHeight}">
			<!--数据进度条-->
			<view class="progress">
				<data-progress :progressList="wechatLineBar" :borderRadius="20" padMiddle="true"></data-progress>
			</view>
			<view class="split_line"></view>
			
			<!-- 微好友运营-->
			<view class="friend_operate">
				<view class="title">微好友运营</view>
				<text-block :content="friendTextBlock"></text-block>
				<view style="display: flex;justify-content: space-around;">
					<view v-for="(item,index) in panelData" :key="index" class="charts-box"
						style="width: 45%;height: 200px;">
						<qiun-data-charts type="gauge"
							:opts="{title:{name: item.series[0].data * 100 + '%',color: '#24ABFD',offsetY:30},subtitle: {name: item.series[0].name,color: '#666666',fontSize: 15,offsetY:70},extra:{gauge:{type:'progress',width:20,splitLine:{fixRadius:-10,width:15,},}}}"
							:chartData="item" :reshow="delayload" :canvas2d="isCanvas2d" :canvasId="'one_a_' + index" />
					</view>
				</view>
				<view class="trend_title">新增微好友&小程序会员趋势</view>
				<view class="charts-box">
					<qiun-data-charts type="mix" :chartData="friendTrand" :reshow="delayload"
						:canvas2d="isCanvas2d" canvasId="one_b" :opts="{yAxis:{data:[{title: ''}]}}" />
				</view>
			</view>
			<view class="split_line"></view>
			
			<!-- 微客群运营-->
			<view class="friend_operate">
				<view class="title">微客群运营</view>
				<text-block :content="friendTextBlock"></text-block>
				<view class="trend_title">新增人群&退群人数趋势</view>
				<view class="charts-box">
					<qiun-data-charts type="mix" :chartData="teamTrand" :reshow="delayload" :canvas2d="isCanvas2d"
						canvasId="one_c" :opts="{yAxis:{data:[{title: '',max:teamTrand?teamTrand.yAxis[0].max:0,min:teamTrand?teamTrand.yAxis[0].min:0}]}}" />
				</view>
			</view>
			<view class="split_line"></view>
			
			<!-- 客户联系-->
			<view class="friend_operate">
				<view class="title">【客户联系】1对1运营执行</view>
				<senior-table :headers="dataTable.headers" :contents="dataTable.contents" :urlCol="dataTable.urlCol" :firstLineFixed="true" :sortCol="dataTable.sortCol"></senior-table>
			</view>
		</scroll-view>
		<view  v-else class="container padding_stand-big normal_color">
			<li class="iconfont icon-cry cry"></li>暂无数据
		</view>
	</view>
</template>

<script>
	import DataProgress from "../data-progress/data-progress.vue"
	import SeniorTable from "../data-table/senior-table.vue"
	
	import wechatLineBar from '../../static/json/wechat/1.json';
	import friendTextBlock from '../../static/json/wechat/2.json';
	import panelData from '../../static/json/wechat/3.json';
	import friendTrand from '../../static/json/wechat/4.json';
	import teamTrand from '../../static/json/wechat/5.json';
	import dataTable from "../../static/json/wechat/6.json"

	import Config from '../../static/js/config'
	
	export default {
		name:'wechat',
		props: {
			scrollHeight:{
				type:String,
				default:"600px"
			}
		},
		components:{
			DataProgress,
			SeniorTable,
		},
		data() {
			return {
				wechatLineBar,
				friendTextBlock,
				friendTrand,
				panelData,
				teamTrand,
				dataTable,
				scrollTop: 0,
				delayload: false,
				isCanvas2d: Config.ISCANVAS2D,
			}
		},
		mounted() {
			this.getData();
		},
		methods:{
			async getData() {
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
			padding: 30rpx 20rpx;
			.title{
				text-align:left;
				margin-bottom: 30rpx;
				font-size: 40rpx;
			}
			.trend_title{
				text-align: right;
				font-size: 22rpx;
				color: #ff9900;
				margin-top: 50rpx;
			}
		}
	}
	
</style>
