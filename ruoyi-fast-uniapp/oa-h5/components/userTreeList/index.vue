<template>
	<view class="check-per">
		<view class="pl10">
			<scroll-view class="scroll-per" scroll-y :scroll-top="0">
				<view class="uni-indexed-list__item-content pr10" v-for="(item,index) in treeData" :key="item.id">
					<!-- 选择用户 -->
					<view class="display-align user-list" v-for="(items,indexs) in item.user" :key="'key'+indexs" @click="userClick(items, indexs)">
						<view v-if="showSelect" style="margin-right: 20rpx;">
							<uni-icons :type="checkedArr.includes(items.id) ? 'checkbox-filled' : 'circle'" :color="checkedArr.includes(items.id) ? '#1890FF' : '#C0C0C0'"
								size="24" />
						</view>
						<image class="avatar" :src="items.card.avatar" mode=""></image>
						<view class="item-content-info">
							<text class="text">{{ items.card.name }}</text>
							<text class="caption">董事长</text>
						</view>
					</view>
					<!-- 选择部门 -->
					<view v-if="item.children" class="user-list" v-for="(items,indexs) in item.children" :key="'dep'+indexs" @click="depClick(items,index)">
						<uni-row class="display-align">
							<uni-col :span="20" class="display-align">
								<view v-if="showSelect" style="margin-right: 20rpx;">
									<uni-icons :type="item.checked ? 'checkbox-filled' : 'circle'" :color="item.checked ? '#1890FF' : '#C0C0C0'" size="24" />
								</view>
								<view class="avatar dep-icon" mode="">
									<text class="iconfont icon-zuzhijiagou"></text>
								</view>
								<view class="item-content-info">
									<text class="text">{{ items.label }}</text>
								</view>
							</uni-col>
							<uni-col :span="4" class="text-right">
								<uni-icons type="right dep-right-icon"></uni-icons>
							</uni-col>
						</uni-row>
					</view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<script setup>
	import { ref, toRefs, reactive } from 'vue'

	const props = defineProps( {
		treeData: {
			type: Object,
			default: () => {
				return {}
			}
		},
		// 是否显示选择按钮
		showSelect: {
			type: Boolean,
			default: false
		},
	} )
	const { treeData, showSelect } = toRefs( props )

	let checkedArr = ref( [] )

	let userList = reactive( {
		isUser: 1,
		user: [],
		dep: []
	} )

	let emit = defineEmits( [ "handleDep" ] );

	// 点击选中人员
	const userClick = ( item, index ) => {
		let len = checkedArr.value.indexOf( item.id );
		if ( len > -1 ) {
			checkedArr.value.splice( len, 1 )
			const index = userList.user.findIndex( ( items ) => items.id === item.id )
			if ( index > -1 ) {
				userList.user.splice( index, 1 )
			}
		} else {
			userList.user.push( item )
			checkedArr.value.push( item.id )
		}
		userList.isUser = 1
		emit( 'handleDep', userList )
	}

	// 点击选中部门
	const depClick = ( item, index ) => {
		userList.isUser = 2
		userList.index = index
		userList.id = item.id
		userList.dep = item
		emit( 'handleDep', userList )
	}
</script>

<style lang="scss" scoped>
	.check-per {
		padding-top: 30rpx;

		.scroll-per {
			height: calc(70vh - 44px);
		}

		.uni-indexed-list__item-content {
			.user-list {
				padding-bottom: 30rpx;
			}

			.avatar {
				height: 80rpx;
				width: 80rpx;
				border-radius: 8rpx;
			}

			.dep-icon {
				background: linear-gradient(203deg, rgba(66, 172, 249, 0.15) 0%, rgba(44, 132, 247, 0.15) 100%);
				text-align: center;
				line-height: 80rpx;

				uni-text {
					font-size: 60rpx;
					color: #318CF8;
				}
			}

			.item-content-info {
				padding-left: 20rpx;

				uni-text {
					display: block;
				}

				.text {
					font-size: 28rpx;
					color: #2B2C32;
				}

				.caption {
					padding-top: 10rpx;
					font-size: 24rpx;
					color: #909399;
				}
			}

			.dep-right-icon {
				color: #ccc !important;
			}

			.item-button {
				background-color: #F0F1F5;
				color: #909399;

				&::after {
					border: none;
				}
			}

		}

	}
</style>
