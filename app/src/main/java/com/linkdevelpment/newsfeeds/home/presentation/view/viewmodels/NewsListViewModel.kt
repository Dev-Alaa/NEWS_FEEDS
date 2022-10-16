package com.linkdevelpment.newsfeeds.home.presentation.view.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkdevelpment.newsfeeds.core.presentation.base.BaseResult
import com.linkdevelpment.newsfeeds.home.data.responseremote.ModelNewsRemoteResponse
import com.linkdevelpment.newsfeeds.home.domain.interactor.NewsSource1ListUseCase
import com.linkdevelpment.newsfeeds.home.domain.interactor.NewsSource2ListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val newsSource1ListUseCase: NewsSource1ListUseCase,
    private val newsSource2ListUseCase: NewsSource2ListUseCase
) :
    ViewModel() {

    private val _newsSource1State =
        MutableStateFlow<NewsSource1ListState>(NewsSource1ListState.Init)
    val newsSource1State: StateFlow<NewsSource1ListState> get() = _newsSource1State

    private val _newsSource2State =
        MutableStateFlow<NewsSource2ListState>(NewsSource2ListState.Init)
    val newsSource2State: StateFlow<NewsSource2ListState> get() = _newsSource2State


    //  var listSavedInvoices: MutableList<Value>? = null


    var isScreenLoaded = false


    fun getArticles1(
        source: String,
        apiKey: String
    ) {
        viewModelScope.launch {
            newsSource1ListUseCase.execute(source, apiKey)
                .onStart {
                    setLoadingArticles1()
                }
                .catch { exception ->
                    hideLoadingArticles1()
                    showToastArticles1(
                        exception.message.toString(),
                        exception is UnknownHostException
                    )
                }
                .collect {
                    hideLoadingArticles1()
                    when (it) {
                        is BaseResult.ErrorState -> _newsSource1State.value =
                            NewsSource1ListState.ErrorResponse(it.errorCode, it.errorMessage)
                        is BaseResult.DataState -> {
                            _newsSource1State.value =
                                it.items?.articles.let { it1 ->
                                    NewsSource1ListState.SuccessResponse(
                                        it1
                                    )
                                }
                        }
                    }
                }
        }
    }

    fun getArticles2(
        source: String,
        apiKey: String
    ) {
        viewModelScope.launch {
            newsSource2ListUseCase.execute(source, apiKey)
                .onStart {
                    setLoadingArticles2()
                }
                .catch { exception ->
                    hideLoadingArticles2()
                    showToastArticles2(
                        exception.message.toString(),
                        exception is UnknownHostException
                    )
                }
                .collect {
                    hideLoadingArticles2()
                    when (it) {
                        is BaseResult.ErrorState -> _newsSource2State.value =
                            NewsSource2ListState.ErrorResponse(it.errorCode, it.errorMessage)
                        is BaseResult.DataState -> {
                            _newsSource2State.value =
                                it.items?.articles.let { it1 ->
                                    NewsSource2ListState.SuccessResponse(
                                        it1
                                    )
                                }
                        }
                    }
                }
        }
    }


    private fun setLoadingArticles1() {
        _newsSource1State.value = NewsSource1ListState.IsLoading(true)
    }

    private fun hideLoadingArticles1() {
        _newsSource1State.value = NewsSource1ListState.IsLoading(false)
    }

    private fun showToastArticles1(message: String, isConnectionError: Boolean) {
        _newsSource1State.value = NewsSource1ListState.ShowToast(message, isConnectionError)
    }

    private fun setLoadingArticles2() {
        _newsSource2State.value = NewsSource2ListState.IsLoading(true)
    }

    private fun hideLoadingArticles2() {
        _newsSource2State.value = NewsSource2ListState.IsLoading(false)
    }

    private fun showToastArticles2(message: String, isConnectionError: Boolean) {
        _newsSource2State.value = NewsSource2ListState.ShowToast(message, isConnectionError)
    }

}

sealed class NewsSource1ListState {
    object Init : NewsSource1ListState()
    data class IsLoading(val isLoading: Boolean) : NewsSource1ListState()
    data class ShowToast(val message: String, val isConnectionError: Boolean) :
        NewsSource1ListState()

    data class SuccessResponse(val newsListResponseRemote: List<ModelNewsRemoteResponse>?) :
        NewsSource1ListState()

    data class ErrorResponse(val errorCode: Int, val errorMessage: String) : NewsSource1ListState()
}

sealed class NewsSource2ListState {
    object Init : NewsSource2ListState()
    data class IsLoading(val isLoading: Boolean) : NewsSource2ListState()
    data class ShowToast(val message: String, val isConnectionError: Boolean) :
        NewsSource2ListState()

    data class SuccessResponse(val newsListResponseRemote: List<ModelNewsRemoteResponse>?) :
        NewsSource2ListState()

    data class ErrorResponse(val errorCode: Int, val errorMessage: String) : NewsSource2ListState()
}
