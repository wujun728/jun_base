const height = (el, all) => {
    if (!el) {
      return 0
    }
    const style = document.defaultView.getComputedStyle(el)
    return parseFloat(
      parseFloat(style.marginTop) +
        parseFloat(style.marginBottom) +
        (all ? 0 : parseFloat(style.paddingTop)) +
        (all ? 0 : parseFloat(style.paddingBottom)) +
        parseFloat(style.borderTopWidth) +
        parseFloat(style.borderBottomWidth)
    )
  }
  
  const heightDirective = {
    inserted(el, data) {
      let dom = null
      el.classList.add('v-height-box')
      let value = 0
      let flag = false
      if (data.value && data.value !== true) {
        value = data.value
      }
      if (!value) {
        let parent = el.parentNode
        let child = el
        while (parent) {
          flag = parent.classList.contains('v-height-box')
          if (flag || parent.classList.contains('divBox') || parent.classList.contains('app-main')) {
            if (flag) {
              dom = parent
            }
            parent = null
          } else {
            value += height(parent)
            if (parent.classList.contains('v-height-flag')) {
              ;[...parent.children].forEach((item) => {
                if (item !== child) {
                  value += height(item, true) + item.clientHeight
                }
              })
            }
            child = parent
            parent = parent.parentNode
          }
        }
        if (value) {
          value *= -1
        }
      }
  
      if (flag) {
        const style = document.defaultView.getComputedStyle(dom)
        value += -1 * parseFloat(parseFloat(style.paddingTop) + parseFloat(style.paddingBottom))
      }
  
      el.style.height = (dom ? dom.clientHeight : window.innerHeight - 132) + value + 'px'
      el.style.overflow = 'auto'
    },
    update() {
      heightDirective.inserted(...arguments)
    }
  }
  
  export default {
    install(Vue) {
      Vue.directive('height', heightDirective)
      Vue.directive('height-flag', {
        inserted(el, data) {
          el.classList.add('v-height-flag')
        }
      })
  
      Vue.directive('height-box', {
        inserted(el, data) {
          el.classList.add('v-height-box')
        }
      })
    }
  }