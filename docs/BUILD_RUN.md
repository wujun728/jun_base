# jun_base_elementui_springboot3 构建运行文档

## 项目信息
- 路径: D:\workspace_github_v1\devops\jun_base_elementui_springboot3\ruo-yi-spring-boot3
- 类型: RuoYi-Vue 3.8.8 (SpringBoot 3 + JDK17 + LangChain4j AI + Vue2 ElementUI)
- 状态: ✅ 构建运行成功
- 同目录其他项目: ruoyi-agent-spring-aialibaba / RuoYi-SpringBoot3-UniApp / RuoYi-Vue-3.92-springboot3（未单独启动）

## 环境
- JDK17: D:\Java\Java\jdk-17.0.7 | Maven: D:\Java\apache-maven-3.9.8
- MySQL: 127.0.0.1:3307 root/mysqladmin | Redis: localhost:6379

## 数据库
- 库名: db1_ruoyi_ai（34 表）
- 脚本: sql/ry_20240629.sql + quartz.sql + springboot3_nofk.sql(仅提取 ai_tables.sql: chat_model_list/vector_database_list/chat_session/chat_session_detail)

## 配置修改记录
| 文件 | 修改内容 |
|------|---------|
| ruoyi-admin/.../application-druid.yml | localhost:3306/ruoyi-ai/root → 127.0.0.1:3307/db1_ruoyi_ai/mysqladmin |
| ruoyi-admin/.../application.yml | port: 8080 → 8082（避免与主基座冲突） |
| ruoyi-ai/.../config/BaseConfig.java | pgVectorEmbeddingStore() 返回 null（本地无 PgVector/Qdrant 服务）；chatAssistant 参数 PgVectorEmbeddingStore → EmbeddingStore<TextSegment> |
| ruoyi-ai/.../KnowledgeBaseLineController.java | @Autowired(required=false) + null 检查（向量库缺失时返回提示而非启动失败） |
| ruoyi-ai/.../LangchainController.java | @Qualifier("inMemoryEmbeddingStore") 消除多 Bean 冲突 |

## 数据种子
- chat_model_list: Ollama http://127.0.0.1:11434 qwen2.5:0.5b（本地无 Ollama 时 AI 对话不可用，但系统正常启动）
- vector_database_list: PgVector-local 记录（实际 Bean 已禁用）

## 构建命令
```bat
set JAVA_HOME=D:\Java\Java\jdk-17.0.7
cd ruo-yi-spring-boot3
D:\Java\apache-maven-3.9.8\bin\mvn.cmd clean package -DskipTests -s D:\Java\apache-maven-3.9.8\conf\settings.xml
```

## 启动
- 后端: ruoyi-admin\target\ruoyi-admin.jar → http://localhost:8082
- 前端: ruoyi-ui → npm run dev → http://localhost:81
- 账号: admin / admin123

## 日志
- 后端: D:\workspace_github_v1\devops\logs\jun_base_elementui_sb3_be.log
- 前端: D:\workspace_github_v1\devops\logs\jun_base_elementui_sb3_fe.log

## 遗留问题
- AI 对话功能需本地 Ollama 服务（127.0.0.1:11434），未安装则该功能不可用
- 向量化功能需 PgVector/Qdrant，本地未部署，已降级为内存存储
