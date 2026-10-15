# Compose MVI 脚手架设计

## 1. 定位与范围

本项目是原生 Android **单模块 Compose + Material 3 + MVI + Clean Architecture 分包**脚手架，用认证和首页示例演示可复制的数据流。

已实现：Hilt、导航、轻量 MVI、Room 缓存、DataStore 偏好和会话、主题、语言切换、类型化校验、失败反馈与重试，以及 JVM/Android 测试样例。

**未实现：**真实后端认证、Token 刷新、持久化演示账号、端到端分页、模块级依赖隔离、CI 和监控。Retrofit/OkHttp 已配置，但远端数据源仍返回 Mock 数据，不能以“已接依赖”代替“业务已接入”。

## 2. 技术与构建

| 技术 | 当前配置 |
|------|----------|
| Kotlin / Compose Compiler 插件 | 2.0.21 |
| AGP / Gradle Wrapper | 8.12.3 / 8.13 |
| Android SDK | min 24，compile/target 36 |
| Gradle 运行时 / 源码 JVM target | JDK 17 / 11 |
| Compose BOM | 2024.09.03 |
| Lifecycle / Navigation Compose | 2.8.7 / 2.8.6 |
| Hilt | 2.52 |
| Retrofit / OkHttp | 2.11.0 / 4.12.0 |
| Room / Preferences DataStore | 2.6.1 / 1.1.1 |
| Coroutines / coroutines-test | 1.9.0 |
| 单测 | JUnit 4.13.2、MockK 1.13.13 |
| Android 测试 | AndroidJUnit4、Compose Testing |

依赖版本以 `gradle/libs.versions.toml` 为准。本轮保留已验证的版本组合，不全面升级。Coil、Timber、LeakCanary 和 Turbine 尚未接入。

## 3. 分层与边界

代码根目录：`app/src/main/java/com/xichen/dev/`。

- `ui/`：State、Intent、Effect、ViewModel、Route、Screen、组件、资源映射和设计参数。
- `domain/`：领域模型、仓库接口、UseCase、纯类型化校验。不引用 UI 类型、`R`、Context 或 Retrofit。
- `data/`：远端、本地数据源、DTO/Entity Mapper、仓库实现、平台异常映射和语言资源管理。
- `di/`：依赖组装；`core/coroutine/AppDispatchers.kt`：可注入调度器。
- `MainActivity` 是平台入口，语言 Context 包装属于入口适配，不能由 feature Composable 直接调用 data 层。

这是单模块分包约束，不是 Gradle 编译期隔离。业务增长后可据此拆模块，不为当前示例增加多余抽象。

## 4. 状态与副作用

```text
Intent → ViewModel → UseCase → Repository → 数据源
             ↓
         StateFlow → Screen 渲染
             ↓
          Effect → Route 消费导航/提示
```

### 4.1 MVI 基类

`ui/mvi/BaseViewModel.kt` 为 `BaseViewModel<S : UiState, E : UiEffect>`，提供：

- `state: StateFlow<S>`，`setState { copy(...) }` 原子更新。
- `effect: Flow<E>`，内部使用 `Channel.BUFFERED`，`sendEffect` 发送一次性事件。
- `launchOn()` 使用 `viewModelScope`，可指定调度器。

`UiState`、`UiIntent`、`UiEffect` 是标记接口，不强制所有页面有相同字段；每个 ViewModel 实现自己的 `onIntent()`，没有公共 Intent 总线。

### 4.2 页面职责

- `Route` 使用 `collectAsStateWithLifecycle` 收集 State，在 `LaunchedEffect` 中消费 Effect。
- `Screen` 只接收 State/回调；Preview 不构造真实 ViewModel。
- 每页只保留一个 Effect 消费者。当前 Effect 收集与 composition 生命周期绑定，不保证后台导航或事件跨进程恢复。
- 加载/重试在派发异步任务前标记状态以阻止重复请求。
- 导航和短暂提示走 Effect；确认对话框等需持续渲染的内容可以保存在 State，不把它当作可丢弃消息。

## 5. 数据操作契约

### 5.1 结果边界

一次性仓库操作返回 `AppResult<T>`：`Success(data)` 或 `Error(AppError)`。持续偏好观察使用 `Flow`。UseCase 直接传递仓库结果，不嵌套 `AppResult`。

`data/common/appResultOf` 捕获普通 Exception 并调用 `Throwable.toAppError()`；**CancellationException 必须重新抛出**，JVM Error 不转换成用户错误。不要使用无差别 `runCatching` 吞取消。

主题观察仅对 I/O 读取失败给默认值；其他异常与取消继续传播。会话读取失败返回 Error，不当作“未登录”或删除已有会话。

### 5.2 首页缓存

`ScaffoldRepositoryImpl.getScaffoldSummary(forceRefresh)`：

1. 读取 Room；有缓存且非强制刷新时直接返回。
2. 否则调用 Mock 远端数据源；失败或空响应不覆盖旧缓存。
3. 成功后通过 DAO 的 `@Transaction replaceAll()` 原子删除/插入。
4. 更新 DataStore 同步时间和指南标记，返回领域模型。

数据库与 DataStore 元数据**不是跨存储事务**，不承诺同步时间与列表在异常情况下永远一致。当前也不是 stale-while-revalidate 策略。

### 5.3 认证与会话

- 登录仅识别 `demo@compose.dev / password123`。
- 注册模拟成功并保存会话，不保存演示账号/密码，不能用于随后再次登录。
- 会话基于 Token 非空判断存在；没有有效期、服务端失效处理和刷新。
- 会话文件 `files/datastore/auth_session.preferences_pb` 从旧版备份、云备份和设备迁移排除。
- Preferences DataStore 不提供加密；真实账号接入还需要凭据安全和请求认证设计。

### 5.4 校验

`AuthValidator` 返回 `ValidationResult.Valid` 或 `Invalid(AuthValidationError)`。UI 的 `ui/common/auth/AuthValidationUiText.kt` 负责资源映射。

保留演示规则：邮箱非空并符合现有正则，密码非空且至少 6 个字符；注册确认密码和昵称由对应 ViewModel 校验。该规则不等于真实账号的安全策略。

## 6. 页面恢复与适配

- 首页首次加载失败显示 `ErrorContent` 和重试；已有内容刷新失败保留列表，通过 Snackbar 提示。
- 模块搜索/筛选始终留在列表结构内；只有结果区显示空态，用户能清空关键词或取消阶段筛选。
- Splash 有独立加载/错误状态，失败可重试；成功后只导航一次。
- 设置写入失败保留当前选择并发送反馈；语言保存成功才触发 Activity 重建。
- 首页按 WindowWidthSizeClass 切换 BottomBar、NavigationRail、PermanentDrawer。
- 认证页保留宽屏双栏，内容可纵向滚动并处理 IME Insets；具体设备键盘布局仍需真机验收。

## 7. 主题、语言与网络配置

- Material 3 动态色（Android 12+）、浅/深/跟随系统主题；共享间距、圆角、阴影和语义色。
- 默认英语，提供内置中英文资源与跟随系统选项。
- `MainActivity.attachBaseContext()` 首帧前同步读取语言，I/O 失败回退默认值。Application 已不重复初始化语言，但仍有一次同步桥接；完整异步初始化/Android per-app locale 迁移另行设计。
- App Bundle 禁用语言拆分，使应用内切换不依赖临时下载语言包。
- Retrofit 地址仍为 `https://example.com/`，本轮不调用真实后端。
- HTTP 日志 Debug 为 BASIC，Release 为 NONE。

## 8. 测试与当前验证

### 8.1 JVM

`app/src/test/` 覆盖：领域校验、取消传播、仓库缓存/失败策略、登录/注册状态、首页刷新/筛选/设置失败、Splash 重试以及 MVI 基类。ViewModel 使用 `MainDispatcherRule` 和 fake 仓库；具体数据源交互用 MockK。

### 8.2 Android

`app/src/androidTest/` 覆盖：首页搜索无结果后仍可清空、错误页重试、短视口表单滚动、Room 替换与插入失败回滚。Room 测试用内存数据库和拒绝插入的 SQLite trigger 验证真正事务回滚。

### 8.3 验证记录（2026-10-15）

- 33 项 JVM 单测通过（包含原默认单测）。
- `testDebugUnitTest`、`lintDebug`、`assembleDebug`、`assembleDebugAndroidTest` 通过；Lint 仍有警告，不能称“零告警”。
- 6 项 Android 真机测试在 Android 10 设备上通过（包含原默认测试），验证搜索恢复、错误重试、短视口表单滚动和 Room 事务回滚。
- 完整登录导航、语言/主题切换、重启会话、真实键盘视觉验收尚未完成。
- 未收集覆盖率，不声称达到 80%。构建命令与报告路径见 README。

## 9. 后续演进

优先补设备验收、真实/Mock 变体绑定、真实会话生命周期、Room schema/迁移和自动化构建；然后依据实际需求处理 KSP、依赖升级、多模块与监控。保留已验证数据流，不以扩充基类数量作为完善目标。

**文档版本：**v1.1；**更新日期：**2026-10-15。此文档描述当前实现，具体任务状态见 `DEVELOPMENT_PLAN.md`。
