<template>
	<view class="content">
		<scroll-view v-if="true" scroll-y class="data_body" :style="{height:scrollHeight}">
			<!--数据进度条-->
			<view class="progress">
				<data-progress :progressList="userOperateLineBar" :borderRadius="20" padMiddle="true"></data-progress>
			</view>
			<view class="split_line"></view>
			
			<!-- 活跃会员-->
			<view class="friend_operate">
				<view class="title">活跃会员</view>
				<text-block :content="userActive"></text-block>
			</view>
			
			<!-- 会员消费 -->
			<view class="friend_operate"  style="padding-bottom: 30rpx;">
				<view class="title">会员消费</view>
				<text-block :content="userConsume"></text-block>
			</view>
			<view class="split_line"></view>
			
			<!-- 会员ARPU -->
			<view class="friend_operate">
				<view class="title">会员ARPU
					<text class="font-small">(会员年度平均销售金额)</text>
					<text class="trend_title">目标增量{{userARPU.targetAdd}}</text>
				</view>
				<view class="charts-box">
					<qiun-data-charts type="mix"
						:chartData="userARPU" 
						:reshow="delayload"
						:canvas2d="isCanvas2d"
						canvasId="two_a"
						:opts="{yAxis:{data:[{title: '',max:userARPU?userARPU.yAxis[0].max:0,min:userARPU?userARPU.yAxis[0].min:0},{title: '',unit:'%',max:userARPU?userARPU.yAxis[1].max:0,min:userARPU?userARPU.yAxis[1].min:0,position:'right'}]},extra:{markLine:{data:[{value:userARPU?userARPU.target:'',LineColor:'#ff9900',showLabel:true}]}}}"/>
				</view>
			</view>
			<view class="split_line"></view>
			
			<!-- 会员消费频次 -->
			<view class="friend_operate">
				<view class="title">会员消费频次
					<text class="font-middle">(年)</text>
					<text class="trend_title">目标增量{{userARPU.targetAdd}}</text>
				</view>
				<view class="charts-box">
						<qiun-data-charts type="mix"
							:chartData="userARPU" 
							:reshow="delayload"
							:canvas2d="isCanvas2d"
							canvasId="two_b"
							:opts="{yAxis:{data:[{title: '',max:userARPU?userARPU.yAxis[0].max:0,min:userARPU?userARPU.yAxis[0].min:0},{title: '',unit:'%',max:userARPU?userARPU.yAxis[1].max:0,min:userARPU?userARPU.yAxis[1].min:0,position:'right'}]},extra:{markLine:{data:[{value:userARPU?userARPU.target:'',LineColor:'#ff9900',showLabel:true}]}}}"/>
				</view>
			</view>
			<view class="split_line"></view>
			
			<!-- 微客群运营-->
			<view class="friend_operate">
				<view class="title">活跃会员分布
					<text class="font-middle">(最近活跃时间)</text>
				</view>
				<senior-table :headers="dataTable.headers" :contents="dataTable.contents" :urlCol="dataTable.urlCol" :firstLineFixed="true" :sortCol="dataTable.sortCol" :computedCol="dataTable.computedCol" :formatCol="dataTable.formatCol"></senior-table>
			</view>
			<view class="split_line"></view>
			
			<!-- X商品脱落率-->
			<view class="friend_operate">
				<view class="title">
					X（慢病） 商品脱落率
				</view>
				<view style="display: flex;justify-content: space-between;">
					<view v-for="(item,index) in xProductDropPrecent" class="charts-box" style="height: 130px;width: 45%;">
						<qiun-data-charts 
							type="arcbar" 
							:chartData="item" 
							:canvasId="'two_c_'+ index" 
							:canvas2d="true"
							:reshow="delayload"
							:opts="{title:{name:item.series[0].data * 100 + '%',color:item.series[0].color,fontSize:25},subtitle:{name:item.series[0].name,color:'#666666',fontSize:12}}" />
					</view>
				</view>
			</view>
			
			<!-- 慢病病种脱落率-->
			<view class="friend_operate">
				<view class="title">慢病病种脱落率
				</view>
				<view class="charts-box">
					<qiun-data-charts type="mix"
						:chartData="illnessDropPrecent" 
						:reshow="delayload"
						:canvas2d="true"
						canvasId="two_d"
						:opts="{yAxis:{data:[{title: ''}]}}"/>
				</view>
			</view>
			
			<!-- W商品脱落率-->
			<view class="friend_operate">
				<view class="title">
					W（保健） 商品脱落率
				</view>
				<view style="display: flex;justify-content: space-between;">
					<view v-for="(item,index) in wProductDropPrecent" class="charts-box" style="height: 130px;width: 45%;">
						<qiun-data-charts 
							type="arcbar" 
							:chartData="item" 
							:canvasId="'two_e_'+index" 
							:canvas2d="true"
							:reshow="delayload"
							:opts="{title:{name:item.series[0].data * 100 + '%',color:item.series[0].color,fontSize:25},subtitle:{name:item.series[0].name,color:'#666666',fontSize:12}}" />
					</view>
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
	import SeniorTable from "../data-table/senior-table.vue"
	
	import userOperateLineBar from '../../static/json/user-operate/1.json';
	import userActive from '../../static/json/user-operate/2.json';
	import userConsume from '../../static/json/user-operate/3.json';
	import userARPU from '../../static/json/user-operate/4.json';
	import dataTable from "../../static/json/user-operate/6.json"
	import xProductDropPrecent from '../../static/json/user-operate/7.json';
	import wProductDropPrecent from '../../static/json/user-operate/9.json';
	import illnessDropPrecent from '../../static/json/user-operate/8.json';
	
	
	export default {
		name:'user-operate',
		props: {
			scrollHeight:{
				type:String,
				default:"600px"
			}
		},
		components:{
			DataProgress,SeniorTable
		},
		data() {
			return {
				userOperateLineBar,
				userActive,
				userConsume,
				userARPU,
				xProductDropPrecent,
				wProductDropPrecent,
				illnessDropPrecent,
				dataTable,
				delayload:false,
				isCanvas2d:Config.ISCANVAS2D
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
			padding: 30rpx 20rpx;
			.title{
				text-align:left;
				margin-bottom: 30rpx;
				font-size: 40rpx;
			}
			.trend_title{
				float: right;
				font-size: 22rpx;
				color: #ff9900;
				margin-top: 20rpx;
			}
		}
	}
</style>
