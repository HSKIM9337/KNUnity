package com.example.knunity.mylist

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.board.BoardMyInsideActivity
import com.example.knunity.board.JobInsideActivity
import com.example.knunity.databinding.JobListItemViewBinding
import com.example.knunity.databinding.MyListItemViewBinding
import com.example.knunity.job.JobModel

class MylistHolder  (private val binding: MyListItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context
    fun bind(data: MylistModel) {
        with(binding) {
            titleMy.text = data.title
            contentsMy.text = data.contents
            timeMy.text = data.time
            // uidTv.text =data.uid
            if(data.what.equals("비밀게시판"))
            {
                nick.text="익명"
            }
            else {
                nick.text = data.nick
            }
            itemView.setOnClickListener{
                val intent = Intent(context, BoardMyInsideActivity::class.java).apply {
                    putExtra("data",data)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}