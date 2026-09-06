<template>
	<view class="canvas-content">
		<canvas :style="{width: width+ 'px', height: height + 'px'}" canvas-id="canvas"></canvas>
	</view>
</template>

<script setup>
	import { ref, reactive, toRefs, watch } from 'vue'
	const props = defineProps( {
		data: {
			type: Object,
			default: () => {
				return []
			}
		},
		width: {
			type: Number,
			default: 320
		},
		height: {
			type: Number,
			default: 225
		}
	} )
	const { data, width, height } = toRefs( props )

	const config = reactive( {
		newData: []
	} )
	import { fileLinkDownLoad } from '@/utils/helper'
	import message from '@/utils/message'
	const poster = () => {

		setTimeout( () => {
			let w = width.value
			let h = height.value
			let len = config.newData.length
			if ( len <= 0 ) {
				message.error( '海报生成内容不能为空' )
				return false
			}
			uni.showLoading( {
				title: '海报生成中'
			} )
			let canvas = uni.createCanvasContext( 'canvas' )
			canvas.clearRect( 0, 0, 0, 0 )
			canvas.fillStyle = "#fff";
			canvas.fillRect( 0, 0, w, h )

			for ( let i = 0; i < len; i++ ) {
				let value = config.newData[ i ]
				if ( value.type === 'image' ) {
					canvas.drawImage( value.path, value.x, value.y, value.w, value.height )
				} else if ( value.type === 'text' ) {
					canvas.setFillStyle( value.color )
					canvas.font = `${value.fw} ${value.fs}px sans-serif`
					canvas.setTextAlign( value.align ? value.align : 'left' )
					let x = value.x
					if ( value.align && value.align === 'center' ) {
						x = w / 2
					}
					canvas.fillText( value.path, x, value.y, value.mw )
				}
			}
			canvas.save()
			//开始绘画，必须调用这一步，才会把之前的一些操作实施
			canvas.draw( true, ( res ) => {
				uni.canvasToTempFilePath( {
					canvasId: "canvas",
					fileType: "png",
					destWidth: w,
					destHeight: h,
					quality: 1,
					success: ( res ) => {
						// #ifdef H5
						fileLinkDownLoad( res.tempFilePath, '邀请成员海报.png' )
						// #ifdef
						uni.hideLoading();
					},
					fail: () => {
						uni.showToast( {
							title: "名片加载失败",
							duration: 2000,
						} );
					}
				} )
			} )
		}, 500 )
	}
	// 数据监听
	watch( data, ( newvalue, oldvalue ) => {
		if ( newvalue ) {
			config.newData = newvalue
		}
	}, { immediate: true, deep: true } )


	defineExpose( { poster } )
</script>

<style lang="scss" scoped>
	.canvas-content {
		width: 100%;
		height: 100%;
		overflow: hidden;
		position: absolute;
		left: -9999;
		top: 0;
		z-index: -1000;
	}
</style>
