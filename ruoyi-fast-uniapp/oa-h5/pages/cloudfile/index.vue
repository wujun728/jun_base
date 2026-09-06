<template>
	<view>
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<view class="cr-new-header plr10">
				<uni-row class="display-align">
					<uni-col :span="12" class="header-left">
						<text class="title line1">云文件</text>
					</uni-col>
					<uni-col :span="12" class="text-right header-right">
						<text class="iconfont icon-sousuo"></text>
					</uni-col>
				</uni-row>
			</view>
			<view class="cloudfile-tab">
				<view class="plr10 cloudfile-tab-list">
					<view class="cloudfile-tab-list-item" :class="index === tabIndex ? 'active': ''" v-for="(item,index) in data.tabData" :key="index" @click="clickTab(index)">
						{{item.text}}
					</view>
				</view>
			</view>
		</view>
		<view class="content">
			<uni-list :border="false">
				<uni-list-item v-for="(item,index) in data.listData" :key="'index'+index">
					<template v-slot:header>
						<view class="slot-header">
							<image class="slot-image" src="/static/image/cloudfile/ent-space.png" mode="widthFix"></image>
						</view>
					</template>
					<template v-slot:body>
						<view class="item-list-box">
							<uni-row class="item-list-box-top">
								<uni-col :span="20" class="list-box-top-left">
									<view class="">企业空间</view>
									<view class="box-time">创建时间:2022/10/13 17:53</view>
								</uni-col>
								<uni-col :span="4" class="list-box-top-right">
									<text class="iconfont icon-qiyekongjian-shezhi"></text>
								</uni-col>
							</uni-row>
						</view>
					</template>
				</uni-list-item>
			</uni-list>
		</view>
		<!-- <empty :index="2" title="正在制作中,请稍等!!!~"></empty> -->
		<view class="uni-p-b-98"></view>
	</view>
</template>

<script setup>
	import empty from '@/components/empty/index'
	import { ref, reactive } from 'vue'
	const tabIndex = ref( 0 )
	const data = reactive( {
		tabData: [
			// { text: '最近常用', id: 1 },
			{ text: '企业空间', id: 2 }
		],
		listData: [ {}, {}, {}, {}, {}, {} ]
	} )
	const clickTab = ( index ) => {
		tabIndex.value = index
	}
</script>
<style>
	page {
		background-color: #fff;
	}
</style>
<style scoped lang="scss">
	.cr-new-header {
		.title {
			padding-left: 0;
		}
	}

	.cloudfile-tab {
		height: 88rpx;
		width: 100%;
		border-bottom: 1px solid #EBEEF5;
		background-color: #fff;

		.cloudfile-tab-list {
			height: 100%;
			display: flex;
			align-items: center;
			font-size: 28rpx;
			font-weight: 400;
			color: $nui-text-color-four;

			.cloudfile-tab-list-item {
				margin-right: 36rpx;
				height: 88rpx;
				line-height: 88rpx;

				&:last-of-type {
					margin-right: 0;
				}

				&.active {
					color: $uni-text-color;
					border-bottom: 2px solid $uni-color-primary;
				}
			}
		}
	}

	.content {
		padding-top: calc(44px + 88rpx + var(--status-bar-height));

		::v-deep .uni-list {
			.uni-list-item {
				position: static;
			}

			.uni-list--border {
				left: auto;
			}

			.uni-list-item__container {
				padding: 40rpx 30rpx 0 30rpx;
			}
		}

		.item-list-box {
			width: calc(100% - 80rpx);
			padding-left: 20rpx;

			.item-list-box-top {
				font-size: 30rpx;
				font-weight: 400;
				color: $uni-text-color;

				.uni-col {
					height: 80rpx;
				}

				.list-box-top-left {
					display: flex;
					justify-content: space-between;
					flex-wrap: wrap;
					flex-direction: column;

					.box-time {
						font-size: 24rpx;
						font-weight: 400;
						color: $nui-text-color-four;
					}
				}

				.list-box-top-right {
					display: flex;
					align-items: center;
					justify-content: flex-end;

					.iconfont {
						color: #C0C4CC;
						font-size: 30rpx;
					}
				}
			}
		}

		.slot-image {
			width: 80rpx;
			height: 80rpx;
		}
	}
</style>
