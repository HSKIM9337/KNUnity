package com.example.knunity.board

import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.databinding.BoardListItemViewBinding

class MyViewHolder(private val binding: BoardListItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(data:BoardModel) {
            with(binding) {
                titleTv.text = data.title
                contentsTv.text = data.contents
                timeTv.text = data.time
                uidTv.text =data.uid

            }
        }
}