class UseStorage {
  get(key) {
    const json = window.localStorage.getItem(key)

    try {
      return JSON.parse(json)
    } catch (error) {
      return json
    }
  }
  set(key, value) {
    window.localStorage.setItem(key, JSON.stringify(value))
    return value
  }
  remove(key) {
    window.localStorage.removeItem(key)
    return key
  }
  clear() {
    window.localStorage.clear()
  }
}
const storage = new UseStorage()
export default storage
