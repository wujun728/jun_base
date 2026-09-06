<template>
	<view>
		<web-view :webview-styles="webviewStyles" :src="webviewUrl"></web-view>
	</view>
</template>
<script>
/**
 * Copyright (c) 2013-Now http://aidex.vip All rights reserved.
 */
export default {
	data() {
		return {
			webviewUrl: '',
			webviewStyles: {
				progress: {
					color: '#FF7200'
				}
			}
		};
	},
	onLoad(params) {
		this.webviewUrl = params.url;
		// this.webviewUrl = `http://127.0.0.1:8980/js/a/bpm/bpmCategory/index/process?__sid=${this.vuex_token}&__cookie=true`;
		if (params.title != ''){
			uni.setNavigationBarTitle({
				title: params.title
			})
		}
	}
};
</script>
<style lang="scss">
	
</style>
