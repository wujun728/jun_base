<template>
	<view class="container">
		<view class="login">
			<view class="logo">
				<image src="../../static/swissmic-logo-black.png"></image>
				<view class="oa_title">
					协同办公 每一天都很高效
				</view>
			</view>
			<view class="login_form">
				<uni-forms :modelValue="formData" :rules="rules" ref="loginForm">
					<uni-forms-item  name="account">
						<uni-easyinput type="text" v-model="formData.account" placeholder="请输入账号" />
					</uni-forms-item>
					<uni-forms-item  name="password">
						<uni-easyinput type="password" v-model="formData.password" placeholder="请输入密码" />
					</uni-forms-item>
						<view>
						    <checkbox-group @change="change">
								<label>
									<checkbox :value="'1'" :checked="checked" @change="checkedChange" style="transform:scale(0.7)" />记住密码
								</label>
							</checkbox-group>
						</view>

						<button type="primary" @click="login" class="login_btn">登录</button>
				</uni-forms>
			</view>
			
			
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				formData:{
					account:'',
					password:''
				},
				rules:{
					account:{
						rules: [{
							required: true,
							errorMessage: '请输入账号',
						}],
					},
					password:{
						rules:[{
							required: true,
							errorMessage: '请输入密码',
						}],
						
					}
				},
				checked:false
			}
		},
		created() {
			let storageAccount = uni.getStorageSync('accountObj')
			let remberPassword = ''			
			if(storageAccount) {
				let remberPassword = JSON.parse(uni.getStorageSync('accountObj'))
			}		
			if(remberPassword){
				this.formData = Object.assign({},remberPassword)
				console.log(this.formData)
				this.checked = true
			}
		},
		methods: {
			login() {
				this.$refs.loginForm.validate().then(res=>{
					// 登录成功再次记住当前密码
					uni.setStorageSync('accountObj',JSON.stringify(this.formData))
					uni.switchTab({
						url:'/pages/home/home',
					});
				}).catch(err =>{
					console.log('表单信息错误')
				})
			},
			change(e) {
				let remberPassword = e.target.value.join()
				if(remberPassword){
					uni.setStorageSync('accountObj',JSON.stringify(this.formData))
				}else{
					uni.removeStorageSync('accountObj');
				}
			},
		}
	}
</script>

<style lang="scss">
	.container {
		.login{
			padding: 20rpx 40rpx;
			.logo{
				text-align: center;
				margin-top: 60rpx;
				image{
					width: 160rpx;
					height: 80rpx;
				}
				.oa_title{
					margin-top: 20rpx;
				}
			}
			
			.login_form{
				margin-top: 50px;
				.login_btn{
					margin-top: 20px;
					height: 36px;
					line-height: 36px;
				}
			}
		}
		
		
	}
</style>
