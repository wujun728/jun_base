s<template>
	<view class="content">
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<default-nav-bar :is-right="true" :default-title="data.defaultTitle"></default-nav-bar>
		</view>
		<!-- 表单内容 -->
		<view class="examine-content m10">
			<uni-forms :border="false" label-width="80px">
				<view class="list-item ">
					<uni-forms-item class="is-direction-top p24">
						<template v-slot:label>
							<view class="uni-forms-item__label mt36">
								记录描述 <text class="iconfont">*</text>
							</view>
						</template>
						<uni-easyinput :inputBorder="false" class="max-height" v-model="formData.content" type="textarea" :clearable="false" :styles="styles"
							:placeholder-style="placeholderStyle" :maxlength="256" :autoHeight="true" placeholder="请填写记录描述">
						</uni-easyinput>
					</uni-forms-item>
				</view>

				<view class="list-item mt20 p24">
					<uni-forms-item class="is-direction-top">
						<template v-slot:label>
							<view class="uni-forms-item-con mt36 ">
								<uni-row class="">
									<uni-col :span="22">
										<view class="">添加文件</view>
										<view class="tips">(建议大小不超过{{fileSizeOne}}M，支持图片、附件、文档)</view>
									</uni-col>
									<uni-col :span="2" class="text-right" @click="uploadAvatar">
										<text class="iconfont icon-biaodan-tianjia"></text>
									</uni-col>
								</uni-row>
							</view>
							<view class="upload">
								<upload-from-list :upload-from-data="data.fileList" :is-clear="true" @handleClear="handleClear"></upload-from-list>
							</view>
						</template>
					</uni-forms-item>
				</view>
			</uni-forms>
		</view>
		<!-- 底部 -->
		<view class="examine-button">
			<button type="primary" :loading="loading" @click="handleConfirm">提交</button>
		</view>
	</view>
</template>

<script setup lang="ts">
	import defaultNavBar from '@/components/defaultNavBar/index'
	import { ref, reactive,type Ref } from 'vue'
	import message from '@/utils/message'
	import uploadFromList from '@/pages/users/examine/components/uploadFromList.vue'
	import { uploadFlie } from '@/utils/file'
	import { delayedReLaunch, fileSizeOne } from '@/utils/helper'
	import { onLoad } from '@dcloudio/uni-app'
	import type { GetType, Res } from '@/utils/typeHelper'
	import { clientContractResourceApi, clientContractResourceDetailApi, clientContractResourceEditApi } from '@/api/customer'

	const placeholderStyle = ref('color: #C0C4CC;font-size: 30rpx')
	const styles = reactive({
		color: '#303133',
		disableColor: '#ffffff'
	})

	interface F {
		relation_type : string
		relation_id ?: number
	}

	// 定义表单
	const data = reactive({
		type: 1,
		cid: 0, // 合同id
		id: 0, // 合同资料id
		fileList: [],
		defaultTitle: ''
	})
	const formData = reactive({
		content: '',
		attach_ids: [],
		cid: 0,
	})

	onLoad((options : GetType) => {
		if (options.cid) {
			data.cid = Number(options.cid)
		}
		data.defaultTitle = '添加合同记录'
		if (options.id) {
			data.id = Number(options.id)
			resourceDetail(data.id)
			data.defaultTitle = '编辑合同记录'
		}
	})

	// 详情
	const resourceDetail = (id : number) : void => {
		clientContractResourceDetailApi(id).then((res : Res) => {
			formData.content = res.data.content
			formData.cid = res.data.cid
			data.fileList = res.data.attachs
		}).catch((err : Res) => {
			loading.value = false
			message.error(err.message)
		})
	}


	// 提交表单
	const handleConfirm = () : boolean => {
		if (!formData.content) {
			message.error('请填写记录描述')
			return false
		}
		if (data.fileList.length > 0) {
			data.fileList.map((value) => {
				formData.attach_ids.push(value.id)
			})
		}
		formData.cid = data.cid
		if (data.id > 0) {
			if (!loading.value) {
				clientContractEdit(data.id, formData)
			}
		} else {
			if (!loading.value) {
				clientContract(formData)
			}
		}
	}

	const loading : Ref<boolean> = ref(false)
	// 添加合同资料
	const clientContract = (datas : object) : void => {
		loading.value = true
		clientContractResourceApi(datas).then((res : Res) => {
			message.success(res.message)
			delayedReLaunch(`/pages/customer/contract/details?id=${data.cid}&tab=4`)
		}).catch((err : Res) => {
			loading.value = false
			message.error(err.message)
		})
	}
	// 编辑合同资料
	const clientContractEdit = (id : number, datas : object) : void => {
		loading.value = true
		clientContractResourceEditApi(id, datas).then((res : Res) => {
			message.success(res.message)
			delayedReLaunch(`/pages/customer/contract/details?id=${data.cid}&tab=4`)
		}).catch((err : Res) => {
			loading.value = false
			message.error(err.message)
		})
	}


	// 删除上传记录
	const handleClear = (index : number) : void => {
		data.fileList.splice(index, 1)
	}

	// 添加图片
	const fileForm = reactive(<F>{})
	const uploadAvatar = () => {
		fileForm.relation_type = 'contract'
		if (data.id > 0) {
			fileForm.relation_id = data.id
		}
		uploadFlie('attach/imgs', fileForm, fileSizeOne).then(res => {
			res.data.id = res.data.attach_id
			data.fileList.push(res.data)
		}).catch(error => {
			message.error(error)
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
			font-size: 20rpx;
			font-family: PingFang SC-常规体, PingFang SC;
			font-weight: 400;
			color: #999999;
			padding-top: 16rpx;
		}

		::v-deep .uni-input-wrapper {
			text-align: right;
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

		.uni-forms-item-con {
			width: 100%;
			font-size: 30rpx;
			color: $uni-text-color;
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

			uni-button {
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
			min-height: 276rpx;
			display: flex;
			flex-wrap: wrap;
			padding: 36rpx 0;
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

		::v-deep .uni-easyinput__content-textarea {
			min-height: 460rpx !important;
			font-size: 28rpx;
		}

		.max-height {
			min-height: 460rpx;
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
			padding: 18rpx 0;
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
</style>