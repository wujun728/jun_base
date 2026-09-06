<template>
	<view class="check-box">
		<view @click="selectBox">
			<view v-if="!configData.newvalue || (configData.newvalue && configData.newvalue.length === 0)" class="picker-input picker-input-placeholder">
				请选择
				<view class="iconfont icon-fanhui"></view>
			</view>
			<view class="picker-input" v-else>
				<text>{{ configData.newvalue.join('、')}}</text>
			</view>
		</view>

		<uni-popup ref="popupRef" background-color="#fff" type="bottom">
			<view class="check-content">
				<checkbox-group @change="changeBox">
					<label class="check-items" v-for="(item,index) in configData.options" :key="index">
						<checkbox :value="item.value" color="#1890FF" style="transform:scale(0.7)" />{{item.label}}
					</label>
				</checkbox-group>
			</view>
		</uni-popup>
	</view>
</template>

<script setup lang="ts">
	import { toRefs, ref } from 'vue'

	const props = defineProps({
		configData: {
			type: Object,
			default: () => {
				return {}
			},
		}
	})
	const { configData } = toRefs(props)

	const popupRef = ref()
	const emit = defineEmits(['change'])

	const selectBox = () => {
		popupRef.value.open()
	}

	const changeBox = (e : any) => {
		emit('change', e.detail.value)
	}
</script>

<style scoped lang="scss">
	.check-box {
		width: 100%;

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

		.check-content {
			height: 50vh;
			padding-top: 30rpx;

			.check-items {
				display: flex;
				align-items: center;
				padding: 0 30rpx 20rpx 30rpx;
			}
		}
	}
</style>