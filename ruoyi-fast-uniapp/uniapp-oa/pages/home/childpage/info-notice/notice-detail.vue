<template>
	<view class="content">
		<view class="notice_detail">
			<h3 class="notice_title">{{detail.title}}</h3>
			<image :src="detail.img" v-if="detail.img"></image>
			<p class="notice_content">{{detail.detail}}</p>
			<p class="notice_signature">{{detail.signature}}</p>
			<p class="notice_date">{{detail.date}}</p>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				nocice_id:'',
				detail:null,
				commonInfo:[{
					title:'放假通知',
					id:'4000',
					img:'',
					description:'经公司决定，圣诞节放假3天，放假时间为10月28-10月30',
					detail:'经公司决定，圣诞节放假3天，放假时间为10月28-10月30。另外加班的同事，可享受3倍工资。当然原则上是不鼓励加班，望各位同事知晓。祝大家节日愉快。',
					signature:'行政办公室',
					time:'2020-10-19 20:20',
					date:'2020-10-19'
				},{
					title:'优秀员工',
					id:'5000',
					img:'https://picnew11.photophoto.cn/20170828/niubi-28562874_1.jpg',
					detail:'员工刘亦非表现6的飞起,工作认真，负责；工作认真负责，爱岗敬业，服从整体安排，形象良好；对本职工作兢兢业业，锐意进取，乐于助人，关心同事，与同事相处融洽，善于合作，起带头作用。现决定将他提拔为研发总监。望大家多向他学习,努力进取。',
					signature:'行政办公室',
					time:'2020-10-19 20:20',
					date:'2020-10-16'
					
				},{
					title:'审核驳回',
					id:'6000',
					img:'',
					detail:'请假理由不充分，近期项目紧，非必要不要请假',
					signature:'研发部：研发经理',
					time:'2020-10-19 20:20',
					date:'2020-10-20'
				}],
				
			}
		},
		onLoad(options) {
			this.notice_id = options.id
			this.getNoticeDetail()
		},
		methods:{
			getNoticeDetail(){
				this.detail = this.commonInfo.filter(item =>{
					return item.id === this.notice_id
				})[0]
				console.log(this.detail)
			}
			
		}
	}
</script>

<style lang="scss">
	.notice_detail{
		padding: 20rpx 40rpx;
		.notice_title{
			text-align: center;
			font-size: 14px;
			height: 48rpx;
			line-height: 48rpx;
		}
		uni-image{
			width:100%;
			height: 480rpx;
			margin: 20rpx 0;
			text-align: center;
		}
		.notice_content{
			font-size: 13px;
			text-indent: 2em;
			line-height: 1.8em;
		}
		.notice_signature{
			margin-top: 40rpx;
			font-size: 12px;
			text-align: right;
			height: 40rpx;
			line-height: 40rpx;
		}
		.notice_date{
			text-align: right;
			font-size: 12px;
			height: 40rpx;
			line-height: 40rpx;
		}
	}
</style>
