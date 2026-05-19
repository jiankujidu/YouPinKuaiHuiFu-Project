# 优品快回复 API 文档

## 基础信息

- **Base URL**: `http://localhost:3000/api`
- **认证方式**: Bearer Token (JWT)

## 认证相关

### 用户注册
```http
POST /auth/register
Content-Type: application/json

{
  "username": "user123",
  "password": "password123",
  "nickname": "张三"
}
```

### 用户登录
```http
POST /auth/login
Content-Type: application/json

{
  "username": "user123",
  "password": "password123"
}

Response:
{
  "success": true,
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "user": {
    "id": "...",
    "username": "user123",
    "nickname": "张三"
  }
}
```

## 话术管理

### 获取话术列表
```http
GET /phrases?type=company&keyword=欢迎
Authorization: Bearer <token>
```

### 创建话术
```http
POST /phrases
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "欢迎语",
  "content": "欢迎光临，请问有什么可以帮您？",
  "type": "company",
  "categoryId": "cat-001"
}
```

### 更新话术
```http
PUT /phrases/:id
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "欢迎语",
  "content": "欢迎光临！",
  "type": "company"
}
```

### 删除话术
```http
DELETE /phrases/:id
Authorization: Bearer <token>
```

## 团队管理

### 获取团队成员
```http
GET /teams/members
Authorization: Bearer <token>
```

### 创建团队
```http
POST /teams
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "客服一组",
  "description": "负责售前咨询"
}
```

## 数据同步

### 获取最后更新时间
```http
GET /sync/last-update
Authorization: Bearer <token>
```

### 批量同步话术
```http
POST /sync/phrases
Authorization: Bearer <token>
Content-Type: application/json

{
  "phrases": [
    {
      "id": "...",
      "title": "...",
      "content": "...",
      "type": "company",
      "usageCount": 10
    }
  ]
}
```

## 健康检查

```http
GET /api/health

Response:
{
  "status": "ok",
  "timestamp": "2024-01-15T10:30:00.000Z"
}
```
