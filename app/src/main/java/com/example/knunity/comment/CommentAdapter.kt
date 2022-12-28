package com.example.knunity.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.databinding.CommentListItemBinding

class CommentAdapter :
    androidx.recyclerview.widget.ListAdapter<CommentModel, RecyclerView.ViewHolder>(CommentUtill()) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CommentHolder) {
            val data = getItem(position) as CommentModel
            holder.bind(data)
        }
    }
    //override fun getItemCount() = mylistdata.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = CommentHolder(
            CommentListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return viewHolder
    }


}