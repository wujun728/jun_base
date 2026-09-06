module.exports = {
	devServer: {
		proxy: {
			'/api': {
				target: 'localhost:5000',
				secure: false,//https需要
				changeOrigin: true, //是否允许跨域
				pathRewrite: {
					'^/api':'' //重命名
				}
			}
		}
	}
}