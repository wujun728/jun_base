"use strict";
const { run } = require('runjs')
const chalk = require('chalk')
const config = require('./vue.config.js')
const rawArgv = process.argv.slice(2)
const args = rawArgv.join(' ')


  const report = rawArgv.includes('--report')

  const port = 3000
  const publicPath = config.publicPath

  var connect = require('connect')
  var serveStatic = require('serve-static')
  const app = connect()

  app.use(
    publicPath,
    serveStatic('./dist', {
      index: ['index.html', '/']
    })
  )

  app.listen(port, function () {
    console.log(chalk.green(`> Preview at  http://localhost:${port}${publicPath}`))
    if (report) {
      console.log(chalk.green(`> Report at  http://localhost:${port}${publicPath}report.html`))
    }

  })
