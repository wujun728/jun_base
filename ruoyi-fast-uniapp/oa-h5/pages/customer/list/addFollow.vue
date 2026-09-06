s<template>
	<view class="content">
		<!-- 跟进记录和跟进提醒用type来区分  跟进记录：type==1 跟进提醒type==2 -->
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<default-nav-bar :is-right="true" :default-title="data.defaultTitle">
			</default-nav-bar>
		</view>
		<!-- 表单内容 -->
		<view class="examine-content m10">
			<uni-forms :border="false" label-width="80px">
				<view class="list-item ">
					<uni-forms-item class="is-direction-top p24">
						<template v-slot:label>
							<view class="uni-forms-item__label mt36">
								{{data.type==1?'跟进信息':'提醒内容'}}<text class="iconfont">*</text>
							</view>
						</template>
						<uni-easyinput :inputBorder="false" class="max-height" v-model="formData.content"
							type="textarea" :clearable="false" :styles="styles" :placeholder-style="placeholderStyle"
							:maxlength="256" :autoHeight="true" :placeholder="data.type==1?'填写跟进信息':'填写提醒内容'">
						</uni-easyinput>
					</uni-forms-item>
				</view>

				<view class="list-item mt20 p24">
					<uni-forms-item class="is-direction-top" v-if="data.type==1">
						<template v-slot:label>
							<view class="uni-forms-item__label mt36 ">
								添加图片 <text class="tips">(建议734*1034，大小不超过{{fileSizeOne}}M，支持jpg、jpeg、png等)</text>
							</view>
							<view class="upload">
								<view v-for="(item,index) in data.imgs" :key="index" class="box">
									<image class="img" v-if="isTypeImage(item.name)" :src="item.url" mode=""></image>
									<image class="img" v-else
										:src="'/static/image/cloudfile/'+isFileTypeIcon(item.name)" mode=""></image>
									<view class="delete" @click="deleteImg(item)">
										<text class="iconfont icon-shenpizhongxin-jujue"></text>
									</view>
								</view>
								<view class="upload-box" @click="uploadAvatar">
									<view class="iconfont icon-paizhao"></view>
									<view class="text">
										添加图片
									</view>
								</view>
							</view>
						</template>
					</uni-forms-item>
					<uni-forms-item class="input-label" v-if="data.type==2">
						<template v-slot:label>
							<view class="uni-forms-item__label">提醒时间 <text class="iconfont">*</text></view>
						</template>
						<uni-datetime-picker type="datetime" :clear-icon="false" :border="false" v-model="formData.time"
							@change="timeChange">
							<view v-if="!formData.time" class="picker-input picker-input-placeholder">
								请选择
								<view class="iconfont icon-fanhui"></view>
							</view>
							<view class="picker-input" v-if="formData.time">
								{{formData.time}}
							</view>
						</uni-datetime-picker>
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

<script setup>
	import defaultNavBar from '@/components/defaultNavBar/index'
	import {
		ref,
		reactive,
		toRefs
	} from 'vue'
	import message from '@/utils/message'
	import {
		followSaveApi,
		followPutApi
	} from '@/api/customer'
	import {
		uploadImage
	} from '@/utils/file'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	const placeholderStyle = ref('color: #C0C4CC;font-size: 30rpx')
	const styles = reactive({
		color: '#303133',
		disableColor: '#ffffff'
	})

	// 定义表单
	const data = reactive({
		type: 1,
		eid: 0, // 客户id
		id: 0, // 跟进记录id
		defaultTitle: '添加跟进记录',
		imgs: [],
		fid: 0
	})
	const formData = reactive({
		content: '',
		time: '',
		attach_ids: [],
		eid: '',
		types: 0,
		follow_id: 0
	})
	const loading = ref(false)

	onLoad(e => {
		if (!e.data) {
			data.type = Number(e.type)
			if (data.type == 2) {
				data.defaultTitle = '添加跟进提醒'
				formData.types = 1
			}
		}
		if (e.kid) {
			data.eid = Number(e.kid)
			formData.eid = Number(e.kid)
		}
		if (e.eid) {
			data.eid = Number(e.eid)
			formData.eid = Number(e.eid)
		}

		if (e.fid) {
			data.fid = Number(e.fid)
		}
		if (e.data) {
			if (e.type == 1) {
				data.defaultTitle = '编辑跟进记录'
				data.type = 1
				formData.types = 0
			} else {
				data.defaultTitle = '编辑跟进提醒'
				data.type = 2
				formData.types = 1
			}
			let editData = JSON.parse(e.data)
			data.id = editData.id
			formData.content = editData.content
			formData.time = editData.time
			data.imgs = editData.attachs
		}
	})

	const deleteImg = e => {
		data.imgs = data.imgs.filter(item => {
			return item.id !== e.id
		})
	}
	import {
		delayedReLaunch,
		debounce,
		isTypeImage,
		isFileTypeIcon,
		fileSizeOne
	} from '@/utils/helper'
	// 提交表单
	const handleConfirm = debounce(() => {
		if (data.type === 1 && !formData.content) {
			message.error('跟进信息不能为空')
			return false
		} else if (data.type === 2 && !formData.content) {
			message.error('提醒内容不能为空')
			return false
		}
		if (data.type === 2) {
			if (!formData.time) {
				message.error('请选择提醒时间')
				return false
			}
		}

		data.imgs.map(item => {
			formData.attach_ids.push(item.id)
		})

		if (data.fid > 0) {
			formData.follow_id = data.fid
		}

		if (data.defaultTitle === '编辑跟进提醒' || data.defaultTitle === '编辑跟进记录') {
			let id = data.id
			loading.value = true
			followPutApi(id, formData).then(res => {
				loading.value = false
				message.success(res.message)
				let type = 2
				delayedReLaunch(`/pages/customer/list/details?id=${data.eid}&type=${type}`)
			}).catch(err => {
				loading.value = false
				message.error(err.message)
			})
		} else {
			loading.value = true
			followSaveApi(formData).then(res => {
				loading.value = false
				let type = 2
				const str = data.fid > 0 ? '/pages/users/schedule/index' :
					`/pages/customer/list/details?id=${data.eid}&type=${type}`
				delayedReLaunch(str)
			}).catch(err => {
				loading.value = false
				message.error(err.message)
			})
		}
	}, 500)

	// 添加图片
	const uploadAvatar = () => {
		const config = {
			eid: data.eid,
			relation_type: 'follow'
		}
		if (data.id > 0) {
			config.relation_id = data.id
		}
		uploadImage('attach/imgs', config, fileSizeOne).then(res => {
			data.imgs.push(res.data)
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
			align-items: center;
			flex-wrap: wrap;
			padding: 36rpx 0;

			.box {
				position: relative;

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

			.img {
				display: block;
				width: 140rpx;
				height: 140rpx;
				margin-right: 20rpx;
				margin-bottom: 10rpx;
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