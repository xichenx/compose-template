# Compose MVI 脚手架开发计划

## 1. 目标与边界

以单模块原生 Android Compose 项目作为可复制开发起点，优先保证现有功能的状态、失败恢复、数据一致性与测试，而不是增加框架复杂度。

本轮保留 Mock 后端、UI 风格与已验证依赖组合。真实账号系统、Token 刷新、分页闭环、多模块拆分、监控和 CI 不纳入本轮交付。

## 2. 当前任务状态（2026-10-15）

### 基础工程

- [x] Gradle Version Catalog 与 Compose/Hilt/网络/存储依赖
- [x] Application、MainActivity、AppNavHost、AppRoute
- [x] State/Intent/Effect 标记接口和 BaseViewModel
- [x] UI/Domain/Data 分包与仓库接口
- [x] 通用反馈、表单、列表和主题参数组件
- [x] 主题偏好、内置中英文和语言切换
- [x] 首页窗口宽度自适应导航
- [ ] 模块级依赖隔离（后续按规模拆分）

### 本轮稳定性改造

- [x] 搜索无结果时保留搜索和阶段筛选
- [x] 初次加载失败可重试，刷新失败保留已显示列表
- [x] 语言/主题操作失败反馈
- [x] Splash 会话读取失败状态及重试、防止重复导航
- [x] 认证页纵向滚动和 IME Insets
- [x] Room 缓存替换事务
- [x] AppResult 数据层错误边界和 CancellationException 传播
- [x] 领域校验与 UI 文案映射分离
- [x] 移除 Application 重复语言初始化，Activity I/O 失败回退默认语言
- [x] Release HTTP 日志关闭、会话备份/迁移排除
- [x] App Bundle 保留内置语言资源
- [ ] 完全非阻塞语言启动 / Android per-app locale 迁移
- [ ] Room 与 DataStore 元数据跨存储一致性设计

### 测试与文档

- [x] coroutines-test、MainDispatcherRule、fake 仓库和 MockK
- [x] 校验、结果/取消、仓库、首页、登录、注册、Splash、MVI 单测
- [x] 首页搜索恢复/错误重试、短视口认证表单和 Room 回滚 Android 测试代码
- [x] README 运行说明、功能边界和扩展步骤
- [x] 设计文档、开发计划与生成约定同步
- [x] Android UI/Room 测试设备执行（Android 10，6 项通过）
- [ ] 真机重启会话、语言/主题切换、小屏/横屏/IME 视觉验收
- [ ] 覆盖率统计（不以测试条数代替覆盖率）

## 3. 本轮验证结果

已执行并通过：

```bash
bash ./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug :app:assembleDebugAndroidTest
bash ./gradlew :app:connectedDebugAndroidTest :app:lintDebug :app:assembleDebug
```

33 项 JVM 单测无失败、无跳过，应用和 Android 测试 APK 构建成功。Lint 任务通过但仍有依赖升级建议、未用资源、Modifier 参数位置、Kapt/KSP 等警告；没有宣称零告警。

6 项 Android 真机测试在 Android 10 设备上通过，无失败、无跳过，覆盖搜索恢复、错误重试、短视口滚动及 Room 回滚。完整登录导航、语言/主题、重启会话和真实键盘视觉验收未完成。最终验证以交付报告和本地 `app/build/reports/` 为准。

## 4. 后续优先级

### P0：完成设备验收

- [x] 连接设备执行 `:app:connectedDebugAndroidTest`（Android 10）
- [ ] 演示登录 → 首页无结果搜索 → 清空恢复 → 刷新
- [ ] 重启会话、语言/主题切换、小屏和键盘状态确认

### P1：生产接入基础

- [ ] 明确真实 API 契约、环境地址和 Mock/Real 变体绑定
- [ ] 会话有效期、刷新、失效清理、请求认证与登出 UI
- [ ] Token 存储安全方案（DataStore 不等于加密）
- [ ] Room schema 导出与迁移验证
- [ ] 自动化 JVM/Lint/APK 构建检查

### P2：按实际需求演进

- [ ] KSP 和依赖兼容性升级，分别验证而非同时盲升
- [ ] 剩余 Lint/资源清理和本地化复数支持
- [ ] 端到端分页、权限、图片加载等业务能力
- [ ] 覆盖率、性能和崩溃监控
- [ ] 业务增长后拆模块；不提前引入空模块和复杂状态框架

## 5. 交付资产

- `README.md`：运行、验证、扩展与 Mock 边界
- `DESIGN_DOCUMENT.md`：当前架构及行为契约
- `DEVELOPMENT_PLAN.md`：已交付与待验收任务
- `.cursor/rules/*.mdc`：开发约定
- `COMPOSE_MVI_SCAFFOLD_SKILL.md`：feature 模板与生成检查清单
- `app/src/test/`、`app/src/androidTest/`：回归示例

一期定位为可运行、可测试的演示脚手架，不是可直接上线的生产认证系统。
