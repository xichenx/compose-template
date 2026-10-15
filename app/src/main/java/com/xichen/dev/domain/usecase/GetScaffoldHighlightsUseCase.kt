package com.xichen.dev.domain.usecase

import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.model.ScaffoldSummary
import com.xichen.dev.domain.repository.ScaffoldRepository
import com.xichen.dev.domain.usecase.base.BaseSuspendUseCase
import javax.inject.Inject

data class GetScaffoldHighlightsParams(
    val forceRefresh: Boolean = false
)

class GetScaffoldHighlightsUseCase @Inject constructor(
    private val scaffoldRepository: ScaffoldRepository
) : BaseSuspendUseCase<GetScaffoldHighlightsParams, ScaffoldSummary>() {
    override suspend fun execute(params: GetScaffoldHighlightsParams): AppResult<ScaffoldSummary> {
        return scaffoldRepository.getScaffoldSummary(params.forceRefresh)
    }
}
