<template>
	<view class="text_block">
		<template v-for="(item,index) in content">
			<view v-if="item.kind == 1" :key="index" :style="{backgroundImage:'linear-gradient(to top right,'+item.background[0]+','+item.background[1]+')'}" :class="[(index+1)%3==0 ? '':'marginRight','kind','kind_one','breathe-blue']">
				<view class="view_100" :style="{fontSize:item.content[0].size,color:item.content[0].colorvalue}">{{item.content[0].value}}</view>
				<view class="view_100" :style="{fontSize:item.content[1].size,color:item.content[1].colortext}">{{item.content[1].text}}</view>
			</view>
			<view v-else-if="item.kind == 2" :class="[(index+1)%3==0 ? '':'marginRight','kind','kind_two','breathe-blue']" :style="{backgroundImage:'linear-gradient(to top right,'+item.background[0]+','+item.background+')' ,marginRight:(index+1)%3==0?'0rpx':'40rpx'}">
				<view v-for="(content,i) in item.content" :key="i" class="two_1">
					<text :style="{fontSize:content.size,color:content.colortext}">{{content.text}}:</text>
					<text :style="{fontSize:content.size,color:content.colorvalue}">{{content.value}}</text>
				</view>
			</view>
			<view v-else-if="item.kind == 3" class="kind kind_three breathe-blue" :style="{backgroundImage:'linear-gradient(to top right,'+item.background[0]+','+item.background+')'}">
				<view class="view_100" :style="{fontSize:item.content[0].size,color:item.content[0].colorvalue}">{{item.content[0].value}}</view>
				<view class="view_100" :style="{fontSize:item.content[1].size,color:item.content[1].colortext}">{{item.content[1].text}}</view>
				<view class="three_3 view_100">
					<view v-for="(j,i) in 2" :key="i">
						<text :style="{fontSize:item.content[i+2].size,color:item.content[i+2].colortext}">{{item.content[i+2].text}}:</text>
						<text :style="{fontSize:item.content[i+2].size,color:item.content[i+2].colorvalue}">{{item.content[i+2].value}}</text>
					</view>
				</view>
			</view>
			<view v-else-if="item.kind == 4" :class="[(index+1)%3==0 ? '':'marginRight','kind','kind_four','breathe-blue']" :style="{backgroundImage:'linear-gradient(to top right,'+item.background[0]+','+item.background[1]+')',marginRight:(index+1)%3==0?'0rpx':'40rpx'}">
				<view class="view_100" :style="{fontSize:item.content[0].size,color:item.content[0].colortext}">{{item.content[0].text}}</view>
				<view class="view_100" :style="{fontSize:item.content[1].size,color:item.content[1].colorvalue}">{{item.content[1].value}}</view>
				<view class="four_3" :style="{fontSize:item.content[2].size,color:item.content[2].colorvalue}">
					<text :style="{fontSize:item.content[3].size,color:item.content[3].colorvalue}">{{item.content[2].text}}</text>
					<li v-if="item.content[3].text == 'up'" class="iconfont icon-up icon li_1" :style="{color:item.content[3].colortext}"></li>
					<li v-else class="iconfont icon-down icon li_1" :style="{color:item.content[3].colortext}"></li>
					<text :style="{fontSize:item.content[3].size,color:item.content[3].colorvalue}">{{item.content[3].value}}</text>
				</view>
			</view>
			<view v-else-if="item.kind == 5" :class="[(index+1)%3==0 ? '':'marginRight','kind','kind_five','breathe-red']" :style="{backgroundImage:'linear-gradient(to top right,'+item.background[0]+','+item.background[1]+')',marginRight:(index+1)%3==0?'0rpx':'40rpx'}">
				<view class="view_100" :style="{fontSize:item.content[0].size,color:item.content[0].colortext}">{{item.content[0].text}}</view>
				<view class="view_100" :style="{fontSize:item.content[1].size,color:item.content[1].colorvalue}">{{item.content[1].value}}</view>
				<view class="five_3 view_100">
					<view v-for="(j,i) in 2" :key="i">
						<text :style="{fontSize:item.content[j+2].size,color:item.content[j+2].colortext}">{{item.content[j+2].text}}</text>
						<text :style="{fontSize:item.content[j+2].size,color:item.content[j+2].colorvalue}">{{item.content[j+2].value}}</text>
					</view>
				</view>
				<view class="five_4">
					<text :style="{fontSize:item.content[4].size,color:item.content[4].colortext}">{{item.content[4].text}}</text>
					<text :style="{fontSize:item.content[4].size,color:item.content[4].colorvalue}">{{item.content[4].value}}</text>
				</view>
			</view>
		</template>
	</view>
</template>

<script>
	export default {
		props:{
			content: {
				type: Array,
				default: []
			},
		},
		data() {
			return {
			}
		},
		methods: {
			
		},
		mounted() {
			uni.onWindowResize((res) => {
			    console.log('变化后的窗口宽度=' + res.size.windowWidth)
			    console.log('变化后的窗口高度=' + res.size.windowHeight)
			})
		}
	}
</script>

<style lang="scss">
	.text_block{
		display: flex;
		width: 100%;
		flex-wrap: wrap;
		.marginRight{
			margin-right: 36rpx!important;
		}
		.view_100{
			width: 100%;
		}
		.CPT_DYBG {
		  overflow: hidden;
		  position: relative;
	    }
		.kind{
			width: 24%;
			padding: 10rpx;
			margin-bottom: 40rpx;
			display: flex;
			justify-content: center;
			flex-wrap: wrap;
			border-radius: 16rpx;
			margin-left: 16rpx;
			box-shadow: -4px 4px 4px #ccc;
			position: relative;
		}
		.kind_one{
			border-radius: 10rpx;
		}
		.kind_two{
			border-radius: 10rpx;
		}
		.kind_three{
			border-radius: 16rpx;
			width: 300rpx;
			margin: 0 auto;
			.three_3{
				display: flex;
				justify-content: space-around;
			}
		}
		.kind_four{
			border-radius: 10rpx;
			.four_3{
				display: flex;
				.li_1 {
					 list-style-type:none;
				}
				.icon{
					margin-top: -8rpx;
					transform: scale(0.8);
				}
			}
		}
		.kind_five{
			border-radius: 20rpx;
			.five_3{
				view{
					width: 100%;
				}
			}
		}
	}
	// .breathe-blue {
	// 	position:relative;
	// 	color:#fff;
	// 	text-align:center;
	// 	cursor:pointer;
	// 	box-shadow:0 1px 2px rgba(0,0,0,.3);
	// 	overflow:hidden;
	// 	-webkit-animation-timing-function:ease-in-out;
	// 	-webkit-animation-name:breatheblue;
	// 	-webkit-animation-duration:2000ms;
	// 	-webkit-animation-iteration-count:infinite;
	// 	-webkit-animation-direction:alternate;
	// }
	// @keyframes breatheblue {
	// 	0% {
	// 		opacity:.8;
	// 		box-shadow:0 1px 2px rgba(62,178,245,0.5);
	// 	}
	// 	100% {
	// 		opacity:1;
	// 		box-shadow:0 1px 30px rgba(147,116,247,0.6);
	// 	}
	// }
	// .breathe-red {
	// 	position:relative;
	// 	color:#fff;
	// 	text-align:center;
	// 	cursor:pointer;
	// 	box-shadow:0 1px 2px rgba(0,0,0,.3);
	// 	overflow:hidden;
	// 	-webkit-animation-timing-function:ease-in-out;
	// 	-webkit-animation-name:breathered;
	// 	-webkit-animation-duration:2000ms;
	// 	-webkit-animation-iteration-count:infinite;
	// 	-webkit-animation-direction:alternate;
	// }
	// @keyframes breathered {
	// 	0% {
	// 		opacity:.8;
	// 		box-shadow:0 1px 2px rgba(247,126,137,0.5);
	// 	}
	// 	100% {
	// 		opacity:1;
	// 		box-shadow:0 1px 30px rgba(247,149,59,0.9);
	// 	}
	// }
</style>
