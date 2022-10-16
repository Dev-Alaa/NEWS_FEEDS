package com.linkdevelpment.newsfeeds.home.domain.repository

import com.linkdevelpment.newsfeeds.core.data.utils.WrappedResponse
import com.linkdevelpment.newsfeeds.core.presentation.base.BaseResult
import com.linkdevelpment.newsfeeds.home.data.responseremote.ModelNewsRemoteResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface HomeRepository {
    suspend fun getArticles1(
        source: String,
        apiKey: String
    ): Flow<BaseResult<WrappedResponse<ModelNewsRemoteResponse>>>

    suspend fun getArticles2(
        source: String,
        apiKey: String
    ): Flow<BaseResult<WrappedResponse<ModelNewsRemoteResponse>>>



}

