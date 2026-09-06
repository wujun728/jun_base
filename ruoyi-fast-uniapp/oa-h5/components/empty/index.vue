<template>
	<view class="empty">
		<view class="empty-content" v-if="isNetwork">
			<image class="empty-img" :src="'/static/image/empty'+getZeroNumber(index)+'.png'" mode=""></image>
			<text class="tips">{{title}}</text>
		</view>
		<view class="empty-content" v-else>
			<image class="no-network" src="@/static/image/users/no-network.png" mode=""></image>
			<text class="tips">网络开小差了～</text>
			<button class="network-btn" type="primary" plain="true" size="mini" @click="reload">刷新一下</button>
		</view>
	</view>
</template>

<script setup lang="ts">
	import { toRefs, ref,type Ref, reactive } from 'vue'
	import { getZeroNumber } from '@/utils/helper'
	import type { PropType } from '@/utils/typeHelper'
	const props = withDefaults(
		defineProps < {
			index: number,
			title ? : string
		} > (), {
			title: '暂无数据'
		}
	)
	const { index, title } = toRefs( props )
	//是否有网络
	const isNetwork: Ref < boolean > = ref( true )
	const toSwitchTab = reactive( [
		'/pages/forum/index', '/pages/notice/index', '/pages/workbench/index'
	] )
	uni.getNetworkType( {
		success: ( res ) => {
			isNetwork.value = res.networkType === 'none' ? false : true
		}
	} );
	const reload = (): void => {
		const pages = getCurrentPages()
		const curPage: PropType = pages[ pages.length - 1 ]
		const url: string = curPage.$page.fullPath
		const path: string = curPage.$page.path
		if ( toSwitchTab.includes( path ) ) {
			setTimeout( () => {
				uni.switchTab( {
					url: decodeURI( url )
				} );
			}, 500 )
		} else {
			setTimeout( () => {
				uni.redirectTo( {
					url: decodeURI( url )
				} );
			}, 500 )
		}

	}
</script>

<style scoped lang="scss">
	.empty {
		text-align: center;
		margin-top: 180rpx;

		.empty-content {
			padding-bottom: 180rpx;
			display: flex;
			flex-direction: column;
			align-items: center;

			.empty-img {
				width: 264rpx;
				height: 248rpx;
			}

			.tips {
				display: block;
				color: #999999;
				font-size: 26rpx;
				margin-top: 16rpx;
			}

			.no-network {
				width: 275rpx;
				height: 293rpx;
			}

			.network-btn {
				margin-top: 30rpx;
				width: 180rpx;
			}
		}
	}
</style>
