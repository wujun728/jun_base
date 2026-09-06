<template>
	<view>
		<view class="aboutme">
			<view class="headPortrait">
				<uni-list :border="false">
				    <uni-list-item :title="accountInfo.realName" :note="accountInfo.position" :thumb="accountInfo.headPortrait"
				     thumb-size="lg"></uni-list-item>
				</uni-list>
			</view>
			<view class="info_view">
				<uni-list>
				    <uni-list-item v-for="(item,index) in infoList" :key="index" :title="item.key" :rightText="item.value" ></uni-list-item>
				</uni-list>

			</view>
			<view class="changepsd">
				修改密码
			</view>
			<view class="loginout">
				退出登录
			</view>
		</view>
		 
	</view>
</template>

<script>
	export default {
		data() {
			return {
				accountInfo:{
					realName:"张三",
					position:"高级Python开发工程师",
					headPortrait:'https://tse2-mm.cn.bing.net/th/id/OIP-C.ngq7YSpDjFAqf7P68Q008QHaEK?pid=ImgDet&rs=1'
				},
				infoList:[{
					key:'手机号',
					value:'18818586666'
				},{
					key:'生日',
					value:'1995-12-30'
				},{
					key:'入职时间',
					value:'2020-02-28'
				}]
				
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="scss">
	.aboutme{
		padding: 20rpx 40rpx;
		.headPortrait{
			.uni-list-item{
				.uni-list--lg{
					width:100rpx;
					height: 100rpx;
				}
			}
		}
		.info_view{
			margin: 20rpx 0;	
		}
		.changepsd{
			height: 84rpx;
			line-height: 84rpx;
			background: #FFFFFF;
			margin-top: 80rpx;
			text-align: center;
			font-size: 14px;
		}
		.loginout{
			height: 84rpx;
			line-height: 84rpx;
			background: #FFFFFF;
			margin-top: 20rpx;
			text-align: center;
			font-size: 14px;
		}
	}
	
</style>
