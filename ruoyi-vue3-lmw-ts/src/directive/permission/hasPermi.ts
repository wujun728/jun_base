import { App } from 'vue'
import useUserStore from '@/store/modules/user'

export default function (app: App) {
  app.directive('hasPermi', {
    mounted(el, binding) {
      const { value } = binding
      const all_permission = '*:*:*'
      const permissions = useUserStore().permissions
      if (value && value instanceof Array && value.length > 0 && value[0]) {
        const permissionFlag = value
        const hasPermissions = permissions.some((permission) => {
          return (
            all_permission === permission || permissionFlag.includes(permission)
          )
        })
        if (!hasPermissions) {
          el.parentNode && el.parentNode.removeChild(el)
        }
      }
    },
  })
}
