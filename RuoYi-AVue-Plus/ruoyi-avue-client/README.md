# ruoyi-avue-client

[服务端测试文档](./src/test/README.md)

## 核心主流程讲解  

>  建议下载源码的时候到`ruoyi-client`的test目录下RuoYiClientTestApplication启动测试应用

同时建议将模版和字典独立出来维护。

核心主流程:

`com.ruoyi.client.handler.AVueAnnotationHandler#parse`

AVueAnnotationHandler.parse:

- com.ruoyi.client.helper.HandlerHelper#handler
  - com.ruoyi.client.handler.AVueLevelHandler#preHandler
  - com.ruoyi.client.handler.AVueHandler#handler
  - com.ruoyi.client.helper.AttrPostProcessHelper#handler 

>  先处理类上面注解，然后遍历字段上面的注解，中间会有一些拦截拓展。可以根据自己的需要进行开发



### 核心流程图

![avue-process](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/avue-process.png)

## 相关配置属性介绍

```yaml
spring:
  avue:
    debug: true # 开发模式， 将不会启用缓存，方便在调试模型的时候，不用重启应用。IDEA->菜单栏->Build -> builder项目就好了, 默认fasle
    show-default-value: false # 是否显示注解内的默认值。默认false  ， 默认值也会展示
    accept-token: abc12345 # 服务访问授权的token
```

### 1. 字典相关

#### 1.1 枚举字典

启用注解功能会让你把字典枚举类回填下来:

```java
@EnableAVue(enumsPackages = "com.ruoyi.client.enums")
```

这里面的枚举类只需要实现`DicEnumData` 接口回填`code`和`label`就好了.然后注解的`dicData`字段加上枚举类的名称就好了.

```java
@AVueSelect(prop = "checkStatus", label = "认证状态", dicData = "CheckStatusEnums", search = true)
```

```java
public enum CheckStatusEnums implements DicEnumData {
    BASKETBALL("1", "已认证"),
    FOOTBALL("0", "未认证"),
    BADMINTON("-99", "已失效"),
    BILLIARDS("-1", "已拒绝");

    private String code;
    private String label;

    CheckStatusEnums(String code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public Object getCode() {
        return code;
    }

    @Override
    public String getLabel() {
        return label;
    }

}
```

#### 1.2 动态字典

需要从数据库拉取或者缓存中拉取的.

`DictionaryDataCallback` 实现该接口

`callName` : 为`dicData`的值

```java
@Component
public class TestInterestProcessor implements DictionaryDataCallback<Map<String, Object>> {
    @Override
    public String callName() {
        return "test-interest";
    }
    
    @Override
    public PropsModel props() {
        // 告诉字典你要以哪个属性名称作为value和label
        // 默认的话就是value和label
        return new PropsModel("code", "label");
    }
    
    @Override
    public List<Map<String, Object>> call(AVueAttrLevel level, AnnotatedElement element, Map<String, Object> attrMap) {
        // 模拟数据
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(mock("001", "周杰伦"));
        list.add(mock("002", "林俊杰"));
        list.add(mock("003", "王力宏"));
        list.add(mock("004", "许嵩"));
        return list;
    }

    private Map<String, Object> mock(String code, String label) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("label", label);
        return map;
    }
}
```

`DictionaryDataCallback<Map<String, Object>> `可以是对象可以是Map可以自己定义，如果是Model

则接口为`DictionaryDataCallback<UserModel>` 等等...

### 2. 自定义注解

##### AVueHandler

默认的实现可以参考 : `com.ruoyi.client.handler.impl.DefaultAnnotationHandler`

被`@EnableAVue`扫描到的包的注解都会经过这些handler处理，如果你有需求自定义的注解的话，可以实现该类来处理注解的逻辑。

#### 3. 属性后置增强

如果你的某个属性需要做后置处理，好比字典相关，你想根据某个属性的值来触发后续的回调，补充属性。

##### AVueAttrPostProcess

这个执行器会在每个字段处理完成的时候回调.

```java
public interface AVueAttrPostProcess {
    public void attrPostProcess(AVueAttrLevel level, AnnotatedElement element, Map<String, Object> attrMap);
}
```

**attrMap** : 代表该属性被解析完成了之后的回调。

具体还可以参考已经实现了的`com.ruoyi.client.processor.DicDataProcess` : 针对字典的增强

### 3. 拓展类

#### 全局特定注解的默认属性值

由于注解内部定义的字段默认情况是不输出默认值的，怕属性太多引起混乱，仅仅是用户自己指定的属性回填的内容会被输出出来。

为了避免用户对某些注解的默认值频繁填写相同的属性，允许用户定义全局特定注解的默认值，把默认值先注册之后，每次输出都会将默认值回填到json中，当然优先级还是以类中定义的注解内容为最高：

**另外AVue中没有集成进来的属性，也可以作为默认值回填输出。**

`类中注解 > 全局默认注解 > 注解的默认值` 

如果类中没有回填该属性，则看全局默认注解是否配置。

```java
// 可以在容器触发完成之后，将对应的属性注册到容器中
Map<String, Object> uploadValue = new LinkedHashMap<>();
uploadValue.put("drag", true);
ObjectConfigCacheHelper.getINSTANCE().registerAnnotationValue(AVueUpload.class, uploadValue);
```

以上是将`AVueUpload` 上传组件的拖拽属性设置为true，这样类中不用定义该注解的属性也能输出到json给前端。

