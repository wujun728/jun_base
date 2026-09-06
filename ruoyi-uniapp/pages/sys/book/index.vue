<template>
	<view class="wrap">
		<view class="search">
			<u-search v-model="keywords" @custom="search" @search="search"></u-search>
		</view>
		<u-index-list :scrollTop="scrollTop">
			<view class="personal-listitem" v-for="(items, idx) in indexList" :key="idx" :use-slot="true">
				<u-index-anchor :index="items" />
				<view class="list-cell" v-for="(item, index) in getRandomInt(1,6)" :key="index"
					@click="navTo('/pages/sys/book/personal-details')">
					<image class="user-images" :src="getReandomImage(index)"></image>
					<view class="user-name"><span>{{nameList[index]}}</span><span
							class="user-other">{{deptList[index]}}</span></view>
				</view>
			</view>
		</u-index-list>
		<u-divider>已经到底了</u-divider>
	</view>

</template>
<script>
	import HeadNavBar from '@/components/headnavbar/index';
	/**
	 * Copyright (c) 2013-Now http://aidex.vip All rights reserved.
	 */
	export default {
		components: {
			HeadNavBar
		},
		data() {
			return {
				keywords: '',
				scrollTop: 0,
				indexList: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
					"T", "U",
					"V", "W", "X", "Y", "Z"
				],
				nameList: ['安全', '安静', '白飞', '百威', '白智英', '李红', '博雅'],
				deptList: ['甘肃分公司', '西藏分公司', '海南分公司', '湖南分公司', '深圳分公司', '广州分公司', '济南分公司'],
				imageList: ['user01', 'user02', 'user03', 'user04', 'user05', 'user06'],
			}
		},
		onLoad() {
		},
		onPageScroll(e) {
			this.scrollTop = e.scrollTop;
		},
		methods: {
			getRandomInt(min, max) {
				let data = Math.floor(Math.random() * (max - min + 1)) + min;
				return data;
			},
			getRandomName() {
				const length = this.nameList.length;
				const i = this.getRandomInt(0, length - 1);
				return this.nameList[i];
			},
			getRandomDept() {
				const length = this.deptList.length;
				const i = this.getRandomInt(0, length - 1);
				return this.deptList[i];
			},
			getReandomImage(i) {
				// const length = this.imageList.length;
				// const i = this.getRandomInt(0, length - 1);
				return '/static/aidex/images/' + this.imageList[i] + '.png';
			},
			navTo(url) {
				uni.navigateTo({
					url: url
				});
			},
		}
	};
</script>
<style lang="scss">
	@import 'index.scss';

	page {
		background-color: #f5f5f5;
	}

	.wrap .search {
		background: #ffffff;
	}

	.list-cell {
		display: flex;
		box-sizing: border-box;
		width: 100%;
		padding: 8px 24rpx;
		overflow: hidden;
		color: #323233;
		font-size: 14px;
		line-height: 24px;
		background-color: #fff;
		align-items: center;
		border-bottom: 1px solid #ececec !important;
	}

	.personal-listitem .list-cell:last-of-type {
		border-bottom: 0 !important;
	}

	.user-images {
		width: 80rpx;
		height: 80rpx;
		margin-right: 10px;
	}

	.user-name {
		font-size: 32rpx;
		color: #000000;
		flex: 1;

		span {
			display: block;
		}

		.user-other {
			font-size: 26rpx;
			color: #999999;
			font-weight: normal;
		}
	}
</style>
