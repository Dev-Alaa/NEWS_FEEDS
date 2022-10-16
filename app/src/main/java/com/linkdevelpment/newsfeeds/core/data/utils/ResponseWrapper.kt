package com.linkdevelpment.newsfeeds.core.data.utils

import com.google.gson.annotations.SerializedName

data class WrappedListResponse<T> (
    var code: Int,
    @SerializedName("message") var message : String,
    @SerializedName("status") var status : Boolean,
    @SerializedName("errors") var errors : List<String>? = null,
    @SerializedName("data") var data : List<T>? = null
)

data class WrappedResponse<T> (
    var code: Int,
    @SerializedName("status") var status : String,
    @SerializedName("source") var source : String,
    @SerializedName("sortBy") var sortBy : String,
    @SerializedName("articles") var articles : List<T>? = null
)

