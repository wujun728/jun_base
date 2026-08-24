import { Message, MessageBox, Alert, Notification } from 'element-ui'
class msgTips {
  static getInstance() {
    return new msgTips()
  }
  // 消息提示
  msg(msg) {
    Message.info({
      message: msg,
      duration: 5000
    })
  }

  // 错误消息
  msgError(msg) {
    Message.error({
      message: msg,
      duration: 5000
    })
  }

  // 成功消息
  msgSuccess(msg) {
    Message.success({
      message: msg,
      duration: 5000
    })
  }

  // 警告消息
  msgWarning(msg) {
    Message.warning({
      message: msg,
      duration: 5000
    })
  }

  // 弹出提示
  alert(msg) {
    Alert(msg, '系统提示')
  }

  // 通知提示
  notify(msg) {
    Notification.info(msg)
  }

  // 错误通知
  notifyError(msg) {
    Notification.error(msg)
  }

  // 成功通知
  notifySuccess(msg) {
    Notification.success(msg)
  }

  // 警告通知
  notifyWarning(msg) {
    Notification.warning(msg)
  }

  // 确认窗体
  confirm(options) {
    return new Promise((resolve, reject) => {
      MessageBox.confirm(options.message, options.title || '温馨提示', {
        confirmButtonText: options.confirmButtonText || '确定',
        confirmButtonClass: options.confirmButtonClass || '',
        cancelButtonText: options.cancelButtonText || '取消',
        cancelButtonClass: options.cancelButtonClass || '',
        type: options.type || 'warning'
      })
        .then(() => {
          resolve()
        })
        .catch(() => {
          console.log(options.cancelButtonText || '取消')
        })
    })
  }

  // 提交内容
  // prompt(content: string, title: string, options?: ElMessageBoxOptions) {
  //   return this.$prompt(content, title, {
  //     confirmButtonText: '确定',
  //     cancelButtonText: '取消',
  //     ...options
  //   })
  // }
}

const Tips = msgTips.getInstance()
export default Tips
