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

## 第二阶段范围

第二阶段已完成学习计划模块：

- `study_plan` 表及相关 SQL
- 学习计划后端 MVC 三层
- 学习计划前端页面
- 首页“今日学习计划概览”真实接口接入

## 第三阶段范围

第三阶段聚焦生活记录模块：

- `life_record` 表落库
- 生活记录后端 MVC 三层
- 生活记录前端页面
- 首页“今日记录提醒”真实接口接入
- Swagger 文档补充
- 前后端联调与 README 说明补充

## 第四阶段范围

第四阶段聚焦每日复盘模块：

- `daily_review` 表落库
- 每日复盘后端 MVC 三层
- 每日复盘前端页面
- 首页“今日复盘提醒”真实接口接入
- Swagger 文档补充
- 前后端联调与 README 说明补充

## 目录说明

- `study-life-server`：Spring Boot 后端
- `study-life-web`：Vue 3 + Vite 前端
- `docs`：需求、数据库、接口、页面原型文档

## 数据库初始化

1. 创建数据库并执行第一阶段初始化脚本：

```sql
source study-life-server/src/main/resources/db/init.sql;
```

2. 执行第三阶段生活记录表脚本：

```sql
source study-life-server/src/main/resources/db/phase-3-life-record.sql;
```

3. 执行第四阶段每日复盘表脚本：

```sql
source study-life-server/src/main/resources/db/phase-4-daily-review.sql;
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

## 第三阶段启动与联调说明

1. 先启动 MySQL，并依次执行：

```sql
source study-life-server/src/main/resources/db/init.sql;
source study-life-server/src/main/resources/db/phase-3-life-record.sql;
```

2. 启动后端：

```bash
cd study-life-server
mvn spring-boot:run
```

3. 启动前端：

```bash
cd study-life-web
npm install
npm run dev
```

4. 先登录获取令牌：`POST /api/auth/login`
5. 访问生活记录接口时统一携带：

```http
Authorization: Bearer <token>
```

6. 生活记录接口：

- `POST /api/life-record`
- `GET /api/life-record/list`
- `GET /api/life-record/{id}`
- `PUT /api/life-record/{id}`
- `DELETE /api/life-record/{id}`

7. 首页会调用生活记录列表接口查询今天是否已记录，并展示“去记录”或“已记录”状态。

## 第三阶段测试提示

- 同一用户同一天重复创建生活记录，应返回业务错误
- `sleepHours` 为空允许，不为空时必须在 `0.0 ~ 24.0`
- 修改记录日期时，若目标日期已存在记录，应返回业务错误
- 删除后列表默认不再返回该条记录
- 首页“今日记录提醒”应与当天真实记录状态一致

## 第四阶段启动与联调说明

1. 先启动 MySQL，并依次执行：

```sql
source study-life-server/src/main/resources/db/init.sql;
source study-life-server/src/main/resources/db/phase-3-life-record.sql;
source study-life-server/src/main/resources/db/phase-4-daily-review.sql;
```

2. 启动后端：

```bash
cd study-life-server
mvn spring-boot:run
```

3. 启动前端：

```bash
cd study-life-web
npm install
npm run dev
```

4. 先登录获取令牌：`POST /api/auth/login`
5. 访问每日复盘接口时统一携带：

```http
Authorization: Bearer <token>
```

6. 每日复盘接口：

- `POST /api/daily-review`
- `GET /api/daily-review/list`
- `GET /api/daily-review/{id}`
- `PUT /api/daily-review/{id}`
- `DELETE /api/daily-review/{id}`

7. 首页会调用每日复盘列表接口查询今天是否已复盘，并展示“去复盘”或“已复盘”状态。

## 第四阶段测试提示

- 同一用户同一天重复创建每日复盘，应返回业务错误
- `reviewScore` 为空允许，不为空时必须在 `1 ~ 10`
- 修改复盘日期时，若目标日期已存在复盘，应返回业务错误
- 删除后列表默认不再返回该条记录
- 首页“今日复盘提醒”应与当天真实复盘状态一致

## 说明

- 所有 SQL 均写在 `Mapper.xml`
- 已完成注册登录、学习计划、生活记录、每日复盘四个阶段的最小可运行能力
- 每日复盘、统计分析、图片上传、提醒通知等扩展能力尚未实现
