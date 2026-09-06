<template>
	<view class="content" v-if="data.listData.id">
		<view class="cr-position-header">
			<!-- #ifdef APP-PLUS -->
			<view class="status_bar"></view>
			<view class="position bar">
				<default-nav-bar></default-nav-bar>
			</view>
			<image class="personal-bg" src="/static/image/personal-bg.png" mode=""></image>
			<!-- #endif -->

			<!-- #ifndef APP-PLUS -->
			<image class="personal-bg" src="/static/image/personal-bg-h5.png" mode=""></image>
			<!-- #endif -->

			<view class="position personal-header plr10">
				<uni-row class="display-align">
					<uni-col :span="6" class="personal-header-left">
						<avatar :src="data.listData.avatar ? data.listData.avatar : '/static/image/default-avatar.png'" :radius="8"></avatar>
					</uni-col>
					<uni-col :span="18" class="personal-header-right">
						<view class="name line1">{{data.listData.name ? data.listData.name : ''}}</view>
						<view class="job line1">{{data.listData.job ? data.listData.job.name : '--'}}</view>
					</uni-col>
				</uni-row>
				<view class="company-name default-color line1">{{enterprise.enterprise_name}}</view>
			</view>

		</view>
		<view class="assessment m10">
			<view class="cr-center-list">
				<view class="center-list-item">
					<uni-row class="center-list-item-con">
						<uni-col :span="6">手机号</uni-col>
						<uni-col :span="18" class="text-right line1">{{data.listData.phone}}</uni-col>
					</uni-row>
					<uni-row class="center-list-item-con">
						<uni-col :span="6">邮箱</uni-col>
						<uni-col :span="18" class="text-right line1">{{data.listData.info ? data.listData.info.email : '--'}}</uni-col>
					</uni-row>
					<template v-if="data.listData.frames">
						<uni-row class="center-list-item-con" v-for="(item,index) in data.listData.frames" :key="index">
							<uni-col :span="6">{{index===0 ? '部门':''}}</uni-col>
							<uni-col :span="18" class="display-align right">
								<view class="title">{{ item.name}}</view>
							</uni-col>
						</uni-row>
					</template>
					<template v-else>
						<uni-row class="center-list-item-con">
							<uni-col :span="6">部门</uni-col>
							<uni-col :span="18" class="display-align right">
								<view class="title">--</view>
							</uni-col>
						</uni-row>
					</template>

				</view>
			</view>
		</view>
		<global-index></global-index>
	</view>
</template>

<script setup lang="ts">
	import defaultNavBar from '@/components/defaultNavBar/index'
	import globalIndex from '@/components/globalIndex/index.vue'
	import avatar from '@/components/avatar/index.vue'
	import message from '@/utils/message'
	import { enterpriseUserInfoApi } from '@/api/user'
	import type { GetType, Res } from '@/utils/typeHelper'
	import { useStore } from 'vuex'
	const store = useStore();

	const enterprise = computed(() => store.state.app.enterprise)

	onLoad((e : GetType) => {
		if (e.id) {
			getUserInfo(e.id)
		}
	})

	const data = reactive({
		title: '',
		listData: <any>{}
	})

	// 获取个人信息
	const getUserInfo = (id : number) : void => {
		enterpriseUserInfoApi(id).then((res : Res) => {
			data.listData = res.data
			if (data.listData.frames && data.listData.frames.length > 1) {
				data.listData.frames.sort((a, b) => { return b.is_mastart - a.is_mastart })
			}
		}).catch((error : Res) => {
			message.error(error.message)
		})
	}
</script>

<style scoped lang="scss">
	@import '@/static/css/form-item-list.scss';

	.content {
		width: 100%;

		.cr-position-header {
			background-color: #fff;
			position: sticky;
			// #ifndef APP-PLUS
			height: 216rpx;
			// #endif
			// #ifdef APP-PLUS
			height: 392rpx;
			// #endif


			.position {
				width: 100%;
				position: absolute;
				left: 0;
			}

			.personal-bg {
				position: absolute;
				right: 0;
				top: 0;
				bottom: 0;
				// #ifndef APP-PLUS
				width: 176rpx;
				// #endif
				// #ifdef APP-PLUS
				width: 322rpx;
				// #endif
				height: 100%;
			}

			.bar {
				top: 44px;
			}

			.personal-header {
				// #ifndef APP-PLUS
				top: 30rpx;
				// #endif
				// #ifdef APP-PLUS
				top: calc(88px + 30rpx);
				// #endif

				.personal-header-left {
					width: 100rpx;
					height: 100rpx;
				}

				.personal-header-right {
					padding-left: 26rpx !important;
					width: calc(100% - 100rpx);

					.name {
						font-size: 32rpx;
						font-weight: $uni-default-font-weight;
						color: #2B2C32;
					}

					.job {
						padding-top: 8rpx;
						font-size: 26rpx;
						color: #888888;
					}
				}

				.company-name {
					padding-top: 30rpx;
					font-size: 24rpx;
					font-weight: 400;
				}
			}
		}

		.assessment {}
	}
</style>