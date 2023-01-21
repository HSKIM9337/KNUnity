package com.example.knunity.board

import androidx.recyclerview.widget.DiffUtil

class ScrapUtil : DiffUtil.ItemCallback<ScrapModel>() {
    override fun areItemsTheSame(oldItem: ScrapModel, newItem: ScrapModel): Boolean {

        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ScrapModel, newItem: ScrapModel): Boolean {
        return oldItem == newItem
    }
}