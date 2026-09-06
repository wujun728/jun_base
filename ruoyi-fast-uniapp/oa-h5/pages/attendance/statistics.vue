<template>
	<view class="nav">
		<uni-nav-bar :fixed="true" left-icon="left" status-bar backgroundColor="#308BF8" color="#fff" :border="false"
			@clickLeft="cancel">
			<view class="nav-content" v-if="!isUser">
				<view class="title" :class="{ on: index == sel }" v-for="(item, index) in tabData" :key="index"
					@click="selIndex(index)">{{ item.name }}</view>
			</view>
			<view class="nav-content" v-if="isUser">
				<view class="title">{{ pageTitle }}</view>
			</view>
		</uni-nav-bar>
		<!-- #ifdef APP-PLUS -->
		<team v-show="sel == 0"></team>
		<user v-show="sel == 1"></user>
		<!-- #endif -->
		<!-- #ifndef APP-PLUS -->
		<team v-if="sel == 0"></team>
		<user v-if="sel == 1"></user>
		<!-- #endif -->

		<bottom-navigation :type="4" page-path="/pages/attendance/statistics"></bottom-navigation>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		toRefs,
		onMounted
	} from "vue";
	import defaultNavBar from "@/components/defaultNavBar/index";
	import bottomNavigation from "@/components/bottomNavigation/index.vue";
	import team from "./components/teamAttendance.vue";
	import user from "./components/userAttendance.vue";
	const tabData = reactive([{
			name: "团队统计",
			type: 0,
		},
		{
			name: "我的统计",
			type: 1,
		},
	]);
	const sel = ref(0);
	const isUser = ref(0);
	const pageTitle = ref();
	onLoad((options) => {
		if (options.sel) {
			sel.value = options.sel;
		}
		if (options.is_user) {
			isUser.value = options.is_user;
			sel.value = 1;
			pageTitle.value = `${options.user_name}的打卡记录` || "";
			uni.setNavigationBarTitle({
				title: pageTitle.value,
			});
		}
	});

	function selIndex(index) {
		sel.value = index;
	}
	const cancel = () => {
		uni.switchTab({
			url: '/pages/index/index'
		})
	};
</script>

<style lang="scss" scoped>
	.nav-content {
		display: flex;
		justify-content: space-around;
		align-items: center;
		width: 100%;

		.title {
			font-size: 34rpx;
			font-weight: 500;
			color: rgba(255, 255, 255, 0.6);
			line-height: 34rpx;
		}

		.on {
			font-size: 34rpx;
			font-weight: 500;
			color: #ffffff;
			line-height: 34rpx;
		}
	}
</style>