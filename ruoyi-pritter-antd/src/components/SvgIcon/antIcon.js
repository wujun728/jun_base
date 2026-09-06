import * as components from '@ant-design/icons-vue'

export default {
  install(app) {
    for (const key in components) {
      const componentConfig = components[key]
      if (
        typeof componentConfig === 'function' &&
        componentConfig.displayName
      ) {
        app.component(key, componentConfig)
      }
    }
  },
}
