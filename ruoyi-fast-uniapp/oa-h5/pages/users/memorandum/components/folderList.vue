<template>
	<view class="report-contents">
		<uni-list :border="false" v-if="listData.length">
			<uni-list-item class="list-item-01" v-for="(item, index) in listData" :key="index">s
				<!-- 自定义 body -->
				<template v-slot:body>
					<template v-if="item.folder_type">
						<view class="item-list-right">
							<uni-row class="display-align right-top">
								<uni-col :span="20" class="right-title" @click="itemListClick(item, 1)">
									<view class="right-title-content">
										<view :span="4" class="right-title-left">
											<image class="image" src="@/static/image/cloudfile/folder.png" mode=""></image>
										</view>
										<view :span="20" class="right-title-right">
											<view class="line1-1">{{item.name}}</view>
											<view class="right-time">
												创建于 <uni-dateformat format="yyyy/MM/dd hh:mm" :date="item.created_at"></uni-dateformat>
											</view>
										</view>
									</view>
								</uni-col>
								<uni-col :span="4" class="text-right icon-more">
									<dean-popover :id="item.id" ref="deanPopoverRef" :index="index" model-direction="right">
										<template #icon>
											<text class="iconfont icon-yunwenjian-gengduo"></text>
										</template>
										<view class="modal-item" @click="changePopover(item, index, 1)" v-if="item.path.length < 2"><text
												class="iconfont icon-jishiben-xinjianjishiben"></text>添加子文件</view>
										<view class="modal-item" @click="changePopover(item, index, 2)"><text class="iconfont icon-gongzuohuibao-bianji"></text>编辑</view>
										<view class="modal-item" @click="changePopover(item, index, 4)"><text class="iconfont icon-jishiben-yidongzhi"></text>移动至</view>
										<view class="modal-item" @click="changePopover(item, index, 3)"><text class="iconfont icon-shanchu1"></text>删除</view>
									</dean-popover>
								</uni-col>
							</uni-row>
						</view>
					</template>
					<template v-else>
						<view class="item-list-right" @click="itemListClick(item, 2)">
							<view class="caption-title line1-1">{{item.title}}</view>
							<view class="caption-info line2">{{item.content}}</view>
							<view class="right-time">
								创建于 <uni-dateformat format="yyyy/MM/dd hh:mm" :date="item.created_at"></uni-dateformat>
							</view>
						</view>
					</template>
				</template>

			</uni-list-item>
		</uni-list>
		<empty v-else :index="6" :title="emptyTitle"></empty>
		<news-folder ref="newsFolderRef" :title="config.title" :edit-data="config.editData" @handleOk="change"></news-folder>
		<ba-tree-picker ref="treePickerRef" :multiple="false" @select-change="selectChange" title="移动至" :localdata="config.treeData" valueKey="value" textKey="label"
			childrenKey="children" :border="true" />
	</view>
</template>

<script setup>
	import { ref, reactive, toRefs, onMounted } from "vue"
	import empty from '@/components/empty/index'
	import deanPopover from '@/components/deanPopover/index.vue'
	import baTreePicker from '@/components/baTreePicker/index.vue'
	import newsFolder from './newsFolder.vue'
	import message from '@/utils/message'
	const props = defineProps( {
		listData: {
			type: Array,
			default () {
				return []
			}
		},
		emptyTitle: {
			type: String,
			default: ''
		},
		parentId: {
			type: [ String, Number ],
			default: ''
		},
	} )
	const { listData, emptyTitle, parentId } = toRefs( props )
	const config = reactive( {
		id: null,
		selectIndex: -1,
		editData: {
			placeholder: '请输入文件夹名称',
			type: 0
		},
		treeData: [],
		title: '新建文件夹'
	} )

	const newsFolderRef = ref( null )
	import { clickNavigateTo, showModal } from '@/utils/helper'
	import { userMemorialCateDeleteApi, userMemorialCateEditApi, userMemorialCateMoveApi, userMemorialCateMovebleApi } from '@/api/user'

	const deanPopoverRef = ref( null )
	const treePickerRef = ref( null )
	const changePopover = ( item, index, type ) => {
		deanPopoverRef.value[ index ].close()
		config.id = item.id
		config.selectIndex = index
		if ( type === 1 ) {
			config.title = '新建文件夹'
			config.editData.type = 0
			newsFolderRef.value.popupOpen()
		} else if ( type === 2 ) {
			config.title = '编辑文件夹'
			config.editData.type = 1
			config.editData.time = new Date().getTime()
			config.editData.title = item.name
			newsFolderRef.value.popupOpen()
		} else if ( type === 3 ) {
			showModal( '确定要删除该分类吗' ).then( () => {
				userMemorialCateDeleteApi( config.id ).then( res => {
					message.success( res.message )
					listData.value.splice( index, 1 )
				} ).catch( ( error ) => {
					message.error( error.message )
				} )
			} ).catch( () => {
				console.log( '取消' )
			} )
		} else {
			getCateTree( item.id )
		}
	}

	const emit = defineEmits( [ 'btnClick' ] )

	// 添加子文件夹
	const change = ( e ) => {
		if ( e.type === 0 ) { // 添加
			emit( 'btnClick', { type: 2, id: config.id, name: e.value } )
		} else { // 编辑
			memorialCateEdit( config.id, { name: e.value } )
		}
	}

	// 编辑文件夹
	const memorialCateEdit = ( id, data ) => {
		userMemorialCateEditApi( id, data ).then( res => {
			message.success( res.message )
			listData.value[ config.selectIndex ].name = data.name
		} ).catch( ( error ) => {
			message.error( error.message )
		} )
	}
	const itemListClick = ( item, type ) => {
		if ( type === 1 ) {
			emit( 'btnClick', { type: 1, id: item.id } )
		} else {
			clickNavigateTo( '/pages/users/memorandum/details?tab=folder&id=' + item.id )
		}
	}

	// 移动文件夹至
	const selectChange = ( e ) => {
		getMemorialCateMove( config.id, { pid: e[ 0 ] } )
	}

	// 获取记事本分类详情
	const getCateTree = ( id ) => {
		userMemorialCateMovebleApi( id ).then( res => {
			config.treeData = res.data || []
			if ( config.treeData.length > 0 ) {
				treePickerRef.value.show()
			} else {
				message.error( '文件夹层级不能超过两级' )
			}
		} ).catch( ( error ) => {
			message.error( error.message )
		} )
	}

	// 记事文件夹移动
	const getMemorialCateMove = ( id, data ) => {
		userMemorialCateMoveApi( id, data ).then( res => {
			message.error( res.message )
			clickNavigateTo( '/pages/users/memorandum/index?tab=folder&id=' + parentId.value )
		} ).catch( ( error ) => {
			message.error( error.message )
		} )
	}
</script>

<style scoped lang="scss">
	.report-contents {
		padding-top: 20rpx;

		::v-deep .uni-list {
			background-color: $uni-default-bg;
		}

		.list-item-01 {
			margin-bottom: 20rpx;
			border-radius: 12rpx;

			::v-deep .uni-list-item__container {
				display: block;
				padding: 30rpx 24rpx;
				border-radius: 12rpx;
				overflow: visible;
			}

			::v-deep .uni-list--border {
				left: auto;
				top: auto;
			}
		}

		.item-list-right {
			width: 100%;

			.right-top {
				width: 100%;

				.right-title {
					width: 100%;
					font-size: $uni-font-size-default;
					color: $uni-text-color;

					.right-title-content {
						width: 100%;
						display: flex;
						align-items: center;

						.right-title-left {
							width: 80rpx;
							height: 80rpx;

							.image {
								width: 100%;
								height: 100%;
							}
						}

						.right-title-right {
							width: calc(100% - 80rpx);
							padding-left: 20rpx;
							display: flex;
							justify-content: space-between;
							flex-direction: column;
							height: 80rpx;
						}
					}
				}

				.icon-more {
					.iconfont {
						color: $uni-text-color-five;
					}
				}
			}

			.right-time {
				font-size: 24rpx;
				color: $uni-text-color-five;
			}

			.caption-title {
				font-size: $uni-font-size-default;
				color: $uni-text-color;
				margin-bottom: 16rpx;
			}

			.caption-info {
				font-size: 26rpx;
				color: $nui-text-color-two;
				line-height: 1.5;
				margin-bottom: 16rpx;
			}
		}
	}
</style>
