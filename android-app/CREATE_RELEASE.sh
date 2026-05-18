#!/bin/bash
# 创建 GitHub Release 并上传 APK

set -e

if [ -z "$GITHUB_TOKEN" ]; then
    echo "❌ 请设置 GITHUB_TOKEN 环境变量"
    exit 1
fi

REPO_NAME="YouPinKuaiHuiFu"
USER_NAME="jiankujidu"
VERSION="v1.0.0"
APK_PATH="${1:-app/build/outputs/apk/debug/app-debug.apk}"

echo "📝 创建 Release $VERSION..."

# 创建 Release
RELEASE_RESPONSE=$(curl -s -X POST \
    -H "Authorization: token $GITHUB_TOKEN" \
    -H "Accept: application/vnd.github.v3+json" \
    https://api.github.com/repos/$USER_NAME/$REPO_NAME/releases \
    -d "{
        \"tag_name\":\"$VERSION\",
        \"name\":\"优品快回复 $VERSION\",
        \"body\":\"## 🎉 优品快回复 $VERSION 正式发布\n
### ✨ 功能特性
- 💬 悬浮窗快捷回复
- 🏷️ 三级话术分类（公司/小组/私人）
- 🔍 智能搜索
- 📋 一键复制
- 📊 使用统计
- 🎨 微信风格 UI\n
### 📱 系统要求
- Android 5.0+ (API 21+)\n
### 🚀 安装方式
1. 下载 APK 文件
2. 允许安装未知来源应用
3. 安装并开启悬浮窗权限\n
### 📝 使用说明
1. 首次使用需要授予悬浮窗权限
2. 点击悬浮球唤起话术面板
3. 选择分类查看话术
4. 点击话术自动复制到剪贴板\n
---\n**开发者**: @jiankujidu\n**项目主页**: https://github.com/$USER_NAME/$REPO_NAME\",
        \"draft\":false,
        \"prerelease\":false
    }")

# 提取 upload_url
UPLOAD_URL=$(echo "$RELEASE_RESPONSE" | grep -o '"upload_url": "[^"]*' | cut -d'"' -f4 | sed 's/{?name,label}//')

if [ -z "$UPLOAD_URL" ]; then
    echo "❌ 创建 Release 失败"
    echo "$RELEASE_RESPONSE"
    exit 1
fi

echo "✅ Release 创建成功"
echo ""

# 检查 APK 是否存在
if [ ! -f "$APK_PATH" ]; then
    echo "❌ APK 文件不存在: $APK_PATH"
    echo "请先编译 APK:"
    echo "  ./gradlew assembleDebug"
    exit 1
fi

echo "📤 上传 APK..."

# 上传 APK
curl -s -X POST \
    -H "Authorization: token $GITHUB_TOKEN" \
    -H "Content-Type: application/vnd.android.package-archive" \
    --data-binary @$APK_PATH \
    "$UPLOAD_URL?name=youpin-kuaihuifu-v1.0.0.apk&label=优品快回复%20v1.0.0" \
    > /tmp/upload.json

if grep -q '"browser_download_url"' /tmp/upload.json; then
    DOWNLOAD_URL=$(grep -o '"browser_download_url": "[^"]*' /tmp/upload.json | head -1 | cut -d'"' -f4)
    echo ""
    echo "✅ APK 上传成功!"
    echo ""
    echo "📥 下载地址:"
    echo "   $DOWNLOAD_URL"
    echo ""
else
    echo "❌ APK 上传失败"
    cat /tmp/upload.json
    exit 1
fi

echo ""
echo "🎉 全部完成!"
echo ""
echo "Release 页面: https://github.com/$USER_NAME/$REPO_NAME/releases/tag/$VERSION"
