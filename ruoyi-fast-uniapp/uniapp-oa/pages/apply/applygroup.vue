<template>
	<view class="applygroup">
		<uni-group :title="groupInfo.groupName" top="10" mode="card">
		    <uni-grid :column="4" :showBorder="false">
		        <uni-grid-item  v-for="(item,index) in groupInfo.groupList" :key="item.id">
					<view class="group_item" @click="toKaoQin(item.route)">
						<image :src="item.icon" class="group_item_img"></image>
						<text class="group_item_text">{{item.name}}</text>
					</view>					
		        </uni-grid-item>
		    </uni-grid>
		</uni-group>
	</view>
</template>

<script>
	export default{
		props:{
			groupInfo:{
				type:Object,
				required:true,
			}
		},
		data() {
			return {
				
			}
		},
		methods:{
			toKaoQin(route){
				uni.navigateTo({
				    url: 'childpage/'+route + '/'+route,
					success:(res) =>{					
					},
					fail:(err) =>{
						console.log(err)
					}
				});
			}
		}
	}
</script>

<style lang="scss">
	.applygroup{
		padding: 20rpx;
		.group_item{
			height: 100%;
			font-size: 13px;
			display: flex;
			flex-direction: column;
			justify-content:center !important;
			align-items:center !important;	
			.group_item_img{
				width:64rpx;
				height: 64rpx;
			}
			.group_item_text{
				margin-top: 8rpx;
			}			
		}	 
	}
	
</style>
