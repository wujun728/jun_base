<template>
	<view>
		<view class="comment" v-for="(res, index) in commentList" :key="res.id">
			<view class="left">
				<image :src="res.url" mode="aspectFill"></image>
			</view>
			<view class="right">
				<view class="top">
					<view class="name">{{ res.name }}</view>
					<view class="like" :class="{ highlight: res.isLike }">
						<view class="num">{{ res.likeNum }}</view>
						<u-icon v-if="!res.isLike" name="thumb-up" :size="30" color="#9a9a9a"
							@click="getLike(index)"></u-icon>
						<u-icon v-if="res.isLike" name="thumb-up-fill" :size="30" @click="getLike(index)"></u-icon>
					</view>
				</view>
				<view class="content">{{ res.contentText }}</view>
				<view class="reply-box">
					<view class="item" v-for="(item, index) in res.replyList" :key="item.index">
						<view class="username">{{ item.name }}</view>
						<view class="text">{{ item.contentStr }}</view>
					</view>
					<view class="all-reply" @tap="toAllReply" v-if="res.replyList != undefined">
						共{{ res.allReply }}条回复
						<u-icon class="more" name="arrow-right" :size="26"></u-icon>
					</view>
				</view>
				<view class="bottom">
					{{ res.date }}
					<view class="reply">回复</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import tab from '@/plugins/tab';

const commentList = ref([]);

onMounted(() => {
	getComment();
});

const toAllReply = () => tab.navigateTo('/pages_template/pages/comment/reply')

const getLike = (index) => {
	commentList.value[index].isLike = !commentList.value[index].isLike;
	if (commentList.value[index].isLike) {
		commentList.value[index].likeNum++;
	} else {
		commentList.value[index].likeNum--;
	}
};

const getComment = () => {
	commentList.value = [
		{
			id: 1,
			name: '叶轻眉',
			date: '12-25 18:58',
			contentText: '我不信伊朗会没有后续反应，美国肯定会为今天的事情付出代价的',
			url: 'https://cdn.uviewui.com/uview/template/SmilingDog.jpg',
			allReply: 12,
			likeNum: 33,
			isLike: false,
			replyList: [
				{
					name: 'uview',
					contentStr: 'uview是基于uniapp的一个UI框架，代码优美简洁，宇宙超级无敌彩虹旋转好用，用它！'
				},
				{
					name: '粘粘',
					contentStr: '今天吃什么，明天吃什么，晚上吃什么，我只是一只小猫咪为什么要烦恼这么多'
				}
			]
		},
		{
			id: 2,
			name: '叶轻眉1',
			date: '01-25 13:58',
			contentText: '我不信伊朗会没有后续反应，美国肯定会为今天的事情付出代价的',
			allReply: 0,
			likeNum: 11,
			isLike: false,
			url: 'https://cdn.uviewui.com/uview/template/niannian.jpg',
		},
		{
			id: 3,
			name: '叶轻眉2',
			date: '03-25 13:58',
			contentText: '我不信伊朗会没有后续反应，美国肯定会为今天的事情付出代价的',
			allReply: 0,
			likeNum: 21,
			isLike: false,
			allReply: 2,
			url: 'https://uviewui.com/common/logo.png',
			replyList: [
				{
					name: 'uview',
					contentStr: 'uview是基于uniapp的一个UI框架，代码优美简洁，宇宙超级无敌彩虹旋转好用，用它！'
				},
				{
					name: '豆包',
					contentStr: '想吃冰糖葫芦粘豆包，但没钱5555.........'
				}
			]
		},
		{
			id: 4,
			name: '叶轻眉3',
			date: '06-20 13:58',
			contentText: '我不信伊朗会没有后续反应，美国肯定会为今天的事情付出代价的',
			url: 'https://cdn.uviewui.com/uview/template/SmilingDog.jpg',
			allReply: 0,
			likeNum: 150,
			isLike: false
		}
	];
};
</script>

<style lang="scss" scoped>
.comment {
	display: flex;
	padding: 30rpx;
	margin-bottom: 20rpx;
	background-color: #ffffff;
	border-radius: 12rpx;
	box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
	transition: all 0.3s;
	
	&:hover {
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
	}

	.left {
		image {
			width: 72rpx;
			height: 72rpx;
			border-radius: 50%;
			background-color: #f2f2f2;
			border: 2rpx solid #eaeaea;
			box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.1);
		}
	}

	.right {
		flex: 1;
		padding-left: 24rpx;
		font-size: 28rpx;
		line-height: 1.6;

		.top {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 12rpx;

			.name {
				color: #5677fc;
				font-weight: 500;
				font-size: 30rpx;
			}

			.like {
				display: flex;
				align-items: center;
				color: #9a9a9a;
				font-size: 26rpx;
				padding: 4rpx 12rpx;
				border-radius: 30rpx;
				transition: all 0.2s;
				
				&:active {
					background-color: rgba(86, 119, 252, 0.1);
				}

				.num {
					margin-right: 8rpx;
					color: #9a9a9a;
				}
			}

			.highlight {
				color: #5677fc;

				.num {
					color: #5677fc;
				}
			}
		}

		.content {
			margin-bottom: 16rpx;
			color: #333333;
			line-height: 1.8;
		}

		.reply-box {
			background-color: #f7f7f7;
			border-radius: 12rpx;
			margin-top: 12rpx;
			margin-bottom: 8rpx;
			overflow: hidden;

			.item {
				padding: 20rpx;
				border-bottom: solid 1rpx rgba(0, 0, 0, 0.05);

				.username {
					font-size: 26rpx;
					color: #5677fc;
					font-weight: 500;
					margin-bottom: 6rpx;
				}
				
				.text {
					font-size: 28rpx;
					color: #333333;
				}
			}

			.all-reply {
				padding: 20rpx;
				display: flex;
				color: #5677fc;
				align-items: center;
				font-size: 26rpx;
				transition: all 0.2s;
				
				&:active {
					background-color: rgba(86, 119, 252, 0.1);
				}

				.more {
					margin-left: 6rpx;
				}
			}
		}

		.bottom {
			margin-top: 20rpx;
			display: flex;
			font-size: 24rpx;
			color: #9a9a9a;
			align-items: center;

			.reply {
				color: #5677fc;
				margin-left: 16rpx;
				padding: 4rpx 16rpx;
				border-radius: 30rpx;
				transition: all 0.2s;
				
				&:active {
					background-color: rgba(86, 119, 252, 0.1);
				}
			}
		}
	}
}
</style>
