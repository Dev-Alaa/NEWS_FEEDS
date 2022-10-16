package com.linkdevelpment.newsfeeds.home.presentation.view.fragments

import android.annotation.SuppressLint

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.linkdevelpment.newsfeeds.core.presentation.base.BaseFragmentBinding
import com.linkdevelpment.newsfeeds.databinding.FragmentWebSiteWebViewBinding


class FragmentWebSiteWebView : BaseFragmentBinding<FragmentWebSiteWebViewBinding>() {
    private val args: FragmentWebSiteWebViewArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()
        initialize()
    }

    private fun initialize() {
        showWebView(args.url)
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun showWebView(mURL: String) {
        binding.webView.loadUrl(mURL)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                dismissLoading()
            }
        }
    }

}


