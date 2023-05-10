package com.example.knunity.Incruit

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.board.IncruitInsideActivity
import com.example.knunity.board.JobInsideActivity
import com.example.knunity.databinding.IncruitListItemViewBinding
import com.example.knunity.databinding.JobListItemViewBinding
import com.example.knunity.job.JobModel

class IncruitHolder (private val binding: IncruitListItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context
    fun bind(data: IncruitModel) {
        with(binding) {
            titleTv.text = data.title
            contentsTv.text = data.contents
            timeTv.text = data.time
            // uidTv.text =data.uid
            itemView.setOnClickListener{0
                val intent = Intent(context, IncruitInsideActivity::class.java).apply {
                    putExtra("data",data)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}