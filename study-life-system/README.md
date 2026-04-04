# Study Life System

## 第一阶段范围

本阶段仅实现最小可运行版本：

- 后端基础骨架
- `sys_user` 用户表初始化 SQL
- 用户注册
- 用户登录
- JWT 鉴权
- 获取当前登录用户信息
- 统一返回体 `Result<T>`
- 全局异常处理
- Swagger / OpenAPI 基础接入
- 前端 Vue 3 + Vite 初始化
- 登录页
- 登录成功后的首页框架页

## 目录说明

- `study-life-server`：Spring Boot 后端
- `study-life-web`：Vue 3 + Vite 前端
- `docs`：需求、数据库、接口、页面原型文档

## 数据库初始化

1. 创建数据库并执行初始化脚本：

```sql
source study-life-server/src/main/resources/db/init.sql;
```

2. 默认数据库连接配置位于：

```text
study-life-server/src/main/resources/application.yml
```

如本地 MySQL 用户名或密码不同，请先修改：

- `spring.datasource.username`
- `spring.datasource.password`

## 后端启动

在项目根目录执行：

```bash
mvn -DskipTests compile
```

或进入后端目录执行：

```bash
cd study-life-server
mvn spring-boot:run
```

启动后访问：

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`

## 前端启动

进入前端目录执行：

```bash
cd study-life-web
npm install
npm run dev
```

默认访问地址：

```text
http://localhost:5173/study-life/login
```

## 联调说明

1. 先启动 MySQL，并执行 `init.sql`
2. 启动后端 `study-life-server`
3. 启动前端 `study-life-web`
4. 注册接口：`POST /api/auth/register`
5. 登录接口：`POST /api/auth/login`
6. 前端登录成功后会将 `accessToken` 保存到 `localStorage`
7. 后续请求会自动携带：

```http
Authorization: Bearer <token>
```

8. 首页加载时会调用：

```text
GET /api/user/profile
```

## 说明

- 所有 SQL 均写在 `Mapper.xml`
- 当前阶段未实现学习计划、生活记录、每日复盘、统计分析完整模块
- 当前环境内未安装 Node.js，因此本次提交未在本机执行前端安装与构建验证；后端已完成 Maven 编译验证
