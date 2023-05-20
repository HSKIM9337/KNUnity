package com.example.knunity.comment

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.board.BoardInsideActivity
import com.example.knunity.board.CommentBoardInsideActivity
import com.example.knunity.databinding.MycommentListItemViewBinding

class MycommentHolder (private val binding: MycommentListItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context
    fun bind(data:CommentBoardModel) {
        with(binding) {
            titleMyco.text = data.title
            contentsMyco.text = data.contents
            timeMyco.text = data.time
            if(data.what.equals("비밀게시판"))
            {
                nick.text="익명"
            }
            else {
                nick.text = data.nick
            }
            // uidTv.text =data.uid
            itemView.setOnClickListener{
                val intent = Intent(context, CommentBoardInsideActivity::class.java).apply {
                    putExtra("data",data)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}