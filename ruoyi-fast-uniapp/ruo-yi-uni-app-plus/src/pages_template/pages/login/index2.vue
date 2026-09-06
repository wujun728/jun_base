<template>
	<view>
		<view class="normal-login-container " v-if="page == 'login'">
			<view class="left" @click="back">
				<image src="../../../static/uview/demo/backTop.png" mode="" style="height: 30rpx;"></image>
			</view>
			<view class="scale-in-center">
				<view class="logo-content align-center justify-center flex">
					<text class="title">工作人员入口</text>
				</view>
				<view class="login-form-content">
					<view class="input-item flex align-center">
						<view class="iconfont icon-user icon"></view>
						<input v-model="loginForm.username" class="input" type="text" placeholder="请输入账号" maxlength="30" />
					</view>
					<view class="input-item flex align-center">
						<view class="iconfont icon-password icon"></view>
						<input v-model="loginForm.password" type="password" class="input" placeholder="请输入密码"
							maxlength="20" />
					</view>
					<view class="input-item flex align-center" style="width: 60%;margin: 0px;" v-if="captchaEnabled">
						<view class="iconfont icon-code icon"></view>
						<input v-model="loginForm.code" type="number" class="input" placeholder="请输入验证码" maxlength="4" />
						<view class="login-code">
							<image :src="codeUrl" @click="getCode" class="login-code-img"></image>
						</view>
					</view>
					<view class="action-btn">
						<button @click="handleLogin" class="login-btn cu-btn block bg-blue lg round">登录</button>
					</view>
				</view>
			</view>
		</view>


		<!-- 登录 -->
		<view v-else>
			<view class="container">
				<view class="cover slide-top1" :style="'animation-play-state:' + play[2]">
					<view class="masking slide-top" :class="[collapsedClass, { animating: isAnimating }]" ref="fixedView"
						:style="'animation-play-state:' + play[0]">
						<uni-row>
							<text class="text-first">欢迎使用</text>
							<text class="text-second">校园访客</text>
							<text class="text-third">预约系统</text>
							<view :class="{ active: isActive === true, button: isActive !== true }" @click="startplay"
								:style="'animation-play-state:' + play[1]">
								<uni-row>
									<text class="text-fifth">访客登录</text>
								</uni-row>
							</view>
							<view class="shadow1" :style="'animation-play-state:' + play[1]" />
							<view class="shadow2" :style="'animation-play-state:' + play[1]" />
							<view class="shadow3" :style="'animation-play-state:' + play[1]" />

							<image
								style="width: 100%;height: 1050rpx;opacity: 0.05;border-radius: 0 0 400rpx 400rpx;position: absolute;"
								src="../../../static/uview/common/gray-logo.png"></image>
							<text class="text-forth" @click="login()">账号密码登录</text>
						</uni-row>
					</view>
				</view>
				<view class="sec-masking">
					<uni-row>
						<text class="text-sixth">版本号: v1.0</text>
					</uni-row>
				</view>
			</view>
		</view>
	</view>
</template>
  
<script>

export default {
	data() {
		return {
			src: "staticimagessoutheast.jpg",
			isActive: false,
			isFixedViewVisible: true,
			animationType: "up", // 可选值：right 或 up
			isAnimating: false, // 控制动画执行状态
			play: ["paused", "paused", "paused"],
			page: "index",

			codeUrl: "",
			captchaEnabled: true,
			loginForm: {
				username: "admin",
				password: "admin123",
				code: "",
				uuid: ''
			}
		}
	},
	computed: {
		collapsedClass() {
			return this.isFixedViewVisible ? "" : `collapsed-${this.animationType}`;
		},
	},
	created() {
		this.getCode()
	},
	methods: {
		login() {
			this.play[2] = "running"
			setTimeout(() => { this.page = 'login' }, 1000)

		},
		back() {
			this.page = 'index'
			this.play[0] = "paused"
			this.play[1] = "paused"
			this.play[2] = "paused"
		},
		startplay() {
			this.play[1] = "running"
			this.isActive = true;
			setTimeout(() => { this.isActive = false; }, 300);
			if (this.isAnimating) { return; }
			this.isAnimating = false; // 开始动画执行
			this.play[0] = "running"
			this.isFixedViewVisible = !this.isFixedViewVisible;
			let fixedView = this.$refs.fixedView;
			if (fixedView)
				fixedView.addEventListener('transitionend', () => { this.isAnimating = false; }, { once: true });

			setTimeout(() => { uni.navigateBack({ delta: 1 }); }, 1000)
		},



		// 获取图形验证码
		getCode() {
			let res = {}
			this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
			if (this.captchaEnabled) {
				this.codeUrl = 'data:image/gif;base64,' + res.img
				this.loginForm.uuid = res.uuid
			}
		},
		// 登录方法
		async handleLogin() {
			if (this.loginForm.username === "") {
				this.$modal.msgError("请输入您的账号")
			} else if (this.loginForm.password === "") {
				this.$modal.msgError("请输入您的密码")
			} else if (this.loginForm.code === "" && this.captchaEnabled) {
				this.$modal.msgError("请输入验证码")
			} else {
				this.$modal.loading("登录中，请耐心等待...")
				this.pwdLogin()
			}
		},
		// 密码登录
		async pwdLogin() {
			this.$store.dispatch('Login', this.loginForm).then(() => {
				this.$modal.closeLoading()
				this.loginSuccess()
			}).catch(() => {
				if (this.captchaEnabled) {
					this.getCode()
				}
			})
		},
		// 登录成功后，处理函数
		loginSuccess(result) {
			// 设置用户信息
			this.$store.dispatch('GetInfo').then(res => {
				this.$tab.reLaunch('/pages/index')
			})
		}
	}
}
</script>
  
<style lang="scss">
page {
	background-color: #ffffff;
}

.left {
	display: flex;
	width: 50rpx;
	height: 50rpx;
	position: fixed;
	top: 0px;
	padding: 10rpx;
}

.cover {
	position: absolute;
	width: 100%;
	height: 1250rpx;
	z-index: 9997;
}

.normal-login-container {
	width: 100%;
	margin-top: 200rpx;

	.logo-content {
		width: 100%;
		font-size: 21px;
		text-align: center;
		padding-top: 15%;

		image {
			border-radius: 4px;
		}

		.title {
			margin-left: 10px;
		}
	}

	.login-form-content {
		text-align: center;
		margin: 20px auto;
		margin-top: 15%;
		width: 80%;

		.input-item {
			margin: 20px auto;
			background-color: #f5f6f7;
			height: 45px;
			border-radius: 20px;

			.icon {
				font-size: 38rpx;
				margin-left: 10px;
				color: #999;
			}

			.input {
				width: 100%;
				font-size: 14px;
				line-height: 20px;
				text-align: left;
				padding-left: 15px;
			}

		}

		.login-btn {
			margin-top: 40px;
			height: 45px;
		}

		.xieyi {
			color: #333;
			margin-top: 20px;
		}

		.login-code {
			height: 38px;
			float: right;

			.login-code-img {
				height: 38px;
				position: absolute;
				margin-left: 10px;
				width: 200rpx;
			}
		}
	}
}









.container {
	position: relative;
}

.active {
	position: absolute;
	width: 280rpx;
	height: 280rpx;
	left: 50%;
	margin-left: -140rpx;
	top: 533rpx;
	bottom: 533rpx;
	border-radius: 50%;
	z-index: 9999;

	background: linear-gradient(180deg,
			rgba(110, 109, 122, 0.595),
			rgba(46, 87, 190, 0.714) 70.792%,
			rgb(17, 120, 222) 100%);
	box-shadow: inset 0 0 10px 5px rgba(101, 97, 97, 0.5);
}

.masking {
	position: absolute;
	top: -200rpx;
	width: 100%;
	height: 1250rpx;
	background-color: #9acafc;
	border-radius: 0 0 400rpx 400rpx;
	box-shadow: 0px 5px 8px rgba(0, 0, 0, 0.27);
	transition: transform 0.3s ease-out;
	z-index: 9998;
	padding: 200rpx 0 0 0;
}

.collapsed-up {
	transform: translateY(-100%);
}

.button {
	position: absolute;
	width: 280rpx;
	height: 280rpx;
	left: 50%;
	margin-left: -140rpx;
	top: 533rpx;
	bottom: 533rpx;
	border-radius: 50%;
	transition: background-color 0.3s;
	z-index: 9999;

	background: linear-gradient(180deg,
			rgba(60, 53, 239, 0.595),
			rgba(63, 117, 255, 0.714) 70.792%,
			rgb(70, 161, 253) 100%);
	// box-shadow: 0px 0px 62rpx rgba(1, 7, 22, 0.468);

	box-shadow: 0 0 10px rgba(0, 0, 0, .3) inset;

}

.shadow1 {
	position: absolute;
	width: 280rpx;
	height: 280rpx;
	left: 50%;
	margin-left: -140rpx;
	top: 533rpx;
	bottom: 533rpx;
	border-radius: 50%;
	border-width: 4rpx;
	border-color: rgb(70, 161, 253);
	transition: background-color 0.3s;
	z-index: 9998;
	background-color: rgba(70, 161, 253, 0);

	// box-shadow: 0px 0px 62rpx rgba(1, 7, 22, 0.468);

	box-shadow: 0 0 10px rgba(0, 0, 0, .3) inset;
	-webkit-animation-name: 'ripple1';
	/*动画属性名，也就是我们前面keyframes定义的动画名*/
	-webkit-animation-duration: 0.3s;
	/*动画持续时间*/
	-webkit-animation-timing-function: ease;
	/*动画频率，和transition-timing-function是一样的*/
	-webkit-animation-delay: 0s;
	/*动画延迟时间*/
	-webkit-animation-direction: normal;
	/*定义动画方式*/
}

.shadow2 {
	position: absolute;
	width: 280rpx;
	height: 280rpx;
	left: 50%;
	margin-left: -140rpx;
	top: 533rpx;
	bottom: 533rpx;
	border-radius: 50%;
	border-width: 4rpx;
	border-color: rgb(70, 161, 253);
	transition: background-color 0.3s;
	z-index: 9998;
	background-color: rgba(70, 161, 253, 0);

	// box-shadow: 0px 0px 62rpx rgba(1, 7, 22, 0.468);

	box-shadow: 0 0 10px rgba(0, 0, 0, .3) inset;
	-webkit-animation-name: 'ripple2';
	/*动画属性名，也就是我们前面keyframes定义的动画名*/
	-webkit-animation-duration: 0.4s;
	/*动画持续时间*/
	-webkit-animation-timing-function: ease;
	/*动画频率，和transition-timing-function是一样的*/
	-webkit-animation-delay: 0.1s;
	/*动画延迟时间*/
	-webkit-animation-direction: normal;
	/*定义动画方式*/
}

.shadow3 {
	position: absolute;
	width: 280rpx;
	height: 280rpx;
	left: 50%;
	margin-left: -140rpx;
	top: 533rpx;
	bottom: 533rpx;
	border-radius: 50%;
	border-width: 4rpx;
	border-color: rgb(70, 161, 253);
	transition: background-color 0.3s;
	z-index: 9998;
	background-color: rgba(70, 161, 253, 0);

	// box-shadow: 0px 0px 62rpx rgba(1, 7, 22, 0.468);

	box-shadow: 0 0 10px rgba(0, 0, 0, .3) inset;
	-webkit-animation-name: 'ripple3';
	/*动画属性名，也就是我们前面keyframes定义的动画名*/
	-webkit-animation-duration: 0.5s;
	/*动画持续时间*/
	-webkit-animation-timing-function: ease;
	/*动画频率，和transition-timing-function是一样的*/
	-webkit-animation-delay: 0.2s;
	/*动画延迟时间*/
	-webkit-animation-direction: normal;
	/*定义动画方式*/
}

.button.disabled {
	pointer-events: none;
	/* 禁用按钮的点击事件 */
	opacity: 0.5;
	/* 半透明效果 */
}

.text-first {
	position: absolute;
	width: 256rpx;
	height: 84rpx;
	left: 50%;
	transform: translateX(-50%);
	top: 250rpx;
	bottom: 986.67rpx;

	color: rgb(255, 255, 255);
	font-family: "Microsoft YaHei";
	font-size: 64rpx;
	font-weight: 400;
	line-height: 84rpx;
	letter-spacing: 0rpx;
	text-align: center;
}

.text-second {
	position: absolute;
	width: 256rpx;
	height: 84rpx;
	left: 50%;
	transform: translateX(-50%);
	top: 350rpx;
	bottom: 986.67rpx;

	color: rgb(255, 255, 255);
	font-family: "Microsoft YaHei";
	font-size: 64rpx;
	font-weight: 400;
	line-height: 84rpx;
	letter-spacing: 0rpx;
	text-align: center;
}

.text-third {
	position: absolute;
	width: 200rpx;
	height: 60rpx;
	left: 50%;
	transform: translateX(-50%);
	top: 450rpx;
	bottom: 615rpx;
	margin: 0 auto;

	color: rgb(255, 255, 255);
	font-family: "Microsoft YaHei";
	font-size: 50rpx;
	font-weight: 400;
	line-height: 60rpx;
	letter-spacing: 0rpx;
	text-align: center;
}

.text-forth {
	position: absolute;
	width: 180rpx;
	height: 36rpx;
	left: 0;
	right: 0;
	top: 948rpx;
	bottom: 330rpx;
	margin: 0 auto;

	color: rgb(255, 255, 255);
	font-family: "Microsoft YaHei";
	font-size: 27rpx;
	font-weight: 400;
	line-height: 36rpx;
	letter-spacing: 0rpx;
	text-align: center;
	text-decoration-line: underline;
}

.text-fifth {
	position: absolute;
	width: 120rpx;
	height: 150rpx;
	left: 0;
	right: 0;
	top: 60rpx;
	bottom: 418rpx;
	margin: 0 auto;

	color: rgb(255, 255, 255);
	font-family: "Microsoft YaHei";
	font-size: 60rpx;
	font-weight: 400;
	line-height: 80rpx;
	letter-spacing: 0rpx;
	text-align: center;
}

.text-sixth {
	position: absolute;
	width: 100rpx;
	height: 17rpx;
	margin: 0 auto;
	top: 710rpx;
	left: 50%;
	transform: translateX(-50%);
	padding: 0 179rpx 7rpx 180rpx;
	z-index: 9997;

	color: rgb(15, 15, 15);
	font-family: "Microsoft YaHei";
	font-size: 17rpx;
	font-weight: 400;
	line-height: 17rpx;
	letter-spacing: 0;
	text-align: left;
}

.sec-masking {
	position: fixed;
	bottom: 0;
	width: 100%;
	height: 750rpx;
	background-color: #ffffff;
	z-index: 9996;
}

.slide-top {
	-webkit-animation: slide-top 1s cubic-bezier(0.600, -0.280, 0.735, 0.045) both;
	animation: slide-top 1s cubic-bezier(0.600, -0.280, 0.735, 0.045) both;
}

.slide-top1 {
	-webkit-animation: slide-top1 1s cubic-bezier(0.600, -0.280, 0.735, 0.045) both;
	animation: slide-top1 1s cubic-bezier(0.600, -0.280, 0.735, 0.045) both;
}

.scale-in-center {
	animation: scale-in-center 0.4s cubic-bezier(0.250, 0.460, 0.450, 0.940) both;
}

@-webkit-keyframes slide-top {
	0% {
		-webkit-transform: translateY(0);
		transform: translateY(0);
	}

	100% {
		-webkit-transform: translateY(-1050px);
		transform: translateY(-1050px);
	}
}

@-webkit-keyframes slide-top1 {
	0% {
		-webkit-transform: translateY(0);
		transform: translateY(0);
	}

	100% {
		-webkit-transform: translateY(-1050px);
		transform: translateY(-1050px);
	}
}

@keyframes ripple1 {
	0% {
		transform: scale(1);
	}

	100% {
		transform: scale(1.4);
		opacity: 0;

	}
}

@keyframes ripple2 {
	0% {
		transform: scale(1);
	}

	100% {
		transform: scale(1.5);
		opacity: 0;

	}
}

@keyframes ripple3 {
	0% {
		transform: scale(1);
	}

	100% {
		transform: scale(1.6);
		opacity: 0;

	}
}

@keyframes slide-top {
	0% {
		-webkit-transform: translateY(0);
		transform: translateY(0);
	}

	100% {
		-webkit-transform: translateY(-1050px);
		transform: translateY(-1050px);
	}
}

@keyframes slide-top1 {
	0% {
		-webkit-transform: translateY(0);
		transform: translateY(0);
	}

	100% {
		-webkit-transform: translateY(-1050px);
		transform: translateY(-1050px);
	}
}

@keyframes scale-in-center {
	0% {
		transform: scale(0);
		opacity: 1;
	}

	100% {
		transform: scale(1);
		opacity: 1;
	}
}
</style>
  