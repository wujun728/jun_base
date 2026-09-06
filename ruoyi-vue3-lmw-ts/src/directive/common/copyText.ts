import { App } from 'vue'
function copyTextToClipboard(
  textToCopy: string,
  { target = document.body } = {}
) {
  return new Promise((resolve) => {
    if (navigator.clipboard && window.isSecureContext) {
      // 使用现代 Clipboard API
      navigator.clipboard
        .writeText(textToCopy)
        .then(() => {
          resolve(true)
        })
        .catch((err) => {
          console.log('copy失败: ', err)
          resolve(false)
        })
    } else {
      // 回退方案：创建一个隐藏的 <textarea> 元素
      const textarea = document.createElement('textarea')
      textarea.value = textToCopy
      target.appendChild(textarea)
      textarea.select()
      let isSuccess = false
      try {
        isSuccess = document.execCommand('copy')
      } catch (err) {
        console.log('copy失败: ', err)
        isSuccess = false
      }
      document.body.removeChild(textarea)
      resolve(isSuccess)
    }
  })
}
export default function (app: App) {
  app.directive('copyText', {
    beforeMount(el, { value, arg }) {
      if (arg === 'callback') {
        el.$copyCallback = value
      } else {
        el.$copyValue = value
        const handler = async () => {
          const copyFlag = await copyTextToClipboard(el.$copyValue)
          if (el.$copyCallback) {
            el.$copyCallback(copyFlag, el.$copyValue)
          }
        }
        el.addEventListener('click', handler)
        el.$destroyCopy = () => el.removeEventListener('click', handler)
      }
    },
  })
}
