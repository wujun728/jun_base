const { environment } = require('./config/environment.js')

module.exports = {
  devServer: {
    port: 9001,
    proxy: {
      '/': {
        target: environment.baseURL,
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/': ''
        }
      }
    },
  }
}