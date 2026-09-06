<template>
	<view class="container">
		<view class="address_detail">
			<view class="">
				
			</view>
			<uni-list :border="true">
			    <uni-list-chat  :title="name" :avatar="person.img" :note="key"></uni-list-chat>
			</uni-list>
			<view class="address_content">
				<uni-list>
					<uni-list-item :showArrow="false" :title="eNameTitle"></uni-list-item>
				    <uni-list-item :showArrow="false" :title="positionTitle"></uni-list-item>
					<uni-list-item :showArrow="false" :title="telTitle"></uni-list-item>
					<uni-list-item :showArrow="false" :title="emailTitle"></uni-list-item>
				</uni-list>	
			</view>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return{
				key:'',
				name:'',
				person:{
					key:'生产部',
					name:'李四',
					img:'https://tse1-mm.cn.bing.net/th/id/R-C.150b7e79e4770357d61e9179095eb4a8?rik=x9%2fYOfmF27CQDA&riu=http%3a%2f%2fpic.ntimg.cn%2ffile%2f20180815%2f26607894_202823713037_2.jpg&ehk=boHWVyey7i%2b6v0s31OMrAemA%2fqdS3gBhMc1tN9065uc%3d&risl=&pid=ImgRaw&r=0',
					position:"XXX",
					email:"10086@swiss.com.cn",
					tel:'18856782358',
					eName:'Jerry'
				}
			}
		},
		computed:{
			eNameTitle:function() {
				return '英文名：' + this.person.eName
			},
			positionTitle:function() {
				return '职务：' + this.person.position
			},
			telTitle:function() {
				return '职务：' + this.person.tel
			},
			emailTitle:function() {
				return '职务：' + this.person.email
			}
		},
		onLoad(options) {
			this.key = options.key
			this.name = options.name
		},
		onShow(){
			
			
		},
		methods:{
			
		}
	}
</script>

<style lang="scss">
	.address_detail{
		padding: 20rpx 40rpx;
		.address_content{
			margin-top: 20rpx;
		}
	}
</style>
