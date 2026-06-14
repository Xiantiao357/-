# 电商管理系统

前后端分离的电商项目，实现用户登录和订单列表管理功能。

## 技术栈

### 后端
- **Spring Boot 3.2.0** - Web应用框架
- **MyBatis Plus 3.5.5** - ORM框架
- **JWT (jjwt 0.12.3)** - Token认证
- **Spring Security** - 安全框架
- **H2 内存数据库** - 开发测试数据库
- **Spring AOP** - 日志切面

### 前端
- **Vue 3.4.21** - 渐进式JavaScript框架
- **Vite 5.4.21** - 新一代前端构建工具
- **Vue Router 4.3.0** - Vue官方路由管理器
- **Axios** - HTTP请求库
- **Element Plus 2.6.1** - Vue3 UI组件库

## 项目结构

```
登录验证查询及前后端交互/
├── backend/                                    # 后端项目
│   ├── src/main/java/com/example/ecommerce/
│   │   ├── controller/                        # 控制器层
│   │   │   ├── UserController.java            # 用户控制器
│   │   │   └── OrderController.java           # 订单控制器
│   │   ├── service/                           # 服务层
│   │   │   ├── UserService.java               # 用户服务接口
│   │   │   ├── impl/
│   │   │   │   ├── UserServiceImpl.java       # 用户服务实现
│   │   │   │   └── OrderServiceImpl.java      # 订单服务实现
│   │   │   └── OrderService.java               # 订单服务接口
│   │   ├── mapper/                             # 数据访问层
│   │   │   ├── UserMapper.java                # 用户Mapper
│   │   │   └── OrderMapper.java               # 订单Mapper
│   │   ├── entity/                             # 实体类
│   │   │   ├── User.java                      # 用户实体
│   │   │   └── Order.java                     # 订单实体
│   │   ├── config/                             # 配置类
│   │   │   ├── SecurityConfig.java            # Spring Security配置
│   │   │   ├── WebConfig.java                 # Web配置（拦截器、CORS）
│   │   │   ├── JwtInterceptor.java            # JWT拦截器
│   │   │   ├── DataInitializer.java           # 数据初始化器
│   │   │   └── RequestLogAspect.java          # 请求日志切面
│   │   ├── common/                             # 通用类
│   │   │   ├── Result.java                    # 统一返回格式
│   │   │   └── GlobalExceptionHandler.java    # 全局异常处理
│   │   ├── util/                              # 工具类
│   │   │   └── JwtUtil.java                   # JWT工具类
│   │   └── EcommerceApplication.java          # 启动类
│   └── src/main/resources/
│       ├── application.yml                    # 应用配置
│       ├── schema.sql                         # 数据库表结构
│       └── mapper/
│           └── UserMapper.xml                  # MyBatis XML映射
│
└── frontend/                                  # 前端项目
    ├── src/
    │   ├── api/                               # API接口模块
    │   │   └── index.js                       # 统一API管理
    │   ├── views/                             # 页面组件
    │   │   ├── Login.vue                      # 登录页面
    │   │   └── OrderList.vue                  # 订单列表页面
    │   ├── router/                            # 路由配置
    │   │   └── index.js                       # 路由守卫
    │   ├── utils/                             # 工具函数
    │   │   └── axios.js                       # Axios配置
    │   ├── App.vue                            # 根组件
    │   └── main.js                            # 入口文件
    ├── package.json                           # 依赖配置
    ├── vite.config.js                         # Vite配置
    └── index.html                             # HTML模板
```

## 功能特性

### ✅ 已实现功能

1. **用户认证**
   - 用户登录（JWT Token认证）
   - 用户注册
   - Token拦截器验证
   - 路由守卫保护

2. **订单管理**
   - 订单列表查询（分页）
   - 订单状态展示
   - 订单金额格式化

3. **系统功能**
   - 统一返回结果格式
   - 全局异常处理
   - 请求日志记录
   - CORS跨域支持

4. **前端优化**
   - API接口统一管理
   - 请求/响应拦截器
   - 表单验证
   - Loading状态
   - 错误提示
   - Token过期自动跳转

## 启动说明

### 1. 启动后端服务

```powershell
# 进入后端目录
cd C:\Users\Zj\Desktop\登录验证查询及前后端交互\backend

# 编译项目
mvn clean compile

# 启动服务
mvn spring-boot:run
```

服务将在 `http://localhost:8081` 启动

### 2. 启动前端服务

```powershell
# 进入前端目录
cd C:\Users\Zj\Desktop\登录验证查询及前后端交互\frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

服务将在 `http://localhost:5173` 启动

## 测试账号

- **用户名**: `admin`
- **密码**: `admin123`

## API 接口文档

### 用户接口

| 接口 | 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|------|
| 登录 | POST | `/api/user/login` | 用户登录 | ❌ |
| 注册 | POST | `/api/user/register` | 用户注册 | ❌ |
| 用户信息 | GET | `/api/user/info` | 获取用户信息 | ✅ |

#### 登录接口

**请求**
```json
POST /api/user/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzM4NCJ9...",
    "userId": 1,
    "username": "admin"
  }
}
```

### 订单接口

| 接口 | 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|------|
| 订单列表 | POST | `/api/order/list` | 订单分页查询 | ✅ |

#### 订单列表接口

**请求**
```json
POST /api/order/list
Content-Type: application/json
Authorization: Bearer <token>

{
  "userId": 1,
  "pageNum": 1,
  "pageSize": 10
}
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 1,
        "orderNo": "ORD202401010001",
        "totalAmount": 99.99,
        "status": "COMPLETED",
        "createTime": "2026-06-14T20:35:26",
        "updateTime": "2026-06-14T20:35:26"
      }
    ],
    "total": 5,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

## 代码规范

### 后端规范

1. **分层架构**: Controller → Service → Mapper
2. **统一返回格式**: `Result<T>` 类
3. **异常处理**: `GlobalExceptionHandler`
4. **日志记录**: SLF4J + Logback
5. **参数校验**: Jakarta Validation

### 前端规范

1. **Vue3 Composition API**: 使用 `<script setup>`
2. **API管理**: 统一的API模块
3. **Axios封装**: 请求/响应拦截器
4. **表单验证**: Element Plus表单验证
5. **组件化**: 清晰的组件结构

## 当前服务状态

✅ **后端服务**: 运行中 - `http://localhost:8081`  
✅ **前端服务**: 运行中 - `http://localhost:5173`

## 验证结果

```
✅ 登录接口正常 - POST /api/user/login
✅ 订单列表接口正常 - POST /api/order/list
✅ Token拦截器正常工作
✅ 路由守卫正常工作
✅ 请求日志切面正常
✅ 全局异常处理正常
```

## 项目亮点

1. **安全性**: JWT Token认证 + Spring Security
2. **可维护性**: 分层架构 + 统一API管理
3. **可扩展性**: 预留了注册、订单详情等接口
4. **用户体验**: Loading状态、错误提示、自动跳转
5. **代码质量**: 完整的注释、规范的命名、统一的返回格式

## 注意事项

1. 后端使用H2内存数据库，重启服务后数据会重置
2. JWT Token有效期为24小时
3. 前端请求自动携带Token，Token无效时自动跳转登录页
4. 所有接口返回统一格式，便于前端处理
