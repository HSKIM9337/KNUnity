package com.example.knunity.board
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.knunity.R
//import com.example.knunity.databinding.BoardListItemViewBinding
//import kotlinx.android.synthetic.main.board_list_item_view.view.*
//
//class RecyclerAdapter(private val items: ArrayList<BoardModel>) :
//    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
//
//    inner class ViewHolder(private val binding: BoardListItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(data:BoardModel) {
//            binding.titleTv.text = "${data.title}"
//            binding.contentsTv.text = "${data.contents}"
//
//        }
//
//    }
//        override fun getItemCount(): Int {
//            return items.size
//        }
//
//        override fun onCreateViewHolder(
//            parent: ViewGroup,
//            viewType: Int
//        ): RecyclerAdapter.ViewHolder {
//            val binding = BoardListItemViewBinding.inflate(LayoutInflater.from(parent.context),parent
//                , false)
//
//            return ViewHolder(binding)
//        }
//
//        override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
//            holder.bind(items[position])
//        }
//    }
//
