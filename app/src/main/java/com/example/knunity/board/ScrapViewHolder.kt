package com.example.knunity.board

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.databinding.ScrapListItemViewBinding

class ScrapViewHolder (private val binding: ScrapListItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context
    fun bind(data: ScrapModel) {
        with(binding) {
            titleSc.text = data.title
            contentsSc.text = data.contents
            timeSc.text = data.time
            // uidTv.text =data.uid
            itemView.setOnClickListener{
                val intent = Intent(context, ScrapInsideActivity::class.java).apply {
                    putExtra("data",data)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}