# Compose MVI 架构脚手架 - 详细设计文档

## 1. 项目概述

### 1.1 项目目标
创建一个基于 Jetpack Compose 和 MVI（Model-View-Intent）架构的 Android 项目脚手架，旨在：
- 提供标准化的项目结构和代码组织方式
- 减少新项目的前期配置工作
- 实现清晰的数据流和状态管理
- 支持快速开发和迭代
- 遵循 Android 开发最佳实践

### 1.2 核心特性
- ✅ **MVI 架构模式**：单向数据流，状态可预测
- ✅ **Jetpack Compose**：现代化 UI 开发
- ✅ **依赖注入**：使用 Hilt/Dagger
- ✅ **网络请求**：Retrofit + OkHttp
- ✅ **本地存储**：Room + DataStore
- ✅ **异步处理**：Kotlin Coroutines + Flow
- ✅ **导航管理**：Navigation Compose
- ✅ **主题系统**：Material 3 动态颜色
- ✅ **错误处理**：统一异常处理机制
- ✅ **日志系统**：结构化日志记录
- ✅ **单元测试**：完整的测试框架

## 2. 架构设计

### 2.1 MVI 架构模式

MVI（Model-View-Intent）是一种响应式架构模式，具有以下特点：

```
Intent (用户意图) → ViewModel (处理逻辑) → State (状态) → View (UI)
```

#### 2.1.1 核心组件

1. **Intent（意图）**
   - 用户操作（点击、输入等）
   - 系统事件（生命周期、网络状态等）
   - 内部事件（定时器、后台任务等）

2. **Model（模型）**
   - UI State：界面状态
   - Side Effect：副作用（导航、Toast、Dialog 等）

3. **View（视图）**
   - Compose UI 组件
   - 观察 State 并响应变化
   - 发送 Intent 到 ViewModel

4. **ViewModel**
   - 处理 Intent
   - 管理 State
   - 执行业务逻辑
   - 触发 Side Effect

### 2.2 架构分层

```
┌─────────────────────────────────────┐
│         Presentation Layer          │
│  (UI Components, ViewModels, State) │
┌─────────────────────────────────────┐
│          Domain Layer               │
│    (Use Cases, Business Logic)      │
┌─────────────────────────────────────┐
│           Data Layer                │
│  (Repository, DataSource, Models)   │
┌─────────────────────────────────────┐
```

#### 2.2.1 Presentation Layer（表现层）
- **UI Components**：Compose 可组合函数
- **ViewModels**：状态管理和业务逻辑处理
- **State**：UI 状态数据类
- **Intent**：用户意图密封类

#### 2.2.2 Domain Layer（领域层）
- **Use Cases**：单一职责的业务用例
- **Entities**：领域模型
- **Interfaces**：Repository 接口定义

#### 2.2.3 Data Layer（数据层）
- **Repository**：数据仓库实现
- **Remote DataSource**：网络数据源
- **Local DataSource**：本地数据源（Room、DataStore）
- **DTOs**：数据传输对象

## 3. 技术栈

### 3.1 核心框架

| 技术 | 版本 | 用途 |
|------|------|------|
| Kotlin | 2.0.21+ | 开发语言 |
| Jetpack Compose | 2024.09.00+ | UI 框架 |
| Material 3 | Latest | 设计系统 |
| AndroidX Core | Latest | 核心库 |

### 3.2 架构组件

| 组件 | 版本 | 用途 |
|------|------|------|
| ViewModel | 2.8.6+ | 状态管理 |
| Lifecycle | 2.8.6+ | 生命周期感知 |
| Navigation Compose | 2.8.6+ | 导航管理 |
| Hilt | 2.52+ | 依赖注入 |

### 3.3 网络与数据

| 库 | 版本 | 用途 |
|----|------|------|
| Retrofit | 2.11.0+ | REST API 客户端 |
| OkHttp | 4.12.0+ | HTTP 客户端 |
| Gson/Moshi | Latest | JSON 序列化 |
| Room | 2.6.1+ | 本地数据库 |
| DataStore | 1.1.1+ | 键值对存储 |

### 3.4 异步处理

| 库 | 版本 | 用途 |
|----|------|------|
| Kotlin Coroutines | 1.9.0+ | 协程支持 |
| Kotlin Flow | 1.9.0+ | 响应式流 |

### 3.5 工具库

| 库 | 版本 | 用途 |
|----|------|------|
| Coil | 2.7.0+ | 图片加载 |
| Timber | 5.0.1 | 日志记录 |
| LeakCanary | 2.14 | 内存泄漏检测（Debug） |

### 3.6 测试框架

| 库 | 版本 | 用途 |
|----|------|------|
| JUnit | 4.13.2+ | 单元测试 |
| Mockito | 5.11.0+ | Mock 框架 |
| Turbine | 1.1.0 | Flow 测试 |
| Compose Testing | Latest | UI 测试 |

## 4. 项目结构

### 4.1 目录结构

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/xichen/dev/
│   │   │   ├── di/                    # 依赖注入模块
│   │   │   │   ├── AppModule.kt
│   │   │   │   ├── DatabaseModule.kt
│   │   │   │   ├── NetworkModule.kt
│   │   │   │   └── RepositoryModule.kt
│   │   │   │
│   │   │   ├── data/                  # 数据层
│   │   │   │   ├── local/             # 本地数据源
│   │   │   │   │   ├── database/      # Room 数据库
│   │   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   │   └── dao/       # DAO 接口
│   │   │   │   │   ├── datastore/     # DataStore
│   │   │   │   │   │   └── PreferencesManager.kt
│   │   │   │   │   └── entity/        # 数据库实体
│   │   │   │   │
│   │   │   │   ├── remote/            # 远程数据源
│   │   │   │   │   ├── api/           # API 接口
│   │   │   │   │   │   └── ApiService.kt
│   │   │   │   │   ├── dto/           # 数据传输对象
│   │   │   │   │   └── mapper/        # DTO 到 Entity 映射
│   │   │   │   │
│   │   │   │   ├── repository/        # 数据仓库实现
│   │   │   │   │   └── RepositoryImpl.kt
│   │   │   │   │
│   │   │   │   └── model/             # 数据模型
│   │   │   │
│   │   │   ├── domain/                # 领域层
│   │   │   │   ├── model/             # 领域模型
│   │   │   │   ├── repository/        # Repository 接口
│   │   │   │   └── usecase/           # 用例
│   │   │   │       └── GetDataUseCase.kt
│   │   │   │
│   │   │   ├── ui/                    # 表现层
│   │   │   │   ├── theme/             # 主题配置
│   │   │   │   │   ├── Color.kt
│   │   │   │   │   ├── Type.kt
│   │   │   │   │   └── Theme.kt
│   │   │   │   │
│   │   │   │   ├── navigation/        # 导航配置
│   │   │   │   │   ├── NavGraph.kt
│   │   │   │   │   └── Screen.kt
│   │   │   │   │
│   │   │   │   ├── common/            # 通用 UI 组件
│   │   │   │   │   ├── LoadingIndicator.kt
│   │   │   │   │   ├── ErrorView.kt
│   │   │   │   │   └── EmptyView.kt
│   │   │   │   │
│   │   │   │   └── feature/           # 功能模块
│   │   │   │       └── home/          # 示例：首页模块
│   │   │   │           ├── HomeScreen.kt
│   │   │   │           ├── HomeViewModel.kt
│   │   │   │           ├── HomeState.kt
│   │   │   │           ├── HomeIntent.kt
│   │   │   │           └── component/ # 功能内组件
│   │   │   │
│   │   │   ├── util/                  # 工具类
│   │   │   │   ├── Constants.kt
│   │   │   │   ├── Extensions.kt
│   │   │   │   └── Logger.kt
│   │   │   │
│   │   │   └── MainActivity.kt
│   │   │
│   │   └── res/                       # 资源文件
│   │
│   ├── test/                          # 单元测试
│   │   └── java/com/xichen/dev/
│   │       ├── domain/
│   │       ├── data/
│   │       └── ui/
│   │
│   └── androidTest/                   # 集成测试
│       └── java/com/xichen/dev/
│
├── build.gradle.kts
└── proguard-rules.pro
```

## 5. 核心模块设计

### 5.1 MVI 状态管理

#### 5.1.1 State 设计

```kotlin
// 基础 UI 状态接口
interface UiState {
    val isLoading: Boolean
    val error: String?
}

// 具体功能状态
data class HomeState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val data: List<Item> = emptyList(),
    val isRefreshing: Boolean = false
) : UiState
```

#### 5.1.2 Intent 设计

```kotlin
// 基础 Intent 接口
sealed interface UiIntent

// 具体功能 Intent
sealed interface HomeIntent : UiIntent {
    data object LoadData : HomeIntent
    data object Refresh : HomeIntent
    data class OnItemClick(val id: String) : HomeIntent
}
```

#### 5.1.3 Side Effect 设计

```kotlin
// 副作用（导航、Toast 等）
sealed interface UiSideEffect {
    data class NavigateTo(val route: String) : UiSideEffect
    data class ShowToast(val message: String) : UiSideEffect
    data class ShowDialog(val dialog: DialogData) : UiSideEffect
}
```

#### 5.1.4 ViewModel 模板

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDataUseCase: GetDataUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    
    private val _sideEffect = Channel<UiSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()
    
    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadData -> loadData()
            is HomeIntent.Refresh -> refresh()
            is HomeIntent.OnItemClick -> navigateToDetail(intent.id)
        }
    }
    
    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            getDataUseCase()
                .onSuccess { data ->
                    _state.update { 
                        it.copy(isLoading = false, data = data) 
                    }
                }
                .onFailure { error ->
                    _state.update { 
                        it.copy(isLoading = false, error = error.message) 
                    }
                }
        }
    }
}
```

### 5.2 依赖注入（Hilt）

#### 5.2.1 Application 类

```kotlin
@HiltAndroidApp
class MyApplication : Application()
```

#### 5.2.2 模块设计

- **AppModule**：应用级依赖（Context、Logger 等）
- **NetworkModule**：网络相关（Retrofit、OkHttp）
- **DatabaseModule**：数据库相关（Room、DataStore）
- **RepositoryModule**：Repository 实现绑定

### 5.3 网络层设计

#### 5.3.1 API Service

```kotlin
interface ApiService {
    @GET("endpoint")
    suspend fun getData(): Response<DataDto>
}
```

#### 5.3.2 错误处理

```kotlin
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
}
```

### 5.4 数据层设计

#### 5.4.1 Repository 模式

```kotlin
interface DataRepository {
    fun getData(): Flow<Result<List<Data>>>
    suspend fun refreshData(): Result<Unit>
}
```

#### 5.4.2 数据源优先级

1. 先检查本地缓存
2. 请求网络数据
3. 更新本地缓存
4. 返回数据

### 5.5 导航设计

#### 5.5.1 路由定义

```kotlin
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }
}
```

#### 5.5.2 导航图

```kotlin
@Composable
fun NavGraph(navController: NavController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Detail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            DetailScreen(id = id)
        }
    }
}
```

## 6. 开发规范

### 6.1 命名规范

- **类名**：PascalCase（`HomeViewModel`）
- **函数/变量**：camelCase（`loadData()`）
- **常量**：UPPER_SNAKE_CASE（`API_BASE_URL`）
- **包名**：全小写，单词间用点分隔

### 6.2 文件组织

- 每个功能模块独立文件夹
- ViewModel、State、Intent 放在同一模块下
- 通用组件放在 `ui/common` 目录

### 6.3 代码风格

- 遵循 Kotlin 官方编码规范
- 使用 `ktlint` 或 `detekt` 进行代码检查
- 函数保持单一职责
- 避免过深的嵌套（最多 3 层）

### 6.4 注释规范

- 公共 API 必须添加 KDoc 注释
- 复杂业务逻辑添加行内注释
- TODO 注释需包含任务描述和负责人

## 7. 最佳实践

### 7.1 Compose 最佳实践

1. **状态提升**：将状态提升到合适的层级
2. **重组优化**：使用 `remember`、`derivedStateOf` 减少重组
3. **性能优化**：使用 `LazyColumn` 处理长列表
4. **预览函数**：为每个 Composable 添加 `@Preview`

### 7.2 MVI 最佳实践

1. **单向数据流**：Intent → ViewModel → State → View
2. **不可变状态**：使用 `data class` 和 `copy()` 更新状态
3. **副作用隔离**：导航、Toast 等通过 SideEffect 处理
4. **状态最小化**：只保存 UI 需要的状态

### 7.3 错误处理

1. **统一错误类型**：定义 `AppError` 密封类
2. **用户友好提示**：将技术错误转换为用户可理解的提示
3. **错误恢复**：提供重试机制
4. **错误日志**：记录详细错误信息用于调试

### 7.4 测试策略

1. **单元测试**：测试 ViewModel、UseCase、Repository
2. **UI 测试**：测试关键用户流程
3. **集成测试**：测试模块间交互
4. **覆盖率目标**：核心业务逻辑 > 80%

## 8. 实施计划

### 8.1 第一阶段：基础架构搭建（Week 1）

- [ ] 配置项目依赖（Hilt、Retrofit、Room 等）
- [ ] 搭建基础项目结构
- [ ] 实现依赖注入模块
- [ ] 配置主题系统
- [ ] 实现导航框架

### 8.2 第二阶段：核心功能实现（Week 2）

- [ ] 实现网络层（Retrofit + OkHttp）
- [ ] 实现数据层（Room + DataStore）
- [ ] 实现 Repository 模式
- [ ] 创建 MVI 基础类（State、Intent、ViewModel 模板）
- [ ] 实现错误处理机制

### 8.3 第三阶段：示例功能开发（Week 3）

- [ ] 开发示例功能模块（如首页）
- [ ] 实现完整的 MVI 流程
- [ ] 添加加载、错误、空状态处理
- [ ] 实现下拉刷新、上拉加载更多

### 8.4 第四阶段：工具和优化（Week 4）

- [ ] 添加日志系统
- [ ] 实现工具类和扩展函数
- [ ] 添加单元测试示例
- [ ] 性能优化和代码审查
- [ ] 编写使用文档

## 9. 示例代码结构

### 9.1 完整功能模块示例

```
feature/home/
├── HomeScreen.kt          # UI 界面
├── HomeViewModel.kt       # 状态管理
├── HomeState.kt           # 状态定义
├── HomeIntent.kt          # 意图定义
├── HomeSideEffect.kt      # 副作用定义
└── component/             # 子组件
    ├── HomeItem.kt
    └── HomeHeader.kt
```

### 9.2 数据流示例

```
User Click → HomeIntent.LoadData 
    ↓
HomeViewModel.handleIntent()
    ↓
UseCase.execute()
    ↓
Repository.getData()
    ↓
RemoteDataSource.fetch() / LocalDataSource.get()
    ↓
HomeState.update(data)
    ↓
HomeScreen recompose
```

## 10. 后续扩展

### 10.1 可选功能

- [ ] 多语言支持（i18n）
- [ ] 深色模式完整支持
- [ ] 性能监控（Firebase Performance）
- [ ] 崩溃报告（Firebase Crashlytics）
- [ ] 分析统计（Firebase Analytics）
- [ ] 推送通知
- [ ] 应用内更新

### 10.2 架构演进

- [ ] 模块化拆分（多模块架构）
- [ ] 动态特性模块（Dynamic Feature）
- [ ] 组件化架构
- [ ] 微服务后端集成

## 11. 参考资料

- [Android 官方架构指南](https://developer.android.com/topic/architecture)
- [Jetpack Compose 文档](https://developer.android.com/jetpack/compose)
- [MVI 架构模式](https://www.raywenderlich.com/mvi-architecture-tutorial-for-android-getting-started)
- [Kotlin 编码规范](https://kotlinlang.org/docs/coding-conventions.html)

---

**文档版本**：v1.0  
**最后更新**：2024年  
**维护者**：开发团队
