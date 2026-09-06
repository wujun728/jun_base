<template>
	<view class="content">
		<!-- 添加支付页面 -->
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<default-nav-bar :is-right="true" :default-title="data.defaultTitle"></default-nav-bar>
		</view>
		<!-- 表单内容 -->
		<view class="examine-content m10">
			<uni-forms :border="false" label-width="80px">
				<view class="list-item ">
					<!-- 选择合同 -->
					<uni-forms-item class="input-label" v-if="!data.isContract">
						<template v-slot:label>
							<view class="uni-forms-item__label">选择合同 <text class="iconfont">*</text>
							</view>
						</template>
						<picker mode="selector" :range="data.contractList" :disabled="data.isContract" range-key="title" @change="contractChange">
							<view v-if="!formData.cid" class="picker-input picker-input-placeholder">
								请选择
								<view class="iconfont icon-fanhui"></view>
							</view>
							<view class="picker-input" v-else>
								<template v-if="formData.cid">
									{{data.contractList[getFindIndex(data.contractList, formData.cid)].title}}
								</template>
								<view class="iconfont icon-fanhui"></view>
							</view>
						</picker>

					</uni-forms-item>
					<!-- 业务类型 -->
					<uni-forms-item class="input-label">
						<template v-slot:label>
							<view class="uni-forms-item__label">支出类型 <text class="iconfont">*</text></view>
						</template>
						<view class="picker-tree-data">
							<uni-data-picker class="tree-data" placeholder="请选择账目分类" popup-title="账目分类" :localdata="data.treeData" :map="{text: 'name' , value: 'id' }" v-model="data.cate">
							</uni-data-picker>
							<view class="iconfont icon-fanhui"></view>
						</view>
					</uni-forms-item>

					<uni-forms-item class="input-label">
						<template v-slot:label>
							<view class="uni-forms-item__label">支出金额(元) <text class="iconfont">*</text></view>
						</template>
						<uni-easyinput :inputBorder="false" v-model="formData.num" type="number" :clearable="false" :styles="styles" :placeholder-style="placeholderStyle" maxlength="11"
							:autoHeight="true" placeholder="请填写">
						</uni-easyinput>
					</uni-forms-item>
					<!-- 支付方式 -->
					<uni-forms-item class="input-label">
						<template v-slot:label>
							<view class="uni-forms-item__label">支付方式<text class="iconfont">*</text></view>
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
					<!-- 开始时间 -->
					<uni-forms-item class="input-label">
						<template v-slot:label>
							<view class="uni-forms-item__label">支出时间<text class="iconfont">*</text></view>
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
					<uni-forms-item class="is-direction-top">
						<template v-slot:label>
							<view class="uni-forms-item__label mt36 p24">
								支出凭证
							</view>
							<view class="upload">
								<view class="box" v-if="data.imgs.length > 0">
									<image class="img" :src="data.imgs[0].src" mode="">
									</image>
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
							<text class="tips">建议734*1034，大小不超过2M，支持jpg、jpeg、png
								等</text>
						</template>
					</uni-forms-item>
				</view>
				<!-- 备注 -->
				<view class="list-item mt20 p24">
					<uni-forms-item class="is-direction-top">
						<template v-slot:label>
							<view class="uni-forms-item__label mt36">
								备注
							</view>
						</template>
						<uni-easyinput :inputBorder="false" v-model="formData.mark" type="textarea" :clearable="false" :styles="styles" :placeholder-style="placeholderStyle" :maxlength="256"
							:autoHeight="true" placeholder="填写备注信息">
						</uni-easyinput>
					</uni-forms-item>
				</view>
			</uni-forms>
		</view>
		<!-- 底部 -->
		<view class="examine-button">
			<button type="primary" :loading="loading" class="button" @click="handleConfirm">提交</button>
		</view>

	</view>
</template>
<script setup lang="ts">
	import { ref, reactive, computed,type Ref } from 'vue'
	import message from '@/utils/message'
	import defaultNavBar from '@/components/defaultNavBar/index'
	import { onLoad } from '@dcloudio/uni-app'
	import { contractSelectApi, payTypeApi, billSaveApi, billDetailApi, billPutApi } from '@/api/customer'
	import { financeBillCateApi } from '@/api/finance'
	import type { Res, GetType } from '@/utils/typeHelper'
	import moment from 'moment'
	import { delayedReLaunch, getFindIndex } from '@/utils/helper'
	import { uploadImage } from '@/utils/file'

	const placeholderStyle = ref('color: #C0C4CC;font-size: 30rpx')
	const styles = reactive({
		color: '#303133',
		disableColor: '#ffffff'
	})
	const data = reactive({
		defaultTitle: '添加支出',
		contractIndex: '',
		typesIndex: 0,
		payIndex: -1,
		imgs: [],
		rangePayment: [],
		contractList: [],
		rangeCate: [],
		cid: 0, // 合同id
		id: 0, // 付款id
		tab: 1,
		cate: '',
		isContract: false,
		treeData: []
	})
	// 定义表单
	const formData = reactive({
		eid: 0,
		cid: 0,
		types: 2,
		date: '',
		num: '',
		attach_ids: 0,
		type_id: 0,
		end_date: '',
		mark: '',
		bill_cate_id: '',
		cate_id: 0
	})

	onLoad(async (e : GetType) => {
		getBillCate()
		getPayType()
		formData.eid = e.eid
		getContractSelect(formData.eid)
		formData.date = moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
		if (e.cid > 0) {
			formData.cid = Number(e.cid)
			data.isContract = true
		}

		if (e.id) {
			data.id = Number(e.id)
			data.defaultTitle = '编辑支出'
			setTimeout(() => {
				getBillDetail(data.id)
			}, 300)
		}
	})

	// 获取支出类型
	const getBillCate = () : void => {
		financeBillCateApi({ types: 0 }).then((res : Res) => {
			data.treeData = res.data
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}
	// 获取支付方式接口
	const getPayType = () => {
		payTypeApi().then((res : Res) => {
			data.rangePayment = res.data.list
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}
	// 支付方式
	const payChange = (e : any) => {
		const len = e.detail.value
		data.payIndex = len
		formData.type_id = data.rangePayment[len].id
	}
	// 合同选择
	const contractChange = (e : any) => {
		const len = e.detail.value
		formData.cid = data.contractList[len].id
	}
	// 获取合同列表
	const getContractSelect = (id : number) : void => {
		let types = uni.getStorageSync('types')
		contractSelectApi(id).then((res : Res) => {
			data.contractList = res.data
			if (types == 1) {
				formData.cid = res.data[0].id
			}
			if (data.contractList.length <= 0) {
				message.error('暂无合同，请添加合同')
			}
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}
	// 上传付款凭证
	const uploadAvatar = () => {
		uploadImage('attach/imgs', { relation_type: 'bill' }).then(res => {
			res.data.id = res.data.attach_id
			data.imgs.push(res.data)
		}).catch(error => {
			message.error(error.message)
		})
	}
	// 添加支出
	const getBillDetail = (id : number) => {
		billDetailApi(id).then((res : Res) => {
			formData.num = res.data.num
			formData.date = res.data.date
			formData.cid = res.data.cid
			data.cate = res.data.bill_cate_id
			formData.type_id = res.data.type_id
			data.payIndex = getFindIndex(data.rangePayment, res.data.type_id)
			formData.types = res.data.types
			data.imgs = res.data.attachs
		}).catch((error : Res) => {
			loading.value = false
			message.error(error.message)
		})
	}

	// 删除附件
	const deleteImg = () => {
		data.imgs = []
		formData.attach_ids = 0
	}
	const loading : Ref<boolean> = ref(false)
	// 提交
	const handleConfirm = () : boolean => {
		if (!formData.cid && !data.isContract) {
			message.error('请选择合同')
			return false
		}
		if (!data.cate) {
			message.error('请选择支出类型')
			return false
		}
		if (!formData.num) {
			message.error('请填写支出金额')
			return false
		}
		if (!formData.type_id) {
			message.error('请选择支付方式')
			return false
		}
		if (!formData.date) {
			message.error('请选择支付时间')
			return false
		}

		if (data.imgs.length > 0) {
			formData.attach_ids = data.imgs[0].id
		}
		formData.bill_cate_id = data.cate
		if (!loading.value) {
			if (data.id > 0) {
				billEdit(data.id, formData)
			} else {
				billSave(formData)
			}
		}
	}

	//支出保存
	const billSave = (datas : object) => {
		loading.value = true
		billSaveApi(datas).then((res : Res) => {
			loading.value = true
			message.success(res.message)
			if (data.isContract) {
				delayedReLaunch(`/pages/customer/contract/details?id=${formData.cid}&tab=1`)
			} else {
				delayedReLaunch(`/pages/customer/list/details?id=${formData.eid}&type=3`)
			}
		}).catch((error : Res) => {
			loading.value = false
			message.error(error.message)
		})
	}

	// 编辑支出
	const billEdit = (id : number, datas : object) : void => {
		loading.value = true
		billPutApi(id, datas).then((res : Res) => {
			message.success(res.message)
			loading.value = true
			if (data.isContract) {
				delayedReLaunch(`/pages/customer/contract/details?id=${formData.cid}&tab=1`)
			} else {
				delayedReLaunch(`/pages/customer/list/details?id=${formData.eid}&type=3`)
			}
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}
</script>

<style lang="scss">
	.content {
		width: 100%;
		position: relative;

		.cr-position-header {
			background-color: #fff;
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

		::v-deep .uni-input-wrapper {
			text-align: right;
		}

		::v-deep .uni-date-x {
			padding: 0;
			color: #303133;
			font-size: 30rpx;
		}

		::v-deep .uni-date__x-input {
			padding: 0;
		}

		.examine-content {
			padding-top: calc($uni-default-bar-height + var(--status-bar-height));
			padding-bottom: 126rpx;
		}

		.uni-forms-item__label {
			height: auto;
			padding: 0;
			font-size: 30rpx;
			color: $uni-text-color;
			line-height: 1;
			font-family: PingFang SC-常规体, PingFang SC;

			.iconfont {
				color: #FF2529;
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

		.p24 {
			padding: 0 24rpx;
		}

		.upload {

			width: 100%;
			min-height: 236rpx;
			display: flex;
			align-items: center;
			padding: 28rpx 28rpx 0 28rpx;


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

		.list-item {
			background-color: #fff;
			border-radius: 12rpx;
			width: 100%;
		}

		.mt36 {
			margin-top: 36rpx;

		}

		.mt20 {
			margin-top: 20rpx;
		}

		.uni-easyinput__content-textarea {
			min-height: 252rpx;
		}

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
		}

		.picker-input-placeholder {
			color: #C0C4CC;
		}

		::v-deep .uni-forms-item {
			margin-bottom: 0;
			border-bottom: 1px solid #EBEEF5;

			&:last-of-type {
				border-bottom: none;
			}
		}

		.input-label {
			padding: 18rpx 24rpx 18rpx 0;
			margin-left: 24rpx;
			align-items: center;

			::v-deep .uni-easyinput__content-input {
				text-align: right;
				padding-right: 0 !important;
			}

			::v-deep .uni-icons {
				display: none;
			}

			::v-deep .uni-forms-item__label {
				max-width: 198rpx;
				display: flex;
				line-height: 1.2;

				.iconfont {
					width: 16rpx;
				}
			}
		}
	}

	::v-deep .uni-input-input {
		font-size: 30rpx;
	}
</style>