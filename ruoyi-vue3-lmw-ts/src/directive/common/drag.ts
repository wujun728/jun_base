import { App } from 'vue'
export default function (app: App) {
  app.directive('drag', {
    mounted(_el, binding) {
      if (!binding.value) return false

      const dragDom = document.querySelector(binding.value[0]) as HTMLElement
      const dragHandle = document.querySelector(binding.value[1]) as HTMLElement

      if (!dragHandle || !dragDom) {
        return false
      }

      dragHandle.onmouseover = () => (dragHandle.style.cursor = `move`)

      function down(e: MouseEvent | TouchEvent, type: string): downReturn {
        // 鼠标按下，计算当前元素距离可视区的距离
        const disX =
          type === 'pc'
            ? (e as MouseEvent).clientX - dragHandle.offsetLeft
            : (e as TouchEvent).touches[0].clientX - dragHandle.offsetLeft
        const disY =
          type === 'pc'
            ? (e as MouseEvent).clientY - dragHandle.offsetTop
            : (e as TouchEvent).touches[0].clientY - dragHandle.offsetTop

        // body宽度
        const screenWidth = document.body.clientWidth
        const screenHeight =
          document.body.clientHeight || document.documentElement.clientHeight

        // 被拖动元素宽度
        const dragDomWidth = dragDom.offsetWidth
        // 被拖动元素高度
        const dragDomheight = dragDom.offsetHeight

        // 拖动限位
        const minDragDomLeft = dragDom.offsetLeft
        const maxDragDomLeft = screenWidth - dragDom.offsetLeft - dragDomWidth
        const minDragDomTop = dragDom.offsetTop
        const maxDragDomTop = screenHeight - dragDom.offsetTop - dragDomheight

        // 获取到的值带px 正则匹配替换
        let styL: string | number = getComputedStyle(dragDom).left
        let styT: string | number = getComputedStyle(dragDom).top
        styL = +styL.replace(/\px/g, '')
        styT = +styT.replace(/\px/g, '')

        return {
          disX,
          disY,
          minDragDomLeft,
          maxDragDomLeft,
          minDragDomTop,
          maxDragDomTop,
          styL,
          styT,
        }
      }

      function move(e: MouseEvent | TouchEvent, type: string, obj: downReturn) {
        const {
          disX,
          disY,
          minDragDomLeft,
          maxDragDomLeft,
          minDragDomTop,
          maxDragDomTop,
          styL,
          styT,
        } = obj

        // 通过事件委托，计算移动的距离
        let left =
          type === 'pc'
            ? (e as MouseEvent).clientX - disX
            : (e as TouchEvent).touches[0].clientX - disX
        let top =
          type === 'pc'
            ? (e as MouseEvent).clientY - disY
            : (e as TouchEvent).touches[0].clientY - disY

        // 边界处理
        if (-left > minDragDomLeft) {
          left = -minDragDomLeft
        } else if (left > maxDragDomLeft) {
          left = maxDragDomLeft
        }

        if (-top > minDragDomTop) {
          top = -minDragDomTop
        } else if (top > maxDragDomTop) {
          top = maxDragDomTop
        }

        // 移动当前元素
        dragDom.style.cssText += `;left:${left + styL}px;top:${top + styT}px;`
      }

      dragHandle.onmousedown = (e) => {
        const obj = down(e, 'pc')
        document.onmousemove = (e) => {
          move(e, 'pc', obj)
        }
        document.onmouseup = () => {
          document.onmousemove = null
          document.onmouseup = null
        }
      }
      dragHandle.ontouchstart = (e) => {
        const obj = down(e, 'app')
        document.ontouchmove = (e) => {
          move(e, 'app', obj)
        }
        document.ontouchend = () => {
          document.ontouchmove = null
          document.ontouchend = null
        }
      }
    },
  })
}
