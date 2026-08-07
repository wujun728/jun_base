<style scoped>
	.content-box{
		display: flex; 
		justify-content: center; 
		align-items: center;
		background-color: #EEE;
		cursor: pointer;
		height: 500px;
	}
	.tips-box{text-align: center; margin-top: 0px;}
	.xian{width: 500px; height: 2px; margin: 40px auto  30px auto; background-color: #515151;}
	.error-icon{width: 200px; height: 200px;}
	.error-tips{font-size: 20px; font-weight: 700; color: #515151;}
</style>

<template>
	<el-dialog
		:visible.sync="isShow"
		:show-close="false"
		width="600px"
		top="calc(50vh - 250px)"
		custom-class="full-dialog full-h-dialog"
		>
		<div class="content-box" @click="isShow=false">
			<div class="tips-box">
				<p>
					<img class="error-icon" src="./kulian.png" >
				</p>
				<div class="xian"></div>
				<p class="error-tips">
					<span>404 - 没有找到你想要的页面</span>
				</p>
			</div>
		</div>
	</el-dialog>
</template>

<script>
	export default {
		data() {
			return {
				isShow: false
			}
		},
		methods: {
			
		}
	}
</script>

