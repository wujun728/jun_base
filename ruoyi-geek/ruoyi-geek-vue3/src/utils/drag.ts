/**
 * 拖拽函数
 * @param onDraging 拖拽时触发的函数
 * @param beforeStop 拖拽结束前触发的函数
 * @returns 用于启动拖动的函数
 */
export function drag(onDraging: (arg: { x: number, y: number, dx: number, dy: number }) => void, beforeStop?: Function) {
  let initialX: null | number = null;
  let initialY: null | number = null;
  function doDrag(event: MouseEvent) {
    if (initialX === null || initialY === null) {
      initialX = event.clientX;
      initialY = event.clientY;
    }
    fun.event = event;
    onDraging({
      x: event.clientX,
      y: event.clientY,
      dx: event.clientX - initialX,
      dy: event.clientY - initialY,
    })
    initialX = event.clientX;
    initialY = event.clientY;
  }
  function stopDrag() {
    if (beforeStop) {
      beforeStop()
    }
    initialX = null;
    initialY = null;
    window.removeEventListener("mousemove", doDrag);
    window.removeEventListener("mouseup", stopDrag)
  }
  const fun = () => {
    window.addEventListener("mousemove", doDrag);
    window.addEventListener("mouseup", stopDrag);
  }
  fun.stopDrag = stopDrag
  fun.onDraging = onDraging
  fun.event = {} as any
  return fun
}