<script setup>
import { ref, reactive } from 'vue';
import classifyData from "@/pages_template/static/js/classify.data.js";

const tabbar = reactive(classifyData);
const scrollTop = ref(0); // tab标题的滚动条位置
const current = ref(0); // 预设当前项的值
const menuHeight = ref(0); // 左边菜单的高度
const menuItemHeight = ref(0); // 左边菜单item的高度

// 点击左边的栏目切换
const swichMenu = async (index) => {
	if (index === current.value) return;
	current.value = index;
	// 如果为0，意味着尚未初始化
	if (menuHeight.value === 0 || menuItemHeight.value === 0) {
		await getElRect('menu-scroll-view', 'menuHeight');
		await getElRect('u-tab-item', 'menuItemHeight');
	}
	// 将菜单菜单活动item垂直居中
	scrollTop.value = index * menuItemHeight.value + menuItemHeight.value / 2 - menuHeight.value / 2;
};

// 获取一个目标元素的高度
const getElRect = (elClass, dataVal) => {
	return new Promise((resolve, reject) => {
		const query = uni.createSelectorQuery().in(this);
		query.select('.' + elClass).fields({ size: true }, res => {
			// 如果节点尚未生成，res值为null，循环调用执行
			if (!res) {
				setTimeout(() => {
					getElRect(elClass, dataVal);
				}, 10);
				return;
			}
			if (dataVal === 'menuHeight') {
				menuHeight.value = res.height;
			} else if (dataVal === 'menuItemHeight') {
				menuItemHeight.value = res.height;
			}
			resolve();
		}).exec();
	});
};

const clickMenu = (menu) => {
	console.log(menu);
};
</script>

<template>
	<view class="u-wrap">
		<view class="u-search-box">
			<view class="u-search-inner">
				<u-icon name="search" color="#909399" :size="28"></u-icon>
				<text class="u-search-text">搜索uView</text>
			</view>
		</view>
		<view class="u-menu-wrap">
			<scroll-view scroll-y scroll-with-animation class="u-tab-view menu-scroll-view" :scroll-top="scrollTop">
				<view v-for="(item, index) in tabbar" :key="index" class="u-tab-item"
					:class="[current == index ? 'u-tab-item-active' : '']" :data-current="index"
					@tap.stop="swichMenu(index)">
					<text class="u-line-1">{{ item.name }}</text>
				</view>
			</scroll-view>
			<block v-for="(item, index) in tabbar" :key="index">
				<scroll-view scroll-y class="right-box" v-if="current == index">
					<view class="page-view">
						<view class="class-item">
							<view class="item-title">
								<text>{{ item.name }}</text>
							</view>
							<view class="item-container">
								<view class="thumb-box" v-for="(item1, index1) in item.foods" :key="index1"
									@click="clickMenu(item1)">
									<image class="item-menu-image" :src="item1.icon" mode=""></image>
									<view class="item-menu-name">{{ item1.name }}</view>
								</view>
							</view>
						</view>
					</view>
				</scroll-view>
			</block>
		</view>
	</view>
</template>

<style lang="scss" scoped>
.u-wrap {
	height: calc(100vh);
	/* #ifdef H5 */
	height: calc(100vh - var(--window-top));
	/* #endif */
	display: flex;
	flex-direction: column;
}

.u-search-box {
	padding: 18rpx 30rpx;
}

.u-menu-wrap {
	flex: 1;
	display: flex;
	overflow: hidden;
}

.u-search-inner {
	background-color: rgb(234, 234, 234);
	border-radius: 100rpx;
	display: flex;
	align-items: center;
	padding: 10rpx 16rpx;
}

.u-search-text {
	font-size: 26rpx;
	color: $u-tips-color;
	margin-left: 10rpx;
}

.u-tab-view {
	width: 200rpx;
	height: 100%;
}

.u-tab-item {
	height: 110rpx;
	background: #f6f6f6;
	box-sizing: border-box;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 26rpx;
	color: #444;
	font-weight: 400;
	line-height: 1;
}

.u-tab-item-active {
	position: relative;
	color: #000;
	font-size: 30rpx;
	font-weight: 600;
	background: #fff;
}

.u-tab-item-active::before {
	content: "";
	position: absolute;
	border-left: 4px solid $u-primary;
	height: 32rpx;
	left: 0;
	top: 39rpx;
}

.u-tab-view {
	height: 100%;
}

.right-box {
	background-color: rgb(250, 250, 250);
}

.page-view {
	padding: 16rpx;
}

.class-item {
	margin-bottom: 30rpx;
	background-color: #fff;
	padding: 16rpx;
	border-radius: 8rpx;
}

.item-title {
	font-size: 26rpx;
	color: $u-main-color;
	font-weight: bold;
}

.item-menu-name {
	font-weight: normal;
	font-size: 24rpx;
	color: $u-main-color;
}

.item-container {
	display: flex;
	flex-wrap: wrap;
}

// 定义项目尺寸变量
$item-size: 100rpx;

.thumb-box {
	width: 33.333333%;
	display: flex;
	align-items: center;
	justify-content: center;
	flex-direction: column;
	margin-top: 20rpx;
	padding: 10rpx;
	box-sizing: border-box;
	
	.item-menu-image {
		width: $item-size;
		height: $item-size;
	}
	
	.item-menu-name {
		font-weight: normal;
		font-size: 24rpx;
		color: $u-main-color;
		margin-top: 10rpx;
		text-align: center;
	}
}
</style>
