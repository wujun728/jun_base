# RuoYi-Uniapp（若依-手机端）开源啦

#### 介绍
&nbsp; &nbsp; 若依-Ruoyi APP 移动解决方案，基于 uniapp+uView 封装的一套基础模版，开箱即用，一份代码多终端适配，支持H5+支付宝小程序+微信小程序+APP，实现了与ruoyi-vue后台完美对接的移动解决方案，可直接开始快速开发业务需求，全新UI设计，更多交互细节，我们将为您提供极致的交互体验体验，持续推出高质量的交互产品。

    如果对您有帮助，您可以点右上角 “Star” 收藏一下 ，获取第一时间更新，谢谢！刚刚开源，BUG修复中

* 感谢jeesite，项目参考自[JeeSite Mobile Uni-App](https://gitee.com/thinkgem/jeesite4-uniapp)
* 感谢[RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue)
* 适配ruoyi-vue后端，将doc下的java类放进去即可


#### 快速体验

2、微信小程序端：扫码访问（目前只能用用户名密码方式登录，用户名：admin 密码：admin123）<br><br>
<img src="https://images.gitee.com/uploads/images/2021/1115/214722_20aaf4c8_9700683.jpeg" width="220" height="220" >


## 基于RuoYi修改的美化皮肤的样式地址

- [🎉 RuoYi + vue2.x + Max + element-ui（vue2.x 支持 PC、平板、手机）](http://82.157.44.212:8091/index)

- [🎉 RuoYi + vue3.x + Max + element-plus（vue3.x 支持 PC、平板、手机）](http://82.157.44.212:8090/index)

- [🎉 RuoYi + vue2.x + Max + element-ui + Cloud（vue2.x 支持 PC、平板、手机）](http://82.157.44.212:8093/index)

- [🎉 RuoYi + vue3.x + Max + element-plus + Cloud（vue3.x 支持 PC、平板、手机）](http://82.157.44.212:8092/index)

- [🎉 RuoYi + vue3.x + element-plus + uniapp2（vue3.x 支持 PC、平板、手机）](http://82.157.44.212:8094/#/)


#### 我的另一个项目：

 **AiDex Sharp 快速开发平台** 基于著名的开源项目“ **若依-RuoYi-Vue** ”改造而成，追求 **极致的UI交互体验** 和 **快速开发** ，一切向 **效率** 看齐， **重构优化** 后端的代码，对前端页面进行了 **美化** 。 **我们将持续升级，持续完善，欢迎友友们收藏和点赞** 。

* [打开Aidex Sharp](https://gitee.com/big-hedgehog/aidex-sharp)

<table>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0923/234748_170e4ee7_9700683.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0911/184210_ffa2880b_9700683.png"/></td>
    </tr>
</table>

#### 如何使用uni-app端

##### 一、导入uniapp项目

    1. 首先下载HBuilderX并安装，地址：https://www.dcloud.io/hbuilderx.html
    2. 打开HBuilderX -> 顶部菜单栏 -> 文件 -> 导入 -> 从本地目录导入 -> 选择uniapp端项目目录
    3. 找到common/config.js文件，找到里面的apiUrl项，填入已搭建的后端url地址
    4. 打开manifest.json文件，选择微信小程序配置，填写小程序的appid

##### 二、本地调试

    1. 打开HBuilderX -> 顶部菜单栏 -> 运行 -> 运行到浏览器 -> Chrome
    2. 如果请求后端api时 提示跨域错误，可安装Chrome插件：【Allow CORS: Access-Control-Allow-Origin】，地址：https://chrome.google.com/webstore/detail/allow-cors-access-control/lhobafahddgcelffkeicbaginigeejlf

##### 三、打包发行（H5）

    1. 打开HBuilderX -> 顶部菜单栏 -> 发行 -> 网站H5-手机版
    2. 打包后的文件路径：/unpackage/dist/build/h5
    3. 将打包完成的所有文件 复制到商城后端/pulic目录下，全部替换

##### 四、打包发行（微信小程序）

    1. 下载微信开发者工具并安装，地址：https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html
    2. 打开HBuilderX -> 顶部菜单栏 -> 发行 -> 小程序-微信
    3. 打包后的文件路径：/unpackage/dist/build/mp-weixin
    5. 打开微信开发者工具 导入 打包完成的项目
    6. 检查没有运行错误，在右上方上传小程序

##### 5、后端代码适配ruoyi-vue

    1. 可以启动后端，直接访问http://aidex.vip的公共服务，如果要自己适配，请将doc目录下的代码放到项目中即可。
#### 界面截图


<table>
    <tr>
        <td style="border:5px"><img src="https://images.gitee.com/uploads/images/2021/1112/184344_b519b98b_9700683.png" width="300" height="480"/></td>
        <td style="border:5px"><img src="https://images.gitee.com/uploads/images/2021/1112/223144_43bc09a8_9700683.png" width="300" height="480"/></td>
        <td style="border:5px"><img src="https://images.gitee.com/uploads/images/2021/1112/223205_fd09aab2_9700683.png" width="300" height="480"/></td>
    </tr>
    <tr>
        <td style="border:5px"><img src="https://images.gitee.com/uploads/images/2021/1112/223528_bd934103_9700683.png" width="300" height="480"/></td>
        <td style="border:5px"><img src="https://images.gitee.com/uploads/images/2021/1112/223553_5d4f27a1_9700683.png" width="300" height="480"/></td>
        <td style="border:5px"><img src="https://images.gitee.com/uploads/images/2021/1112/223612_8ca07db6_9700683.png" width="300" height="480"/></td>
    </tr>
    <tr>
        <td style="border:5px"><img src="https://images.gitee.com/uploads/images/2021/1112/223248_7d40c45c_9700683.png" width="300" height="480"/></td>
        <td style="border:5px"><img src="https://images.gitee.com/uploads/images/2021/1112/223306_a6be218d_9700683.png" width="300" height="480"/></td>
        <td style="border:5px"><img src="https://images.gitee.com/uploads/images/2021/1112/223324_a3415319_9700683.png" width="300" height="480"/></td>
    </tr>
    <tr>
        <td style="border:5px"><img src="https://images.gitee.com/uploads/images/2021/1112/223405_a7fd6593_9700683.png" width="300" height="480"/></td>
        <td style="border:5px"><img src="https://images.gitee.com/uploads/images/2021/1112/223424_398ebcde_9700683.png" width="300" height="480"/></td>
        <td style="border:5px"><img src="https://images.gitee.com/uploads/images/2021/1112/223501_db695cd4_9700683.png" width="300" height="480"/></td>
    </tr>
</table>

## 我的另一个项目：

 **AiDex Sharp 快速开发平台** 基于著名的开源项目“ **若依-RuoYi-Vue** ”改造而成，追求 **极致的UI交互体验** 和 **快速开发** ，一切向 **效率** 看齐， **重构优化** 后端的代码，对前端页面进行了 **美化** 。 **我们将持续升级，持续完善，欢迎友友们收藏和点赞** 。

* [打开Aidex Sharp](https://gitee.com/big-hedgehog/aidex-sharp)

官方QQ群
Aidex Sharp快速开发平台3群 208511180 使用问题请入群由专人负责简答

## 后台系统截图

<table>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0923/234823_7d05456a_9700683.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0923/234748_170e4ee7_9700683.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0911/184041_c4d1f1aa_9700683.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0911/184055_0cf08e45_9700683.png"/></td>
    </tr>
	<tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0911/184110_2e6df64f_9700683.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0911/184125_3d5bdddf_9700683.png"/></td>
    </tr>	 
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0911/184139_092a8f07_9700683.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0922/225255_f8710fb3_9700683.png"/></td>
    </tr>
	<tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0911/184210_ffa2880b_9700683.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0911/184223_8f57f5f0_9700683.png"/></td>
    </tr>
	<tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0911/184238_5cb3e09e_9700683.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0911/184256_5bc77bff_9700683.png"/></td>
    </tr>
</table>
更多功能请访问系统体验

## 在线体验

演示地址：http://aidex.vip  帐号：admin 密码：admin123

#### uniapp知识

1. <a href="https://uniapp.dcloud.io/README" target="blank">uni-app介绍</a>
2. <a href="https://ke.qq.com/course/3169971" target="blank">uni-app 官方视频教程</a>
3. <a href="https://www.dcloud.io/hbuilderx.html" target="blank">uni-app开发工具 HBuilderX 下载及使用说明</a>
4. <a href="http://ask.dcloud.net.cn/article/35657" target="blank">uni-app是什么？能解决什么问题</a>
5. <a href="https://uniapp.dcloud.io/vue-basics" target="blank">Vue.js相关文档、视频教程</a>

#### 技术手册

* <a href="https://uniapp.dcloud.io/collocation/pages" target="blank">uni-app 框架文档</a>
* <a href="https://uniapp.dcloud.io/component/README" target="blank">uni-app 组件文档</a>
* <a href="https://uviewui.com/components/intro.html" target="blank">uView 组件文档</a>
* <a href="https://uviewui.com/js/intro.html" target="blank">uView JS 文档</a>

#### 授权许可协议条款

1. Ruoyi-Uniapp采用MIT开源协议协议。
2. 代码可用于个人项目等接私活或企业项目脚手架使用，Ruoyi-Uniapp开源版完全免费。
3. 允许进行商用，但是不允许二次开源出来并进行收费，否则将追究侵权者法律责任。
4. 请不要删除和修改Ruoyi-Uniapp源码头部的版权与作者声明及出处。
5. 不得进行简单修改包装声称是自己的项目。
6. 我们已经申请了相关的软件开发著作权和相关登记
7. 需要在您的软件介绍明显位置说明出处：举例：本软件基于Ruoyi-Uniapp手机端

#### 关于我们

&nbsp; &nbsp; 我们擅长UI、前端开发、后端架构，有一颗热爱开源的心，致力于打造企业级的通用产品设计UI体系让项目
或者更直观，更高效、更简单，未来将持续关注UI交互，持续推出高质量的交互产品。
######
<img src="https://images.gitee.com/uploads/images/2021/1112/114326_5eb079c2_9700683.jpeg" width="220" height="220" >&nbsp; &nbsp; &nbsp;&nbsp;
<img src="https://images.gitee.com/uploads/images/2021/1112/114207_bb1bac92_9700683.jpeg" width="220" height="220" >&nbsp; &nbsp; &nbsp;&nbsp; 
<img src="https://images.gitee.com/uploads/images/2021/1115/164243_d4e0d61d_9700683.png" width="220" height="220" >&nbsp; &nbsp; &nbsp;&nbsp;

版权所有Copyright © 2017-2021 By AiDex (http://www.aidex.vip) All rights reserved