# 通用的增删改查的服务使用手册

## 新增功能描述
- 引入MOCK功能方便调试
- 引入AVUE前端框架，极致增删改查，并改造成支持远端的配置文件
- 引入SpringBoot客户端的介入,暴露HTTP的配置接口

## 前端 ruoyi-avue-ui 相关介绍

- src
  - api : 远程调用相关
    - crud : 涵盖了AVue调用后台逻辑的JS
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

