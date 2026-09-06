<script>
	export default {
		onLaunch: function() {
			
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style lang="scss">
	*{
		margin: 0;
		padding: 0;
	}
	body{
		background: rgb(248, 248, 248);
	}
	/*每个页面公共css */
	ul,li {
		margin: 0;
		padding: 0;
		list-style: none;
	}
	.container {
		height: 100%;
		// padding: 20px;
		// font-size: 14px;
		// line-height: 24px;
		// background: #ffffff;
	}
</style>
