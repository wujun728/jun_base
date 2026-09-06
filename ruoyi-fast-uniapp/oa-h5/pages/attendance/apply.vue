<template>
	<view>
		<!-- #ifdef APP-PLUS -->
		<uni-nav-bar :fixed="true" background-color="#fff" :border="false" status-bar left-icon="left" title="申请"
			@clickLeft="back" />
		<!-- #endif -->
		<view class="menu card">
			<view class="report-list">
				<view class="report-list-item" v-for="item in data.listData" :key="item.id">
					<view class="text-center" @click="handleExamineItem(item)">
						<view class="item-logo">
							<view class="iconfont" :class="getIconChange(item.icon)" :style="{color: item.color}">
							</view>
						</view>
						<view class="item-caption line1">{{item.name}}</view>
					</view>
				</view>
			</view>
		</view>
		<view class="card list" @click="goUrl()">
			<view>申请记录</view>
			<text class="iconfont icon-jinru-copy"></text>
		</view>
		<bottom-navigation :type="4" page-path="/pages/attendance/apply"></bottom-navigation>
	</view>
</template>

<script setup lang="ts">
	import bottomNavigation from "@/components/bottomNavigation/index.vue";
	import {
		ref,
		reactive,
		toRefs
	} from "vue";

	const data = reactive({
		listData: []
	})
	onMounted(() => {
		getConfigSearch(0)
	})
	const back = () => {
		uni.switchTab({
			url: '/pages/index/index'
		})
	}
	import { approveConfigSearchApi } from '@/api/business'
	const getConfigSearch = (id) => {
		approveConfigSearchApi(id).then((res) => {
			const datas = res.data ? res.data : []
			data.listData = datas
		}).catch((error) => {
			message.error(error.message)
		});
	}
	const getIconChange = (icon) => {
		if (icon.indexOf('-') > -1) {
			return icon
		} else {
			return `icon-${icon.slice(4)}`
		}
	}
	import { clickNavigateTo } from '@/utils/helper'
	const handleExamineItem = (item) => {
		clickNavigateTo(`/pages/users/examine/default?id=${item.id}&name=${item.name}`)
	}
	const goUrl = () => {
		clickNavigateTo('/pages/users/examine/center')
	}
</script>

<style lang="scss" scoped>
	.menu {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		flex-direction: column;

		.item {
			display: flex;
			justify-content: space-between;

			.item-list {
				display: flex;
				width: 100rpx;
				text-align: center;
				margin-bottom: 40rpx;
				flex-direction: column;

				image {
					width: 100rpx;
					height: 100rpx;
					margin-bottom: 20rpx;
				}

				.name {
					font-size: 28rpx;
					font-weight: 400;
					color: #606266;
					line-height: 28rpx;
				}
			}
		}

		.item-list:after {
			display: block;
			content: "";
			width: 100rpx;
			height: 0px;
		}
	}

	.menu.card {
		// padding: 44rpx 44rpx 0rpx;
	}

	.list {
		display: flex;
		justify-content: space-between;
		align-items: center;
		font-size: 28rpx;
		font-weight: 400;
		color: #303030;

		.icon-jinru-copy {
			color: #d8d8d8;
			font-size: 20rpx;
		}
	}

	.card {
		background-color: #fff;
		padding: 28rpx 24rpx;
		border-radius: 12rpx;
		margin: 20rpx 20rpx 0;
	}

	.report-list {
		padding-top: 36rpx;
		width: 100%;
		display: flex;
		flex-wrap: wrap;

		.report-list-item {
			width: 25%;
			margin-bottom: 50rpx;

			.item-logo {
				display: inline-block;
				width: 90rpx;
				height: 90rpx;

				.iconfont {
					font-size: 90rpx;
					color: #fff;
				}
			}

			.item-title {
				padding-top: 20rpx;
				font-size: 32rpx;
				color: $uni-text-color;
				font-weight: 600;
			}

			.item-caption {
				padding-top: 16rpx;
				font-size: 28rpx;
				color: $nui-text-color-two;
			}
		}
	}
</style>