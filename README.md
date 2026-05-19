# 优品快回复 (YouPinKuaiHuiFu)

一个客服快捷回复系统，包含 Android App、后端服务和 PC 管理端。

## 📱 项目结构

```
YouPinKuaiHuiFu-Project/
├── android-app/          # Android 客户端
├── backend/              # Node.js 后端服务
├── pc-web/               # Vue3 PC 管理后台
└── docs/                 # 文档
```

## 🎯 功能特性

### Android App
- 💬 **话术库管理**：公司话术、小组话术、私人话术三级分类
- 🔍 **智能搜索**：快速搜索话术内容
- 📋 **一键复制**：点击话术自动复制到剪贴板
- 🎈 **悬浮窗快捷回复**：悬浮球快速访问话术
- 📊 **使用统计**：记录话术使用次数
- ⚙️ **系统设置**：悬浮窗开关、清除缓存

### 后端服务
- 👤 **用户系统**：注册、登录、JWT 认证
- 📝 **话术管理**：CRUD 操作、分类管理
- 👥 **团队管理**：团队成员、话术共享
- 🔄 **数据同步**：多端数据同步

### PC 管理端
- 📋 **话术管理**：批量管理话术
- 👥 **团队管理**：成员管理、权限控制
- 📈 **数据统计**：使用统计、热门话术
- ⚙️ **系统设置**：全局配置

## 🚀 快速开始

### Android App
```bash
cd android-app
# 使用 Android Studio 打开项目
# 或命令行编译
./gradlew assembleDebug
```

### 后端服务
```bash
cd backend
npm install
cp .env.example .env
# 配置数据库信息
npm run dev
```

### PC 管理端
```bash
cd pc-web
npm install
npm run dev
```

## 📥 下载安装

- **Android APK**: [下载地址](https://github.com/jiankujidu/YouPinKuaiHuiFu-Project/releases/download/v1.0.0/youpin-kuaihuifu-v1.0.0.apk)

## 🛠️ 技术栈

| 端 | 技术 |
|---|------|
| Android | Java, AndroidX, Room, RecyclerView |
| 后端 | Node.js, Express, MySQL |
| PC 端 | Vue3, Element Plus, Pinia |

## 📄 开源协议

MIT License
