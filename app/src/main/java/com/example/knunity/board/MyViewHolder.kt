package com.example.knunity.board

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.databinding.BoardListItemViewBinding

class MyViewHolder(private val binding: BoardListItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context
        fun bind(data:BoardModel) {
            with(binding) {
                titleTv.text = data.title
                contentsTv.text = data.contents
                timeTv.text = data.time
//                likeCountTv.text=data.likCount
//                commentCountTv.text=data.comCount
//                scrapCountTv.text=data.scrCount
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