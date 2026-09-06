import { App } from 'vue'
import hasRole from './permission/hasRole'
import hasPermi from './permission/hasPermi'
import copyText from './common/copyText'
import drag from './common/drag'
export default function directive(app: App) {
  hasRole(app)
  hasPermi(app)
  copyText(app)
  drag(app)
}
