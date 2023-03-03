package com.example.knunity.crawling

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.databinding.ItemListCrawlBinding
import java.util.*

class CrawlRecyclerAdapter : ListAdapter<Data, RecyclerView.ViewHolder>(CrawlDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = CrawlViewHolder(
            ItemListCrawlBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        )
        return viewHolder
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CrawlViewHolder) {
            val items = getItem(position) as Data
            holder.bind(items)
        }

    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val newList = currentList.toMutableList()
        Collections.swap(newList, fromPosition, toPosition)
        submitList(newList)
    }

    fun removeItem(position: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(position)
        submitList(newList)
    }


}