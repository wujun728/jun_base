// 请务必保证pageName在整个项目中是唯一的
export const authUserRole = 'authUserRole'
export const authRole = 'authRole'
export const user = 'user'
export const job = 'job'
export const jobLog = 'jobLog'
export const logininfor = 'logininfor'
export const online = 'online'
export const operlog = 'operlog'
export const dictData = 'dict/data'
export const config = 'config'
export const dept = 'dept'
export const dictType = 'dict/type'
export const menu = 'menu'
export const notice = 'notice'
export const post = 'post'
export const role = 'role'
export const gen = 'gen'

const map = new Map()
map.set([authUserRole, authRole], '/role')

export const getUrl = (pageName) => {
  const mapSize = map.size
  const iterator = map.keys()
  for (let i = 0; i < mapSize; i++) {
    const key = iterator.next().value
    if (key.includes(pageName)) {
      return map.get(key)
    }
  }
  return `/${pageName}`
}
