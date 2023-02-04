package com.example.knunity.comment

import androidx.recyclerview.widget.DiffUtil
import com.example.knunity.hotboard.LikeBoardModel

class MycommentUtill  : DiffUtil.ItemCallback<MycommentModel>() {
    override fun areItemsTheSame(oldItem: MycommentModel, newItem: MycommentModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: MycommentModel, newItem: MycommentModel): Boolean {
        return oldItem == newItem
    }
}