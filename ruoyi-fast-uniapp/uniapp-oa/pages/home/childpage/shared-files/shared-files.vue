<template>
	<view class="container">
		<view class="shared-files">
			<uni-file-picker
			    v-model="imageValue" 
			    fileMediatype="image" 
			    mode="grid" 
			    @select="select" 
			    @progress="progress" 
			    @success="success" 
			    @fail="fail" 
			/>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				imageValue:[]
			}
		},
		methods: {
			// 获取上传状态
			select(e){
				console.log('选择文件：',e)
			},
			// 获取上传进度
			progress(e){
				console.log('上传进度：',e)
			},

			// 上传成功
			success(e){
				console.log('上传成功')
			},

			// 上传失败
			fail(e){
				console.log('上传失败：',e)
			}
		}
	}
</script>

<style lang="scss">
	.shared-files{
		padding: 20rpx 40rpx;
	}
</style>
