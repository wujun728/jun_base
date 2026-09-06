import Cookies from 'js-cookie'
import useStorage from '@/utils/hsj/useStorage'
const TokenKey = 'Admin-Token'

export function getToken() {
  Cookies.get(TokenKey)
  return useStorage.get(TokenKey)
}

export function setToken(token) {
  Cookies.set(TokenKey, token, { expires: 7 })
  return useStorage.set(TokenKey, token)
}

export function removeToken() {
  Cookies.remove(TokenKey)
  return useStorage.remove(TokenKey)
}
