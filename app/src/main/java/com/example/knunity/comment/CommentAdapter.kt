package com.example.knunity.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.R
import com.example.knunity.databinding.CommentListItemBinding
import com.example.knunity.utils.FBRef

class CommentAdapter() :
    androidx.recyclerview.widget.ListAdapter<CommentModel, RecyclerView.ViewHolder>(CommentUtill()) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CommentHolder) {
            val data = getItem(position) as CommentModel
            holder.bind(data)
                      }
        }


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