export default {
  /**
   * 消息提示
   * @param content 消息内容
   */
  msg(content: string): void {
    uni.showToast({
      title: content,
      icon: 'none'
    })
  },
  /**
   * 错误消息
   * @param content 消息内容
   */
  msgError(content: string): void {
    uni.showToast({
      title: content,
      icon: 'error'
    })
  },
  /**
   * 成功消息
   * @param content 消息内容
   */
  msgSuccess(content: string): void {
    uni.showToast({
      title: content,
      icon: 'success'
    })
  },
  /**
   * 隐藏消息
   */
  hideMsg(): void {
    uni.hideToast()
  },
  /**
   * 弹出提示
   * @param content 提示内容
   */
  alert(content: string): void {
    uni.showModal({
      title: '提示',
      content: content,
      showCancel: false
    })
  },
  /**
   * 确认窗体
   * @param content 提示内容
   * @returns 
   */
  confirm(content: string): Promise<unknown> {
    return new Promise((resolve: Function, reject: Function) => {
      uni.showModal({
        title: '系统提示',
        content: content,
        cancelText: '取消',
        confirmText: '确定',
        success: function (res) {
          if (res.confirm) {
            resolve(res.confirm)
          }
        }
      })
    })
  },
  /**
   * 提示信息
   * @param option 提示内容或者提示框配置
   */
  showToast(option: string | object): void {
    if (typeof option === "object") {
      uni.showToast(option)
    } else {
      uni.showToast({
        title: option,
        icon: "none",
        duration: 2500
      })
    }
  },
  /**
   * 打开遮罩层，需要手动关闭遮罩层
   * @param content 遮罩层内容
   */
  loading(content: string): void {
    uni.showLoading({
      title: content
    })
  },
  /**
   * 关闭遮罩层
   */
  closeLoading(): void {
    uni.hideLoading()
  }
}
