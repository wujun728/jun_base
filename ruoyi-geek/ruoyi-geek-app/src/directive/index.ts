// #ifdef APP-VUE || H5
import copyText from './common/copyText'
// #endif
import hasRole from './permission/hasRole'
import hasPermi from './permission/hasPermi'
import focus from './common/focus'
import full from './common/full'
import { App } from 'vue'

export default function directive(app: App) {
  // #ifdef APP-VUE || H5
  app.directive('copyText', copyText)
  // #endif
  app.directive('hasRole', hasRole)
  app.directive('hasPermi', hasPermi)
  app.directive('focus', focus)
  app.directive('full', full)
}