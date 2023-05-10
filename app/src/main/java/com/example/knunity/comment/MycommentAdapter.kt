package com.example.knunity.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.databinding.MycommentListItemViewBinding

class MycommentAdapter: ListAdapter<CommentBoardModel, RecyclerView.ViewHolder>(MycommentUtill()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MycommentHolder) {
            val data = getItem(position) as CommentBoardModel
            holder.bind(data)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = MycommentHolder(
            MycommentListItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false

            )
        )
        return viewHolder
    }
}