<template>
	<view v-if='isShow'>
		<view class="more-mask" @click="maskClick"></view>
		<view class="more-content">
			<view class="bar-more plr10">
				<uni-tag v-for="(item,index) in checkPer" :class="index === newIndex ? 'active': ''" :key="index" :inverted="true" @click="tabItemClick(item, index)"
					:text="item.cate_name" type="primary" />
			</view>
		</view>
	</view>
</template>

<script setup>
	import { ref, toRefs, watch } from 'vue'
	const props = defineProps( {
		checkPer: {
			type: Object,
			default: () => {
				return []
			}
		},
		activeIndex: {
			type: Number,
			default: 0
		}
	} )
	const isShow = ref( false )
	const { checkPer, activeIndex } = toRefs( props )
	const newIndex = ref( 0 )

	// 打开弹出
	const popupOpen = () => {
		isShow.value = true
	}

	const cancel = () => {
		isShow.value = false
	}

	const tabItemClick = ( item, index ) => {
		newIndex.value = index
		isShow.value = false
		emit( 'handleCancel', { index: index, item: item } )
	}

	let emit = defineEmits( [ "handleCancel" ] )

	// 点击遮罩层关闭
	const maskClick = () => {
		isShow.value = false
		emit( 'handleCancel' )
	}

	watch(
		() => activeIndex,
		( newvalue, oldalue ) => {
			newIndex.value = newvalue.value
		}, { deep: true, immediate: true },
	)

	defineExpose( { popupOpen, cancel } )
</script>

<style lang="scss" scoped>
	.more-mask {
		position: fixed;
		// #ifndef APP-PLUS
		top: calc(40px + var(--status-bar-height));
		// #endif
		// #ifdef APP-PLUS
		top: calc(84px + var(--status-bar-height));
		// #endif
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.4);
		z-index: 98;
	}

	.more-content {
		position: fixed;
		// #ifndef APP-PLUS
		top: calc(40px + var(--status-bar-height));
		// #endif
		// #ifdef APP-PLUS
		top: calc(84px + var(--status-bar-height));
		// #endif
		left: 0;
		right: 0;
		z-index: 99;
	}

	.bar-more {
		border-top: 1px solid #F0F1F5;
		padding-top: 40rpx;
		background-color: #fff;

		.uni-tag {
			display: inline-block;
			margin-right: 12rpx;
			margin-bottom: 40rpx;
			background: #F0F1F5;
			border-radius: 8rpx;
			font-size: 28rpx;
			border: 1px solid #F0F1F5;
			color: $uni-text-color;
			font-weight: 500;
			padding: 14rpx 18rpx;

			&.active {
				background: rgba(24, 144, 255, 0.1);
				border: 1px solid $uni-color-primary;
				color: $uni-color-primary;
			}
		}
	}
</style>
