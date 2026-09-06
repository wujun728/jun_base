<template>
	<view class="long-box" v-if="data.show" @click="data.show = false">
		<view class="long-box-content" :style="{top: position.bottom+'px'}">
			<view class="drop-down-list-item" v-for="(item,index) in meus" :key="'meus'+index" @click="dropDownItem(item)">
				<text class="iconfont" :class="item.icon"></text><text>{{item.name}}</text>
			</view>
		</view>
	</view>
</template>

<script setup>
	const props = defineProps( {
		position: {
			type: Object,
			default: () => {
				return {}
			}
		},
		meus: {
			type: Array,
			default: () => {
				return []
			}
		}
	} )
	const { position, meus } = toRefs( props )

	const emit = defineEmits( [ 'change' ] )
	const data = reactive( {
		show: false
	} )

	const openLong = () => {
		data.show = true
	}

	const dropDownItem = ( item ) => {
		emit( 'change', item )
	}

	// 数据监听
	watch( position, ( newvalue, oldvalue ) => {
		if ( Object.keys( newvalue ).length > 0 ) {

		}
	}, { immediate: true, deep: true } );

	defineExpose( { openLong } )
</script>

<style scoped lang="scss">
	.long-box {
		position: fixed;
		left: 0;
		top: 0;
		width: 100vw;
		height: 100vh;
		z-index: 999;

		.long-box-content {
			font-size: 28rpx;
			font-weight: 400;
			color: $nui-text-color-two;
			line-height: 28rpx;
			position: fixed;
			right: 20rpx;
			top: 0;
			box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.16);
			border-radius: 12rpx;
			padding: 32rpx 30rpx;
			background-color: #fff;

			.drop-down-list-item {
				margin-bottom: 44rpx;

				&:last-of-type {
					margin-bottom: 0;
				}

				.iconfont {
					font-size: 32rpx;
					color: $nui-text-color-four;
					margin-right: 14rpx;
				}
			}
		}
	}
</style>