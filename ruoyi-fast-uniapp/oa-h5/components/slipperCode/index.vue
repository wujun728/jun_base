<template>
	<view>
		<uni-popup ref="popupRef" type="dialog" :mask-click="false">
			<view class="slider rotate">
				<view class="close" @click="cancel" style="">
					<uni-icons type="close" size="30"></uni-icons>
				</view>
				<view class="content" :style="{height: (parseInt(setSize.imgHeight) + moveInfo.vSpace) + 'rpx'}">
					<view class="bg-img-div" style="width: 620rpx;">
						<img ref="bgImg" :src="'data:image/png;base64,'+imageUrl.backImgBase" alt />
					</view>
					<view class="rotate-img" :style="{left:moveInfo.moveBlockLeft}">
						<img ref="sliderImg" :src="'data:image/png;base64,'+imageUrl.blockBackImgBase" alt />
					</view>
				</view>
				<view class="verify-bar-area">
					<text class="verify-msg">向右滑动完成验证</text>
					<view class="verify-left-bar" @touchstart="sliderDown" @touchmove="move" @touchend="end" :style="{left:moveInfo.moveBlockLeft}">
						<i class="icon-tuodong iconfont" />
					</view>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		onMounted,
		reactive,
		getCurrentInstance,
		computed,
		nextTick
	} from 'vue'
	import message from '@/utils/message'
	import {
		sliderCodeApi,
		ajCaptchaCheck
	} from '@/api/public'
	const popupRef = ref( null )
	const verifyBlock = ref( null )
	const instance = getCurrentInstance() // 获取组件实例

	let captcha = reactive( {} )
	let sliderImgStyles = reactive( {
		transform: null,
	} )
	let moveStyles = reactive( {
		left: 0,
		transform: 'width .3s',
		backgroundPosition: null,
		moveBlockBackgroundColor: undefined,
		leftBarBorderColor: '#ddd',
		leftBarBorderColor: '#337AB7',
		iconColor: '#fff'
	} )
	let currentCaptchaConfig = reactive( {} )
	// 图片背景
	let imageUrl = reactive( {
		backImgBase: '',
		blockBackImgBase: '',
		backToken: '',
		secretKey: ''
	} )

	const barSize = reactive( {
		width: '100%',
		height: '80rpx'
	} )
	const setSize = reactive( {
		imgHeight: '310rpx',
		imgWidth: '310px',
		barHeight: '155rpx',
		barWidth: '35rpx'
	} )
	// 滑块滑动
	let moveInfo = reactive( {
		endMovetime: '',
		startMoveTime: '',
		startLeft: 0,
		// 移动中样式
		moveBlockLeft: 0,
		leftBarWidth: undefined,
		// 移动中样式
		moveBlockBackgroundColor: undefined,
		leftBarBorderColor: '#ddd',
		iconColor: undefined,
		iconClass: 'icon-right',
		status: false, // 鼠标状态
		isEnd: false, // 是够验证完成
		showRefresh: true,
		transitionLeft: '',
		transitionWidth: '',
		moveLeft: 0,
		vSpace: 5
	} )

	let tipWords = ref( 's' )

	// 打开弹出
	const popupOpen = () => {
		popupRef.value.open()
	}

	// 关闭验证码
	const cancel = () => {
		popupRef.value.close()
	}

	// 刷新验证码
	const refreshCaptcha = () => {

	}
	onMounted( () => {
		init()
	} )

	// watch(
	// 	() => type,
	// 	( newvalue, oldalue ) => {
	// 		const values = Object.assign( [], newvalue.value )
	// 		newCheckPer.value = values
	// 	}, { deep: true, immediate: true },
	// )

	defineExpose( {
		popupOpen
	} )



	const init = () => {
		getPictrue()
		// nextTick(() => {
		// 	const setSize = resetSize() // 重新设置宽度高度
		// 	for (const key in setSize) {
		// 		setSize = setSize[key]
		// 	}
		// })

		window.removeEventListener( 'touchmove', function ( e ) {
			move( e )
		} )
		window.removeEventListener( 'mousemove', function ( e ) {
			move( e )
		} )

		// 鼠标松开
		window.removeEventListener( 'touchend', function () {
			end()
		} )
		window.removeEventListener( 'mouseup', function () {
			end()
		} )

		window.addEventListener( 'touchmove', function ( e ) {
			move( e )
		} )
		window.addEventListener( 'mousemove', function ( e ) {
			move( e )
		} )

		// 鼠标松开
		window.addEventListener( 'touchend', function () {
			end()
		} )
		window.addEventListener( 'mouseup', function () {
			end()
		} )
	}
	const getPosition = () => {
		const query = uni.createSelectorQuery().in( instance )
		query.select( '.verify-left-bar' ).boundingClientRect( data => {
			moveInfo.moveleft = data.left
		} ).exec()
	}
	// 鼠标按下
	const sliderDown = ( e ) => {
		e = e || window.event
		if ( !e.touches ) { // 兼容PC端
			var x = e.clientX
		} else { // 兼容移动端
			var x = e.touches[ 0 ].pageX
		}
		// getPosition()
		moveInfo.startLeft = Math.floor( x - moveInfo.moveleft )
		moveInfo.startMoveTime = +new Date() // 开始滑动的时间
		if ( moveInfo.isEnd == false ) {
			moveInfo.text = ''
			moveStyles.moveBlockBackgroundColor = '#337ab7'
			moveStyles.leftBarBorderColor = '#337AB7'
			e.stopPropagation()
			moveInfo.status = true
		}
	}
	// 鼠标移动
	const move = ( e ) => {
		e = e || window.event
		if ( moveInfo.status && moveInfo.isEnd == false ) {
			if ( !e.touches ) { // 兼容PC端
				var x = e.clientX
			} else { // 兼容移动端
				var x = e.touches[ 0 ].pageX
			}
			getPosition()
			var move_block_left = x - moveInfo.moveLeft // 小方块相对于父元素的left值
			if ( move_block_left >= verifyBlock.offsetWidth - parseInt( parseInt( barSize.width ) / 2 ) -
				2 ) {
				move_block_left = verifyBlock.offsetWidth - parseInt( parseInt( barSize.width ) / 2 ) - 2
			}
			if ( move_block_left <= 0 ) {
				move_block_left = parseInt( parseInt( barSize.width ) / 2 )
			}
			// 拖动后小方块的left值
			if ( ( move_block_left - moveInfo.startLeft ) >= 0 && ( move_block_left - moveInfo.startLeft ) <= 310 ) {
				moveInfo.moveBlockLeft = ( move_block_left - moveInfo.startLeft ) * 2 + 'rpx'
				moveInfo.leftBarWidth = ( move_block_left - moveInfo.startLeft ) * 2 + 'rpx'
			}
		}
	}

	// 鼠标松开
	const end = () => {
		moveInfo.endMovetime = new Date()
		// 判断是否重合
		if ( moveInfo.status && !moveInfo.isEnd ) {
			var moveLeftDistance = parseInt( ( moveInfo.moveBlockLeft || '' ).replace( 'rpx', '' ) )
			moveLeftDistance = moveLeftDistance * 310 / parseInt( setSize.imgWidth )
			const data = {
				captchaType: 'blockPuzzle',
				'pointJson': imageUrl.secretKey ? aesEncrypt( JSON.stringify( {
					x: moveLeftDistance,
					y: 5.0
				} ), imageUrl.secretKey ) : JSON.stringify( {
					x: moveLeftDistance,
					y: 5.0
				} ),
				'token': imageUrl.backToken
			}
			ajCaptchaCheck( data ).then( res => {
				moveInfo.moveBlockBackgroundColor = '#5cb85c'
				moveInfo.leftBarBorderColor = '#5cb85c'
				moveInfo.iconColor = '#fff'
				moveInfo.iconClass = 'icon-check'
				moveInfo.showRefresh = false
				moveInfo.isEnd = true
				if ( moveInfo.mode == 'pop' ) {
					setTimeout( () => {
						message.success( res.data.message )
						refresh()
					}, 1500 )
				}
				moveInfo.tipWords =
					`${((moveInfo.endMovetime - moveInfo.startMoveTime) / 1000).toFixed(2)}s验证成功`
				var captchaVerification = imageUrl.secretKey ? aesEncrypt( imageUrl.backToken + '---' + JSON
					.stringify( {
						x: moveLeftDistance,
						y: 5.0
					} ), imageUrl.secretKey ) : imageUrl.backToken + '---' + JSON.stringify( {
					x: moveLeftDistance,
					y: 5.0
				} )
				setTimeout( () => {

				}, 1000 )
			} ).catch( res => {
				moveInfo.moveBlockBackgroundColor = '#d9534f'
				moveInfo.leftBarBorderColor = '#d9534f'
				moveInfo.iconColor = '#fff'
				moveInfo.passFlag = false
				setTimeout( function () {
					refresh()
				}, 1000 )
				message.error( res.message )
				moveInfo.moveBlockLeft = 0
				tipWords.value = '验证失败'
				setTimeout( () => {
					tipWords.value = ''
				}, 1000 )
			} )
			moveInfo.status = false
		}
	}

	const refresh = () => {
		moveInfo.showRefresh = true
		moveInfo.finishText = ''

		moveInfo.transitionLeft = 'left .3s'
		moveInfo.moveBlockLeft = 0

		moveInfo.leftBarWidth = undefined
		moveInfo.transitionWidth = 'width .3s'

		moveInfo.leftBarBorderColor = '#ddd'
		moveInfo.moveBlockBackgroundColor = '#fff'
		moveInfo.iconColor = '#000'
		moveInfo.iconClass = 'icon-right'
		moveInfo.isEnd = false

		getPictrue()
		setTimeout( () => {
			moveInfo.transitionWidth = ''
			moveInfo.transitionLeft = ''
			moveInfo.text = ''
		}, 300 )
	}

	// 请求背景图片和验证图片
	const getPictrue = () => {
		const data = {
			captchaType: 2,
			clientUid: localStorage.getItem( 'slider' ),
			ts: Date.now(), // 现在的时间戳
		}
		sliderCodeApi( data ).then( res => {
			imageUrl.backImgBase = res.data.originalImageBase64
			imageUrl.blockBackImgBase = res.data.jigsawImageBase64
			imageUrl.backToken = res.data.token
			imageUrl.secretKey = res.data.secretKey
		} ).catch( res => {
			tipWords.value = res.msg
			imageUrl.backImgBase = null
			imageUrl.blockBackImgBase = null
		} )
	}
	const resetSize = () => {
		var vm = getCurrentInstance()
		console.log( getCurrentInstance() )
		var img_width, img_height, bar_width, bar_height // 图片的宽度、高度，移动条的宽度、高度

		var parentWidth = vm.$el.parentNode.offsetWidth || window.offsetWidth
		var parentHeight = vm.$el.parentNode.offsetHeight || window.offsetHeight

		if ( vm.imgSize.width.indexOf( '%' ) != -1 ) {
			img_width = parseInt( this.imgSize.width ) / 100 * parentWidth + 'px'
		} else {
			img_width = this.imgSize.width
		}

		if ( vm.imgSize.height.indexOf( '%' ) != -1 ) {
			img_height = parseInt( this.imgSize.height ) / 100 * parentHeight + 'px'
		} else {
			img_height = this.imgSize.height
		}

		if ( vm.barSize.width.indexOf( '%' ) != -1 ) {
			bar_width = parseInt( this.barSize.width ) / 100 * parentWidth + 'px'
		} else {
			bar_width = this.barSize.width
		}

		if ( vm.barSize.height.indexOf( '%' ) != -1 ) {
			bar_height = parseInt( this.barSize.height ) / 100 * parentHeight + 'px'
		} else {
			bar_height = this.barSize.height
		}

		return {
			imgWidth: img_width,
			imgHeight: img_height,
			barWidth: bar_width,
			barHeight: bar_height
		}
	}
	import CryptoJS from 'crypto-js'
	/**
	 * @word 要加密的内容
	 * @keyWord String  服务器随机返回的关键字
	 *  */
	const aesEncrypt = ( word, keyWord = 'XwKsGlMcdPMEhR1B' ) => {
		var key = CryptoJS.enc.Utf8.parse( keyWord )
		var srcs = CryptoJS.enc.Utf8.parse( word )
		var encrypted = CryptoJS.AES.encrypt( srcs, key, {
			mode: CryptoJS.mode.ECB,
			padding: CryptoJS.pad.Pkcs7
		} )
		return encrypted.toString()
	}
</script>

<style lang="scss" scoped>
	.slider {
		width: 680rpx;
		z-index: 999;
		box-sizing: border-box;
		padding: 30rpx;
		border-radius: 6px;
		background-color: #fff;
		box-shadow: 0 0 11px 0 #999999;

		.close {
			position: absolute;
			width: 80rpx;
			height: 80rpx;
			top: 0;
			right: 0;
			display: flex;
			justify-content: center;
			align-items: center;
		}

		.content {
			width: 100%;
			height: 159px;
			margin-top: 30px;
			position: relative;

			.bg-img-div {
				width: 100%;
				height: 100%;
				position: absolute;
				transform: translate(0px, 0px);

				img {
					width: 100%;
				}
			}

			.rotate-img {
				position: absolute;
				left: 0;
				top: 0;
				z-index: 2;
			}
		}

		.verify-bar-area {
			margin: 40rpx 0;
			width: 100%;
			height: 70rpx;
			line-height: 70rpx;
			border-radius: 35rpx;
			color: #88949d;
			white-space: nowrap;
			text-align: center;
			font-size: 28rpx;
			background-color: #dfe1e3;

			.verify-left-bar {
				transform: translate(0px, 0px);
				position: relative;
				top: -95rpx;
				left: 0;
				background-color: #fff;
				border: 4rpx solid #d5d5d6;
				height: 120rpx;
				width: 120rpx;
				line-height: 120rpx;
				border-radius: 50%;

				.icon-tuodong {
					font-size: 50rpx;
					color: #0bd16a;
				}
			}
		}

		.slider-move {
			height: 60px;
			width: 100%;
			margin: 11px 0;
			position: relative;

			.slider-move-track {
				line-height: 38px;
				font-size: 14px;
				text-align: center;
				white-space: nowrap;
				color: #88949d;
				-moz-user-select: none;
				-webkit-user-select: none;
				user-select: none;
			}

			.slider-move-btn {
				transform: translate(0px, 0px);
				background-position: -5px 11.79625%;
				position: absolute;
				top: -12px;
				left: 0;
				width: 66px;
				height: 66px;
			}
		}

		.bottom {
			height: 19px;
			width: 100%;

			.refresh-btn {
				width: 20px;
				height: 20px;
				background-position: 0 81.38425%;
				margin-left: 10px;
			}
		}
	}
</style>
