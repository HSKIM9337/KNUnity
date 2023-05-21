package com.example.knunity.crawling

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.databinding.ItemListCrawlBinding

class CrawlViewHolder (private val binding: ItemListCrawlBinding): RecyclerView.ViewHolder(binding.root) {

    val imageselect : ImageView = binding.ivDelete
    init {
        imageselect.setOnClickListener {

        }
    }

    fun bind(item:Data) {
        with(binding) {
            tvId.text = item.Num
            tvTitle.text = item.title

        }

    }
    fun setAlpha(alpha: Float) {
        with(binding) {
            tvId.alpha = alpha
            tvTitle.alpha = alpha

        }
    }

}