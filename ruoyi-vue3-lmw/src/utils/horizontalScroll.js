/**
 * 横向滚动条
 */
export default class horizontalScroll {
  constructor(nativeElement) {
    this.el = nativeElement
    this.handleWheelEvent()
  }
  handleWheelEvent() {
    let wheel = ''

    if ('onmousewheel' in this.el) {
      wheel = 'mousewheel'
    } else if ('onwheel' in this.el) {
      wheel = 'wheel'
    } else if ('attachEvent' in window) {
      wheel = 'onmousewheel'
    } else {
      wheel = 'DOMMouseScroll'
    }
    this.el['addEventListener'](wheel, this.scroll, { passive: true })
  }

  scroll = (event) => {
    if (this.el.clientWidth >= this.el.scrollWidth) {
      return
    }
    this.el.scrollLeft += event.deltaY
      ? event.deltaY
      : event.detail && event.detail !== 0
        ? event.detail
        : -event.wheelDelta
  }
}
