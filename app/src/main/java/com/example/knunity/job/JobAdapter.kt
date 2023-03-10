package com.example.knunity.job

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.board.BoardModel
import com.example.knunity.board.MyViewHolder
import com.example.knunity.board.MydiffUtil
import com.example.knunity.databinding.BoardListItemViewBinding
import com.example.knunity.databinding.JobListItemViewBinding

class JobAdapter : ListAdapter<JobModel, RecyclerView.ViewHolder>(JobUtill()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is JobHolder) {
            val data = getItem(position) as JobModel
            holder.bind(data)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = JobHolder(
            JobListItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return viewHolder
    }




}