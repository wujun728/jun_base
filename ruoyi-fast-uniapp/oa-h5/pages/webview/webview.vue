<template>
	<web-view class="web-view" :webview-styles="webviewStyles" :src="url" :style="{width: windowW + 'px', height: windowH + 'px'}"></web-view>
</template>

<script>
	// +----------------------------------------------------------------------
	// | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
	// +----------------------------------------------------------------------
	// | Copyright (c) 2016~2024 https://www.crmeb.com All rights reserved.
	// +----------------------------------------------------------------------
	// | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
	// +----------------------------------------------------------------------
	// | Author: CRMEB Team <admin@crmeb.com>
	// +----------------------------------------------------------------------
	export default {
		data() {
			return {
				windowH: 0,
				windowW: 0,
				webviewStyles: {
					progress: {
						color: 'transparent'
					}
				},
				url: ''
			}
		},
		onLoad(option) {
			this.url = decodeURIComponent(option.url);
			try {
				const res = uni.getSystemInfoSync();
				this.windowW = res.windowWidth;
				this.windowH = res.windowHeight;
			} catch (e) {
				// error
			}
		}
	}
</script>
