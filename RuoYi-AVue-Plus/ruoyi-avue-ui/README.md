## 开发

​		**请先安装好NodeJS环境**

```bash
# 克隆项目
git clone https://gitee.com/liukaixiong/RuoYi-AVue-Plus.git

# 进入项目目录
cd ruoyi-avue-ui

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
```

浏览器访问 http://localhost:80

默认帐号密码 : amdin / admin123

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

### tomcat部署

执行上面编译命令之后，会有一个`dist` 文件夹，里面就是打包好的文件

1. 非根目录编译

比如路径为`http://localhost:8080/ry` 的情况，需要关注一下`vue.config.js`文件中的`publicPath` 属性。各环境的配置中参数为`BASE_URL` 。这种情况请填写成`/ry` 。

**如果在tomcat中出现刷新路径404的情况**， 可以参考这个方式 ： 

在比如tomcat的webapps的ry目录下新建`WEB-INF\web.xml`的目录和文件，新建好了之后

`web.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xmlns="http://xmlns.jcp.org/xml/ns/javaee"
 xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee/web-app_2_5.xsd"
 id="scplatform" version="2.5">
	 <display-name>/</display-name><!--/webName/-->
	 <error-page>
	 <error-code>404</error-code>
	 <location>/index.html</location>
	 </error-page>
</web-app>
```

这个方案的思路是遇到404的路径直接跳转到index.html，完美`Vue`解决了单页面跳转的问题。

[若依部署方案](http://doc.ruoyi.vip/ruoyi-vue/document/hjbs.html#%E5%89%8D%E7%AB%AF%E9%83%A8%E7%BD%B2)



### IDEA 开发



1. 编辑启动配置

![image-20211028113735024](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/image-20211028113735024.png)

2. 设置应用环境，找到`ruoyi-avue-ui`的`package.json`位置

![image-20211028113633480](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/image-20211028113633480.png)

点击启动DEBUG就OK了。

**页面调试的话，请打开浏览器F12里面还是按照前端那一套调试逻辑。**

## 代码相关介绍

- src
  - api : 远程调用相关
    - crud : 涵盖了AVue调用后台逻辑的JS
      - event : 事件定义 
        - dialogSubmitEvent.js : 通用的弹层提交的事件定义
        - rowClickEvent.js : 行点击事件定义
  - const : 常量枚举定义  
  - mixins : AvueJS的增删改查增强的js
    - crud.js : 原生的AVueJS增强版
    - server-crud.js : 支持远端服务的AVueJs改版
  - mock : 模拟数据
  - option : AvueJS的页面渲染Json存储
  - views : 页面视图处理
    - crud : 通用的增删改查模版
      - avue-crud-test.vue  // **支持AvueJs的增删改查版本**
      - crud-test.vue  // **支持AVue的通用原生版本**
      - **server-crud.vue // 支持AVue增删改查的服务端版本**

有相关的调试内容或者增强功能，请根据需要去view的crud中找匹配版本进行调试并且增强。



