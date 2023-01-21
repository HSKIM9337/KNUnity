package com.example.knunity.hotboard

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.board.BoardInsideActivity
import com.example.knunity.board.ScrapModel
import com.example.knunity.databinding.LikeListItemViewBinding
import com.example.knunity.databinding.ScrapListItemViewBinding

class LikeViewHolder (private val binding: LikeListItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context
    fun bind(data: LikeBoardModel) {
        with(binding) {
            titleLi.text = data.title
            contentsLi.text = data.contents
            timeLi.text = data.time
            // uidTv.text =data.uid
            itemView.setOnClickListener{
                val intent = Intent(context, BoardInsideActivity::class.java).apply {
                    putExtra("data",data)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}