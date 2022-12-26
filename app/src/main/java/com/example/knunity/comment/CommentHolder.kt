package com.example.knunity.comment

import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.comment.CommentModel
import com.example.knunity.databinding.CommentListItemBinding

class CommentHolder(private val binding: CommentListItemBinding)
    :RecyclerView.ViewHolder(binding.root)
{
    private val context2 = binding.root.context
    fun bind(data:CommentModel) {
        with(binding) {
            titleCo.text = data.commentTitle
            timeCo.text=data.commentCreatedTime
            uidCo.text=data.commenteryUid


//            titleTv.text = data.title
//            contentsTv.text = data.contents
//            timeTv.text = data.time
//            uidTv.text =data.uid
//            itemView.setOnClickListener{
//                val intent = Intent(context, BoardInsideActivity::class.java).apply {
//                    putExtra("data",data)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }.run { context.startActivity(this) }
//            }
        }
    }
}