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
            numTv.text = data.Num
            contentsTv.text = data.title
            // uidTv.text =data.uid
//            itemView.setOnClickListener{
//
//            }
        }
    }
    fun setAlpha(alpha: Float) {
        with(binding) {
            numTv.alpha = alpha
            contentsTv.alpha = alpha

        }
    }
}