package com.xichen.dev.domain.usecase.base

import com.xichen.dev.domain.model.AppResult

object NoParams

abstract class BaseSuspendUseCase<in Params, Result> {
    suspend operator fun invoke(params: Params): AppResult<Result> {
        return execute(params)
    }

    protected abstract suspend fun execute(params: Params): AppResult<Result>
}

abstract class BaseSuspendNoParamsUseCase<Result> {
    suspend operator fun invoke(): AppResult<Result> {
        return execute()
    }

    protected abstract suspend fun execute(): AppResult<Result>
}
