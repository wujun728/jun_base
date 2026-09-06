<template>
	<view class="uni-steps" v-if="listData.length > 0">
		<view class="uni-steps__column">
			<view class="uni-steps__column-text-container">
				<view v-for="(item,index) in listData" :key="index" class="uni-steps__column-text">
					<view class="uni-steps__column-line-item">
						<view class="uni-steps__column-line" :class="index === 0 ? 'uni-steps__column-line-first--before' : 'uni-steps__column-line--before'"
							:style=" {backgroundColor:index<=active&&index!==0?activeColor:index===0?'transparent':deactiveColor}">
						</view>
						<view class="uni-steps__column-check" v-if="index === active">
							<text class="yuan"></text>
							<!-- <uni-icons :color="activeColor" :type="activeIcon" size="12"></uni-icons> -->
						</view>
						<view v-else class="uni-steps__column-circle" :style="{backgroundColor:index<active?activeColor:deactiveColor}">
						</view>
						<view class="uni-steps__column-line uni-steps__column-line--after"
							:style="{backgroundColor:index<active&&index!==listData.length-1?activeColor:index===listData.length-1?'transparent':deactiveColor}">
						</view>
					</view>

					<view class="uni-steps-left">
						<view class="time">
							{{item.created_at||item.time }}
						</view>
						<view class="content">
							<view class="header">
								<view class="flex">
									<image :src="item.user.avatar" mode="" class="img"></image>
									<text>{{item.user.name}}</text>
								</view>
								<dean-popover ref="deanPopoverRef" model-direction="right">
									<template #icon>
										<text class="iconfont icon-yunwenjian-gengduo"></text>
									</template>
									<view class="modal-item" @click="addRecord(1,item,index)"><text class="iconfont icon-gongzuohuibao-bianji"></text>编辑</view>
									<view class="modal-item" @click="addRecord(2,item,index)"><text class="iconfont icon-shanchu1"></text>删除
									</view>
								</dean-popover>
							</view>
							<view class="mark">
								{{item.content }}
								<view class="document" v-for="(img,index) in item.attachs" :key="index">
									<view class="document-left display-align" @click="lookOver(img)">
										<image class="img" v-if="isTypeImage(img.name)" :src="img.url" mode=""></image>
										<image class="img" v-else :src="'/static/image/cloudfile/'+isFileTypeIcon(img.name)" mode=""></image>
										<text class="imgText"> {{img.name }}</text>
									</view>
									<view class="document-right" @click="lookDownload(img)">
										<text class="default-color iconfont icon-xiazai"></text>
									</view>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
	<empty v-else :index="7" :title="emptyTitle"></empty>
</template>

<script setup lang="ts">
	import { ref, toRefs } from 'vue'
	import deanPopover from '@/components/deanPopover/index.vue'
	import empty from '@/components/empty/index.vue'
	import { clientContractResourceDeleteApi } from '@/api/customer'
	import message from '@/utils/message'
	import { showModal, isTypeImage, isFileTypeIcon, clickNavigateTo, uploadDownload, lookPreview } from '@/utils/helper'
	import type { Res, PropType } from '@/utils/typeHelper'

	const emit = defineEmits(['editFollow'])
	const deanPopoverRef = ref(null)
	const addRecord = (type : number, item : PropType, index : number) : void => {
		deanPopoverRef.value[index].close()
		if (type === 2) {
			showModal('您确定要删除合同资料吗').then(() => {
				resourceDelete(item.id, index)
			}).catch(() => { })
		} else {
			clickNavigateTo(`/pages/customer/contract/addFile?cid=${item.cid}&id=${item.id}`)
		}
	}

	//删除合同资料
	const resourceDelete = (id : number, index : number) => {
		clientContractResourceDeleteApi(id).then((res : Res) => {
			message.success(res.message)
			listData.value.splice(index, 1)
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}

	// 预览
	const lookOver = (item : any) => {
		lookPreview(item.url, item.real_name, [item.url])
	}
	// 下载
	const lookDownload = (val : any) : void => {
		uploadDownload(val.url, val.name)
	}
	const props = withDefaults(
		defineProps<{
			listData : Array<any>,
			emptyTitle ?: string
			direction ?: string
			activeColor ?: string
			deactiveColor ?: string
			activeIcon ?: string
			active ?: number
		}>(), {
		emptyTitle: '暂无数据',
		listData: <any>[],
		direction: 'row',
		activeColor: '#1890FF',
		deactiveColor: '#1890FF',
		activeIcon: '#circle',
		active: 0,
	})
	const { direction, listData, activeIcon, active, deactiveColor, activeColor, emptyTitle } = toRefs(props)
</script>

<style lang="scss" scoped>
	$uni-primary: rgba(48, 139, 248, 1) !default;
	$uni-border-color: #EDEDED;

	.time {
		font-size: 24rpx;
		font-family: PingFang SC-常规体, PingFang SC;
		font-weight: 400;
		color: #909399;
	}

	.yuan {
		display: inline-block;
		width: 12rpx;
		height: 12rpx;
		border-radius: 50%;
		border: 2px solid #1890FF;
	}

	.content {
		width: 100%;
		margin-top: 20rpx;
		background: #F2F6FC;
		padding: 24rpx;
		border-left: 2px solid #1890FF;
		border-radius: 8rpx;

		.header {
			display: flex;
			justify-content: space-between;
			color: #909399;
			font-size: 26rpx;
			font-family: PingFang SC-常规体, PingFang SC;

			.flex {
				display: flex;
				align-items: center;
			}

			.img {
				display: block;
				width: 40rpx;
				height: 40rpx;
				border-radius: 50%;
				margin-right: 10rpx;
			}
		}

		.mark {
			margin-top: 10rpx;
			margin-left: 50rpx;
			font-size: 28rpx;
			font-family: PingFang SC-常规体, PingFang SC;
			font-weight: 400;
			color: #333333;

			.remind {
				margin-top: 20rpx;
				width: 100%;
				height: 76rpx;
				border-radius: 8rpx;
				background-color: rgba(24, 144, 255, 0.1);
				font-size: 26rpx;
				font-family: PingFang SC-Regular, PingFang SC;
				font-weight: 400;
				color: #1890FF;
				padding-left: 18rpx;
				display: flex;
				align-items: center;

				.img {
					width: 34rpx;
					height: 38rpx;
					display: block;
					margin-right: 12rpx;
				}
			}

			.document {
				margin-top: 20rpx;
				width: 100%;
				height: 76rpx;
				border-radius: 8rpx;
				background: #FFFFFF;
				font-size: 26rpx;
				font-family: PingFang SC-常规体, PingFang SC;
				font-weight: 400;
				color: #333333;
				display: flex;
				align-items: center;
				padding-left: 16rpx;

				.document-left {
					width: calc(100% - 60rpx);

					.img {
						display: block;
						width: 40rpx;
						height: 40rpx;
						margin-right: 12rpx;
					}

					.imgText {
						display: inline-block;
						overflow: hidden;
						text-overflow: ellipsis;
						white-space: nowrap;
						width: calc(100% - 52rpx);
						font-size: 26rpx;
					}
				}

				.document-right {
					width: 60rpx;
					text-align: center;
				}
			}
		}
	}

	.uni-steps {
		/* #ifndef APP-NVUE */
		display: flex;
		width: 100%;
		/* #endif */
		/* #ifdef APP-NVUE */
		flex: 1;
		/* #endif */
		flex-direction: column;
		margin-top: 24rpx;
	}

	.uni-steps__row {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
	}

	.uni-steps__column {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row-reverse;
	}

	.uni-steps__row-text-container {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: flex-end;
		margin-bottom: 8px;
	}

	.uni-steps__column-text-container {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		flex: 1;
		width: 100%;
	}

	.uni-steps__row-text {
		/* #ifndef APP-NVUE */
		display: inline-flex;
		/* #endif */
		flex: 1;
		flex-direction: column;
	}

	.uni-steps__column-text {
		// border-bottom-style: solid;
		// border-bottom-width: 1px;
		border-bottom-color: $uni-border-color;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
	}

	.uni-steps__row-title {
		font-size: 14px;
		line-height: 16px;
		text-align: center;
	}

	.uni-steps__column-title {
		font-size: 14px;
		text-align: left;
		line-height: 18px;
	}

	.uni-steps__row-desc {
		font-size: 12px;
		line-height: 14px;
		text-align: center;
	}

	.uni-steps__column-desc {
		font-size: 12px;
		text-align: left;
		line-height: 18px;
	}

	.uni-steps__row-container {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
	}

	.uni-steps__column-container {
		/* #ifndef APP-NVUE */
		display: inline-flex;
		/* #endif */
		width: 30px;
		flex-direction: column;
	}

	.uni-steps__row-line-item {
		/* #ifndef APP-NVUE */
		display: inline-flex;
		/* #endif */
		flex-direction: row;
		flex: 1;
		height: 14px;
		line-height: 14px;
		align-items: center;
		justify-content: center;
	}

	.uni-steps__column-line-item {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		flex: 1;
		width: 30px;
		align-items: center;
		justify-content: center;
	}

	.uni-steps-left {
		width: calc(100% - 30px);
		padding: 6px 0px;
	}

	.uni-steps__row-line {
		flex: 1;
		height: 1px;
		background-color: #B7BDC6;
	}

	.uni-steps__column-line {
		width: 1px;
		background-color: rgba(48, 139, 248, 1);
	}

	.uni-steps__row-line--after {
		transform: translateX(1px);
	}

	.uni-steps__column-line--after {
		flex: 1;
		transform: translate(-1px, -1px);
	}

	.uni-steps__row-line--before {
		transform: translateX(-1px);
	}

	.uni-steps__column-line--before {
		height: 12px;
		transform: translate(-1px, -2px);
	}

	.uni-steps__column-line-first--before {
		height: 6px;
		transform: translate(-1px, -2px);
	}

	.uni-steps__row-circle {
		width: 6px;
		height: 6px;
		border-radius: 50%;
		background-color: #B7BDC6;
		margin: 0px 3px;
	}

	.uni-steps__column-circle {
		width: 8px;
		height: 8px;
		border-radius: 50%;
		background-color: #B7BDC6;
		// margin: 4px 0px 5px 0px;
	}

	.uni-steps__row-check {
		margin: 0px 6px;
	}

	.uni-steps__column-check {
		height: 12px;
		line-height: 14px;
		margin: 2px 0px;
	}
</style>