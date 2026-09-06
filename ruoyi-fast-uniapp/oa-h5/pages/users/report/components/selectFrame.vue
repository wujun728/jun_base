<template>
	<view class="frame">
		<view class="search-default-date" @click="clickFrame">
			{{data.frameText}}
			<text v-if="!formData.time" class="date-open-icon iconfont icon-zhankai1"></text>
			<!-- <text  class="date-open"></text> -->
		</view>
		<uni-popup ref="popupRef" type="bottom" :mask-click="true">
			<view class="frame-slider">
				<view class="title-slider">部门筛选</view>
				<view class="frame-list plr10">
					<button class="frame-list-item" v-for="(item,index) in data.listData" :class="index === data.index ? 'active' : ''" @click="selectItem(index)" :key="index">
						{{item.text}}
					</button>
				</view>
				<view class="frame-btn plr10">
					<button type="primary" @click="confirm">确认</button>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script setup>
	import { ref, reactive, onMounted, toRefs } from "vue"
	import message from "@/utils/message"

	const props = defineProps( {
		type: {
			type: String,
			default: 'mine'
		}
	} )
	const { type } = toRefs( props )
	const data = reactive( {
		frameText: '选择部门',
		index: 0,
		listData: [
			{ text: '本人及下属', id: 1 },
			{ text: '仅本人', id: 2 },
			{ text: '仅本部门', id: 3 },
			{ text: '选择部门', id: 4 },
			{ text: '选择成员', id: 5 },

		]
	} )

	const formData = reactive( {
		time: '',
		approveId: '',
		status: ''
	} )

	const popupRef = ref( null )

	let emit = defineEmits( [ 'change' ] )

	onMounted( () => {

	} )

	const clickFrame = () => {
		popupRef.value.open()
	}

	// 关闭
	const cancel = () => {
		popupRef.value.close()
	}

	const selectItem = ( index ) => {
		data.index = index
	}

	const confirm = () => {
		cancel()
	}
</script>

<style scoped lang="scss">
	.frame {
		width: 100%;
		display: flex;
		align-items: center;
		justify-content: center;

		.search-default-date {
			width: 100%;
			flex: 1;
			height: 72rpx;
			font-size: 24rpx;
			font-weight: 400;
			display: flex;
			justify-content: center;
			align-items: center;
			color: $nui-text-color-two;

			.iconfont {
				margin-top: 4rpx;
				font-size: 24rpx;
				transform: scale(0.63);
				margin-left: 10rpx;
				color: #C0C4CC;
			}
		}

		.frame-slider {
			width: 100%;
			height: 720rpx;
			background-color: #fff;
			border-radius: 20rpx 20rpx 0px 0px;

			.title-slider {
				padding-top: 44rpx;
				padding-bottom: 42rpx;
				font-size: $uni-font-size-default;
				font-weight: $uni-default-font-weight;
				color: $uni-text-color;
				text-align: center;
			}

			.frame-list {
				width: 100%;
				display: flex;
				align-items: center;
				flex-wrap: wrap;

				.frame-list-item {
					height: 72rpx;
					line-height: 72rpx;
					margin: 30rpx 30rpx 0 0;
					width: 210rpx;
					background: #F0F1F5;
					padding: 0;
					font-size: 28rpx;
					color: $uni-text-color;

					&:nth-of-type(3n) {
						margin-right: 0;
					}

					&::after {
						border: none;
						border-radius: 8rpx;
					}

					&.active {
						background-color: $uni-color-primary;
						color: #fff;
					}
				}
			}

			.frame-btn {
				width: 100%;
				position: fixed;
				bottom: 20rpx;
				left: 0;

				uni-button {
					height: 86rpx;
					line-height: 86rpx;
					font-size: $uni-font-size-default;
					border-radius: 12rpx;

					&::after {
						border: none;
						border-radius: 0;
					}
				}
			}
		}
	}
</style>
