<template>
	<div class="progress">
		<canvas ref="canvas" :width="width" :height="height"></canvas>
		<div class="data">
			<div class="item mb">
				<div class="point blue"></div>
				<div class="text">正常人数</div>
				<div class="num suc">{{ normal }}</div>
			</div>
			<div class="item">
				<div class="point red"></div>
				<div class="text">异常人数</div>
				<div class="num err">{{ abnormal }}</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { ref, watch, toRefs } from 'vue';

	const width = 300;
	const height = 130;
	const radius = 114;
	const strokeWidth = 15;
	const gradientStartColor = '#C1DDFD';
	const gradientEndColor = '#308BF8';

	const props = defineProps({
		normal: {
			type: Number,
			default: 0,
		},
		abnormal: {
			type: Number,
			default: 0,
		},
		total: {
			type: Number,
			default: 0,
		}
	});
	const {
		normal,
		abnormal,
		total
	} = toRefs(props);
	const progress = ref(0);
	const init = ref(0);
	const mycanvas = ref(null)
	onMounted(() => {
		initData();
		  nextTick(() => {
			mycanvas.value = document.querySelector("canvas");
		  });
	});

	watch(
		() => props.normal,
		(newValue, oldValue) => {
			if (newValue != oldValue) {
				init.value = 0;// 重置init的值为0
				initData();
			}
		}
	);

	watch(
		() => props.total,
		(newValue, oldValue) => {
			if (newValue != oldValue) {
				init.value = 0;
				initData();
			}
		}
	);

	const initData = () => {
		if (props.normal > 0 && props.total > 0) {
			progress.value = (props.normal / props.total).toFixed(2);
		} else {
			progress.value = 0
		}
		draw();
	};

	const draw = () => {
		init.value += 0.01;
		if (init.value <= progress.value) {
			drawProgress();
			setTimeout(() => {
				draw();
			}, 5);
		} else {
			drawProgress();
		}
	};

	const drawProgress = () => {
		const canvas = mycanvas.value
		if (!canvas) {
			return;
		}

		const ctx = canvas.getContext('2d');

		if (!ctx) {
			return;
		}

		ctx.clearRect(0, 0, width, height);

		// 绘制背景圆弧
		ctx.beginPath();
		const startAngle = Math.PI;
		const endAngle = 2 * Math.PI;
		ctx.arc(width / 2, height, radius, startAngle, endAngle);
		ctx.lineWidth = strokeWidth;
		ctx.strokeStyle = '#E5E9F2';
		ctx.stroke();

		// 绘制进度圆弧
		ctx.beginPath();
		const progressAngle = startAngle + init.value * Math.PI;
		ctx.arc(width / 2, height, radius, startAngle, progressAngle);
		const gradient = ctx.createLinearGradient(
			width / 2 - radius,
			height,
			width / 2 + radius,
			height
		);
		gradient.addColorStop(0, gradientStartColor);
		gradient.addColorStop(1, gradientEndColor);
		ctx.strokeStyle = gradient;
		ctx.stroke();
	};
</script>

<style scoped lang="scss">
	.progress {
		position: relative;
		    display: flex;
		    flex-direction: column;
		    align-items: center;

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