<template>
	<view class="approval-bil">
		<view class="approval-bil-list" v-for="(item,index) in listData">
			<view class="approval-bil-list-header">
				<text class="approval-bil-list-header-title">{{uploadFromData.content.title}} {{index+1}}</text>
			</view>
			<check-examine-from :options="item"></check-examine-from>
		</view>
	</view>
</template>

<script setup>
	import { reactive, toRefs, watch } from "vue"
	import checkExamineFrom from "./checkExamineFrom.vue"
	const props = defineProps( {
		uploadFromData: {
			type: Object,
			default () {
				return {}
			}
		}
	} )
	const { uploadFromData } = toRefs( props )
	let listData = reactive( [] )

	// 数据监听
	watch( () => uploadFromData, ( newvalue, oldvalue ) => {
		let arr1 = []
		if ( newvalue.value.content.children.length && newvalue.value.value.length > 0 ) {
			newvalue.value.value.forEach( ( val, index ) => {
				let key = Object.keys( val )
				let values = Object.values( val )
				arr1.push( [] )
				newvalue.value.content.children.forEach( ( value, indexs ) => {
					if ( key.length > 0 ) { // 类型为空判断
						if ( value.field === key[ indexs ] ) {
							arr1[ index ].push( { "content": value, "value": values[ indexs ] } )
						}
					} else {
						arr1[ index ].push( { "content": value, "value": '' } )
					}
				} )
			} )
		}
		listData = arr1
	}, { immediate: true, deep: true } )
</script>

<style scoped lang="scss">
	.approval-bil {
		width: 100%;

		.approval-bil-list-header {
			width: 100%;
			background-color: #F6F7F9;
			height: 66rpx;
			padding: 0 20rpx;
			display: flex;
			align-items: center;

			.approval-bil-list-header-title {
				font-size: 26rpx;
				font-weight: 500;
				color: $nui-text-color-two;
				position: relative;
				padding-left: 16rpx;

				&::after {
					content: '';
					position: absolute;
					left: 0;
					top: 0;
					border-left: 2px solid $uni-color-primary;
					height: 100%;
				}
			}
		}
	}
</style>
