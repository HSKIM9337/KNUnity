package com.example.knunity.secret

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.board.*
import com.example.knunity.databinding.SecretListItemViewBinding

class SecretAdapter : ListAdapter<SecretBoardModel, RecyclerView.ViewHolder>(SecretUtill()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SecretHolder) {
            val data = getItem(position) as SecretBoardModel
            holder.bind(data)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = SecretHolder(
            SecretListItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return viewHolder
    }

}