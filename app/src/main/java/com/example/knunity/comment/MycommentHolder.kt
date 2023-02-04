package com.example.knunity.comment

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.board.BoardInsideActivity
import com.example.knunity.databinding.LikeListItemViewBinding
import com.example.knunity.databinding.MycommentListItemViewBinding
import com.example.knunity.hotboard.LikeBoardModel

class MycommentHolder (private val binding: MycommentListItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context
    fun bind(data: MycommentModel) {
        with(binding) {
            titleMyco.text = data.title
            contentsMyco.text = data.contents
            timeMyco.text = data.time
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