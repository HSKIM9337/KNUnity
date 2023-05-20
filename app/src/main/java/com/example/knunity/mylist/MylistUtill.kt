package com.example.knunity.mylist

import androidx.recyclerview.widget.DiffUtil


class MylistUtill : DiffUtil.ItemCallback<MylistModel>() {
    override fun areItemsTheSame(oldItem: MylistModel, newItem: MylistModel): Boolean {

        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: MylistModel, newItem: MylistModel): Boolean {

        return oldItem == newItem
    }
}