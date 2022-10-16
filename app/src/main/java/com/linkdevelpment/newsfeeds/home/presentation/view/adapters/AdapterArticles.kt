package com.linkdevelpment.newsfeeds.home.presentation.view.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.linkdevelpment.newsfeeds.core.presentation.extentions.layoutInflater
import com.linkdevelpment.newsfeeds.core.presentation.extentions.loadImage
import com.linkdevelpment.newsfeeds.core.presentation.extentions.reFormatDate
import com.linkdevelpment.newsfeeds.databinding.RvNewsBinding
import com.linkdevelpment.newsfeeds.home.data.responseremote.ModelNewsRemoteResponse

class AdapterArticles(private val onClickedItem: (ModelNewsRemoteResponse) -> Unit) :
    ListAdapter<ModelNewsRemoteResponse, RecyclerView.ViewHolder>(
        diffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            RvNewsBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        getItem(position)?.let {
            (holder as ViewHolder).bind(it)
        }
    }

    private inner class ViewHolder(private val binding: RvNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(item: ModelNewsRemoteResponse) = with(binding) {
            binding.root.setOnClickListener {
                onClickedItem.invoke(item)
            }
            item.urlToImage?.let { photo.loadImage(root.context, it) }


            if (item.author != null) {
                tvAuthor.text = item.author
            } else {
                tvAuthor.text = ""

            }

            if (item.title != null) {
                tvTitle.text = item.title
            } else {
                tvTitle.text = ""

            }

            if (item.publishedAt != null) {
                tvDate.text = item.publishedAt.reFormatDate()
            } else {
                tvDate.text = ""

            }

        }

    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ModelNewsRemoteResponse>() {
            override fun areItemsTheSame(
                oldItem: ModelNewsRemoteResponse,
                newItem: ModelNewsRemoteResponse
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ModelNewsRemoteResponse,
                newItem: ModelNewsRemoteResponse
            ): Boolean {
                return oldItem == newItem

            }
        }
    }
}