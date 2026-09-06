<template>
	<uni-popup ref="popupRef" type="right" :mask-click="false">
		<view class="slider">
			<view class="status_bar"></view>
			<default-nav-bar :is-sider="true" :default-title="data.defaultTitle" @goBackChange="goBackChange"></default-nav-bar>
			<view class="slider-tips plr10" v-if="configData.type !== 0"><text class="iconfont icon-yemiantishi"></text>
				{{configData.type === 2 ? '保存后，收支记账与对应合同付款记录也会同步修改！' : '审核通过后，系统自动生成收支记账流水信息！'}}
			</view>
			<view class="content">
				<scroll-view style="height: 100%;" scroll-y="true">
					<view class="m10">
						<uni-forms :border="false" label-width="80px">
							<view class="form-item-list" v-if="configData.type !== 0">
								<!-- 账目分类 -->
								<uni-forms-item class="input-label" v-if="configData.type !== 2">
									<template v-slot:label>
										<view class="uni-forms-item__label">账目分类 <text class="iconfont">*</text></view>
									</template>
									<view class="picker-tree-data">
										<uni-data-picker class="tree-data" placeholder="请选择账目分类" popup-title="账目分类" :localdata="data.treeData" :map="{text: 'name' , value: 'id' }" v-model="data.cate">
										</uni-data-picker>
										<view class="iconfont icon-fanhui"></view>
									</view>
								</uni-forms-item>
								<uni-forms-item class="input-label">
									<template v-slot:label>
										<view class="uni-forms-item__label">{{getPayRecordTypes(configData.row.types)}}金额(元) <text class="iconfont">*</text></view>
									</template>
									<uni-easyinput v-model="formData.num" :inputBorder="false" type="number" :clearable="false" maxlength="11" :autoHeight="true" placeholder="请填写">
									</uni-easyinput>
								</uni-forms-item>
								<uni-forms-item class="input-label" v-if="configData.row.types === 1">
									<template v-slot:label>
										<view class="uni-forms-item__label">续费类型 <text class="iconfont">*</text></view>
									</template>
									<picker mode="selector" :value="data.cateIndex" :range="data.rangeCate" range-key="title" @change="cateChange">
										<view v-if="formData.cate_id === 0" class="picker-input picker-input-placeholder">
											请选择
											<view class="iconfont icon-fanhui"></view>
										</view>
										<view class="picker-input" v-else>
											{{data.rangeCate[data.cateIndex].title}}
											<view class="iconfont icon-fanhui"></view>
										</view>
									</picker>
								</uni-forms-item>
								<uni-forms-item class="input-label" v-if="configData.row.types === 1">
									<template v-slot:label>
										<view class="uni-forms-item__label">续费结束日期 </view>
									</template>
									<uni-datetime-picker type="date" :clear-icon="false" :border="false" v-model="formData.end_date">
										<view v-if="!formData.end_date" class="picker-input picker-input-placeholder">
											请选择
											<view class="iconfont icon-fanhui"></view>
											<view class="picker-input" v-if="formData.end_date">
												{{formData.end_date}}
											</view>
										</view>
									</uni-datetime-picker>
								</uni-forms-item>
								<!-- 支付方式 -->
								<uni-forms-item class="input-label">
									<template v-slot:label>
										<view class="uni-forms-item__label">{{configData.type < 2 ? '支付' : '支出'}}方式<text class="iconfont">*</text></view>
									</template>
									<picker mode="selector" :value="data.payIndex" :range="data.rangePayment" range-key="name" @change="payChange">
										<view v-if="formData.type_id===0" class="picker-input picker-input-placeholder">
											请选择
											<view class="iconfont icon-fanhui"></view>
										</view>
										<view class="picker-input" v-else>
											{{data.rangePayment[data.payIndex].name}}
											<view class="iconfont icon-fanhui"></view>
										</view>
									</picker>
								</uni-forms-item>
								<!-- 付款时间 -->
								<uni-forms-item class="input-label">
									<template v-slot:label>
										<view class="uni-forms-item__label">{{configData.type < 2 ? '付款' : '支出'}}时间<text class="iconfont">*</text></view>
									</template>
									<uni-datetime-picker type="datetime" :clear-icon="false" :border="false" v-model="formData.date">
										<view v-if="!formData.date" class="picker-input picker-input-placeholder">
											请选择
											<view class="iconfont icon-fanhui"></view>
											<view class="picker-input" v-if="formData.date">
												{{formData.date}}
											</view>
										</view>
									</uni-datetime-picker>
								</uni-forms-item>
								<!-- 付款凭证 -->
								<uni-forms-item class="input-label is-direction-top">
									<template v-slot:label>
										<view class="uni-forms-item__label item-label-left">{{configData.type < 2 ? '付款' : '支出'}}凭证</view>
										<view class="upload">
											<view class="box" v-if="data.imgs!==''">
												<image class="img" :src="data.imgs" mode=""></image>
												<view class="delete" @click="deleteImg">
													<text class="iconfont icon-shenpizhongxin-jujue"></text>
												</view>
											</view>

											<view v-else class="upload-box" @click="uploadAvatar">
												<view class="iconfont icon-paizhao"></view>
												<view class="text">
													上传凭证
												</view>
											</view>
										</view>
										<text class="tips">建议734*1034，大小不超过{{fileSizeOne}}M，支持jpg、jpeg、png等</text>
									</template>
								</uni-forms-item>
							</view>

							<view class="form-item-list">
								<uni-forms-item class="input-label is-direction-top">
									<template v-slot:label>
										<view class="uni-forms-item__label item-label-left">{{configData.type !== 0 ? '备注' : '拒绝理由'}}</view>
									</template>
									<uni-easyinput :inputBorder="false" v-model="formData.mark" type="textarea" :clearable="false" :maxlength="256" :autoHeight="true"
										:placeholder="configData.type !== 0 ? '请填写备注' : '请填写拒绝理由'">
									</uni-easyinput>
								</uni-forms-item>
							</view>
						</uni-forms>
					</view>
				</scroll-view>
			</view>
			<!-- 底部 -->
			<view class="examine-button">
				<button type="primary" class="button" :loading="loading" @click="handleConfirm">{{configData.type === 2 ? '保存' : '提交'}}</button>
			</view>
		</view>
	</uni-popup>
</template>

<script setup lang="ts">
	import defaultNavBar from '@/components/defaultNavBar/index.vue'
	import { uploadImage } from '@/utils/file'
	import message from '@/utils/message'
	import type { Res, Detail, PropType } from '@/utils/typeHelper'
	import { getFindIndex, fileSizeOne } from '@/utils/helper'
	import { financeBillStatuslApi, financeBillCateApi, financeBillEditApi } from '@/api/finance'
	import { payTypeApi, clientSourceApi } from '@/api/customer'
	import { getPayRecordTypes } from '@/utils/assessment'

	interface C {
		row : Detail
		type : number,
		path ?: Array<number>
	}
	const props = defineProps<{
		configData : C,
	}>()
	const { configData } = toRefs(props)

	const data = reactive({
		defaultTitle: '审核通过',
		rangePayment: [],
		payIndex: 0,
		imgs: '',
		uploadImage: [],
		treeData: [],
		cateIndex: 0,
		rangeCate: [],
		cate: <string | number>''
	})

	const formData = reactive({
		bill_cate_id: <string | number>'',
		num: '',
		type_id: 0,
		attach_ids: [],
		date: '',
		cate_id: 0,
		mark: '',
		end_date: '',
		types: 0,
		status: 1,
		cid: 0,
		eid: 0
	})

	// emit导出{
	let emit = defineEmits(['change'])
	const popupRef = ref(null)
	const popupOpen = () => {
		popupRef.value.open()
	}

	const goBackChange = () : void => {
		cancel()
	}

	onMounted(() => {
		getPayType()
		getSourceData()
	})

	// 获取支付方式接口
	const getPayType = () => {
		payTypeApi().then((res : Res) : void => {
			data.rangePayment = res.data.list
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}
	const type : Ref<number> = ref(0)
	// 账目分类
	const getBillCate = () : void => {
		financeBillCateApi({ types: type.value }).then((res : Res) : void => {
			data.treeData = res.data
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}

	// 获取续费类型接口
	const getSourceData = () => {
		let dataInfo = { keys: ['renew'] }
		clientSourceApi(dataInfo).then((res : Res) => {
			data.rangeCate = res.data.renew
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}

	// 关闭验证码
	const cancel = () => {
		popupRef.value.close()
		emit('change', false)
	}

	const payChange = (e : PropType) : void => {
		const len = e.detail.value
		data.payIndex = len
		formData.type_id = data.rangePayment[len].id
	}


	const cateChange = (e : PropType) : void => {
		let len = e.detail.value
		data.cateIndex = len
		formData.cate_id = data.rangeCate[len].id
	}

	const loading : Ref<boolean> = ref(false)
	const handleConfirm = () : boolean => {
		if (configData.value.type === 0) {
			if (!formData.mark) {
				message.error('请填写拒绝原因')
				return false
			}
			if (!loading.value) {
				getBilllStatus(configData.value.row.id, { fail_msg: formData.mark, status: 2 })
			}
		} else {
			if (!data.cate) {
				message.error('请选择账目分类')
				return false
			}
			if (!formData.num) {
				message.error(formData.types === 1 ? '请填写续费金额' : '请填写回款金额')
				return false
			}
			if (!formData.type_id) {
				message.error('请选择支付方式')
				return false
			}
			if (!formData.date) {
				message.error('请选择付款时间')
				return false
			}

			if (formData.types === 1 && !formData.cate_id) {
				message.error('请选择续费类型')
				return false
			}
			formData.bill_cate_id = data.cate
			if (data.uploadImage.length > 0) {
				data.uploadImage.map((value : Detail) => {
					formData.attach_ids.push(value.id)
				})
			}
			if (!loading.value) {
				if (configData.value.type === 2) {
					// 编辑
					getBillEdit(configData.value.row.id, formData)
				} else {
					getBilllStatus(configData.value.row.id, formData)
				}
			}
		}
	}

	// 撤销审核
	const getBilllStatus = (id : number, datas : object) : void => {
		loading.value = true
		financeBillStatuslApi(id, datas).then((res : Res) : void => {
			message.error(res.message)
			loading.value = false
			cancel()
		}).catch((error : Res) : void => {
			message.error(error.message)
			loading.value = false
		})
	}
	// 付款记录编辑
	const getBillEdit = (id : number, datas : object) : void => {
		loading.value = true
		financeBillEditApi(id, datas).then((res : Res) : void => {
			message.error(res.message)
			loading.value = false
			cancel()
		}).catch((error : Res) : void => {
			message.error(error.message)
			loading.value = false
		})
	}

	// 上传付款凭证
	const uploadAvatar = () => {
		uploadImage('attach/imgs', { relation_type: 'bill' }, fileSizeOne).then(res => {
			data.imgs = res.data.src
			res.data.id = res.data.attach_id
			data.uploadImage.push(res.data)
		}).catch(error => {
			message.error(error)
		})
	}

	const deleteImg = () : void => {
		data.imgs = ''
		formData.attach_ids = []
		data.uploadImage = []
	}

	watch(configData, async (newvalue) => {
		type.value = newvalue.row.types < 2 ? 1 : 0
		await getBillCate()
		if (newvalue.type === 0) {
			data.defaultTitle = '审核拒绝'
			formData.mark = ''
		} else if (newvalue.type === 1 || newvalue.type === 2) {
			data.defaultTitle = newvalue.type === 1 ? '审核通过' : '编辑付款记录'
			formData.date = newvalue.row.date
			formData.num = newvalue.row.num
			formData.cid = newvalue.row.cid
			formData.eid = newvalue.row.eid
			formData.types = newvalue.row.types
			formData.type_id = newvalue.row.type_id
			data.uploadImage = newvalue.row.attachs
			data.payIndex = getFindIndex(data.rangePayment, formData.type_id)
			if (newvalue.row.types === 1) {
				formData.cate_id = newvalue.row.renew.id
				data.cateIndex = getFindIndex(data.rangeCate, formData.cate_id)
				if (newvalue.row.end_date !== '0000-00-00') {
					formData.end_date = newvalue.row.end_date
				} else {
					formData.end_date = ''
				}
			}
			if (newvalue.row.types < 2) {
				if (newvalue.path.length > 0) {
					data.cate = newvalue.path[newvalue.path.length - 1]
				}
			} else {
				data.cate = newvalue.row.bill_cate_id
			}
			formData.mark = newvalue.row.mark
			data.imgs = newvalue.row.attachs.length > 0 ? newvalue.row.attachs[0].src : ''
		}
	})

	defineExpose({ popupOpen })
</script>

<style lang="scss" scoped>
	::v-deep .uni-popup {
		z-index: 999;
	}

	::v-deep .uni-popup__wrapper.left {
		padding-top: 0 !important;
	}

	.slider {
		height: 100vh;
		width: 100vw;
		background-color: $uni-default-bg;

		.slider-tips {
			background-color: #F2F6FC;
			width: 100%;
			height: 68rpx;
			line-height: 68rpx;
			color: $uni-color-primary;
			font-size: 24rpx;

			.iconfont {
				font-size: 24rpx;
			}
		}

		.content {
			background-color: $uni-default-bg;
			height: calc(100vh - 126rpx - 68rpx - 44px - var(--status-bar-height));

			.form-item-list {
				background-color: #fff;
				border-radius: 12rpx;
				margin-bottom: 20rpx;

				&:last-of-type {
					margin-bottom: 0;
				}


				.input-label {
					padding: 18rpx 24rpx 18rpx 0;
					margin-left: 24rpx;
					align-items: center;
					margin-bottom: 0;
					border-bottom: 1px solid #EBEEF5;

					&:last-of-type {
						border-bottom: none;
					}

					::v-deep .uni-easyinput__content-input {
						text-align: right;
						padding-right: 0 !important;
					}

					::v-deep .uni-easyinput__placeholder-class {
						color: $uni-text-color-five;
						font-size: $uni-font-size-default;
					}

					::v-deep .uni-icons {
						display: none;
					}

					::v-deep .uni-forms-item__content {
						width: 100%;
					}

					::v-deep .uni-input-input {
						font-size: $uni-font-size-default;
						color: $uni-text-color;
						text-align: right;
					}

					::v-deep .uni-forms-item__label {
						max-width: 198rpx;
						display: flex;
						line-height: 1.2;

						.iconfont {
							width: 16rpx;
						}
					}

					.uni-date-x,
					.uni-date__x-input {
						padding: 0;
					}

					.uni-forms-item__label {
						height: auto;
						padding: 0;
						font-size: $uni-font-size-default;
						color: $uni-text-color;
						line-height: 1;
						font-family: PingFang SC-常规体, PingFang SC;

						.iconfont {
							color: #FF2529;
						}
					}

					// 选择器样式
					.picker-input {
						text-align: right;
						height: 35px;
						color: $uni-text-color;
						font-size: 30rpx;
						align-items: center;
						display: flex;
						justify-content: flex-end;

						.iconfont {
							padding-right: 16rpx;
							margin-top: 7rpx;
							transform: rotate(180deg);
							font-size: 24rpx;
							color: #C0C4CC;
						}

						&.picker-input-placeholder {
							color: #C0C4CC;
						}
					}

					//上传样式
					&.is-direction-top {
						align-items: flex-start;
					}

					.item-label-left {
						padding-top: 20rpx;
					}

					.upload {
						width: 100%;
						min-height: 236rpx;
						display: flex;
						align-items: center;
						padding: 0 28rpx;

						.box {
							position: relative;

							.img {
								display: block;
								width: 140rpx;
								height: 140rpx;
								margin-right: 20rpx;
							}

							.delete {
								position: absolute;
								top: 0;
								right: 20rpx;
								width: 32rpx;
								height: 32rpx;
								background: rgba(0, 0, 0, 0.6);
								border-radius: 0 8rpx 0 16rpx;
								display: flex;
								align-items: center;
								justify-content: center;

								.icon-paizhao {
									font-size: 35rpx;
									color: #BFBFBF;
								}
							}
						}


						.upload-box {
							width: 140rpx;
							height: 140rpx;
							border-radius: 8rpx 8rpx 8rpx 8rpx;
							border: 2rpx solid #DDDDDD;
							display: flex;
							flex-direction: column;
							justify-content: center;
							align-items: center;

							.icon-paizhao {
								font-size: 40rpx;
								color: #BFBFBF;
							}

							.text {
								margin-top: 20rpx;
								font-size: 24rpx;
								font-family: PingFang SC-常规体, PingFang SC;
								font-weight: 400;
								color: #999999;
							}
						}
					}

					.tips {
						margin-left: 20rpx;
						font-size: 20rpx;
						font-family: PingFang SC-常规体, PingFang SC;
						font-weight: 400;
						color: #999999;
						margin-bottom: 20rpx;
					}

					.picker-tree-data {
						width: 100%;
						display: flex;
						justify-content: center;
						align-items: center;

						.iconfont {
							padding-right: 16rpx;
							margin-top: 7rpx;
							transform: rotate(180deg);
							font-size: 24rpx;
							color: #C0C4CC;
						}

						::v-deep .tree-data {
							.input-value-border {
								border: none;
								border-radius: 0;
								font-size: $uni-font-size-default;
								color: $uni-text-color;
								padding-right: 0;

								.selected-list {
									justify-content: flex-end;

									.selected-item {
										word-break: break-all;
										display: -webkit-box;
										-webkit-line-clamp: 1;
										-webkit-box-orient: vertical;
										overflow: hidden
									}
								}
							}

							.arrow-area {
								display: none;
							}

							.placeholder {
								color: #C0C4CC;
								font-size: $uni-font-size-default;
								justify-content: flex-end;
							}
						}
					}
				}
			}
		}

		.examine-button {
			height: 126rpx;
			line-height: 126rpx;
			width: 100%;
			padding: 0 20rpx;
			position: fixed;
			left: 0;
			bottom: 0;
			right: 0;
			display: flex;
			align-items: center;
			background-color: $uni-default-bg;
			border: 1px solid #F0F1F5;

			.button {
				width: 100%;
				height: 86rpx;
				line-height: 86rpx;
				font-size: $uni-font-size-default;
				border-radius: 12rpx;
			}
		}
	}
</style>