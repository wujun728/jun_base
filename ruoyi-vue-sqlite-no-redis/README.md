# 项目介绍
基于[若依vue springboot3版本](https://gitee.com/y_project/RuoYi-Vue/tree/springboot3/) 实现的一套无mysql，无redis的项目。

目的在于支持那些极度轻量级的项目，支持快速启动，减少开发成本和部署成本。

希望通过一个jar服务，实现一个完整的项目。但因为引入了sqlite和caffeine, 本项目只能当做一个轻量级单体项目来使用，不支持微服务架构。

本项目将与若依版本保持一致。

# 技术栈
- springboot 3.5.8
- java 17
- sqlite 3.45.3
- Caffeine 3.1.8


# 部署
1. 正常的打包整spring fatjar包，并部署到服务器即可

# 修改记录
1. 2026-02-21 修复v3.8.9版本[代码生成报错问题](https://gitee.com/hanchers/ruoyi-vue-sqlite-no-redis/issues/ID1KRY)
2. 2026-02-22 同步若依v3.9.1代码
3. 2026-05-24 增加mybatis-flex 版本，参考对应分支

# 个人博客
[寒澈笔记](https://www.hancher.top/)