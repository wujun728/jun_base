<template>
	<view class="frame">
		<view class="search-default-date" @click="clickFrame">
			{{data.frameText}}
			<text v-if="!data.currentYear" class="date-open-icon iconfont icon-zhankai1"></text>
			<!-- <text  class="date-open"></text> -->
			<text v-if="data.currentYear" class="iconfont date-clear  icon-shenpizhongxin-jujue" @click.stop="clickClear"></text>
		</view>
		<uni-popup ref="popupRef" type="bottom" :mask-click="true">
			<view class="frame-slider">
				<view class="title-slider">
					<view class="slider-header">
						<view class="slider-header-left" @click="upperYear">
							<view class="iconfont icon-fanhui"></view>
						</view>
						<view class="slider-header-text">{{data.yearText}}年</view>
						<view class="slider-header-right" @click="nextYear">
							<view class="iconfont icon-fanhui"></view>
						</view>
					</view>
				</view>
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
	import { ref, reactive, toRefs } from "vue"
	import message from "@/utils/message"
	import moment from 'moment'

	const props = defineProps( {
		type: {
			type: String,
			default: 'mine'
		}
	} )
	const { type } = toRefs( props )
	const data = reactive( {
		frameText: '本月',
		index: moment( new Date() ).format( 'M' ) - 1,
		listData: [
			{ text: '一月', id: 1 },
			{ text: '二月', id: 2 },
			{ text: '三月', id: 3 },
			{ text: '四月', id: 4 },
			{ text: '五月', id: 5 },
			{ text: '六月', id: 6 },
			{ text: '七月', id: 7 },
			{ text: '八月', id: 8 },
			{ text: '九月', id: 9 },
			{ text: '十月', id: 10 },
			{ text: '十一月', id: 11 },
			{ text: '十二月', id: 12 },
		],
		yearText: moment( new Date() ).format( 'YYYY' ),
		currentYear: false
	} )

	const formData = reactive( {
		time: ''
	} )

	const popupRef = ref( null )

	let emit = defineEmits( [ 'change' ] )

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
	const clickClear = () => {
		data.currentYear = false
		data.frameText = '本月'
		data.index = moment( new Date() ).format( 'M' ) - 1
		const cur = moment( new Date() ).format( 'YYYY-MM' )
		getMonthDate( cur )
	}
	// 上一年
	const upperYear = () => {
		if ( data.yearText <= 2020 ) return false
		data.yearText--
	}
	// 下一年
	const nextYear = ( index ) => {
		const year = moment( new Date() ).format( 'YYYY' )
		if ( data.yearText >= Number( year ) + 2 ) return false
		data.yearText++
	}

	import { getZeroNumber } from '@/utils/helper'
	const confirm = () => {
		const index = moment( new Date() ).format( 'M' ) - 1
		const cur = moment( new Date() ).format( 'YYYY-MM' )
		const year = `${data.yearText}-${getZeroNumber(data.listData[data.index].id)}`
		if ( year === cur && data.index === index ) {
			data.currentYear = false
			data.frameText = '本月'
		} else {
			data.currentYear = true
			data.frameText = year.replace( /-/g, '年' ) + '月'
		}
		getMonthDate( year )
		cancel()
	}

	const getMonthDate = ( time ) => {
		const start = moment( time, 'YYYY-MM' ).startOf( 'month' ).format( 'YYYY/MM/DD' )
		const end = moment( time, 'YYYY-MM' ).endOf( 'month' ).format( 'YYYY/MM/DD' )
		formData.time = `${start}-${end}`
		emit( 'change', formData )
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
			position: relative;

			.date-open-icon {
				margin-top: 4rpx;
				font-size: 24rpx;
				transform: scale(0.63);
				margin-left: 10rpx;
				color: #C0C4CC;
			}

			.date-clear {
				position: absolute;
				right: 0;
				z-index: 2;
				font-size: 28rpx;
				color: #C0C4CC;
			}
		}

		.frame-slider {
			width: 100%;
			height: 720rpx;
			background-color: #fff;
			border-radius: 20rpx 20rpx 0px 0px;

			.title-slider {
				width: 100%;
				padding-top: 20rpx;
				font-size: $uni-font-size-default;
				color: $nui-text-color-two;
				height: 100rpx;

				.slider-header {
					height: 100%;
					width: 100%;
					display: flex;
					align-items: center;
					justify-content: center;

					.slider-header-left,
					.slider-header-right {
						width: 100rpx;
						text-align: center;

						.iconfont {
							font-size: 32rpx;
							color: $nui-text-color-four;
						}
					}

					.slider-header-right {
						.iconfont {
							transform: rotate(-180deg);
						}
					}
				}
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
					border-radius: 43rpx;

					&::after {
						border: none;
						border-radius: 0;
					}
				}
			}
		}
	}
</style>
