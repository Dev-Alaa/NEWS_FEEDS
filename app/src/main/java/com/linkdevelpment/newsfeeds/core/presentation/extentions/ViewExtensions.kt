package com.linkdevelpment.newsfeeds.core.presentation.extentions


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.linkdevelpment.newsfeeds.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


val ViewGroup.layoutInflater: LayoutInflater get() = LayoutInflater.from(this.context)

fun ImageView.loadImage(context: Context, url: String) {
    Glide.with(context).load(url).placeholder(R.drawable.ic_articles_placeholder)
        .error(R.drawable.ic_articles_placeholder).into(this)
}

fun ImageView.loadImageProfile(context: Context, url: String) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    Glide.with(context).load(url).placeholder(circularProgressDrawable)
        .error(R.drawable.profile).into(this)
}

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
fun String.reFormatDate() :String {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    val date: Date = dateFormat.parse(this)

    val dfFormat: DateFormat =
        SimpleDateFormat("MMM d, yyyy")
    return dfFormat.format(date)
}
