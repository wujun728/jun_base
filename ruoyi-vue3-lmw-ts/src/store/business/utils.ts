export const deepFindValue = (
  obj: anyObj,
  key: string,
  isFirstValue = true
) => {
  let finalData
  if (isFirstValue) {
    finalData = undefined
  } else {
    finalData = []
  }
  let isChange = false
  function getList(obj: anyObj, key: string) {
    if (!isChange) {
      if (Reflect.toString.call(obj) === '[object Object]') {
        if (obj.hasOwnProperty(key)) {
          if (isFirstValue) {
            finalData = obj[key]
            isChange = true
          } else {
            finalData.push(obj[key])
          }
          return
        } else {
          const objKeys = Object.keys(obj)
          for (let objKey of objKeys) {
            getList(obj[objKey], key)
          }
        }
      }
    }
  }
  getList(obj, key)

  return finalData
}
