import hasRole from './permission/hasRole'
import hasPermi from './permission/hasPermi'
import copyText from './common/copyText'
import focus from './common/focus'
import full from './common/full'
import type { App } from 'vue'

export default function directive(app: App<Element>) {
  app.directive('hasRole', hasRole)
  app.directive('hasPermi', hasPermi)
  app.directive('copyText', copyText)
  app.directive('focus', focus)
  app.directive('full', full)
}