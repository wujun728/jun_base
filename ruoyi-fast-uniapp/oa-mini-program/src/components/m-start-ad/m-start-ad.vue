<template>
	<view v-if="show" class="start">
		<view class="skip" :style="skipPositionStyle" @click="onSkip">跳过 {{time2}}</view>
		<swiper class="swiper" :interval="interval" @change="onChangeSwiper">
			<swiper-item v-for="(item, index) in list" :key="index">
				<view class="swiper-item" :style="bgStyle">
					<image class="image" :src="item" mode="widthFix"></image>
					<view class="after" :style="afterStyle"></view>
				</view>
			</swiper-item>
			<!-- autoplay -->
		</swiper>
		<view v-if="list.length>1" class="swiper-dot">
			<view v-for="(item, index) in list" :key="index" class="view"
				:style="index === current ? currentStyle : ''" :class="{'active': index === current}" />
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			list: {
				type: Array,
				default () {
					return []
				},
				required: true
			},
			hasTabbar: {
				type: Boolean,
				default: false,
			},
			hasNavbar: {
				type: Boolean,
				default: false,
			},
			interval: {
				type: Number,
				default: 3000
			},
			time: {
				type: Number,
				default: 3
			},
			url: {
				type: String,
				default: ''
			},
			bgColor: {
				type: String,
				default: '#BB1219'
			},
			afterColor: {
				type: String,
				default: ''
			},
			currentColor: {
				type: String,
				default: '#BB1219'
			}
		},
		data() {
			return {
				current: 0,
				show: true,
				timer: null,
				time2: this.time
			}
		},
		computed: {
			bgStyle() {
				return this.obj2strStyle({
					'background-color': this.bgColor
				})
			},
			afterStyle() {
				return this.obj2strStyle({
					'background-color': this.afterColor
				})
			},
			currentStyle() {
				return this.obj2strStyle({
					'background-color': this.currentColor
				})
			},
			skipPositionStyle() {
				const {
					statusBarHeight
				} = uni.getSystemInfoSync()
				if (!this.hasNavbar) {
					return this.obj2strStyle({
						'top': `${statusBarHeight*2 + 88 + 30}rpx`
					})
				}
				return this.obj2strStyle({
					'top': '30rpx'
				})
			}
		},
		watch: {
			time2(val) {
				if (val <= 0) {
					this.onSkip()
				}
			}
		},
		mounted() {
			if (this.hasTabbar) {
				uni.hideTabBar()
			}
			this.timer = setInterval(() => {
				this.time2--
			}, 1000)
		},
		methods: {
			obj2strStyle(obj) {
				let style = ''
				for (let key in obj) {
					style += `${key}:${obj[key]};`
				}
				return style
			},
			onSkip() {
				const {
					url,
					hasTabbar,
					timer
				} = this
				clearTimeout(timer)
				this.show = false
				if (hasTabbar) {
					uni.showTabBar()
				}
				if (url) {
					uni.reLaunch({
						url: url,
					})
				}
			},
			onChangeSwiper(e) {
				this.current = e.detail.current
			}
		}
	}
</script>

<style scoped>
	/* $nav-height: 44px; */
	.start {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		z-index: 9999;
	}

	.skip {
		position: absolute;
		z-index: 2;
		background-color: rgba(0, 0, 0, 0.1);
		color: #fff;
		right: 30rpx;
		font-size: 28rpx;
		width: 133rpx;
		height: 60rpx;
		border-radius: 44rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.swiper {
		height: 100vh;
		width: 100vw;
	}

	.swiper-item {
		height: 100vh;
		width: 100vw;
		display: flex;
		justify-content: center;
		align-items: center;
		overflow: hidden;
		background-color: transparent;
	}

	.swiper-item .after {
		width: 100vw;
		height: 500rpx;
		position: absolute;
		left: 0;
		bottom: 0;
		z-index: 1;
	}

	.swiper-item .image {
		height: 100vh;
		width: 100vw;
		display: block;
		position: relative;
		z-index: 2;
	}

	.swiper-dot {
		position: absolute;
		width: 100vw;
		left: 0;
		bottom: 100rpx;
		z-index: 3;
		display: flex;
		justify-content: center;
	}

	.swiper-dot .view {
		width: 16rpx;
		height: 16rpx;
		border-radius: 100%;
		background-color: rgba(0, 0, 0, 0.2);
		margin: 0 12rpx;
	}

	.swiper-dot .view.active {
		width: 30rpx;
		border-radius: 24rpx;
	}
</style>
