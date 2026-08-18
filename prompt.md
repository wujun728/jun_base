# jun_base
jun_base


当前项目是elementui+java的技术栈的基座，我想保留几个基座版本，将其他的项目合并到基座上，后面根据基座来二次开发不同的项目【D:\workspace_github_v1\devops\jun_base_elementui\】 给出对应的方案，看下那些适合作为基座，那些适合作为模块合并进来。基座的可以有多个，适配不同的技术栈，但是大体的技术路线不变。


这个是个双前端的项目，我想在新前端里面使用原前端的所有功能，有两个方向（主旨，使用新前端的登录、菜单、导航，主页等功能，各个路由子页面不变）。方案1，在新前端里面改造，参考原前端，适配后端；方案2，copy一份原前端，将菜单及导航功能移植过去，其他的页面及功能等不变；
新前端：D:\workspace_github_v1\devops\jun_base_elementui\RuoYi-Vue-3.92-springboot2-jdk8\crmeb_admin\
原前端：D:\workspace_github_v1\devops\jun_base_elementui\RuoYi-Vue-3.92-springboot2-jdk8\ruoyi-ui\



使用Playwright MCP打开本地Vue项目地址http://localhost:9527，等待页面完全加载，
1、抓取浏览器控制台所有Error、Warning报错，整理完整报错堆栈；
2、判断页面是否白屏、组件是否正常渲染；
3、定位报错对应的Vue文件与大致代码行，分析AI修改产生bug的原因。
4、帐密：admin admin123
5、验证码先识别图片，识别不了，提示我输入验证码
6、遍历菜单，点击菜单，查看菜单页面是否正常打开，是否报错，是否404，是否渲染异常。还有每个页面是否正常打开，是否报错，还有每个页面的按钮及列表按钮等。




这个是个双前端的项目，我想在新前端里面使用原前端的所有功能，
dev2.0 这个是crm的原始版本，是可以正常运行的，只是菜单跟各种接口与若依并不匹配，能不能写一层适配器转若依的json对象为这个crm需要的对象，尽量少的改动原版的代码：D:\workspace_github_v1\devops\jun_base_elementui\RuoYi-Vue-3.92-springboot2-jdk8\crmeb_admin2\
dev1.0 这个是crm改动非常大的版本，但是改动各种报错，特别是菜单那块，改动面目全非，路由各种问题：D:\workspace_github_v1\devops\jun_base_elementui\RuoYi-Vue-3.92-springboot2-jdk8\crmeb_admin\

这个是若依的原前端：D:\workspace_github_v1\devops\jun_base_elementui\RuoYi-Vue-3.92-springboot2-jdk8\ruoyi-ui\


能不能启动多个agent来并发执行，我觉得效率太慢了，主agent掌控进度及tood，子agent负责执行及测试，执行的agent可以多个  


菜单的icon没有了；系统工具的菜单404；dashboard主页的快捷入口都是404；   菜单不能写死，菜单是动态生成的 



在工程【D:\workspace_github_v1\devops\jun_base_elementui\】 中以 【 D:\workspace_github_v1\devops\jun_base_elementui\RuoYi-Vue-3.92-springboot2-jdk8】 作为基座，想将其他的项目（工程下所有的非基座模块）作为模块（前端模块+后端模块）迁移到基座里面来，给出具体的实施方案。并输出为md文档。


在工程【D:\workspace_github_v1\product\jun_product_center_private_qixing】 中以 【 D:\workspace_github_v1\product\jun_product_center_private_qixing\RuoYi-Vue-3.92-springboot2-jdk8\】 作为基座，想将其他的项目（工程下所有的非基座模块）作为模块（前端模块+后端模块）迁移到基座里面来，给出具体的实施方案。并输出为md文档。


java --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --           datarule/oss/biz-demo）             
add-opens java.base/sun.reflect.annotation=ALL-UNNAMED -jar ruoyi-admin.jar  


--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.lang.reflect=ALL-UNNAMED
--add-opens java.base/java.util=ALL-UNNAMED
--add-opens java.base/java.util.collection=ALL-UNNAMED
--add-opens java.base/java.io=ALL-UNNAMED
--add-opens java.base/java.net=ALL-UNNAMED

cd D:\workspace_github_v1\devops\jun_base_elementui\RuoYi-Vue-3.92-springboot2-jdk8\scrm_ui

npm install
npm install --registry=https://registry.npmmirror.com
npm run dev

关闭最后一个tab页签的时候，当前页面没有消失，应该跳转到主页tab页签【主页tab页签是默认打开的，无法关闭的tab页签】，打开主页如果是404找不到菜单兜底就找【主页tab页签】

下面这两个项目有没有可以迁移的东西或者模块到
D:\workspace_github_v1\devops\jun_base_elementui\jun_base_vue_mybatisplus\
D:\workspace_github_v1\devops\jun_base_elementui\jun_base_vue2_mybatisplus_elementui\

下面的底座里面来呢？
D:\workspace_github_v1\devops\jun_base_elementui\RuoYi-Vue-3.92-springboot2-jdk8\