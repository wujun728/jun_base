<template>
	<view>
		<view class="ranking">
			<view class="ranking-item" v-for="(content,index) in content" :key="index">
				<view class="name">{{content.name}}</view>
				<view class="progress" >
					<text :style="{backgroundImage:'linear-gradient(to top right,'+getColor(0,index)+','+getColor(1,index)+')',width:content.width + '%'}"></text>
				</view>
				<view class="num">{{content.num}}</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default{
		name:'ranking-list',
		props:{
			content:{
				type: Array,
				default() {
					return []
				}
			}
		},
		methods:{
			init(){
				if(this.content && this.content.length >0){
					this.content = this.content.sort((a,b) => b.num - a.num);
					let max = this.content[0].num;
					this.content.map((item,index) =>{
						item.width = this.computeWidth(max,item.num);
					});
					this.$emit("updateRanking",this.content)
				}
			},
			computeWidth(max,current){
				let num = (current / max) * 100;
				return num.toFixed(2);
			},
			getColor(id,index){
				let color = "";
				switch(index){
					case 0:
						color = id == 0 ? "#fff":"#A80BFC";
						break;
					case 1:
						color = id == 0 ? "#fff":"#740CFF";
						break;
					case 2:
						color = id == 0 ? "#fff":"#4A08DC";
						break;
					default :
						color = id == 0 ? "#fff":"#1936DA";
						break;
				}
				return color;
			},
		},
		mounted() {
			this.init();
		}
	}
</script>

<style scoped lang="scss">
	.ranking-item{
		display: flex;
		margin-bottom: 13rpx;
		padding: 10rpx;
		align-content: center;
		
		.name{
			padding-right: 10rpx;
			color: #868688;
		}
		.progress{
			flex:5;
			text-align: left;
			padding-right: 10rpx;
			
			text{
				display: inline-block;
				height: 100%;
			}
			
		}
		.num{
			font-size: 26rpx;
			color: #3EB2F5;
		}
	}
</style>
