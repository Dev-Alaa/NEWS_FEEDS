package com.linkdevelpment.newsfeeds.home.data.repositoryimp


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.linkdevelpment.newsfeeds.core.data.utils.WrappedResponse
import com.linkdevelpment.newsfeeds.core.presentation.base.BaseResult
import com.linkdevelpment.newsfeeds.home.data.datasource.HomeService
import com.linkdevelpment.newsfeeds.home.data.responseremote.ModelNewsRemoteResponse
import com.linkdevelpment.newsfeeds.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeService: HomeService) :
    HomeRepository {

    override suspend fun getArticles1(
        source: String,
        apiKey: String
    ): Flow<BaseResult<WrappedResponse<ModelNewsRemoteResponse>>> {
        return flow {
            val response = homeService.getArticles1(source, apiKey)
            if (response.isSuccessful) {
                val body = response.body()!!
                emit(BaseResult.DataState(body))

            } else {
                val type = object : TypeToken<WrappedResponse<ModelNewsRemoteResponse>>() {}.type
                val err: WrappedResponse<ModelNewsRemoteResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.ErrorState(err.code, err.status))
            }
        }
    }

    override suspend fun getArticles2(
        source: String,
        apiKey: String
    ): Flow<BaseResult<WrappedResponse<ModelNewsRemoteResponse>>> {
        return flow {
            val response = homeService.getArticles2(source, apiKey)
            if (response.isSuccessful) {
                val body = response.body()!!
                emit(BaseResult.DataState(body))

            } else {
                val type = object : TypeToken<WrappedResponse<ModelNewsRemoteResponse>>() {}.type
                val err: WrappedResponse<ModelNewsRemoteResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.ErrorState(err.code, err.status))
            }
        }
    }
}


