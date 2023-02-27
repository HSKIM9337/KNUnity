package com.example.knunity.comment

import androidx.recyclerview.widget.DiffUtil

class MycommentUtill  : DiffUtil.ItemCallback<CommentBoardModel>() {
    override fun areItemsTheSame(oldItem: CommentBoardModel, newItem: CommentBoardModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: CommentBoardModel, newItem: CommentBoardModel): Boolean {
        return oldItem == newItem
    }
}