
# 测试相关介绍

## 单个实体进行测试
`com.ruoyi.client.handler.AVueAnnotationHandlerTest` : 可针对单个实体进行测试得到JSON字符串.

## 整体测试
`com.ruoyi.client.RuoYiClientTestApplication` : 针对整个client配置进行测试

### 相关的样例实体

test目录下 : `com.ruoyi.client.model.AVueCrudModel`

```java
@AVueRouteKey(groupKey = "test-route")
@AVueTableOption(title = "这是一个测试", dialogDrag = true)
@AVueConfig(list = AVueControllerTest.LIST_URL, update = AVueControllerTest.UPDATE_URL, save = AVueControllerTest.UPDATE_URL)
public class AVueCrudModel {

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

    @AVueTimeRange(prop = "timeRange", label = "时分秒范围", rangeSeparator = "-")
    private String timeRange;

    @AVueDateRange(prop = "dateRange", rangeSeparator = "-", label = "日期范围", search = true, valueFormat = "yyyy-MM-dd", format = "yyyy-MM-dd")
    private List<Date> dateRange;

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

}
```

## 演示注意事项

### 1. 注册地址

测试用例中的端口为`8765` ， 请确认前端菜单栏中

-> [系统管理-服务注册] -> 是否注册有测试服务的地址

### 2. 注册菜单

1. 确认是否有菜单栏出现，比如案例脚本中会涵盖`测试案例`的模块。如果没有请到项目根目录下找到`sql`目录下的server.sql的脚本内有菜单栏的SQL执行.
2. 查看菜单栏中的路由地址: `service?server=1&&group=test-route` 

### 3. 授权字样

`RuoYi-AVue-Plus\ruoyi-avue-client\src\test\resources\application.yml` 

```yaml
spring:
  avue:
    debug: true
    accept-token: abc12345
```

请查看注册服务节点是否`accept-token`值一致。

