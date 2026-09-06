class UseStorage {
  get(key: string) {
    const json = window.localStorage.getItem(key)
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
    window.localStorage.setItem(key, JSON.stringify(value))
    return value
  }
  remove(key: any) {
    window.localStorage.removeItem(key)
    return key
  }
  clear() {
    window.localStorage.clear()
  }
}
const storage = new UseStorage()
export default storage
