<template>
	<view class="swiper_view">
		<view class="uni-padding-wrap">
			<view class="page-section swiper">
				<view class="page-section-spacing">
					<swiper class="swiper" indicator-dots="true" autoplay="true" :interval="interval" >
						<swiper-item v-for="(item,index) in swiperList" :key="index">
							<view class="swiper-item-bg" :style="{'background': 'url(' + item.bgUrl +')'}"></view>
						</swiper-item>
					</swiper>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				interval: 3000,
				swiperList:[{
					bgUrl:'https://tse1-mm.cn.bing.net/th/id/OIP-C.lrWWUV-KPDeqGJO0AJ7oigHaEK?pid=ImgDet&rs=1',
					name:'苹果手机'
				},{
					bgUrl:'https://tse1-mm.cn.bing.net/th/id/OIP-C.EoNqCiechRoJcB4YV4aT7QHaE8?pid=ImgDet&rs=1',
					name:'苹果手机'
				},{
					bgUrl:'https://tse3-mm.cn.bing.net/th/id/OIP-C.POaC8o6sLUhp3UmT3_NNqQHaDt?pid=ImgDet&rs=1',
					name:'广告'
				}]
			}
		}
	}
</script>

<style lang="scss">
	.swiper-item-bg{
		width:100%;
		height:100%;
		background-size: cover !important;
	}
</style>
