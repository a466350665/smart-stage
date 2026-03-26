# Smart Stage

一组面向 Spring Boot 3 的轻量级基础能力 Starter，聚焦通用规范和插件化，快速构建兼具单体和聚合启动的Java多模块应用。

## 主要特性

- 统一响应模型 `Result<T>` 与标准分页模型 `Page<T>`
- 统一错误码与异常体系，提供全局异常拦截
- 可选的国际化支持（基于 Spring `MessageSource`）
- MyBatis-Plus 的分页拦截器、基础实体与 Service 支持
- **插件机制（`plugin/*` 插件资源目录的自动加载）**
- **兼具单体和聚合启动方式**

## 目录结构和说明

```
smart-stage
├── smart-stage-core                 核心实体与工具类（Result、Page、错误码、消息工具）
├── smart-stage-starter              Spring Boot 自动配置（异常拦截、i18n、校验、插件资源加载）
├── smart-stage-starter-mybatisplus  MyBatis-Plus 自动配置与通用基类
└── pom.xml
```

## 环境要求

- JDK 17
- Spring Boot 3.5.x

## 相关项目（脚手架与示例）

Smart Stage 负责提供基础能力，以下两个项目分别用于“生成工程骨架”和“演示完整落地”：

### smart-stage-archetype（Maven Archetype 模板）

- 开源地址：[smart-stage-archetype](https://github.com/a466350665/smart-stage-archetype)
- 定位：**快速生成支持插件机制和聚合部署的Java基础应用**
- 预置能力：
  - `service` 模块默认集成 MyBatis-Plus + MySQL
  - `boot` 模块内置 Spring Boot 打包配置与 Dockerfile
  - 示例 CRUD、`init.sql`、插件配置目录 `plugin/${symbol}` 与 i18n 资源

### smart-stage-governor（多模块示例工程）

- 开源地址：[smart-stage-governor](https://github.com/a466350665/smart-stage-governor)
- 定位：**展示 Smart Stage 兼具单体和聚合的能力**
- 结构与能力：
  - `smart-stage-governor-boot` 聚合启动模块（便于单体方式快速验证）
  - `smart-stage-sample1`：示例应用1（基于 [smart-stage-archetype](https://github.com/a466350665/smart-stage-archetype) 生成的Java基础应用）
  - `smart-stage-sample2`：示例应用2（通过 OpenFeign 调用 示例应用1）

## 安装与依赖

项目发布到 Maven Central 后，可按需引入：

```xml
<dependency>
    <groupId>io.github.openjoe</groupId>
    <artifactId>smart-stage-core</artifactId>
    <version>2.0.0</version>
</dependency>
```

```xml
<dependency>
    <groupId>io.github.openjoe</groupId>
    <artifactId>smart-stage-starter</artifactId>
    <version>2.0.0</version>
</dependency>
```

```xml
<dependency>
    <groupId>io.github.openjoe</groupId>
    <artifactId>smart-stage-starter-mybatisplus</artifactId>
    <version>2.0.0</version>
</dependency>
```

如果你使用父 POM 统一管理版本，可使用 `${revision}` 统一控制版本号。

## 快速使用

### 统一响应体与错误码

`Result` 提供统一响应结构，配合 `ErrorCode` 支持动态消息与国际化。

```java
return Result.success(data);
// or
throw new ApplicationException(ErrorCodeEnum.ERROR);
```

### 全局异常处理

`ApplicationException` 与 `CommonException` 支持错误码与国际化消息：

```java
throw new ApplicationException(ErrorCodeEnum.ERROR);
```

由 `GlobalExceptionHandler` 提供，返回统一 `Result` 结构。

### 国际化（i18n）

启用国际化：

```properties
smart.stage.i18n.enabled=true
```

`MessageUtils` 会自动使用 Spring `MessageSource`，支持参数化消息：

```java
String text = MessageUtils.getOrDefault("user.notfound", "用户不存在", userId);
```

资源文件规则：

- 默认：`classpath:messages*.properties`，其中 `messages` 可通过spring.messages.basename配置调整

### 插件资源加载

支持从 `plugin/*` 目录加载配置与资源：

- 配置：`plugin/*/application.yml|yaml|properties`  
  支持 profile：`application-dev.yml` 等
- 国际化资源：`plugin/*/messages*.properties`

无需额外代码，通过 `EnvironmentPostProcessor` 自动加载。

推荐目录结构：

```text
plugin/
  demo/
    application.yml
    application-dev.yml
    messages.properties
    messages_en.properties
```

配置优先级约定：

- 命令行参数、高优先级外部配置、系统环境变量优先于插件配置
- 主应用 `application.yml` 优先于插件默认配置
- 插件 profile 配置优先于同插件下的默认配置
- 插件配置更适合作为扩展默认值，宿主应用负责最终覆盖

使用建议：

- 插件内只放该插件自己的默认配置，避免和宿主应用的大范围通用配置重名
- 如果多个插件声明同名配置，应在宿主应用中显式覆盖，避免依赖插件加载顺序

### MyBatis-Plus 增强

引入 `smart-stage-starter-mybatisplus` 后：

- 自动注册分页拦截器
- 自动填充 `createTime` / `updateTime`
- 提供 `BaseEntity`、`BaseService`、`BaseServiceImpl` 与分页转换

配置示例：

```properties
smart.stage.mybatis-plus.page-db-type=mysql
```

分页调用示例：

```java
Page<User> page = userService.findPage(1, 10);
```

实体与服务示例：

```java
public class UserEntity extends BaseEntity {
  private String name;
}

public interface UserService extends BaseService<UserEntity> {
}
```

分页统一返回：

```java
Page<UserEntity> page = userService.findPage(1, 20);
```

## 配置项一览

- `smart.stage.i18n.enabled`：是否启用国际化（默认 false）
- `smart.stage.mybatis-plus.page-db-type`：MyBatis-Plus 分页数据库类型（默认 ``，仅当需要特指数据库类型分页语法时才配置，如：OceanBase支持Mysql和Oracle两种语法模式）

## License

Apache License 2.0
