package com.example.knunity.job

import androidx.recyclerview.widget.DiffUtil
import com.example.knunity.board.BoardModel

class JobUtill  : DiffUtil.ItemCallback<JobModel>() {
    override fun areItemsTheSame(oldItem: JobModel, newItem: JobModel): Boolean {

        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: JobModel, newItem: JobModel): Boolean {

        return oldItem == newItem
    }
}