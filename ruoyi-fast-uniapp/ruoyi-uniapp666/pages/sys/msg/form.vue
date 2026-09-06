<template>
	<view class="wrap">
		<view class="article-title">AiDex Sharp快速开发平台</view>
		<view class="article-meta">
			系统管理员 发起于 2021-4-20 12:30</text>
		</view>
		<view class="article-content">
			<p>
			AiDex Sharp快速开发平台：基于若依-ruoyi-vue项目扩展，前端采用Ant-Design-VUE，代码易读易懂、界面简洁美观，不仅仅是一个后台开发框架，它是一个企业级快速开发解决方案，我们将把UI交互、快速开发能力追求到极致，适配国产数据库，国产中间件，将支持多租户、flowable工作流，移动APP，更多插件正在扩展中。
			</p>
			<div class="banner-pic">
				<image src="../../../static/aidex/images/new-pic.png"></image>
			</div>
			<p>
			aidex 快速开发平台的主要目的是能够让初级的研发人员快速的开发出复杂的业务功能（经典架构会的人多），让开发者注重专注业务，其余有平台来封装技术细节，降低技术难度，从而节省人力成本，缩短项目周期，提高软件安全质量。
			</p>
			<p>
			aidex 自 2013 年发布以来已被广大爱好者用到了企业、政府、医疗、金融、互联网等各个领域中，aidex 架构精良、易于扩展、大众思维的设计模式、工匠精神打磨每一个细节，深入开发者的内心，并荣获开源中国《最受欢迎中国开源软件》奖杯，期间也帮助了不少刚毕业的大学生，教师作为入门教材，快速的去实践。
			</p>
			<p>
			aidex 的升级，作者结合了多年总结和经验，以及各方面的应用案例，对架构完成了一次全部重构，也纳入很多新的思想。不管是从开发者模式、底层架构、逻辑处理还是到用户界面，用户交互体验上都有很大的进步，在不忘学习成本、提高开发效率的情况下，安全方面也做和很多工作，包括：身份认证、密码策略、安全审计、日志收集等众多安全选项供你选择。努力为大中小微企业打造全方位企业级快速开发解决方案。
			</p>`
		</view>
		<view class="article-foot"><u-icon name="eye" size="34" color="" label="30"></u-icon> <u-icon name="thumb-up" size="34" color="" label="15"></u-icon></view>
	</view>
</template>
<script>
/**
 * Copyright (c) 2013-Now http://aidex.vip All rights reserved.
 */
export default {
	data() {
		return {
			
		};
	},
	onLoad() {
		
	},
	methods: {
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
	background-color: #ffffff;
}
.article-title {
	font-size: 40rpx;
	font-weight: 400;
	text-align: left;
	padding-bottom: 10rpx;
	font-weight: bold;
	margin: 30rpx 30rpx 0;
	color: #333333;
}
.article-meta {
	padding: 10rpx 30rpx 30rpx;
	color: #999999;
	border-bottom: 1px solid #ededed;
}
.article-content {
	padding: 30rpx 30rpx 0rpx;
	overflow: hidden;
	font-size: 30rpx;
	line-height: 50rpx;
	/deep/ p {
		margin-bottom: 20rpx;
		text-indent: 60rpx;
	}
	.banner-pic{
		margin: 10px auto;
		text-align: center;
		uni-image{
			width: 300px;
			height: 160px;
			box-shadow:0 3px 10px rgba(0,0,0,0.15);
			
		}
	}
}
.article-foot{
	padding:0 30rpx 20rpx;
	font-size: 26rpx;
	color: #999999;
	.u-icon{
		margin-right: 10px;
	}
}


</style>
