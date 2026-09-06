<script setup lang="ts">
import tab from '@/plugins/tab';
import { AddressInfo, useAddressListPage } from './index';

// 使用列表页面Hook
const {
	addressList,
	emptyStatus,
	setDefaultAddress,
	deleteAddress
} = useAddressListPage();

// 跳转到添加地址页面
function toAddSite() {
	tab.navigateTo('/pages_template/pages/address/addSite');
}

// 跳转到编辑地址页面
function toEditSite(id: string) {
	tab.navigateTo(`/pages_template/pages/address/addSite?id=${id}`);
}

// 设置为默认地址
function handleSetDefault(id: string) {
	try {
		const success = setDefaultAddress(id);

		if (success) {
			uni.showToast({
				title: '设置成功',
				icon: 'success'
			});
		} else {
			uni.showToast({
				title: '设置失败',
				icon: 'none'
			});
		}
	} catch (e) {
		console.error('设置默认地址失败', e);
		uni.showToast({
			title: '设置失败',
			icon: 'none'
		});
	}
}

// 删除地址
function handleDeleteAddress(id: string) {
	uni.showModal({
		title: '提示',
		content: '确定要删除此地址吗？',
		success: (res) => {
			if (res.confirm) {
				try {
					const success = deleteAddress(id);

					if (success) {
						uni.showToast({
							title: '删除成功',
							icon: 'success'
						});
					} else {
						uni.showToast({
							title: '删除失败',
							icon: 'none'
						});
					}
				} catch (e) {
					console.error('删除地址失败', e);
					uni.showToast({
						title: '删除失败',
						icon: 'none'
					});
				}
			}
		}
	});
}

// 选择并返回地址（用于从订单页面选择地址的场景）
function selectAddress(address: AddressInfo) {
	const pages = getCurrentPages();
	const prevPage: any = pages[pages.length - 2];

	// 检查页面是否从订单页面跳转而来
	if (prevPage && prevPage.$page?.options?.from === 'order') {
		// 设置选中的地址并返回
		uni.$emit('address-selected', address);
		tab.navigateBack();
	}
}
</script>

<template>
	<view class="address-container">
		<!-- 空状态 -->
		<view class="empty-state" v-if="emptyStatus">
			<image src="/static/images/empty-address.png" mode="aspectFit" class="empty-image"></image>
			<view class="empty-text">您还没有添加收货地址</view>
		</view>

		<!-- 地址列表 -->
		<view v-else>
			<view class="item" v-for="(address, index) in addressList" :key="address.id">
				<view class="top" @tap="selectAddress(address)">
					<view class="name">{{ address.name }}</view>
					<view class="phone">{{ address.phone }}</view>
					<view class="tag">
						<text v-if="address.isDefault" class="red">默认</text>
						<text v-if="address.tag">{{ address.tag }}</text>
					</view>
				</view>
				<view class="bottom" @tap="selectAddress(address)">
					{{ address.region }} {{ address.address }}
				</view>
				<view class="actions">
					<view class="action-btn" @tap="handleSetDefault(address.id)" v-if="!address.isDefault">
						<u-icon name="checkmark-circle" color="#999" size="40rpx"></u-icon>
						<text>设为默认</text>
					</view>
					<view class="action-btn" @tap="toEditSite(address.id)">
						<u-icon name="edit-pen" color="#999" size="40rpx"></u-icon>
						<text>编辑</text>
					</view>
					<view class="action-btn" @tap="handleDeleteAddress(address.id)">
						<u-icon name="trash" color="#999" size="40rpx"></u-icon>
						<text>删除</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 新建地址按钮 -->
		<view class="addSite" @tap="toAddSite">
			<view class="add">
				<u-icon name="plus" color="#ffffff" class="icon" :size="30"></u-icon>新建收货地址
			</view>
		</view>
	</view>
</template>

<style lang="scss" scoped>
.address-container {
	padding-bottom: 180rpx;
	min-height: 100vh;
	background-color: #f5f5f5;
}

.empty-state {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding-top: 200rpx;

	.empty-image {
		width: 200rpx;
		height: 200rpx;
		margin-bottom: 40rpx;
	}

	.empty-text {
		color: #999;
		font-size: 30rpx;
	}
}

.item {
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
	border-radius: 16rpx;
	margin: 20rpx;
	padding: 30rpx;
	background-color: #ffffff;

	.top {
		display: flex;
		align-items: center;
		font-size: 32rpx;
		font-weight: bold;

		.phone {
			margin-left: 40rpx;
			color: #666666;
		}

		.tag {
			margin-left: auto;

			text {
				background-color: #e0f7fa;
				color: #00bcd4;
				border-radius: 20rpx;
				padding: 8rpx 16rpx;
				font-size: 24rpx;
				margin-left: 10rpx;
			}

			.red {
				background-color: #ffebee;
				color: #d32f2f;
			}
		}
	}

	.bottom {
		margin-top: 20rpx;
		font-size: 28rpx;
		color: #666666;
		line-height: 1.6;
		padding-bottom: 20rpx;
		border-bottom: 1px solid #f0f0f0;
	}

	.actions {
		display: flex;
		justify-content: flex-end;
		margin-top: 20rpx;

		.action-btn {
			display: flex;
			align-items: center;
			margin-left: 30rpx;
			font-size: 26rpx;
			color: #666;

			text {
				margin-left: 6rpx;
			}
		}
	}
}

.addSite {
	position: fixed;
	bottom: 40rpx;
	left: 50%;
	transform: translateX(-50%);
	width: 80%;
	height: 100rpx;
	line-height: 100rpx;
	background: linear-gradient(90deg, #ff4034, #fa3534);
	border-radius: 50rpx;
	text-align: center;
	color: #ffffff;
	font-size: 32rpx;
	box-shadow: 0 8rpx 16rpx rgba(250, 53, 52, 0.2);
	z-index: 100;

	.add {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 100%;
		width: 100%;

		.icon {
			margin-right: 10rpx;
		}
	}
}
</style>
