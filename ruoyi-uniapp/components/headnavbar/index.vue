<template>
	<view class="wrap wrap-home">
		<u-navbar :title="title" height="44" :is-back="false">
			<!-- #ifdef APP-PLUS --><!-- #endif -->
			<view slot="right">
				<view style="color: #22262d;font-size: 20px;" class="iconfont icon-setting-two" @click="navTo('/pages/sys/user/setting')"></view>
			</view>
			<u-avatar class="home-head" :src="avatarUrl" @click="show = true"></u-avatar>
		</u-navbar>
	</view>
</template>
<script>
	export default {
		
		props: {
			title: {
				type: String,
				required: true
			}
		},
		data() {
			return {
				src: '',
				show: false,
			};
		},
		onLoad() {

		},
		computed: {
			avatarUrl() {
				var url = this.vuex_config.baseUrl + this.vuex_user.avatar || '/static/aidex/tabbar/my_2.png';
				url = this.replaceAll(url,'\\','/');
				return url + '?t=' + new Date().getTime();
			}
		},
		methods: {
			showPersonalInfo() {
				this.show = true
			},
			navTo(url) {
				uni.navigateTo({
					url: url
				});
			}
		}
	};
</script>
<style lang="scss">
	.slot-wrap {
		display: flex;
		align-items: center;
		/* 如果您想让slot内容占满整个导航栏的宽度 */
		/* flex: 1; */
		/* 如果您想让slot内容与导航栏左右有空隙 */
		/* padding: 0 30rpx; */
	}

	page {
		background-color: #ffffff;
	}


</style>
