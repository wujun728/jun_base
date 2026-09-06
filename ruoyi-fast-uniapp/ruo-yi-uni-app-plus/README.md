# RuoYi-UniApp-Plus

# 介绍
* 本框架兼容狮子大佬RuoYi-Cloud-Plus 2.x,RuoYi-Vue-Plus 5.x
* 本框架基于RuoYi-App框架二次修改，使用Uniapp+Vue3。
* 同时支持js或者ts。
* 同时支持css、scss、less
* 已经做了初步分包
* 已经引入uview-plus（好看的ui）
* 已经引入ucharts（方便的图表）
* 分别提供了uview-plus和ucharts的模板（可以直接使用或者用于学习）
* 里面还提供了作者自己封装的一些常用组件供大家选择

# 安装

一下三种方式均可，感觉速度 pnpm > yarn > cnpm > npm

***Vue3/Vite版要求 node 版本^14.18.0 || >=16.0.0***

```shell
npm install
cnpm install
yarn
pnpm install
```

# 启动

详见package.json的“scripts”，以下是案例：

```shell
:: 微信小程序编译运行
npm run dev:mp-weixin
```

成功后使用微信小程序打开：dist\dev\mp-weixin

### 代码压缩

默认是启动代码压缩的，

如果需要关闭代码压缩，可以将vite.config.js中的build.minify赋值为false。

如果需要打开代码压缩，可以将vite.config.js中的build.minify赋值为true。

# 内置组件(geek-xd)

1. 颜色选择器组件
2. 二维码组件
3. 圆形菜单组件
4. 常用的订单组件
5. 信息展示组件

# 插件的使用

#### tab - 页面插件

可以通过设置参数中的config.data来实现页面传参

| 方法         | 作用                                             | 参数        |
| ------------ | ------------------------------------------------ | ----------- |
| getData      | 可以拿到上个页面通过tab传递的参数                | 无          |
| reLaunch     | 关闭所有页面，打开到应用内的某个页面             | url、config |
| switchTab    | 跳转到 tabBar 页面，并关闭其他所有非 tabBar 页面 | url、config |
| redirectTo   | 关闭当前页面，跳转到应用内的某个页面             | url、config |
| navigateTo   | 保留当前页面，跳转到应用内的某个页面             | url、config |
| navigateBack | 关闭当前页面，返回上一页面或多级页面             | config      |

#### auth - 鉴权插件

下面所有方法返回值都是布尔值，permission代表权限字符串，role代表角色字符串，复数形式代表数组。

| 方法        | 作用                                       | 参数        |
| ----------- | ------------------------------------------ | ----------- |
| hasPermi    | 验证用户是否具备某权限                     | permission  |
| hasPermiOr  | 验证用户是否含有指定权限，只需包含其中一个 | permissions |
| hasPermiAnd | 验证用户是否含有指定权限，必须全部拥有     | permissions |
| hasRole     | 验证用户是否具备某角色                     | role        |
| hasRoleOr   | 验证用户是否含有指定角色，只需包含其中一个 | roles       |
| hasRoleAnd  | roles                                      | roles       |

#### modal - 弹窗插件

content是消息内容，option是详细配置。

| 方法         | 作用                           | 参数    |
| ------------ | ------------------------------ | ------- |
| msg          | 消息提示                       | content |
| msgError     | 错误消息                       | content |
| msgSuccess   | 成功消息                       | content |
| hideMsg      | 隐藏消息                       | 无      |
| alert        | 弹出提示                       | content |
| confirm      | 确认窗体                       | content |
| showToast    | 提示信息                       | option  |
| loading      | 打开遮罩层，需要手动关闭遮罩层 | content |
| closeLoading | 关闭遮罩层                     | 无      |

#### bus - 事件插件

eventName是事件名称，eventFun是事件处理函数，请尽量避免事件插件的使用，请在组件销毁是解绑素有该组件有关的事件，避免产生bug。

| 方法  | 作用         | 参数                |
| ----- | ------------ | ------------------- |
| $on   | 绑定一个事件 | eventName、eventFun |
| $off  | 解绑一个事件 | eventName           |
| $emit | 触发一个事件 | eventName、...args  |

#### socket

设置项enableUUID，是否启用基于uuid的消息处理机制，要求当发送的消息携带uuid字段时，返回的消息也要携带uuid字段。

设置项enableEvent，是否启用基于事件的消息处理机制，要求当希望被事件处理函数处理的消息需要携带event字段。

| 方法      | 作用                                                                                                                          | 参数                                                                                                                                   |
| --------- | ----------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------- |
| connect   | 连接websocke，当连接成功后触发回调函数                                                                                        | 最简单的用法就是传入{url:"ws://demo"}                                                                                                  |
| send      | 发送信息，当uuid不为空且不为false时，当收到携带相同uuid的消息时触发回调函数，只触发一次。否则由默认处理函数处理。 | msg消息内容，会被处理成json字符串uuid唯一标识符，可以手动传入，也可以设置为true当为true时会自动生成一个uuid并添加到msg中。 |
| close     | 关闭连接，会触发回调函数的内容，不会触发默认关闭事件的处理函数，也不会修改默认关闭事件的处理函数。                | 无                                                                                                                                     |
| on        | 监听事件，当收到携带event的消息时会调用回调函数。                                                                             | event事件的名称                                                                                                                        |
| off       | 取消监听事件                                                                                                                  |                                                                                                                                        |
| onMessage | 定义默认监听事件                                                                                                              | callback 默认监听事件的处理函数                                                                                                        |
| onError   | 定义异常事件                                                                                                                  | callback 默认异常事件的处理函数                                                                                                        |
| onClose   | 定义关闭事件                                                                                                                  | callback 默认关闭事件的处理函数                                                                                                        |

# 作者建议

### 对于选项式

```js
this.$tab // 建议使用this.$tab进行页面跳转，理由：便于在跳转前处理其他事务
this.$auth // 建议使用this.$auth进行鉴权操作
this.$modal // 建议使用this.$modal打开弹窗，理由：便于以后想要使用自定义弹窗
```

### 对于组合式

```js
import tab from '@/plugins/tab' // 建议使用tab进行页面跳转，理由：便于在跳转前处理其他事务
import auth from '@/plugins/auth' // 建议使用auth进行鉴权操作
import modal from '@/plugins/modal' // 建议使用modal打开弹窗，理由：便于以后想要使用自定义弹窗

// 也可以使用下面的方式
import { tab, auth, modal} from '@/plugins'
```

### 对于ucharts

建议即便暂时不使用图表也不要删除它，以后可能会用到。

# 压缩内存

(主包最低809kb左右)

### 删除geek组件

1. 删除pages_geek和components/geek-xd文件夹
2. 删除pages.json中subPackages的root值为“pages_geek/pages”的配置
3. 删除pages/template.config.js中geek组件

### 去除模板

（占用主包582kb左右）

1. 删除pages_template、pages_qiun两个文件夹
2. 删除pages.json中subPackages的root值为“pages_qiun/pages”、“pages_template/pages”的两个配置
3. 删除pages/template.config.js和pages/template.vue
4. 删除pages.json中subPackages的“tabBar”中的模板一项和“pages”中模板的一项
5. 删除static中的uview，里面都是示例图片。
6. 删除plugins中的config.js和common.js，并在plugins/index.js中删除相关配置

### 删除uchart

（占用主包175kb左右，不建议删除，以防以后会用）

1. 删除components/qiun-data-charts文件夹
2. 删除pages.json中的easycom下的custom中的"qiun-(.*)"的那一行
3. 删除pages/index中使用 “qiun-data-charts” 的部分

### 删除uview

（按需引入，没必要删除）

1. main.js或者main.ts中删除

```js
import uviewPlus from 'uview-plus'
// ......
app.use(uviewPlus)
```

2. uni.scss中删除 @import 'uview-plus/theme.scss';
3. App.vue中删除  @import '@/static/scss/index.scss';
4. package.json中删除    "clipboard": "^2.0.11","dayjs": "^1.11.9","uview-plus": "^3.1.36",

# 附录

[Vue3官网](https://cn.vuejs.org/)；
[uniapp官网](https://uniapp.dcloud.net.cn/)；
[uView-plus官网](https://uiadmin.net/uview-plus/)；
[uCharts官网](https://www.ucharts.cn/v2/#/)；
[RuoYi官网](http://ruoyi.vip/)；
