<template>
	<view class="approval-bill">
		<view class="approval-bill-list" v-for="(item,index) in data.billData" :key="'bill'+index">
			<uni-row class="approval-bill-list-header">
				<uni-col :span="12" class="">{{configData.title}} {{ data.billData.length === 1 ? '' : (index+1)}}</uni-col>
				<uni-col :span="12" class="text-right">
					<text v-if="index > 0" @click="deleteApprovalBill(index)" class="default-color">删除</text>
				</uni-col>
			</uni-row>
			<view class="approval-bill-list-con">
				<examine-form :configData="item" @change="changeExamineForm($event,index)" :key="'bill'+index"></examine-form>
				<view class="approval-bill-list-add" v-if="index === 0">
					<view class="display-center" @click="addApprovalBill">
						<text class="iconfont icon-tianjia1"></text>
						<text class="name">添加{{configData.title}}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import { ref, reactive, toRefs, watch } from 'vue'
	import message from '@/utils/message'
	import examineForm from '../index.vue'

	const props = defineProps( {
		configData: {
			type: Object,
			default: () => {
				return {}
			},
		}
	} )
	const { configData } = toRefs( props )

	const examineFormRef1 = ref( null )
	const data = reactive( {
		billData: [ {} ],
		newBillData: [ {} ],
		defaultData: {}
	} )

	let emit = defineEmits( [ 'change' ] )

	const changeBill = () => {
		emit( 'change', data.newBillData )
	}

	const addApprovalBill = () => {
		let res = JSON.parse( JSON.stringify( data.defaultData ) )
		res.forEach( val => {
			val.newvalue = ''
		} )
		data.billData.push( res )
		data.newBillData.push( getObjectData( res ) )
		changeBill()
	}

	const deleteApprovalBill = ( index ) => {
		data.billData.splice( index, 1 )
		data.newBillData.splice( index, 1 )
		changeBill()
	}

	const changeExamineForm = ( e, index ) => {
		let res = getObjectData( e )
		data.billData[ index ] = e
		data.newBillData[ index ] = res
		changeBill()
	}

	const getObjectData = ( datas = [] ) => {
		let obj = {}
		datas.forEach( val => {
			obj[ val.field ] = val.newvalue
		} )
		return obj
	}

	// 数据监听
	watch( configData, ( value, oldvalue ) => {
		if ( value ) {
			data.defaultData = value.children
			data.billData[ 0 ] = value.children
			data.newBillData[ 0 ] = getObjectData( value.children )
			changeBill()
		}
	}, { immediate: true } );
</script>

<style lang="scss" scoped>
	.approval-bill {
		width: 100%;

		.approval-bill-list {
			margin-bottom: 20rpx;

			.approval-bill-list-header {
				font-size: 28rpx;
				color: $nui-text-color-two;
			}

			.approval-bill-list-con {
				margin-top: 24rpx;
				background-color: #fff;
				border-radius: 12rpx;

				::v-deep .uni-forms-item {
					padding: 26rpx 0;
					margin: 0 24rpx;
					border-radius: 0;
					margin-bottom: 0 !important;
					border-bottom: 1px solid #EBEEF5;

					&:last-of-type {
						border-bottom: none;
					}
				}

				::v-deep .upload-from-list {
					padding-bottom: 0;
				}

				.approval-bill-list-add {
					padding: 38rpx 0;
					border-top: 1px solid #EBEEF5;

					.iconfont {
						font-size: 40rpx;
						color: $uni-color-primary;
					}

					.name {
						padding-left: 10rpx;
						font-size: 30rpx;
						color: $uni-color-primary;
					}
				}
			}
		}

	}
</style>
