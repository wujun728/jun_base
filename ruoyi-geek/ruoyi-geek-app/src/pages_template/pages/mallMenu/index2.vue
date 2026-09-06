<script setup>
import { ref, onMounted, nextTick } from 'vue';
import classifyData from '@/pages_template/static/js/classify.data.js';

const scrollTop = ref(0); // tab标题的滚动条位置
const oldScrollTop = ref(0);
const current = ref(0); // 预设当前项的值
const menuHeight = ref(0); // 左边菜单的高度
const menuItemHeight = ref(0); // 左边菜单item的高度
const itemId = ref(''); // 栏目右边scroll-view用于滚动的id
const tabbar = classifyData;

const arr = ref([]);
const scrollRightTop = ref(0); // 右边栏目scroll-view的滚动条高度
const timer = ref(null); // 定时器

onMounted(() => {
	getMenuItemTop();
});

// 点击左边的栏目切换
const swichMenu = async (index) => {
	if (arr.value.length === 0) {
		await getMenuItemTop();
	}
	if (index === current.value) return;
	scrollRightTop.value = oldScrollTop.value;
	await nextTick(() => {
		scrollRightTop.value = arr.value[index];
		current.value = index;
		leftMenuStatus(index);
	});
};

// 获取一个目标元素的高度
const getElRect = (elClass, dataVal) => {
	return new Promise((resolve, reject) => {
		const query = uni.createSelectorQuery().in(this);
		query.select('.' + elClass).fields({
			size: true
		}, (res) => {
			// 如果节点尚未生成，res值为null，循环调用执行
			if (!res) {
				setTimeout(() => {
					getElRect(elClass, dataVal);
				}, 10);
				return;
			}
			dataVal.value = res.height;
			resolve();
		}).exec();
	});
};

// 观测元素相交状态
const observer = () => {
	tabbar.map((val, index) => {
		let observer = uni.createIntersectionObserver(this);
		// 检测右边scroll-view的id为itemxx的元素与right-box的相交状态
		// 如果跟.right-box底部相交，就动态设置左边栏目的活动状态
		observer.relativeTo('.right-box', {
			top: 0
		}).observe('#item' + index, (res) => {
			if (res.intersectionRatio > 0) {
				let id = res.id.substring(4);
				leftMenuStatus(id);
			}
		});
	});
};

// 设置左边菜单的滚动状态
const leftMenuStatus = async (index) => {
	current.value = index;
	// 如果为0，意味着尚未初始化
	if (menuHeight.value === 0 || menuItemHeight.value === 0) {
		await getElRect('menu-scroll-view', menuHeight);
		await getElRect('u-tab-item', menuItemHeight);
	}
	// 将菜单活动item垂直居中
	scrollTop.value = index * menuItemHeight.value + menuItemHeight.value / 2 - menuHeight.value / 2;
};

// 获取右边菜单每个item到顶部的距离
const getMenuItemTop = () => {
	return new Promise((resolve) => {
		let selectorQuery = uni.createSelectorQuery();
		selectorQuery.selectAll('.class-item').boundingClientRect((rects) => {
			// 如果节点尚未生成，rects值为[](因为用selectAll，所以返回的是数组)，循环调用执行
			if (!rects.length) {
				setTimeout(() => {
					getMenuItemTop();
				}, 10);
				return;
			}
			rects.forEach((rect) => {
				// 这里减去rects[0].top，是因为第一项顶部可能不是贴到导航栏(比如有个搜索框的情况)
				arr.value.push(rect.top - rects[0].top);
				resolve();
			});
		}).exec();
	});
};

// 右边菜单滚动
const rightScroll = async (e) => {
	oldScrollTop.value = e.detail.scrollTop;
	if (arr.value.length === 0) {
		await getMenuItemTop();
	}
	if (timer.value) return;
	if (!menuHeight.value) {
		await getElRect('menu-scroll-view', menuHeight);
	}
	setTimeout(() => { // 节流
		timer.value = null;
		// scrollHeight为右边菜单垂直中点位置
		let scrollHeight = e.detail.scrollTop + menuHeight.value / 2;
		for (let i = 0; i < arr.value.length; i++) {
			let height1 = arr.value[i];
			let height2 = arr.value[i + 1];
			// 如果不存在height2，意味着数据循环已经到了最后一个，设置左边菜单为最后一项即可
			if (!height2 || scrollHeight >= height1 && scrollHeight < height2) {
				leftMenuStatus(i);
				return;
			}
		}
	}, 10);
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
				<text class="u-search-text">搜索</text>
			</view>
		</view>
		<view class="u-menu-wrap">
			<scroll-view scroll-y scroll-with-animation class="u-tab-view menu-scroll-view" :scroll-top="scrollTop"
				:scroll-into-view="itemId">
				<view v-for="(item, index) in tabbar" :key="index" class="u-tab-item"
					:class="[current == index ? 'u-tab-item-active' : '']" @tap.stop="swichMenu(index)">
					<text class="u-line-1">{{ item.name }}</text>
				</view>
			</scroll-view>
			<scroll-view :scroll-top="scrollRightTop" scroll-y scroll-with-animation class="right-box"
				@scroll="rightScroll">
				<view class="page-view">
					<view class="class-item" :id="'item' + index" v-for="(item, index) in tabbar" :key="index">
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

.class-item:last-child {
	min-height: 100vh;
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
	
	// 使图片容器的宽高都基于item-size变量
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

.item-menu-image {
	width: 120rpx;
	height: 120rpx;
}
</style>
