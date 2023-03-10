package com.example.knunity.Incruit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.databinding.IncruitListItemViewBinding
import com.example.knunity.databinding.JobListItemViewBinding
import com.example.knunity.job.JobHolder
import com.example.knunity.job.JobModel
import com.example.knunity.job.JobUtill

class IncruitAdapter : ListAdapter<IncruitModel, RecyclerView.ViewHolder>(IncruitUtill()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IncruitHolder) {
            val data = getItem(position) as IncruitModel
            holder.bind(data)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = IncruitHolder(
            IncruitListItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return viewHolder
    }


}