# Practice 4 验证记录

本文件区分已验证的事实与需要外部环境完成的步骤。

- 已读取课程 practice4 要求，补齐标准单模块 Android 项目及 Debug / Release 工作流。
- Gradle 8.9 Wrapper JAR SHA-256：`498495120a03b9a6ab5d155f5de3c8f0d986a449153702fb80fc80e134484f17`，已与 Gradle 官方校验值比对一致。
- Gradle 分发包配置了官方 SHA-256：`d725d707bfabd4dfdc958c624003b3c80accc03f7037b5122c4b1d0ef15cecab`。
- 本地未发现 JDK、Android SDK 或连接设备，因此本机不能直接运行 Gradle 构建或确认手机安装。

## 外部验收状态

- Debug Actions：等待首次推送运行。
- Release Actions：需要在仓库中配置四项签名 secrets 后运行。
- APK 下载：等待 CI 产物。
- 手机安装及交互：尚未验证。需在 Android 6.0+ 设备执行 README 中的操作。

只有实际完成的项目才能在作业最终清单中勾选；工作流配置存在不等于构建已成功。
