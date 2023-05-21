package com.example.knunity.Incruit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.crawling.CrawlViewHolder
import com.example.knunity.crawling.Data
import com.example.knunity.databinding.IncruitListItemViewBinding
import com.example.knunity.databinding.ItemListCrawlBinding
import com.example.knunity.databinding.JobListItemViewBinding
import com.example.knunity.job.JobHolder
import com.example.knunity.job.JobModel
import com.example.knunity.job.JobUtill
import java.util.*

class IncruitAdapter: ListAdapter<IncruitModel, RecyclerView.ViewHolder>(IncruitUtill()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder =IncruitHolder(
            IncruitListItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        )
        return viewHolder
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IncruitHolder) {
            val items = getItem(position) as IncruitModel
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