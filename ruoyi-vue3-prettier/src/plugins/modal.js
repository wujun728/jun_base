import {
  ElMessage,
  ElMessageBox,
  ElNotification,
  ElLoading,
} from 'element-plus'

let loadingInstance

export default {
  // 消息提示
  msg(content) {
    return ElMessage.info(content)
  },
  // 错误消息
  msgError(content) {
    return ElMessage.error(content)
  },
  // 成功消息
  msgSuccess(content) {
    return ElMessage.success(content)
  },
  // 警告消息
  msgWarning(content) {
    return ElMessage.warning(content)
  },
  // 弹出提示
  alert(content, title = '系统提示', config = {}) {
    return ElMessageBox.alert(content, title, { type: 'info', ...config })
  },
  // 错误提示
  alertError(content, title = '系统提示', config = {}) {
    return ElMessageBox.alert(content, title, { type: 'error', ...config })
  },
  // 成功提示
  alertSuccess(content, title = '系统提示', config = {}) {
    return ElMessageBox.alert(content, title, { type: 'success', ...config })
  },
  // 警告提示
  alertWarning(content, title = '系统提示', config = {}) {
    return ElMessageBox.alert(content, title, { type: 'warning', ...config })
  },
  // 通知提示
  notify(content) {
    return ElNotification.info(content)
  },
  // 错误通知
  notifyError(content) {
    return ElNotification.error(content)
  },
  // 成功通知
  notifySuccess(content) {
    return ElNotification.success(content)
  },
  // 警告通知
  notifyWarning(content) {
    return ElNotification.warning(content)
  },
  // 确认窗体
  confirm(content, title = '系统提示', config = {}) {
    return ElMessageBox.confirm(content, title, {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      ...config,
    })
  },
  // 提交内容
  prompt(content, title = '系统提示', config = {}) {
    return ElMessageBox.prompt(content, title, {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      ...config,
    })
  },
  // 打开遮罩层
  loading(content) {
    loadingInstance = ElLoading.service({
      lock: true,
      text: content,
      background: 'rgba(0, 0, 0, 0.7)',
    })
  },
  // 关闭遮罩层
  closeLoading() {
    loadingInstance.close()
  },
}
