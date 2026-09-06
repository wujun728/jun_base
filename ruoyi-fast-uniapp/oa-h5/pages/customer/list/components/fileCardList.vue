<template>
	<view class="examine-content-list">
		<uni-list :border="false" v-if="listData.length>0">
			<uni-list-item v-for="(item,index) in listData" :class="type ===0 ? 'content-item-list' : 'content-item-list-card'" :key="'index'+index">
				<template v-slot:header>
					<view class="slot-header" @click="preview(item)">
						<image class="slot-image" :src="`/static/image/cloudfile/${isFileTypeIcon(item.name)}`" mode="widthFix">
						</image>
					</view>
				</template>
				<template v-slot:body>
					<view class="item-list-box">
						<uni-row class="item-list-box-top">
							<uni-col :span="20" class="list-box-top-left" @click="preview(item)">
								<view style="width: 100%;" class="line1-1">{{item.real_name}}</view>
								<view class="box-time line1-1">
									<text class="text flie-size line1">{{formatBytes(item.att_size)}}</text>
									<text class="text real-name line1">{{item.card.name}}</text>
									<uni-dateformat format="yyyy/MM/dd hh:mm" :date="item.created_at"></uni-dateformat>
								</view>
							</uni-col>
							<uni-col :span="4" class="list-box-top-right">
								<dean-popover :id="item.id" ref="deanPopoverRef" :index="index" model-direction="right">
									<template #icon>
										<text class="iconfont icon-yunwenjian-gengduo"></text>
									</template>
									<view class="modal-item" @click="onDownload(index, item, 1)"><text class="iconfont icon-yunwenjiandanchuang-xiazai"></text>下载</view>
									<view class="modal-item" @click="onDownload(index, item, 2)"><text class="iconfont icon-yunwenjiandanchuang-zhongmingming"></text>重命名</view>
									<view class="modal-item" @click="onDownload(index, item, 3)"><text class="iconfont icon-shanchu1"></text>删除</view>
								</dean-popover>
							</uni-col>
						</uni-row>
					</view>
				</template>
			</uni-list-item>
		</uni-list>
		<empty v-else :index="11" :title="emptyTitle" />
		<textarea-popup ref="textareaPopupRef" :config-data="data.configData" @change="changePop" />
	</view>
</template>

<script setup lang="ts">
	import empty from '@/components/empty/index.vue'
	import textareaPopup from '@/components/textareaPopup/index.vue'
	import deanPopover from '@/components/deanPopover/index.vue'
	import { ref, reactive, toRefs, watch,type Ref } from 'vue'
	import message from '@/utils/message'
	import { isFileTypeIcon, uploadDownload, isTypeImage, lookPreview } from '@/utils/helper'
	import { formatBytes } from '@/utils/file'
	import { clientFileDeleteApi, clientFileRealNameApi } from '@/api/customer'
	import type { Res, PropType, Detail } from '@/utils/typeHelper'

	const data = reactive({
		configData: {},
		imageList: []
	})

	const indexItem : Ref<number> = ref(-1)
	const deanPopoverRef = ref(null)
	const textareaPopupRef = ref(null)
	// 文件下载、编辑、删除
	const onDownload = (index : number, item : Detail, type : number) : void => {
		indexItem.value = index
		if (type === 1) {
			uploadDownload(item.att_dir, item.real_name)
		} else if (type === 2) {
			data.configData = {
				title: '资料重命名',
				placeholder: '请填写资料名称',
				type: item.id,
				text: item.real_name
			}
			textareaPopupRef.value.popupOpen()
		} else {
			uni.showModal({
				title: '提示',
				content: '您确定要删除该资料吗?',
				success: res => {
					if (res.confirm) {
						fileDelete(item.id)
					}
				}
			})
		}
		deanPopoverRef.value[index].close()
	}
	//重命名
	const changePop = (e : any) => {
		fileRealName(e.type, { real_name: e.value })
	}

	const fileDelete = (id : number) : void => {
		clientFileDeleteApi(id).then((res : Res) => {
			message.success(res.message)
			listData.value.splice(indexItem.value, 1)
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}
	// 重名命
	const fileRealName = (id : number, datas : PropType) : void => {
		clientFileRealNameApi(id, datas).then((res : Res) => {
			message.success(res.message)
			listData.value[indexItem.value].real_name = datas.real_name
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}

	// 图片与文档预览
	const preview = (item : Detail) : void => {
		lookPreview(item.att_dir, item.real_name, data.imageList)
	}
	const props = withDefaults(
		defineProps<{
			listData : Array<any>,
			type ?: number
			emptyTitle ?: string
		}>(), {
		emptyTitle: '暂无数据',
		type: 0,
		listData: <any>[]
	})
	const { listData, emptyTitle, type } = toRefs(props)

	// 监听数据变化
	watch(listData, (newvalue) => {
		if (newvalue.length > 0) {
			newvalue.forEach(value => {
				if (isTypeImage(value.name)) {
					data.imageList.push(value.att_dir)
				}
			})
		}
	}, {
		deep: true,
		immediate: true
	})
</script>

<style scoped lang="scss">
	.examine-content-list {
		::v-deep .uni-list {
			background-color: $uni-default-bg;

			.uni-list--border {
				left: auto;
			}

			.content-item-list {
				margin-bottom: 20rpx;
				border-radius: 12rpx;
				position: static;

				.uni-list-item__container {
					padding: 30rpx;
					overflow: visible;
				}
			}

			.content-item-list-card {
				border-bottom: 1px solid $uni-line-style-color-three;
				position: static;

				&:last-of-type {
					border-bottom: none;
				}

				.uni-list-item__container {
					padding: 36rpx 0;
					overflow: visible;
				}
			}
		}

		.item-list-box {
			width: calc(100% - 80rpx);
			padding-left: 20rpx;

			.item-list-box-top {
				font-size: 30rpx;
				font-weight: 600;
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
						display: flex;
						align-items: flex-end;

						.flie-size {
							max-width: 130rpx;
							display: inline-block;
						}

						.real-name {
							max-width: 100rpx;
							display: inline-block;
						}

						.text {
							padding-right: 16rpx;
						}
					}
				}

				.list-box-top-right {
					display: flex;
					align-items: center;
					justify-content: flex-end;

					.iconfont {
						color: $uni-text-color-five;
						font-weight: normal;
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