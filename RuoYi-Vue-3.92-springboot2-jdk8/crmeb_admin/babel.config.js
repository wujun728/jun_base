module.exports = {
  presets: [
    ['@vue/app', {
      'useBuiltIns': 'entry',
      polyfills: ['es6.promise', 'es6.symbol']
    }]
  ],
  plugins: [
    '@babel/plugin-proposal-class-properties',
    '@babel/plugin-proposal-optional-chaining'
  ]
}
