<template>
	<view class="content">
		<view class="cr-position-header">
			<view class="status_bar"></view>
			<default-nav-bar :index="1"></default-nav-bar>
		</view>
		<view class="assessment m10">
			<view class="cr-center-list">
				<view class="center-list-item" v-for="(item,index) in data.listData" :key="index">
					<uni-row class="center-list-item-con">
						<uni-col :span="6">姓名<text class="is-required">*</text></uni-col>
						<uni-col :span="18" class="display-align">
							<uni-easyinput v-model="item.name" :inputBorder="false" type="text" placeholder="请输入姓名"></uni-easyinput>
						</uni-col>
					</uni-row>
					<uni-row class="center-list-item-con">
						<uni-col :span="6">手机号<text class="is-required">*</text></uni-col>
						<uni-col :span="18" class="display-align">
							<uni-easyinput v-model="item.phone" :inputBorder="false" type="number" placeholder="请输入手机号"></uni-easyinput>
						</uni-col>
					</uni-row>
				</view>

				<view class="center-list-item">
					<view class="add-botton display-align" @click="addUserItem">
						<text class="iconfont icon-liuyan-fasong"></text>添加新成员
					</view>
				</view>
			</view>
		</view>
		<view class="cr-bottom-button p10">
			<button type="primary">确认添加</button>
		</view>
	</view>
</template>

<script setup lang="ts">
	import defaultNavBar from '@/components/defaultNavBar/index'

	const data = reactive({
		title: '',
		listData: [{ name: '', phone: '' }]
	})

	const addUserItem = () : void => {
		data.listData.push({ name: '', phone: '' })
	}
</script>

<style>
	page {
		background-color: $uni-default-bg;
	}
</style>

<style scoped lang="scss">
	@import '@/static/css/form-item-list.scss';

	.content {
		width: 100%;

		.assessment {
			padding-top: calc(44px + var(--status-bar-height));
			padding-bottom: $uni-bottom-default-height;
		}
	}
</style>