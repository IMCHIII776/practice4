# Practice 4 — Android APK CI/CD

本项目对应课程 `session-2/practice-4-ci-cd-android-apk.md`。
应用是一个原生 Java 离线计数器，支持加一、清零、关闭重开后恢复计数，无需联网权限。

## 快速使用

仓库：https://github.com/IMCHIII776/practice4

1. 将本目录**内容**作为仓库根目录，确保 `.github/`、`gradle/`、`app/` 全部上传，尤其不要遗漏 `gradle-wrapper.jar`。
2. 推送到 `main` 或 `develop`，或在 Actions 中手动运行 **Android Debug Build**。
3. 等待绿色成功标记，打开该次运行，在 Artifacts 下载 `app-debug`，解压得到 `app-debug.apk`。
4. 将 APK 传到 Android 6.0+ 手机，允许该文件来源安装后打开。点击 Add one，关闭重开检查计数保留，点击 Reset counter 检查归零。

Debug 不依赖任何 secrets。若手机已安装相同包名但签名不同的版本，需要先卸载旧版；卸载会清除计数。

## 声明的构建环境

| 项目 | 版本 |
|---|---|
| Runner | ubuntu-latest |
| JDK | Temurin 17 |
| Android Gradle Plugin | 8.7.3 |
| Gradle Wrapper | 8.9，包含官方 JAR 和 SHA-256 校验 |
| compileSdk / targetSdk | 35 / 35 |
| Build Tools | 34.0.0 |
| minSdk | 23（Android 6.0） |

版本依据：[AGP 8.7 兼容性表](https://developer.android.com/build/releases/agp-8-7-0-release-notes)、[Gradle Wrapper 官方文档](https://docs.gradle.org/current/userguide/gradle_wrapper.html)。

## Debug 流程

文件：`.github/workflows/android-debug.yml`

触发条件：push 到 main/develop、面向 main 的 PR、手动触发。

步骤：Checkout → JDK 17 → Gradle 缓存 → 安装指定 SDK → chmod → `lintDebug assembleDebug` → 上传 APK 和 lint 报告。

APK 路径：`app/build/outputs/apk/debug/app-debug.apk`。保留 7 天；上传路径找不到文件时直接失败。
非 main 引用的缓存只读，PR 不写入共享缓存。

## 签名 Release 配置

文件：`.github/workflows/android-release.yml`

在 GitHub Codespaces 或安装 JDK 的安全终端交互式生成密钥（不要把密码写进命令或提交到 Git）：

```bash
keytool -genkeypair -v -storetype JKS -keystore upload-keystore.jks \
  -alias practice4 -keyalg RSA -keysize 2048 -validity 10000
```

在仓库 Settings → Secrets and variables → Actions → New repository secret 中创建：

| Secret | 填写内容 |
|---|---|
| KEYSTORE_BASE64 | JKS 文件的 Base64 内容 |
| KEYSTORE_PASSWORD | 生成密钥时输入的 keystore 密码 |
| KEY_ALIAS | practice4 |
| KEY_PASSWORD | 密钥密码（与 keystore 密码相同时填同一个） |

Linux/Codespaces 编码：`base64 -w0 upload-keystore.jks`。
macOS 编码：`base64 -i upload-keystore.jks`。
Windows PowerShell（结果写到剪贴板，不打印到终端）：

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes((Resolve-Path './upload-keystore.jks').Path)) | Set-Clipboard
```

Base64 是编码，不是加密。设置 secret 后清空剪贴板，将原始密钥和密码另行安全备份；以后发布更新需使用相同签名密钥。临时云环境中的密钥文件在备份后删除。不要提交 `.jks`、`.keystore` 或密码。

配置完成后在 Actions 手动运行 **Android Release Build**，或推送版本标签：

```bash
git tag v1.0.0
git push origin v1.0.0
```

步骤：Checkout → JDK/缓存/SDK → lintRelease → 检查四个 secrets → 解码密钥到 RUNNER_TEMP → assembleRelease → apksigner verify → 上传 app-release → 始终清理临时密钥。

APK 路径：`app/build/outputs/apk/release/app-release.apk`，保留 14 天。普通分支 push 不运行 Release；缺少 secrets 会明确失败，不能产出冒充签名版的 unsigned APK。
标签本身不会修改 Android 的版本信息；后续正式升级要先修改 `app/build.gradle` 中的 versionCode 和 versionName。
本项目以 Actions Artifact 交付 APK，不自动创建 GitHub Release 页面。

## 可选：本地构建

不需要本地 Android 环境即可使用上述 CI。若本地已有 JDK 17 和 Android SDK 35：

```powershell
./gradlew.bat lintDebug assembleDebug
```

Linux/macOS：`chmod +x gradlew && ./gradlew lintDebug assembleDebug`。
本地 SDK 路径可用 ANDROID_HOME 或不提交的 local.properties 指定。

## 提交材料与验收

源码和两套工作流已经包含在本仓库。实际运行记录、下载及手机安装结果见 `VALIDATION.md`。
作业展示时保留：仓库链接、Debug 成功运行链接、已配置签名后的 Release 成功运行链接，以及手机运行截图。
不要用示例截图代替真实安装记录。
