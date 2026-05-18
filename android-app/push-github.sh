#!/bin/bash
# 优品快回复 - GitHub 推送脚本

set -e

echo "========================================"
echo "   优品快回复 - GitHub 推送脚本"
echo "========================================"
echo ""

# 检查 GitHub Token
if [ -z "$GITHUB_TOKEN" ]; then
    echo "❌ 错误: 未设置 GITHUB_TOKEN 环境变量"
    echo ""
    echo "请按以下步骤操作:"
    echo "1. 访问 https://github.com/settings/tokens"
    echo "2. 点击 'Generate new token (classic)'"
    echo "3. 勾选 'repo' 权限"
    echo "4. 生成并复制 Token"
    echo "5. 运行: export GITHUB_TOKEN=你的token"
    echo ""
    exit 1
fi

REPO_NAME="YouPinKuaiHuiFu"
USER_NAME="jiankujidu"

echo "📝 步骤 1: 创建 GitHub 仓库..."

# 创建仓库
curl -s -X POST \
    -H "Authorization: token $GITHUB_TOKEN" \
    -H "Accept: application/vnd.github.v3+json" \
    https://api.github.com/user/repos \
    -d "{\"name\":\"$REPO_NAME\",\"description\":\"优品快回复 - 微信风格快捷回复工具\",\"private\":false}" \
    > /tmp/repo_create.json

if grep -q "\"message\":" /tmp/repo_create.json; then
    echo "⚠️  仓库可能已存在或创建失败，尝试继续..."
    cat /tmp/repo_create.json
fi

echo ""
echo "🔗 步骤 2: 配置远程仓库..."

# 配置远程仓库（使用 token）
git remote remove origin 2>/dev/null || true
git remote add origin "https://$USER_NAME:$GITHUB_TOKEN@github.com/$USER_NAME/$REPO_NAME.git"

echo ""
echo "📤 步骤 3: 推送代码..."

# 推送代码
git push -u origin master --force

echo ""
echo "✅ 代码推送成功!"
echo ""
echo "📦 仓库地址: https://github.com/$USER_NAME/$REPO_NAME"
echo ""
echo "========================================"
echo "   下一步: 创建 Release 并上传 APK"
echo "========================================"
echo ""
echo "由于 APK 需要通过 Android Studio 编译生成，"
echo "请在本地编译后手动上传到 Release。"
echo ""
echo "编译步骤:"
echo "  1. 用 Android Studio 打开项目"
echo "  2. Build → Build Bundle(s) / APK(s) → Build APK(s)"
echo "  3. 生成的 APK 在 app/build/outputs/apk/debug/"
echo ""
