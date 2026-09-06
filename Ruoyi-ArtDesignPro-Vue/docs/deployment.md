# 部署说明

本文档说明 Nexa Admin 的常见部署方式。生产环境请根据实际网络、域名、证书和安全策略调整。

## 环境要求

- JDK 17+
- Maven 3.8+
- Node.js 20.19+
- pnpm 8.8+
- MySQL 8+
- Redis 6+
- Nginx 1.20+

## 数据库初始化

新环境创建数据库：

```sql
CREATE DATABASE `ry-vue` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

导入完整初始化 SQL：

```bash
mysql -uroot -p ry-vue < sql/ry_20260417.sql
```

已有 RuoYi 数据库请按 README 的增量 SQL 顺序执行。增量脚本设计为幂等执行，适合升级环境重复校验。

## 后端配置

数据库配置：

```text
ruoyi-admin/src/main/resources/application-druid.yml
```

Redis、端口、Token 和上传目录配置：

```text
ruoyi-admin/src/main/resources/application.yml
```

生产环境至少需要调整：

- `spring.datasource.druid.master.url`
- `spring.datasource.druid.master.username`
- `spring.datasource.druid.master.password`
- `spring.data.redis.host`
- `spring.data.redis.password`
- `token.secret`
- `ruoyi.profile`
- Druid 控制台账号密码

## 后端启动

本地开发可直接从 IDE 启动：

```text
com.ruoyi.RuoYiApplication
```

服务器部署可打包后运行：

```bash
mvn -pl ruoyi-admin -am clean package -DskipTests
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

建议用 systemd、Supervisor 或容器平台托管 Java 进程，并将日志输出到独立目录。

## 前端配置

开发环境：

```text
artpro-ui/.env.development
```

生产环境：

```text
artpro-ui/.env.production
```

如果前端和后端部署在同一域名，推荐生产配置：

```env
VITE_BASE_URL = /
VITE_API_URL = /api
```

如果后端为独立域名，可以配置为完整地址：

```env
VITE_API_URL = https://api.example.com
```

## 前端构建

```bash
cd artpro-ui
pnpm install --frozen-lockfile
pnpm run build
```

构建产物默认输出到：

```text
artpro-ui/dist
```

## Nginx 示例

同域名部署推荐使用 `/api` 代理到 RuoYi 后端，同时保留 Druid 和 Swagger 代理。

```nginx
server {
    listen 80;
    server_name example.com;

    root /data/www/artpro-ui/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://127.0.0.1:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /druid/ {
        proxy_pass http://127.0.0.1:8080/druid/;
        proxy_set_header Host $host;
    }

    location /swagger-ui/ {
        proxy_pass http://127.0.0.1:8080/swagger-ui/;
        proxy_set_header Host $host;
    }

    location /v3/api-docs/ {
        proxy_pass http://127.0.0.1:8080/v3/api-docs/;
        proxy_set_header Host $host;
    }
}
```

## Art Bot 部署注意事项

- 模型 API Base URL 会自动拼接 `/chat/completions`。
- API Key 不会通过管理接口返回明文，但当前版本仍以明文写入数据库。
- 生产环境应限制数据库访问权限，并使用最小权限模型 Key。
- 若模型服务在外网，服务器需要具备访问该服务的网络权限。

## 权限与缓存

缓存监控权限已拆分：

- `monitor:cache:list`：缓存概览、分类和键名。
- `monitor:cache:query`：查看缓存值。
- `monitor:cache:remove`：清理缓存。

建议仅给可信管理员分配 `monitor:cache:query` 和 `monitor:cache:remove`。

## 发布检查清单

- 修改默认管理员密码。
- 修改数据库、Redis、Druid、JWT Secret。
- 确认上传目录可写。
- 确认前端 `VITE_API_URL` 与 Nginx 代理一致。
- 确认普通用户没有敏感缓存权限。
- 确认 Art Bot API Key 已最小权限化。
- 确认生产日志、备份和监控策略。
