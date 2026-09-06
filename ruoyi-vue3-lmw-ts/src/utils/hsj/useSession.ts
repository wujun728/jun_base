class UseSession {
  get(key: string) {
    const json = window.sessionStorage.getItem(key)
    try {
      if (json) {
        return JSON.parse(json)
      } else {
        return json
      }
    } catch (error) {
      return json
    }
  }
  set(key: string, value: any) {
    window.sessionStorage.setItem(key, JSON.stringify(value))
    return window.sessionStorage.getItem(key)
  }
  remove(key: string) {
    window.sessionStorage.removeItem(key)
    return key
  }
  clear() {
    window.sessionStorage.clear()
  }
}
const session = new UseSession()
export default session
