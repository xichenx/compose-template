package com.xichen.dev.ui.mvi

import com.xichen.dev.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun reducerUpdatesStateAndEffectCanBeConsumedOnce() = runTest {
        val vm = CounterViewModel()
        vm.increment()
        vm.increment()
        assertEquals(2, vm.state.value.count)
        vm.notifyCount()
        advanceUntilIdle()
        assertEquals(CountEffect(2), vm.effect.first())
    }

    private data class CounterState(val count: Int = 0) : UiState
    private data class CountEffect(val count: Int) : UiEffect
    private class CounterViewModel : BaseViewModel<CounterState, CountEffect>(CounterState()) {
        fun increment() = setState { copy(count = count + 1) }
        fun notifyCount() = sendEffect { CountEffect(currentState.count) }
    }
}
