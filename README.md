# Compose Template

基于 **Kotlin + Jetpack Compose + Material 3 + MVI** 的原生 Android 单模块脚手架。包含认证、首页、本地缓存、主题和语言切换示例，适合作为新功能的开发起点。

> 默认使用本地 Mock 远端数据，**不是生产级账号系统**。API 地址 `https://example.com/` 是占位值；本项目没有真实网络认证、Token 刷新或持久化演示用户。

## 快速开始

### 环境

- JDK 17（Gradle 运行环境；源码 JVM target 为 11）
- Android SDK Platform 36；minSdk 24
- AGP 8.12.3、Gradle Wrapper 8.13、Kotlin 2.0.21
- SDK 路径通过 Android Studio、`ANDROID_HOME` 或不提交的 `local.properties` 配置
- 首次构建需要能访问 Google Maven、Maven Central 和 Gradle 下载源

```bash
# 如果当前检出没有可执行权限，使用 bash 调用 Wrapper
bash ./gradlew :app:assembleDebug
bash ./gradlew :app:testDebugUnitTest :app:lintDebug
bash ./gradlew :app:assembleDebugAndroidTest

# 连接可用设备/模拟器后执行；会安装应用和测试 APK
bash ./gradlew :app:connectedDebugAndroidTest

# 安装并启动演示 App
bash ./gradlew :app:installDebug
adb shell am start -n com.xichen.dev/.MainActivity
```

APK：`app/build/outputs/apk/debug/app-debug.apk`。包名：`com.xichen.dev`。

### 演示流程

1. 启动时读取本地会话：无会话进入认证入口，有会话进入首页；读取失败显示可重试错误。
2. 登录账号：`demo@compose.dev`，密码：`password123`。表单默认填入演示值。
3. 注册允许除演示邮箱外的符合校验规则的输入，返回模拟会话；**注册用户不能随后通过登录接口再次登录**。
4. 首页包括概览、模块搜索/阶段筛选、个人设置。搜索无匹配时仍可清空查询或取消筛选。
5. 首页刷新失败会保留已显示内容并提示错误；首次加载失败显示重试页。
6. 主题支持跟随系统/浅色/深色；语言支持跟随系统/英语/简体中文，默认英语。

## 项目结构

代码根目录：`app/src/main/java/com/xichen/dev/`。

```text
core/coroutine/      可注入的 AppDispatchers
 di/                Hilt 仓库绑定、网络、数据库和调度器
 domain/
   model/           AppResult、AppError、领域模型
   repository/      数据操作契约
   usecase/         业务入口
   validation/      不依赖 UI 的类型化认证校验
 data/
   common/          异常映射与可取消的结果转换
   local/           Room、DAO、DataStore、Mapper
   remote/          API 声明、Mock 数据源、DTO、Mapper
   repository/      数据操作与错误边界
 ui/
   mvi/             UiState/UiIntent/UiEffect、BaseViewModel、PagedListState
   common/          通用组件、UiText、认证组件及校验文案映射
   feature/         splash、authentry、login、register、home
   navigation/      AppRoute、AppNavHost
   theme/           颜色、字体、间距、圆角、阴影
```

### 数据流与约定

```text
用户行为 → Intent → ViewModel → UseCase → Repository → 数据源
                          ↓
                    StateFlow → Screen
                          ↓
                     Effect → Route 中执行导航/提示
```

- `Route` 收集状态和消费 Effect，`Screen` 只渲染并转发操作。
- 复用 `BaseViewModel.setState()`、`sendEffect()`、`launchOn()`；不重复创建状态流。
- 一次性仓库操作返回 `AppResult<T>`，平台异常在 data 层转为 `AppError`，UseCase 直接传递结果。
- `appResultOf` 重新抛出 `CancellationException`，不把取消或 JVM 致命错误转换成普通失败。
- 领域校验返回 `AuthValidationError`，UI 层通过 `toUiTextOrNull()` 转成资源文案。
- 首页缓存优先：有缓存且非强制刷新时不请求远端。替换缓存使用 Room 事务；数据库与 DataStore 元数据更新不是跨存储事务。
- 初始化/重试需避免重复请求；刷新失败与首次加载失败采用不同展示策略。
- 列表空态不要移除搜索和筛选控件；表单需可滚动并处理 IME Insets。

## 新增 Feature

1. 在 `ui/feature/<name>/` 定义 `State : UiState`、`Intent : UiIntent` 和需要的 `Effect : UiEffect`。
2. ViewModel 继承 `BaseViewModel<State, Effect>`，实现 `onIntent()`。
3. 使用领域仓库接口和 UseCase；不要让 UI 直接引用 data 层实现。
4. 新增数据源/Mapper/仓库实现时在 `di/RepositoryModule.kt` 绑定，异步依赖复用 `AppDispatchers`。
5. 编写无状态 Screen 和负责状态收集的 Route，在 `AppNavHost` 注册路由。
6. 补资源、Preview 和成功/失败/重试/取消回归测试，再更新文档。

可参考 `HomeViewModel`、`LoginViewModel`、`ScaffoldRepositoryImpl` 以及 `.cursor/rules/`。

## 测试与验证

- JVM 单测：认证校验、可取消结果转换、仓库缓存策略、登录/注册、首页、启动重试和 MVI 基类。
- ViewModel 测试复用 `testutil/MainDispatcherRule` 与 fake 仓库；具体数据源交互使用 MockK。
- Android 测试：首页搜索无匹配后恢复、错误页重试、短视口表单滚动、Room 缓存替换/失败回滚。
- 报告：`app/build/reports/tests/testDebugUnitTest/`、`app/build/reports/lint-results-debug.html`；设备测试报告在 `app/build/reports/androidTests/connected/`。

2026-10-15 本轮验证：**33 项 JVM 单测、6 项 Android 真机测试通过**（含原默认测试）。真机测试在 Android 10 设备上验证了搜索恢复、错误重试、短视口表单滚动和 Room 事务回滚；Debug 应用/测试 APK 构建及 Lint 任务通过，Lint 仍有警告。完整登录导航、真机语言切换、重启会话恢复和真实键盘视觉验收尚未完成。

## 生产接入前的边界

- Mock 数据源需替换或按构建变体绑定真实实现，并配置真实环境地址。
- 会话仅根据 Token 非空判断登录；未接过期检查、刷新、登出 UI 或请求认证拦截器。
- Preferences DataStore **不提供加密**；会话文件已从云备份和设备迁移中排除，这不能替代 Token 安全设计。
- HTTP 日志 Debug 为 BASIC，Release 关闭；仍应避免记录密码和 Token。
- 首帧语言通过 Activity 的一次同步 DataStore 读取应用，读取 I/O 失败回退默认英语；已删除 Application 重复初始化，但没有彻底消除启动线程等待。
- App Bundle 禁用语言拆分以保留内置中英文；后续可迁移 Android per-app locale 支持。
- 有分页状态/组件，但没有端到端分页业务；没有 CI、崩溃上报、性能监控或多模块依赖隔离。
- Room schema 导出与迁移策略、KSP 迁移、依赖升级和剩余 Lint 警告需按实际项目推进。

更多设计与后续任务见 [DESIGN_DOCUMENT.md](DESIGN_DOCUMENT.md)、[DEVELOPMENT_PLAN.md](DEVELOPMENT_PLAN.md)。
