# AutoEE-快速开发平台：前端框架

## 平台简介

- 基于 vue3 + CompositionAPI + vite + ant-design-vue3 实现的管理平台前端框架。
- 使用各组件的最新版本，融合新技术，代码简单易懂，组件拆分清晰，不做过度封装，符合常规开发模式，自动代码生成，修改简单高效，运行速度快。
- 使用Vue3的< script setup >语法糖，代码结构清晰，代码量大幅减少，真香！！！
- 界面简洁美观，使用 Ant Design of Vue 最新 3.x 版本，原汁原味，清晰明了，操作方便。
- 后端直接对接 RuoYi-Vue。[访问 RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue)

## 已完成功能

- 系统管理
  - 用户管理
  - 角色管理
  - 菜单管理
  - 部门管理
  - 岗位管理
  - 字典管理
  - 参数设置
  - 日志管理
    - 操作日志
    - 登录日志
- 系统监控
  - 定时任务
- 系统工具
  - 代码生成

## 使用方法

1. 启动前端 autoee-ui 工程
   - (1)下载 autoee-ui 工程到本地
   - (2)执行 npm install
   - (3)执行 npm run dev
2. 启动后端 RuoYi-Vue 工程
   - (1)下载 RuoYi-Vue 工程到本地
   - (2)将前端 autoee-ui 工程目录 src\javaFiles\template\vm\vue\下的如下文件覆盖到后端 RuoYi-Vue 工程目录 ruoyi-generator/src/main/resources/vm/vue 中
     - index.vue.vm
     - addUpdateModal.vue.vm
     - detailModal.vue.vm
   - (3)将前端 autoee-ui 工程目录 src\javaFiles\下的如下文件覆盖到后端 RuoYi-Vue 工程中
     - GenTableServiceImpl.java
     - VelocityUtils.java
   - (4)[可选]将前端 autoee-ui 工程目录 src\sqlFiles\下的如下文件导入到数据库中
     - 1.向表 gen_table 中插入数据.sql
     - 2.向表 gen_table_column 中插入数据.sql
   - (5)启动 RuoYi-Vue 工程
3. 访问 http://localhost:8090/

## 如果 AutoEE 对您有帮助，请我喝杯咖啡吧！

<img src="https://gitee.com/Double_AutoEE/AutoEE/raw/master/autoee-ui/src/assets/styles/loginPage/admireQRcode.jpg" width=30% />

## 平台界面图片

<img src="https://gitee.com/Double_AutoEE/AutoEE/raw/master/autoee-ui/src/assets/images/autoee/%E7%99%BB%E9%99%86%E9%A1%B5%E9%9D%A2.png"  />
<img src="https://gitee.com/Double_AutoEE/AutoEE/raw/master/autoee-ui/src/assets/images/autoee/%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86.png"  />
<img src="https://gitee.com/Double_AutoEE/AutoEE/raw/master/autoee-ui/src/assets/images/autoee/%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86-2.png"  />
<img src="https://gitee.com/Double_AutoEE/AutoEE/raw/master/autoee-ui/src/assets/images/autoee/%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86-3.png"  />
<img src="https://gitee.com/Double_AutoEE/AutoEE/raw/master/autoee-ui/src/assets/images/autoee/%E8%8F%9C%E5%8D%95%E7%AE%A1%E7%90%86-1.png"  />
<img src="https://gitee.com/Double_AutoEE/AutoEE/raw/master/autoee-ui/src/assets/images/autoee/%E8%8F%9C%E5%8D%95%E7%AE%A1%E7%90%86-2.png"  />
<img src="https://gitee.com/Double_AutoEE/AutoEE/raw/master/autoee-ui/src/assets/images/autoee/%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90-1.png"  />
<img src="https://gitee.com/Double_AutoEE/AutoEE/raw/master/autoee-ui/src/assets/images/autoee/%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90-2.png"  />
<img src="https://gitee.com/Double_AutoEE/AutoEE/raw/master/autoee-ui/src/assets/images/autoee/%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90-3.png"  />
<img src="https://gitee.com/Double_AutoEE/AutoEE/raw/master/autoee-ui/src/assets/images/autoee/genCodeReview.png"  />
