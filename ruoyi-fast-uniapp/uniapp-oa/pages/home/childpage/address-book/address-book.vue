<template>
	<view class="container">
		<view class="address">
			<uni-indexed-list :options="list" :showSelect="false" @click="bindClick" link></uni-indexed-list>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list:[{
					"letter": "总经办",
					"data": [
						"陈老师",
						"陈圆圆",
					]
				},{
					"letter": "行政部",
					"data": [
						"阿卡丽",
						"阿拉斯加",
						
					]
				}, {
					"letter": "人事部",
					"data": [
						"百合",
						"百度",
					]
				},
				{
					"letter": "生产部",
					"data": [
						"古天乐",
						"古帅",
						"古神"
					]
				},{
					"letter": "研发部",
					"data": [
						"刘涛",
						"刘德华",
						"刘亦菲",
						"刘某人"
					]
				}]
			}
		},
		methods: {
			bindClick(e){
				uni.navigateTo({
					url:'address-detail?key='+ e.item.key+ '&&name='+e.item.name
				})
			}
		}
	}
</script>

<style lang="scss">
	.container{
		width: 100%;
		height: 100%;
		background: #f5f6f7;
		.address{
			padding: 10px 20px;
			background: #ffffff;
			margin-bottom: 10px;
		}
	}
</style>

