

**一款基于Java注解模版就能轻松完成复杂的增删改查的页面渲染以及事件交互，让后端小伙伴不在为了查询表单而头疼。**
# 项目更换: https://github.com/liukaixiong/spring-avue-plus
> 目前项目还是太重了,需要依赖的东西比较多,应用运行成本高,所以换了一个思路改用依赖的方式: https://github.com/liukaixiong/spring-avue-plus
## 功能介绍

- 基于注解驱动前端页面的渲染
  - 注解支持Spring的表达式
  - 支持静态枚举类、动态字典等等
  - 支持复杂表单的嵌套(页面嵌套页面)
  - 支持页面分组
  - 支持规则校验配置等等
- 支持通用按钮事件的定义
  - 行按钮
  - 左边栏按钮
  - 右边栏按钮
- 支持集成页面组件
  - 如json，默认input、select等等
- **ruoyi后台基础管理系统基础组件**
  - 用户管理：用户是系统操作者，该功能主要完成系统用户配置。
  - 部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
  - 岗位管理：配置系统用户所属担任职务。
  - 菜单管理：配置系统菜单，操作权限，按钮权限标识等。
  - 角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
  - 字典管理：对系统中经常使用的一些较为固定的数据进行维护。
  - 参数管理：对系统动态配置常用参数。
  - 通知公告：系统通知公告信息发布维护。
  - 操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
  - 登录日志：系统登录日志记录查询包含登录异常。
  - 在线用户：当前系统中活跃用户状态监控。
  - 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
  - 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
  - 系统接口：根据业务代码自动生成相关的api接口文档。
  - 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
  - 缓存监控：对系统的缓存查询，删除、清空等操作。
  - 在线构建器：拖动表单元素生成相应的HTML代码。
  - 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。

## 文档

[项目背景及应用场景介绍](./doc/project_background.md)

为了提升阅读体验可以观看全局文档:

[全局文档点击观看](https://www.yuque.com/docs/share/912323d1-644e-44c7-8488-4bf7e1583fba)

[全局文档点击观看](https://www.yuque.com/docs/share/912323d1-644e-44c7-8488-4bf7e1583fba)

[全局文档点击观看](https://www.yuque.com/docs/share/912323d1-644e-44c7-8488-4bf7e1583fba)

使用前稍微了解下:

- [AVue的组件官网](https://avuejs.com/form/form-event.html) : 大部分注解都是基于AVue的属性设计而来。
- [ruoyi的文档地址](http://doc.ruoyi.vip/) : 针对一些部署情况，如果是后端同学只需要稍微了解nodejs的情况。
- 相对于`ruoyi`的[改动文档](./doc/ruoyi.md)

## 如何使用

[1. 环境搭建以及项目引入以及效果展示](./doc/introduction.md)

[2. 前端ruoyi-avue-ui环境搭建](./ruoyi-avue-ui/README.md)

## 代码结构

[ruoyi-avue-client 相关](./ruoyi-avue-client/README.md)

[注解相关的介绍](./doc/annotation.md)

## 展示图

### 1. 操作介绍

负责展示了各模块的关系

![image-20211029110727955](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/image-20211029110727955.png)

### 2. 模版案例

```java
// 模版对应的编号
@AVueRouteKey(groupKey = "test-route")
// 表格的标题
@AVueTableOption(title = "这是一个测试", dialogDrag = true)
// 适配后台服务的对应的处理接口
@AVueConfig(list = AVueControllerTest.LIST_URL, update = AVueControllerTest.UPDATE_URL, save = AVueControllerTest.UPDATE_URL, successKeyword = "true", successField = "success", messageField = "message")
public class AVueCrudModel {
    /**
     * 注解介绍
     *
     * `@AVueInput`: 组件类型以@AVue开始，后面是具体的组件。
     * - input  ： 代表文本框
     * - select ： 代表选择框
     * - number ： 代表数字框
     * - json   ： 代表json组件
     * <p>
     * 属性介绍：
     * addDisplay       ： 表示新增的时候是否展示
     * editDisabled     ： 表示修改的时候是否不可编辑
     * search           ： 代表列表页是否为搜索条件
     * searchRequired   ： 代表搜索条件是否为必填
     * dicData          ： 代表枚举字典
     * dicUrl           ： 代表后台拉取对象
     * 具体属性释义可参考 :  https://avuejs.com/views/doc.html
     * 注解文档参考 : https://gitee.com/liukaixiong/RuoYi-AVue-Plus/blob/master/doc/annotation.md
     */
    @AVueInput(prop = "id", label = "主键", addDisplay = false, editDisabled = true, search = true)
    private String id;

    @AVueInput(prop = "username", label = "用户名称", search = true)
    private String username;

    @AVueSelect(prop = "checkStatus", label = "认证状态", dicData = "CheckStatusEnums", search = true)
    private String checkStatus;

    @AVueSelect(prop = "likeStar", label = "喜欢明星", dicData = "test-likeStar-map")
    private Integer likeStar;

    @AVueNumber(prop = "age", label = "年龄", labelTip = "这是选择年龄的地方")
    private Integer age;

    @AVueRadio(prop = "sex", label = "性别", border = true, dicData = "SexEnums")
    private int sex;

    @AVueDatetime(prop = "validDate", label = "有效时间")
    private Date validDate;

    @AVueTime(prop = "time", label = "时分秒选择")
    private Date time;

    @AVueTimeRange(prop = "timeRange", label = "时分秒范围")
    private Date timeRange;

    @AVueDateRange(prop = "dateRange", rangeSeparator = "-", label = "日期范围", search = true, valueFormat = "yyyy年MM月dd日", format = "yyyy-MM-dd")
    private String dateRange;

    @AVueCheckbox(prop = "interest", label = "兴趣爱好", dicData = "InterestEnums")
    private List<String> interest;

    @AVueSwitch(prop = "status", label = "状态", dicData = "StatusEnums")
    private int status;

    @AVueDynamic(prop = "simpleModel", label = "子表单测试")
    private AVueSimpleModel simpleModel;

    @AVueGroup(prop = "groupModel", label = "分组测试1")
    private AVueNodeModel groupModel;

    @AVueGroup(prop = "groupModel2", label = "分组测试2")
    private AVueNodeModel groupModel2;
    // 省略get/set .... 其实如果只作为模版的话不需要getset.
}
```

[相关案例模版，后续会继续补充](./doc/templateCase.md)


### 3. 页面展示

#### 1. 列表页效果

![](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/rq10ne.png)

#### 2. 新增页面效果

![](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/q8y6ja.png)

#### 3. 修改效果

![修改](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/image-20210820171255314.png)


### 动态效果

![新增](ruoyi-avue-client/README.assets/6d4ftd.gif)

![新增](ruoyi-avue-client/README.assets/xyju9p.gif)

## 特别感谢

| 作者主页                                     | 框架     |
| -------------------------------------------- | -------- |
| [smallweigit](https://gitee.com/smallweigit) | AVue     |
| [**若依**](https://gitee.com/y_project)      | 若依系统 |
|                                              |          |

**如果你有比较好的想法或者建议请加QQ : 444368875 或者发送邮件 444368875@qq.com**

**希望大家多多支持，我会时刻关注并且加以改进。**

