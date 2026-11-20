# Practice 4 验证记录

验证日期：2026-11-20（Asia/Shanghai）。

## 已完成

- 完整 Android Java 离线计数器项目；支持加一、清零、持久保存。
- Debug：main/develop push、面向 main 的 PR 和手动触发；JDK 17、Gradle 缓存、指定 Android SDK、严格 lint、APK 构建、签名验证、artifact 上传。
- Release：v* 标签或手动触发；四项 secrets 检查、临时解码 keystore、lint 和签名构建、签名验证、artifact 上传、始终清理密钥。按用户要求暂不运行 Release。
- 官方 Gradle 8.9 Wrapper JAR SHA-256 已核对：`498495120a03b9a6ab5d155f5de3c8f0d986a449153702fb80fc80e134484f17`。
- XML 语法、Wrapper JAR 完整性、工作流 LF 换行、git diff --check 检查通过。

## 实际 CI 结果

成功运行：[Android Debug Build #6](https://github.com/IMCHIII776/practice4/actions/runs/44661850288)

构建提交：`d636cc0`。

- GitHub 工作流结论：SUCCESS。
- `lintDebug assembleDebug`：通过。
- lint 文本报告：`No issues found.`
- `apksigner verify --verbose`：通过（工作流中为必要步骤）。
- `app-debug` 和 `lint-debug-report` artifacts：已上传。
- 两项 artifacts：均已实际下载；公开产物通过 nightly.link 取得。
- APK ZIP 完整性及 AndroidManifest.xml、classes.dex、resources.arsc 检查：通过。
- APK 大小：15,381 字节（原生 Java 应用，没有第三方运行时依赖）。
- APK SHA-256：`9f1b6f0f550429eb3326e1d9dac17b884a3a25f610bacc4d25cf45d5c73bde38`。

## 尚未验证

- Release secrets 未配置；按用户选择保留流程，暂不进行签名 Release 验证。
- 手机安装及交互尚未验证：本机未发现 Android SDK 或连接设备。请将 app-debug.apk 安装至 Android 6.0+ 手机，验证加一、清零、关闭后重开保存，以及屏幕旋转后的计数恢复。
- APK 签名和文件完整性检查不能替代真实设备安装测试。

## 本次修复

实际 runner 上 sdkmanager 不在 PATH，改用完整路径；严格 lint 发现的问题均已修复：补齐图标、Android 12+ 数据迁移规则、旧 Android 备份设置，并将背景颜色放入主题以消除重复绘制。
