package com.linkdevelpment.newsfeeds.home.presentation.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.linkdevelpment.newsfeeds.R
import androidx.navigation.fragment.findNavController
import com.linkdevelpment.newsfeeds.core.presentation.base.BaseFragmentBinding
import com.linkdevelpment.newsfeeds.core.presentation.extentions.showGenericAlertDialog
import com.linkdevelpment.newsfeeds.core.presentation.extentions.showToast
import com.linkdevelpment.newsfeeds.core.presentation.utils.Nav
import com.linkdevelpment.newsfeeds.databinding.FragmentHomeBinding
import com.linkdevelpment.newsfeeds.home.data.responseremote.ModelNewsRemoteResponse
import com.linkdevelpment.newsfeeds.home.presentation.view.adapters.AdapterArticles
import com.linkdevelpment.newsfeeds.home.presentation.view.viewmodels.NewsListViewModel
import com.linkdevelpment.newsfeeds.home.presentation.view.viewmodels.NewsSource1ListState
import com.linkdevelpment.newsfeeds.home.presentation.view.viewmodels.NewsSource2ListState
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FragmentHome : BaseFragmentBinding<FragmentHomeBinding>() {

    private var articlesList1: List<ModelNewsRemoteResponse>? = null
    private var articlesList2: List<ModelNewsRemoteResponse>? = null
    private var allArticles: List<ModelNewsRemoteResponse>? = null


    private val adapter: AdapterArticles by lazy {
        AdapterArticles(onClickedItem = {
            val bundle = Bundle().apply {
                putParcelable(Nav.Article.ARTICLE_MODEL, it)
            }
            findNavController().navigate(R.id.fragmentHomeDetails, bundle)

        })
    }

    val viewModel: NewsListViewModel by lazy {
        ViewModelProvider(requireActivity())[NewsListViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isScreenLoaded.not()) {
            viewModel.getArticles1("associated-press", "533af958594143758318137469b41ba9")
            viewModel.getArticles2("the-next-web", "533af958594143758318137469b41ba9")
        }
        observeStateFlow()

    }

    private fun observeStateFlow() {

        viewModel.newsSource1State
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.newsSource2State
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleStateChangeSource2(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleStateChange(state: NewsSource1ListState) {
        when (state) {
            is NewsSource1ListState.Init -> Unit
            is NewsSource1ListState.ErrorResponse -> handleError(
                state.errorCode,
                state.errorMessage
            )
            is NewsSource1ListState.SuccessResponse -> handleSuccess(state.newsListResponseRemote)
            is NewsSource1ListState.ShowToast -> requireActivity().showToast(
                state.message,
                state.isConnectionError
            )
            is NewsSource1ListState.IsLoading -> handleShimmer(state.isLoading)
        }

    }

    private fun handleSuccess(articles: List<ModelNewsRemoteResponse>?) {
        if (articles?.isEmpty() == true) {
            Toasty.error(requireContext(), getString(R.string.empty), Toast.LENGTH_SHORT, true)
                .show()

        } else {
            articlesList1 = articles
            articlesList1?.let { articlesList2?.let { it1 -> combineAllArticles(it, it1) } }

        }
        viewModel.isScreenLoaded = true

    }

    private fun handleStateChangeSource2(state: NewsSource2ListState) {
        when (state) {
            is NewsSource2ListState.Init -> Unit
            is NewsSource2ListState.ErrorResponse -> handleError(
                state.errorCode,
                state.errorMessage
            )
            is NewsSource2ListState.SuccessResponse -> handleSuccessSource2(state.newsListResponseRemote)
            is NewsSource2ListState.ShowToast -> requireActivity().showToast(
                state.message,
                state.isConnectionError
            )
            is NewsSource2ListState.IsLoading -> handleShimmer(state.isLoading)
        }

    }

    private fun handleSuccessSource2(articles: List<ModelNewsRemoteResponse>?) {
        if (articles?.isEmpty() == true) {
            Toasty.error(requireContext(), getString(R.string.empty), Toast.LENGTH_SHORT, true)
                .show()
        } else {
            articlesList2 = articles
            articlesList1?.let { articlesList2?.let { it1 -> combineAllArticles(it, it1) } }

        }
        viewModel.isScreenLoaded = true

    }

    private fun combineAllArticles(
        articles1: List<ModelNewsRemoteResponse>,
        articles2: List<ModelNewsRemoteResponse>
    ) {
        allArticles = concatenate(articles1, articles2)
        binding.rvNews.adapter = adapter
        adapter.submitList(allArticles)
    }

    private fun handleError(errorCode: Int, errorMessage: String) {
        requireActivity().showGenericAlertDialog(errorMessage, errorCode)
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            showLoading()
        } else {
            dismissLoading()
        }

    }

    private fun handleShimmer(isLoading: Boolean) {
        if (isLoading) {
            startShimmer(binding.shimmerView)
        } else {
            stopShimmer(binding.shimmerView)
        }

    }

    private fun <T> concatenate(vararg lists: List<T>): List<T> {
        val result: MutableList<T> = ArrayList()
        for (list in lists) {
            result += list
        }
        return result
    }


}