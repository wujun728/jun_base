<template>
	<view class="container">	
		<view class="home">
			<ul  class="menu_ul">			
				<li class="menu_li" v-for="(item,index) in menuList" @click="toNav(item.nav)" :key="index" :style="{'background':item.bg}">
					<image :src="item.iconSrc"></image>
						<text>{{item.name}}</text>
				</li>						
			</ul>
		</view>
		<!-- 轮播图 -->
		<view class="home_swiper">
			<v-swiper></v-swiper>
		</view>
		<!-- 新闻动态 -->
		<view class="home_news">
			<v-news></v-news>
		</view>
		
	</view>
</template>
<script>
	import VSwiper from './swiper.vue'
	import VNews from './news.vue'
	export default {
		components:{ VSwiper,VNews },
		data() {
			return {
				menuList:[{
					name:"通知",
					id:'001',
					nav:"info-notice",
					iconSrc:"../../static/info.png",
					bg:"#F39C12"
				},{
					name:"通讯录",
					id:'002',
					nav:"address-book",
					iconSrc:"../../static/tongxunlu.png",
					bg:"#9285f9"
				},{
					name:"组织架构",
					id:'003',
					nav:"organition-structure",
					iconSrc:"../../static/zhuzhijiagou.png",
					bg:"#ee2130"
				},{
					name:"共享文件",
					id:'004',
					nav:"shared-files",
					iconSrc:"../../static/files.png",				
					bg:"#25c4ec"
				},{
					name:"考勤打卡",
					id:'005',
					nav:'attendance-clock',
					iconSrc:"../../static/kaoqindaka.png",
					bg:"#91cc5f"
				},{
					name:"邮箱",
					id:'006',
					nav:"mail-box",
					iconSrc:"../../static/email.png",
					bg:"#409eff"
				}]
			}
		},
		mounted() {
			
		},
		methods: {
			toNav(nav) {
				uni.navigateTo({
				    url: 'childpage/'+nav+'/'+nav,
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
	.container {
		width: 100%;
		height: 100%;
		background: #f5f6f7;
		.home {
			padding: 10px 20px;
			background: #ffffff;
			margin-bottom: 10px;
			.menu_ul {
				display: flex;
				flex-wrap: wrap;
	
				.menu_li {
					width:120rpx;
					height: 120rpx;
					// background:#F39C12;
					display: flex;
					flex-direction: column;
					justify-content:center;
					align-items: center;
					align-content:center;
					border-radius: 16rpx;
					margin-top: 20rpx;
					margin-bottom: 20rpx;
					margin-right: 62rpx;
				}
				.menu_li:nth-child(4n){
					margin-right: 0;
				}
				.menu_li:nth-child(4n+1){
					margin-left: 0;
				}			
				image {
					width:28px;
					height: 28px;
					margin-top: 4px;
				}
				text {
					color: #ffffff;
					font-size: 12px;
				}
			}
		}
		.home_swiper{
			padding: 10px 20px !important;
			background: #ffffff;
		}
		.home_news{
			margin-top: 10px;
			width: 100%;
		}
		
	}
	@media screen and (max-width: 320px) {
		.container {
			.home {
				.menu_ul {
					display: flex;
					flex-wrap: wrap;
		
					.menu_li {
						width:120rpx;
						height: 120rpx;
						background:#F39C12;
						display: flex;
						flex-direction: column;
						justify-content:center;
						align-items: center;
						align-content:center;
						border-radius: 16rpx;
						margin-top: 20rpx;
						margin-bottom: 20rpx;
						margin-right: 60rpx;
					}
					.menu_li:nth-child(4n){
						margin-right: 0;
					}
					.menu_li:nth-child(4n+1){
						margin-left: 0;
					}
					
					image {
						width:28px;
						height: 28px;
						margin-top: 4px;
					}
					text {
						color: #ffffff;
						font-size: 12px;
					}
				}
			}
			
		}
	}

</style>
