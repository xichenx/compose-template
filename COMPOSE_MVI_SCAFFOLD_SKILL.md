# Compose MVI Scaffold Skill

## 1. Skill Purpose

这份 skill 用于指导 AI 或团队成员在当前仓库中稳定地产出符合规范的 Compose + MVI 脚手架代码、文档和功能模块。

适用场景：

- 初始化项目基础架构
- 新增一个业务 feature
- 补充通用 MVI 基类
- 接入网络层、本地缓存和导航
- 编写测试样例和开发文档

## 2. Skill Inputs

在使用该 skill 时，建议明确提供以下输入：

- 功能名称，例如 `home`、`login`、`profile`
- 页面目标，例如列表页、详情页、表单页
- 数据来源，例如本地、远端、混合缓存
- 是否需要导航、副作用、刷新、分页
- 是否需要测试样例

## 3. Skill Output Standard

输出内容默认应包含以下部分：

### 3.1 新增 feature 时

- `State`
- `Intent`
- `Effect`
- `ViewModel`
- `Screen`
- 必要的 `component/` 组件
- 如有业务逻辑，再增加 `usecase`、`repository interface`、`repository impl`

### 3.2 新增基础能力时

- 对应目录结构
- 依赖声明或版本声明
- 关键实现代码
- 使用方式说明
- 最小可运行样例

## 4. Execution Checklist

每次生成代码前，按以下顺序思考：

1. 先判断改动属于 `ui / domain / data / di / navigation / common` 哪一层。
2. 确认是否已有公共能力可复用，避免重复实现。
3. 为页面先定义 `State / Intent / Effect`，再写 ViewModel。
4. 让 Screen 只处理渲染和事件绑定，不直接承载业务逻辑。
5. 涉及依赖时优先更新 `libs.versions.toml`。
6. 涉及文档时同步更新 `DESIGN_DOCUMENT.md` 或 `DEVELOPMENT_PLAN.md`。

## 5. MVI Generation Template

### 5.1 State Template

```kotlin
data class DemoState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val items: List<String> = emptyList()
)
```

### 5.2 Intent Template

```kotlin
sealed interface DemoIntent {
    data object Initialize : DemoIntent
    data object Refresh : DemoIntent
    data class ItemClick(val id: String) : DemoIntent
}
```

### 5.3 Effect Template

```kotlin
sealed interface DemoEffect {
    data class Navigate(val route: String) : DemoEffect
    data class Toast(val message: String) : DemoEffect
}
```

### 5.4 ViewModel Template

```kotlin
class DemoViewModel : ViewModel() {

    private val _state = MutableStateFlow(DemoState())
    val state: StateFlow<DemoState> = _state.asStateFlow()

    private val _effect = Channel<DemoEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: DemoIntent) {
        when (intent) {
            DemoIntent.Initialize -> {}
            DemoIntent.Refresh -> {}
            is DemoIntent.ItemClick -> {}
        }
    }
}
```

## 6. Prompt Recipes

### 6.1 新增模块

可直接使用：

`请按照当前仓库的 Compose MVI 规则，新增一个 login 模块，包含 State、Intent、Effect、ViewModel、Screen，并补充必要的 use case 和测试样例。`

### 6.2 补基础设施

可直接使用：

`请基于当前脚手架架构，为项目补充 BaseViewModel、UiState、UiIntent、UiEffect，并让 home 模块按新的 MVI 基类改造。`

### 6.3 补数据层

可直接使用：

`请按项目分层规范接入 Retrofit、Room、DataStore，并给出 home 模块从 repository 到 UI 的完整示例。`

## 7. Constraints

- 不跳过状态建模，先定义契约再写 UI。
- 不让 Composable 直接依赖 data 层实现。
- 不把一次性事件放入可持久状态。
- 不在模块内硬编码依赖版本。
- 不为了快速交付破坏项目分层。

## 8. Related Assets

- 设计文档：`DESIGN_DOCUMENT.md`
- 开发计划：`DEVELOPMENT_PLAN.md`
- 架构规则：`.cursor/rules/project-architecture.mdc`
- Kotlin/Compose 规则：`.cursor/rules/kotlin-compose-style.mdc`
- Feature 模板规则：`.cursor/rules/feature-mvi-template.mdc`
