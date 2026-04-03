# AGENTS.md

## 项目说明
这是一个前后端分离的“学习计划与日常生活记录系统”。

项目中已提供以下参考文档，请在开发前先完整阅读并严格遵循：
- docs/学习计划与日常生活记录系统需求分析文档.md
- docs/学习计划与日常生活记录系统数据库设计说明书.md
- docs/学习计划与日常生活记录系统接口约束文件.md
- docs/学习计划与日常生活记录系统页面原型说明书.md

## 项目命名
- 前端项目：study-life-web
- 后端项目：study-life-server
- 数据库：study_life_db
- Java 包名：com.sakura.studylife

## 架构要求
后端严格采用 MVC 三层架构：
- Controller 层：接收请求、参数校验、返回统一结果
- Service 层：处理业务逻辑
- Mapper 层：负责数据库访问

不要擅自改成 DDD、微服务、六边形架构等其他复杂架构。

## 后端技术约束
- JDK 17
- Spring Boot 3.5.x
- Maven 3.9.x
- MyBatis + MyBatis XML
- MySQL 8.0.x
- JWT 鉴权
- Swagger / OpenAPI
- 统一返回体 Result<T>
- 全局异常处理
- 逻辑删除字段统一处理

## 前端技术约束
- Node.js 24 LTS
- Vue 3
- Vite
- Element Plus
- Axios
- Vue Router
- Pinia

## 数据与接口约束
- 所有后端接口统一前缀：/api
- 所有业务数据必须按当前登录用户隔离
- 不允许前端直接传 userId 控制数据归属
- 所有 SQL 必须写在 Mapper.xml 中，不允许注解 SQL
- 默认查询必须过滤 deleted_flag = 0
- 返回字段使用小驼峰，数据库字段使用下划线

## 开发规则
1. 先阅读文档，再开始写代码
2. 先输出实现计划，再动手修改文件
3. 优先做最小可运行版本，不要一次性实现全部模块
4. 每完成一个阶段，都要说明：
    - 修改了哪些文件
    - 每个文件的作用
    - 启动命令
    - 测试步骤
5. 修改后要确保能编译、能运行、能联调
6. 未经要求，不要擅自引入 Redis、Docker、消息队列、对象存储等额外基础设施