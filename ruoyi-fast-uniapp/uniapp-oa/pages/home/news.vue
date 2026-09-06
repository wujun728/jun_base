<template>
	<view class="news">
		<uni-card
		    title="新闻动态"
		    mode="title"
		    :is-shadow="true"
		>
		
		 
		       <uni-list :border="true">
		           <uni-list-chat v-for="(item,index) in newsList" :key="item.id" :title="item.title" :avatar="item.img" :note="item.description" :time="item.time"></uni-list-chat>
		       </uni-list>
		   
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				newsList:[{
					title:'深圳最新台风',
					id:'1013',
					img:'https://tse2-mm.cn.bing.net/th/id/OIP-C.LXUhWhoOAUCw7vlQmBXq8wHaFB?pid=ImgDet&rs=1',
					description:'圆规台风好像走了',
					time:'2020-10-13 20:20'				
				},
				{
					title:'张三牛逼666',
					id:'1014',
					img:'https://picnew11.photophoto.cn/20170828/niubi-28562874_1.jpg',
					description:'6的飞起',
					time:'2020-10-13 20:20'				
				}]
			}
		}
	}
</script>

<style lang="scss" scoped>
	.news{
		width: 100%;
		background: #ffffff;
		padding: 10px 0;
		.uni-list-chat{
			/deep/ .uni-list-chat__container{
				padding: 10px 0px !important;
			}
			/deep/ .uni-list--border{
				margin-left: 0;
			}
		}
		
		
	}
	
</style>
