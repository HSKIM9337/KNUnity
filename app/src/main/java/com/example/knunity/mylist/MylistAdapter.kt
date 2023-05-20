package com.example.knunity.mylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.databinding.JobListItemViewBinding
import com.example.knunity.databinding.MyListItemViewBinding
import com.example.knunity.job.JobHolder
import com.example.knunity.job.JobModel
import com.example.knunity.job.JobUtill

class MylistAdapter : ListAdapter<MylistModel, RecyclerView.ViewHolder>(MylistUtill()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MylistHolder) {
            val data = getItem(position) as MylistModel
            holder.bind(data)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = MylistHolder(
            MyListItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return viewHolder
    }

}