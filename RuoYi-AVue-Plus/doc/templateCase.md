# 模版实战案例

## 1. 简单基础模版

模版来源 : `ruoyi-avue-client > test > com/ruoyi/client/model/AVueSimpleModel.java` 

- 组编号
- 表单配置项
- 后台服务项
- 基础组件

```java
@AVueRouteKey(groupKey = "test-config")
@AVueTableOption(title = "这是一个测试")
@AVueConfig(list = AVueConfigControllerTest.LIST_URL, update = AVueConfigControllerTest.UPDATE_URL, save = AVueConfigControllerTest.UPDATE_URL, successKeyword = "true", successField = "success", messageField = "message")
public class AVueSimpleModel {

    @AVueInput(prop = "id", label = "主键", addDisplay = false, row = true, editDisabled = true, search = true)
    private String id;

    @AVueInput(prop = "configGroup", label = "组名称", search = true, row = true, rules = {
        @AVueRule(required = true, message = "组名称要填咧"), @AVueRule(min = 5, max = 10, message = "我跟你讲最小5个,最大10个.")})
    private String configGroup;

    @AVueInput(prop = "configName", label = "配置名称", search = true, required = true, row = true)
    private String configName;

    @AVueSelect(prop = "configCode", label = "配置值", dicData = "StatusEnums", search = true, required = true, row = true)
    private String configCode;

    @AVueNumber(prop = "validDay", label = "有效天数", search = true, required = true, row = true)
    private Integer validDay;

    @AVueSwitch(prop = "status", label = "状态", dicData = "StatusEnums", row = true)
    private int status; 
     
}

```

非常基础的模版，没有复杂的组件，但是大部分都是必须的.基于以上模版得到的结果: 

![image-20211029144730914](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/image-20211029144730914.png)

![image-20211029144832043](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/image-20211029144832043.png)

![image-20211029144845206](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/image-20211029144845206.png)

## 2. 复杂模版

模版来源 : `ruoyi-avue-client > test > com/ruoyi/client/model/AVueCrudModel.java:27` 

​	里面包含了特殊按钮、特殊组件、表单嵌套等等

```java
// 模版对应的编号
@AVueRouteKey(groupKey = "test-route")
// 表格的标题
@AVueTableOption(title = "这是一个复杂的模版", dialogDrag = true)
// 适配后台服务的对应的处理接口
@AVueConfig(list = AVueControllerTest.LIST_URL, update = AVueControllerTest.UPDATE_URL, save = AVueControllerTest.UPDATE_URL, successKeyword = "true", successField = "success", messageField = "message")
// 设置后台接口调用之后成功或者失败的结构模型
@AVueEventButtons(
        // 每一行的按钮及事件定义
        tableRowButtons = {
                // 指定方法名称，还有按钮名称
                @AVueClickButton(methodName = "testA", btnName = "测试按钮A"),
                @AVueClickButton(methodName = "testB", btnName = "测试按钮B"),
                // 指定方法名称按钮事件名称
                @AVueClickButton(methodName = "confirmClickRemoteApi", btnName = "确认按钮", attrExt = {
                        @AVueAttr(name = "title", value = "小伙子，你确定吗？有惊喜喔!"),
                        @AVueAttr(name = "url", value = AVueControllerTest.BODY_URL)}),
                // 指定事件
                @AVueClickButton(type = "success", btnName = "弹层按钮测试", methodName = "openWindowJsonRemote", attrExt = {
                        // 当前弹层的提交路径
                        @AVueAttr(name = "submitUrl", value = AVueControllerTest.BODY_URL),
                        // 找下一个模版
                        @AVueAttr(name = "group", value = "test-config")})
        },
        // 左上角按钮事件
        tableTopLeftButtons = {
                @AVueClickButton(methodName = "hrefClick", btnName = "跳转链接", type = "success", icon = "el-icon-setting", attrExt = {
                        @AVueAttr(name = "url", value = "https://www.baidu.com")})
        }
)
public class AVueCrudModel {

    /**
     * 注解介绍
     *
     * @AVueInput: 组件类型以@AVue开始，后面是具体的组件。
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

    @AVueInput(prop = "username", label = "用户名称", search = true, searchRequired = true, onClick = "testB")
    private String username;

    @AVueSelect(prop = "checkStatus", label = "认证状态", dicData = "CheckStatusEnums", search = true)
    private String checkStatus = CheckStatusEnums.FOOTBALL.getCode().toString();

    @AVueSelect(prop = "likeStar", label = "喜欢明星", dicData = "test-likeStar-map", dicUrl = "/liukx")
    private Integer likeStar;

    @AVueNumber(prop = "age", label = "年龄", labelTip = "这是选择年龄的地方")
    private Integer age = 18;

    @AVueRadio(prop = "sex", label = "性别", border = true, dicData = "SexEnums")
    private int sex = (int) SexEnums.UNKNOWN.getCode();

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

    @AVueUpload(prop = "image", label = "单图上传", listType = "picture-img", action = "/upload", propsHttp = @AVueUploadPropsHttp(res = "single", name = "fileName"))
    private String image;

    @AVueUpload(prop = "imageList", dataType = "array", listType = "picture-card", label = "单图上传", action = "/upload", propsHttp = @AVueUploadPropsHttp(res = "single", name = "fileName"))
    private List<String> imageList;

    @AVueJson(prop = "dataJson", label = "拓展字段")
    private String dataJson;

    /**
     * 支持模版嵌套
     */
    @AVueDynamic(prop = "simpleModel", label = "子表单测试")
    private AVueSimpleModel simpleModel;

    /**
     * 支持分组类型
     */
    @AVueGroup(prop = "groupModel", label = "分组测试1")
    private AVueNodeModel groupModel;

    @AVueGroup(prop = "groupModel2", label = "分组测试2")
    private AVueNodeModel groupModel2;

}

```

得到效果

![image-20211029145437212](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/image-20211029145437212.png)

下面是修改按照两张图截全的。

![image-20211029150239178](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/image-20211029150239178.png)

![image-20211029150252017](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/image-20211029150252017.png)