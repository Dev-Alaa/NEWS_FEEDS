package com.linkdevelpment.newsfeeds.home.domain.interactor


import com.linkdevelpment.newsfeeds.core.data.utils.WrappedResponse
import com.linkdevelpment.newsfeeds.core.presentation.base.BaseResult
import com.linkdevelpment.newsfeeds.home.data.responseremote.ModelNewsRemoteResponse
import com.linkdevelpment.newsfeeds.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsSource2ListUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend fun execute(
        source: String,
        apiKey: String
    ): Flow<BaseResult<WrappedResponse<ModelNewsRemoteResponse>>> {
        return homeRepository.getArticles2(source, apiKey)
    }
}