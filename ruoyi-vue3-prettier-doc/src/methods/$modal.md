# $modal 对象

`$modal` 对象用于做消息提示、通知提示、对话框提醒、二次确认、遮罩等。它定义在 `src/plugins/modal.js` 文件中。

## 消息提示

可以直接传入字符串，也可以传入对象，属性参考 element-plus 的 Message 组件。

```js
const proxy = inject('proxy')

// 默认提示
proxy.$modal.msg('默认反馈')

// 错误提示
proxy.$modal.msgError('错误反馈')

// 成功提示
proxy.$modal.msgSuccess('成功反馈')

// 警告提示
proxy.$modal.msgWarning('警告反馈')

// 传入配置对象
proxy.$modal.msg({
  plain: true,
  message: '你好! mw',
})
```

## 对话框提示

第一个参数是提示内容，第二个参数是提示标题，第三个参数是配置对象，可以参考 element-plus 的 Alert 组件。

```js
const proxy = inject('proxy')

// 默认提示
proxy.$modal.alert('默认提示', '系统提示', {
  closable: true,
})

// 错误提示
proxy.$modal.alertError('错误提示', 'ruoyi提示', {
  closable: false,
  center: true,
})

// 成功提示
proxy.$modal.alertSuccess('成功提示')

// 警告提示
proxy.$modal.alertWarning('警告提示')
```

## 通知提示

可以直接传入字符串，也可以传入对象，属性参考 element-plus 的 Notification 组件。

```js
const proxy = inject('proxy')

// 默认通知
proxy.$modal.notify('默认通知')

// 错误通知
proxy.$modal.notifyError('错误通知')

// 成功通知
proxy.$modal.notifySuccess('成功通知')

// 警告通知
proxy.$modal.notifyWarning('警告通知')
```

## 确认对话框

```js
const proxy = inject('proxy')

proxy.$modal
  .confirm('确认信息')
  .then(function () {
    // 确认后的操作
  })
  .then(() => {
    // 后续操作
  })
  .catch(() => {
    // 取消操作
  })
```

## 遮罩层

```js
const proxy = inject('proxy')

// 打开遮罩层
proxy.$modal.loading('正在导出数据，请稍后...')

// 关闭遮罩层
proxy.$modal.closeLoading()
```
