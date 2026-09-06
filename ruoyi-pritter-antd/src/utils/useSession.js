class UseSession {
  get(key) {
    const json = window.sessionStorage.getItem(key)
    try {
      return JSON.parse(json)
    } catch (error) {
      return json
    }
  }
  set(key, value) {
    window.sessionStorage.setItem(key, JSON.stringify(value))
    return window.sessionStorage.getItem(key)
  }
  remove(key) {
    window.sessionStorage.removeItem(key)
    return key
  }
  clear() {
    window.sessionStorage.clear()
  }
}
const session = new UseSession()
export default session
