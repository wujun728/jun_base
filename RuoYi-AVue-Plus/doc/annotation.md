# 注解相关类文档

## AVue的基本数据结构

```json
// 根节点
{
    option : {  // 页面渲染配置项
    	column: [
           ... // 每个字段的渲染配置项
        ]
        ... // 表格的配置项
    } 
}
```

具体的格式参考 : 

[Crud/Form属性文档](https://avuejs.com/views/doc.html)

## 特殊的注解字段描述

### 类级别注解

#### @AVueRouteKey : 模版编号

负责定义一个组，前端根据这个组来确定模版，**单个服务中必须唯一否则会出现覆盖的情况**。

#### @AVueConfig : 后端路由配置

后端接口服务的配置路径，支持Spring的环境变量配置的表达式 -> `${xxx}`。

- list : 列表的服务访问路径
- save : 新增的服务请求路径

- update : 修改的服务请求路径
- del : 删除的服务请求路径

- messageField: 接口的消息提示字段名称
- successField : 代表服务返回成功的字段名称

- successKeyword: 根据返回字段的关键字做匹配来判断当前请求是否成功

- - 比如success:true为成功，那么successField = `success`，successKeyword = `true`，如果为false那么会显示`messageField`指定的提示字段。

#### @AVueTableOption : 表格查询配置

表格的参数

page开头: 代表分页请求的返回结果编排。

#### @AVueFromOption : 表单配置

表单的参数

- `successKeyword` : 增删改的接口返回成功的标识关键字



#### @AVuePage : 分页配置

分页的配置参数定义，需要与后台的接口返回做适配。

举例: 

```json
{
    "success":true,
    "errorCode":null,
    "message":null, 
    "pageModel":{ // pageRoot : 根路径
        "rowTotal":0,
        "pageSize":10, // pageSize : 每页大小
        "count":1,
        "total":1, // pageTotal : // 每页总数
        "beginIndex":0,
        "endIndex":0,
        "resultSet":[ // pageData : 每页的数据名称
            {
                "id":"HW1RIXwBi112tDv4JLJf",
                "ruleName":"所有项目通用规则",
                "project":"all",
                "title":"通用规则",
                "status":-1,
                "creator":"",
                "createDate":1632646538330,
                "updator":"",
                "updated":1632909056496
            } 
        ],
        "orderby":"",
        "totalRow":0
    }
}
```

#### @AVueEventButtons : 按钮事件定义

按钮事件组，通过指定确定布局的方式，去找对应的函数执行内容。执行方法是一个个模版，通过注解传递过去的参数来触发按钮的事件。

##### @AVueClickButton

- type : 按钮类型参考类型 : 参考 element-ui -> https://element.eleme.cn/#/zh-CN/component/button
- icon : 按钮标题

- btnName: 按钮名称
- fieldNames : 按钮参数

- attrExt： 拓展属性
- methodName: 方法名称

**定义在ruoyi-avue-ui/src/api/crud/event/rowClickEvent.js中的方法名称**

| 事件名称  | 事件参数        | 事件描述             |
| :-------- | --------------- | -------------------- |
| hrefClick | url : 跳转的URL | 点击跳转到特定的页面 |
| confirmClickRemoteApi | title : 确认的标题<br />url : 处理的后台地址<br />method: 接口的请求方法[post,get] 默认是post | 点击弹出确认框，确认之后出发一个后台接口调用 |
| openWindowJsonRemote | attrExt:{<br />submitUrl: "提交地址",<br />url: "弹窗内容渲染JSON,默认`/avue/crud`",<br />group: "路由的分组名称"} | 点击按钮，弹出一个弹窗，内容渲染按照递归的方式查找渲染JSON。 |

#### @AVueRule

数据校验规则

必填项等等...

#### @AVue组件类

##### @AVueInput : 文本框

##### @AVueSelect : 下拉框

##### @AVueCascader : 级联选择器

##### @AVueCheckbox : 复选框

##### @AVueDateRange : 时间范围选择

##### @AVueDatetime : 时间选择器

##### @AVueDynamic : 子表单

允许递归嵌套处理。

##### @AVueNumber : 数字组件

##### @AVueRadio : 单选框

##### @AVueRate : 评分组件

##### @AVueSlider : 滑块属性

##### @AVueSwitch : 开关组件

##### @AVueTextarea : 大文本框

##### @AVueTime : 时分秒组件

##### @AVueTimeRange : 时分秒范围

##### @AVueTree : 树形组件

##### @AVueUpload : 上传组件

#### [@AVueTable](./components/AVueTable.md) : 表格，支持详情页展示。允许嵌套的属性

