# $tab 对象

`$tab` 对象用于做页签操作，包括刷新页签、关闭页签、打开页签、修改页签等。它定义在 `src/plugins/tab.js` 文件中。

## 打开页签

```js
const proxy = inject('proxy')

// 打开页签
proxy.$tab.openPage('用户管理', '/system/user')

// 打开页签并执行回调
proxy.$tab.openPage('用户管理', '/system/user').then(() => {
  // 执行结束的逻辑
})
```

## 修改页签

```js
const proxy = inject('proxy')
const route = useRoute()

// 修改当前页签标题
const obj = Object.assign({}, { ...route }, { title: '自定义标题' })
proxy.$tab.updatePage(obj)

// 修改页签并执行回调
proxy.$tab.updatePage(obj).then(() => {
  // 执行结束的逻辑
})
```

## 关闭页签

```js
const proxy = inject('proxy')

// 关闭当前tab页签，打开新页签
const obj = { path: '/system/user' }
proxy.$tab.closeOpenPage(obj)

// 关闭当前页签，回到首页
proxy.$tab.closePage()

// 关闭指定页签
const obj = { path: '/system/user', name: 'User' }
proxy.$tab.closePage(obj)

// 关闭页签并执行回调
proxy.$tab.closePage(obj).then(() => {
  // 执行结束的逻辑
})
```

## 刷新页签

```js
const proxy = inject('proxy')

// 刷新当前页签
proxy.$tab.refreshPage()

// 刷新指定页签
const obj = { path: '/system/user', name: 'User' }
proxy.$tab.refreshPage(obj)

// 刷新页签并执行回调
proxy.$tab.refreshPage(obj).then(() => {
  // 执行结束的逻辑
})
```

## 关闭所有页签

```js
const proxy = inject('proxy')

// 关闭所有页签
proxy.$tab.closeAllPage()

// 关闭所有页签并执行回调
proxy.$tab.closeAllPage().then(() => {
  // 执行结束的逻辑
})
```

## 关闭左侧页签

```js
const proxy = inject('proxy')

// 关闭当前页签左侧的所有页签
proxy.$tab.closeLeftPage()

// 关闭指定页签左侧的所有页签
const obj = { path: '/system/user', name: 'User' }
proxy.$tab.closeLeftPage(obj)

// 关闭左侧页签并执行回调
proxy.$tab.closeLeftPage(obj).then(() => {
  // 执行结束的逻辑
})
```

## 关闭右侧页签

```js
const proxy = inject('proxy')

// 关闭当前页签右侧的所有页签
proxy.$tab.closeRightPage()

// 关闭指定页签右侧的所有页签
const obj = { path: '/system/user', name: 'User' }
proxy.$tab.closeRightPage(obj)

// 关闭右侧页签并执行回调
proxy.$tab.closeRightPage(obj).then(() => {
  // 执行结束的逻辑
})
```

## 关闭其他页签

```js
const proxy = inject('proxy')

// 关闭除当前页签外的所有页签
proxy.$tab.closeOtherPage()

// 关闭除指定页签外的所有页签
const obj = { path: '/system/user', name: 'User' }
proxy.$tab.closeOtherPage(obj)

// 关闭其他页签并执行回调
proxy.$tab.closeOtherPage(obj).then(() => {
  // 执行结束的逻辑
})
```
