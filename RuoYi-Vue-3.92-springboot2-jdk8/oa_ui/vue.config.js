'use strict';
const path = require('path');
const defaultSettings = require('./src/settings.js');
function resolve(dir) {
  return path.join(__dirname, dir);
}

const name = defaultSettings.title || 'vue Element Admin'; // page title
const port = process.env.port || process.env.npm_config_port || 9527; // dev port

module.exports = {
  publicPath: defaultSettings.roterPre + '/',
  outputDir: 'dist',
  assetsDir: 'system',

  // 在 dist/index.html 的输出
  indexPath: 'index.html',
  lintOnSave: process.env.NODE_ENV !== 'production',
  productionSourceMap: false,
  devServer: {
    publicPath: defaultSettings.roterPre + '/',
    port: port,
    proxy: {
      "/dev-api": {
        target: "http://localhost:8081",
        changeOrigin: true,
        pathRewrite: { "^/dev-api": "" },
      },
    },
    overlay: {
      warnings: false,
      errors: true,
    }
  },
  configureWebpack: {
    name: name,
    output: {
      hashFunction: 'sha256',
    },
    resolve: {
      alias: {
        '@': resolve('src'),
        '~@': resolve('static'),
      },
    },
  },
  transpileDependencies: [
    // 对 xmind 编辑器中的 quill 做处理
    "quill",

    // 将 xmind 在线预览工具加入 babel 进行处理
    "xmind-embed-viewer"
  ],
  chainWebpack(config) {
    // 打包可视化插件
    // config.plugin('webpack-bundle-analyzer')
    // .use(require('webpack-bundle-analyzer').BundleAnalyzerPlugin);
    // config.plugin('compressionPlugin')
    // .use(new CompressionPlugin({
    //     filename: '[path].gz[query]',
    //     algorithm: 'gzip',
    //     test: productionGzipExtensions,
    //     threshold: 10240,
    //     minRatio: 0.8,
    //     deleteOriginalAssets: true
    // }));

    config.plugins.delete('preload'); // TODO: need test
    config.plugins.delete('prefetch'); // TODO: need test


    config.module.rule('svg').exclude.add(resolve('src/icons')).end();
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]',
      })
      .end();


    config.module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap((options) => {
        options.compilerOptions.preserveWhitespace = true;
        return options;
      })
      .end();
    config
      // https://webpack.js.org/configuration/devtool/#development
      .when(process.env.NODE_ENV === 'development', (config) => config.devtool('cheap-source-map'));

    config.when(process.env.NODE_ENV !== 'development', (config) => {
      config
        .plugin('ScriptExtHtmlWebpackPlugin')
        .after('html')
        .use('script-ext-html-webpack-plugin', [
          {
            // `runtime` must same as runtimeChunk name. default is `runtime`
            inline: /runtime\..*\.js$/,
          },
        ])
        .end();
      config.optimization.splitChunks({
        chunks: 'all',
        cacheGroups: {
          libs: {
            name: 'chunk-libs',
            test: /[\\/]node_modules[\\/]/,
            priority: 10,
            chunks: 'initial', // only package third parties that are initially dependent
          },
          elementUI: {
            name: 'chunk-elementUI', // split elementUI into a single package
            priority: 20, // the weight needs to be larger than libs and app or it will be packaged into libs or app
            test: /[\\/]node_modules[\\/]_?element-ui(.*)/, // in order to adapt to cnpm
          },
          commons: {
            name: 'chunk-commons',
            test: resolve('src/components'), // can customize your rules
            minChunks: 3, //  minimum common number
            priority: 5,
            reuseExistingChunk: true,
          },
        },
      });
      config.optimization.runtimeChunk('single');
    });
  },
};
