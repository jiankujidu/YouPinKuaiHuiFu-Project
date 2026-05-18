# 优品快回复 - 微信风格快捷回复工具

一款仿微信界面风格的 Android 快捷回复工具，支持悬浮窗快速发送常用话术。

## 📱 功能特性

| 功能 | 描述 |
|------|------|
| 💬 **悬浮窗** | 悬浮球快速唤起，支持拖拽吸附 |
| 🏷️ **三级分类** | 公司话术 / 小组话术 / 私人话术 |
| 🔍 **智能搜索** | 关键词快速搜索话术 |
| 📋 **一键复制** | 点击话术直接复制到剪贴板 |
| 📊 **使用统计** | 记录每条话术的使用次数 |
| 🎨 **微信风格** | 完全仿微信 UI 设计 |

## 🎨 界面预览

```
┌─────────────────────────────┐
│      快捷回复               │  ← 微信风格标题栏
├─────────────────────────────┤
│ 全部 │ 公司 │ 小组 │ 私人   │  ← Tab 切换
├─────────────────────────────┤
│ ┌─────────────────────────┐ │
│ │ 您好，欢迎咨询！        │ │  ← 话术卡片
│ │ [常用回复]    12次使用 📋│ │
│ └─────────────────────────┘ │
│ ┌─────────────────────────┐ │
│ │ 稍等，我查一下          │ │
│ │ [团队话术]     5次使用 📋│ │
│ └─────────────────────────┘ │
│                             │
│           [ + ]             │  ← 添加按钮
└─────────────────────────────┘

┌─────────────────────────────┐
│      快捷回复        ⚙️ ✕   │  ← 悬浮窗
├─────────────────────────────┤
│ 公司话术 │ 小组话术 │ 私人话术│
├─────────────────────────────┤
│ 🔍 搜索话术...              │
├─────────────────────────────┤
│ 您好，欢迎咨询！            │
│ [常用回复]                  │
│ ─────────────────────────── │
│ 稍等，我查一下              │
│ [团队话术]                  │
└─────────────────────────────┘
```

## 🚀 快速开始

### 环境要求
- Android Studio 2022.1 或更高版本
- Android SDK 21+ (Android 5.0+)
- Java 8 或更高版本

### 编译安装

```bash
# 克隆项目
git clone https://github.com/jiankujidu/YouPinKuaiHuiFu.git
cd YouPinKuaiHuiFu

# 使用 Android Studio 打开
# File → Open → 选择项目目录

# 编译 APK
./gradlew assembleDebug

# 安装到设备
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 📁 项目结构

```
QuickReplyWeChat/
├── app/
│   ├── src/main/java/com/quickreply/wechat/
│   │   ├── QuickReplyApp.java          # 应用入口
│   │   ├── model/
│   │   │   ├── Phrase.java             # 话术模型
│   │   │   └── Category.java           # 分类模型
│   │   ├── database/
│   │   │   ├── AppDatabase.java        # Room数据库
│   │   │   ├── PhraseDao.java          # 话术DAO
│   │   │   └── CategoryDao.java        # 分类DAO
│   │   ├── service/
│   │   │   └── FloatingWindowService.java # 悬浮窗服务
│   │   ├── ui/
│   │   │   ├── MainActivity.java       # 主界面
│   │   │   ├── PhraseEditActivity.java # 话术编辑
│   │   │   ├── CategoryManageActivity.java # 分类管理
│   │   │   └── SettingsActivity.java   # 设置
│   │   └── adapter/
│   │       ├── PhraseAdapter.java      # 话术适配器
│   │       ├── CategoryAdapter.java    # 分类适配器
│   │       └── FloatingPhraseAdapter.java # 悬浮窗适配器
│   ├── src/main/res/
│   │   ├── layout/                     # 布局文件
│   │   ├── drawable/                   # 图形资源
│   │   ├── values/                     # 颜色、字符串、样式
│   │   └── anim/                       # 动画
│   └── build.gradle                    # 模块配置
├── build.gradle                        # 项目配置
├── settings.gradle                     # 设置
└── README.md                           # 说明文档
```

## 🎯 核心功能说明

### 悬浮窗权限
应用需要 `SYSTEM_ALERT_WINDOW` 权限来显示悬浮窗：

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

Android 6.0+ 需要动态申请权限：
```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    if (!Settings.canDrawOverlays(context)) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        startActivity(intent);
    }
}
```

### 数据存储
使用 Room 数据库存储话术数据：

```java
@Entity(tableName = "phrases")
public class Phrase {
    @PrimaryKey
    private String id;
    private String content;
    private String categoryId;
    private String categoryType; // company/team/private
    private int useCount;
    // ...
}
```

### 话术分类
- **公司话术** (绿色 #07C160) - 企业统一话术
- **小组话术** (蓝色 #10AEFF) - 团队共享话术
- **私人话术** (橙色 #FF9500) - 个人收藏话术

## 🛠️ 技术栈

| 技术 | 用途 |
|------|------|
| Java | 开发语言 |
| Android SDK | 原生开发 |
| Room | 本地数据库 |
| Material Design | UI 组件 |
| Service | 悬浮窗后台服务 |

## 📋 更新日志

### v1.0.0 (2024-01-15)
- ✅ 悬浮窗快捷回复
- ✅ 三级话术分类
- ✅ 搜索功能
- ✅ 使用统计
- ✅ 微信风格 UI

## 📄 开源协议

MIT License

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📧 联系

- GitHub: [@jiankujidu](https://github.com/jiankujidu)
- Email: jiankujidu@gmail.com
