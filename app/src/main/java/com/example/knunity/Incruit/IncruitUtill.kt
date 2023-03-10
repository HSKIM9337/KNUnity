package com.example.knunity.Incruit

import androidx.recyclerview.widget.DiffUtil
import com.example.knunity.job.JobModel

class IncruitUtill : DiffUtil.ItemCallback<IncruitModel>() {
    override fun areItemsTheSame(oldItem: IncruitModel, newItem: IncruitModel): Boolean {

        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: IncruitModel, newItem: IncruitModel): Boolean {

        return oldItem == newItem
    }
}