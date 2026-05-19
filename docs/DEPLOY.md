# 部署指南

## 后端服务部署

### 环境要求
- Node.js >= 16
- MySQL >= 5.7

### 部署步骤

1. **安装依赖**
```bash
cd backend
npm install
```

2. **配置环境变量**
```bash
cp .env.example .env
# 编辑 .env 文件，配置数据库连接信息
```

3. **初始化数据库**
```bash
mysql -u root -p < database.sql
```

4. **启动服务**
```bash
# 开发模式
npm run dev

# 生产模式
npm start
```

### PM2 部署
```bash
npm install -g pm2
pm2 start server.js --name youpin-backend
pm2 save
pm2 startup
```

## PC 管理端部署

### 开发环境
```bash
cd pc-web
npm install
npm run dev
```

### 生产构建
```bash
npm run build
# 构建后的文件在 dist/ 目录
```

### Nginx 配置
```nginx
server {
    listen 80;
    server_name admin.youpin.com;
    
    location / {
        root /var/www/youpin/pc-web/dist;
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:3000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## Android App 发布

### 构建 Release APK
```bash
cd android-app
./gradlew assembleRelease
```

### 签名 APK
```bash
# 生成密钥
keytool -genkey -v -keystore youpin.keystore -alias youpin -keyalg RSA -keysize 2048 -validity 10000

# 签名
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore youpin.keystore app-release-unsigned.apk youpin

# 对齐
zipalign -v 4 app-release-unsigned.apk youpin-kuaihuifu-release.apk
```

## Docker 部署

### 构建镜像
```bash
docker build -t youpin-backend ./backend
docker build -t youpin-pc-web ./pc-web
```

### Docker Compose
```yaml
version: '3'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: youpin_kuaihuifu
    volumes:
      - mysql_data:/var/lib/mysql
      
  backend:
    build: ./backend
    ports:
      - "3000:3000"
    environment:
      DB_HOST: mysql
      DB_USER: root
      DB_PASSWORD: password
      DB_NAME: youpin_kuaihuifu
    depends_on:
      - mysql
      
  pc-web:
    build: ./pc-web
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql_data:
```

## 环境变量说明

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| DB_HOST | 数据库主机 | localhost |
| DB_USER | 数据库用户 | root |
| DB_PASSWORD | 数据库密码 | - |
| DB_NAME | 数据库名称 | youpin_kuaihuifu |
| JWT_SECRET | JWT 密钥 | your-secret-key |
| PORT | 服务端口 | 3000 |
