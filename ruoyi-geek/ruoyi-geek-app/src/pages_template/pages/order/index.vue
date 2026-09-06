<script setup>
import { ref, reactive, onMounted } from 'vue';
import OrderItem from './OrderItem.vue';

const orderList = ref([[], [], [], []]);
const dataList = reactive([
	{
		id: 1,
		store: '夏日流星限定贩卖',
		deal: '交易成功',
		goodsList: [
			{
				goodsUrl: '//img13.360buyimg.com/n7/jfs/t1/103005/7/17719/314825/5e8c19faEb7eed50d/5b81ae4b2f7f3bb7.jpg',
				title: '【冬日限定】现货 原创jk制服女2020冬装新款小清新宽松软糯毛衣外套女开衫短款百搭日系甜美风',
				type: '灰色;M',
				deliveryTime: '付款后30天内发货',
				price: '348.58',
				number: 2
			},
			{
				goodsUrl: '//img12.360buyimg.com/n7/jfs/t1/102191/19/9072/330688/5e0af7cfE17698872/c91c00d713bf729a.jpg',
				title: '【葡萄藤】现货 小清新学院风制服格裙百褶裙女短款百搭日系甜美风原创jk制服女2020新款',
				type: '45cm;S',
				deliveryTime: '付款后30天内发货',
				price: '135.00',
				number: 1
			}
		]
	},
	{
		id: 2,
		store: '江南皮革厂',
		deal: '交易失败',
		goodsList: [
			{
				goodsUrl: '//img14.360buyimg.com/n7/jfs/t1/60319/15/6105/406802/5d43f68aE9f00db8c/0affb7ac46c345e2.jpg',
				title: '【冬日限定】现货 原创jk制服女2020冬装新款小清新宽松软糯毛衣外套女开衫短款百搭日系甜美风',
				type: '粉色;M',
				deliveryTime: '付款后7天内发货',
				price: '128.05',
				number: 1
			}
		]
	},
	{
		id: 3,
		store: '三星旗舰店',
		deal: '交易失败',
		goodsList: [
			{
				goodsUrl: '//img11.360buyimg.com/n7/jfs/t1/94448/29/2734/524808/5dd4cc16E990dfb6b/59c256f85a8c3757.jpg',
				title: '三星（SAMSUNG）京品家电 UA65RUF70AJXXZ 65英寸4K超高清 HDR 京东微联 智能语音 教育资源液晶电视机',
				type: '4K，广色域',
				deliveryTime: '保质5年',
				price: '1998',
				number: 3
			},
			{
				goodsUrl: '//img14.360buyimg.com/n7/jfs/t6007/205/4099529191/294869/ae4e6d4f/595dcf19Ndce3227d.jpg!q90.jpg',
				title: '美的(Midea)639升 对开门冰箱 19分钟急速净味 一级能效冷藏双开门杀菌智能家用双变频节能 BCD-639WKPZM(E)',
				type: '容量大，速冻',
				deliveryTime: '保质5年',
				price: '2354',
				number: 1
			}
		]
	},
	{
		id: 4,
		store: '三星旗舰店',
		deal: '交易失败',
		goodsList: [
			{
				goodsUrl: '//img10.360buyimg.com/n7/jfs/t22300/31/1505958241/171936/9e201a89/5b2b12ffNe6dbb594.jpg!q90.jpg',
				title: '法国进口红酒 拉菲（LAFITE）传奇波尔多干红葡萄酒750ml*6整箱装',
				type: '4K，广色域',
				deliveryTime: '珍藏10年好酒',
				price: '1543',
				number: 3
			},
			{
				goodsUrl: '//img10.360buyimg.com/n7/jfs/t1/107598/17/3766/525060/5e143aacE9a94d43c/03573ae60b8bf0ee.jpg',
				title: '蓝妹（BLUE GIRL）酷爽啤酒 清啤 原装进口啤酒 罐装 500ml*9听 整箱装',
				type: '一打',
				deliveryTime: '口感好',
				price: '120',
				number: 1
			}
		]
	},
	{
		id: 5,
		store: '三星旗舰店',
		deal: '交易成功',
		goodsList: [
			{
				goodsUrl: '//img12.360buyimg.com/n7/jfs/t1/52408/35/3554/78293/5d12e9cfEfd118ba1/ba5995e62cbd747f.jpg!q90.jpg',
				title: '企业微信 中控人脸指纹识别考勤机刷脸机 无线签到异地多店打卡机WX108',
				type: '识别效率高',
				deliveryTime: '使用方便',
				price: '451',
				number: 9
			}
		]
	}
]);
const list = reactive([
	{ name: '待付款' },
	{ name: '待发货' },
	{ name: '待收货' },
	{ name: '待评价', count: 12 }
]);
const current = ref(0);
const swiperCurrent = ref(0);
const loadStatus = ref(['loadmore', 'loadmore', 'loadmore', 'loadmore']);

onMounted(() => {
	getOrderList(0);
	getOrderList(1);
	getOrderList(3);
});

// 触底懒加载
const reachBottom = () => {
	loadStatus.value.splice(current.value, 1, "loading");
	setTimeout(() => {
		getOrderList(current.value);
	}, 1200);
};

// 页面数据
const getOrderList = (idx) => {
	// 模拟第n个页面获取数据
	for (let i = 0; i < 5; i++) {
		let index = Math.floor(Math.random() * dataList.length);
		let data = JSON.parse(JSON.stringify(dataList[index]));
		data.id = Math.random().toString(36).substr(2, 9);
		orderList.value[idx].push(data);
	}
	loadStatus.value.splice(current.value, 1, "loadmore");
};

// tab栏切换
const change = ({ index }) => {
	current.value = index; // 更新current变量
	swiperCurrent.value = index;
	getOrderList(index);
};

const animationfinish = (e) => {
	const currentIndex = e.detail.current;
	swiperCurrent.value = currentIndex;
	current.value = currentIndex; // 将current的值正确更新为swiper的current
};
</script>

<template>
	<view>
		<view class="wrap">
			<view class="u-tabs-box">
				<u-tabs activeColor="#f29100" ref="tabs" :list="list" :current="current" @change="change"
					:is-scroll="false" swiperWidth="750"></u-tabs>
			</view>
			<swiper class="swiper-box" :current="swiperCurrent" @animationfinish="animationfinish">
				<swiper-item class="swiper-item" v-for="(orderlist, index) in orderList" :key="index">
					<scroll-view scroll-y style="height: 100%;width: 100%;" @scrolltolower="reachBottom"
						v-if="orderlist.length !== 0">
						<view class="page-box">
							<OrderItem v-for="res in orderlist" :key="res.id" :order="res" />
							<u-loadmore :status="loadStatus[0]" bgColor="#f2f2f2"></u-loadmore>
						</view>
					</scroll-view>
					<scroll-view scroll-y style="height: 100%;width: 100%;" v-else>
						<view class="page-box">
							<view class="centre">
								<image src="https://cdn.uviewui.com/uview/template/taobao-order.png" mode="aspectFit" class="empty-image">
								</image>
								<view class="explain">
									您还没有相关的订单
									<view class="tips">可以去看看有那些想买的</view>
								</view>
								<view class="btn">随便逛逛</view>
							</view>
						</view>
					</scroll-view>
				</swiper-item>
			</swiper>
		</view>
	</view>
</template>
<style>
/* #ifndef H5 */
page {
	height: 100%;
	background-color: #f2f2f2;
}

/* #endif */
</style>

<style lang="scss" scoped>
.centre {
	text-align: center;
	margin: 200rpx auto;
	font-size: 32rpx;
	width: 100%;

	.empty-image {
		width: 164rpx;
		height: 164rpx;
		border-radius: 50%;
		margin: 0 auto 20rpx;
		display: block; /* 确保图片作为块级元素 */
	}

	.tips {
		font-size: 24rpx;
		color: #999999;
		margin-top: 20rpx;
	}

	.btn {
		margin: 80rpx auto;
		width: 200rpx;
		border-radius: 32rpx;
		line-height: 64rpx;
		color: #ffffff;
		font-size: 26rpx;
		background: linear-gradient(270deg, rgba(249, 116, 90, 1) 0%, rgba(255, 158, 1, 1) 100%);
	}
}

.wrap {
	display: flex;
	flex-direction: column;
	height: calc(100vh - var(--window-top));
	width: 100%;
}

.swiper-box {
	flex: 1;
}

.swiper-item {
	height: 100%;
}
</style>
