package com.example.knunity.job

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.board.BoardInsideActivity
import com.example.knunity.board.BoardModel
import com.example.knunity.board.JobInsideActivity
import com.example.knunity.databinding.BoardListItemViewBinding
import com.example.knunity.databinding.JobListItemViewBinding

class JobHolder (private val binding: JobListItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context
    fun bind(data: JobModel) {
        with(binding) {
            titleTv.text = data.title
            contentsTv.text = data.contents
            timeTv.text = data.time
            // uidTv.text =data.uid
            itemView.setOnClickListener{
                val intent = Intent(context, JobInsideActivity::class.java).apply {
                    putExtra("data",data)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}