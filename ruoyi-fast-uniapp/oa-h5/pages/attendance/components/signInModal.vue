<template>
	<view class="modal">
		<image v-if="type" src="/static/image/attendance/dk-success.png" alt="" />
		<image v-else src="/static/image/attendance/dk-error.png" alt="" />
		<view class="title">{{onWork == 'on' ? '上班打卡成功' : '下班打卡成功'}} {{ clockTime }}</view>
		<view class="tip">{{onWork == 'on' ? '上班了，打卡成功！' : '工作辛苦了，感谢你一天的努力'}}</view>
		<view class="btn" @click="ok">我知道了</view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		toRefs
	} from "vue";
	const props = defineProps({
		type: {
			type: Number,
			default: 0,
		},
		clockTime: {
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
		onWork,
		clockTime
	} = toRefs(props);
	const emit = defineEmits(["cancel", "ok"]);

	const ok = () => {
		emit("ok", false);
	};
</script>

<style lang="scss" scoped>
	.modal {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		background-color: #fff;
		width: 560rpx;
		height: 676rpx;
		background: #ffffff;
		border-radius: 16rpx 16rpx 16rpx 16rpx;

		image {
			width: 350rpx;
			height: 198rpx;
		}

		.title {
			font-size: 36rpx;
			font-weight: 500;
			color: #303133;
			line-height: 36rpx;
			margin: 68rpx 0 30rpx 0;
		}

		.tip {
			font-size: 28rpx;
			font-weight: 400;
			color: #909399;
			line-height: 28rpx;
		}

		.btn {
			display: flex;
			align-items: center;
			justify-content: center;
			width: 332rpx;
			height: 86rpx;
			background: #308bf8;
			border-radius: 12rpx;
			font-size: 30rpx;
			font-weight: 400;
			color: #ffffff;
			margin-top: 72rpx;
		}
	}
</style>