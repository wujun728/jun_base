import { encrypt, decrypt } from '@/utils/jsencrypt.js'

const STORAGE_KEY = 'ruoyi_login_remember_cipher'

/** 与 React 版一致的读写接口：记住用户名与加密后的密码 */
export function readStoredLoginCredentials() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    if (!raw) return null
    const { username, passwordEnc } = JSON.parse(raw)
    if (!username || !passwordEnc) return null
    const password = decrypt(passwordEnc)
    return { username, password }
  } catch {
    return null
  }
}

export function writeStoredLoginCredentials({ username, password }) {
  localStorage.setItem(
    STORAGE_KEY,
    JSON.stringify({
      username,
      passwordEnc: encrypt(password),
    }),
  )
}

export function clearStoredLoginCredentials() {
  localStorage.removeItem(STORAGE_KEY)
}
