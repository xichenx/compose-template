# Compose MVI 脚手架开发计划

## 1. 目标定义

本计划用于把 `DESIGN_DOCUMENT.md` 中的架构设计落成一个可执行、可迭代、可复用的 Compose Android 脚手架项目。最终产物应满足以下目标：

- 新项目初始化后即可直接进入业务开发，不再重复手写基础层代码。
- 默认采用 `Compose + Material 3 + MVI + Clean Architecture`。
- 具备开箱即用的 `网络层 / 本地存储 / 导航 / 状态管理 / 错误处理 / 测试` 能力。
- 支持后续平滑演进到多模块架构。

## 2. 范围边界

### 2.1 本期范围

- 项目基础依赖和版本管理
- 通用 MVI 基础设施
- 单模块 App 架构骨架
- 示例业务模块 `home`
- 网络、本地缓存、偏好设置基础能力
- 主题、导航、日志、错误处理
- 基础单元测试模板
- Cursor rules 和 skill 资产

### 2.2 暂不纳入

- 多模块拆分
- 动态特性模块
- Firebase 全家桶
- 复杂权限中心
- CI/CD 流水线
- 代码生成器或 Gradle 插件化模板

## 3. 里程碑

### M1：项目骨架可运行

目标：应用可启动，主题、导航、基础目录结构完成。

交付物：
- 依赖版本统一管理
- `Application`、`MainActivity`、`NavGraph`
- 主题系统
- 目录结构初始化

验收标准：
- 项目可编译运行
- 首页可正常展示
- 支持深浅色主题切换

### M2：MVI 基础设施完成

目标：形成可复用的状态、事件、副作用、基类 ViewModel。

交付物：
- `UiIntent`
- `UiState`
- `UiEffect`
- `BaseViewModel`
- 通用加载/错误/空态组件

验收标准：
- 新功能可基于模板快速创建
- 所有 UI 状态通过 `StateFlow` 驱动
- 一次性事件通过 `Channel/Flow` 下发

### M3：数据层可用

目标：完成网络、本地数据库、DataStore 和 Repository 基础能力。

交付物：
- Retrofit + OkHttp 配置
- Room 数据库模板
- DataStore 偏好存储
- Result/Error 模型
- Repository 模板

验收标准：
- 示例模块能够发起请求并渲染数据
- 本地缓存与刷新逻辑可运行
- 错误态具备统一转换与展示能力

### M4：示例模块闭环

目标：完成一个完整业务模块，验证脚手架设计有效。

交付物：
- `home` 示例模块
- 列表、刷新、点击事件、副作用跳转
- 预览函数与测试样例

验收标准：
- 形成完整 `Intent -> ViewModel -> State -> UI` 链路
- 关键交互可测试
- 新成员可按样例复制扩展

### M5：工程化收尾

目标：让脚手架具备实际团队交付价值。

交付物：
- 日志与调试能力
- Lint / 格式化建议
- README 或使用说明
- Cursor rules / skill

验收标准：
- 文档可指导新项目接入
- AI 和人工协作时输出一致
- 代码结构符合既定约束

## 4. 工作拆解

### 4.1 基础工程

- 升级并统一 `libs.versions.toml` 中的依赖版本
- 增加 `lifecycle-viewmodel-compose`、`navigation-compose`
- 增加 `hilt`、`retrofit`、`okhttp`、`room`、`datastore`
- 增加测试依赖 `mockk` 或 `mockito`、`turbine`

### 4.2 应用启动层

- 新增 `App.kt` 作为 `@HiltAndroidApp` 入口
- 改造 `MainActivity`，只负责承载根导航和根主题
- 添加 `AppNavHost`、`AppRoute`

### 4.3 通用 MVI 基建

- 设计 `UiIntent`、`UiState`、`UiEffect`
- 设计 `BaseViewModel<State, Intent, Effect>`
- 提供 `reduce`、`sendEffect`、`handleIntent` 统一范式
- 统一加载、错误、空态状态字段

### 4.4 数据层

- 新增 `remote/api`
- 新增 `remote/dto`
- 新增 `local/database`
- 新增 `local/datastore`
- 新增 `repository`
- 定义领域层接口，数据层仅负责实现

### 4.5 领域层

- 明确 `model` 与 `usecase` 边界
- 每个 UseCase 仅处理单一业务动作
- Repository 接口放在 `domain/repository`

### 4.6 UI 层

- 以 `feature/home` 作为模板模块
- Screen 不直接持有业务逻辑
- ViewModel 负责状态流与副作用
- 组件拆分优先无状态组件

### 4.7 测试与质量

- 为 `BaseViewModel` 编写基础单测
- 为 `HomeViewModel` 编写状态流测试
- 为 Repository 编写成功/失败分支测试
- 为 `HomeScreen` 编写基础 Compose UI 测试

## 5. 推荐开发顺序

1. 完成依赖与版本治理。
2. 建立目录结构和 Application 入口。
3. 完成导航与主题根节点。
4. 实现 MVI 基类与通用 UI 状态组件。
5. 接入 Hilt。
6. 接入 Retrofit / OkHttp / Room / DataStore。
7. 编写 `home` 示例模块。
8. 补齐测试与文档。

## 6. 任务清单

### P0

- [ ] 补全工程依赖与插件
- [ ] 搭建标准目录结构
- [ ] 完成 MVI 基类
- [ ] 接入 Hilt
- [ ] 完成导航骨架
- [ ] 完成 `home` 模块样板

### P1

- [ ] 接入 Retrofit + OkHttp
- [ ] 接入 Room + DataStore
- [ ] 完成统一错误模型
- [ ] 完成日志封装
- [ ] 添加测试样例

### P2

- [ ] 补充更多通用组件
- [ ] 预留多模块拆分边界
- [ ] 增强脚手架 README

## 7. 交付清单

- `DESIGN_DOCUMENT.md`
- `DEVELOPMENT_PLAN.md`
- `.cursor/rules/*.mdc`
- `.cursor/skills/compose-mvi-scaffold-skill.md`
- 可运行的 Compose MVI 骨架代码
- 示例模块与测试样例

## 8. 风险与应对

| 风险 | 说明 | 应对策略 |
|------|------|----------|
| 依赖版本不兼容 | Compose、Kotlin、AGP 联动较强 | 固定 BOM 与 Kotlin 版本，逐步升级 |
| MVI 过度模板化 | 可能导致样板代码增多 | 用基类和约定减少重复 |
| 单模块后期膨胀 | 功能增加后目录复杂 | 提前约束 package 结构，保留多模块迁移点 |
| 状态设计不清 | 容易把业务和 UI 状态混在一起 | 强制区分 State、Effect、Domain Model |

## 9. 完成定义

满足以下条件视为脚手架一期完成：

- 项目可直接作为新业务项目起点使用
- 新增一个 feature 时只需复制模板并替换领域模型
- 首页模块具备完整的 MVI 演示能力
- 核心路径具备基础测试
- 文档、rules、skill 三类资产齐备
