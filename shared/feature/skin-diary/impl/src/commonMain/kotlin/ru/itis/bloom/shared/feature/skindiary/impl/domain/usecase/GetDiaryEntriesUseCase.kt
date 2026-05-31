package ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryRepository
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntriesPageResponse

internal class GetDiaryEntriesUseCase(
    private val repository: SkinDiaryRepository
) {
    operator fun invoke(
        fromDate: LocalDate?,
        toDate: LocalDate?,
        sort: String,
        page: Int,
        size: Int
    ): Flow<Result<DiaryEntriesPageResponse>> {
        return repository.getEntriesFlow(
            fromDate = fromDate,
            toDate = toDate,
            sort = sort,
            page = page,
            size = size
        )
    }
}