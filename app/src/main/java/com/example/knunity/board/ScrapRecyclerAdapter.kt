package com.example.knunity.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.databinding.ScrapListItemViewBinding

class ScrapRecyclerAdapter : ListAdapter<ScrapModel, RecyclerView.ViewHolder>(ScrapUtil()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ScrapViewHolder) {
            val data = getItem(position) as ScrapModel
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = ScrapViewHolder(
            ScrapListItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return viewHolder
    }
}