package com.example.knunity.board

import androidx.recyclerview.widget.DiffUtil

class MydiffUtil : DiffUtil.ItemCallback<BoardModel>() {
    override fun areItemsTheSame(oldItem: BoardModel, newItem: BoardModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: BoardModel, newItem: BoardModel): Boolean {
        return oldItem == newItem
    }
}