# $download 对象

`$download` 对象用于文件下载，它定义在 `plugins/download.js` 文件中。

```js
const proxy = inject('proxy')
const name = 'ruoyi.xlsx'
const isDelete = true

// 默认下载方法
proxy.$download.name(name)

// 下载完成后是否删除文件
proxy.$download.name(name, isDelete)
```
