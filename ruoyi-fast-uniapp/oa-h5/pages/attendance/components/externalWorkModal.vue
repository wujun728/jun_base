<template>
	<view class="modal">
		<view class="head">
			<view class="title">{{!isEffectiveRange?'您不在考勤范围内，是否继续打卡？':'您需要进行拍照打卡，是否继续打卡？'}}</view>
			<view class="time">
				<text class="iconfont icon-kaoqin-shijian"></text> 打卡时间
				{{ timeString }}
			</view>
			<view class="address"><text class="iconfont icon-kaoqin-dingwei"></text> {{ address }}</view>
		</view>
		<view class="content">
			<uni-easyinput type="textarea" :placeholder-style="placeholderStyle" :inputBorder="false"
				v-model="formData.text" placeholder="请输入内容" :maxlength="140" :rows="10"></uni-easyinput>
			<view class="upload">
				<template v-if="formData.imgs.length">
					<view class="box" v-for="(item, index) in formData.imgs" :key="index">
						<image class="img" :src="item" mode=""> </image>
						<view class="delete" @click="deleteImg(index)">
							<text class="iconfont icon-shenpizhongxin-jujue"></text>
						</view>
					</view>
				</template>
				<view v-if="formData.imgs.length < 4" class="upload-box" @click="uploadAvatar">
					<view class="iconfont icon-paizhao"></view>
				</view>
			</view>
		</view>
		<view class="bottom">
			<view class="cancel btn" @click="cancel">取消</view>
			<view class="line"></view>
			<view class="ok btn" @click="ok">确定</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		toRefs
	} from "vue";
	import {
		uploadImage
	} from "@/utils/file";

	const props = defineProps({
		// 照片是否必填
		isPic: {
			type: Number,
			default: 0,
		},
		// 备注是否必填
		isText: {
			type: Number,
			default: 0,
		},
		// 打卡类型
		typeName: {
			type: String,
			default: "",
		},
		// 是否在考勤范围
		isEffectiveRange: {
			type: Boolean,
			default: false,
		},
		address: {
			type: String,
			default: "",
		},
		onWork: {
			type: String,
			default: "",
		},
		// 当前打卡进度
		recordLength: {
		  type: Number,
		  default: 0,
		},
	});
	const {
		type,
		address,
		onWork,
		isPic,
		isText,
		recordLength,
		isEffectiveRange
	} = toRefs(props);
	const timeString = ref();
	onMounted(() => {
		// 获取当前时间
		const now = new Date();
		// 获取小时、分钟、秒数
		const hour = now.getHours();
		const minute = now.getMinutes();
		const second = now.getSeconds();

		// 格式化小时、分钟、秒数字符串
		const formattedHour = hour < 10 ? `0${hour}` : hour;
		const formattedMinute = minute < 10 ? `0${minute}` : minute;
		const formattedSecond = second < 10 ? `0${second}` : second;

		// 将时间部分拼接成一个字符串
		timeString.value = `${formattedHour}:${formattedMinute}:${formattedSecond}`;
	});

	const formData = ref({
		text: "",
		imgs: [],
		is_external: 0
	});
	const placeholderStyle = ref("color: #C0C4CC;font-size: 28rpx");
	const emit = defineEmits(["cancel", "ok"]);

	const cancel = () => {
		emit("cancel");
	};
	const ok = () => {
		formData.value.is_external = isEffectiveRange.value? 0 : 1
		if (isText.value == 1 && !formData.value.text) {
			uni.showToast({
				title: '请先输入备注',
				icon: 'none'
			})
			return
		} else if (isPic.value == 1 && !formData.value.imgs.length) {
			uni.showToast({
				title: '请先选择照片',
				icon: 'none'
			})
			return
		}
		emit("ok", formData.value);
	};

	// 上传照片
	const uploadAvatar = () => {
		uploadImage('common/upload', {
				relation_type: "bill"
			}, 20, ['camera'])
			.then((res) => {
				res.data.id = res.data.attach_id;
				formData.value.imgs.push(res.data.src);
				uni.showToast({
					title: "上传成功",
					icon: 'none'
				});
			})
			.catch((error) => {
				message.error(error.message);
				uni.showToast({
					title: "error",
					icon: 'none'
				});
			});
	};
	// 删除附件
	const deleteImg = (index) => {
		console.log(index);
		formData.value.imgs.splice(index, 1);
	};
</script>

<style lang="scss" scoped>
	.modal {
		display: flex;
		flex-direction: column;
		background-color: #fff;
		width: 654rpx;
		height: 742rpx;
    border-radius: 16rpx 16rpx 16rpx 16rpx;
		background-image: url("@/static/image/attendance/external-bag.png");
		background-repeat: no-repeat;
		background-size: 654rpx 132.35rpx;
		padding: 40rpx;

		.head {
			.title {
				font-size: 32rpx;
				font-weight: 500;
				color: #303133;
				line-height: 32rpx;
			}

			.time {
				font-size: 28rpx;
				font-weight: 500;
				color: #308bf8;
				line-height: 28rpx;
				margin-top: 28rpx;
			}

			.iconfont {
				font-size: 26rpx;
			}

			.address {
				font-size: 24rpx;
				font-weight: 400;
				color: #909399;
				line-height: 24rpx;
				margin-top: 22rpx;
			}
		}

		.content {
			width: 100%;
			height: 380rpx;
			background: #f0f1f5;
			border-radius: 8rpx 8rpx 8rpx 8rpx;
			margin-top: 42rpx;
			padding: 14rpx 24rpx 36rpx;

			::v-deep .is-textarea {
				background-color: inherit !important;
			}

			::v-deep .uni-easyinput__content-textarea {
				height: 200rpx;
			}

			.upload {
				width: 100%;
				display: flex;
				align-items: center;

				.box {
					position: relative;

					.img {
						display: block;
						width: 100rpx;
						height: 100rpx;
						margin-right: 20rpx;
					}

					.delete {
						position: absolute;
						top: -10rpx;
						right: 8rpx;
						background: #c0c4cc;
						border-radius: 50%;
						display: flex;
						align-items: center;
						justify-content: center;
						color: #fff;
						padding: 3rpx 4rpx;

						.icon-shenpizhongxin-jujue {
							font-size: 20rpx;
						}
					}
				}

				.upload-box {
					width: 100rpx;
					height: 100rpx;
					border-radius: 8rpx 8rpx 8rpx 8rpx;
					border: 2rpx dashed #dddddd;
					background-color: #ffffff;
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: center;

					.icon-paizhao {
						font-size: 30rpx;
						color: #bfbfbf;
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
		}

		.bottom {
			border-top: 1px solid #ebeef5;
			display: flex;
			position: absolute;
			bottom: 0;
			left: 0;
			width: 100%;

			.line {
				width: 1px;
				height: 88rpx;
				background-color: #ebeef5;
			}

			.btn {
				flex: 1;
				text-align: center;
				line-height: 88rpx;
				font-size: 30rpx;
			}

			.cancel {
				color: #909399;
			}

			.ok {
				color: #308bf8;
			}
		}
	}
</style>
