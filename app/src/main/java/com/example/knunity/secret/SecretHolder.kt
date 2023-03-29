package com.example.knunity.secret

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.board.BoardSecretInsideActivity
import com.example.knunity.board.CommentBoardInsideActivity
import com.example.knunity.comment.CommentBoardModel
import com.example.knunity.databinding.MycommentListItemViewBinding
import com.example.knunity.databinding.SecretListItemViewBinding

class SecretHolder(private val binding: SecretListItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context
    fun bind(data: SecretBoardModel) {
        with(binding) {
          titleTv.text=data.title
            contentsTv.text = data.contents
            timeTv.text = data.time
            // uidTv.text =data.uid
            itemView.setOnClickListener{
                val intent = Intent(context, BoardSecretInsideActivity::class.java).apply {
                    putExtra("data",data)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}