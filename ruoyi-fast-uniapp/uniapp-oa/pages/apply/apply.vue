<template>
	<view class="container">
		<view class="apply">
			<apply-group :groupInfo="kaoqinInfo"></apply-group>
			<apply-group :groupInfo="paymentInfo"></apply-group>
			<apply-group :groupInfo="administrativeInfo"></apply-group>
		</view>
	</view>
</template>

<script>
	import ApplyGroup from './applygroup.vue'
	export default {
		components:{ ApplyGroup },
		data() {
			return {
				kaoqinInfo:{
					groupName:'考勤申请',
					groupList:[{
						icon:'../../static/qingjia.png',
						name:'请假',
						route:'ask-leave',
						id:'101'
					},{
						icon:'../../static/jiaban.png',
						name:'加班',
						route:'extra-work',
						id:'102'
					},{
						icon:'../../static/waichu.png',
						name:'外出',
						route:'go-out',
						id:'103'
					}]
				},
				paymentInfo:{
					groupName:'费用报销',
					groupList:[
						{
							icon:'../../static/chailv.png',
							name:'差旅',
							route:'business-travel',
							id:'111'
						},{
							icon:'../../static/chizhu.png',
							name:'吃住',
							id:'112'
						},{
							icon:'../../static/jie.png',
							name:'借款',
							id:'113'
						},{
							icon:'../../static/fukuan.png',
							name:'预付款',
							id:'114'
						}
					]
				},
				administrativeInfo:{
					groupName:'综合行政',
					groupList:[
						{
							icon:'../../static/bangong.png',
							name:'办公用品',
							id:'121'
						},{
							icon:'../../static/wuliao.png',
							name:'物料',
							id:'122'
						},{
							icon:'../../static/gongzhang.png',
							name:'公章',
							id:'123'
						},{
							icon:'../../static/zhizhao.png',
							name:'执照',
							id:'124'
						},{
							icon:'../../static/car.png',
							name:'车辆',
							id:'125'
						}
					]
				}
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="scss">
</style>
