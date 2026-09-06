<template>
	<div class="progress">
		<canvas canvas-id="canvas" :width="width" :height="height"></canvas>
		<view class="data">
			<view class="item mb">
				<view class="point blue"></view>
				<view class="text">正常人数</view>
				<view class="num suc">{{ normal }}</view>
			</view>
			<view class="item">
				<view class="point red"></view>
				<view class="text">异常人数</view>
				<view class="num err">{{ abnormal }}</view>
			</view>
		</view>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				// canvas 宽度
				width: 300,
				// canvas 高度
				height: 130,
				// 圆弧半径
				radius: 114,
				// 圆弧宽度
				strokeWidth: 15,
				// 圆弧进度
				progress: 0,
				// 渐变色起始颜色
				gradientStartColor: "#C1DDFD",
				// 渐变色结束颜色
				gradientEndColor: "#308BF8",
				init: 0,
			};
		},
		props: {
			// 正常人数
			normal: {
				type: Number,
				default: 0,
			},
			// 异常人数
			abnormal: {
				type: Number,
				default: 0,
			},
			// 总人数
			total: {
				type: Number,
				default: 0,
			},
		},
		created() {
			this.initData();
			this.$watch('normal', this.handleNormalChange);
			this.$watch('total', this.handleTotalChange);
		},
		methods: {
			handleNormalChange(newVal, oldVal) {
				if (newVal !== oldVal) {
					this.init = 0; // 重置init的值为0
					this.initData();
				}
			},
			handleTotalChange(newVal, oldVal) {
				if (newVal !== oldVal) {
					this.init = 0; // 重置init的值为0
					this.initData();
				}
			},
			initData() {
				if (this.normal > 0 && this.total > 0) {
					this.progress = (this.normal / this.total).toFixed(2);
				} else {
					this.progress = 0;
				}
				this.draw();
			},
			draw() {
				this.init += 0.01;
				if (this.init <= this.progress) {
					this.drawProgress();
					setTimeout(() => {
						this.draw();
					}, 5);
				} else {
					this.drawProgress();
				}
			},
			drawProgress() {
				const ctx = uni.createCanvasContext("canvas");

				// 清空画布
				ctx.clearRect(0, 0, this.width, this.height);

				// 绘制背景圆弧
				ctx.beginPath();
				const startAngle = Math.PI;
				const endAngle = 2 * Math.PI;
				ctx.arc(this.width / 2, this.height, this.radius, startAngle, endAngle);
				ctx.lineWidth = this.strokeWidth;
				//   ctx.lineCap = "round";

				ctx.strokeStyle = "#E5E9F2";
				ctx.stroke();

				// 绘制进度圆弧
				ctx.beginPath();
				const progressAngle = startAngle + this.init * Math.PI;
				ctx.arc(
					this.width / 2,
					this.height,
					this.radius,
					startAngle,
					progressAngle
				);
				const gradient = ctx.createLinearGradient(
					this.width / 2 - this.radius,
					this.height,
					this.width / 2 + this.radius,
					this.height
				);
				gradient.addColorStop(0, this.gradientStartColor);
				gradient.addColorStop(1, this.gradientEndColor);
				ctx.strokeStyle = gradient;
				ctx.stroke();
				ctx.draw();
			},
		},
		watch: {
			progress() {
				this.drawProgress();
			},
		},
	};
</script>
<style lang="scss">
	uni-canvas {
		width: 300px;
		margin: auto;
	}

	.progress {
		position: relative;

		.data {
			position: absolute;
			top: 170rpx;
			left: 50%;
			transform: translate(-50%, -50%);

			.item {
				display: flex;
				align-items: center;
				justify-content: center;

				.point {
					width: 12rpx;
					height: 12rpx;
					border-radius: 50%;
					margin-right: 16rpx;
				}

				.blue {
					background-color: #308bf8;
				}

				.red {
					background-color: #ed4014;
				}

				.text {
					font-size: 26rpx;
					font-weight: 400;
					color: #909399;
					line-height: 26rpx;
					margin-right: 10rpx;
				}

				.num {
					font-size: 44rpx;
					font-weight: 500;
					line-height: 44rpx;
				}

				.suc {
					color: #308bf8;
				}

				.err {
					color: #ed4014;
				}
			}

			.mb {
				margin-bottom: 38rpx;
			}
		}
	}
</style>