package com.linkdevelpment.newsfeeds.home.data.datasource


import com.linkdevelpment.newsfeeds.core.data.utils.WrappedResponse
import com.linkdevelpment.newsfeeds.home.data.responseremote.ModelNewsRemoteResponse
import retrofit2.Response
import retrofit2.http.*

interface HomeService {

    @GET("articles?")
    suspend fun getArticles1(
        @Query("source") source: String,
        @Query("apiKey") apiKey: String
    ): Response<WrappedResponse<ModelNewsRemoteResponse>>

    @GET("articles?")
    suspend fun getArticles2(
        @Query("source") source: String,
        @Query("apiKey") apiKey: String
    ): Response<WrappedResponse<ModelNewsRemoteResponse>>
}
