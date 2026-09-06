import Cookies from 'js-cookie'

import { getStorageItem, setStorageItem, delStorageItem } from '@/utils/storage'

const ShareTokenKey = 'shareToken'
const AccessUserKey = 'gaeaUser'

const TokenKey = 'Admin-Token'

export function getToken() {
  return Cookies.get(TokenKey)
}
export function getShareToken() {
  return getStorageItem(ShareTokenKey) == null ? '' : getStorageItem(ShareTokenKey)
}
export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
