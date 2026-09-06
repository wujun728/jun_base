<script>
export default {
	onLaunch: function () {
		console.log('App Launch')
		// this.$socket.connect({ url: "ws://127.0.0.1:8080/websocket" }).then(res => {
		// 	console.log("success");
		// 	this.$socket.onMessage(res => {
		// 		console.log("onmessage",res);
		// 	})
		// 	this.$socket.on("on").then(res=>{
		// 		console.log("on",res);
		// 	})
		// 	this.$socket.send({msg:"xxx"},true).then(res=>{
		// 		console.log("callback-uuid",res);
		// 	})
		// 	this.$socket.send({event:"on"})
		// })
	},
	onShow: function () {
		console.log('App Show')
	},
	onHide: function () {
		console.log('App Hide')
	}
}
</script>

<style lang="scss">
@import "uview-plus/index.scss";
@import '@/static/scss/index.scss';
</style>