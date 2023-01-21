package com.example.knunity.hotboard

import androidx.recyclerview.widget.DiffUtil
import com.example.knunity.board.ScrapModel

class LikeUtil : DiffUtil.ItemCallback<LikeBoardModel>() {
    override fun areItemsTheSame(oldItem: LikeBoardModel, newItem: LikeBoardModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: LikeBoardModel, newItem: LikeBoardModel): Boolean {
        return oldItem == newItem
    }
}