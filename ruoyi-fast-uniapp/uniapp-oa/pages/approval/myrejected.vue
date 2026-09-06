<template>
	<view class="my_approval">
		<uni-swipe-action>
			<ul>
				<li v-for="(item,index) in myApprovalList" :key="index" class="my_approval_li">
					<uni-swipe-action-item :right-options="options"  @click="bindClick($event,item.eventId)" @change="swipeChange($event, index)">
					   <view class="my_approval_item">
							<h3 class="my_approval_title"><text>[{{item.type}}]--{{item.name}}</text> <text class="approval_title_right">{{item.stateName}}</text></h3>
							<view class="my_approval_info">
								<text>{{item.department}}</text>
								<text class="approval_info_right">{{item.time}}</text>
							</view>
						</view>
					   </uni-swipe-action-item>
				</li>
			</ul>
		</uni-swipe-action>
		
	</view>
</template>

<script>
	export default{
		data() {
			return {
				myApprovalList:[{
					type:"请假",
					name:"张三",
					reason:"家里有事",
					stateName:"已驳回",
					state:'-1',
					department:"研发部",
					time:'2021-10-14',
					eventId:'211'
				}],
				options:[
					{
						text: '重新提交',
						style: {
							backgroundColor: '#4cd964'
						},
					}
				]
			}
		},
		methods:{
			bindClick(e,id) {
				if(e.content.text === '重新提交'){
					console.log(id)
				}
			},
			swipeChange(e,index){
				
			}
		}
	}
</script>

<style lang="scss">
	.my_approval{
		.my_approval_li{
			margin-bottom: 20rpx;
			background: #ffffff;
			padding:20rpx;
			border-radius: 8rpx;
		}
		.my_approval_item{
			.my_approval_title{
				display: flex;
				font-size: 14px;
				.approval_title_right{
					font-size: 13px;
					font-weight: 400;
					flex: 1;
					text-align: right;
					color: #dd524d;
				}
			}
			.my_approval_info{
				height: 60rpx;
				line-height: 60rpx;
				font-size: 12px;
				display: flex;
				color: #999;
				.approval_info_right{
					flex: 1;
					text-align: right;
					
				}
			}
		}
	}
</style>


