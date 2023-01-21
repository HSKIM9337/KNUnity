package com.example.knunity.hotboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.board.ScrapModel
import com.example.knunity.board.ScrapUtil
import com.example.knunity.board.ScrapViewHolder
import com.example.knunity.databinding.LikeListItemViewBinding
import com.example.knunity.databinding.ScrapListItemViewBinding

class LikeRecyclerAdapter : ListAdapter<LikeBoardModel, RecyclerView.ViewHolder>(LikeUtil()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LikeViewHolder) {
            val data = getItem(position) as LikeBoardModel
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = LikeViewHolder(
            LikeListItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return viewHolder
    }
}