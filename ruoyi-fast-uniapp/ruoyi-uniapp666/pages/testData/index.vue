<template>
	<view class="wrap">
		<view class="search">
			<u-search v-model="keywords" @custom="search" @search="search"></u-search>
		</view>
		<scroll-view class="scroll-list" scroll-y="true" @scrolltolower="loadMore">
			<u-cell-group class="list" :border="false">
				<u-swipe-action :options="options" v-for="(item, index) in list" :key="item.id" :index="index" @click="optionsClick">
					<u-cell-item :arrow="true" @click="navTo('form?id='+item.id)">
						<text slot="title">{{item.testInput || item.id}}</text>
						<text slot="label">创建者：{{item.createBy}} &nbsp;|&nbsp; 时间：{{item.createDate}}</text>
					</u-cell-item>
				</u-swipe-action>
			</u-cell-group>
			<view class="loadmore" @click="loadMore">
				<u-loadmore :status="loadStatus"></u-loadmore>
			</view>
		</scroll-view>
		<view class="btn-plus" @click="navTo('form')">
			<u-icon name="plus-circle-fill" size="90" color="#3d87ff"></u-icon>
		</view>
	</view>
</template>
<script>
/**
 * Copyright (c) 2013-Now http://aidex.vip All rights reserved.
 */
export default {
	data() {
		return {
			keywords: '',
			query: {
				pageNo: 1,
				pageSize: 20
			},
			list: [],
			count: 0,
			loadStatus: 'loadmore',
			options: [
				{text: '删除', style: { background: '#dd524d'}}
			]
		};
	},
	onLoad() {
		this.loadList();
	},
	methods: {
		loadMore() {
			this.loadStatus = "loading";
			setTimeout(() => {
				this.query.pageNo += 1;
				this.loadList();
			}, 100);
		},
		loadList() {
			this.$u.api.testData.list(this.query).then(res => {
				if (!res.list || res.list.length == 0){
					this.loadStatus = "nomore";
					return;
				}
				this.list = this.list.concat(res.list);
				this.count = res.count;
				this.query.pageNo = res.pageNo;
				this.query.pageSize = res.pageSize;
				this.loadStatus = "loadmore";
			});
		},
		optionsClick(rowIndex, btnIndex) {
			if(btnIndex == 0) {
				let self = this;
				uni.showModal({
					title: '提示',
					content: '确认要删除该数据吗？',
					showCancel: true,
					success: function (res2) {
						if (res2.confirm) {
							let row = self.list[rowIndex];
							self.$u.api.testData.delete({id: row.id}).then(res => {
								self.$u.toast(res.message);
								if (res.result == 'true'){
									self.list.splice(rowIndex, 1);
								}
							});
						}
					}
				});
			}
		},
		search(value) {
			this.list = [];
			this.query.pageNo = 0;
			this.query.testInput = value;
			this.loadList();
		},
		navTo(url) {
			uni.navigateTo({
				url: url
			});
		}
	}
};
</script>
<style lang="scss">
page {
	background-color: #f5f5f5;
}
.btn-plus {
	position: absolute;
	bottom: 50rpx;
	right: 50rpx;
	z-index: 1;
	opacity: 0.6;
}
.btn-plus:hover {
	opacity: 1;
}
</style>
