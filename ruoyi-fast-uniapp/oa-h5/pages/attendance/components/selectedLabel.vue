<template>
	<view>
		<uni-popup ref="popupRef" type="bottom" :mask-click="false">
			<view class="slider">
				<view class="share-header">
					<view class="share-title">{{ title }}</view>
					<view @click="cancel" class="iconfont icon-shenpizhongxin-jujue"></view>
				</view>

				<view class="slider-laber">
					<scroll-view scroll-y="true" style="width: 100%; height: 100%">
						<view>
							<view class="slider-laber-item plr10">
								<button class="laber-item-button line1" :class="
                    data.selectLabelData.includes(item.id) ? 'active' : ''
                  " v-for="(item, index) in listData" :key="'list' + index" @click="selectItemLabel(item)">
									<text class="title line1">{{ item.name }}</text>
								</button>
							</view>
						</view>
					</scroll-view>
				</view>

				<view class="slider-laber-bottom">
					<button class="reset laber-bottom" @click="reset">重置</button>
					<button class="laber-bottom confirm" @click="confirm">确认</button>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		toRefs,
		onMounted
	} from "vue";
	import message from "@/utils/message";
	import {
		customerTabData,
		overTimeTabData
	} from "@/utils/attendance";
	const props = defineProps({
		typeData: {
			type: Object,
			default: () => {
				return [];
			},
		},
		title: {
			type: String,
			default: "",
		},
		type: {
			type: String,
			default: "",
		},
	});
	const {
		typeData,
		title,
		type
	} = toRefs(props);
	const emit = defineEmits(["changeItem"]);
	const popupRef = ref(null);
	const styleId = ref(1);
	// 全部、异常、迟到、严重迟到、早退、旷工、缺卡、异常外勤
	const data = reactive({
		listData: customerTabData,
		selectLabelData: [],
		selectLabelName: [],
	});

	const listData = computed(() => {
		if (type.value == 0 || type.value == 1) {
			return customerTabData;
		} else if (type.value == 3) {
			return overTimeTabData;
		}
	});

	// 打开弹出
	const popupOpen = (val, name) => {
		popupRef.value.open();
		if (val) {
			data.selectLabelData = val;
		}
		if (name) {
			data.selectLabelName = name;
		}
	};
	onMounted((e) => {
		// getList();
	});

	// 关闭
	const cancel = () => {
		data.selectLabelData = [];
		popupRef.value.close();
	};
	// 标签选择
	const selectItemLabel = (item) => {
		const index = data.selectLabelData.indexOf(item.id);
		if (index > -1) {
			data.selectLabelData.splice(index, 1);
			data.selectLabelName.splice(index, 1);
		} else {
			data.selectLabelData.push(item.id);
			data.selectLabelName.push(item.name);
		}
	};

	// 标签重置
	const reset = () => {
		if (data.selectLabelData.length > 0) {
			data.selectLabelName = [];
			emit("changeItem", [], data.selectLabelName);
		}
		emit("changeItem", [], data.selectLabelName);
		cancel();
	};

	// 标签确认
	const confirm = () => {
		if (data.selectLabelData.length <= 0) {
			message.error("至少选择一个" + title.value);
			return false;
		}
		emit("changeItem", data.selectLabelData, data.selectLabelName);
		cancel();
	};

	defineExpose({
		popupOpen
	});
</script>

<style lang="scss" scoped>
	::v-deep .uni-popup {
		z-index: 100;
	}

	.slider {
		position: relative;
		height: 772rpx;
		width: 100%;
		background-color: #fff;
		border-radius: 20rpx 20rpx 0 0;

		.share-header {
			width: 100%;
			height: 134rpx;
			position: relative;

			.image {
				width: 100%;
				height: 100%;
			}

			.share-title {
				position: absolute;
				top: 38rpx;
				left: 50%;
				transform: translate(-50%, 0);
				font-size: 30rpx;
				font-weight: 600;
				color: $uni-text-color;
			}

			.iconfont {
				font-size: 40rpx;
				color: #c0c4cc;
				position: absolute;
				top: 30rpx;
				right: 32rpx;
			}
		}

		.slider-laber {
			margin-top: -30rpx;

			height: calc(100% - 134rpx - 120rpx);

			.text {
				display: inline-block;
				font-size: 28rpx;
				margin-left: 40rpx;
				margin-top: 40rpx;
			}

			.slider-laber-item {
				width: 100%;
				display: flex;
				align-items: center;
				flex-wrap: wrap;

				.laber-item-button {
					position: relative;
					width: calc((100% - 60rpx) / 3);
					height: 70rpx;
					line-height: 70rpx;
					background-color: #f0f1f5;
					border-radius: 8rpx;
					font-size: 26rpx;
					padding: 0 15rpx;
					margin: 0;
					color: $uni-text-color;
					overflow: visible;
					margin-top: 20rpx;
					margin-right: 30rpx;
					display: flex;

					&:nth-of-type(3n) {
						margin-right: 0;
					}

					&::after {
						border-color: #f0f1f5;
						border-radius: 8rpx;
					}

					&.active {
						background: rgba(24, 144, 255, 0.1);
						color: $uni-color-primary;

						&::after {
							border-color: $uni-color-primary;
						}

						.icon {
							display: block;
						}
					}

					.title {
						display: block;
						width: 100%;
						height: 100%;
						// display: flex;
						// align-items: center;
						// justify-content: center;
					}

					.icon {
						display: none;
						position: absolute;
						right: 0;
						bottom: 0;
						border: 20rpx solid $uni-color-primary;
						border-left: 20rpx solid transparent;
						border-top: 20rpx solid transparent;

						.iconfont {
							position: absolute;
							left: 0;
							top: -6rpx;
							color: #fff;
							z-index: 2;
							font-size: 22rpx;
							line-height: 1;
						}
					}
				}
			}
		}

		.slider-laber-bottom {
			position: absolute;
			display: flex;
			bottom: 30rpx;
			height: 106rpx;
			left: 30rpx;
			right: 30rpx;

			//padding-bottom: 20rpx;
			align-items: center;
			background-color: #fff;

			.laber-bottom {
				height: 100%;
				border-radius: 8rpx;
				border: none;
				font-size: $uni-font-size-default;
				line-height: 106rpx;

				&::after {
					border: 0;
				}
			}

			.reset {
				width: 184rpx;
				margin-right: 30rpx;
				background: #f0f1f5;
				color: $uni-text-color;
			}

			.confirm {
				width: calc(100% - 214rpx);
				background: #308bf8;
				color: #fff;
			}
		}
	}
</style>