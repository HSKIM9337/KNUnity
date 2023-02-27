package com.example.knunity.board

import androidx.recyclerview.widget.DiffUtil

class LikeUtil : DiffUtil.ItemCallback<LikeBoardModel>() {
    override fun areItemsTheSame(oldItem: LikeBoardModel, newItem: LikeBoardModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: LikeBoardModel, newItem: LikeBoardModel): Boolean {
        return oldItem == newItem
    }
}