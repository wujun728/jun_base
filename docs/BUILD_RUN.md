# jun_base_base 构建运行文档

## 项目信息
- 路径: D:\workspace_github_v1\devops\jun_base_base\jun_springboot_vue
- 类型: RuoYi 自研基座（SpringBoot 2 + Vue2 ElementUI）
- 状态: ✅ 构建运行成功

## 环境要求
- JDK: D:\Java\Java\jdk1.8.0_161_64
- Maven: D:\Java\apache-maven-3.9.8（settings.xml 同目录 conf 下）
- MySQL: 127.0.0.1:3307 root/mysqladmin
- Redis: localhost:6379 无密码
- Node: v24.14.0

## 数据库
- 库名: db1_lowcode（原 db_lowcode 按规则加 db1_ 前缀）
- 初始化脚本: sql/dev_init.sql + rbac_menu_init.sql + quartz.sql + sql_20230223.sql
- 表数量: 63

## 配置修改记录
| 文件 | 修改前 | 修改后 |
|------|--------|--------|
| jun_admin/src/main/resources/application-druid.yml | jdbc:mysql://localhost:3306/db_lowcode, root/空密码 | jdbc:mysql://127.0.0.1:3307/db1_lowcode, root/mysqladmin |
| jun_api/.../MongoApp.java | 引用不存在的 com.ruoyi.nocode.domain.MyFormData | 移至 docs/backup/MongoApp.java.bak（孤儿代码，无任何调用方） |

## 构建命令
```bat
set JAVA_HOME=D:\Java\Java\jdk1.8.0_161_64
D:\Java\apache-maven-3.9.8\bin\mvn.cmd clean package -DskipTests -s D:\Java\apache-maven-3.9.8\conf\settings.xml
```

## 启动命令
```bat
rem 后端
"D:\Java\Java\jdk1.8.0_161_64\bin\javaw.exe" -jar jun_admin\target\jun_admin.jar
rem 前端
cd jun_vue_element_ui
npm run dev
```

## 访问地址
- 后端: http://localhost:8080
- 前端: http://localhost:8000
- 默认账号: admin / admin123

## 日志
- 后端: docs/jun_admin_run.log
- 前端: D:\workspace_github_v1\devops\logs\jun_base_base_fe.log

## 遗留问题
- 无
