module.exports = {
  presets: [['@vue/app', { useBuiltIns: 'entry' }]],
  env: { development: { plugins: ['dynamic-import-node'] } },
  plugins: [
    '@babel/plugin-proposal-optional-chaining' //可选链 ?.
  ]
}
