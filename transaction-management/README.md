# 交易管理系统
## 系统介绍
创建一个与银行系统中的交易管理相关的简单应用程序。应用程序应该使用户能够记录、查看和管理金融交易。

## 设计方案

### 架构风格:

- **RESTful API**: 使用标准的HTTP方法（如GET、POST、PUT、DELETE等）来访问资源，实现构建前后端分离。

### 技术栈:

- **后端**: 
- Java 21
- Spring Boot
- h2database 轻量级内存数据库 
- Spring Data JPA 整合H2，简化数据访问层开发
- Caffeine Cache 本地缓存，提高性能
- Guava BloomFilter 高效地判断一个元素是否可能存在集合中,应对缓存击穿场景。
- **前端**:
- react
- **构建工具**: Maven
- **容器化**: Docker, K8S
### 数据设计：

**数据库**：使用轻量级H2 数据库，可以在内存模式下运行

主要实体: Transaction ：交易

**字段** :

- `id`: 唯一标识符

- `amount`: 交易金额

- `currency`: 货币类型

- `type`: 交易类型

- `category`: 交易种类

- `description`: 交易描述

- `status`: 交易状态

- `createTime`: 创建时间

- `updateTime`: 修改时间

### API 设计：

1. **接口设计**:

- GET /api/transaction

  交易分页列表：

    - 支持参数: 分页（`pageNo` 和 `pageSize`）
    - 返回: 交易记录分页列表
  
- GET /api/transaction/{id}

  交易详情：

    - 支持参数: 交易 ID
    - 返回: 交易详情

- POST /api/transaction

  创建交易：

    - 请求体: `transaction` 对象
    - 返回: 创建的交易对象

- PUT /api/transaction/{id}

  修改交易：

    - 参数: 交易 ID
    - 请求体: 更新的 `transaction` 对象
    - 返回: 更新后的交易对象

- DELETE /api/transaction/{id}

  删除交易：

    - 参数: 交易 ID
    - 返回: 操作结果状态
  
2. **返回和异常处理**:  
- 封装统一返回格式：code,message,data 提供给前端标准化处理。
    - 示例：
```json
{
  "code": 200,
  "msg": "操作成功！",
  "data": {
    "field": "value"
  }
}
```
- 使用spring-boot-starter-validation进行参数校验，并在统一异常处理中解析校验错误信息返回统一格式
  - 示例：
```json
{
    "code": 300,
    "msg": "参数不合法！",
    "data": { "amount": "金额必须为整数" }
}
```

### 缓存机制：

使用Spring的 @Cacheable 、 @CachePut 、@CacheEvict 注解进行缓存管理
配置Caffeine本地缓存，交易列表和详情使用不同的缓存实例，分别设置最大容量和淘汰策略。
- 缓存穿透：使用Guava的BloomFilter类，查询前先检查数据是否存在，并在新增时放入BloomFilter。
- 缓存雪崩：后续优化，可以利用不同的缓存淘汰策略应对，比如：使缓存在不同时间过期。

### 全面测试：

1、单元测试：
使用 JUnit 和 Mockito 对服务层和控制器层进行单元测试。保证服务层和控制器层单元测试单元测试覆盖率。

2、功能测试：书写功能测试用例，并执行。

3、压力测试：使用 Jmeter模拟高并发请求做性能测试，保证服务高可用。

### Docker部署
- 配置 Dockerfile 构建前后台镜像，并启动。
- 后端DockerFile文件：

  FROM openjdk:21

  WORKDIR /app

  COPY transaction-management-0.0.1-SNAPSHOT.jar /app/transaction-management.jar

  EXPOSE 8080

  ENTRYPOINT ["java", "-jar", "/app/transaction-management.jar"]

- 后端镜像指令：
  docker build --no-cache --build-arg jar_file=transaction-management.jar -t transaction-management:latest .
- 后端docker run指令：
  docker run -d --restart always --name transaction-management -p 8080:8080 -v /Users/admin/Documents/log/ -e spring.profiles.active=dev -e TZ=Asia/Shanghai transaction-management:latest

- 前端 docker-compose.yaml文件：
  version: '3'
  services:
  nginx:
  image: nginx:latest
  ports:
  - "8088:80"  # 将容器的80端口映射到宿主机的8088端口
  volumes:
  - /Users/admin/Documents/nginx/conf:/etc/nginx/conf.d:ro  # 挂载配置文件为只读模式
  - /Users/admin/Documents/nginx/html:/usr/share/nginx/html:ro  # 可选：挂载静态文件目录为只读模式（如果有的话）
  restart: unless-stopped  # 在容器退出时总是重启容器

- 前端docker compose指令：
docker compose up -d

### 后续优化
微服务分布式，可以引进redis等中间件

