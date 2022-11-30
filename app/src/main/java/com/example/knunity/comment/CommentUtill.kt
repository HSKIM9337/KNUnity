package com.example.knunity.comment

import androidx.recyclerview.widget.DiffUtil
import com.example.knunity.comment.CommentModel

class CommentUtill : DiffUtil.ItemCallback<CommentModel>() {
    override fun areItemsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
        return oldItem == newItem
    }

}