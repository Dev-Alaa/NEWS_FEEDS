package com.linkdevelpment.newsfeeds.home

import android.os.Bundle
import com.linkdevelpment.newsfeeds.R
import com.linkdevelpment.newsfeeds.core.presentation.base.BaseActivity
import com.linkdevelpment.newsfeeds.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}