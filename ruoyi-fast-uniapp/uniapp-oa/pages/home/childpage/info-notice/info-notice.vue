<template>
	<view class="container">
		<view class="notice">
			<uni-group title="系统通告" top="20">
			    <uni-list :border="true">
			        <uni-list-chat v-for="(item,index) in infoList.commonInfo" :key="item.id" :title="item.title" :avatar="item.img" :note="item.description" @click="toDetail(item.id)" link></uni-list-chat>
			    </uni-list>
			</uni-group>
			<uni-group title="个人通知" top="20">
			    <uni-list>
			        <uni-list-item v-for="(item,index) in infoList.userInfo" :key="item.id" :ellipsis="1" :title="item.title" @click="toDetail(item.id)" link></uni-list-item>
				</uni-list>	
			</uni-group>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				infoList:{
					commonInfo:[{
					title:'放假通知',
					id:'4000',
					img:'',
					description:'经公司决定，圣诞节放假3天，放假时间为10月28-10月30',
					time:'2020-10-19 20:20'				
				},{
					title:'优秀员工',
					id:'5000',
					img:'https://picnew11.photophoto.cn/20170828/niubi-28562874_1.jpg',
					description:'表现6的飞起',
					time:'2020-10-19 20:20'				
				}],
				userInfo:[{
					title:'审核驳回',
					id:'6000',
					img:'https://picnew11.photophoto.cn/20170828/niubi-28562874_1.jpg',
					description:'你的请假申请被驳回',
	
					time:'2020-10-19 20:20'		
				}
					
				]
				}
			}
		},
		methods: {
			toDetail(id){
				uni.navigateTo({
				    url: 'notice-detail?id='+id,
				})
			}
		}
	}
</script>

<style lang="scss">
	.container{
		width: 100%;
		height: 100%;
		background: #f5f6f7;
		.notice{
			/deep/ .uni-group{
				margin-top: 0 !important;
			}
			.uni-list{
				
				/deep/ .uni-list-chat__container{
					padding: 10px 0px !important;
					.uni-list-chat__content-title{
						font-size: 14px !important;
					}
				}
				/deep/ .uni-list--border{
					margin-left: 0;
				}
				/deep/ .uni-list-item__container{
					padding: 10px 0px !important;
				}
			}
		}
		
	}
</style>
