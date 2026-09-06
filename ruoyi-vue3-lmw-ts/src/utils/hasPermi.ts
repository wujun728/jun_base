import useUserStore from '@/store/modules/user'
const admin = '*:*:*'
let allPermi: string[]
export default (permission: string | string[]) => {
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
