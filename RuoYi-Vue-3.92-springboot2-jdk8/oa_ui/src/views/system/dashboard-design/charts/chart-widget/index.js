export const registerChartWidgets = (app) => {
  // const modules = require.context('./', false, /\w+\.vue$/)
  // for (const path in modules) {
  //   let cname = modules[path].default.name
  //   app.component(cname, modules[path].default)
  // }

  const requireComponent = require.context('./', false, /\w+\.vue$/)
  /* 全局注册！！ */
  requireComponent.keys().map((fileName) => {
    let comp = requireComponent(fileName).default
    app.component(comp.name, comp)
  })
}
