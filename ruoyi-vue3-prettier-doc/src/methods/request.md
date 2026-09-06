# 请求方法

## request

项目封装了基于 Axios 的请求方法，支持自动处理 token、请求/响应拦截以及错误处理等功能。

## GET

```js
import request from '@/utils/hsj/service'

// GET 请求
request.get({
  url: '/api/data',
  params: {},
  interceptors: {
    // 请求拦截
    requestInterceptor(config) {
      return config
    },
    // 响应拦截
    responseInterceptor(data) {
      return data
    },
  },
})
```

## POST

```js
// POST 请求
request.post({
  url: '/api/data',
  params: {},
  interceptors: {
    // 请求拦截
    requestInterceptor(config) {
      return config
    },
    // 响应拦截
    responseInterceptor(data) {
      return data
    },
  },
})
```

## PUT

```js
// PUT 请求
request.put({
  url: '/api/data',
  params: {},
  interceptors: {
    // 请求拦截
    requestInterceptor(config) {
      return config
    },
    // 响应拦截
    responseInterceptor(data) {
      return data
    },
  },
})
```

## DELETE

```js
// DELETE 请求
request.delete({
  url: '/api/data/1',
  params: {},
  interceptors: {
    // 请求拦截
    requestInterceptor(config) {
      return config
    },
    // 响应拦截
    responseInterceptor(data) {
      return data
    },
  },
})
```
