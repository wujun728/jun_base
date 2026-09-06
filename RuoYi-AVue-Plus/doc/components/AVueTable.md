## 主表格的属性
```java
@AVueRouteKey(groupKey = "sandbox-heartbeat-platform")
// 将修改、删除按钮隐藏，并打开详情按钮，改名为查看详情
@AVueTableOption(title = "沙箱在线应用管理", dialogDrag = true, editBtn = false, delBtn = false, viewBtn = true, viewBtnText = "查看详情")
// 下面是该模版对应的数据接口地址
@AVueConfig(list = HeartbeatController.HEARTBEAT_LIST_URL, successField = "success", successKeyword = "true", messageField = "message")
public class HeartbeatTemplate {

    @AVueInput(prop = "appName", label = "应用名称" )
    private String appName;
    @AVueInput(prop = "ip", label = "应用ip")
    private String ip;
    @AVueInput(prop = "port", label = "应用端口")
    private String port;
    @AVueInput(prop = "pid", label = "进程号")
    private String pid;
    @AVueInput(prop = "environment", label = "环境")
    private String environment;
    @AVueInput(prop = "mode", label = "启动方式")
    private String mode;
    @AVueInput(prop = "isEnableUnsafe", label = "是否开启安全模式")
    private String isEnableUnsafe;
    @AVueInput(prop = "namespace", label = "工作空间名称")
    private String namespace;
    @AVueInput(prop = "version", label = "版本号",row = true)
    private String version;
    @AVueInput(prop = "status", label = "状态")
    private String status;
    
    // 这里非常关键，希望详情里面嵌套一个表格
    @AVueTable(prop = "moduleList", label = "加载插件", hide = true,width = "1000px",row = true,span = 24)
    private ModuleInfoTemplate moduleList;
    
}
```

## 子表格的渲染属性

```java
@AVueTableOption(span = 24, addBtn = false, delBtn = false, editBtn = false)
// 设置后台接口调用之后成功或者失败的结构模型
@AVueEventButtons(
        // 每一行的按钮及事件定义
        tableRowButtons = {
                // 指定方法名称，还有按钮名称
                @AVueClickButton(methodName = "testA", btnName = "测试按钮A"),
                @AVueClickButton(methodName = "testB", btnName = "测试按钮B"),
        }
)
public class ModuleInfoTemplate {
    @AVueInput(prop = "id", label = "插件编号", row = true)
    private String id;
    @AVueSwitch(prop = "isLoaded", label = "是否加载", row = true)
    private boolean isLoaded;
    @AVueSwitch(prop = "isActivated", label = "是否开启", row = true)
    private boolean isActivated;
    @AVueInput(prop = "version", label = "版本号", row = true)
    private String version;
    @AVueInput(prop = "author", label = "作者", row = true)
    private String author;
}
```

主表格为一个正常查询入口，希望点击查询的时候，里面嵌套了一个子表格。



## 页面填充的数据结构

```json
{
    "success":true,
    "data":[
        {
            "appName":"z-demo",
            "ip":"172.19.189.160",
            "port":"39651",
            "pid":"27697",
            "mode":"ATTACH",
            "environment":"test",
            "moduleList":[
                {
                    "id":"online-manager-module",
                    "isLoaded":true,
                    "isActivated":true,
                    "version":"0.0.2",
                    "author":"luanjia@taobao.com"
                },
                {
                    "id":"sandbox-info",
                    "isLoaded":true,
                    "isActivated":true,
                    "version":"0.0.4",
                    "author":"luanjia@taobao.com"
                },
                {
                    "id":"sandbox-module-mgr",
                    "isLoaded":true,
                    "isActivated":true,
                    "version":"0.0.2",
                    "author":"luanjia@taobao.com"
                },
                {
                    "id":"sandbox-control",
                    "isLoaded":true,
                    "isActivated":true,
                    "version":"0.0.3",
                    "author":"luanjia@taobao.com"
                }
            ],
            "isEnableUnsafe":"true",
            "namespace":"default",
            "version":"1.3.3",
            "status":"ACTIVE"
        }
    ],
    "message":null
}
```



## 页面效果

### 主表格入口

![img](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/1636438595940-550a2a21-7696-4493-9aae-cfe574023381.png)

### 查看详情

![img](https://cdn.nlark.com/yuque/0/2021/png/22279048/1636438640919-5a5fa9d7-d8c1-4983-9175-442616fdc274.png)

加载插件部分就是子列表。



## 源码实现



### 后端

#### 1. 嵌套功能

由于`@AVueTable`是自定义的实现，我们需要它支持属性嵌套。



属性嵌套的实现在`com.ruoyi.client.handler.impl.SupportNestingAnnotationHandler#handler`中



在`com.ruoyi.client.config.AVueAutoConfiguration#dynamicAnnotationHandler`被实例化的时候，确定哪些注解需要被嵌套支持。

- 相当于注册到该处理器中，遇到该注解就按照嵌套的处理。



#### 2. 自定义的组件

面对后端自定义的注解，需要特殊标记出来，让前端根据逻辑找到对应的渲染对象。



`com.ruoyi.client.handler.impl.NewImportComponentsHandler`会将自定义的注解的属性值单独注册到一个`registerFieldComponents`列表中，该列表会表示都是需要特殊处理的属性名称



它的初始化时机在`com.ruoyi.client.config.AVueAutoConfiguration#newImportComponentsHandler`中



### 前端

模版代码还是在`ruoyi-avue-ui\src\views\crud\server-crud.vue`通用模版中，在属性渲染的时候会遍历每个属性，如果属性的type类型是table的，默认初始化该组件。

```xml
<span v-if="column.type === 'table'">
  <avue-crud ref="crud" :option="column.children" :data="value || []" v-on="onEvent" v-bind="bindVal">
    <template is="column.children.tableRowButtons && column.children.tableRowButtons.length > 0" slot-scope="{row,index}" slot="menu">
      <span v-for="nodeItem in column.children.tableRowButtons">
        <el-button :type="nodeItem.type || 'text'"
                   :icon="nodeItem.icon || 'el-icon-edit'"
                   :size="nodeItem.size || 'mini'"
                   plain
                   @click="btnClick(nodeItem,row,index)">{{ nodeItem.btnName }}
        </el-button>
      </span>
    </template>
  </avue-crud>
</span>
```