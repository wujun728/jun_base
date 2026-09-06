<template>
	<view>
		<view class="top-swiper">
			<view class="box">
				<view style="height: 44px;"></view>
				<swiper class="swiper" :previous-margin="swiper.margin" :next-margin='swiper.margin' :circular="true"
					@change="swiperChange">
					<swiper-item v-for="(item,index) in card_menu" :key="index" @click="toUrl(item.url)">
						<!-- <image class='le-img' :src='item' :class="{'le-active':swiper.index == index}"></image> -->
						<view class="le-img" :class="{'le-active':swiper.index == index}">
							<view class="img_box">
								<image class="card_img" :src="item.img" mode="aspectFill"></image>
							</view>
							<view class="detail_box">
								<view class="title_box">{{item.title}}</view>
								<view class="author_box">By:{{item.author}}</view>
							</view>
						</view>
					</swiper-item>
				</swiper>
			</view>
		</view>
	</view>
</template>

<script>
	import Common from '../../static/js/common'
	export default {
		props: {
			card_menu: {
				type: Array,
				default: []
			}
		},
		data() {
			return {
				swiper: {
					margin: "150rpx",
					index: 0,
					list: [
						"/static/images/douyin/0.jpg",
						"/static/images/douyin/4.jpg",
						"/static/images/douyin/7.jpg",
					]
				}
			}
		},
		components: {

		},
		mounted() {

		},
		methods: {
			//swiper滑动事件
			swiperChange: function(e) {
				this.swiper.index = e.detail.current;
			},
			toUrl(url){
				Common.navigateTo(url);
			}
		}
	}
</script>

<style lang="scss">
	.top-swiper {
		margin-bottom: 30rpx;

		.box {
			padding-top: var(--status-bar-height);
			box-sizing: content-box;
			position: absolute;
			z-index: 5;
			top: 0;
			left: 0;
			width: 100%;
			height: auto;
		}

		.swiper {
			height: 600rpx;
			margin: 0 20rpx;

			.le-img {
				width: 100%;
				height: 100%;
				display: block;
				transform: scale(0.9);
				transition: transform 0.3s ease-in-out 0s;
				border-radius: 10px;
				background-color: #fff;
				overflow: hidden;

				&.le-active {
					transform: scale(1);
				}

				.img_box {
					width: 100%;
					height: 65%;
					overflow: hidden;

					.card_img {
						height: 100%;
						width: 100%;
					}
				}

				.detail_box {
					width: 100%;
					height: 35%;
					overflow: hidden;

					.title_box {
						width: 100%;
						text-align: center;
						font-size: 40rpx;
						margin: 30rpx 0;
					}



					.author_box {
						width: 100%;
						text-align: center;
						font-size: 30rpx;
						position: relative;
						height: 80rpx;
						line-height: 80rpx;

						&::before {
							content: "";
							height: 1px;
							width: 150rpx;
							position: absolute;
							transform: translateX(-50%);
							left: 50%;
							top: 0;
							background: #000;
						}
					}
				}
			}

		}
	}
</style>
