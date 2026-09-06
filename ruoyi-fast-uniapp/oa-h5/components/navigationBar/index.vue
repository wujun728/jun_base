<template>
	<view class="cr-navigation-bar pl10">
		<uni-row>
			<uni-col :span="20" class="navigation-bar-left" @click="showMoreClick">
				<view class="nav-tab-box">
					<view class="long-tab">
						<scroll-view scroll-x="true" style="white-space: nowrap; display: flex;" scroll-with-animation show-scrollbar="false">
							<view class="long-item" v-for="(item,index) in barData" :class="index === data.tabClick ? 'on' : ' '" :key="index" :id="'id'+index" @click="longClick(item,index)">
								{{propsData.name ? item[propsData.name] : item.cate_name}}
							</view>
							<view v-if="isActiveLine" class="underline-box" :style='"transform:translateX("+isLeft+"px);width:"+isWidth+"px"'></view>
						</scroll-view>
					</view>
				</view>
			</uni-col>
			<uni-col :span="4" class="navigation-bar-right text-center" @click="moreClick">
				<image class="forum-bar-img" src="/static/image/forum-bar-bg.png" mode=""></image>
				<view class="iconfont icon-zhankai1"></view>
			</uni-col>
		</uni-row>
	</view>
	<bar-more :check-per="barData" :active-index="data.tabClick" ref="barMoreRef" @handleCancel="handleCancel"></bar-more>
	<siderbar v-if="isSidebar" ref="siderbarRef" :isFirstlogin="isFirstlogin" @handleOk="handleOk"></siderbar>
</template>

<script setup>
	import { ref, reactive, toRefs, onMounted, nextTick, getCurrentInstance } from 'vue'
	import barMore from './components/barMore'
	import siderbar from './components/siderbar'
	const props = defineProps( {
		barData: {
			type: Object,
			default: () => {
				return []
			},
			required: true,
		},
		isSidebar: {
			type: Boolean,
			default: false
		},
		isActiveLine: {
			type: Boolean,
			default: true
		},
		isFirstlogin: {
			type: Boolean,
			default: false
		},
		// 默认字段判断
		propsData: {
			type: Object,
			default: () => {
				return {}
			}
		},
	} )
	const { barData, isSidebar, propsData, isActiveLine, isFirstlogin } = toRefs( props )
	const tabLeft = ref( 0 )
	const isLeft = ref( 0 )
	const isWidth = ref( 0 )
	const showLimit = ref( 4 )
	const showMore = ref( false )
	const data = reactive( {
		tabClick: 0
	} )

	onMounted( () => {
		getClientTop()
	} )

	const barMoreRef = ref( null )
	const siderbarRef = ref( null )

	const moreClick = () => {
		//头部下拉导航栏
		if ( !isSidebar.value ) {
			if ( showMore.value ) {
				barMoreRef.value.cancel()
				showMore.value = false
			} else {
				barMoreRef.value.popupOpen()
				showMore.value = true
			}
		} else {
			// 侧边栏
			siderbarRef.value.popupOpen()
		}

	}
	// 点击其他地方隐藏
	const showMoreClick = () => {
		barMoreRef.value.cancel()
		showMore.value = false
	}

	const handleCancel = ( e ) => {
		if ( e !== undefined ) {
			longClick( e.item, e.index )
		}
		showMore.value = false
	}

	const instance = getCurrentInstance() // 获取组件实例
	const entHeight = ref( 0 )
	// 获取元素宽高
	const getClientTop = async () => {
		await nextTick()
		let query = uni.createSelectorQuery().in( instance )
		query.select( '.long-tab' ).fields( { size: true } )
		query.exec( ( data ) => {
			isWidth.value = Math.floor( data[ 0 ].width / showLimit.value )
		} )
	}

	let emit = defineEmits( [ "handleData" ] )
	const longClick = ( item, index ) => {
		if ( barData.value.length > showLimit.value ) {
			tabLeft.value = ( index - 2 ) * isWidth.value
		}
		data.tabClick = index
		isLeft.value = index * isWidth.value
		emit( 'handleData', item )
	}

	const handleOk = () => {
		emit( 'handleData' )
	}

	defineExpose( { showMoreClick } )
</script>

<style scoped lang="scss">
	.cr-navigation-bar {
		height: 40px;
		background-color: #fff;
		width: 100%;

		::v-deep .uni-row {
			height: 100%;

			.uni-col {
				height: 100%;
			}
		}

		.navigation-bar-left {
			width: calc(100% - 74rpx);

			::-webkit-scrollbar {
				display: none;
			}

			.nav-tab-box {
				width: 100%;
				height: 100%;
			}

			.long-tab {
				width: 100%;
				height: 100%;
				position: relative;
				z-index: 100;

				.long-item {
					height: 40px;
					display: inline-block;
					line-height: 40px;
					text-align: center;
					font-size: 26rpx;
					padding-right: 40rpx;
					color: $nui-text-color-two;

					&.on {
						font-weight: $uni-default-font-weight;
						color: $uni-text-color;
					}
				}

				.underline-box {
					height: 2px;
					width: 20%;
					transition: .5s;
					position: absolute;
					bottom: 3px;
					background-color: $uni-color-primary;
				}
			}
		}

		.navigation-bar-right {
			width: 74rpx;
			position: relative;
			z-index: 100;
			line-height: 40px;

			.forum-bar-img {
				width: 74rpx;
				height: 40px;
			}

			.iconfont {
				display: inline-block;
				position: absolute;
				left: 50%;
				color: #D8D8D8;
				font-size: 24rpx;
				transform: scale(0.83) translate(-50%, 0);
			}

			// &::before {
			// 	content: '';
			// 	height: 40px;
			// 	width: 2px;
			// 	position: absolute;
			// 	left: 0;
			// 	bottom: 0;
			// 	background: linear-gradient(0, rgba(0, 0, 0, 0.08) 0%, rgba(255, 255, 255, 0) 100%);
			// }
		}
	}
</style>