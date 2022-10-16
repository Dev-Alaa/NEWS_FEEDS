package com.linkdevelpment.newsfeeds.home.presentation.di


import com.linkdevelpment.newsfeeds.core.data.module.NetworkModule
import com.linkdevelpment.newsfeeds.home.data.datasource.HomeService
import com.linkdevelpment.newsfeeds.home.data.repositoryimp.HomeRepositoryImpl
import com.linkdevelpment.newsfeeds.home.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class HomeModule {

    @Singleton
    @Provides
    fun provideHomeApi(retrofit: Retrofit): HomeService {
        return retrofit.create(HomeService::class.java)
    }

    @Singleton
    @Provides
    fun provideHomeRepository(homeService: HomeService): HomeRepository {
        return HomeRepositoryImpl(homeService)
    }

}