package com.linkdevelpment.newsfeeds.home.data.responseremote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelNewsRemoteResponse(
    val author: String?,
    val description: String?,
    val publishedAt: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
): Parcelable