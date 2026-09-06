<template>
    <view class="container">
		<view class="approval">	
			<uni-segmented-control :current="current" :values="approvalList" @clickItem="onClickItem" styleType="button" activeColor="#007AFF"></uni-segmented-control>
			<view class="content">
				<view v-show="current === 0" class="approval_content">
					<my-apply></my-apply>
				</view>
				<view v-show="current === 1" class="approval_content">
					<my-approval></my-approval>
				</view>
				<view v-show="current === 2" class="approval_content">
					<my-rejected></my-rejected>
				</view>
			</view>
		</view>
    </view>
</template>

<script>
	import MyApply from'./myapply.vue'
	import MyApproval from './myapproval.vue'
	import MyRejected from './myrejected.vue'
	export default {
		components:{ MyApproval,  MyApply, MyRejected },
		data() {
			return {
				current:0,
				approvalList:['我的申请','经我审批','已驳回']
			}
		},
		methods: {
			onClickItem(e) {
				this.current = e.currentIndex
			}
			
		}
	}
</script>

<style lang="scss">
	.container{
		width:100%;
		height: 100%;
		.approval{
			padding: 10px 20px;
			margin-bottom: 10px;
			.approval_content{
				margin-top: 10px;				
				border-radius: 4px;
			}
		}
		
		
	}

</style>
