import useUserStore from '@/store/modules/user'
const admin = '*:*:*'
let allPermi = void 0
export default (permission) => {
  if (!allPermi) {
    allPermi = useUserStore().permissions
  }
  if (!permission) return true
  if (admin === allPermi[0]) return true
  let hasPermissions = false
  if (Array.isArray(permission)) {
    hasPermissions = permission.some((item) => {
      return allPermi.includes(item)
    })
  }
  if (typeof permission === 'string') {
    hasPermissions = allPermi.includes(permission)
  }
  return hasPermissions
}
