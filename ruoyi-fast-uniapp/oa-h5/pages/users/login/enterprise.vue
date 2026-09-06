<template>
	<view class="content">
		<default-nav-bar :is-jump-bar="false" jump-url="/pages/users/login/index" :is-right="true"
			:right-data="rightIcon" @handleNarItem="handleNarItem">
		</default-nav-bar>
		<view class="card" v-for="(item, index) in data.serverConfigInfo" :key="index">
			<view class="header">
				<image :src="item.logo"></image>
				<text>{{ item.enterprise_name }}</text>
			</view>
			<view class="line"></view>
			<view class="btn">
				<view class="agree-content">
					<checkbox-group @change="changeAgree(item)">
						<label>
							<checkbox style="transform:scale(0.7)" :value="index.toString()"
								:checked="item.isDefault" />
							选中企业
						</label>
					</checkbox-group>
				</view>
				<view class="del" @click.stop="delEnterprise(item, index)">
					<view class="iconfont icon-shanchu1"></view>
					<text>删除</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import defaultNavBar from '@/components/defaultNavBar/index.vue'
	import {
		showModal
	} from '@/utils/helper'
	import {
		onLoad
	} from '@dcloudio/uni-app'

	const serverConfigInfoData = uni.getStorageSync('serverConfigInfo') || []
	// 当前企业
	const chooseEnterprise = uni.getStorageSync('chooseEnterprise') || []

	const rightIcon = reactive([{
		type: 1,
		icon: 'icon-danchuang-tianjiahetong'
	}, ])
	const data = reactive({
		serverConfigInfo: serverConfigInfoData,
		type: 0
	})
	onLoad((options) => {
		data.type = options.type || 0
		if (chooseEnterprise) {
			data.serverConfigInfo.forEach(item => {
				item.isDefault = item.address === chooseEnterprise.address;
			});
		}
	})
	// 配置企业
	const handleNarItem = e => {
		if (e.type === 1) {
			uni.navigateTo({
				url: '/pages/users/login/config'
			})
		}
	}
	// 删除企业
	const delEnterprise = (item, index) => {
		let containsIsITtem = false;
		showModal('确定要删除该企业吗').then(() => {
			if (item.isDefault) {
				uni.showToast({
					title: '无法删除选中企业',
					icon: 'none',
					duration: 2000
				});
				return
			}
			if (data.serverConfigInfo) {
				containsIsITtem = data.serverConfigInfo.some(it => it.address === item.address);
				if (containsIsITtem) {
					data.serverConfigInfo.splice(index, 1);
					uni.setStorageSync('serverConfigInfo', data.serverConfigInfo)
				}
			}
		}).catch(() => {})
	}
	// 选择企业
	const changeAgree = (selectedItem) => {
		uni.setStorageSync('chooseApiUrl', selectedItem.address)
		// 将所有企业的isDefault设置为false
		data.serverConfigInfo.forEach(item => {
			item.isDefault = false;
		});

		// 被选中的企业设置isDefault为true
		const index = data.serverConfigInfo.findIndex(item => item === selectedItem);
		if (index !== -1) {
			data.serverConfigInfo[index].isDefault = true;
			uni.setStorageSync('chooseEnterprise', data.serverConfigInfo[index]);
		}

		uni.setStorageSync('serverConfigInfo', data.serverConfigInfo);
		uni.navigateTo({
			url: '/pages/users/login/index'
		})
	};
</script>

<style scoped lang="scss">
	.card {
		margin: 20rpx;
		background: #fff;
		padding: 32rpx 0 30rpx 24rpx;

		.header {
			display: flex;
			align-items: center;

			image {
				width: 80rpx;
				height: 80rpx;
				border-radius: 12rpx;
				margin-right: 20rpx;
			}
		}

		.line {
			height: 1rpx;
			background: #EEEEEE;
			margin: 32rpx 44rpx 28rpx 0;
		}

		.btn {
			display: flex;
			justify-content: space-between;
			align-items: center;
			color: #282828;

			.agree-content {
				margin-top: 10rpx;
				font-size: 26rpx;

				::v-deep .uni-label-pointer {
					display: flex;
					align-items: center;

					.uni-checkbox-input {
						border-radius: 50%;
					}
				}
			}

			.del {
				display: flex;
				align-items: center;
				margin-right: 44rpx;

				text {
					margin-left: 10rpx;
				}
			}
		}
	}

	::v-deep .uni-navbar__content {
		// #ifdef APP-PLUS
		top: 80rpx;
		// #endif
	}

	::v-deep .uni-navbar__header {
		// #ifdef APP-PLUS
		margin-bottom: 90rpx;
		// #endif
	}
</style>