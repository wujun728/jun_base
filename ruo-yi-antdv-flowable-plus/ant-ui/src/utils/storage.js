import Cookies from 'js-cookie'
export function setCookie(k, v) {
  if(typeof(v) == 'undefined' || v == null){
    return
  }

  var val = v
  if(typeof(v) == 'object'){
    val = JSON.stringify(v)
  }
  Cookies.set(k, val)
  Cookies.get(k, val)
}

export function getCookie(k) {
  var val = Cookies.get(k)
  try{
    //如果是number boolean jsonstring是不会报错的
    return JSON.parse(val)
  }catch(e){
    return val
  }
}
export function delCookie(k) {
  Cookies.remove(k)
}



export function setStorageItem(k, v) {
  if(typeof(v) == 'undefined' || v == null){
    return
  }

  var val = v
  if(typeof(v) == 'object'){
    val = JSON.stringify(v)
  }
  localStorage.setItem(k, val)
}

export function getStorageItem(k) {
  var val = localStorage.getItem(k)
  try{
    //如果是number boolean jsonstring是不会报错的
    return JSON.parse(val)
  }catch(e){
    return val
  }
}
export function delStorageItem(k) {
  localStorage.removeItem(k)
}