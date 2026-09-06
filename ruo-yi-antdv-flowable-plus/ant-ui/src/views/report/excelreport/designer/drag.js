import Vue from 'vue'
Vue.directive('dragx', (el, binding, vnode) => {
  //  默认参数
  const defaultOpts = {
    dragDirection: 'n, e, s, w, ne, se, sw, nw, all', // 东 e 西 w 南 s 北 n 东北ne 东南 se 西南 sw 西北 nw 全部 all
    dragContainerId: '',
    dragBarClass: '',  // 类选择器
    resizeEdge: 10,
    dirctDom: true,
    canDrag: true,
    canResize: true
  }
  let isMove = false
  binding.value = binding.value || {}
  const cfg = Object.assign({}, defaultOpts, binding.value)

  // 获取目标元素 resize方向
  function getDirection (e) {
    const el = e.currentTarget
    let dir = ''
    const rect = el.getBoundingClientRect()
    const win = el.ownerDocument.defaultView
    const offset = {
      top: rect.top + win.pageYOffset,
      left: rect.left + win.pageXOffset,
      right: rect.right + win.pageXOffset,
      bottom: rect.bottom + win.pageYOffset,
    }
    // 东 E    西 W   南 S   北 N
    if (e.pageY > offset.top && e.pageY < offset.top + cfg.resizeEdge) {
      dir += 'n'
    }
    else if (e.pageY < offset.bottom && e.pageY > offset.bottom - cfg.resizeEdge) {
      dir += 's'
    }
    if (e.pageX > offset.left && e.pageX < offset.left + cfg.resizeEdge) {
      dir += 'w'
    }
    else if (e.pageX < offset.right && e.pageX > offset.right - cfg.resizeEdge) {
      dir += 'e'
    }
    if (binding.value) {
      const directions = cfg.dragDirection.split(',')
      for (let i = 0; i < directions.length; i++) {
        const handle = directions[i].replace(/(^\s*)|(\s*$)/g, '')
        if (handle == 'all' || handle === dir) {
          return dir
        }
      }
    }
    return ''
  }

  el.onmousedown = function (e) {
    isMove = false
    // drag1
    if (cfg.dragBarClass.length > 0 && e.target.classList.contains(cfg.dragBarClass)) {
      isMove = true
      document.body.style.cursor = 'move'
    }
    const dir = getDirection(e)
    const style = window.getComputedStyle(el)
    function getStyleNumValue (key) {
      return parseInt(style.getPropertyValue(key), 10)
    }
    const rect = el.getBoundingClientRect()
    const data = {
      width: getStyleNumValue('width'),
      height: getStyleNumValue('height'),
      left: getStyleNumValue('left'),
      top: getStyleNumValue('top'),
      borderLeft: getStyleNumValue('border-left-width'),
      borderTop: getStyleNumValue('border-top-width'),
      borderRight: getStyleNumValue('border-right-width'),
      borderBottom: getStyleNumValue('border-bottom-width'),
      deltX: e.pageX - rect.left, // 鼠标在元素内位置
      deltY: e.pageY - rect.top,
      startX: rect.left,  // 元素的位置
      startY: rect.top
    }
    if (dir === '' && !isMove) return
    // 创建遮罩
    const mask = document.createElement('div')
    mask.style.cssText = 'position:absolute;top:0px;bottom:0px;left:0px;right:0px;'
    document.body.appendChild(mask)
    document.onmousemove = function (edom) {
      // 获取当前鼠标位置
      // 右
      if (dir.indexOf('e') > -1) {
        data.width = edom.pageX - data.startX + data.borderLeft + data.borderRight
      }
      // 下
      if (dir.indexOf('s') > -1) {
        data.height = edom.pageY - data.startY + data.borderBottom + data.borderTop
      }
      // 上
      if (dir.indexOf('n') > -1) {
        const deltheight = data.startY + data.borderBottom + data.borderTop - edom.pageY
        data.height += deltheight
        data.top -= deltheight
        data.startY -= deltheight
      }
      // 左
      if (dir.indexOf('w') > -1) {
        const deltwidth = data.startX + data.borderLeft + data.borderRight - edom.pageX
        data.width += deltwidth
        data.left -= deltwidth
        data.startX -= deltwidth
      }
      // 移动
      if (isMove && cfg.canDrag) {
        const targetPageX = edom.pageX
        const targetPageY = edom.pageY
        const deltX = targetPageX - data.startX - data.deltX
        const deltY = targetPageY - data.startY - data.deltY
        let newLeft = parseInt(getStyleNumValue('left') || '0', 10) + deltX
        let newTop = parseInt(getStyleNumValue('top') || '0', 10) + deltY
        console.log(window.innerWidth + data.borderLeft + data.borderRight - data.width - cfg.resizeEdge)
        if (newLeft <= 0) {
          newLeft = 0
        }
        else if (newLeft >= window.innerWidth - data.width - cfg.resizeEdge) {
          newLeft = window.innerWidth - data.width - cfg.resizeEdge
        }
        if (newTop <= 0) {
          newTop = 0
        }
        else if (newTop >= window.innerHeight - data.height - cfg.resizeEdge) {
          newLeft = window.innerHeight - data.height - cfg.resizeEdge
        }
        data.left = newLeft
        data.top = newTop
        data.startX = data.startX + deltX
        data.startY = data.startY + deltY
      }
      // 缩放
      if (cfg.dirctDom) {
        if (cfg.canResize) {
          el.style.width = data.width + 'px'
          el.style.height = data.height + 'px'
        }
        if (cfg.canDrag) {
          el.style.left = data.left + 'px'
          el.style.top = data.top + 'px'
        }
      }
      el.dispatchEvent(new CustomEvent('bindUpdate', { detail: data }))
    }
    document.onmouseup = function (e) {
      document.body.style.cursor = ''
      document.onmousemove = null
      document.onmouseup = null
      isMove = false
      document.body.removeChild(mask)
    }
    document.body.style.cursor = dir + '-resize'
  }

  el.onmousemove = function (e) {
    if (cfg.dragBarClass.length > 0 && e.target.classList.contains(cfg.dragBarClass) && cfg.canDrag) {
      el.style.cursor = 'move'
      return
    }
    const dir = getDirection(e)
    if (dir !== '') {
      el.style.cursor = dir + '-resize'
      return
    }
    el.style.cursor = ''
  }

  el.onmouseleave = function (e) {
    el.style.cursor = ''
  }
})
