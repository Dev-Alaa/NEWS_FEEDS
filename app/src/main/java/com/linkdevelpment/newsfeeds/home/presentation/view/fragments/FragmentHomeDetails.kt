package com.linkdevelpment.newsfeeds.home.presentation.view.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.linkdevelpment.newsfeeds.R
import com.linkdevelpment.newsfeeds.core.presentation.base.BaseFragmentBinding
import com.linkdevelpment.newsfeeds.core.presentation.extentions.loadImage
import com.linkdevelpment.newsfeeds.core.presentation.extentions.reFormatDate
import com.linkdevelpment.newsfeeds.core.presentation.utils.Nav
import com.linkdevelpment.newsfeeds.databinding.FragmentHomeDetailsBinding
import com.linkdevelpment.newsfeeds.home.data.responseremote.ModelNewsRemoteResponse
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentHomeDetails : BaseFragmentBinding<FragmentHomeDetailsBinding>() {

    private val args: FragmentHomeDetailsArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillArticleData(args.articleModel)
        addListenersOnViews()

    }

    private fun addListenersOnViews() {
        binding.btnOpenWebsite.setOnClickListener {
            val bundle = Bundle().apply {
                putString(Nav.Article.TITLE, args.articleModel.title)
                putString(Nav.Article.URL, args.articleModel.url)
            }
            findNavController().navigate(R.id.fragmentWebView, bundle)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fillArticleData(article: ModelNewsRemoteResponse) {
        article.urlToImage?.let { binding.photo.loadImage(requireContext(), it) }


        if (article.author != null) {
            binding.tvAuthor.text = article.author
        } else {
            binding.tvAuthor.text = ""

        }

        if (article.title != null) {
            binding.tvTitle.text = article.title
        } else {
            binding.tvTitle.text = ""

        }

        if (article.description != null) {
            binding.tvDescription.text = article.description
        } else {
            binding.tvDescription.text = ""

        }

        if (article.publishedAt != null) {
            binding.tvDate.text = article.publishedAt.reFormatDate()
        } else {
            binding.tvDate.text = ""

        }

    }


}