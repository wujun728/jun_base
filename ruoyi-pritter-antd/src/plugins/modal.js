import { h, ref } from 'vue'
import { Input } from 'ant-design-vue'
import { message, notification, Modal } from 'ant-design-vue'

let loadingInstance

function toNotifyOptions(content) {
  return typeof content === 'string' ? { message: content } : content
}

function createAlert(type) {
  return (content, title = '系统提示', config = {}) => {
    const modalFn = Modal[type] || Modal.info
    return new Promise((resolve) => {
      modalFn({
        title,
        content,
        onOk: () => resolve(),
        ...config,
      })
    })
  }
}

export default {
  // 消息提示
  msg(content) {
    return message.info(content)
  },
  // 错误消息
  msgError(content) {
    return message.error(content)
  },
  // 成功消息
  msgSuccess(content) {
    return message.success(content)
  },
  // 警告消息
  msgWarning(content) {
    return message.warning(content)
  },
  // 弹出提示
  alert: createAlert('info'),
  // 错误提示
  alertError: createAlert('error'),
  // 成功提示
  alertSuccess: createAlert('success'),
  // 警告提示
  alertWarning: createAlert('warning'),
  // 通知提示
  notify(content) {
    return notification.info(toNotifyOptions(content))
  },
  // 错误通知
  notifyError(content) {
    return notification.error(toNotifyOptions(content))
  },
  // 成功通知
  notifySuccess(content) {
    return notification.success(toNotifyOptions(content))
  },
  // 警告通知
  notifyWarning(content) {
    return notification.warning(toNotifyOptions(content))
  },
  // 确认窗体
  confirm(content, title = '系统提示', config = {}) {
    const { confirmButtonText, cancelButtonText, ...rest } = config
    return new Promise((resolve, reject) => {
      Modal.confirm({
        title,
        content,
        okText: confirmButtonText || '确定',
        cancelText: cancelButtonText || '取消',
        onOk: () => resolve(),
        onCancel: () => reject('cancel'),
        ...rest,
      })
    })
  },
  // 提交内容
  prompt(content, title = '系统提示', config = {}) {
    const { confirmButtonText, cancelButtonText, inputValue, ...rest } = config
    const value = ref(inputValue ?? '')
    return new Promise((resolve, reject) => {
      Modal.confirm({
        title,
        content: () =>
          h('div', [
            h('p', { style: 'margin-bottom: 8px' }, content),
            h(Input, {
              value: value.value,
              placeholder: rest.inputPlaceholder,
              'onUpdate:value': (val) => {
                value.value = val
              },
            }),
          ]),
        okText: confirmButtonText || '确定',
        cancelText: cancelButtonText || '取消',
        onOk: () => resolve({ value: value.value }),
        onCancel: () => reject('cancel'),
        ...rest,
      })
    })
  },
  // 打开遮罩层
  loading(content) {
    loadingInstance = message.loading(content || '加载中...', 0)
  },
  // 关闭遮罩层
  closeLoading() {
    loadingInstance?.()
    loadingInstance = null
  },
}
