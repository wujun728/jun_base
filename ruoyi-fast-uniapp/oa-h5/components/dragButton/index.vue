<template>
	<view>
		<view id="_drag_button" class="cr-create-buttton" :style="'left: ' + data.left + 'px; top:' + data.top + 'px;'" @touchstart="touchstart" @touchmove.stop.prevent="touchmove"
			@touchend="touchend" @click.stop.prevent="click" :class="{transition: isDock && !data.isMove }">
			<slot></slot>
		</view>
	</view>
</template>

<script setup>
	import { ref, reactive, toRefs, computed, onMounted, getCurrentInstance } from 'vue'
	const props = defineProps( {
		isDock: {
			type: Boolean,
			default: false
		},
		existTabBar: {
			type: Boolean,
			default: false
		}
	} )

	const { isDock, existTabBar } = toRefs( props )
	const instance = getCurrentInstance()

	const data = reactive( {
		top: 0,
		left: 0,
		width: 0,
		height: 0,
		offsetWidth: 0,
		offsetHeight: 0,
		windowWidth: 0,
		windowHeight: 0,
		isMove: true,
		edge: 10
	} )

	onMounted( () => {
		const sys = uni.getSystemInfoSync();

		data.windowWidth = sys.windowWidth;
		data.windowHeight = sys.windowHeight;

		// #ifdef APP-PLUS
		existTabBar.value && ( data.windowHeight -= 50 );
		// #endif
		if ( sys.windowTop ) {
			data.windowHeight += sys.windowTop;
		}

		const query = uni.createSelectorQuery().in( instance );
		query.select( '#_drag_button' ).boundingClientRect( res => {
			data.width = res.width;
			data.height = res.height;
			data.offsetWidth = res.width / 2;
			data.offsetHeight = res.height / 2;
			data.left = data.windowWidth - data.width - data.edge;
			data.top = data.windowHeight - data.height - data.edge - 60;
		} ).exec();
	} )


	const emit = defineEmits( [ 'btnClick', 'btnTouchstart', 'btnTouchend' ] )


	const click = () => {
		emit( 'btnClick' )
	}
	const touchstart = ( e ) => {
		emit( 'btnTouchstart' )
	}
	const touchmove = ( e ) => {
		// 单指触摸
		if ( e.touches.length !== 1 ) {
			return false;
		}
		data.isMove = true;
		data.left = e.touches[ 0 ].clientX - data.offsetWidth;
		let clientY = e.touches[ 0 ].clientY - data.offsetHeight;
		// #ifdef H5
		clientY += data.height;
		// #endif
		let edgeBottom = data.windowHeight - data.height - data.edge;

		// 上下触及边界
		// if ( clientY < data.edge ) {
		// 	data.top = data.edge;
		// } else if ( clientY > edgeBottom ) {
		// 	data.top = edgeBottom;
		// } else {
		// 	data.top = clientY
		// }
	}
	const touchend = ( e ) => {
		if ( isDock.value ) {
			let edgeRigth = data.windowWidth - data.width - data.edge;

			if ( data.left < data.windowWidth / 2 - data.offsetWidth ) {
				data.left = data.edge;
			} else {
				data.left = edgeRigth;
			}

		}
		data.isMove = false;
		emit( 'btnTouchend' )
	}
</script>

<style lang="scss" scoped>
	// 全局添加文件按钮
	.cr-create-buttton {
		position: fixed;
		right: 20rpx;
		bottom: 100rpx;
		width: 100rpx;
		height: 100rpx;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		background: linear-gradient(135deg, #47B5FF 0%, #0F86F5 100%);
		box-shadow: 0px 4px 4px 0px rgba(28, 146, 248, 0.1);

		&.transition {
			transition: left .3s ease, top .3s ease;
		}

		::v-deep .iconfont {
			font-size: 54rpx;
			color: #fff;
		}
	}
</style>
